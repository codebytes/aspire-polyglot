from concurrent.futures import ThreadPoolExecutor
from unittest import skipUnless

from django.db import close_old_connections, connection
from django.test import Client, TestCase, TransactionTestCase
from django.urls import reverse

from .models import Choice, Poll


class PollTests(TestCase):
    def setUp(self):
        self.poll = Poll.objects.create(question="Test poll")
        self.first = Choice.objects.create(poll=self.poll, text="First", votes=2)
        self.second = Choice.objects.create(poll=self.poll, text="Second", votes=1)

    def test_detail_and_partial_render_the_same_results(self):
        for name in ("detail", "results_partial"):
            response = self.client.get(reverse(name, args=[self.poll.id]))
            self.assertEqual(response.status_code, 200)
            self.assertEqual(response.context["total_votes"], 3)
            self.assertEqual(len(response.context["choices_with_percentages"]), 2)
            self.assertContains(response, "2 votes")
            self.assertContains(response, "66.7%")

    def test_vote_persists_and_is_visible_after_reload(self):
        response = self.client.post(reverse("vote", args=[self.poll.id, self.second.id]))
        self.assertEqual(response.context["total_votes"], 4)
        self.second.refresh_from_db()
        self.assertEqual(self.second.votes, 2)
        response = self.client.get(reverse("detail", args=[self.poll.id]))
        self.assertContains(response, 'style="width: 50.0%"', count=2)
        self.assertEqual(
            [item["percentage"] for item in response.context["choices_with_percentages"]],
            [50.0, 50.0],
        )

    def test_creation_preserves_more_than_nine_choices(self):
        data = {"question": "  Ten choices  "}
        data.update({f"choice_{i}": f"Option {i}" for i in range(1, 11)})
        response = self.client.post(reverse("create"), data)
        self.assertEqual(response.status_code, 302)
        poll = Poll.objects.get(question="Ten choices")
        self.assertEqual(poll.choices.count(), 10)
        self.assertEqual(poll.choices.last().text, "Option 10")

    def test_invalid_poll_is_rejected_without_partial_records(self):
        response = self.client.post(reverse("create"), {
            "question": "Incomplete", "choice_1": "Only choice", "choice_2": "  ",
        })
        self.assertEqual(response.status_code, 400)
        self.assertFalse(Poll.objects.filter(question="Incomplete").exists())

    def test_dynamic_choice_numbers_are_validated(self):
        response = self.client.get(reverse("add_choice_input"), {"num": "10"})
        self.assertContains(response, 'name="choice_10"')
        for value in ("invalid", "0", "-1"):
            response = self.client.get(reverse("add_choice_input"), {"num": value})
            self.assertEqual(response.status_code, 400)


@skipUnless(connection.vendor == "postgresql", "Concurrency check requires PostgreSQL")
class ConcurrentVoteTests(TransactionTestCase):
    def test_concurrent_votes_are_not_lost(self):
        poll = Poll.objects.create(question="Concurrent")
        choice = Choice.objects.create(poll=poll, text="Choice")
        url = reverse("vote", args=[poll.id, choice.id])

        def vote(_):
            try:
                return Client().post(url).status_code
            finally:
                close_old_connections()

        with ThreadPoolExecutor(max_workers=8) as pool:
            statuses = list(pool.map(vote, range(24)))
        self.assertEqual(statuses, [200] * 24)
        choice.refresh_from_db()
        self.assertEqual(choice.votes, 24)

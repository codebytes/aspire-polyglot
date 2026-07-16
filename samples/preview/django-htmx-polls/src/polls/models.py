from django.db import models


class Poll(models.Model):
    """A poll question with multiple choice answers."""
    question = models.CharField(max_length=200)
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.question

    class Meta:
        ordering = ['-created_at']


class Choice(models.Model):
    """A choice for a poll question."""
    poll = models.ForeignKey(Poll, on_delete=models.CASCADE, related_name='choices')
    text = models.CharField(max_length=100)
    votes = models.IntegerField(default=0)

    def __str__(self):
        return f"{self.text} ({self.votes} votes)"

    class Meta:
        ordering = ['id']

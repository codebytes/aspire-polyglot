from django.core.management.base import BaseCommand
from polls.models import Poll, Choice


class Command(BaseCommand):
    help = 'Seeds the database with sample polls if empty'

    def handle(self, *args, **options):
        # Check if polls already exist
        if Poll.objects.exists():
            self.stdout.write(self.style.SUCCESS('Database already has polls, skipping seed.'))
            return

        self.stdout.write('Seeding database with sample polls...')

        # Poll 1: Favorite Programming Language
        poll1 = Poll.objects.create(question="What's your favorite programming language?")
        Choice.objects.create(poll=poll1, text="Python", votes=5)
        Choice.objects.create(poll=poll1, text="JavaScript", votes=3)
        Choice.objects.create(poll=poll1, text="C#", votes=4)
        Choice.objects.create(poll=poll1, text="Go", votes=2)
        Choice.objects.create(poll=poll1, text="Rust", votes=1)

        # Poll 2: Best Web Framework
        poll2 = Poll.objects.create(question="Which web framework do you prefer?")
        Choice.objects.create(poll=poll2, text="Django", votes=8)
        Choice.objects.create(poll=poll2, text="Flask", votes=4)
        Choice.objects.create(poll=poll2, text="FastAPI", votes=6)
        Choice.objects.create(poll=poll2, text="Express.js", votes=5)

        # Poll 3: Development Environment
        poll3 = Poll.objects.create(question="What's your primary development environment?")
        Choice.objects.create(poll=poll3, text="VS Code", votes=12)
        Choice.objects.create(poll=poll3, text="PyCharm", votes=6)
        Choice.objects.create(poll=poll3, text="Visual Studio", votes=4)
        Choice.objects.create(poll=poll3, text="Vim/Neovim", votes=3)
        Choice.objects.create(poll=poll3, text="Sublime Text", votes=1)

        self.stdout.write(self.style.SUCCESS('Successfully seeded 3 polls with choices!'))

import re

from django.db import transaction
from django.db.models import F
from django.shortcuts import render, get_object_or_404, redirect
from django.http import HttpResponse
from django.views.decorators.http import require_POST
from .models import Poll, Choice


def index(request):
    """List all polls."""
    polls = Poll.objects.all()
    return render(request, 'polls/index.html', {'polls': polls})


def detail(request, poll_id):
    """Show poll detail with voting buttons."""
    poll = get_object_or_404(Poll, pk=poll_id)
    return render(request, 'polls/detail.html', _results_context(poll))


@require_POST
def vote(request, poll_id, choice_id):
    """Handle vote submission and return updated results partial."""
    poll = get_object_or_404(Poll, pk=poll_id)
    choice = get_object_or_404(Choice, pk=choice_id, poll=poll)
    
    Choice.objects.filter(pk=choice.pk).update(votes=F('votes') + 1)
    
    # Return HTMX partial with updated results
    return results_partial(request, poll_id)


def results_partial(request, poll_id):
    """Return HTMX partial showing poll results."""
    poll = get_object_or_404(Poll, pk=poll_id)
    
    return render(request, 'polls/results_partial.html', _results_context(poll))


def _results_context(poll):
    choices = list(poll.choices.all())
    total_votes = sum(choice.votes for choice in choices)
    
    # Calculate percentages for each choice
    choices_with_percentages = []
    for choice in choices:
        percentage = (choice.votes / total_votes * 100) if total_votes > 0 else 0
        choices_with_percentages.append({
            'choice': choice,
            'percentage': percentage
        })
    
    return {
        'poll': poll,
        'total_votes': total_votes,
        'choices_with_percentages': choices_with_percentages
    }


def create(request):
    """Create a new poll with choices."""
    if request.method == 'POST':
        question = request.POST.get('question', '').strip()
        choice_texts = [
            value.strip()
            for name, value in request.POST.items()
            if re.fullmatch(r'choice_[1-9][0-9]*', name) and value.strip()
        ]
        if not question or len(question) > 200 or len(choice_texts) < 2 or any(
            len(text) > 100 for text in choice_texts
        ):
            return render(request, 'polls/create.html', {
                'error': 'Enter a question (up to 200 characters) and at least two choices (up to 100 characters each).',
            }, status=400)

        with transaction.atomic():
            poll = Poll.objects.create(question=question)
            Choice.objects.bulk_create([
                Choice(poll=poll, text=text) for text in choice_texts
            ])
        return redirect('detail', poll_id=poll.id)
    
    return render(request, 'polls/create.html')


def add_choice_input(request):
    """HTMX endpoint to add another choice input field."""
    # Get the next choice number from the request
    try:
        choice_num = int(request.GET.get('num', 2))
    except ValueError:
        return HttpResponse('Choice number must be a positive integer.', status=400)
    if choice_num < 1:
        return HttpResponse('Choice number must be a positive integer.', status=400)
    return HttpResponse(
        f'<div class="choice-input">'
        f'<input type="text" name="choice_{choice_num}" '
        f'placeholder="Choice {choice_num}" class="input">'
        f'</div>'
    )

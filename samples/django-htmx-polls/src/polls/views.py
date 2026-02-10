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
    return render(request, 'polls/detail.html', {'poll': poll})


@require_POST
def vote(request, poll_id, choice_id):
    """Handle vote submission and return updated results partial."""
    poll = get_object_or_404(Poll, pk=poll_id)
    choice = get_object_or_404(Choice, pk=choice_id, poll=poll)
    
    # Increment vote count
    choice.votes += 1
    choice.save()
    
    # Return HTMX partial with updated results
    return results_partial(request, poll_id)


def results_partial(request, poll_id):
    """Return HTMX partial showing poll results."""
    poll = get_object_or_404(Poll, pk=poll_id)
    
    # Calculate total votes
    total_votes = sum(choice.votes for choice in poll.choices.all())
    
    # Calculate percentages for each choice
    choices_with_percentages = []
    for choice in poll.choices.all():
        percentage = (choice.votes / total_votes * 100) if total_votes > 0 else 0
        choices_with_percentages.append({
            'choice': choice,
            'percentage': percentage
        })
    
    return render(request, 'polls/results_partial.html', {
        'poll': poll,
        'total_votes': total_votes,
        'choices_with_percentages': choices_with_percentages
    })


def create(request):
    """Create a new poll with choices."""
    if request.method == 'POST':
        question = request.POST.get('question')
        
        if question:
            # Create poll
            poll = Poll.objects.create(question=question)
            
            # Create choices
            choice_texts = [
                request.POST.get(f'choice_{i}')
                for i in range(1, 10)
                if request.POST.get(f'choice_{i}')
            ]
            
            for text in choice_texts:
                Choice.objects.create(poll=poll, text=text)
            
            return redirect('detail', poll_id=poll.id)
    
    return render(request, 'polls/create.html')


def add_choice_input(request):
    """HTMX endpoint to add another choice input field."""
    # Get the next choice number from the request
    choice_num = int(request.GET.get('num', 2))
    return HttpResponse(
        f'<div class="choice-input">'
        f'<input type="text" name="choice_{choice_num}" '
        f'placeholder="Choice {choice_num}" class="input">'
        f'</div>'
    )

// Shared score state for the whole game
let score = 0;
let total_questions = 0;

document.addEventListener('DOMContentLoaded', () => {
    const root = document.getElementById('app');
    const play = document.getElementById('play');
    const exitButton = document.getElementById('exit-button');

    // Start the game when Play is clicked
    play.addEventListener('click', async () => {
        play.style.display = 'none';
        score = 0;
        total_questions = 0;

        try {
            const res = await fetch('/api/questions');
            if (!res.ok) throw new Error('HTTP ' + res.status);

            const questions = await res.json();

            const startIndex = 78;
            loadQuestion(startIndex, questions, root);

        } catch (e) {
            root.innerHTML = `
        <p>Failed to load question: ${e}</p>
        <p>Please make sure the server is running and try opening <code>/api/questions</code> directly.</p>
      `;
        }
    });

    // Exit button: save the session, then go home
    if (exitButton) {
        exitButton.addEventListener('click', async (event) => {
            event.preventDefault();

            const data = {
                score: score,
                total_questions: total_questions
            };

            try {
                const res = await fetch('/api/sessions', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                });

                if (!res.ok) {
                    console.error('Failed to save session:', res.status);
                    alert('Could not save your score (status ' + res.status + ').');
                } else {
                    window.location.href = '/';
                }

            } catch (err) {
                console.error('Error saving session:', err);
                alert('An error occurred while saving your score.');
            }
        });
    }
});

/**
 * Render a question, handle answer selection, scoring, and navigation.
 */
function loadQuestion(index, questions, root) {
    const q = questions[index];
    const optionTexts = [q.optionA, q.optionB, q.optionC, q.optionD];

    root.innerHTML = `
    <div id="question">
      <h3>${q.text}</h3>
      <ol id="choices"></ol>
      <button id="submit" disabled>Submit</button><br/>
      <button id="next" disabled>Next</button>
    </div>
    <p id="result"></p>
  `;

    const question = document.getElementById('question');
    const choicesEl = document.getElementById('choices');
    const submit = document.getElementById('submit');
    const next = document.getElementById('next');
    const result = document.getElementById('result');

    // Build choices list
    optionTexts.forEach((text) => {
        const li = document.createElement('li');
        li.innerHTML = `<label><input type="radio" name="ans" value="${text}">${text}</label>`;
        choicesEl.appendChild(li);
    });

    // Enable submit when a choice is selected
    choicesEl.addEventListener('change', () => {
        submit.disabled = false;
    });

    // Handle grading
    submit.addEventListener('click', () => {
        next.disabled = false;

        const sel = document.querySelector('input[name="ans"]:checked');
        if (!sel) return;

        const selAnswer = sel.value;

        if (selAnswer === q.correctAnswer) {
            result.textContent = '✅ Correct! Nice job.';
            score++;
        } else {
            result.textContent = `❌ Not quite. Correct answer: ${q.correctAnswer}`;
        }

        total_questions++;
        submit.disabled = true;

        // Disable further changes
        choicesEl.querySelectorAll('input[type="radio"]').forEach((radio) => {
            radio.disabled = true;
        });
    });

    // Handle moving to the next question
    next.addEventListener('click', () => {
        if (index + 1 < questions.length) {
            next.disabled = true;
            loadQuestion(index + 1, questions, root);
        } else {
            // End of quiz
            question.style.display = 'none';
            submit.style.display = 'none';
            next.style.display = 'none';
            result.textContent = `Your Score is ${score}/${total_questions}`;
        }
    });
}

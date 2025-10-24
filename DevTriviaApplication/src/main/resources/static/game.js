// I am waiting for the DOM to be fully loaded before I run any of my code.
document.addEventListener('DOMContentLoaded', async () => {
    // This references the main content area where I will display the question and choices.
    const root = document.getElementById('app');

    try {

        // I am requesting JSON data from the server endpoint that returns trivia questions.
        // Because I used a relative path (/api/questions), it will call the same host and port that served this page.
        const res = await fetch('/api/questions');

        // If the server response is not OK (like 404 or 500), I will throw an error to handle below.
        if (!res.ok) throw new Error('HTTP ' + res.status);

        // I am converting the response body into a JavaScript array of questions.
        const questions = await res.json();

        let index = 0;
        loadQuestion(index , questions, root);


        // For this demo, I will use the first question in the array.
        /*
        let index = 0
        //for (index; index < questions.length; i++) {

        const q = questions[index];
        const optionTexts = [q.optionA, q.optionB, q.optionC, q.optionD];


        // I am replacing the loading message with the question UI.
        // It includes the question text, a list of choices with radio buttons, a submit button, and a result area.

        root.innerHTML = `
              <h2>${q.text}</h2>
              <ol id="choices"></ol>
              <button id="submit" disabled>Submit</button></br>
              <button id="next" disabled>Next</button>

              <p id="result"></p>
              `;


        const choicesEl = document.getElementById('choices');
        const submit = document.getElementById('submit');
        const next = document.getElementById('next')
        const result = document.getElementById('result');


        // Use Object.values() to iterate over the array of option TEXTS
        optionTexts.forEach((text) => {
            const li = document.createElement('li');
            // We use the index for the radio button value, as intended.
            const radioValue = encodeURIComponent(text);
            li.innerHTML = `<label><input type="radio" name="ans" value="${radioValue}"> ${text}</label>`;
            choicesEl.appendChild(li);
        });

        choicesEl.addEventListener('change', () => {
            submit.disabled = false;
        });

        // When the user clicks Submit, I will check whether their selected index matches the correct answer index.
        submit.addEventListener('click', () => {
            next.disabled = false;
            // This finds whichever radio button is currently selected.
            const sel = document.querySelector('input[name="ans"]:checked');
            const selAnswer = decodeURIComponent(sel.value);

            // If the chosen index is the same as the correct index, I will show a success message.
            // Otherwise, I will display which choice was actually correct.
            if (selAnswer === q.correctAnswer) {
                result.textContent = "✅ Correct! Nice job.";
            } else {
                result.textContent = `❌ Not quite. Correct answer: ${q.correctAnswer}`;
            }
            submit.disabled = true;
            // Disable all radio buttons after submission.
            choicesEl.querySelectorAll('input[type="radio"]').forEach(radio => radio.disabled = true);
        });

        next.addEventListener('click', () => {
            index += 1
            next.disabled = true;
        });

         */
    }
    catch
        (e)
    {
        // If anything goes wrong (for example, the server is not running), I will show an error message on the page.
        // This helps me diagnose problems without opening the browser console.
        root.innerHTML = `<p>Failed to load question: ${e}</p>
                        <p>Please make sure the server is running and try opening <code>api/questions</code> directly.</p>`;
    }



});

function loadQuestion(index, questions, root) {
    const q = questions[index];
    const optionTexts = [q.optionA, q.optionB, q.optionC, q.optionD];


    // I am replacing the loading message with the question UI.
    // It includes the question text, a list of choices with radio buttons, a submit button, and a result area.

    root.innerHTML = `
              <h2>${q.text}</h2>
              <ol id="choices"></ol>
              <button id="submit" disabled>Submit</button></br>
              <button id="next" disabled>Next</button>

              <p id="result"></p>
              `;


    const choicesEl = document.getElementById('choices');
    const submit = document.getElementById('submit');
    const next = document.getElementById('next')
    const result = document.getElementById('result');


    // Use Object.values() to iterate over the array of option TEXTS
    optionTexts.forEach((text) => {
        const li = document.createElement('li');
        // We use the index for the radio button value, as intended.
        const radioValue = text;
        li.innerHTML = `<label><input type="radio" name="ans" value="${radioValue}"></label>`;
        choicesEl.appendChild(li);

        const label = li.querySelector('label');
        label.appendChild(document.createTextNode(text));
    });

    choicesEl.addEventListener('change', () => {
        submit.disabled = false;
    });

    // When the user clicks Submit, I will check whether their selected index matches the correct answer index.
    submit.addEventListener('click', () => {
        next.disabled = false;
        // This finds whichever radio button is currently selected.
        const sel = document.querySelector('input[name="ans"]:checked');
        const selAnswer = decodeURIComponent(sel.value);

        // If the chosen index is the same as the correct index, I will show a success message.
        // Otherwise, I will display which choice was actually correct.
        if (selAnswer === q.correctAnswer) {
            result.textContent = "✅ Correct! Nice job.";
        } else {
            result.textContent = `❌ Not quite. Correct answer: ${q.correctAnswer}`;
        }
        submit.disabled = true;
        // Disable all radio buttons after submission.
        choicesEl.querySelectorAll('input[type="radio"]').forEach(radio => radio.disabled = true);
    });

    next.addEventListener('click', () => {
        next.disabled = true;
        loadQuestion(index+1, questions, root)
    });
}
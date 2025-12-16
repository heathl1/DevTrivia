document.addEventListener('DOMContentLoaded', async () => {
    await reloadAccount(); // reload account everytime the page is accessed
});

// reloads account so that games played after user logs in are accounted for on the account screen
async function reloadAccount() {
    const greetingEl = document.getElementById("greeting");
    const statsEl = document.getElementById("stats");
    const sessionsEl = document.getElementById("sessions");

    greetingEl.textContent = await greetUser(); // display personalized greeting

    await loadStats(statsEl); // load user stats
    await loadSessions(sessionsEl); // load recent sessions
}

async function greetUser() {
    const response = await fetch("/user/details", { cache: "no-store" });

    if (!response.ok) { // displays general greeting if user isn't found (for example, guest user)
        return "Hello, User";
    }
    const data = await response.json(); // user details response
    if (!data.user) {
        return "Hello, User"; // displays general greeting if user data isn't found
    }
    return `Hello, ${data.user.username}`; // if all data is found, custom greeting will be displayed
}

async function loadStats(root) {
    const response = await fetch("/user/details", { cache: "no-store" });
    if (!response.ok) { // error message if stats can't be loaded
        root.innerHTML = "<p>Could not load stats.</p>";
        return;
    }

    const data = await response.json(); // user data json format
    const user = data.user;
    if (!user) { // check to make sure there is a user logged in
        root.innerHTML = "<p>No user logged in.</p>";
        return;
    }

    // retreive and display user info
    root.innerHTML = `
        <h3>Your Stats</h3>
        <table>
            <tr><th>Games Played</th><td>${user.gamesPlayed}</td></tr>
            <tr><th>Total Correct</th><td>${user.totalCorrect}</td></tr>
            <tr><th>Join Date</th><td>${user.joinDate}</td></tr>
        </table>
    `;
}

// retreives and displays user sessions
async function loadSessions(root) {
    const userDetails = await fetch("/user/details", { cache: "no-store" });
    // check for the logged in user
    if (!userDetails.ok) {
        root.innerHTML = "<p>Could not load sessions (user).</p>";
        return;
    }
    const userData = await userDetails.json();
    const user = userData.user;
    if (!user) {
        root.innerHTML = "<p>No user logged in.</p>";
        return;
    }

    const userId = user.id;

    // fetch all sessions
    const sessionResponse = await fetch("/api/sessions", { cache: "no-store" });
    if (!sessionResponse.ok) {
        root.innerHTML = "<p>Could not load sessions.</p>";
        return;
    }

    const allSessions = await sessionResponse.json();

    const userSessions = Array.isArray(allSessions)
        // filter sessions so they match current users userId and
        ? allSessions
            .filter(s =>
                s.userId === userId ||
                (s.user && s.user.id === userId)
            )
            .sort((a, b) => { // sort sessions
                const da = new Date(a.completionDate || a.completion_date || 0);
                const db = new Date(b.completionDate || b.completion_date || 0);
                return db - da;
            })
            .slice(0, 10) // show only the 10 most recent sessions
        : [];

    let rows = "";

    if (userSessions.length === 0) { // checks to see if there are any sessions yet
        rows = `<tr><td colspan="3">No sessions yet.</td></tr>`;
    } else { // create a new table row for each session
        rows = userSessions.map(s => `
            <tr>
                <td>${s.score}</td>
                <td>${s.totalQuestions ?? s.total_questions}</td>
                <td>${s.completionDate ?? s.completion_date}</td>
            </tr>
        `).join("");
    }

    // display session table
    root.innerHTML = `
        <h3>Recent Sessions</h3>
        <table border="1">
            <thead>
                <tr><th>Score</th><th>Total Questions</th><th>Date</th></tr>
            </thead>
            <tbody>${rows}</tbody>
        </table>
    `;
}
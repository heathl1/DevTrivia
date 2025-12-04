document.addEventListener('DOMContentLoaded', async () => {
    await reloadAccount();
});

async function reloadAccount() {
    const greetingEl = document.getElementById("greeting");
    const statsEl = document.getElementById("stats");
    const sessionsEl = document.getElementById("sessions");

    const message = await greetUser();
    greetingEl.textContent = message;

    await loadStats(statsEl);
    await loadSessions(sessionsEl);
}

async function greetUser() {
    const response = await fetch("/user/details", { cache: "no-store" });
    if (!response.ok) {
        return "Hello, User";
    }
    const data = await response.json();
    if (!data.user) {
        return "Hello, User";
    }
    return `Hello, ${data.user.username}`;
}

async function loadStats(root) {
    const response = await fetch("/user/details", { cache: "no-store" });
    if (!response.ok) {
        root.innerHTML = "<p>Could not load stats.</p>";
        return;
    }

    const data = await response.json();
    const user = data.user;
    if (!user) {
        root.innerHTML = "<p>No user logged in.</p>";
        return;
    }

    root.innerHTML = `
        <h3>Your Stats</h3>
        <table>
            <tr><th>Games Played</th><td>${user.gamesPlayed}</td></tr>
            <tr><th>Total Correct</th><td>${user.totalCorrect}</td></tr>
            <tr><th>Join Date</th><td>${user.joinDate}</td></tr>
        </table>
    `;
}

async function loadSessions(root) {
    const userDetails = await fetch("/user/details", { cache: "no-store" });
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

    const sessionResponse = await fetch("/api/sessions", { cache: "no-store" });
    if (!sessionResponse.ok) {
        root.innerHTML = "<p>Could not load sessions.</p>";
        return;
    }

    const allSessions = await sessionResponse.json();

    const userSessions = Array.isArray(allSessions)
        ? allSessions
            .filter(s =>
                s.userId === userId ||
                (s.user && s.user.id === userId)
            )
            .sort((a, b) => {
                const da = new Date(a.completionDate || a.completion_date || 0);
                const db = new Date(b.completionDate || b.completion_date || 0);
                return db - da;
            })
            .slice(0, 10)
        : [];

    let rows = "";

    if (userSessions.length === 0) {
        rows = `<tr><td colspan="3">No sessions yet.</td></tr>`;
    } else {
        rows = userSessions.map(s => `
            <tr>
                <td>${s.score}</td>
                <td>${s.totalQuestions ?? s.total_questions}</td>
                <td>${s.completionDate ?? s.completion_date}</td>
            </tr>
        `).join("");
    }

    root.innerHTML = `
        <h3>Recent Sessions</h3>
        <table>
            <thead>
                <tr><th>Score</th><th>Total Questions</th><th>Date</th></tr>
            </thead>
            <tbody>${rows}</tbody>
        </table>
    `;
}

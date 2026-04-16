// ================= SEND OTP =================
function sendOtp() {

    const email = document.getElementById("email")?.value;

    if (!email) {
        alert("Enter email first ❌");
        return;
    }

    fetch("http://localhost:8080/api/users/send-otp", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ email: email })
    })
    .then(() => alert("OTP sent ✅ Check your email"))
    .catch(() => alert("OTP failed ❌"));
}


// ================= PROFILE =================
const isLoggedIn = localStorage.getItem("isLoggedIn");
const userEmail = localStorage.getItem("userEmail");

if (isLoggedIn === "true") {

    const loginLink = document.getElementById("login-link");
    const signupLink = document.getElementById("signup-link");
    const profileBox = document.getElementById("profile-box");
    const profileIcon = document.getElementById("profile-icon");

    if (loginLink) loginLink.style.display = "none";
    if (signupLink) signupLink.style.display = "none";
    if (profileBox) profileBox.style.display = "block";

    // 🔥 FIXED ICON LOGIC
    const userName = localStorage.getItem("userName");

    if (profileIcon) {

        if (userName) {
            const parts = userName.trim().split(" ");

            let initials = parts.length >= 2
                ? parts[0][0] + parts[1][0]
                : parts[0][0];

            profileIcon.innerText = initials.toUpperCase();
        } else if (userEmail) {
            profileIcon.innerText = userEmail.substring(0,2).toUpperCase();
        }
    }
}


// ================= DROPDOWN =================
function toggleDropdown() {
    const menu = document.getElementById("dropdown-menu");
    if (!menu) return;

    menu.style.display = menu.style.display === "block" ? "none" : "block";
}

function logout() {
    localStorage.clear();
    window.location.href = "login.html";
}

window.addEventListener("click", function (e) {
    const profileBox = document.getElementById("profile-box");
    const menu = document.getElementById("dropdown-menu");

    if (profileBox && menu && !profileBox.contains(e.target)) {
        menu.style.display = "none";
    }
});


// ================= APPLY JOB =================
function applyJob(jobId) {

    if (localStorage.getItem("isLoggedIn") !== "true") {
        alert("Please login first ❌");
        window.location.href = "login.html";
        return;
    }

    let appliedJobs = JSON.parse(localStorage.getItem("appliedJobs")) || [];

    if (appliedJobs.includes(jobId)) {
        alert("Already applied ❌");
        return;
    }

    fetch("http://localhost:8080/api/applications", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            userEmail: localStorage.getItem("userEmail"),
            jobId: jobId
        })
    })
    .then(response => {
        if (response.ok) {

            appliedJobs.push(jobId);
            localStorage.setItem("appliedJobs", JSON.stringify(appliedJobs));

            alert("Applied Successfully ✅");

            location.reload(); // 🔥 refresh to disable button
        } else {
            alert("Failed to apply ❌");
        }
    })
    .catch(() => alert("Error ❌"));
}


// ================= LOAD JOBS =================
function loadJobs() {

    fetch("http://localhost:8080/api/jobs")
    .then(response => response.json())
    .then(jobs => {

        const jobList = document.getElementById("job-list");
        if (!jobList) return;

        jobList.innerHTML = "";

        const appliedJobs = JSON.parse(localStorage.getItem("appliedJobs")) || [];

        jobs.forEach(job => {

            const isApplied = appliedJobs.includes(job.id);

            const jobCard = document.createElement("div");
            jobCard.className = "job-card";

            jobCard.innerHTML = `
                <h3>${job.title}</h3>
                <p>${job.company}</p>
                <p>${job.location}</p>
                <button 
                    onclick="applyJob(${job.id})"
                    ${isApplied ? "disabled" : ""}
                >
                    ${isApplied ? "Applied ✅" : "Apply"}
                </button>
            `;

            jobList.appendChild(jobCard);
        });

    })
    .catch(() => console.log("Error loading jobs"));
}

loadJobs();


// ================= SIGNUP =================
const signupForm = document.getElementById("signup-form");

if (signupForm) {
    signupForm.addEventListener("submit", function (e) {

        e.preventDefault();

        fetch("http://localhost:8080/api/users/signup", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                name: document.getElementById("name").value,
                email: document.getElementById("email").value,
                password: document.getElementById("password").value,
                otp: document.getElementById("otp").value
            })
        })
        .then(res => res.json())
        .then(() => {
            alert("Signup Successful ✅");
            window.location.href = "login.html";
        })
        .catch(() => alert("Signup Failed ❌"));
    });
}


// ================= LOGIN =================
const loginForm = document.getElementById("login-form");

if (loginForm) {
    loginForm.addEventListener("submit", function (e) {

        e.preventDefault();

        const email = document.getElementById("email").value.trim().toLowerCase();
        const password = document.getElementById("password").value.trim();

        fetch("http://localhost:8080/api/users/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: email,
                password: password
            })
        })
        .then(res => res.json())
        .then(data => {

            console.log("LOGIN DATA:", data); // 🔥 DEBUG

            if (data && data.id) {

                localStorage.setItem("isLoggedIn", "true");
                localStorage.setItem("userEmail", data.email);

                // 🔥 SAFE FIX (important)
                localStorage.setItem("userName", data.name || data.email);

                alert("Login Successful ✅");

                window.location.href = "index.html";

            } else {
                alert("Invalid Credentials ❌");
            }
        })
        .catch((err) => {
            console.error(err);
            alert("Server Error ❌");
        });
    });
}
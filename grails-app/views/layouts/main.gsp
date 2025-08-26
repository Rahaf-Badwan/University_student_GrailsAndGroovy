<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
    <g:layoutTitle default="Grails"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>
    <asset:stylesheet src="application.css"/>

    <style>
    :root {
        --primary-color: #003366;
        --secondary-color: #004080;
        --light-color: #f8f9fa;
        --dark-color: #003366;
        --text-color: #212529;
        --sidebar-width: 250px;
    }

    body {
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        color: var(--text-color);
        margin: 0;
        height: 100vh;
        display: flex;
        flex-direction: column;
    }

    /* صفحة تسجيل الدخول */
    body.login-page {
        background-color: #003366 !important;
        color: white !important;
    }

    .login-header {
        background-color: #004080;
        color: white;
        padding: 1.5rem;
        text-align: center;
        font-size: 1.8rem;
        font-weight: bold;
    }

    .login-footer {
        background-color: #004080;
        color: white;
        padding: 1rem;
        text-align: center;
        font-size: 0.9rem;
    }

    /* input في login */
    body.login-page input[type="text"],
    body.login-page input[type="password"] {
        color: #FFD700; /* نص أصفر لامع */
        background-color: #002244; /* خلفية أغمق */
        border: 1px solid #004080;
        padding: 0.5rem;
        border-radius: 4px;
        width: 100%;
        box-sizing: border-box;
        margin-bottom: 1rem;
    }

    body.login-page input::placeholder {
        color: #FFD700;
        opacity: 0.8;
    }

    #content-wrapper {
        flex: 1;
        display: flex;
        overflow: hidden;
    }

    /* Labels في صفحة login */
    body.login-page label {
        color: #003366; /* لون غامق قريب من الأسود */
        font-weight: 600;
        display: block;
        margin-bottom: 0.3rem;
    }


    #sidebar {
        width: var(--sidebar-width);
        background-color: var(--primary-color);
        color: white;
        display: flex;
        flex-direction: column;
        transition: transform 0.3s ease;
        transform: translateX(0);
        position: fixed;
        height: 100vh;
        top: 0;
        left: 0;
        z-index: 1000;
    }

    #sidebar.closed {
        transform: translateX(calc(-1 * var(--sidebar-width)));
    }

    #sidebar nav {
        flex-grow: 1;
        padding: 1rem;
    }

    #sidebar nav ul {
        list-style: none;
        padding: 0;
        margin: 0;
    }

    #sidebar nav ul li {
        padding: 15px 10px;
        border-bottom: 1px solid rgba(255, 255, 255, 0.3);
    }

    #sidebar nav ul li a, #sidebar nav ul li g\\:link {
        color: white;
        text-decoration: none;
        display: block;
        font-weight: 600;
    }

    #sidebar nav ul li a:hover,
    #sidebar nav ul li g\\:link:hover {
        background-color: var(--secondary-color);
        cursor: pointer;
    }

    #toggle-btn {
        position: fixed;
        top: 10px;
        left: 10px;
        z-index: 1101;
        background-color: var(--secondary-color);
        border: none;
        color: white;
        padding: 10px;
        cursor: pointer;
        font-size: 1.5rem;
        border-radius: 4px;
        display: block;
    }

    main.main-content {
        flex: 1;
        overflow-y: auto;
        background-color: var(--light-color);
        padding: 2rem;
        margin-left: var(--sidebar-width);
        transition: margin-left 0.3s ease;
    }

    main.main-content.sidebar-closed {
        margin-left: 0;
    }

    .navbar-static-top {
        display: none;
    }

    footer.footer {
        background-color: var(--dark-color);
        color: white;
        padding: 1rem 0;
        text-align: center;
    }

    </style>

    <g:layoutHead/>
</head>

<g:form id="logoutForm" url="[controller: 'logout']" method="POST" style="display:none;"></g:form>

<body class="${controllerName == 'login' && actionName == 'auth' ? 'login-page' : ''}">

<!-- الصفحات المسجّل دخولها -->
<sec:ifLoggedIn>
    <button id="toggle-btn" aria-label="Toggle menu">☰ Menu</button>

    <div id="content-wrapper">
        <aside id="sidebar">
            <nav>
                <ul>
                    <li><a href="/">🏠 Home</a></li>
                    <li><g:link controller="student" action="index">👤 Student</g:link></li>
                    <li><g:link controller="course" action="index">📘 Course</g:link></li>
                    <li><g:link controller="enrollment" action="index">📝 Enrollment</g:link></li>
                    <li><g:link controller="book" action="index">📚 Books</g:link></li>
                    <li>
                        <a href="#" onclick="event.preventDefault();
                        document.getElementById('logoutForm').submit();">🚪 Logout</a>
                    </li>
                </ul>

            </nav>
        </aside>

        <main id="mainContent" class="main-content">
            <div class="container">
                <g:layoutBody/>
            </div>
        </main>
    </div>

    <footer class="footer" role="contentinfo">
        <div class="container">
            <p>&copy; <g:formatDate date="${new Date()}" format="yyyy"/> EduTrack. All rights reserved.</p>
        </div>
    </footer>
</sec:ifLoggedIn>

<!-- صفحة تسجيل الدخول -->
<sec:ifNotLoggedIn>
    <header class="login-header">
        Welcome to EduTrack
    </header>

    <main class="main-content" style="flex: 1; margin-left: 0;">
        <div class="container">
            <g:layoutBody/>
        </div>
    </main>

    <footer class="login-footer">
        &copy; <g:formatDate date="${new Date()}" format="yyyy"/> EduTrack. All rights reserved.
    </footer>
</sec:ifNotLoggedIn>

<div id="spinner" class="spinner" style="display:none;">
    <g:message code="spinner.alt" default="Loading&hellip;"/>
</div>

<asset:javascript src="application.js"/>

<script>
    const toggleBtn = document.getElementById('toggle-btn');
    const sidebar = document.getElementById('sidebar');
    const mainContent = document.getElementById('mainContent');

    if (toggleBtn && sidebar && mainContent) {
        toggleBtn.addEventListener('click', () => {
            sidebar.classList.toggle('closed');
            mainContent.classList.toggle('sidebar-closed');
        });
    }
</script>

</body>
</html>

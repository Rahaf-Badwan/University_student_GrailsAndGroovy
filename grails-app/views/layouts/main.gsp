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
        --primary-color: #4CAF50;
        --secondary-color: #2E7D32;
        --light-color: #f8f9fa;
        --dark-color: #343a40;
        --text-color: #212529;
    }

    body {
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        color: var(--text-color);
        display: flex;
        flex-direction: column;
        min-height: 100vh;
        margin: 0;
    }

    /* Header Styles */
    .navbar {
        background-color: var(--primary-color) !important;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        padding: 0.8rem 1rem;
    }

    .navbar-brand {
        display: flex;
        align-items: center;
        font-weight: 600;
        color: white !important;
    }

    .navbar-brand img {
        height: 30px;
        margin-right: 10px;
    }

    .navbar-dark .navbar-nav .nav-link {
        color: rgba(255,255,255,0.9);
        padding: 0.5rem 1rem;
        transition: all 0.3s ease;
    }

    .navbar-dark .navbar-nav .nav-link:hover {
        color: white;
        transform: translateY(-2px);
    }

    /* Main Content */
    .main-content {
        flex: 1;
        padding: 2rem 0;
    }

    /* Footer Styles */
    .footer {
        background-color: var(--dark-color);
        color: white;
        padding: 3rem 0;
        margin-top: auto;
    }

    .footer a {
        color: #6c757d;
        text-decoration: none;
        transition: color 0.3s ease;
    }

    .footer a:hover {
        color: white;
        text-decoration: underline;
    }

    .footer .col {
        margin-bottom: 1.5rem;
        padding: 0 15px;
    }

    .footer img {
        height: 50px;
        margin-right: 15px;
        margin-bottom: 15px;
        float: left;
    }

    .footer strong {
        display: block;
        color: white;
        margin-bottom: 1rem;
        font-size: 1.2rem;
        clear: both;
    }

    .footer p {
        color: #adb5bd;
        line-height: 1.6;
        margin-bottom: 0;
    }

    .footer .footer-copyright {
        border-top: 1px solid rgba(255,255,255,0.1);
        padding-top: 20px;
        margin-top: 20px;
        text-align: center;
        color: #6c757d;
    }

    /* Responsive Adjustments */
    @media (max-width: 768px) {
        .navbar-brand img {
            height: 25px;
        }

        .footer .row {
            flex-direction: column;
        }

        .footer .col {
            text-align: center;
            margin-bottom: 30px;
        }

        .footer img {
            float: none !important;
            display: block;
            margin: 0 auto 15px;
        }
    }
    </style>

    <g:layoutHead/>
</head>

<body>
<!-- Header -->
<nav class="navbar navbar-expand-lg navbar-dark navbar-static-top" role="navigation">
    <div class="container">
        <a class="navbar-brand" href="/">
            <asset:image src="grails.svg" alt="Grails Logo"/>
            <span>Grails Application</span>
        </a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarContent">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarContent">
            <ul class="navbar-nav ml-auto">
                <!-- Home -->
                <li class="nav-item">
                    <a class="nav-link" href="/">🏠 Home</a>
                </li>
                <!-- Student -->
                <li class="nav-item">
                    <g:link class="nav-link" controller="student" action="index">👤 Student</g:link>
                </li>
                <!-- Course -->
                <li class="nav-item">
                    <g:link class="nav-link" controller="course" action="index">📘 Course</g:link>
                </li>
                <!-- Enrollment -->
                <li class="nav-item">
                    <g:link class="nav-link" controller="enrollment" action="index">📝 Enrollment</g:link>
                </li>
            </ul>
        </div>
    </div>
</nav>



<!-- Main Content -->
<main class="main-content">
    <div class="container">
        <g:layoutBody/>
    </div>
</main>

<!-- Footer -->
<footer class="footer" role="contentinfo">
    <div class="container">
        <div class="row">
            <div class="col-md-4">
                <a href="http://guides.grails.org" target="_blank">
                    <asset:image src="advancedgrails.svg" alt="Grails Guides"/>
                </a>
                <strong><a href="http://guides.grails.org" target="_blank">Grails Guides</a></strong>
                <p>Building your first Grails app? Looking to add security, or create a Single-Page-App? Check out the <a href="http://guides.grails.org" target="_blank">Grails Guides</a> for step-by-step tutorials.</p>
            </div>

            <div class="col-md-4">
                <a href="http://docs.grails.org" target="_blank">
                    <asset:image src="documentation.svg" alt="Grails Documentation"/>
                </a>
                <strong><a href="http://docs.grails.org" target="_blank">Documentation</a></strong>
                <p>Ready to dig in? You can find in-depth documentation for all the features of Grails in the <a href="http://docs.grails.org" target="_blank">User Guide</a>.</p>
            </div>

            <div class="col-md-4">
                <a href="https://slack.grails.org" target="_blank">
                    <asset:image src="slack.svg" alt="Grails Slack"/>
                </a>
                <strong><a href="https://slack.grails.org" target="_blank">Join the Community</a></strong>
                <p>Get feedback and share your experience with other Grails developers in the community <a href="https://slack.grails.org" target="_blank">Slack channel</a>.</p>
            </div>
        </div>

        <div class="footer-copyright">
            <p>&copy; <g:formatDate date="${new Date()}" format="yyyy"/> Grails Application. All rights reserved.</p>
        </div>
    </div>
</footer>

<div id="spinner" class="spinner" style="display:none;">
    <g:message code="spinner.alt" default="Loading&hellip;"/>
</div>

<asset:javascript src="application.js"/>
</body>
</html>

<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Welcome Page</title>
</head>
<body>
<content tag="nav">
    <!-- (القائمة السابقة بنفس شكلها) -->
</content>

<div class="svg" role="presentation">
    <div class="grails-logo-container">
        <asset:image src="grails-cupsonly-logo-white.svg" class="grails-logo"/>
    </div>
</div>

<div id="content" role="main">
    <div class="container">
        <section>
        <!-- للمدير فقط (ROLE_ADMIN) -->
            <sec:ifAllGranted roles="ROLE_ADMIN">
                <h1>Welcome, Admin!</h1>
                <p>You have access to manage courses, students, and enrollments.</p>
                <ul>
                    <li><g:link controller="course" action="index">Manage Courses</g:link></li>
                    <li><g:link controller="student" action="index">Manage Students</g:link></li>
                    <li><g:link controller="enrollment" action="index">Manage Enrollments</g:link></li>
                </ul>
            </sec:ifAllGranted>

        <!-- للطلاب فقط (ROLE_STUDENT) -->
            <sec:ifAllGranted roles="ROLE_STUDENT">
                <h1>Welcome, <sec:username/></h1>
                <p>You can view available courses and your enrollments.</p>
                <ul>
                    <li><g:link controller="course" action="index">View Courses</g:link></li>
                    <li><g:link controller="enrollment" action="index">View Enrollments</g:link></li>
                </ul>
            </sec:ifAllGranted>
        </section>
    </div>
</div>
</body>
</html>

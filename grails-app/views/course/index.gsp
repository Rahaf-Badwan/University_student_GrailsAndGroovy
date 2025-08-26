<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'course.label', default: 'Course')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
    <style>
    /* General body & headings */
    body {
        background-color: #f9f9f9;
        font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
    }

    h1 {
        font-weight: bold;
        margin-bottom: 20px;
        text-align: center;
        color: #0d6efd;
    }

    /* Table style */
    .course-table {
        border-collapse: collapse;
        width: 100%;
    }

    .course-table th {
        background-color: #f0f0f0; /* Header فاتح */
        color: #000; /* نص أسود */
        font-weight: bold;
        cursor: pointer;
        padding: 8px 12px; /* أصغر padding */
        text-align: left;
    }

    .course-table td {
        color: #000; /* نص أسود */
        padding: 8px 12px; /* أصغر padding */
        vertical-align: middle;
    }

    .course-table td a {
        color: #0d6efd; /* رابط أزرق */
        text-decoration: underline;
    }

    /* Search bar & dropdown */
    .course-search-bar {
        margin-bottom: 15px;
    }

    .course-search-bar .form-control {
        height: 35px; /* تصغير مربع البحث */
    }

    .course-search-bar .form-select {
        height: 35px; /* تصغير dropdown */
        margin-left: 10px;
        margin-right: 10px;
    }

    /* Buttons */
    .course-btn {
        border-radius: 8px;
        padding: 6px 12px; /* أصغر حجم */
    }

    /* Optional: hover effect on table rows */
    .course-table tbody tr:hover {
        background-color: #f1f1f1;
    }
    </style>

</head>

<body>
<div id="content" role="main">
    <div class="container">

        <!-- Navigation -->
        <section class="row">
            <a href="#list-course" class="skip" tabindex="-1">
                <g:message code="default.link.skip.label" default="Skip to content…"/>
            </a>

            <div class="nav" role="navigation">
                <ul>
                    <li><a class="home" href="${createLink(uri: '/')}">
                        <g:message code="default.home.label"/>
                    </a></li>
                    <g:if test="${isAdmin}">
                        <li>
                            <g:link class="create" action="create">
                                <g:message code="default.new.label" args="[entityName]"/>
                            </g:link>
                        </li>
                    </g:if>
                </ul>
            </div>
        </section>

        <!-- Search and Sort Form -->
        <section class="row mb-3">
            <div class="col-12">
                <g:form controller="course" action="index" method="get" class="form-inline" id="searchForm">
                    <!-- البحث بالـ Title وDescription وCredits -->
                    <input type="text" name="query" placeholder="Search ..."
                           value="${params.query ?: ''}" class="form-control mr-2"/>

                    <label for="sortBy" class="mr-2">Sort by:</label>
                    <select name="sortBy" id="sortBy" class="form-control mr-2" onchange="this.form.submit()">
                        <option value="">-- sortBy --</option>
                        <option value="title" ${params.sortBy == 'title' ? 'selected' : ''}>Title</option>
                        <option value="description" ${params.sortBy == 'description' ? 'selected' : ''}>Description</option>
                        <option value="credits" ${params.sortBy == 'credits' ? 'selected' : ''}>Credits</option>
                    </select>

                    <button type="submit" class="btn btn-primary">Search</button>
                </g:form>
            </div>
        </section>

        <!-- Course List -->
        <section class="row">
            <div id="list-course" class="col-12 content scaffold-list" role="main">
                <h1><g:message code="default.list.label" args="[entityName]"/></h1>

                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>

                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Title</th>
                        <th>Description</th>
                        <th>Credits</th>
                        <g:if test="${isAdmin}">
                            <th>Actions</th>
                        </g:if>
                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${courseList}" var="course">
                        <tr>
                            <td>
                                <g:if test="${isAdmin}">
                                    <g:link action="edit" resource="${course}">${course.title}</g:link>
                                </g:if>
                                <g:else>${course.title}</g:else>
                            </td>
                            <td>${course.description}</td>
                            <td>${course.credits}</td>
                            <g:if test="${isAdmin}">
                                <td>
                                    <g:link class="btn btn-sm btn-primary" action="edit"
                                            resource="${course}">Edit</g:link>
                                    <g:form action="delete" resource="${course}" method="DELETE"
                                            style="display:inline;">
                                        <input type="submit" class="btn btn-sm btn-danger" value="Delete"
                                               onclick="return confirm('Are you sure you want to delete this course?');"/>
                                    </g:form>
                                </td>
                            </g:if>
                        </tr>
                    </g:each>
                    </tbody>
                </table>

                <g:if test="${courseCount > params.int('max')}">
                    <div class="pagination">
                        <g:paginate total="${courseCount ?: 0}"/>
                    </div>
                </g:if>
            </div>
        </section>

    </div>
</div>
</body>
</html>

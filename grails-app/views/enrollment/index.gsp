<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'enrollment.label', default: 'Enrollment')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<div id="content" role="main">
    <div class="container">
        <section class="row">
            <a href="#list-enrollment" class="skip" tabindex="-1">
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
                <g:form controller="enrollment" action="index" method="get" class="form-inline mb-3" id="searchForm">
                    <!-- البحث باسم المستخدم -->
                    <input type="text" name="query" placeholder="Search ..." value="${params.query ?: ''}"
                           class="form-control mr-2"/>

                    <!-- نطاق GPA -->
                    <input type="number" step="0.1" name="gpaMin" id="gpaMin" placeholder="GPA Min"
                           value="${params.gpaMin ?: ''}" class="form-control mr-2" min="0" max="4"/>
                    <input type="number" step="0.1" name="gpaMax" id="gpaMax" placeholder="GPA Max"
                           value="${params.gpaMax ?: ''}" class="form-control mr-2" min="0" max="4"/>

                    <!-- السورت -->
                    <label for="sortBy" class="mr-2">Sort by:</label>
                    <select name="sortBy" id="sortBy" class="form-control mr-2">
                        <option value="">-- sortBy --</option>
                        <option value="username" ${params.sortBy == 'username' ? 'selected' : ''}>Student Name</option>
                        <option value="enrollmentDate" ${params.sortBy == 'enrollmentDate' ? 'selected' : ''}>Enrollment Date</option>
                        <option value="grade" ${params.sortBy == 'grade' ? 'selected' : ''}>Grade</option>
                    </select>

                    <button type="submit" class="btn btn-primary">Search</button>
                </g:form>

                <script>
                    document.getElementById('searchForm').addEventListener('submit', function (e) {
                        const gpaMin = parseFloat(document.getElementById('gpaMin').value) || 0;
                        const gpaMax = parseFloat(document.getElementById('gpaMax').value) || 4;

                        if (gpaMax < gpaMin) {
                            alert("GPA Max cannot be less than GPA Min");
                            e.preventDefault(); // يمنع الفورم من الإرسال
                        }
                    });
                </script>

            </div>
        </section>

        <!-- Enrollment List -->
        <section class="row">
            <div id="list-enrollment" class="col-12 content scaffold-list" role="main">
                <h1><g:message code="default.list.label" args="[entityName]"/></h1>

                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>

                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Student Name</th>
                        <th>Course Title</th>
                        <th>Enrollment Date</th>
                        <th>Grade</th>
                        <th>GPA</th>
                        <g:if test="${isAdmin}">
                            <th>Actions</th>
                        </g:if>
                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${enrollmentList}" var="enrollment">
                        <tr>
                            <td>
                                <g:if test="${isAdmin}">
                                    <g:link action="edit" resource="${enrollment}">${enrollment.student?.name}</g:link>
                                </g:if>
                                <g:else>${enrollment.student?.name}</g:else>
                            </td>
                            <td>${enrollment.course?.title}</td>
                            <td>
                                <g:formatDate date="${enrollment.enrollmentDate}" format="yyyy-MM-dd"/>
                            </td>
                            <td>${enrollment.grade ?: 'N/A'}</td>
                            <td>
                                <g:if test="${gpaList}">
                                    <g:set var="gpaItem"
                                           value="${gpaList.find { it.studentId == enrollment.student?.id }}"/>
                                    ${gpaItem?.gpa ?: 'N/A'}
                                </g:if>
                            </td>
                            <g:if test="${isAdmin}">
                                <td>
                                    <g:link class="btn btn-sm btn-primary" action="edit"
                                            resource="${enrollment}">Edit</g:link>
                                    <g:form action="delete" resource="${enrollment}" method="DELETE"
                                            style="display:inline;">
                                        <input type="submit" class="btn btn-sm btn-danger" value="Delete"
                                               onclick="return confirm('Are you sure you want to delete this enrollment?');"/>
                                    </g:form>
                                </td>
                            </g:if>
                        </tr>
                    </g:each>
                    </tbody>
                </table>

                <g:if test="${enrollmentCount > params.int('max')}">
                    <div class="pagination">
                        <g:paginate total="${enrollmentCount ?: 0}"/>
                    </div>
                </g:if>
            </div>
        </section>
    </div>
</div>
</body>
</html>

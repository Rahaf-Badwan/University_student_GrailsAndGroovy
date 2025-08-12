<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'student.label', default: 'Student')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<div id="content" role="main">
    <div class="container">
        <!-- Navigation -->
        <section class="row">
            <a href="#list-student" class="skip" tabindex="-1">
                <g:message code="default.link.skip.label" default="Skip to content…" />
            </a>
            <div class="nav" role="navigation">
                <ul>
                    <li><a class="home" href="${createLink(uri: '/')}">
                        <g:message code="default.home.label" />
                    </a></li>
                    <g:if test="${isAdmin}">
                        <li><g:link class="create" action="create">
                            <g:message code="default.new.label" args="[entityName]" />
                        </g:link></li>
                    </g:if>
                </ul>
            </div>
        </section>

        <!-- Student List -->
        <section class="row">
            <div id="list-student" class="col-12 content scaffold-list" role="main">
                <h1><g:message code="default.list.label" args="[entityName]" /></h1>

                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>

                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Photo</th>
                        <th>Name</th>
                        <th>Email</th>
                        <g:if test="${isAdmin}">
                            <th>Actions</th>
                        </g:if>
                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${studentList}" var="student">
                        <tr>
                            <td>
                                <g:if test="${student.profilePhoto}">
                                    <img src="${createLink(controller:'student', action:'profilePhoto', id:student.id)}"
                                         alt="Profile Photo" style="max-width: 50px; max-height: 50px; border-radius: 50%;"/>
                                </g:if>
                                <g:else>
                                    <img src="${resource(dir:'images', file:'default-profile.png')}"
                                         alt="No Photo" style="max-width: 50px; max-height: 50px; border-radius: 50%; opacity: 0.5;" />
                                </g:else>
                            </td>
                            <td>
                                <g:if test="${isAdmin}">
                                    <g:link action="edit" resource="${student}">${student.name}</g:link>
                                </g:if>
                                <g:else>
                                    ${student.name}
                                </g:else>
                            </td>
                            <td>${student.email}</td>
                            <g:if test="${isAdmin}">
                                <td>
                                    <g:link class="btn btn-sm btn-primary" action="edit" resource="${student}">Edit</g:link>
                                    <g:form action="delete" resource="${student}" method="DELETE" style="display:inline;">
                                        <input type="submit" class="btn btn-sm btn-danger" value="Delete"
                                               onclick="return confirm('Are you sure you want to delete this student?');" />
                                    </g:form>
                                </td>
                            </g:if>
                        </tr>
                    </g:each>
                    </tbody>
                </table>

                <g:if test="${studentCount > params.int('max')}">
                    <div class="pagination">
                        <g:paginate total="${studentCount ?: 0}" />
                    </div>
                </g:if>
            </div>
        </section>
    </div>
</div>
</body>
</html>

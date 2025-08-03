<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'student.label', default: 'Student')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
<div id="content" role="main">
    <div class="container">
        <section class="row">
            <a href="#show-student" class="skip" tabindex="-1">
                <g:message code="default.link.skip.label" default="Skip to content&hellip;" />
            </a>
            <div class="nav" role="navigation">
                <ul>
                    <li><a class="home" href="${createLink(uri: '/')}">
                        <g:message code="default.home.label"/>
                    </a></li>
                    <li><g:link class="list" action="index">
                        <g:message code="default.list.label" args="[entityName]" />
                    </g:link></li>
                    <li><g:link class="create" action="create">
                        <g:message code="default.new.label" args="[entityName]" />
                    </g:link></li>
                </ul>
            </div>
        </section>

        <section class="row">
            <div id="show-student" class="col-12 content scaffold-show" role="main">
                <h1><g:message code="default.show.label" args="[entityName]" /></h1>

                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>

                <div class="card p-4 shadow-sm">
                    <div class="mb-3">
                        <strong>Name:</strong> ${student?.name}
                    </div>
                    <div class="mb-3">
                        <strong>Email:</strong> ${student?.email}
                    </div>
                </div>

                <g:form resource="${student}" method="DELETE" class="mt-3">
                    <fieldset class="buttons">
                        <g:link class="edit btn btn-primary" action="edit" resource="${student}">
                            <g:message code="default.button.edit.label" default="Edit" />
                        </g:link>
                        <input class="delete btn btn-danger" type="submit"
                               value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                               onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                    </fieldset>
                </g:form>
            </div>
        </section>
    </div>
</div>
</body>
</html>

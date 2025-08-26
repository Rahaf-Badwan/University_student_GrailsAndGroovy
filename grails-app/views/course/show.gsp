<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'course.label', default: 'Course')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<div id="content" role="main">
    <div class="container">
        <section class="row">
            <a href="#show-course" class="skip" tabindex="-1">
                <g:message code="default.link.skip.label" default="Skip to content&hellip;"/>
            </a>

            <div class="nav" role="navigation">
                <ul>
                    <li><a class="home" href="${createLink(uri: '/')}">
                        <g:message code="default.home.label"/>
                    </a></li>
                    <li><g:link class="list" action="index">
                        <g:message code="default.list.label" args="[entityName]"/>
                    </g:link></li>
                    <li><g:link class="create" action="create">
                        <g:message code="default.new.label" args="[entityName]"/>
                    </g:link></li>
                </ul>
            </div>
        </section>

        <section class="row">
            <div id="show-course" class="col-12 content scaffold-show" role="main">
                <h1><g:message code="default.show.label" args="[entityName]"/></h1>

                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>

                <div class="card p-4 shadow-sm">
                    <div class="mb-3">
                        <strong>Title:</strong> ${course?.title}
                    </div>

                    <div class="mb-3">
                        <strong>Description:</strong> ${course?.description}
                    </div>

                    <div class="mb-3">
                        <strong>Credits:</strong> ${course?.credits}
                    </div>
                </div>

                <g:form resource="${course}" method="DELETE" class="mt-3">
                    <fieldset class="buttons">
                        <g:link class="edit btn btn-primary" action="edit" resource="${course}">
                            <g:message code="default.button.edit.label" default="Edit"/>
                        </g:link>
                        <input class="delete btn btn-danger" type="submit"
                               value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                               onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
                    </fieldset>
                </g:form>
            </div>
        </section>
    </div>
</div>
</body>
</html>

<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'student.label', default: 'Student')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
    <div id="content" role="main">
        <div class="container">
            <section class="row">
                <a href="#create-student" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
                <div class="nav" role="navigation">
                    <ul>
                        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                        <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                    </ul>
                </div>
            </section>
            <section class="row">
                <div id="create-student" class="col-12 content scaffold-create" role="main">
                    <h1><g:message code="default.create.label" args="[entityName]" /></h1>
                    <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                    </g:if>
                    <g:hasErrors bean="${this.student}">
                    <ul class="errors" role="alert">
                        <g:eachError bean="${this.student}" var="error">
                        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                        </g:eachError>
                    </ul>
                    </g:hasErrors>
                    <g:form resource="${student}" method="POST" class="needs-validation">
                        <div class="card p-4 shadow-sm">
                            <fieldset class="form">

                                <div class="mb-3">
                                    <label for="name" class="form-label">Name *</label>
                                    <g:textField name="name" value="${student?.name}" required=""/>
                                    <g:fieldError bean="${student}" field="name" class="text-danger" />
                                </div>

                                <div class="mb-3">
                                    <label for="email" class="form-label">Email *</label>
                                    <g:textField name="email" value="${student?.email}" required=""/>
                                    <g:fieldError bean="${student}" field="email" class="text-danger" />
                                </div>

                                <!-- 🚫 لا تكتب حقل enrollments -->
                            </fieldset>

                            <div class="mt-4">
                                <g:submitButton name="create" class="btn btn-success" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                                <g:link class="btn btn-secondary ms-2" action="index">Cancel</g:link>
                            </div>
                        </div>
                    </g:form>

                </div>
            </section>
        </div>
    </div>
    </body>
</html>

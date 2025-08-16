<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'student.label', default: 'Student')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
<div id="content" role="main">
    <div class="container">
        <section class="row">
            <a href="#edit-student" class="skip" tabindex="-1">
                <g:message code="default.link.skip.label" default="Skip to content&hellip;"/>
            </a>
            <div class="nav" role="navigation">
                <ul>
                    <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                    <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                    <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
                </ul>
            </div>
        </section>

        <section class="row">
            <div id="edit-student" class="col-12 content scaffold-edit" role="main">
                <h1><g:message code="default.edit.label" args="[entityName]" /></h1>

                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>

                <g:hasErrors bean="${student}">
                    <ul class="errors" role="alert">
                        <g:eachError bean="${student}" var="error">
                            <li <g:if test="${error instanceof org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                                <g:message error="${error}"/>
                            </li>
                        </g:eachError>
                    </ul>
                </g:hasErrors>

                <g:form resource="${student}" method="PUT" enctype="multipart/form-data">
                    <g:hiddenField name="version" value="${student?.version}" />

                    <fieldset class="form">

                        <div class="mb-3">
                            <label for="name">Name *</label>
                            <g:textField id="name" name="name" value="${student?.name}" required="required"/>
                            <g:fieldError field="name" bean="${student}" class="text-danger" />
                        </div>

                        <div class="mb-3">
                            <label for="email">Email *</label>
                            <g:textField id="email" name="email" value="${student?.email}" required="required"/>
                            <g:fieldError field="email" bean="${student}" class="text-danger" />
                        </div>
                        <div class="mb-3">
                            <label for="username">Username *</label>
                            <g:textField id="username" name="username" value="${student?.user?.username}" required="required" />
                            <g:fieldError field="username" bean="${student?.user}" class="text-danger" />
                        </div>

                        <div class="mb-3">
                            <label for="password">Password</label>
                            <g:passwordField id="password" name="password" />
                            <g:fieldError field="password" bean="${student?.user}" class="text-danger" />
                        </div>

                        <div class="mb-3">
                            <label for="confirmPassword">Confirm Password</label>
                            <g:passwordField id="confirmPassword" name="confirmPassword" />
                            <!-- عادة ما يتم التحقق في الكود وليس من الجافا، فلا يوجد فالديرور هنا -->
                        </div>


                        <div class="mb-3">
                            <label for="profilePhoto">Profile Photo</label>
                            <g:if test="${student?.profilePhoto}">
                                <br/>
                                <img src="${createLink(controller:'student', action:'profilePhoto', id:student.id)}"
                                     alt="Profile Photo" style="max-width: 200px; max-height: 200px;"/>
                                <br/>
                                <small>Upload new image to replace the existing one</small>
                            </g:if>
                            <input type="file" id="profilePhoto" name="profilePhoto" accept="image/*" />
                        </div>


                        <!-- No enrollments field here -->
                    </fieldset>

                    <fieldset class="buttons">
                        <input class="save" type="submit" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                    </fieldset>
                </g:form>
            </div>
        </section>
    </div>
</div>
</body>
</html>

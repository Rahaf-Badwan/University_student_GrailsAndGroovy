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
            <a href="#create-student" class="skip" tabindex="-1">
                <g:message code="default.link.skip.label" default="Skip to content&hellip;"/>
            </a>
            <div class="nav" role="navigation">
                <ul>
                    <li><a class="home" href="${createLink(uri: '/')}">
                        <g:message code="default.home.label"/>
                    </a></li>
                    <li><g:link class="list" action="index">
                        <g:message code="default.list.label" args="[entityName]" />
                    </g:link></li>
                </ul>
            </div>
        </section>

        <section class="row">
            <div id="create-student" class="col-12 content scaffold-create" role="main">
                <h1><g:message code="default.create.label" args="[entityName]" /></h1>

            <!-- Flash message -->
                <g:if test="${flash.message}">
                    <div class="alert alert-warning mt-2">${flash.message}</div>
                </g:if>

            <!-- Form Errors -->
                <g:hasErrors bean="${student}">
                    <div class="alert alert-danger mt-2">
                        <ul>
                            <g:eachError bean="${student}" var="error">
                                <li><g:message error="${error}" /></li>
                            </g:eachError>
                        </ul>
                    </div>
                </g:hasErrors>

                <g:form controller="student" action="save" method="POST" enctype="multipart/form-data">
                    <div class="card p-4 shadow-sm">
                        <fieldset class="form">

                            <!-- Name -->
                            <div class="mb-3">
                                <label for="name" class="form-label">Name *</label>
                                <g:textField name="name" value="${student?.name}" required=""/>
                                <g:fieldError bean="${student}" field="name" class="text-danger"/>
                            </div>

                            <!-- Email -->
                            <div class="mb-3">
                                <label for="email" class="form-label">Email *</label>
                                <g:textField name="email" value="${student?.email}" required=""/>
                                <g:fieldError bean="${student}" field="email" class="text-danger"/>
                            </div>

                            <!-- Username -->
                            <div class="mb-3">
                                <label for="username" class="form-label">Username *</label>
                                <input type="text" id="username" name="username" required="" value="${params.username ?: ''}" />
                                <g:fieldError bean="${student}" field="user" class="text-danger"/>
                            </div>

                            <!-- Password -->
                            <div class="mb-3">
                                <label for="password" class="form-label">Password *</label>
                                <input type="password" id="password" name="password" required="" />
                                <g:fieldError bean="${student}" field="user" class="text-danger"/>
                            </div>

                            <!-- Confirm Password -->
                            <div class="mb-3">
                                <label for="passwordConfirm" class="form-label">Confirm Password *</label>
                                <input type="password" id="passwordConfirm" name="passwordConfirm" required="" />
                            </div>

                            <!-- Profile Photo -->
                            <div class="mb-3">
                                <label for="profilePhoto" class="form-label">Profile Photo</label>
                                <input type="file" id="profilePhoto" name="profilePhoto" accept="image/*" />
                                <g:fieldError bean="${student}" field="profilePhoto" class="text-danger"/>
                            </div>

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

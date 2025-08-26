<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'course.label', default: 'Course')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>

<body>
<div id="content" role="main">
    <div class="container">
        <section class="row">
            <a href="#edit-course" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                                         default="Skip to content&hellip;"/></a>

            <div class="nav" role="navigation">
                <ul>
                    <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                    <li><g:link class="list" action="index"><g:message code="default.list.label"
                                                                       args="[entityName]"/></g:link></li>
                    <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                                          args="[entityName]"/></g:link></li>
                </ul>
            </div>
        </section>
        <section class="row">
            <div id="edit-course" class="col-12 content scaffold-edit" role="main">
                <h1><g:message code="default.edit.label" args="[entityName]"/></h1>
                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>
                <g:hasErrors bean="${this.course}">
                    <ul class="errors" role="alert">
                        <g:eachError bean="${this.course}" var="error">
                            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                                    error="${error}"/></li>
                        </g:eachError>
                    </ul>
                </g:hasErrors>
                <g:form resource="${this.course}" method="PUT">
                    <g:hiddenField name="version" value="${this.course?.version}"/>
                    <fieldset class="form">
                        <div class="fieldcontain ${hasErrors(bean: course, field: 'title', 'error')}">
                            <label for="title">
                                <g:message code="course.title.label" default="Title"/>
                            </label>
                            <g:textField name="title" value="${course?.title}"/>
                        </div>

                        <div class="fieldcontain ${hasErrors(bean: course, field: 'description', 'error')}">
                            <label for="description">
                                <g:message code="course.description.label" default="Description"/>
                            </label>
                            <g:textArea name="description" value="${course?.description}" rows="5" cols="40"/>
                        </div>

                        <div class="fieldcontain ${hasErrors(bean: course, field: 'credits', 'error')}">
                            <label for="credits">
                                <g:message code="course.credits.label" default="Credits"/>
                            </label>
                            <g:field type="number" name="credits" value="${course?.credits}" step="0.5"/>
                        </div>
                    </fieldset>

                    <fieldset class="buttons">
                        <input class="save" type="submit"
                               value="${message(code: 'default.button.update.label', default: 'Update')}"/>
                    </fieldset>
                </g:form>
            </div>
        </section>
    </div>
</div>
</body>
</html>

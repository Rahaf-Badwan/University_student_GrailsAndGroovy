<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'enrollment.label', default: 'Enrollment')}" />
    <title><g:message code="default.edit.label" args="[entityName]" /></title>
</head>
<body>
<div id="content" role="main">
    <div class="container mt-4">

        <h2>Edit Enrollment</h2>

    <!-- رسائل عامة -->
        <g:if test="${flash.message}">
            <div class="alert alert-info mt-2">${flash.message}</div>
        </g:if>

    <!-- عرض أخطاء التحقق -->
        <g:hasErrors bean="${enrollment}">
            <div class="alert alert-danger mt-3">
                <ul>
                    <g:eachError bean="${enrollment}" var="error">
                        <li><g:message error="${error}" /></li>
                    </g:eachError>
                </ul>
            </div>
        </g:hasErrors>

    <!-- النموذج -->
        <g:form action="update" method="PUT" class="mt-4">
            <g:hiddenField name="id" value="${enrollment?.id}" />
            <g:hiddenField name="version" value="${enrollment?.version}" />

            <!-- عرض الطالب بدون تعديل -->
            <div class="mb-3">
                <label class="form-label">🧑‍🎓 Student:</label>
                <input type="text" class="form-control" value="${enrollment?.student?.name}" readonly />
            </div>

            <!-- عرض الكورس بدون تعديل -->
            <div class="mb-3">
                <label class="form-label">🔷 Course:</label>
                <input type="text" class="form-control" value="${enrollment?.course?.title}" readonly />
            </div>

            <!-- تعديل العلامة فقط -->
            <div class="mb-3">
                <label for="grade" class="form-label">📊 Grade:</label>
                <input type="number" name="grade" step="0.1" min="0" max="4"
                       value="${enrollment?.grade}" required class="form-control" />
                <g:fieldError field="grade" bean="${enrollment}" class="text-danger" />
            </div>

            <button type="submit" class="btn btn-success">Update Grade</button>
        </g:form>
    </div>
</div>
</body>
</html>

<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <title>Create Enrollment</title>
</head>
<body>
<div class="container mt-4">
    <h2>Create Enrollment</h2>

<!-- رسائل عامة -->
    <g:if test="${flash.message}">
        <div class="alert alert-warning mt-3">${flash.message}</div>
    </g:if>

    <g:hasErrors bean="${enrollment}">
        <div class="alert alert-danger mt-3">
            <ul>
                <g:eachError bean="${enrollment}" var="error">
                    <li><g:message error="${error}" /></li>
                </g:eachError>
            </ul>
        </div>
    </g:hasErrors>

    <g:form controller="enrollment" action="save" class="mt-4">

        <!-- اختيار الطالب -->
        <div class="mb-3">
            <label for="student.id" class="form-label">🧑‍🎓 Student:</label>
            <g:select name="student.id"
                      from="${students}"
                      optionKey="id"
                      optionValue="name"
                      value="${enrollment?.student?.id}"
                      noSelection="['':'👤 Please select a student']" />
            <g:fieldError field="student" bean="${enrollment}" class="text-danger" />
        </div>

        <!-- اختيار الكورس -->
        <div class="mb-3">
            <label for="course.id" class="form-label">🔷 Course:</label>
            <g:select name="course.id"
                      from="${courses}"
                      optionKey="id"
                      optionValue="title"
                      value="${enrollment?.course?.id}"
                      noSelection="['':'📘 Please select a course']" />
            <g:fieldError field="course" bean="${enrollment}" class="text-danger" />
        </div>

        <!-- إدخال العلامة -->
        <div class="mb-3">
            <label for="grade" class="form-label">📊 Grade:</label>
            <input type="number" name="grade" step="0.1" min="0" max="4"
                   value="${enrollment?.grade}" />
            <g:fieldError field="grade" bean="${enrollment}" class="text-danger" />
        </div>

        <!-- إدخال تاريخ التسجيل -->
        <div class="mb-3">
            <label for="enrollmentDate" class="form-label">📅 Enrollment Date:</label>
            <g:datePicker name="enrollmentDate"
                          value="${enrollment?.enrollmentDate ?: new Date()}"
                          precision="day" />
            <g:fieldError field="enrollmentDate" bean="${enrollment}" class="text-danger" />
        </div>

        <button type="submit" class="btn btn-primary">Enroll Student</button>
    </g:form>
</div>
</body>
</html>

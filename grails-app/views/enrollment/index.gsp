<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'enrollment.label', default: 'Enrollment')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<div id="content" role="main">
    <div class="container">
        <section class="row">
            <a href="#list-enrollment" class="skip" tabindex="-1">
                <g:message code="default.link.skip.label" default="Skip to content…" />
            </a>
            <div class="nav" role="navigation">
                <ul>
                    <li><a class="home" href="${createLink(uri: '/')}">
                        <g:message code="default.home.label" />
                    </a></li>

                <!-- زر إنشاء يظهر فقط للأدمن -->
                    <g:if test="${isAdmin}">
                        <li>
                            <g:link class="create" action="create">
                                <g:message code="default.new.label" args="[entityName]" />
                            </g:link>
                        </li>
                    </g:if>
                </ul>
            </div>
        </section>

        <section class="row">
            <div id="list-enrollment" class="col-12 content scaffold-list" role="main">
                <h1><g:message code="default.list.label" args="[entityName]" /></h1>

                <g:if test="${flash.message}">
                    <div class="message" role="status">${flash.message}</div>
                </g:if>

                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>Student Name</th>
                        <th>Course Title</th>
                        <th>Enrollment Date</th>

                    <!-- عمود الأوامر يظهر فقط للأدمن -->
                        <g:if test="${isAdmin}">
                            <th>Actions</th>
                        </g:if>
                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${enrollmentList}" var="enrollment">
                        <tr>
                            <td>
                                <!-- اسم الطالب كرابط للتعديل فقط للأدمن، أما المستخدم العادي فقط يعرض الاسم -->
                                <g:if test="${isAdmin}">
                                    <g:link action="edit" resource="${enrollment}">${enrollment.student?.name}</g:link>
                                </g:if>
                                <g:else>
                                    ${enrollment.student?.name}
                                </g:else>
                            </td>
                            <td>${enrollment.course?.title}</td>
                            <td>
                                <g:formatDate date="${enrollment.enrollmentDate}" format="yyyy-MM-dd" />
                            </td>

                        <!-- أزرار تعديل وحذف تظهر فقط للأدمن -->
                            <g:if test="${isAdmin}">
                                <td>
                                    <g:link class="btn btn-sm btn-primary" action="edit" resource="${enrollment}">Edit</g:link>
                                    <g:form action="delete" resource="${enrollment}" method="DELETE" style="display:inline;">
                                        <input type="submit" class="btn btn-sm btn-danger" value="Delete"
                                               onclick="return confirm('Are you sure you want to delete this enrollment?');" />
                                    </g:form>
                                </td>
                            </g:if>
                        </tr>
                    </g:each>
                    </tbody>
                </table>

                <g:if test="${enrollmentCount > params.int('max')}">
                    <div class="pagination">
                        <g:paginate total="${enrollmentCount ?: 0}" />
                    </div>
                </g:if>
            </div>
        </section>

        <g:if test="${isAdmin}">
            <!-- جدول GPA لكل الطلاب -->
            <section class="row" style="margin-top: 3rem;">
                <div id="list-gpa" class="col-12 content scaffold-list" role="main">
                    <h2>Students GPA</h2>
                    <table class="table table-bordered table-hover">
                        <thead>
                        <tr>
                            <th>Student Name</th>
                            <th>GPA</th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${gpaList}" var="gpaItem">
                            <tr>
                                <td>${gpaItem.studentName}</td>
                                <td>${gpaItem.gpa}</td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                </div>
            </section>
        </g:if>


    </div>
</div>
</body>
</html>

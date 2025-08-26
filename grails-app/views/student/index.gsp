<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Students</title>
    <style>
    body { background-color: #f9f9f9; font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif; }
    h1 { font-weight: bold; margin-bottom: 20px; text-align: center; color: #0d6efd; }
    .card { border-radius: 15px; padding: 20px; background-color: #fff; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
    .table { border-collapse: collapse; width: 100%; }
    .table th { background-color: #f0f0f0; color: #000; font-weight: bold; cursor: pointer; padding: 8px 12px; text-align: left; }
    .table td { color: #000; padding: 8px 12px; vertical-align: middle; }
    .table td a { color: #0d6efd; text-decoration: underline; }
    .btn { border-radius: 8px; padding: 6px 12px; }
    </style>
</head>

<body>
<div class="container mt-4">

    <g:if test="${isAdmin}">
        <!-- Admin: Table + Search/Sort -->
        <section class="row">
            <div class="nav" role="navigation">
                <ul>
                    <li><a class="home" href="${createLink(uri: '/')}">Home</a></li>
                    <li><g:link class="create" controller="student" action="create">New Student</g:link></li>
                </ul>
            </div>
        </section>

        <!-- Search & Sort -->
        <div class="row search-bar mb-3">
            <div class="col-md-6 d-flex align-items-center">
                <g:form controller="student" action="index" method="get" class="d-flex">
                    <input type="text" name="query" value="${query ?: ''}" class="form-control me-2" placeholder="Search..."/>
                    <label for="sortBy" class="me-2">Sort:</label>
                    <select name="sortBy" id="sortBy" class="form-select me-2" onchange="sortTableBySelect()">
                        <option value="">-- Select --</option>
                        <option value="name" ${sortBy == 'name' ? 'selected' : ''}>Name</option>
                    </select>
                    <button type="submit" class="btn btn-primary">Search</button>
                </g:form>
            </div>
        </div>

        <!-- Table for Admin -->
        <table id="studentTable" class="table table-bordered table-hover shadow-sm">
            <thead>
            <tr>
                <th>Photo</th>
                <th onclick="sortTable(0)">Name</th>
                <th onclick="sortTable(1)">Email</th>
                <th onclick="sortTable(2)">Username</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${studentList}" var="student">
                <tr>
                    <td>
                        <g:if test="${student.profilePhoto}">
                            <img src="${createLink(controller: 'student', action: 'profilePhoto', id: student.id)}"
                                 alt="Profile Photo" style="width:40px; height:40px; border-radius:50%; object-fit:cover;"/>
                        </g:if>
                        <g:else>
                            <img src="${resource(dir: 'images', file: 'default-profile.png')}"
                                 alt="No Photo" style="width:40px; height:40px; border-radius:50%; opacity:0.5;"/>
                        </g:else>
                    </td>
                    <td><g:link controller="student" action="edit" id="${student.id}">${student.name}</g:link></td>
                    <td>${student.email}</td>
                    <td>${student.user?.username}</td>
                    <td>
                        <g:link controller="student" action="edit" id="${student.id}" class="btn btn-warning btn-sm me-1">Edit</g:link>
                        <g:form controller="student" action="delete" id="${student.id}" method="DELETE" style="display:inline">
                            <g:actionSubmit value="Delete" class="btn btn-danger btn-sm"
                                            onclick="return confirm('Are you sure you want to delete this student?');"/>
                        </g:form>
                    </td>
                </tr>
            </g:each>
            </tbody>
        </table>

        <p>Total Students: ${studentCount}</p>
    </g:if>

    <g:else>
        <!-- Normal User: فقط Home + بطاقة البروفايل -->
        <section class="row">
            <div class="nav" role="navigation">
                <ul>
                    <li><a class="home" href="${createLink(uri: '/')}">Home</a></li>
                </ul>
            </div>
        </section>

        <section class="row justify-content-center mt-3">
            <div class="col-md-6">
                <div class="card shadow p-3 text-center">
                    <div class="card-body">
                        <h1>Student Profile</h1> <!-- العنوان الجديد -->
                        <g:if test="${studentList[0]?.profilePhoto}">
                            <img src="${createLink(controller: 'student', action: 'profilePhoto', id: studentList[0]?.id)}"
                                 alt="Profile Photo" style="width:100px; height:100px; border-radius:50%; object-fit:cover; margin-bottom:10px;"/>
                        </g:if>
                        <g:else>
                            <img src="${resource(dir: 'images', file: 'default-profile.png')}"
                                 alt="No Photo" style="width:100px; height:100px; border-radius:50%; opacity:0.5; margin-bottom:10px;"/>
                        </g:else>

                        <h4 class="card-title">${studentList[0]?.name}</h4>
                        <p><strong>Email:</strong> ${studentList[0]?.email}</p>
                        <p><strong>Username:</strong> ${studentList[0]?.user?.username}</p>
                        <p>
                            <strong>Password:</strong>
                            <input type="text" id="passwordField" value="${studentList[0]?.user?.password}" readonly
                                   style="border:none; background:none; -webkit-text-security: disc;"/>
                            <button type="button" id="togglePassword" style="border:none; background:none; cursor:pointer;">
                                👁
                            </button>
                        </p>
                    </div>
                </div>
            </div>
        </section>
    </g:else>

    <script>
        const togglePasswordBtn = document.getElementById('togglePassword');
        const passwordField = document.getElementById('passwordField');

        togglePasswordBtn.addEventListener('click', () => {
            if (passwordField.style.webkitTextSecurity === 'disc') {
                passwordField.style.webkitTextSecurity = 'none'; // إظهار النص
            } else {
                passwordField.style.webkitTextSecurity = 'disc'; // إخفاء النص
            }
        });
    </script>


</div>

<script>
    function sortTable(colIndex) {
        var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
        table = document.getElementById("studentTable");
        switching = true;
        dir = "asc";
        while (switching) {
            switching = false;
            rows = table.rows;
            for (i = 1; i < (rows.length - 1); i++) {
                shouldSwitch = false;
                x = rows[i].getElementsByTagName("TD")[colIndex];
                y = rows[i + 1].getElementsByTagName("TD")[colIndex];
                if (dir == "asc") {
                    if (x.innerText.toLowerCase() > y.innerText.toLowerCase()) { shouldSwitch = true; break; }
                } else if (dir == "desc") {
                    if (x.innerText.toLowerCase() < y.innerText.toLowerCase()) { shouldSwitch = true; break; }
                }
            }
            if (shouldSwitch) { rows[i].parentNode.insertBefore(rows[i + 1], rows[i]); switching = true; switchcount++; }
            else { if (switchcount == 0 && dir == "asc") { dir = "desc"; switching = true; } }
        }
    }

    function sortTableBySelect() {
        var select = document.getElementById("sortBy");
        if (select.value === "name") sortTable(0);
    }
</script>
</body>
</html>

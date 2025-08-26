<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Students</title>
    <style>
    /* General body & headings */
    body {
        background-color: #f9f9f9;
        font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
    }

    h1 {
        font-weight: bold;
        margin-bottom: 20px;
        text-align: center;
        color: #0d6efd;
    }

    /* Card style */
    .card {
        border-radius: 15px;
        padding: 20px;
        background-color: #fff;
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }

    /* Table style */
    .table {
        border-collapse: collapse;
        width: 100%;
    }

    .table th {
        background-color: #f0f0f0; /* فاتح للـ header */
        color: #000; /* نص أسود */
        font-weight: bold;
        cursor: pointer;
        padding: 8px 12px; /* أصغر padding */
        text-align: left;
    }

    .table td {
        color: #000; /* نص أسود */
        padding: 8px 12px; /* أصغر padding */
        vertical-align: middle;
    }

    .table td a {
        color: #0d6efd; /* رابط أزرق */
        text-decoration: underline;
    }

    /* Search bar & dropdown */
    .search-bar {
        margin-bottom: 15px;
    }

    .form-control {
        height: 35px; /* أصغر مربع البحث */
    }

    .form-select {
        height: 35px; /* أصغر dropdown */
        margin-left: 10px;
        margin-right: 10px;
    }

    /* Buttons */
    .btn {
        border-radius: 8px;
        padding: 6px 12px; /* أصغر حجم */
    }

    /* Optional: hover effect on table rows */
    .table tbody tr:hover {
        background-color: #f1f1f1;
    }
    </style>

</head>
<body>
<div class="container mt-4">
    <h1>Student List</h1>

    <g:if test="${isAdmin}">
        <!-- Search & Sort -->
        <div class="row search-bar">
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
                            <img src="${createLink(controller:'student', action:'profilePhoto', id:student.id)}"
                                 alt="Profile Photo" style="width:40px; height:40px; border-radius:50%; object-fit:cover;"/>
                        </g:if>
                        <g:else>
                            <img src="${resource(dir:'images', file:'default-profile.png')}"
                                 alt="No Photo" style="width:40px; height:40px; border-radius:50%; opacity:0.5;"/>
                        </g:else>
                    </td>
                    <td>
                        <g:link controller="student" action="edit" id="${student.id}">
                            ${student.name}
                        </g:link>
                    </td>
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
        <!-- Profile Card for Normal User -->
        <section class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow p-3 text-center">
                    <div class="card-body">
                    <!-- Profile Photo -->
                        <g:if test="${studentList[0]?.profilePhoto}">
                            <img src="${createLink(controller:'student', action:'profilePhoto', id:studentList[0]?.id)}"
                                 alt="Profile Photo" style="width:100px; height:100px; border-radius:50%; object-fit:cover; margin-bottom:10px;"/>
                        </g:if>
                        <g:else>
                            <img src="${resource(dir:'images', file:'default-profile.png')}"
                                 alt="No Photo" style="width:100px; height:100px; border-radius:50%; opacity:0.5; margin-bottom:10px;"/>
                        </g:else>

                        <h4 class="card-title">${studentList[0]?.name}</h4>
                        <p><strong>Email:</strong> ${studentList[0]?.email}</p>
                        <p><strong>Username:</strong> ${studentList[0]?.user?.username}</p>
                    </div>
                </div>
            </div>
        </section>
    </g:else>
</div>

<script>
    // Function to sort table by column
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
                    if (x.innerText.toLowerCase() > y.innerText.toLowerCase()) {
                        shouldSwitch = true;
                        break;
                    }
                } else if (dir == "desc") {
                    if (x.innerText.toLowerCase() < y.innerText.toLowerCase()) {
                        shouldSwitch = true;
                        break;
                    }
                }
            }
            if (shouldSwitch) {
                rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                switching = true;
                switchcount++;
            } else {
                if (switchcount == 0 && dir == "asc") {
                    dir = "desc";
                    switching = true;
                }
            }
        }
    }

    // Sort based on select dropdown (only Name)
    function sortTableBySelect() {
        var select = document.getElementById("sortBy");
        if(select.value === "name") sortTable(0);
    }

</script>
</body>
</html>

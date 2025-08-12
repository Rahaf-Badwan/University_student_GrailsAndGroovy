<!DOCTYPE html>
<html>
<head>
    <title>All Students GPA</title>
</head>
<body>
<h1>Students GPA List</h1>

<table border="1" cellpadding="5">
    <thead>
    <tr>
        <th>Student Name</th>
        <th>GPA</th>
    </tr>
    </thead>
    <tbody>
    <g:each in="${gpaList}" var="entry">
        <tr>
            <td>${entry.studentName}</td>
            <td>${entry.gpa ?: 'N/A'}</td>
        </tr>
    </g:each>
    </tbody>
</table>

</body>
</html>

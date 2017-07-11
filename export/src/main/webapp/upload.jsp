<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>load xml</title>
</head>
<body>
<form action="/upload" enctype="multipart/form-data" method="post">
    <p>
        <label>Select a file: </label>
        <input type="file" name="file"/>
    </p>
    <input type="submit" value="Upload" />
</form>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Users</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Users</h2>
<form action="users" method="post">
    <label>
        User:
        <select name="authUser">
            <c:forEach items="${users}" var="user">
                <option ${user==authUser ? 'selected' : ''}>${user}</option>
            </c:forEach>
        </select>
    </label>
    <button>Изменить</button>
</form>
</body>
</html>
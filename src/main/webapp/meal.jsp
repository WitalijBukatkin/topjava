<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <meta charset="utf-8">
        <title>Meal</title>
        <style>
            label{
                display: block;
                float: left;
                width: 100px;
            }
        </style>
    </head>
    <body>
        <h3><a href="index.html">Home</a></h3>
        <h2>Meal insert/update</h2>
        <form action="meal" method="post">
            <input type="hidden" name="id" value="<c:out value="${id}"/>">

            <label>DateTime</label>
            <input type="datetime-local" name="dateTime" value="<c:out value="${dateTime}"/>">
            <br>

            <label>Description</label>
            <input name="description" value="<c:out value="${description}"/>">
            <br>

            <label>Calories</label>
            <input name="calories" value="<c:out value="${calories}"/>">
            <br>

            <input type="submit">
        </form>
    </body>
</html>
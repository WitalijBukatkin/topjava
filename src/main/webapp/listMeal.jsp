<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <meta charset="utf-8">
        <title>Meal</title>
        <style>
            .exceed{
                color: red;
            }
            .notExceed{
                color: green;
            }
        </style>
    </head>
    <body>
        <h3><a href="index.html">Home</a></h3>
        <h2>Meal</h2>
        <table>
            <tr>
                <th>Id</th>
                <th>DateTime</th>
                <th>Description</th>
                <th>Calories</th>
                <th colspan=2>Action</th>
            </tr>
            <c:forEach var="meal" items="${meals}">
                <tr class="<c:out value="${meal.exceed ? 'exceed' : 'notExceed'}"/>">
                    <td><c:out value = "${meal.id}"/></td>
                    <td><c:out value = "${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}"/></td>
                    <td><c:out value = "${meal.description}"/></td>
                    <td><c:out value = "${meal.calories}"/></td>
                    <td><a href="?action=edit&id=<c:out value = "${meal.id}"/>">edit</a></td>
                    <td><a href="?action=delete&id=<c:out value = "${meal.id}"/>">delete</a></td>
                </tr>
            </c:forEach>
        </table>
        <a href="?action=insert">Insert</a>
    </body>
</html>
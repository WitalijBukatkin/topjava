<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
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
                <th>DateTime</th>
                <th>Description</th>
                <th>Calories</th>
            </tr>
            <c:forEach var="meal" items="${meals}">
                <tr class="<c:out value="${meal.exceed ? 'exceed' : 'notExceed'}"/>">
                    <td><c:out value = "${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}"/></td>
                    <td><c:out value = "${meal.description}"/></td>
                    <td><c:out value = "${meal.calories}"/></td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
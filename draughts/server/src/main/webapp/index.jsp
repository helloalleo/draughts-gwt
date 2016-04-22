<%--
  Created by IntelliJ IDEA.
  User: alekspo
  Date: 22.04.16
  Time: 12:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    Cookie[] cookies = request.getCookies();
    if (null != cookies) {
        String locale = "ru";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase("locale")) {
                locale = cookie.getValue();
            }
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase("loggedIn")) {
                if (cookie.getValue().equals("1")) {
                    response.sendRedirect("/d/");
                } else if (cookie.getValue().equals("0")) {
                    response.sendRedirect("/l/");
                }
            } else {
                response.sendRedirect("/l/index.html");
            }
        }
    }
%>
</body>
</html>

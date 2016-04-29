<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    Cookie[] cookies = request.getCookies();
    if (null != cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase("loggedIn")) {
                if (cookie.getValue().equals("1")) {
                    response.sendRedirect("/d/");
                } else if (cookie.getValue().equals("0")) {
                    response.sendRedirect("/l/");
                }
            } else {
                response.sendRedirect("/l/");
            }
        }
    }
%>
</body>
</html>

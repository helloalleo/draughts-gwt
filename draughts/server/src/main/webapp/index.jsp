<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Шашки Онлайн</title>

    <meta name="keywords" content="шашки,онлайн,играть,бесплатно,игры,русские,социальные,сети,обучение,с другом,
    по сети,на двоих,по интернету,через интернет,з другом,по интернету,бесплатно,без регистрации">
    <meta name="description" content="Играйте в шашки онлайн со своими друзьями или просто посетителями сайта.
    На сайте: вход через социальные сети, Push-уведомления, история игр.">
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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Шашки онлайн</title>

    <meta name="keywords" content="шашки,онлайн,играть,бесплатно,игры,русские,социальные,сети,обучение,с другом,
    по сети,на двоих,по интернету,через интернет,з другом,по интернету,бесплатно,без регистрации">
    <meta name="description" content="Играйте в шашки онлайн со своими друзьями или просто посетителями сайта.
    На сайте есть: вход через социальные сети, Push-уведомления, история игр.">
</head>
<body>
<!-- Yandex.Metrika counter -->
<script type="text/javascript">
    (function (d, w, c) {
        (w[c] = w[c] || []).push(function() {
            try {
                w.yaCounter37190295 = new Ya.Metrika({
                    id:37190295,
                    clickmap:true,
                    trackLinks:true,
                    accurateTrackBounce:true,
                    webvisor:true,
                    trackHash:true
                });
            } catch(e) { }
        });

        var n = d.getElementsByTagName("script")[0],
                s = d.createElement("script"),
                f = function () { n.parentNode.insertBefore(s, n); };
        s.type = "text/javascript";
        s.async = true;
        s.src = "https://mc.yandex.ru/metrika/watch.js";

        if (w.opera == "[object Opera]") {
            d.addEventListener("DOMContentLoaded", f, false);
        } else { f(); }
    })(document, window, "yandex_metrika_callbacks");
</script>
<noscript><div><img src="https://mc.yandex.ru/watch/37190295" style="position:absolute; left:-9999px;" alt="" /></div></noscript>
<!-- /Yandex.Metrika counter -->
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

<%
    Cookie[] cookies = request.getCookies();
    if (null != cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase("loggedIn")) {
                if (cookie.getValue().equals("1")) {
                    response.sendRedirect("/shashki/");
                }
            }
        }
    }
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Шашки онлайн</title>
    <link rel="stylesheet" href="assets/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/fonts/font-awesome.min.css">
    <link rel="stylesheet" href="assets/css/user.css">

    <link rel="shortcut icon" type="image/png" href="/favicon.png">

    <meta name="keywords" content="шашки,онлайн,играть,бесплатно,игры,русские,социальные,сети,обучение,с другом,
    по сети,на двоих,по интернету,через интернет,з другом,по интернету,бесплатно,без регистрации">
    <meta name="description" content="Играйте в шашки онлайн со своими друзьями или просто посетителями сайта.
    На сайте есть: вход через социальные сети, история игр, push-уведомления.">
</head>

<body>
<!-- Yandex.Metrika counter -->
<script type="text/javascript">
    (function (d, w, c) {
        (w[c] = w[c] || []).push(function() {
            try {
                w.yaCounter37584045 = new Ya.Metrika({
                    id:37584045,
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
<noscript><div><img src="https://mc.yandex.ru/watch/37584045" style="position:absolute; left:-9999px;" alt="" /></div></noscript>
<!-- /Yandex.Metrika counter -->

<nav class="navbar navbar-default">
    <div class="container">
        <div class="navbar-header"><a class="navbar-brand navbar-link" href="#"></a>
            <button class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navcol-1"><span
                    class="sr-only">Навигация</span><span class="icon-bar"></span><span class="icon-bar"></span><span
                    class="icon-bar"></span></button>
        </div>
        <div class="collapse navbar-collapse" id="navcol-1">
            <ul class="nav navbar-nav navbar-right">
                <li role="presentation"><a href="/shashki/">Главная </a></li>
                <li role="presentation"><a href="/shashki/#!login">Войти </a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="jumbotron hero">
    <div class="container">
        <div class="row">
            <div class="col-md-4 col-md-push-7 phone-preview">
                <div class="iphone-mockup"><img src="assets/img/logo.png" class="device"></div>
            </div>
            <div class="col-md-6 col-md-pull-3 get-it">
                <h1>Шашки онлайн</h1>
                <h2>Играйте в шашки и общайтесь на нашем сайте</h2>
                <p><a class="btn btn-primary btn-lg" role="button" href="/shashki/#!login">Войти на сайт</a></p>
            </div>
        </div>
    </div>
</div>
<section class="features">
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <h2 class="text-center">На сайте можно играть в шашки </h2>
                <div class="row">
                    <video style="width: 100%;" height="320" autoplay="" muted="" loop="">
                        <source src="/assets/video/draughts.mp4" type="video/mp4">
                    </video>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-10 col-md-offset-1">
                <h2 class="text-center">Смотреть историю своих игр </h2><img class="img-responsive"
                                                                              src="assets/img/game-log.png"></div>
        </div>
        <div class="row">
            <div class="col-md-10 col-md-offset-1">
                <h2 class="text-center">Делиться своими мыслями о сыгранных партиях </h2><img class="img-responsive"
                                                                                               src="assets/img/comments.png">
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <h2 class="text-center">Общаться!</h2><img class="img-responsive" src="assets/img/chat.png"
                                                           text-align="center"></div>
        </div>
        <div class="row">
            <div class="col-xs-8 col-xs-offset-2 col-md-2 col-md-offset-5"><a style="width: 100%;" class="btn btn-primary btn-lg on-site-btn" role="button"
                                                     href="/shashki/#!login">Войти на сайт</a></div>
        </div>
    </div>
</section>
<footer class="site-footer">
    <div class="container">
        <div class="row">
            <div class="col-sm-6">
                <h5>Рабочий Бит © 2016</h5></div>
            <div class="col-sm-6 social-icons"><a href="https://vk.com/draughts.online" target="_blank"><i
                    class="fa fa-vk"></i></a></div>
        </div>
    </div>
</footer>
<script src="assets/js/jquery.min.js"></script>
<script src="assets/bootstrap/js/bootstrap.min.js"></script>
<script>
    (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
                (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
            m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
    })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

    ga('create', 'UA-78356339-2', 'auto');
    ga('send', 'pageview');

</script>
</body>

</html>
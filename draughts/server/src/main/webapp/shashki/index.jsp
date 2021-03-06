<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String loggedIn = "false";
    Cookie[] cookies = request.getCookies();
    if (null != cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase("loggedIn")) {
                loggedIn = cookie.getValue().equals("1") ? "true" : "false";
            }
        }
    }
%>
<!doctype html>
<html>
<head>
    <meta name="gwt:property" content="locale=ru">
    <!-- GWTP-->
    <meta name="fragment" content="!">
    <meta name="google-site-verification" content="hpqclqQ9Qg8yYuHMMaZO3L3OjcMf7Gz0xxhmcHEu6BQ"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0,maximum-scale=1.0">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name="keywords" content="шашки,онлайн,играть,бесплатно,игры,русские,социальные,сети,с другом,по сети,на двоих,по интернету,через интернет,з другом,по интернету,без регистрации">
    <meta name="description" content="Играйте в шашки онлайн со своими друзьями и корифеями сайта. На сайте есть: вход через социальные сети, история игр., push-уведомления">

    <title>Шашки онлайн</title>

    <link rel="shortcut icon" type="text/x-icon" href="/favicon.ico">
    <link rel="icon" type="image/png" href="/favicon.png">

    <link rel="alternate" href="https://shashki.online/?locale=ru" hreflang="ru"/>
    <link rel="alternate" href="https://shashki.online/?locale=ua" hreflang="ru-UA"/>
    <link rel="alternate" href="https://shashki.online/?locale=en" hreflang="en"/>

    <script src="/assets/bower_components/firebase/firebase.js"></script>

    <!-- Put this script tag to the <head> of your page -->
    <script type="text/javascript" src="http://vk.com/js/api/share.js?93" charset="windows-1251"></script>
    <script type="text/javascript" src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>

    <!-- Channel API -->
    <%--<script type="text/javascript" src="/_ah/channel/jsapi"></script>--%>
    <script type="text/javascript" language="javascript" src="/shashki/Application/Application.nocache.js"></script>

    <link href='//fonts.googleapis.com/css?family=Lato:300,400,700' rel='stylesheet' type='text/css'>
    <link rel="manifest" href="/manifest.json">
    <script src="https://cdn.onesignal.com/sdks/OneSignalSDK.js" async='async'></script>
    <link rel="stylesheet" href="style.css" type="text/css">
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

<!-- OPTIONAL: include this if you want history support -->
<iframe id="__gwt_historyFrame" tabIndex='-1'
        style="position:absolute;width:0;height:0;border:0"></iframe>

<!-- RECOMMENDED if your web app will not function without JavaScript enabled -->
<noscript>
    <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
        Для того, чтобы корректно отобразить страницу,
        Вам необходимо включить поддержку JavaScript.
        <br>
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
    </div>
</noscript>
<script type="text/javascript">
    var reformalOptions = {
        project_id: 969010,
        project_host: "draughtsonline.reformal.ru",
        tab_orientation: "left",
        tab_indent: "50%",
        tab_bg_color: "#3da8a6",
        tab_border_color: "#FFFFFF",
        tab_image_url: "http://tab.reformal.ru/T9GC0LfRi9Cy0Ysg0Lgg0L%252FRgNC10LTQu9C%252B0LbQtdC90LjRjw==/FFFFFF/2a94cfe6511106e7a48d0af3904e3090/left/1/tab.png",
        tab_border_width: 2
    };

    (function () {
        var script = document.createElement('script');
        script.type = 'text/javascript';
        script.async = true;
        script.src = ('https:' == document.location.protocol ? 'https://' : 'http://') +
                'media.reformal.ru/widgets/v3/reformal.js';
        document.getElementsByTagName('head')[0].appendChild(script);
    })();
</script>
<noscript>
    <a href="http://reformal.ru">
        <img src="http://media.reformal.ru/reformal.png"/>
    </a>
    <a href="http://draughtsonline.reformal.ru">Oтзывы и предложения для shashki.online</a>
</noscript>
<script>
    (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
                (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
            m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
    })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

    ga('create', 'UA-78356339-1', 'auto');
    ga('send', 'pageview');

</script>
<%--<script>--%>
    <%--var OneSignal = OneSignal || [];--%>

    <%--OneSignal.push(["init", {--%>
        <%--appId: "78dcc7d5-0794-45d8-aaff-4df6f4bef7a7",--%>
        <%--safari_web_id: "web.onesignal.auto.5130fec1-dc87-4e71-b719-29a6a70279c4",--%>
        <%--autoRegister: true,--%>
        <%--promptOptions: {--%>
            <%--actionMessage: 'Мы хотим присылать вам уведомления',--%>
            <%--exampleNotificationTitleDesktop: 'Это пример уведомления',--%>
            <%--exampleNotificationMessageDesktop: 'Такое уведомление появится на вашем компьютере',--%>
            <%--exampleNotificationCaption: 'Вы можете отписаться в любое время',--%>
            <%--acceptButtonText: "Продолжить",--%>
            <%--cancelButtonText: "Нет, спасибо",--%>
            <%--showCredit: false--%>
        <%--},--%>
        <%--welcomeNotification: {--%>
            <%--title: 'Подписка на shashki.online',--%>
            <%--message: 'Спасибо, что подписались!'--%>
        <%--},--%>
        <%--persistNotification: false,--%>
        <%--notifyButton: {--%>
            <%--enable: <%= loggedIn %>, // Set to false to hide--%>
            <%--text: {--%>
                <%--'tip.state.unsubscribed': 'Подписаться на уведомления',--%>
                <%--'tip.state.subscribed': "Вы подписаны на уведомления",--%>
                <%--'tip.state.blocked': "Вы заблокировали уведомления",--%>
                <%--'message.prenotify': 'Кликните, чтобы подписаться на уведомления',--%>
                <%--'message.action.subscribed': "Спасибо, что подписались!",--%>
                <%--'message.action.resubscribed': "Вы подписаны на уведомления",--%>
                <%--'message.action.unsubscribed': "Вы больше не получаете уведомлений",--%>
                <%--'dialog.main.title': 'Настройки уведомлений',--%>
                <%--'dialog.main.button.subscribe': 'ПОДПИСАТЬСЯ',--%>
                <%--'dialog.main.button.unsubscribe': 'ОТПИСАТЬСЯ',--%>
                <%--'dialog.blocked.title': 'Разблокировать уведомления',--%>
                <%--'dialog.blocked.message': "Следуйте следующим инструкциям, чтобы разрешить уведомления:"--%>
            <%--}--%>
        <%--}--%>
    <%--}]);--%>

    <%--OneSignal.push(function () {--%>
        <%--// If we're on an unsupported browser, do nothing--%>
        <%--if (!OneSignal.isPushNotificationsSupported()) {--%>
            <%--return;--%>
        <%--}--%>

        <%--OneSignal.on('subscriptionChange', function (isSubscribed) {--%>
            <%--// The user is subscribed--%>
            <%--//   Either the user subscribed for the first time--%>
            <%--//   Or the user was subscribed -> unsubscribed -> subscribed--%>
            <%--OneSignal.getUserId(function (userId) {--%>
                <%--// Make a POST call to your server with the user ID--%>
                <%--var xhr = new XMLHttpRequest();--%>
                <%--var json = JSON.stringify({--%>
                    <%--notificationsUserId: isSubscribed ? userId : '',--%>
                    <%--sessionId: '<%= request.getRequestedSessionId() %>'--%>
                <%--});--%>

                <%--xhr.open("POST", '/shashki/resource/players/notifications', true)--%>
                <%--xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');--%>
                <%--xhr.send(json);--%>
            <%--});--%>
        <%--});--%>
    <%--});--%>
<%--</script>--%>
<script>
    Share = {
        vkontakte: function(purl, ptitle, pimg, text) {
            url  = 'http://vkontakte.ru/share.php?';
            url += 'url='          + encodeURIComponent(purl);
            url += '&title='       + encodeURIComponent(ptitle);
            url += '&description=' + encodeURIComponent(text);
            url += '&image='       + encodeURIComponent(pimg);
            url += '&noparse=true';
            Share.popup(url);
        },
        odnoklassniki: function(purl, text) {
            url  = 'http://www.odnoklassniki.ru/dk?st.cmd=addShare&st.s=1';
            url += '&st.comments=' + encodeURIComponent(text);
            url += '&st._surl='    + encodeURIComponent(purl);
            Share.popup(url);
        },
        facebook: function(purl, ptitle, pimg, text) {
            url  = 'http://www.facebook.com/sharer.php?s=100';
            url += '&p[title]='     + encodeURIComponent(ptitle);
            url += '&p[summary]='   + encodeURIComponent(text);
            url += '&p[url]='       + encodeURIComponent(purl);
            url += '&p[images][0]=' + encodeURIComponent(pimg);
            Share.popup(url);
        },
        twitter: function(purl, ptitle) {
            url  = 'http://twitter.com/share?';
            url += 'text='      + encodeURIComponent(ptitle);
            url += '&url='      + encodeURIComponent(purl);
            url += '&counturl=' + encodeURIComponent(purl);
            Share.popup(url);
        },
        mailru: function(purl, ptitle, pimg, text) {
            url  = 'http://connect.mail.ru/share?';
            url += 'url='          + encodeURIComponent(purl);
            url += '&title='       + encodeURIComponent(ptitle);
            url += '&description=' + encodeURIComponent(text);
            url += '&imageurl='    + encodeURIComponent(pimg);
            Share.popup(url)
        },

        popup: function(url) {
            window.open(url,'','toolbar=0,status=0,width=626,height=436');
        }
    };
</script>
</body>
</html>

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
    <meta name="keywords" content="шашки,онлайн,играть,бесплатно,игры,русские,социальные,сети,обучение">
    <meta name="description"
          content="Играть в шашки онлайн. Вход через социальные сети. Push-уведомления. История игр.">

    <title>Шашки Онлайн</title>

    <link rel="shortcut icon" type="image/png" href="favicon.png">

    <link rel="alternate" href="http://xn--80aaxfchnde0hb.com/?locale=ru" hreflang="ru"/>
    <link rel="alternate" href="http://xn--80aaxfchnde0hb.com/?locale=ua" hreflang="ua"/>
    <link rel="alternate" href="http://xn--80aaxfchnde0hb.com/?locale=en" hreflang="en"/>

    <!-- Channel API -->
    <script type="text/javascript" src="/_ah/channel/jsapi"></script>

    <script type="text/javascript" language="javascript" src="Application/Application.nocache.js"></script>

    <link href='//fonts.googleapis.com/css?family=Lato:300,400,700' rel='stylesheet' type='text/css'>

    <link rel="stylesheet" href="style.css" type="text/css">
</head>
<body>

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
    <a href="http://draughtsonline.reformal.ru">Oтзывы и предложения для ШашкиОнлайн.COM</a>
</noscript>
<script>
    (function (i, s, o, g, r, a, m) {
        i['GoogleAnalyticsObject'] = r;
        i[r] = i[r] || function () {
                    (i[r].q = i[r].q || []).push(arguments)
                }, i[r].l = 1 * new Date();
        a = s.createElement(o),
                m = s.getElementsByTagName(o)[0];
        a.async = 1;
        a.src = g;
        m.parentNode.insertBefore(a, m)
    })(window, document, 'script', 'https://www.google-analytics.com/analytics.js', 'ga');

    ga('create', 'UA-76960011-1', 'auto');
    ga('send', 'pageview');

</script>
<script src="https://cdn.onesignal.com/sdks/OneSignalSDK.js" async='async'></script>
<a href="#" id="subscribe-link" style="position: fixed; bottom: 0; right: 0;">Подписаться на приглашения</a>
<script>
    function subscribe(event) {
        OneSignal.push(["registerForPushNotifications"]);
        event.preventDefault();
    }

    var OneSignal = OneSignal || [];

    OneSignal.push(["init", {
        appId: "78dcc7d5-0794-45d8-aaff-4df6f4bef7a7",
        subdomainName: "draughtsonline",
        safari_web_id: "web.onesignal.auto.5130fec1-dc87-4e71-b719-29a6a70279c4",
        notifyButton: {
            enable: true // Set to false to hide
        }
    }]);

//    OneSignal.push(["init", {
//        appId: "78dcc7d5-0794-45d8-aaff-4df6f4bef7a7",
////        subdomainName: "draughtsonline",
//        safari_web_id: "web.onesignal.auto.5130fec1-dc87-4e71-b719-29a6a70279c4",
//        autoRegister: false,
//        notifyButton: {
//            enable: false
//        },
//        text: {
//            'tip.state.unsubscribed': 'Подписаться на приглашения в игру',
//            'tip.state.subscribed': "Вы подписаны на приглашения",
//            'tip.state.blocked': "Вы заблокировали приглашения",
//            'message.prenotify': 'Кликните, чтобы подписаться на приглашения в игру',
//            'message.action.subscribed': "Спасибо, что подписались!",
//            'message.action.resubscribed': "Вы подписаны на приглашения",
//            'message.action.unsubscribed': "Вы больше не получаете приглашений",
//            'dialog.main.title': 'Управлять Настройками Приглашений',
//            'dialog.main.button.subscribe': 'ПОДПИСАТЬСЯ',
//            'dialog.main.button.unsubscribe': 'ОТПИСАТЬСЯ',
//            'dialog.blocked.title': 'Разблокировать приглашения',
//            'dialog.blocked.message': "Следуйте следующим инструкциям, чтобы разрешить приглашения:"
//        }
//    }]);

    OneSignal.push(function () {
        // If we're on an unsupported browser, do nothing
        if (!OneSignal.isPushNotificationsSupported()) {
            return;
        }
        OneSignal.isPushNotificationsEnabled(function (isEnabled) {
            if (isEnabled) {
                // The user is subscribed to notifications
                // Don't show anything
            } else {
                document.getElementById("subscribe-link").textContent = "Подписаться на уведомления";
                document.getElementById("subscribe-link").addEventListener('click', subscribe);
            }
        });

        OneSignal.on('subscriptionChange', function (isSubscribed) {
            if (isSubscribed) {
                // The user is subscribed
                //   Either the user subscribed for the first time
                //   Or the user was subscribed -> unsubscribed -> subscribed
                OneSignal.getUserId(function (userId) {
                    // Make a POST call to your server with the user ID
                    var xhr = new XMLHttpRequest();
                    var json = JSON.stringify({
                        notificationsUserId: userId,
                        sessionId: '<%= request.getRequestedSessionId() %>'
                    });

                    xhr.open("POST", '/d/resource/players/notifications', true)
                    xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
                    xhr.send(json);
                });
            }
        });
    });
</script>

</body>
</html>
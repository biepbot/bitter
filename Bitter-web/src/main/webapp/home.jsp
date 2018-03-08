<%-- 
    Document   : home
    Created on : 01-Mar-2018, 15:47:19
    Author     : Rowan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Bitter. We know we had to do it to them.</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link rel="stylesheet" href="CSS/fa/css/font-awesome.min.css" />
        <link rel="stylesheet" href="CSS/universal.min.css" />
        <link rel="stylesheet" href="CSS/user.css" />
        <script src="JS/universal.min.js"></script>

        <link rel="shortcut icon" type="image/png" href="IMG/bitter-logo.png"/>
    </head>
    <header>
         <div class="limit-3">
            <div class="link active"><img class="logo" src='IMG/bitter-logo.png' alt=''/><a href="#">Home</a></div>
            <div class="link"><a href="/notifications.jsp">Notifications</a></div>
            <div class="link right"><a href="#">Bark</a></div>
            <div class="link right"><a href="/profile.jsp">Profile</a></div>
            <div class="link right search"><a href="/search.jsp">Search Bitter</a></div>
         </div>
    </header>
    <body>
        <div class="limit-3">
            <div class="small-box">
                <div class="profile-box">
                     <!-- test data -->
                     <!-- test data -->
                     <!-- test data -->
                     <div id="header" class="header">
                         <img src="https://pbs.twimg.com/profile_banners/3126552135/1490991020/600x200"/>
                     </div>
                     <div class="profile-info">
                         <div class="username">
                            <img id="avatar" src="https://cdn.discordapp.com/emojis/414037045560999956.png?v=1"/>
                            <div>
                                <p id="username">Biepbot</p>
                                <p id="at_username">@biepbot</p>
                            </div>
                         </div>
                         <div class="major">
                            <div class="minor">
                                <p>Barks</p>
                                <p id="barks_count">3</p>
                            </div>
                            <div class="minor">
                                <p>Following</p>
                                <p id="following_count">19</p>
                            </div>
                            <div class="minor">
                                <p>Followers</p>
                                <p id="follower_count">2.591</p>
                            </div>
                         </div>
                     </div>
                </div>
                     <!-- test data -->
                     <!-- test data -->
                     <!-- test data -->
                <div class="trending-box"></div>
            </div>
            <div class="long-contents">
                <div id="new-bark" class="new-bark">
                     <input type="text" name="bark" class="form-control" id="bark" placeholder="What's up?">
                </div>
            </div>
        </div>
        <script async src="JS/home.js"></script>
    </body>
</html>

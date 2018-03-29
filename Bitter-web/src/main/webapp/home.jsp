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
                            <img id="avatar" src=""/>
                            <div>
                                <p id="username"></p>
                                <p id="at_username">@</p>
                            </div>
                        </div>
                        <div class="major">
                            <div class="minor">
                                <p>Barks</p>
                                <p class="flash" id="barks_count">0</p>
                            </div>
                            <div class="minor">
                                <p>Following</p>
                                <p id="following_count">0</p>
                            </div>
                            <div class="minor">
                                <p>Followers</p>
                                <p id="follower_count">0</p>
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
                    <textarea class="form-control" id="bark" placeholder="What's up?"></textarea>
                    <div id="bark-button" class="btn" onclick="bark()">Bark</div>
                    <p class="special">Preview</p>
                    <div id="test-bark" class="bark flash">
                        <div class="bark-image">
                            <img id="test-avatar" class="user-image"/>
                        </div>
                        <div id="test-username" class="bark-user">
                        </div>
                        <div id="test-content" class="bark-content">
                        </div>
                        <div class="bark-interact">
                            <li>
                                <ul class="ul-bite"><span class="fa fa-bolt"></span>bite</ul>
                                <ul class="ul-bark"><span class="fa fa-share-alt"></span>bark</ul>
                                <ul><span class="fa fa-reply"></span>reply</ul>
                            </li>
                        </div>
                    </div>
                </div>
            </div>
            <!-- darkener -->
            <div id="darkener" class="darkener hidden">
                <div id="modal-owner" class="modal">
                    <!-- user pop-up -->

                    <div class="small-box">
                        <div class="profile-box">
                            <!-- test data -->
                            <!-- test data -->
                            <!-- test data -->
                            <div id="modal-ownerheader" class="header">
                                <img src="https://pbs.twimg.com/profile_banners/3126552135/1490991020/600x200"/>
                            </div>
                            <div class="profile-info">
                                <div class="username">
                                    <img id="modal-owneravatar" src=""/>
                                    <div>
                                        <p id="modal-ownerusername"></p>
                                        <p id="modal-ownerat_username">@</p>
                                    </div>
                                </div>
                                <div class="major">
                                    <div class="minor">
                                        <p>Barks</p>
                                        <p class="flash" id="modal-ownerbarks_count">0</p>
                                    </div>
                                    <div class="minor">
                                        <p>Following</p>
                                        <p id="modal-ownerfollowing_count">0</p>
                                    </div>
                                    <div class="minor">
                                        <p>Followers</p>
                                        <p id="modal-ownerfollower_count" class="flash">0</p>
                                    </div>
                                </div>
                                <div class="divider"></div>
                                <div class="major">
                                    <div class="minor">
                                        <p class="medium-text">Bio</p>
                                        <p id="modal-ownerbio" class="small-text">This user has no bio</p>
                                    </div>
                                    <div class="minor">
                                        <p class="medium-text">Website</p>
                                        <p id="modal-ownerwebsite" class="small-text">No website</p>
                                    </div>
                                </div>
                                <div class="divider"></div>
                                <div class="major major-2">
                                    <div class="minor">
                                        <a id="visit_user" href="users/">Visit their profile</a>
                                    </div>
                                    <div class="minor">
                                        <div id="follow_btn" class="btn">LOADING</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <script async src="JS/home.js"></script>
    </body>
</html>

<%-- 
    Document   : index
    Created on : 26-Feb-2018, 13:08:02
    Author     : Rowan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Bitter. We know we had to do it to them.</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link rel="stylesheet" href="CSS/login.min.css" />
        <link rel="stylesheet" href="CSS/universal.min.css" />
        <link rel="stylesheet" href="CSS/stars.min.css" />
        <script src="JS/universal.min.js"></script>
        <script src="JS/delaunay.min.js"></script>

        <link rel="shortcut icon" type="image/png" href="IMG/bitter-logo.png"/>
    </head>
    <body>
        <header>
            <div class="limit-2">
                <div class="link"><img class="logo" src='IMG/bitter-logo.png' alt=''/><a href="#">Home</a></div>
                <div class="link"><a href="/about.html">About</a></div>
            </div>
        </header>
        <div class="front-card">
            <div class="intro-card">
                <h3>Welcome to Bitter.</h3>
                <p>The platform for sharing 'that shitty meme' with your friends. Meet strangers with same interests, and get in touch with the world.</p>
                <p>You're one step away to the most appealing social media, why stop here?</p>
            </div>
            <div class="form-card">
                <div>
                    <form class="form-signin" action="login" id="signin-form" method="POST">
                        <div class="input-group">
                            <span class="input-group-addon">@</span>
                            <input type="text" name="username" class="form-control" id="j_username" placeholder="Username">
                        </div>
                        <input type="password" name="password" id="j_password" class="form-control" placeholder="Password">
                        <button class="btn btn-lg btn-primary btn-block" type="submit">Login</button>
                        <div id="login-fail" class="fail hidden" onclick="hide(this)"><p>The username or password was incorrect</p></div>
                        <div id="login-error" class="fail hidden" onclick="hide(this)"><p>Unable to connect to our servers</p></div>
                    </form>
                </div>
                <h2 class="hr"><span>OR</span></h2>
                <div>
                    <form class="form-register" id="signup-form" action="register" method="POST">
                        <div class="input-group">
                            <span class="input-group-addon">@</span>
                            <input type="text" name="username" class="form-control" id="registerUsername" placeholder="Username">
                        </div>
                        <input type="password" name="password" id="registerPassword" class="form-control ds" placeholder="Password">
                        <input type="password" name="password" id="registerPassword2" class="form-control ds" placeholder="Password">
                        <button class="btn btn-lg btn-primary btn-block full-width" type="submit">Join the community</button>
                    </form>
                </div>
            </div>
        </div>
        <canvas id="stars" width="300" height="300"></canvas>

        <script async src="JS/login.min.js"></script>
        <script async src="JS/stars.js"></script>
    </body>
</html>

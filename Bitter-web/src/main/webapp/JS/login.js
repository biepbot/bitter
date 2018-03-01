/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

window.onload = function (e) {
    $('signin-form').addEventListener('submit', svalidate);
};

function svalidate(e) {
    e.preventDefault();
    var data = "username="+ $('username').value +"&password=" + $('inputPassword').value;
    call('POST', 'api/sessions/logon?' + data, null, function(e, success) {
        if (!success) {
            show($('login-fail')); // 500
        } else if (e == '') {
            show($('login-fail')); // Could not log in
        }
        else {
            // push user to different screen
            console.log(e);
            getOATH();
        }
    });
}

function getOATH() {
    call('POST', 'api/sessions/whoami', null, function(e, success) {
        console.log(e);
    });
}

function logout() {
    call('POST', 'api/sessions/logout', null, function(e, success) {
    });
}
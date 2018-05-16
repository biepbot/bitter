/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

window.onload = function (e) {
    $('signin-form').addEventListener('submit', login);
};

function login(e) {
    e.preventDefault();
    var u = $('username').value;
    var p = $('password').value;
    
    call('POST', 'api/auth/logon', 'username=' + u + '&password=' + p, function (e, success) {
        if (!success) {
            show($('login-fail')); // Could not log in
            return;
        }
        if (e.indexOf('401') === -1) {
            // logon
            location.href = 'home.jsp';
        } else {
            show($('login-fail')); // Could not log in
        }
    }, 1);
}
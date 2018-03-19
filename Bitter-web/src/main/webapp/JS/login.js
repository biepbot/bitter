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
    var data = new FormData(document.getElementById('form-signin'));
    call('POST', 'j_security_check', data, function (e, success) {
        if (e.indexOf('401') === -1) {
            // logon
            //window.location = 'home.jsp';
        } else {
            show($('login-fail')); // Could not log in
        }
    });
}
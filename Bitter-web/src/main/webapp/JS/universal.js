/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function hide(ele, n) {
    if (!n) {
        addClass(ele, 'hidden');
    } else
        addClass(ele, 'hidden-space');
}
function show(ele, n) {
    if (!n) {
        removeClass(ele, 'hidden');
    } else
        removeClass(ele, 'hidden-space');
}

function hasClass(ele, cls) {
    return (" " + ele.className + " ").indexOf(" " + cls + " ") > -1;
}

function addClass(ele, cls) {
    if (!hasClass(ele, cls)) {
        var space = ' ';
        if (endsWith(ele.className, space)) {
            space = '';
        }
        ele.className += space + cls;
        return true;
    }
    return false;
}

function removeClass(ele, cls) {
    if (hasClass(ele, cls)) {
        var reg = new RegExp("(\\s|^)" + cls + "(\\s|$)");
        ele.className = ele.className.replace(reg, " ")
    }
}

function toggleClass(ele, cls) {
    if (!addClass(ele, cls)) {
        removeClass(ele, cls)
    }
}

function endsWith(str, suffix) {
    return str.indexOf(suffix, str.length - suffix.length) !== -1;
}

function close() {
    this.parentNode.removeChild(this);
}

function $(id) {
    return document.getElementById(id);
}

function call(type, url, data, callback) {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.withCredentials = true;
    xmlHttp.onreadystatechange = function () {
        if (xmlHttp.readyState === 4 && xmlHttp.status === 200)
        {
            callback(xmlHttp.responseText, true);
        } else if (xmlHttp.readyState === 4 && xmlHttp.status !== 200) {
            callback(xmlHttp.status + ' ERROR: ' + xmlHttp.responseText, false);
        }
    }
    xmlHttp.open(type, url, true);
    xmlHttp.send(data);
}


// COOKIE MANAGEMENT //

function getCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}

function setCookie(cname, cvalue, time) {
    time = time || (exdays * 24 * 60 * 60 * 1000);
    var exdays = 365;
    var d = new Date();
    d.setTime(d.getTime() + time);
    var expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}
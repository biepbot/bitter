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


// GENERAL FUNCTIONS //

function logout() {
    call('POST', 'api/sessions/logout', null, function(e, success) {
    });
}
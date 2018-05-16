/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var root = '/Bitter-web/';
// Api call method
function apicall(method, url, callback, data)
{
    var tries = 0;
    function t() {
        call(method, root + url, data, function (e, success) {
            if (success) {
                e = JSON.parse(e);
                callback(e);
            } else {
                if (++tries < 5) {
                    t();
                } else {
                    console.log(e);
                }
            }
        });
    }
    t();
}

var throttled = [];

function throttle(func, time) {
    var inarr = false;

    // check if in array
    for (var i = 0; i < throttled.length; i++) {
        var f = throttled[i];
        if (f === func) {
            inarr = true;
            break;
        }
    }

    // if not
    if (inarr) {
        // exec
        func();
        setTimeout(function () {
            // remove from array after timeout
            // check if next one of this function should be ran
            timeThrottle(func);
        }, time);
    }

    // add to array
    throttled.push({
        'func': func,
        'time': time
    });

}

function timeThrottle(func) {
    var delet = false;
    for (var i = 0; i < throttled.length; i++) {
        var f = throttled[i];
        if (f === func) {

            if (!delet) {
                // delet, this moves the pointer
                throttle.splice(i, 1);
                delet = true;
            } else {
                // run next function
                throttle(func, f.time);
            }
        }
    }
}
function urlify(str) {
    var urlRegex = /(https?:\/\/[^\s]+)/g;
    var change = str.replace(urlRegex, function (url) {
        if (checkImgUrl(url)) {
            return '<a href="' + url + '" target="_blank"><img src="' + url + '"/></a>';
        }
        return '<a href="' + url + '">' + url + '</a>';
    });
    return change;
}
function checkImgUrl(url) {
    return(url.match(/\.(jpeg|jpg|gif|png)$/) != null);
}

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

function call(type, url, data, callback, form) {
    var xmlHttp = new XMLHttpRequest();
    //xmlHttp.withCredentials = true;
    xmlHttp.onreadystatechange = function () {
        if (xmlHttp.readyState === 4 && (xmlHttp.status >= 200 && xmlHttp.status < 300))
        {
            callback(xmlHttp.responseText, true, xmlHttp);
        } else if (xmlHttp.readyState === 4 && xmlHttp.status !== 200) {
            callback(xmlHttp, false);
        }
    }
    xmlHttp.open(type, url, true);
    if (form == 1)
        xmlHttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xmlHttp.send(data);
}

function replaceAll(str, find, replace) {
    return str.replace(new RegExp(escapeRegExp(find), 'g'), replace);
}
function elementFromString(htmlString) {
    var div = document.createElement('div');
    div.innerHTML = htmlString.trim();
    return div.firstChild;
}
function escapeRegExp(str) {
    return str.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
}

// GENERAL FUNCTIONS //
var els = [];
function saveElement(ele) {
    els.push({
        'id' : ele.id,
        'savedState' : ele.cloneNode(true)
    });
}
function restoreElement(ele) {
    for (var i = 0; i < els.length; i++) {
        var e = els[i];
        if (e.id === ele.id) {
            ele.parentNode.replaceChild(e.savedState, ele);
        }
    }
}

function logout() {
    call('POST', 'api/sessions/logout', null, function (e, success) {
    });
}

var entityMap = {
    '<': '&lt;',
    '>': '&gt;',
};

function escapeHtml(string) {
    return String(string).replace(/[<>]/g, function (s) {
        return entityMap[s];
    });
}

/*
 * Flashing stuff
 */
function flash(ele) {
    if (hasClass(ele, 'flash')) {
        addClass(ele, 'flash-color');
        setTimeout(function () {
            removeClass(ele, 'flash-color');
        }, parseFloat(getComputedStyle(document.getElementById('barks_count'))['transitionDuration']) * 2000);
    }
}
/* 
 * HOME.JS
 * 
 * Set:
 *  username        - username
 *  at_username     - username
 *  avatar          - avatar
 *  header          - color
 * 
 *  barks_count
 *  following_count
 *  follower_count
 */
// Setup
document.getElementById("bark")
        .addEventListener("keyup", function (event) {
            event.preventDefault();
            if (event.keyCode === 13) {
                if (this.value)
                    bark(this.value);
            }
        });



// testing
var default_user = 'biepbot';

var data = {};
data.user = {};
data.user.name = default_user;
data.register = [];
data.barktemplate =
        '<div class="bark flash">'
        + '            <div class="bark-image">'
        + '                <img src="$avatar"/>'
        + '            </div>'
        + '            <div class="bark-user">'
        + '                @$name'
        + '            </div>'
        + '            <div class="bark-content">'
        + "                $content"
        + '            </div>'
        + '            <div class="bark-interact">'
        + '                <li>'
        + '                    <ul class="ul-bite"><span class="fa fa-bolt"></span>bite</ul>'
        + '                    <ul class="ul-bark"><span class="fa fa-share-alt"></span>bark</ul>'
        + '                    <ul><span class="fa fa-reply"></span>reply</ul>'
        + '                </li>'
        + '            </div>'
        + '        </div>';

function bark(msg) {
    var d = "bark=" + msg;
    call('POST', 'api/users/' + data.user.name + '/bark', d, function (e, success) {
        if (success) {
            var bark = addBark(JSON.parse(e));
            var b = document.getElementById('barks_count');
            var amount = b.innerHTML;
            amount++;
            b.innerHTML = amount;
            flash(b);
            flash(bark);
        } else {
            // show error?
            console.log(e);
        }
    }, 1);
    var b2 = document.getElementById('bark');
    b2.value = '';
}

function loadUser() {
    apicall('GET', 'api/users/' + data.user.name,
            function (e) {
                for (var i = 0; i < data.register.length; i++)
                {
                    var r = data.register[i];
                    var ele = r.element;

                    var val = e[r.value];
                    data[r.value] = val;

                    if (val !== '') {
                        ele.innerHTML = val;
                    } else {
                        hide(ele);
                    }
                }

                // extra:
                var color = e.color || '#ffec58';
                $('header').style.background = color;

                var avatar = e.avatar || '';
                $('avatar').src = avatar;
                data.user.avatar = avatar;
            });
}

// Loads in the Timeline
function loadTimeline() {
    apicall('GET', 'api/users/' + data.user.name + '/timeline',
            function (e) {
                for (var i = 0; i < e.length; i++)
                {
                    var ei = e[i];
                    var ele = addBark(ei);
                    loadBarkDetails(ei.id, ele);
                }
            });
}

function addBark(ei) {
    var t = data.barktemplate;
    t = replaceAll(t, '$avatar', ei.poster['avatar']);
    t = replaceAll(t, '$name', ei.poster['name']);
    t = replaceAll(t, '$content', escapeHtml(ei['content']));

    var ele = elementFromString(t);
    ele.id = ei.id;
    // add onclick
    ele.onclick = function (e) {
        if (hasClass(e.target, 'ul-bite')) {
            // bite
            like(e.currentTarget);
            e.preventDefault();
        } else if (hasClass(e.target, 'ul-bark')) {
            // bark
            rebark(e.currentTarget);
            e.preventDefault();
        }
    }

    $('new-bark').insertAdjacentElement('afterend', ele);
    return ele;
}

function like(ele) {
    apicall('POST', 'api/users/' + data.user.name + '/like/' + ele.id, function (e) {
        var res = e;
        if (res === 1) {
            // like
            addClass(ele, 'bitten');
        } else {
            // unlike
            removeClass(ele, 'bitten');
        }
    });
}

function rebark(ele) {
    apicall('POST', 'api/users/' + data.user.name + '/rebark/' + ele.id, function (e) {
        var res = e;
        if (res === 1) {
            // bark
            addClass(ele, 'barked');
        } else {
            // unrebark
            removeClass(ele, 'barked');
        }
    });
}

// Loads in details of a single bark
// id: id of bark
// ele: element of bark
function loadBarkDetails(id, ele)
{
    apicall('GET', 'api/barks/' + id + '/likes/' + data.user.name, function (e) {
        removeClass(ele, '$bitten');
        if (e == 1)
        {
            addClass(ele, 'bitten');
        }
    });

    apicall('GET', 'api/barks/' + id + '/rebarks/' + data.user.name, function (e) {
        removeClass(ele, '$barked');
        if (e == 1)
        {
            addClass(ele, 'barked');
        }
    });
}

// Api call method
function apicall(method, url, callback, data)
{
    var tries = 0;
    function t() {
        call(method, url, data, function (e, success) {
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

// Registers an element to match with user data
// automaticall hides any element that has no data
function registerElement(element, property)
{
    var d = {};
    d.element = element;
    d.value = property;
    data.register.push(d);
}

registerElement($('username'), 'name');
registerElement($('at_username'), 'name');
registerElement($('barks_count'), 'bark_count');
registerElement($('follower_count'), 'follower_count');
registerElement($('following_count'), 'following_count');

loadUser();
loadTimeline();
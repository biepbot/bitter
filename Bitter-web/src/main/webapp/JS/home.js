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
document.getElementById('darkener').onclick = hideIfDarkener;


// testing
var default_user = 'biepbot';

var data = {};
data.user = {};
data.user.name = default_user;
data.register = [];
data.barktemplate =
        '<div class="bark flash">'
        + '            <div class="bark-image">'
        + '                <img src="$avatar" class="user-image"/>'
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
    tl.bark(msg);
}

function loadUser(who, type) {
    if (who == null)
        who = data.user.name;
    if (type == null)
        type = '';

    apicall('GET', 'api/users/' + who,
            function (e) {
                for (var i = 0; i < data.register.length; i++)
                {
                    var r = data.register[i];
                    var ele = $(type + r.element);

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
                $(type + 'header').style.background = color;

                var avatar = e.avatar || '';
                $(type + 'avatar').src = avatar;
                data.user.avatar = avatar;
            });
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

registerElement('username', 'name');
registerElement('at_username', 'name');
registerElement('barks_count', 'bark_count');
registerElement('follower_count', 'follower_count');
registerElement('following_count', 'following_count');

loadUser();
var tl = new Timeline();

function hideIfDarkener(e) {
    if (e.target.id === 'darkener') {
        hide(e.target);
    }
}

// Objects
//
//

// Timeline
// handles timeline events
// manages barks
function Timeline() {
    // fields
    this.barks = [];

    Timeline.prototype.load = function () {
        var me = this;
        apicall('GET', 'api/users/' + data.user.name + '/timeline',
                function (e) {
                    for (var i = 0; i < e.length; i++)
                    {
                        // a single bark
                        var ei = e[i];

                        // turn into object
                        var bark = new Bark(ei);
                        me.addBark(bark);
                    }
                });
    };

    Timeline.prototype.bark = function (msg) {
        var d = "bark=" + msg;
        var me = this;
        call('POST', 'api/users/' + data.user.name + '/bark', d, function (e, success) {
            if (success) {
                var bark = new Bark(JSON.parse(e));
                me.addBark(bark);
                var b = $('barks_count');
                var amount = b.innerHTML;
                amount++;
                b.innerHTML = amount;
                flash(b);
                bark.flash();
            } else {
                // show error?
                console.log(e);
            }
        }, 1);
        var b2 = $('bark');
        b2.value = '';
    };

    // adds and renders a bark
    Timeline.prototype.addBark = function (bark) {
        this.barks.push(bark);
        bark.render();
    };

    // mandatory TL load
    this.load();

    // Bark
    // handles bark events
    // handles bark information
    function Bark(details) {
        this.id = details.id;
        this.details = details;
        this.element = null;

        Bark.prototype.follow = function (un) {
            var followbtn = $('follow_btn');
            followbtn.onclick = function(){};
            followbtn.innerHTML = un === '' ? 'Following...' : 'Unfollowing...';
            
            var me = this;
            call('POST', 'api/users/' + data.user.name + '/' + un + 'follow/' + this.details.poster.name, null, function (e, success) {
                if (success) {
                    me.loadOwner();
                    if (e == 1) {
                        // show unfollow
                        followbtn.innerHTML = 'Unfollow';
                        followbtn.onclick = function () {
                            follow('un');
                        };
                    } else {
                        // show follow
                        followbtn.innerHTML = 'Follow';
                        followbtn.onclick = function () {
                            follow('');
                        };
                    }
                    flash($('modal-ownerfollower_count'));
                } else {
                    // todo: show error?
                }
            });
        }

        // adds an element
        Bark.prototype.registerElement = function (element) {
            this.element = element;
        };

        // Syncs the server with the local bark
        Bark.prototype.updateStatus = function () {
            var id = this.id;
            var ele = this.element;
            apicall('GET', 'api/barks/' + id + '/likes/' + data.user.name, function (e) {
                removeClass(ele, '$bitten');
                if (e == 1)
                {
                    addClass(ele, 'bitten');
                } else {
                    removeClass(ele, 'bitten');
                }
            });

            apicall('GET', 'api/barks/' + id + '/rebarks/' + data.user.name, function (e) {
                removeClass(ele, '$barked');
                if (e == 1)
                {
                    addClass(ele, 'barked');
                } else {
                    removeClass(ele, 'barked');
                }
            });
        };

        // Loads the owner modal information
        Bark.prototype.loadOwner = function () {
            var n = this.details.poster.name;
            var you = n === data.user.name;

            // load in details
            loadUser(n, 'modal-owner');

            // Load in urls

            // Check if the logged in user is following them, and adjust the follow button accordingly
            var profile = $('visit_user');
            var followbtn = $('follow_btn');
            profile.href = 'users/' + n + '.jsp';
            if (you) {
                hide(followbtn);
                profile.innerHTML = 'Visit your profile';
            } else {
                show(followbtn);
                profile.innerHTML = 'Visit ' + n + '\'s profile';

                var me = this;

                apicall('GET', 'api/users/' + data.user.name + '/following', function (e) {
                    var follows = false;
                    for (var i = 0; i < e.length; i++) {
                        var u = e[i];
                        if (u.name === n) {
                            follows = true;
                            break;
                        }
                    }
                    if (follows) {
                        // show unfollow
                        followbtn.innerHTML = 'Unfollow';
                        followbtn.onclick = function () {
                            me.follow('un');
                        };
                    } else {
                        // show follow
                        followbtn.innerHTML = 'Follow';
                        followbtn.onclick = function () {
                            me.follow('');
                        };
                    }
                });
            }
        }

        // Opens the owner modal
        Bark.prototype.openOwner = function () {
            // todo: reload old state of object

            this.loadOwner();

            // show
            show($('darkener'));
            show($('modal-owner'));
            //hide($('big-tweet'));
        };

        Bark.prototype.openTweet = function () {
            // todo: open a modal
        };

        // Returns whether the bark is liked or not
        Bark.prototype.isLiked = function () {
            return hasClass(this.element, 'ul-bite');
        };

        // Returns whether the bark is rebarked or not
        Bark.prototype.isRebarked = function () {
            return hasClass(this.element, 'ul-bark');
        };

        // Likes the bark
        // toggle function
        Bark.prototype.like = function () {
            var id = this.id;
            var element = this.element;
            apicall('POST', 'api/users/' + data.user.name + '/like/' + id, function (e) {
                if (e === 1) {
                    // like
                    addClass(element, 'bitten');
                } else {
                    // unlike
                    removeClass(element, 'bitten');
                }
            });
        };

        // Rebarks the bark
        // toggle function
        Bark.prototype.rebark = function () {
            var id = this.id;
            var element = this.element;
            apicall('POST', 'api/users/' + data.user.name + '/rebark/' + id, function (e) {
                if (e === 1) {
                    // bark
                    addClass(element, 'barked');
                } else {
                    // unrebark
                    removeClass(element, 'barked');
                }
            });
        };

        // Renders the bark on the screen
        Bark.prototype.render = function () {

            var t = data.barktemplate;
            t = replaceAll(t, '$avatar', details.poster['avatar']);
            t = replaceAll(t, '$name', details.poster['name']);
            t = replaceAll(t, '$content', escapeHtml(details['content']));

            var ele = elementFromString(t);
            ele.id = this.id;
            // add onclick

            var who = this;
            ele.onclick = function (e) {
                if (hasClass(e.target, 'ul-bite')) { // on like button click
                    who.like();
                } else if (hasClass(e.target, 'ul-bark')) { // on rebark button click
                    who.rebark();
                } else if (hasClass(e.target, 'user-image')) { // open profile
                    who.openOwner();
                }
                e.preventDefault();
            };
            this.registerElement(ele);
            $('new-bark').insertAdjacentElement('afterend', ele);

            this.updateStatus();
        };

        Bark.prototype.flash = function () {
            flash(this.element);
        };
    }
    ;
}
;
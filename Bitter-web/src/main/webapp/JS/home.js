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

// Setup
var tl = new Timeline();
var testbark = document.getElementById('test-bark');
testbark = tl.getPreviewBark(testbark);

var barkEle = document.getElementById('bark');
barkEle.addEventListener('keyup', function (event) {
    event.preventDefault();
    
    // Only do stuff on change
    if (this.value === testbark.content) return;
    
    // check if user stopped typing after 200ms
    if (!tt) {
        tt = true;
        setTimeout(afterType, delay);
    }

    // user is typing right now
    typing = true;

});
document.getElementById('darkener').onclick = hideIfDarkener;

// Type timeout functions
var delay = 500;
var tt = false;
var typing = false;
function afterType() {
    if (!typing) {
        testbark.setContent(barkEle.value);
        tt = false;
    } else {
        typing = false;
        setTimeout(afterType, delay);
    }
}

// Public functions
function bark() {
    if (barkEle.value) {
        tl.bark(barkEle.value);
        testbark.clear();
    }
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
                        if (ele)
                            ele.innerHTML = val;
                    } else {
                        hide(ele);
                    }
                }

                // extra:
                var color = e.color || '#ffec58';
                var header = $(type + 'header');
                if (header)
                    header.style.background = color;

                var a = e.avatar || '';
                var avatar = $(type + 'avatar');
                if (avatar)
                    avatar.src = a;
                data.user.avatar = a;
            });
}

function hideIfDarkener(e) {
    if (e.target.id === 'darkener') {
        hide(e.target);
    }
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

// Setup through functions

registerElement('username', 'name');
registerElement('at_username', 'name');
registerElement('barks_count', 'bark_count');
registerElement('follower_count', 'follower_count');
registerElement('following_count', 'following_count');

loadUser();
loadUser(null, 'test-');

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

    Timeline.prototype.getPreviewBark = function (element) {
        var details = {'id': -1};

        var b = new Bark(details);
        b.registerElement(element);

        return b;
    }

    // mandatory TL load
    this.load();

    // Bark
    // handles bark events
    // handles bark information
    function Bark(details) {
        this.id = details.id;
        this.details = details;
        this.element = null;

        this.isLoadingPreview = false;

        Bark.prototype.follow = function (un) {
            var followbtn = $('follow_btn');
            followbtn.onclick = function () {};
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
        };

        // Clears the contents of a bark
        Bark.prototype.clear = function () {
            this.setContent('');
        };

        // Sets the content
        Bark.prototype.setContent = function (content) {
            var c = this.element.getElementsByClassName('bark-content')[0];
            this.content = content;
            content = urlify(escapeHtml(content));
            c.innerHTML = content;

            if (!this.isLoadingPreview) {
                this.isLoadingPreview = true;

                // get urls in content
                var urls = getUrls(content);
                if (urls.length) {
                    var who = this;
                    for (var i = 0; i < urls.length; i++) {
                        var u = urls[i];
                        getPreview(u, function (e, succ) {
                            if (succ) {
                                who.loadPreviewFromJSON(e, u);
                            }
                            who.isLoadingPreview = false;
                        });
                    }
                } else {
                    this.isLoadingPreview = false;
                }
            }
        };
        
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

        // Add rich text preview
        Bark.prototype.addPreview = function (original, title, desc, image, url) {
            var c = this.element.getElementsByClassName('bark-content')[0];
            var original2 = original.replace(/\/+$/, "");

            // delete the link
            original = '<a href="' + original + '">' + original + '</a>';
            original2 = '<a href="' + original2 + '">' + original2 + '</a>';
            c.innerHTML = c.innerHTML.replace(original, '');
            c.innerHTML = c.innerHTML.replace(original2, '');

            // add the preview
            var div = document.createElement('a');
            div.href = url;
            addClass(div, 'rich-preview');

            var t = document.createElement('p');
            t.innerHTML = title;
            div.appendChild(t);

            var d = document.createElement('p');
            d.innerHTML = desc;
            div.appendChild(d);

            var div2 = document.createElement('div');
            var i = document.createElement('img');
            i.src = image;
            addClass(div2, 'preview-image');
            div2.appendChild(i);
            div.appendChild(div2);

            c.insertAdjacentElement('beforeend', div);
        };

        // Wrapper function for loading preview from JSON data
        Bark.prototype.loadPreviewFromJSON = function (json, url) {
            json = JSON.parse(json);
            this.addPreview(url, json.title, json.description, json.image, json.url);
        };

        function getUrls(content) {
            var d = document.createElement('div');
            d.innerHTML = content;

            var as = d.getElementsByTagName('A');
            var urls = [];
            for (var i = 0; i < as.length; i++) {
                if (as[i].children.length === 0)
                    urls.push(as[i].href);
            }
            return urls;
        };

        // Renders the bark on the screen
        Bark.prototype.render = function () {

            var who = this;
            var t = data.barktemplate;
            t = replaceAll(t, '$avatar', details.poster['avatar']);
            t = replaceAll(t, '$name', details.poster['name']);

            var ele = elementFromString(t);
            ele.id = this.id;
            // add onclick

            ele.onclick = function (e) {
                if (hasClass(e.target, 'ul-bite')) { // on like button click
                    who.like();
                } else if (hasClass(e.target, 'ul-bark')) { // on rebark button click
                    who.rebark();
                } else if (hasClass(e.target, 'user-image')) { // open profile
                    who.openOwner();
                }
            };
            this.registerElement(ele);
            $('new-bark').insertAdjacentElement('afterend', ele);

            this.updateStatus();
            this.setContent(details['content']);
        };

        Bark.prototype.flash = function () {
            flash(this.element);
        };
    }
    ;
}
;
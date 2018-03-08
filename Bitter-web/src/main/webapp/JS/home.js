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
         '<div class="bark">'
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
       + '                    <ul><span class="fa fa-bolt $bitten"></span>bite</ul>'
       + '                    <ul><span class="fa fa-share-alt $barked"></span>bark</ul>'
       + '                    <ul><span class="fa fa-reply"></span>reply</ul>'
       + '                </li>'
       + '            </div>'
       + '        </div>';

function loadUser() {
    call('GET', 'api/users/' + data.user.name, null, function (e, success) {
        if (success) {
            e = JSON.parse(e);
            
            for (var i=0; i<data.register.length;i++)
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
            
        } else {
            console.log(e);
        }
    });
}

// Loads in the Timeline
function loadTimeline() {
    call('GET', 'api/users/' + data.user.name + '/timeline', null, function (e, success) {
        if (success) {
            e = JSON.parse(e);
            
            for (var i=0;i<e.length;i++)
            {
                var ei = e[i];
                
                var t = data.barktemplate;
                t = replaceAll(t, '$avatar', ei.poster['avatar']);
                t = replaceAll(t, '$name', ei.poster['name']);
                t = replaceAll(t, '$content', ei['content']);
                t = replaceAll(t, '$barked', '');
                t = replaceAll(t, '$bitten', '');

                var ele = elementFromString(t);
                $('new-bark').insertAdjacentElement('afterend', ele);
            }
            
        } else {
            console.log(e);
        }
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

registerElement($('username'), 'name');
registerElement($('at_username'), 'name');
registerElement($('barks_count'), 'bark_count');
registerElement($('follower_count'), 'follower_count');
registerElement($('following_count'), 'following_count');

loadUser();
loadTimeline();
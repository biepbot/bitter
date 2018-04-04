/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


(function (window, document, undefined) {
    /*
     * getRichPreview : void
     *  url : url
     *  callback : function
     *      returns object e
     *          success
     *          error
     *              request
     *              status code
     *      
     *          title
     *          description
     *          image
     *          url
     *          original_url
     */
    function getPreview(url, callback) {
        // create object
        var ret = {
            'title': '',
            'description': '',
            'image': '',
            'url': '',
            'original_url': url,
            'success': false,
            'error': {
                'request': null,
                'status': 0
            }
        };
        
        // create request
        var x = new XMLHttpRequest();
        x.onreadystatechange = function () {
            if (x.readyState === 4) {
                if (x.status >= 200 && x.status < 300 || x.status === 302)
                {
                    // parse the response for all data necessary
                    var preview = new RichPreview(x.responseText, url);

                    // fill in data
                    ret.title = preview.title;
                    ret.description = preview.description;
                    ret.image = preview.image;
                    ret.url = x.responseURL || url;
                    ret.success = true;
                }
                ret.error.status = x.status;
                callback(ret);
            }
        };
        x.open('POST', 'api/website/string', true);
        x.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

        // set the request
        ret.error.request = x;

        var data = "url=" + url;
        x.send(data);
    }

    RichPreview = function (dom, url) {
        if (!(dom instanceof HTMLElement)) {
            // todo: destroy src to prevent image loading
            
            // todo: destroy script to prevent scripts running
            
            var div = document.createElement('div');
            div.innerHTML = dom;
            dom = div;
        }
        
        // declare variables
        this.title = getTitle();
        this.description = getDescription();
        this.image = getImage();

        // get to make function calls easier
        function get(selector, attribute) {
            var ele = dom.querySelector(selector) || document.createElement('div');
            if (attribute) {
                return ele.getAttribute(attribute);
            } else {
                return ele;
            }
        }

        function getTitle() {
            return get('meta[property="og:title"]', 'content') ||
                    get('meta[name="og:title"]', 'content') ||
                    get('meta[name=title]', 'content') ||
                    get('title').innerHTML ||
                    '';
        }

        function getDescription() {
            return get('meta[property="og:description"]', 'content') ||
                    get('meta[name="og:description"]', 'content') ||
                    get('meta[name=description]', 'content') ||
                    get('div .description').innerHTML ||
                    '';
        }

        function getImage() {
            var imgUrl = get('meta[property="og:image"]', 'content') ||
                    get('meta[name="og:image"]', 'content') ||
                    get('meta[name=image]', 'content') ||
                    get('img[src]', 'src');

            // Ensure absolute url
            if (imgUrl && !validateUrl(imgUrl)) {
                var a = document.createElement("a");
                a.href = url;
                var add = '';
                if (a.hostname.indexOf('www') === -1) {
                    add = 'www.';
                }
                imgUrl = a.protocol + "//" + add + a.hostname + imgUrl;
            }
            return imgUrl;
        }

        function validateUrl(value) {
            return (/^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i).test(value);
        }
        delete dom;
    };

    window.getPreview = getPreview;
})(window, document);

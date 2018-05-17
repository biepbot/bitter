/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var homeApp = angular.module('homeApp', ['ngWebSocket']);
var me = sessionStorage.getItem('USER');

if (!me) {
    location.href = 'index.jsp';
}

homeApp.config(['$httpProvider', function ($httpProvider) {
        $httpProvider.defaults.withCredentials = true;
        $httpProvider.defaults.headers.common['Authorization'] = sessionStorage.getItem('AUTH');
    }]);

homeApp.filter('reverse', function () {
    return function (items) {
        return items.slice().reverse();
    };
});

homeApp.controller('mainController', ['$http', '$websocket', '$scope', function ($http, $websocket, $scope) {
        var bc = this;

        // variables
        bc.timeline = [];
        bc.user = {};

        var dataStream = $websocket('ws://localhost:8080/Bitter-web/actions/bark');

        dataStream.onMessage(function (message) {
            bc.timeline.push(JSON.parse(message.data));
        });

        // load User
        $http({
            method: 'GET',
            url: 'api/users/' + me
        }).then(function successCallback(response) {
            // set user
            bc.user = response.data;

            // load TL
            $http({
                method: 'GET',
                url: 'api/users/' + bc.user.name + '/timeline'
            }).then(function successCallback(response) {
                // set barks

                response.data.forEach(function (e) {
                    // add functions
                    e.bite = like;
                    e.bark = rebark;
                    e.open = openBark;
                    e.openOwner = openOwner;

                    // check if liked or rebarked
                    $http({
                        method: 'GET',
                        url: 'api/users/' + bc.user.name + '/timeline'
                    }).then(function successCallback(response) {
                    });


                });
                bc.timeline = response.data;

            }, function errorCallback(response) {
                // todo;
                // show error
            });
        }, function errorCallback(response) {
            // todo;
            // show error
            location.href = 'index.jsp';
        });

        // end point functions
        bc.bark = function (content) {
            document.getElementById('bark').value = '';
            $http({
                method: 'POST',
                url: 'api/users/' + bc.user.name + '/bark',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                data: {bark: content}
            }).then(function successCallback(response) {
                dataStream.send(response.data);
            }, function errorCallback(response) {
                // todo;
                // show error
            });
        };
        bc.hide = function () {
            var d = angular.element(document.getElementById('darkener'));
            var d1 = angular.element(document.getElementById('big-bark'));
            var d2 = angular.element(document.getElementById('modal-owner'));
            d.addClass('hidden');
            d1.addClass('hidden');
            d2.addClass('hidden');
        };

        // BARK FUNCTIONS
        var openOwner = function () {
            $scope.barkowner = this;

            var d = angular.element(document.getElementById('darkener'));
            var d1 = angular.element(document.getElementById('modal-owner'));
            d.removeClass('hidden');
            d1.removeClass('hidden');
        };
        var openBark = function () {
            $scope.bigbark = this;

            var d = angular.element(document.getElementById('darkener'));
            var d1 = angular.element(document.getElementById('big-bark'));
            d.removeClass('hidden');
            d1.removeClass('hidden');
        };
        var like = function () {
            var b = this;
            // get id somehow.
            $http({
                method: 'POST',
                url: 'api/users/' + bc.user.name + '/like/' + this.id
            }).then(function (response) {
                b.bitten = response.data ? 'bitten' : '';
            });
        };
        var rebark = function () {
            var b = this;
            $http({
                method: 'POST',
                url: 'api/users/' + bc.user.name + '/rebark/' + this.id
            }).then(function (response) {
                b.barked = response.data ? 'barked' : '';
            });
        };
    }]);


homeApp.controller('barkController', ['$scope', function ($scope) {
        $scope.value = function (value) {
            value = value || '';

            // todo;
            // parse to urls and stuff

            // no excessive newlines
            while (value.indexOf('\n\n\n') > -1) {
                value = value.replace(/\n\n\n/g, '\n\n'); // force max newlines of two
            }

            // support newlines
            //value = value.replace(/\n/g, '<br/>');

            // support any brackets and such
            //value = escapeHtml(value);

            // turn any accepted html values back
            //value = getTrustedHtml(value);

            return value;
        };
    }]);

function getTrustedHtml(string) {
    var tstring = string;
    var trusted = ['b', 'bold', 'br'];
    var entityMap = {
        '&lt;': '<',
        '&gt;': '>'
    };

    for (var i = 0; i < trusted.length; i++) {
        var r = trusted[i];
        var regex = new RegExp('&lt;' + r + '[ ]*[\/]{0,1}&gt;', "g");

        tstring = tstring.replace(regex, function (s) {
            return String(s).replace(/&lt;|&gt;/g, function (s2) {
                return entityMap[s2];
            });
        });
    }

    return tstring;
}

function escapeHtml(string) {
    var entityMap = {
        '<': '&lt;',
        '>': '&gt;'
    };
    return String(string).replace(/[<>]/g, function (s) {
        return entityMap[s];
    }
    );
}

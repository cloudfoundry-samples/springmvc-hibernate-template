/***
 * Controller to handle interfacing with the RESTful endpoint
 */
$.ajaxSetup({
    cache:false
});


function notExists(o, p, cb) {
    if (!o[p] && o[p] == null) {
        cb(p);
    }
}
function exists(o, p, cb) {
    if (o[p] && o[p] != null) {
        cb(p, o[p]);
    }
}

// todo
var Ajax = (function (exp) {

    var url = null;

    function communicationError(url, method, data, ex) {
        var errMsg = 'error when ' + (method + '').toUpperCase() +
            '\'ing to url ' + url + (data == null ? '. Payload: ' +
            JSON.stringify(data) : '') +
            '. Exception: ' + JSON.stringify(ex);
        console.log(errMsg);
    }

    function enrichHeadersForSubmission(headers) {
        notExists(headers, 'contentType', function (pn) {
            headers[pn] = 'application/json; charset=utf-8';
        });
        notExists(headers, 'cache', function (pn) {
            headers[pn] = false;
        });
        notExists(headers, 'dataType', function (pn) {
            headers[pn] = 'json';
        });
        return headers;
    }


    exp.setup = function (u) {
        url = u;
    };
    exp.url = function (u) {
        return  url + u;
    };
    exp.get = function (url, callback) {
        $.ajax(enrichHeadersForSubmission({
            type:'GET',
            url:url,
            success:callback,
            error:function (e) {
                communicationError(url, 'GET', data, e);
            }
        }));
    };
    exp.put = function (url, d, callback) {
        var k = '_method',
            v = 'PUT';

        var data = {};//{customer:d};
        data[k] = v;

        console.log( JSON.stringify( data))
        var headers = {};
        headers[k] = v;

        // todo restor the call to enrichHeaders
        $.ajax( ({
            type:'POST',
            url:url,
            headers:headers,
            data:data,
            success:callback,
            error:function (e) {
                communicationError(url, 'POST', data, e);
            }
        }));
    };
    exp.post = function (url, data, callback) {
        $.ajax(enrichHeadersForSubmission({
            type:'POST',
            url:url,
            data:data,
            success:callback,
            error:function (e) {
                communicationError(u, 'PUT', data, e);
            }
        }));
    };

    return exp;
})({});

var utils = Ajax;
/*if(false)
 {
 _url:'',
 _communicationError:function (url, method, data, ex) {
 var err = 'error when ' + (method + '').toUpperCase() +
 '\'ing to url ' + url + (data == null ? '. Payload: ' +
 JSON.stringify(data) : '') +
 '. Exception: ' + JSON.stringify(ex);
 console.log(err);
 },
 setup:function (u) {
 this._url = u;
 },
 url:function (u) {
 return this._url + u;
 },
 get:function (url, data, cb) {
 $.ajax({
 type:'GET',
 url:url,
 cache:false,
 dataType:'json',
 contentType:'application/json; charset=utf-8',
 success:cb,
 error:function (e) {
 utils._communicationError(url, 'GET', data, e);
 }
 });
 },
 put:function (url, data, cb) {
 var k = '_method',
 v = 'PUT';
 data[k] = v;
 var headers = {};
 headers[k] = v;
 $.ajax({
 type:'POST',
 url:url,
 cache:false,
 contentType:'application/json; charset=utf-8',
 headers:headers,
 data:data,
 success:function (result) {
 cb(result);
 },
 error:function (e) {
 utils._communicationError(url, 'POST', data, e);
 }
 });     // todo

 },
 post:function (u, data, cb) {
 $.ajax({
 type:'POST',
 url:url,
 cache:false,
 dataType:'json',
 data:data,
 contentType:'application/json; charset=utf-8',
 success:cb,
 error:function (e) {
 utils._communicationError(u, 'PUT', data, e);
 }
 });
 }
 };*/

function CustomerCtrl($scope) {
    $scope.customers = [];

    $scope.query = 'juergen';

    $scope.searchResultsFound = function () {
        return $scope.customers != null && $scope.customers.length > 0;
    };

    $scope.load = function (customer) {
        $scope.customer = customer;
        $scope.id = customer.id;
    };

    $scope.search = function () {
        var u = utils.url('/crm/search?q=' + $scope.query);
        utils.get(u, function (customers) {
            $scope.$apply(function () {
                $scope.customers = customers;
                if ($scope.searchResultsFound()) {
                    if (customers.length == 1) {
                        $scope.load(customers[0]);
                    }
                }
            });
        });
    };
    $scope.isCustomerLoaded = function () {
        return $scope.customer != null && $scope.customer.id != null && $scope.customer.id > 0;
    };

    function loadCustomerById(id, cb) {
        var u = utils.url('/crm/customer/' + id);
        utils.get(u, {}, cb);
    }

    $scope.lookupCustomer = function () {
        loadCustomerById($scope.id, function (c) {
            $scope.$apply(function () {
                $scope.load(c);
            });
        });
    };

    $scope.save = function () {
        var id = $scope.id;
        var data = {   firstName:$scope.customer.firstName, lastName:$scope.customer.lastName  };

        exists($scope.customer, 'id', function (pName, val) {
            data[pName] = val;
        });
        exists($scope.customer, 'signupDate', function (pN, v) {
            data[pN] = v;
        });
        var idReceivingCallback = function (id) {
            console.log('id is ' + id);
            $scope.$apply(function () {
                $scope.id = id;
                $scope.lookupCustomer();
            });

        };

        var u = null;
        if (id != null && id > 0) {
            // then we're simply going to update it
            u = utils.url('/crm/customer/' + id);
            console.log('JSON to send' + JSON.stringify(data))
            utils.post(u, JSON.stringify(data), idReceivingCallback);
        }
        else {
            u = utils.url('/crm/customers');
            utils.put(u, data, idReceivingCallback);
        }

    };

    $scope.trash = function () {
        $scope.id = null;
        $scope.customer = null;
    };
}


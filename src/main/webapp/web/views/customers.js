/***
 * Controller to handle interfacing with the RESTful endpoint
 */
$.ajaxSetup({
    cache:false
});

var utils = {
    _url:'',
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
            error:function () {
                alert('error trying to retrieve ' + u);
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
            headers:headers,
            data:data,
            success:function (result) {
                cb(result);
            },
            error:function (e) {
                console.log('error PUT\'ing to url ' + url + '. ' + JSON.stringify(e));
            }
        });     // todo

    },
    post:function (u, data, cb) {
        $.ajax({
            type:'POST',
            url:u,
            cache:false,
            dataType:'json',
            data:data,
            contentType:'application/json; charset=utf-8',
            success:cb,
            error:function () {
                alert('error trying to post to ' + u);
            }
        });
    }
};

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
        utils.get(u, {}, function (customers) {
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
        var u = utils.url('/crm/customers/' + id);
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

        function exists(o, p, cb) {
            if (o[p] && o[p] != null) {
                cb(p, o[p]);
            }
        }

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
            u = utils.url('/crm/customers/' + id);
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


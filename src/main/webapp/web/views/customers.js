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
    post:function (u, data, cb) {
        $.ajax({
            type:'POST',
            url:u,
            cache:false,
            // headers : getAuthorizationHeader(),
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

    $scope.isCustomerLoaded = function () {
        return $scope.customer != null;
    };

    function loadCustomerById(id, cb) {
        var u = utils.url('/crm/customer/' + id);
        utils.get( u, {}, cb);
    }

    $scope.lookupCustomer = function () {
        loadCustomerById($scope.id, function (c) {
            $scope.$apply(function () {
                $scope.customer = c;
            });
        });
    };

    $scope.save = function () {
        var id = $scope.id;
        var u = utils.url('/crm/customer/' + id);
        var data = JSON.stringify($scope.customer);
        utils.post(u, data, function(){});
    };

    $scope.trash = function () {
        $scope.id = null;
        $scope.customer = null;
    };
}


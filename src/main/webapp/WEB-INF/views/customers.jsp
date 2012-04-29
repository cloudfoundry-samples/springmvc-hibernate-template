<!doctype html>
<html ng-app>
<head>

    <script src="${context}/web/assets/js/jquery-1.7.2.min.js"></script>
    <script src="${context}/web/assets/js/angular-1.0.0rc6.js"></script>
    <link rel="stylesheet" href="${context}/web/assets/bootstrap/bootstrap.css">
    <script src="${context}/web/views/customers.js"></script>
	<link rel="stylesheet" href="${context}/web/views/customers.css"/>
</head>
<body>
 <script language = "javascript" type = "text/javascript">
     <!--
		 $(function(){
		  utils.setup( '${context}'); 
		})
 	   //   utils.setup( '${context}');
    //-->
    </script>
<h2>Customer Data </h2>

<div ng-controller="CustomerCtrl">
    <div>
        <form class="well form-search" ng-submit="lookupCustomer()">
            <label> Search by ID</label>
            <input type="text" ng-model="id" class="input-medium search-query" width="5" size="5" placeholder="customer #">
            <button type="submit" class="btn btn-primary" ng-click="lookupCustomer()" >
                <a class="icon-search"></a>
            </button>
        </form>
    </div>

    <form class="form-horizontal" ng-submit="updateCustomer">
        <fieldset>
            <legend>
                <span class="customer-visible-{{!isCustomerLoaded()}}"> Create New Customer </span>
                <span class="customer-visible-{{isCustomerLoaded()}}"> Update {{customer.firstName}} {{customer.lastName}} - {{customer.id}} </span>
            </legend>
            <div class="control-group">
                <label class="control-label" for="fn">First Name:</label>
                <div class="controls">
                    <input class="input-xlarge" id="fn" type="text" ng-model="customer.firstName" placeholder="first name"  required="required"/>
                    <p class="help-block">Change the first name</p>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="ln">Last Name:</label>
                <div class="controls">
                    <input class="input-xlarge" id="ln" type="text" ng-model="customer.lastName" placeholder="last name"  required="required"/>
                    <p class="help-block">Change the last name</p>
                </div>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary" ng-click="save()" ng-model-instant>
                    <a class="icon-plus"></a> Save
                </button>
                <button class="btn " ng-click="trash()"><a class="icon-trash"></a> Cancel</button>
            </div>
        </fieldset>
    </form>
</div>
</body>
</html>
/*jquery相关*/
$(document).ready(function(){
	

});
var app = angular.module('login', ['ui.bootstrap']);
app.directive("passwordVerify", function() {
	   return {
	      require: "ngModel",
	      scope: {
	        passwordVerify: '='
	      },
	      link: function(scope, element, attrs, HomeLoginController) {
	        scope.$watch(function() {
	            var combined;

	            if (scope.passwordVerify || HomeLoginController.$viewValue) {
	               combined = scope.passwordVerify + '_' + HomeLoginController.$viewValue; 
	            }                    
	            return combined;
	        }, function(value) {
	            if (value) {
	            	HomeLoginController.$parsers.unshift(function(viewValue) {
	                    var origin = scope.passwordVerify;
	                    if (origin !== viewValue) {
	                    	HomeLoginController.$setValidity("passwordVerify", false);
	                        return undefined;
	                    } else {
	                    	HomeLoginController.$setValidity("passwordVerify", true);
	                        return viewValue;
	                    }
	                });
	            }
	        });
	     }
	   };
	});

function HomeLoginController($scope,$http) {
	$("#frmLogin").submit(function(){
		 if($("#username").val()===""){
			 alert("用户名不能为空!");
			 return false;
		 }
		 if($("#userpass").val()===""){
			 alert("密码不能为空!");
			 return false;
		 }
	});
	
	
	// function to submit the form after all validation has occurred			
	$scope.submitForm = function(isValid) {
		// check to make sure the form is completely valid
		if (isValid) { 
			var username=$scope.user.username;
			var userpass=$scope.user.userpass;
			var useremail=$scope.user.useremail;
			$http(
				      {
				          method: 'POST',
				          url: "/gy404yun/user/save",
				          data: $.param({"username":username,"userpass":userpass,"useremail":useremail}),
				          headers: {
				              'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
				          }
				        }).success(function (data, status, headers, config) {
				        	alert(data.msg);
				         }).error(function (data, status, headers, config) {
					       alert(status);
					       return false;
				 });
		}
	};
	
	


	
};
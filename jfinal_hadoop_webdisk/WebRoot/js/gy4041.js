/*jquery相关*/
$(document).ready(function(){
	

});

/*AngularJS相关*/
var app = angular.module('gy4041', ['ui.bootstrap']);

function gy404body($scope, $http) {
	/*  视频插件下载按钮 触发事件 */
	   $scope.downloadcodecs=function(filepath){
			//alert(filepath);
			window.location.href="/gy404yun/file/downloadcodecs?filepath="+filepath;
		};
};
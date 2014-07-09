/*jquery相关*/
$(document).ready(function(){
	

});
var app = angular.module('login', ['ui.bootstrap']);


/*AngularJS相关*/
function MyBody($scope,$http) {
	
	 $scope.selectdir="";//表示选中的目录
	 $scope.dirid="";//表示选中的目录的ID号
	 
	 $scope.dirlist="";//初始化目录数组
	 
	 getAllDir();//初始化文件夹列表
	 
	 
	 $scope.filelist="";//文件列表
	 $scope.currentPage=1;//当前的页数
	 $scope.maxSize = 5;

	 
	 $scope.setPage = function (pageNo) {
	   $scope.currentPage = pageNo;
	   //console.log('dirid: ' + $scope.dirid);
	   //$scope.dirid=$scope.dirid;
	 };
	 $scope.pageChanged = function() {
		    //console.log('Page changed to: ' + $scope.currentPage);
		    //console.log('dirid: ' + $scope.dirid);
		    queryfilesbydirid($scope.dirid);
		  };

	 
     
	/* 添加文件夹 按钮 触发件 */
	 $(".contain").on("click","#adddir_bt",function(){
		 
		var dirname=$(this).parent().prev().val();//新建文件夹名称
		if(dirname===""){
			alert("文件夹名称不可为空！");
			return false;
		}
		$http(
			      {
			          method: 'POST',
			          url: "/gy404yun/dir/addDir",
			          data: $.param({"dirname":dirname}),
			          headers: {
			              'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
			          }
			        }).success(function (data, status, headers, config) {
			        	alert(data.message);
			        	
			        	if(data.status===1)//只有创建成功的时候才刷新文件夹列表
			        	getAllDir();//刷新文件夹列表
			        	
			         }).error(function (data, status, headers, config) {
				       alert("系统发生错误，请检查网络是否畅通！");
				       return false;
			 });
		
		//$(this).parent().prev().val("");
	 });
	

	 /* 获取当前云盘所有文件夹 公共js */
	 function getAllDir(){
		 $http(
			      {
			          method: 'POST',
			          url: "/gy404yun/dir/getAllDir",
			          //data: $.param({"dirname":dirname}),
			          headers: {
			              'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
			          }
			        }).success(function (data, status, headers, config) {
			        	 $scope.dirlist=data.dirlist;

			         }).error(function (data, status, headers, config) {
				       alert("系统发生错误，请检查网络是否畅通！");
				       return false;
			 });
		
	  }

	 

	 
	 
	 /* 通过dirid查询该目录文件列表 */
	 function queryfilesbydirid(dirid){
		 $http(
			      {
			          method: 'POST',
			          url: "/gy404yun/file/getfilelist",
			          data: $.param({"dirid":dirid,"nowpage":$scope.currentPage}),
			          headers: {
			              'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
			          }
			        }).success(function (data, status, headers, config) {
			        	 $scope.filelist=data.filelist.list;
			        	 $scope.totalItems=data.filecount;

			         }).error(function (data, status, headers, config) {
				       alert("系统发生错误，请检查网络是否畅通！");
				       return false;
			 });
	 }
	 	 
	 
	 /* 文件夹单击  触发事件 */
	 $scope.select_dir=function(id,name){
		 $scope.selectdir=name;
		 $scope.dirid=id;
		 queryfilesbydirid(id);//查询该目录下的文件列表
	 };
	 
	 /* 文件上传表单  提交事件 */
	 $("#text_form").submit(function(){
		 if($scope.selectdir===""){
			 alert("请先选择上传的云端目录！");
			 return false;
		 }
		 if($("#uploadfile").val()===""){
			 alert("请先选择文件！");
			 return false;
		 }
		 
		$(this).ajaxSubmit(function(data){	
				alert(data.message);
				queryfilesbydirid($scope.dirid);//刷新当前目录下文件列表
				});
			return false;
		});
	 
	 
	 /* 文件删除 按钮触发事件 */
	 $scope.deletebyid=function(id){
		 $http(
			      {
			          method: 'POST',
			          url: "/gy404yun/file/deletefile",
			          data: $.param({"fileid":id}),
			          headers: {
			              'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
			          }
			        }).success(function (data, status, headers, config) {
			        	alert(data.message);
			        	queryfilesbydirid($scope.dirid);//刷新当前目录下的文件列表
			         }).error(function (data, status, headers, config) {
				       alert("系统发生错误，请检查网络是否畅通！");
				       return false;
			 });
	 };
	 
	 
	/*  下载按钮 触发事件 */
    $scope.download=function(fileid){
		alert(fileid);
		window.location.href="/gy404yun/file/downloadfile?fileid="+fileid;
	};
	
	
}
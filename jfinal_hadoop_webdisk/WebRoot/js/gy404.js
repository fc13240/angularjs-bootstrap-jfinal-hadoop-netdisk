/*jquery相关*/
$(document).ready(function(){
	

});

/*AngularJS相关*/
var app = angular.module('gy404', ['ui.bootstrap','angularFileUpload']);

function gy404body($scope, $http, $timeout, $upload) {


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
	 $(".container").on("click","#adddir_bt",function(){
		 
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
	 	 
	 	 /* 通过关键字查询该目录文件列表 */
	  $scope.queryfilesbykey=function(){
	 var key=$("#keywords").val();
		 $http(
			      {
			          method: 'POST',
			          url: "/gy404yun/file/getfilelistbykey",
			          data: $.param({"key":key,"nowpage":$scope.currentPage}),
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
	 
	 $scope.queryfilesbydirid1=function(){
		 //alert("刷新");
		 queryfilesbydirid($("#dirid11").val());
	 };
	 
	 /* 通过文件类型查询该类型文件列表 */
	 $scope.queryfilesbyfiletype=function(filetype){
		 $http(
			      {
			          method: 'POST',
			          url: "/gy404yun/file/getfilelist1",
			          data: $.param({"filetype":filetype,"nowpage":$scope.currentPage}),
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
	 };
	 
	
	 
	 /* 文件夹单击  触发事件 */
	 $scope.select_dir=function(id,name){
		 $scope.selectdir=name;
		 $scope.dirid=id;
		 queryfilesbydirid(id);//查询该目录下的文件列表
	 };
	 
	 /* 文件上传表单  提交事件 */
	 /*$("#text_form").submit(function(){
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
		});*/
	 
	 
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
		//alert(fileid);
		window.location.href="/gy404yun/file/downloadfile?fileid="+fileid;
	};
	
	
	/*  视频插件下载按钮 触发事件 */
   $scope.downloadcodecs=function(filepath){
		//alert(filepath);
		window.location.href="/gy404yun/file/downloadcodecs?filepath="+filepath;
	};
	
	/*  查看按钮 触发事件 */
	   $scope.viewfile=function(fileid){
		   $http(
				      {
				          method: 'POST',
				          url: "/gy404yun/file/viewfile",
				          data: $.param({"fileid":fileid}),
				          headers: {
				              'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
				          }
				        }).success(function (data, status, headers, config) {
				        	console.log("filename:"+data.filename);
				        	console.log("filetype:"+data.filetype);
				        	console.log("flexurl:"+data.flexurl);
				        	console.log("win7codecs:"+data.win7codecs);
				        	console.log("xpcodecs:"+data.xpcodecs);
				        	var flexurl1=data.flexurl;
				        	
				        	
window.open("/gy404yun/viewfile.jsp?filename="+data.filename+"&filetype="+data.filetype+"&flexurl="+encodeURIComponent(encodeURIComponent(flexurl1))+"&win7codecs="+data.win7codecs+"&xpcodecs="+data.xpcodecs);
//alert(data);
				        	//console.log(data);
				        	//queryfilesbydirid($scope.dirid);//刷新当前目录下的文件列表
				         }).error(function (data, status, headers, config) {
					       alert("系统发生错误，请检查网络是否畅通！");
					       return false;
				 });
		};
	
	
	
	$scope.fileReaderSupported = window.FileReader != null;
	$scope.uploadRightAway = true;
	$scope.hasUploader = function(index) {
		return $scope.upload[index] != null;
	};
	$scope.abort = function(index) {
		$scope.upload[index].abort(); 
		$scope.upload[index] = null;
	};
	$scope.onFileSelect = function($files) {
		$scope.selectedFiles = [];
		$scope.progress = [];
		if ($scope.upload && $scope.upload.length > 0) {
			for (var i = 0; i < $scope.upload.length; i++) {
				if ($scope.upload[i] != null) {
					$scope.upload[i].abort();
				}
			}
		}
		$scope.upload = [];
		$scope.uploadResult = [];
		$scope.selectedFiles = $files;
		$scope.dataUrls = [];
		for ( var i = 0; i < $files.length; i++) {
			var $file = $files[i];
			if (window.FileReader && $file.type.indexOf('image') > -1) {
				var fileReader = new FileReader();
				fileReader.readAsDataURL($files[i]);
				var loadFile = function(fileReader, index) {
					fileReader.onload = function(e) {
						$timeout(function() {
							$scope.dataUrls[index] = e.target.result;
						});
					};
				}(fileReader, i);
			}
			$scope.progress[i] = -1;
			
		}
		
	};

	$scope.start = function(index) {
		$scope.progress[index] = 0;
		$scope.errorMsg = null;
		
			$scope.upload[index] = $upload.upload({
				url : '/gy404yun/file/uploadfile?dirid='+$("#dirid11").val()+'&dirname='+encodeURIComponent(encodeURIComponent($("#dirname11").val())),
				method: "post",
				//headers: {'my-header': 'my-header-value'},
				//data: $.param({"dirid":$("#dirid11").val(),"dirname": $("#dirname11").val()}),
				file: $scope.selectedFiles[index],
				fileFormDataName: 'myFile'
			}).then(function(response) {
				alert(response.data.message);
				//$scope.uploadResult.push(response.data);
			}, function(response) {
				if (response.status > 0) $scope.errorMsg = response.status + ': ' + response.data;
			}, function(evt) {
				// Math.min is to fix IE which reports 200% sometimes
				$scope.progress[index] = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
				//alert(response.data.message);
				//queryfilesbydirid($scope.dirid);//刷新当前目录下文件列表
			}).xhr(function(xhr){
				xhr.upload.addEventListener('abort', function() {console.log('abort complete')}, false);
			});
		
	};
	
};


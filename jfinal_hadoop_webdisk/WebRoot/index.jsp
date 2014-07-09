<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*,com.hadoop.gy404.model.UserInfo,com.jfinal.core.Controller;"
    pageEncoding="UTF-8"%>
	
	<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html>
<html ng-app="gy404">
<head>

<base href="<%=basePath%>">

<title>gy404凌云</title>

<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=no" />
<meta name="keywords" content="关键词,5个左右,单个8汉字以内" />
<meta name="description" content="网站描述，字数尽量空制在80个汉字，160个字符以内！" />
<link rel="Bookmark" href="favicon.ico" />
<link rel="Shortcut Icon" href="favicon.ico" />


<script type="text/javascript" src="<%=basePath%>js/html5shiv.js"></script>
<!--自己的样式-->
<!--[if IE 6]>
    <script type="text/javascript" src="<%=basePath%>js/DD_belatedPNG_0.0.8a-min.js" ></script>
    <script>DD_belatedPNG.fix('.pngfix,.icon');</script>
    <![endif]-->

<!-- angularjs插件 -->
<script type="text/javascript" src="<%=basePath%>js/angular-1.2.18/angular.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/ui-bootstrap-tpls-0.11.0.min.js"></script>
<!-- jquery插件 -->
<script type="text/javascript" src="<%=basePath%>js/jquery-1.11.1.min.js"></script>

<!-- bootstrap插件 -->
<script type="text/javascript" src="<%=basePath%>js/bootstrap-3.2.0/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="<%=basePath%>js/bootstrap-3.2.0/css/bootstrap.min.css"/>

<!-- modernizr插件 -->
<script type="text/javascript" src="<%=basePath%>js/modernizr-2.8.2.js"></script>

<!-- Font-Awesome插件 -->
<link rel="stylesheet" href="<%=basePath%>js/Font-Awesome-3.2.1/css/font-awesome.min.css">

<!-- angular-multifile-upload插件 -->
<script src="<%=basePath%>js/angular-multifile-upload/angular-file-upload-shim.js"></script>
<script src="<%=basePath%>js/angular-multifile-upload/angular-file-upload.js"></script>
<link type="text/css" rel="stylesheet" href="<%=basePath%>js/angular-multifile-upload/common.css">

<!-- 全局JS -->
<script type="text/javascript" src="<%=basePath%>js/gy404.js"></script>
</head>

<body class="container"  ng-controller="gy404body">

<div class="row">
	<div class="col-xs-12" style="background-color:rgb(18,85,152);">
		<div class="row">
      <div class="col-xs-1 col-sm-1">
  <div style="background:url(./images/left1.png) no-repeat;width:18px;height:32px;">
     	</div>
  </div>
      <div class="col-xs-6">
  <div  class="col-xs-offset-0" style="background:url(./images/logo.gif) no-repeat;width:132px;height:68px;">
     	</div>
  </div>
  <div class="col-xs-6">
  <div  class="col-xs-offset-0" style="width:132px;height:68px;">
     	</div>
  </div>
  <div class="col-xs-10">
  <div class="col-xs-5 col-xs-offset-12">
  <div style="color:white">
        <% 
        UserInfo user=(UserInfo)request.getSession().getAttribute("user");
        if(user != null){%>
            <span>欢迎：<%=user.get("username")%></span>
            <span><a href="<%=basePath%>user/logout" style="color:white;text-decoration:underline;font-weight:bold;">退出</a></span>
        <% }%>
    </div>
     	</div>
  </div>
  </div>
	</div>
  <div class="col-xs-2">
<ul class="nav nav-pills navbar-default nav-stacked" style="max-width: 300px;">
      <li class="active" ng-click="queryfilesbydirid2();" style="cursor:pointer"><a><i class="icon-hdd icon-large"></i><label style="text-indent:1em">全部文件</label></a></li>
      <li ng-click="queryfilesbyfiletype('文档');" style="cursor:pointer"><a><i class="icon-book icon-large"></i><label style="text-indent:1em">我的文档</label></a></li>
      <li ng-click="queryfilesbyfiletype('图片');" style="cursor:pointer"><a><i class="icon-picture icon-large"></i><label style="text-indent:1em">我的图片</label></a></li>
      <li ng-click="queryfilesbyfiletype('音乐');" style="cursor:pointer"><a><i class="icon-music icon-large"></i><label style="text-indent:1em">&nbsp;我的音乐</label></a></li>
      <li ng-click="queryfilesbyfiletype('视频');" style="cursor:pointer"><a><i class="icon-film icon-large"></i><label style="text-indent:1em">我的视频</label></a></li>
      <li ng-click="queryfilesbyfiletype('压缩包');" style="cursor:pointer"><a><i class="icon-envelope-alt icon-large"></i><label style="text-indent:1em">我的压缩包</label></a></li>
      <li ng-click="queryfilesbyfiletype('其它');" style="cursor:pointer"><a><i class="icon-file-alt icon-large"></i><label style="text-indent:1em">&nbsp;我的其它</label></a></li>
      <li onClick="javascript:alert('完善中');" style="cursor:pointer"><a><i class="icon-share icon-large"></i><label style="text-indent:1em">我的分享</label></a></li>
      <li onClick="javascript:alert('完善中');" style="cursor:pointer"><a><i class="icon-star icon-large"></i><label style="text-indent:1em">我的收藏</label></a></li>
      <li onClick="javascript:alert('完善中');" style="cursor:pointer"><a><label style="text-indent:1em">FTP文件抓取</label></a></li>
    </ul>
</div>
  <div class="col-xs-10">
<!-- 文件夹预览 -->
<img  ng-repeat="item in dirlist" src="<%=basePath%>images/file.jpg" width="60" class="img-circle" id="popover_dir_{{item.id}}"
onmouseover="$(this).popover('show');"
onmouseleave="$(this).popover('hide');"  ng-click="select_dir(item.id,item.name)"
data-placement="bottom" data-toggle="popover" data-html=true
data-content="<span class='label label-success'>文件夹名称：{{item.name}}</span><br><br>
<span class='label label-success'>已上传文件数：{{item.sonfilenum}}个</span>
" data-original-title="<b>文件夹描述</>">


<!-- 添加文件夹 -->
<img src="<%=basePath%>images/addfile.jpg" width="40" class="img-circle" id="popover_adddir" 
onmouseover="$('#popover_adddir').popover('show');" data-placement="right"  data-toggle="popover" data-html=true
data-content="<div class='col-lg-16'>
    <div class='input-group'>
     <input type='text' class='form-control' placeholder='输入文件夹名称'>
      <span class='input-group-btn'>
        <button class='btn btn-default' type='button' id='adddir_bt'>确认添加</button>
      </span>
    </div>
  </div>" data-original-title="<b>添加文件夹</b>" >
<div class="clearfix" style="height:5px;"></div> <div>
 <div class="col-lg-6">
    <div class="input-group">
      <input type="text" class="form-control" placeholder="请输入文件名称或相关关键词">
      <span class="input-group-btn">
        <button class="btn btn-warning" type="button" onClick="javascript:alert('完善中');">搜索文件</button>
      </span>
    </div><!-- /input-group -->
  </div><!-- /.col-lg-6 -->
  
  <button type="button" class="btn btn-info" data-toggle="modal" data-target="#myModal">上传文件到云盘</button>
  <br/><span class="label label-success">当前路径：/</span>
  <span class="label label-info" ng-bind="selectdir"></span>
</div><!-- /.row -->

<div class="clearfix" style="height:5px;"></div>

<!-- 文件列表 -->
<table class="table table-hover table-bordered table-striped  table-condensed" >
<thead>
	<tr>
  <td class="active">选择</td>
  <td class="success">文件名称</td>
  <td class="warning">文件大小</td>
  <td class="danger">文件类型</td>
  <td class="active">下载次数</td>
  <td class="success">工具栏</td>
  </tr>
</thead>
<tbody>
 <tr align="center" ng-repeat="item in filelist">
  <td width="50"><input type="checkbox"></td>
  <td ng-bind="item.filename"></td>
  <td ng-bind="item.filesize"></td>
  <td ng-bind="item.filetype"></td>
  <td ng-bind="item.download_num"></td>
  <td width=300>
  <button type="button" class="btn btn-primary" ng-click="viewfile(item.id)">查看</button>
  <button type="button" class="btn btn-primary" ng-click="download(item.id)">下载</button><!--  ng-href="javascript:window.location.href='/file/downloadfile?fileid={{item.id}}'" -->
  <button type="button" class="btn btn-danger" ng-click="deletebyid(item.id)">删除</button>
  <button type="button" class="btn btn-success" onClick="javascript:alert('完善中');">重命名</button>
  </td>
</tr>
</tbody>
</table>
<center>
<div>
   <pagination total-items="totalItems" ng-model="currentPage" ng-change="pageChanged()" items-per-page="5" max-size="maxSize" class="pagination pagination-lg" boundary-links="true" rotate="true" num-pages="numPages" previous-text="上一页" next-text="下一页" first-text="首页" last-text="末页"></pagination>
    <pre>第{{currentPage}} / {{numPages}}页</pre>
</div>
</center>

</div>


<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
 <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">文件上传</h4>
      </div>
      <div class="modal-body">

<form id="text_form" action="" enctype="multipart/form-data" method="post">
  <table class="table table-bordered">
  <tbody>
  <tr><td class="success">
  <input type="hidden" name="dirname" id="dirname11" value="{{selectdir}}">
  <input type="hidden" name="dirid" id="dirid11" value="{{dirid}}">
  <input type="file" name="file" id="uploadfile" ng-file-select="onFileSelect($files)" multiple onclick="this.value=null" value="选择文件">
  <div ng-show="dropSupported" class="drop-box" ng-file-drop="onFileSelect($files);" ng-file-drop-available="dropSupported=true">或将需要上传的文件拖拽到此处</div>
		<div ng-show="!dropSupported">该浏览器不支持HTML5文件拖拽</div></td>
  </tr>
  <tr>
		<td><div class="err" ng-show="errorMsg!=null"><span>{{errorMsg}}</span></div>
		<br/>
		<font>进度:</font>
		<br/>
		<br/>
		<div ng-show="selectedFiles!=null">
			<div class="sel-file" ng-repeat="f in selectedFiles">
  <div class="row">
					<div class="col-xs-1">
						<span>{{($index + 1) + '.'}}</span>
					</div>
					<div class="col-xs-4">
						<span>{{f.name}}</span><img ng-show="dataUrls[$index]" ng-src="{{dataUrls[$index]}}">
					</div>
					<div class="col-xs-1"></div>
					<div class="col-xs-1">
						<button class="btn" ng-click="start($index)" ng-show="progress[$index] < 0 || progress[$index] < 100">开始</button>
					</div>					
					<div class="col-xs-1"></div>
					<div class="col-xs-1">
						<span class="progress" ng-show="progress[$index] >= 0"></span>
					</div>
					<div class="col-xs-1">
						<div style="width:'{{progress[$index]}}%'"><span>{{progress[$index]}}%</span></div>
					</div>
					<div class="col-xs-1"></div>
					<div class="col-xs-1">
						<button class="btn" ng-click="abort($index)" ng-show="hasUploader($index) && progress[$index] < 100">停止</button>
					</div>
					<!-- <div class="col-xs-1">
						<span>{{f.name}} - size: {{f.size}}B - type: {{f.type}}"</span>
					</div> -->
				</div>		
			</div>
		</div>
		</td>
  </tr>
  </tbody>
  </table>
  </form>       


      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal" id="closeupload" ng-click="queryfilesbydirid1()">关闭</button>
<!--         <button type="button" class="btn btn-primary">上传</button> -->
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->


</div>

</body>
</html>

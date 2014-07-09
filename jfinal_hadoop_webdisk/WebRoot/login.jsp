<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html>
<html ng-app="login">
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

<!-- 全局JS -->
<script type="text/javascript" src="<%=basePath%>js/login.js"></script>
</head>

<body class="container" ng-controller="HomeLoginController">
<div class="container-fluid" style="background-color:rgb(18,85,152);">
	<div class="row-fluid">
  <div class="col-xs-9 col-sm-3" style="">
  <div class="row">
      <div class="col-xs-8 col-sm-3" style="">
  <div style="background:url(./images/left1.png) no-repeat;width:18px;height:32px;">
     	</div>
  </div>
  
      <div class="col-xs-8 col-sm-8" style="">
  <div  class="col-xs-offset-0" style="background:url(./images/logo.gif) no-repeat;width:132px;height:68px;">
     	</div>
  </div>
  </div>
  <div class="clearfix visible-xs-block"></div>

  
  <div class="clearfix visible-xs-block"></div>
     <div class="row"><div class="col-xs-8 col-sm-3" style="height:68px;"></div></div>
   
   <div class="row">
      <div class="col-xs-8 col-sm-3" style="">
 <div style="background:url(./images/left2.gif) no-repeat;width:50px;height:100px;vertical-align:middle;">
     	</div>
  </div>
  </div>
    <div class="row"><div class="col-xs-8 col-sm-3" style="height:68px;"></div></div>

  </div>
  <div class="clearfix visible-xs-block"></div>
  
  <div class="col-xs-5 col-sm-4" style="">
  <div class="row"><div class="col-xs-8 col-sm-10" style="height:68px;"></div></div>
  <div class="row"><div class="col-xs-8 col-sm-10" style="height:68px;color:white;"><p>GY404 凌云盘</p></div></div>
  <div class="row"><div class="col-xs-8 col-sm-10" style="color:white;">
     	 <p style="text-indent:2em">在生活中，你能和好友相互分享、传送文件，增加生活的娱乐性。</p>
 <p style="text-indent:2em">在工作中，你能和同事或者老板相互传送工作文件，提高自己的工作效率。</p>
 <p style="text-indent:2em">凌云，让你生活工作两不误！</p>
     	</div>
     	</div>
     	<div class="row"><div class="col-xs-8 col-sm-3" style="height:68px;"></div></div>
     	</div>
  <!-- Optional: clear the XS cols if their content doesn't match in height -->
  
  
  <div class="clearfix visible-xs-block"></div>
  
    <div class="col-xs-5 col-sm-4" style="">
 <div class="row"><div class="col-xs-8 col-sm-3" style="height:68px;"></div></div>
  <div class="row"><div style="color:white;"><p>登录</p></div>
  <div>
     			<br/><form id="frmLogin" action="<%=basePath%>user/login" method="post">
            	<div class="form-control input-lg input-group">
              <span class="input-group-btn input-group-addon input-lg first-child last-child">
              <i class="icon-user"></i>
              </span>
              <input name="username" id="username" type="text" class="input-group input-lg" placeholder="请输入用户名"/>
              </div>
              <div class="clearfix" style="height:5px;"></div>
              <div class="form-control input-lg input-group">
              <span class="input-group-btn input-group-addon input-lg first-child last-child">
              <i class="icon-key"></i>
              </span>
              <input name="userpass" id="userpass" type="password" class="input-group input-lg" placeholder="请输入密码"/>
              </div>
               <div class="clearfix" style="height:5px;"></div>
              <button type="submit" class="btn-lg btn-primary btn-block">登录</button>
                      			
    	 		</form>
    	 	</div>
    	 	<div><p>
                	<span class="-label">&nbsp;</span>
                	<span class="-error-msg">
                	<% String msg="";
                	if(request.getAttribute("msg")!=null) 
                		msg=request.getAttribute("msg").toString();
                	%>
               		<%=msg %> </span>
               		<span style="color:white;">还没帐号？点击
<a data-toggle="modal" href="#myModal" style="color:white;text-decoration:underline;font-weight:bold;">这里</a>
               		注册</span>
           	 	</p>
 

<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        <h4 class="modal-title" id="myModalLabel">用户注册</h4>
      </div>
      <div class="modal-body">
<form name="userForm" ng-submit="submitForm(userForm.$valid)" novalidate> <!-- novalidate prevents HTML5 validation since we will be validating ourselves -->

        <!-- USERNAME -->
    <div class="form-group" ng-class="{ 'has-error' : userForm.username.$invalid && !userForm.username.$pristine }">
        <label>用户名</label>
        <input type="text" name="username" class="form-control" ng-model="user.username" placeholder='用户名' ng-minlength="3" ng-maxlength="8" required>
        <p ng-show="userForm.username.$error.required" class="help-block">必填项!</p>
        <p ng-show="userForm.username.$error.minlength" class="help-block">用户名不能少于3个字符</p>
        <p ng-show="userForm.username.$error.maxlength" class="help-block">用户名不能大于8个字符</p>
    </div>
    
    <!-- USERPASS -->
    <div class="form-group" ng-class="{ 'has-error' : userForm.userpass.$invalid && !userForm.userpass.$pristine }">
        <label>密码</label>
        <input type="password" name="userpass" class="form-control" ng-model="user.userpass" placeholder='密码' ng-minlength="3" ng-maxlength="8" required>
        <p ng-show="userForm.userpass.$error.required" class="help-block">必填项!</p>
        <p ng-show="userForm.userpass.$error.minlength" class="help-block">密码不能少于3个字符</p>
        <p ng-show="userForm.userpass.$error.maxlength" class="help-block">密码不能大于8个字符</p>
    </div>
      
    <!-- CONFIRM_USERPASS -->
    <div class="form-group" ng-class="{ 'has-error' : userForm.confirm_password.$invalid && !userForm.confirm_password.$pristine }">
        <label>确认密码</label>    
        <input type="password" name='confirm_password' class="form-control" ng-model='user.password_verify' placeholder='确认密码' required data-password-verify="user.userpass">
      	<p ng-show="userForm.confirm_password.$error.required" class="help-block">必填项!</p>
      	<p ng-show="userForm.confirm_password.$error.passwordVerify" class="help-block">密码与确认密码不一致！</p>
     </div>   
        
    <!-- EMAIL -->
    <div class="form-group" ng-class="{ 'has-error' : userForm.user.useremail.$invalid && !userForm.user.useremail.$pristine }">
        <label>邮箱</label>
        <input type="email" name="useremail" class="form-control" ng-model="user.useremail" placeholder='邮箱' required>
        <p ng-show="userForm.useremail.$error.required" class="help-block">必填项!</p>
        <p ng-show="userForm.useremail.$invalid && !userForm.useremail.$pristine" class="help-block">请输入正确的邮箱</p>
    </div>
        
        <!-- SUBMIT BUTTON -->
        <button type="submit" class="btn btn-primary">保存</button>
        
    </form>

      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
    </div>
  </div>
</div>

           	 </div>
           	 </div>
         <div class="row"><div class="col-xs-8 col-sm-3" style="height:68px;"></div></div>
  </div>

	</div>
</div>

</body>

</html>

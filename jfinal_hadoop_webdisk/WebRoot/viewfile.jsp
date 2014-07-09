<%@ page language="java" import="java.util.*,java.net.URLDecoder;"  contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html>
<html ng-app="gy4041">
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
<script type="text/javascript" src="<%=basePath%>js/gy4041.js"></script>

<link rel="stylesheet" type="text/css" href="<%=basePath%>js/flexpaper.css" />
   <script type="text/javascript" src="<%=basePath%>js/flexpaper.js"></script>
    <script type="text/javascript" src="<%=basePath%>js/flexpaper_handlers.js"></script>
    
</head>

<body class="container"  ng-controller="gy404body">
<div class="container-fluid">
<%
String filename="";
String filetype="";
String flexurl="";
String win7codecs="";
String xpcodecs="";


if(request.getParameter("filename")!=null)
	filename=request.getParameter("filename").toString();
	System.out.println("filename:"+filename);
	
	if(request.getParameter("filetype")!=null)
	filetype=request.getParameter("filetype").toString();
	
	if(request.getParameter("flexurl")!=null)
	flexurl=request.getParameter("flexurl").toString();
	flexurl=URLDecoder.decode(flexurl);
	
	flexurl=basePath+flexurl.substring(1);
	
	if(request.getParameter("win7codecs")!=null)
		win7codecs=request.getParameter("win7codecs").toString();
	System.out.println("win7codecs:"+win7codecs);
	if(request.getParameter("xpcodecs")!=null)
		xpcodecs=request.getParameter("xpcodecs").toString();
	System.out.println("xpcodecs:"+xpcodecs);
%>
	 <div class="col-xs-12">
		<div><h1>文件名：<%=filename%></h1>
		</div>
	 </div>
	 <div class="col-xs-12">
	 	<div><h2>文件类型：<%=filetype%></h2>
		</div>
	 </div>
	 <div class="col-xs-12">
	 	<div>
	 	<h3>文件预览：</h3>
	 	<br/>
	 	<br/>
	 	<%
	 		if(filetype.equals("图片"))
	 		{
	 	%>
	 		<img src="<%=flexurl%>" alt="<%=filename%>" />
	 		<%
	 		}else if(filetype.equals("文档"))
	 		{
	 		%>
	 		<div id="documentViewer" class="flexpaper_viewer" style="height:700px;"></div>
	 		 <script type="text/javascript">
        $("#documentViewer").FlexPaperViewer(
        {config: {
        SWFFile: escape("<%=flexurl%>"),
        Scale: 0.6,
        ZoomTransition: 'easeOut',
        ZoomTime: 0.5,
        ZoomInterval: 0.2,
        FitPageOnLoad: false,
        FitWidthOnLoad: true,
        FullScreenAsMaxWindow: false,
        ProgressiveLoading: true,
        MinZoomSize: 0.2,
        MaxZoomSize: 5,
        SearchMatchAll: false,
        PrintEnabled: false,
        PrintVisible: true,
        InitViewMode: 'Portrait',
        RenderingOrder: 'flash,html',
        StickyTools: true,
        ViewModeToolsVisible: true,
        ZoomToolsVisible: true,
        NavToolsVisible: true,
        CursorToolsVisible: true,
        SearchToolsVisible: true,
        WMode: 'window',
        localeChain: 'zh_CN'
        }}
        );  
        </script>
        <%
        }else if(filetype.equals("音频") || filetype.equals("视频"))
 		{
	 	%>
	 		<div id="videoViewer" >
    <div style="width:960px;overflow: auto;float:left;">

<object classid="CLSID:22d6f312-b0f6-11d0-94ab-0080c74c7e95" id="MediaPlayer1" width="100%" height="600" >  
<param name="AudioStream" value="1" >  
<param name="AutoSize" value="1" >  
<!--是否自动调整播放大小-- >  
<param name="AutoStart" value="1" >  
<!--是否自动播放-->
<param name="AnimationAtStart" value="1">
<param name="AllowScan" value="1">
<param name="AllowChangeDisplaySize" value="1">
<param name="AutoRewind" value="0">
<param name="Balance" value="0">
<!--左右声道平衡,最左-9640,最右9640-->
<param name="BaseURL" value>
<param name="BufferingTime" value="15">
<!--缓冲时间-->
<param name="CaptioningID" value>
<param name="ClickToPlay" value="1">
<param name="CursorType" value="0">
<param name="CurrentPosition" value="0">
<!--当前播放进度1表示不变,0表示开头单位是秒,比如10表示从第10秒处开始播放,值必须是1.0或大于等于0-->
<param name="CurrentMarker" value="0">
<param name="DefaultFrame" value>
<param name="DisplayBackColor" value="0">
<param name="DisplayForeColor" value="16777215">
<param name="DisplayMode" value="0">
<param name="DisplaySize" value="0">
<!--视频1-50%,0100%,2-200%,3-全屏其它的值作0处理,小数则采用四舍五入然后按前的处理-->
<param name="Enabled" value="1">
<param name="EnableContextMenu" value="1">
<!--是否用右键弹出菜单控制-->
<param name="EnablePositionControls"  value="1">
<param name="EnableFullScreenControls"  value="1">
<param name="EnableTracker"  value="1">
<!--是否允许拉动播放进度条到任意地方播放-->
<param name="Filename"  value="<%=flexurl%>"  valuetype="ref">

<!--播放的文件地址-->
<param name="InvokeURLs"  value="1">
<param name="Language"  value="1">
<param name="Mute"  value="0">
<!--是否静音-->
<param name="PlayCount"  value="10">
<!--重复播放次数,0为始终重复-->
<param name="PreviewMode"  value="1">
<param name="Rate"  value="1">
<!--播放速率控制,1为正常,允许小数-->
<param name="SAMIStyle"  value>
<!--SAMI样式-->
<param name="SAMILang"  value>
<!--SAMI语言-->
<param name="SAMIFilename"  value>
<!--字幕ID-->
<param name="SelectionStart"  value="1">
<param name="SelectionEnd"  value="1">
<param name="SendOpenStateChangeEvents"  value="1">
<param name="SendWarningEvents"  value="1">
<param name="SendErrorEvents"  value="1">
<param name="SendKeyboardEvents"  value="0">
<param name="SendMouseClickEvents"  value="0">
<param name="SendMouseMoveEvents"  value="0">
<param name="SendPlayStateChangeEvents"  value="1">
<param name="ShowCaptioning"  value="0">
<!--是否显示字幕,为一块黑色,下面会有一大块黑色,一般不显示-->
<param name="ShowControls"  value="1">
<!--是否显示控制,比如播放,停止,暂停-->
<param name="ShowAudioControls"  value="1">
<!--是否显示音量控制-->
<param name="ShowDisplay"  value="0">
<!--显示节目信息,比如版权等-->
<param name="ShowGotoBar"  value="0">
<!--是否启用上下文菜单-->
<param name="ShowPositionControls"  value="1">
<!--是否显示往前往后及列表,如果显示一般也都是灰色不可控制-->
<param name="ShowStatusBar"  value="1">
<!--当前播放信息,显示是否正在播放,及总播放时间和当前播放到的时间-->
<param name="ShowTracker"  value="1">
<!--是否显示当前播放跟踪条,即当前的播放进度条-->
<param name="TransparentAtStart"  value="1">
<param name="VideoBorderWidth"  value="0">
<!--显示部的宽部,如果小于视频宽,则最小为视频宽,或者加大到指定值,并自动加大高度.此改变只改变四周的黑框大小,不改变视频大小-->
<param name="VideoBorderColor"  value="0">
<!--显示黑色框的颜色,为RGB值,比如ffff00为黄色-->
<param name="VideoBorder3D" value="0">
<param name="Volume" value="0">
<!--音量大小,负值表示是当前音量的减值,值自动会取绝对值,最大为0,最小为-9640-->
<param name="WindowlessVideo"  value="0">
<!--如果是0可以允许全屏,否则只能在窗口中查看-->
</object>
<br/>
<br/>
<div><font>如果不正常播放，请下载解码器。win7系统请下载：<button type="button" class="btn btn-primary" ng-click="downloadcodecs('<%=win7codecs%>')">点击下载</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;xp系统请下载：<button type="button" class="btn btn-primary" ng-click="downloadcodecs('<%=xpcodecs%>')">点击下载</button></font></div>
<br/>
<br/>
</div>
</div>
<%} %>
		</div>
	 </div>
</div>

</body>

</html>

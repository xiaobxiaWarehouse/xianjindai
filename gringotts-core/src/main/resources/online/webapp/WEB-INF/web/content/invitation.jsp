<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String path = request.getContextPath();
    String basePath = path + "/common/web/zmxy";
%>
<c:set var="path" value="<%=path%>"></c:set>
<c:set var="basePath" value="<%=basePath%>"></c:set>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>我的邀请</title>
<meta http-equiv="Expires" content="-1">               
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache"> 
<meta name="format-detection" content="telephone=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<link rel="stylesheet" type="text/css" href="${basePath}/css/jquery.mobile-1.4.2.min.css">
<link rel="stylesheet" type="text/css" href="${basePath}/css/basic.css" />
<link rel="stylesheet" type="text/css" href="${basePath}/css/common.css" />
 
<script type="text/javascript" src="${basePath}/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${basePath}/js/jquery.mobile-1.4.2.min.js"></script>

</head>
<body style="background:#fff;">
    <div class="ui-page ui-page-theme-a ui-page-active" data-role="page"  style="background:#fff;"><!--
        <div class="header">
            <a class="back_arr" href="javascript:history.back();"></a>
            <h1>我的邀请</h1>
        </div>
        --><!-- main start -->
        <div class="wrapper">
          <ul class="explain invite">
             <li>
               <h3>邀请方式一：</h3>
               <p>请把邀请码告知好友后，使用最新版APP注册，在邀请码一项填写该邀请码，注册后即成为邀请关系。</p>
             </li>
             <li>
               <h3>邀请方式二：</h3>
                <p>在APP“我的”TAB页面点击推荐给好友，好友在分享页面注册后将成为您邀请的好友</p>
             </li>
            
          </ul>
          
        </div>
        <!-- main end -->
        
    </div>
</body>
</html>

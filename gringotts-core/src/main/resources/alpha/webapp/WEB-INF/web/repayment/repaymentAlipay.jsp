<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    String path = request.getContextPath();
    String basePath = path + "/common/web/zmxy";
%>
<c:set var="path" value="<%=path%>"></c:set>
<c:set var="basePath" value="<%=basePath%>"></c:set>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>支付宝支付页面</title>
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
    <link rel="stylesheet" type="text/css" href="${basePath}/css/style.css" />
    <link rel="stylesheet" type="text/css" href="${basePath}/css/theme-orange.css" />
    <link rel="stylesheet" type="text/css" href="${basePath}/css/validate.css" />

    <script type="text/javascript" src="${basePath}/js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="${basePath}/js/base.js"></script>
    <script type="text/javascript" src="${basePath }/js/global-1.1.0.min.js"></script>
    <script type="text/javascript" src="${basePath }/js/jquery.mobile-1.4.2.min.js"></script>
    <script src="${basePath }/js/jquery-mvalidate.js"></script>

</head>
<body style="background:#fff;">
<div class="ui-page ui-page-theme-a ui-page-active" data-role="page"  style="background:#fff;">
    <div class="wrapper">
        <div class="apply">
            <h2>申请还款支付费用，请确认并支付！</h2>
            <input type="hidden" id="borrowId" vale="${bo.id}">
            <input type="hidden" id="orderId" value=""/>
            <ul class="zl_info tk_new sqxf">
                <li>
                    <a rel="external" href="javascript:;">
                        <span>支付金额<strong><fmt:formatNumber pattern='###,###,##0.00' value="${(repayment.repaymentAmount - repayment.repaymentedAmount) / 100.00}"/></strong><em>元</em></span>
                    </a>
                </li>
                <li>
                    <a rel="external" href="javascript:;">
                        <span style="width:100%;">支付方式<strong>支付宝支付</strong></span>
                    </a>
                </li>
            </ul>
        </div>
        <a href="javascript:;" id="mima-btn-1" class="js-btn" style="border-radius: 0.2rem;">马上支付</a>
    </div>

    <div class="sure-tc" style="height: 6.25rem;">
        <p>确认已经支付了吗？</p>
        <div class="btn-both clearfix" style="padding:0 0.5rem;">
            <a data-ajax="false" href="javascript:;" onclick="querySureTc();" class="btn sure-btn" style="width:7rem;float:left;border:1px solid #31c27c;height:2rem;line-height:2rem;">已支付</a>
            <a href="javascript:;" onclick="closeSureTc();" class="btn sure-btn" style="width:7rem;float:right;background:none;border:1px solid #31c27c; color:#fff !important;height:2rem;line-height:2rem;">未支付</a>
        </div>
    </div>
    <!-- 遮罩层 -->
    <div class="cover"></div>
</div>
<script type="text/javascript">

    function goBack(){
        window.location.href = "${path}/repayment/detail?id=${bo.id}";
    }

    var flag = true;
    jQuery(document).ready(function($) {
        $('#mima-btn-1').click(function(){
            if(!flag){return;}//避免重复操作
            flag = false;
            show_loading("正在处理中，请稍等");
            //调用支付宝app进行支付
            $.post('${path}/alipay/alipay_withhold_req', {id:'${bo.id}'} , function(data){
                console.log(data);
                flag = true;
                hide_loading();

                if(data.code == "-103"){
                    $.mvalidateTip(data.msg);
                }else if(data.code == "0"){
                    $("#orderId").val(data.ext);
                    window.location.href = data.msg;
                    setTimeout(function(){
                        $('.sure-tc,.cover').show();
                    },1000);

                }else{
                    $.mvalidateTip(data.msg);
                }
            });
        });
    });

    function show_loading(msg){
        $.mobile.loading('show', {
            text: msg, // 加载器中显示的文字
            textVisible: true, // 是否显示文字
            theme: 'b',        // 加载器主题样式a-e
            textonly: false,   // 是否只显示文字
            html: ""           // 要显示的html内容，如图片等
        });
    }

    function hide_loading(){
        // 隐藏加载器
        $.mobile.loading('hide');
    }
    var query_flag = true;
    function querySureTc() {
        if(!query_flag){return;}//避免重复操作
        query_flag = true;
        closeSureTc();//关闭窗口
        show_loading("正在查询中，请稍等");
        //调用支付宝app进行支付
        $.post('${path}/alipay/alipay_query_req', {orderId:$("#orderId").val()} , function(data){
            query_flag = true;
            hide_loading();
            if(data.code == "0"){
                showLoader("还款成功");
                goBack();
            }else{
                $.mvalidateTip(data.msg);
            }
        });
    }

    function closeSureTc(){
        $('.sure-tc,.cover').hide();
    }
</script>
</body>
</html>
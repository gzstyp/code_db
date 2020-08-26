<#ftl encoding="utf-8"/>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <meta charset="utf-8"/>
    <title>${nameSpace}添加|编辑</title>
    <link rel="stylesheet" type="text/css" href="static/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="static/themes/icon.css">
    <script type="text/javascript" src="static/js/jquery.min.js"></script>
    <script type="text/javascript" src="static/js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="static/js/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="static/lib/layer/layer.js"></script>
    <script type="text/javascript" src="static/lib/page.common.js"></script>
    <link rel="stylesheet" type="text/css" href="static/css/view.table.css">
</head>
<body style="background-color: white;">
<form id='key_form'>
    <input id="${keyId}" name="${keyId}" value="${r'${'}${keyId}${r'}'}" type="hidden"/>
    <table id="tblAdd" class="view" width=100%; align="center">
		<#list listData as ld>
			<#if "${keyId}" == "${ld.column_name}">
            <#else>
		<tr>
            <td align="right">${ld.column_comment !'字段注释'}</td>
            <td align="left"><input id='${ld.column_name}' name='${ld.column_name}' data-options="prompt:'输入${ld.column_comment !'数据'}'" value="${r'${pd.'}${ld.column_name}${r'}'}" class="easyui-textbox" style="width: 285px;height: 28px;"/></td>
        </tr>
            </#if>
        </#list>
    </table>
</form>
<script type="text/javascript">
    ;$(function () {
    });
    ;(function ($) {
        var base_uri = '${nameSpace}/';
        /*请求controller层的url*/
        thisPage = {
            init: function () {
            },
            submit: function (callBak) {
	<#list listData as ld>
            <#if "${keyId}" == "${ld.column_name}">
            <#else>
                if(verifyFn.inputRequired('#${ld.column_name}')) {
                    layerFn.verifyTips(AppKey.code.code199, '${ld.column_comment !'数据'}不能为空');
                    return;
                }
            </#if>
        </#list>
                var url = base_uri + 'add';
                var params = winFn.formParams('#key_form');
                if(!verifyFn.inputRequired('#${keyId}')) {
                    url = base_uri + 'edit';
                }
                layerFn.submit(url, params, callBak);
            }
        };
        thisPage.init();
    })(jQuery);
</script>
</body>
</html>
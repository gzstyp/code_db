<#ftl encoding="utf-8"/>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <meta charset="utf-8"/>
    <title>${nameSpace}数据列表</title>
    <link rel="stylesheet" type="text/css" href="static/themes/bootstrap/easyui.css">
    <link rel="stylesheet" type="text/css" href="static/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="static/css/view.table.css">
    <style type="text/css">
        <!--
        a:link{
            text-decoration: none;
            outline:none;
        }
        a:hover {
            color:#008eda;
        }
        -->
    </style>
</head>
<body class="easyui-layout" style="padding-top:4px;">
<div data-options="region:'north',title:'查询过滤'" style="height:66px;">
    <form id='search_form'>
        <table style="margin-top:2px;margin-bottom:0px">
            <tr>
                <td align="right">编号</td>
                <td align="left">
                    <input id="CODE" name="CODE" class="easyui-textbox" data-options="prompt:'请输入编号'" style="width: 130px;height: 28px;"/>
                </td>
                <td align="right">时间</td>
                <td align="left">
                    <input id="DATE_START" name="DATE_START" class="easyui-datebox" data-options="editable:true,prompt:'开始日期'"  style="width:100px;height: 28px;"/>
                    <input id="DATE_END" name="DATE_END" class="easyui-datebox" data-options="editable:true,prompt:'结束日期'" style="width:100px;height: 28px;"/>
                </td>
                <td>
                    <button type="button" class="easyui-linkbutton" onclick="thisPage.search(1);" iconCls="icon_search">查询</button>
                </td>
            </tr>
        </table>
    </form>
</div>
<div data-options="region:'center'" style="border: 0px">
    <table id="datagrid_${nameSpace}" border="0"></table>
    <div id="toolbar_${nameSpace}" style="padding-top:2px; padding-bottom:2px;height:auto;">
        <table border="0" style="margin-top:2px;">
            <tr>
                <td><a type="button" class="easyui-linkbutton" href="javascript:;" onclick="thisPage.edit();" iconCls="icon-add">新增</a></td>
                <td><a type="button" class="easyui-linkbutton" href="javascript:;" onclick="thisPage.search();" iconCls="icon_reload">刷新</a></td>
            </tr>
        </table>
    </div>
</div>
<script type="text/javascript" src="static/js/jquery.min.js"></script>
<script type="text/javascript" src="static/lib/jquery.form.js"></script>
<script type="text/javascript" src="static/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="static/js/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="static/lib/layer/layer.js"></script>
<script type="text/javascript" src="static/lib/page.common.js"></script>
<script type="text/javascript">
    ;(function ($) {
        var base_uri = '${nameSpace}/';
        thisPage = {
            datagrid : '#datagrid_${nameSpace}',/*datagrid的id标识*/
            toolbar : '#toolbar_${nameSpace}',/*toolbar的id标识*/
            urlList: base_uri+'listData?',
            urlDel : base_uri+'delById?',
            urlEdit : base_uri+'page?page=${nameSpace}_edit&',/*编辑的页面*/
            init: function () {
                thisPage.initDom();
                thisPage.initDatagrid();
            },
            initDom: function () {
            },
            initDatagrid: function () {
                var _self = this;
                $(_self.datagrid).datagrid({
                    fit: true,
                    url: _self.urlList,
                    pageSize: euiFn.datagrid.settings.pageSize,
                    pageList: euiFn.datagrid.settings.pageList,
                    checkOnSelect: true,
                    pagination: true,
                    fitColumns: true,
                    showFooter: false,
                    striped: true,
                    autoRowHeight: true,
                    rownumbers: true,
                    loadMsg: AppKey.loadMsg,
                    toolbar: _self.toolbar,
                    singleSelect: false,
                    idField: '${keyId}',
                    onBeforeLoad: function (param) {
                    },
                    onLoadSuccess: function (data) {
                    },
                    onLoadError: function () {
                        layerFn.showTips(AppKey.msg_error);
                    },
                    onDblClickRow: function (index,row){
                        thisPage.edit(1,row.GUID);
                    },
                    loadFilter: function (data) {
                        return winFn.dataFilter(data);
                    },
                    columns: [[
                    <#list listData as ld>
                        <#if "${keyId}" == "${ld.column_name}">
                        <#else>
                        {field:'${ld.column_name}',title:'${ld.column_comment !'字段注释'}',width:50,align:'left'},
                        </#if>
                    </#list>
                        {field: 'opt', title: '操作选项', width:20, align: 'center', formatter: function (value, row, index){
                                var html = "<a href='javascript:;' onclick='javascript:thisPage.edit(1,\"" + row.GUID + "\");' title='编辑|修改' style=\"margin-right:8px;\">编辑</a>|<a style=\"margin-left:8px;\" href='javascript:;' onclick='javascript:thisPage.del(\"" + row.GUID + "\");' title='删除'>删除</a>";
                                return html;
                            }
                        },
                    ]]
                });
            },
            /**params为空时是刷新,否则是搜索*/
            search: function (params){
                if (params != null && params != '') {
                    euiFn.refreshDatagrid(thisPage.datagrid,thisPage.urlList + winFn.formParams("#search_form"));
                } else {
                    euiFn.refreshDatagrid(thisPage.datagrid,thisPage.urlList);
                }
            },
            /**type == 1编辑*/
            edit : function(type,GUID){
                var url = thisPage.urlEdit;
                var title;
                if (type == 1){
                    url = url+'GUID='+GUID;
                    title = "编辑";
                }else{
                    title = '添加';
                }
                thisPage.operate(title,url);
            },
            /**添加|编辑*/
            operate : function(title,url){
                layerFn.openWin(url,title,['540px','400px'],function(index,layero){
                    var iframeWin = layerFn.getIframe(layero);
                    iframeWin.thisPage.submit(function(data){
                        euiFn.showRb(data.code,'操作成功');
                        euiFn.refreshDatagrid(thisPage.datagrid,{},1);
                        layerFn.loading.hide(index);
                    });
                });
            },
            /**逻辑删除*/
            del : function(GUID){
                layerFn.handleVerify('数据删除之后无法恢复,确认吗?',function(){
                    layerFn.submit(thisPage.urlDel,{GUID:GUID},function(data){
                        euiFn.showRb(data.code,'操作成功');
                        euiFn.refreshDatagrid(thisPage.datagrid,{},1);
                    });
                });
            }
        };
        thisPage.init();
    })(jQuery);
</script>
</body>
</html>
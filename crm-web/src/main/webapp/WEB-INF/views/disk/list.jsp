<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-公司网盘</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">

    <%@include file="../include/css.jsp"%>

    <link rel="stylesheet" href="/static/plugins/webuploader/webuploader.css">
    <style>
        tr{
            height: 50px;
            line-height: 50px;
        }
        .table>tbody>tr>td{
            vertical-align: middle;
        }
        .file_icon {
            font-size: 30px;
            text-align: center;
        }
        .table>tbody>tr:hover{
            cursor: pointer;
        }
        .webuploader-container {
            display: inline-block;
        }
        .webuploader-pick {
            padding: 5px 10px;
            overflow: visible;
            font-size: 12px;
            line-height:1.5;
            font-weight: 400;
        }
        .tools {
            margin:0px 50px 0px 0px;
            color: red;
            font-size: large;
        }
        .icons {
            margin:0px 10px;
        }
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <!-- 顶部导航栏部分 -->
    <%@include file="../include/header.jsp"%>

    <!-- =============================================== -->

    <!-- 左侧菜单栏 -->
    <jsp:include page="../include/left-side.jsp">
        <jsp:param name="menu" value="file"/>
    </jsp:include>

    <!-- =============================================== -->

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">公司网盘</h3>
                    <div class="box-tools pull-right">
                        <c:if test="${not empty file}">
                            <a href="/disk?_=${file.pId}" class="btn btn-default btn-sm"><i class="fa fa-arrow-left"></i> 返回上级</a>
                        </c:if>
                        <c:choose>
                            <c:when test="${file.type == 'file'}">
                                <a href="/disk/download?_=${file.id}&fileName=${file.fileName}" class="btn btn-sm btn-danger"><i class="fa fa-download"></i> 下载</a>
                            </c:when>
                            <c:otherwise>
                                <div id="picker"><i class="fa fa-upload"></i> 上传文件</div>
                                <button id="newDirBtn" class="btn btn-success btn-sm"><i class="fa fa-plus"></i> 新建文件夹</button>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="box-body no-padding">
                    <table class="table table-hover">
                        <tbody  id="dataTable">
                        <c:if test="${file.type == 'file'}">
                            <tr class="tr">
                                <td width="50" class="file_icon">
                                    <i class="fa fa-file-o"></i>
                                </td>
                                <td class="file_detail" rel="${file.id}">
                                    ${file.fileName}
                                    <span class="tools pull-right">
                                        <i class="fa fa-edit"></i>
                                        <i class="fa fa-trash-o"></i>
                                    </span>
                                </td>
                                <td colspan="2">
                                    <c:choose>
                                        <c:when test="${file.fileName.endsWith('.pdf') or file.fileName.endsWith('.jpg') or file.fileName.endsWith('.png') or file.fileName.endsWith('.bmp') or fileF.fileName.endsWith('.gif')}">
                                            <a href="/disk/download?_=${file.id}" target="_blank" class="btn btn-sm btn-info"><i class="fa fa-search"></i> 在线预览</a>
                                            <a href="/disk/download?_=${file.id}&fileName=${file.fileName}" class="btn btn-sm btn-danger"><i class="fa fa-download"></i> 下载</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="/disk/download?_=${file.id}&fileName=${file.fileName}" class="btn btn-sm btn-danger"><i class="fa fa-download"></i> 下载</a>
                                        </c:otherwise>
                                    </c:choose>

                                </td>
                            </tr>
                        </c:if>
                        <c:if test="${empty fileList}">
                            <tr><td colspan="4">暂无内容</td></tr>
                        </c:if>
                        <c:forEach items="${fileList}" var="file">
                            <tr class="tr" rel="${file.id}">
                                <td width="50" class="file_icon">
                                    <c:choose>
                                        <c:when test="${file.type == 'dir'}">
                                            <i class="fa fa-folder-o"></i>
                                        </c:when>
                                        <c:otherwise>
                                            <i class="fa fa-file-o"></i>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="file_detail" rel="${file.id}">
                                    ${file.fileName}
                                    <sapn class="tools pull-right">
                                        <i class="fa fa-edit"></i>
                                        <i class="fa fa-trash-o"></i>
                                    </sapn>
                                </td>
                                <td><fmt:formatDate value="${file.updateTime}" pattern="YYYY-MM-dd"/> </td>
                                <td width="100">
                                    <c:if test="${file.type == 'file'}">
                                        ${file.fileSize}
                                    </c:if>
                                </td>
                                <td width="150">
                                    <div class="btn-group">
                                        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                                            <i class="fa fa-ellipsis-h"></i>
                                        </button>
                                        <ul class="dropdown-menu">
                                            <c:if test="${file.type == 'file'}">
                                                <li><a href="/disk/download?_=${file.id}">下载</a></li>
                                            </c:if>
                                            <li><a class="reNameLink" rel="${file.id}" href="javascript:;">重命名</a></li>
                                            <li><a href="#">删除</a></li>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                <!-- /.box-body -->
            </div>
            <!-- /.box -->

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <!-- 底部 -->
    <%@include file="../include/footer.jsp"%>

</div>
<!-- ./wrapper -->

<%@include file="../include/js.jsp"%>
<script src="/static/plugins/art-template/art-template.js"></script>
<script src="/static/plugins/webuploader/webuploader.min.js"></script>
<script src="/static/plugins/moment/moment.js"></script>
<script type="text/template" id="trTemplate">
    <tr class="tr">
        <td width="80" class="file_icon">
            <? if(type == 'file') { ?>
            <i class="fa fa-file-o"></i>
            <? } else if(type == 'dir') { ?>
            <i class="fa fa-folder-o"></i>
            <?}?>
        </td>
        <td class="file_detail" rel="{{id}}">{{fileName}}</td>
        <td>{{updateTime}}</td>
        <td width="100">{{fileSize}}</td>
        <td width="100">
            <div class="btn-group">
                <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
                    <i class="fa fa-ellipsis-h"></i>
                </button>
                <ul class="dropdown-menu">
                    <? if(type == 'file') { ?>
                    <li><a href="/disk/download?_=${file.id}">下载</a></li>
                    <? } ?>
                    <li><a class="reNameLink" rel="${file.id}" href="javascript:;">重命名</a></li>
                    <li><a href="#">删除</a></li>
                </ul>
            </div>
        </td>
    </tr>
</script>

<script>
    $(function () {
        var pId = ${not empty requestScope.file ? requestScope.file.id : '0'};
        var accountId = ${sessionScope.curr_account.id};

        <!--重命名-->
        $(document).delegate(".reNameLink","click",function () {
            layer.prompt({title:"请输入新的文件名"},function (fileName,index) {

            });
        });

        <!--拼接 tr-->
        var appendTr = function (data) {
            $("#dataTable").html("");
            for (var i = 0; i < data.data.length; i++) {
                var obj = data.data[i];
                obj.updateTime = moment(obj.updateTime).format("YYYY-MM-DD");
                var html = template("trTemplate", obj);
                $("#dataTable").append(html);
            }
        };

        <!--根据pid异步获得当前文件列表-->
        var getFileList = function () {
            $.get("/disk/list.json?_="+pId).done(function (data) {
                if(data.state == "success") {
                    appendTr(data);
                } else {
                    layer.msg("系统异常");
                }
            }).error(function () {
                layer.msg("系统异常")
            });
        };

        <!--行点击事件,委托模式-->
        $(document).delegate(".file_detail","click",function () {
            var id = $(this).attr("rel");
            window.location.href = "/disk?_="+id;
        });

        <!--创建文件夹-->
        $("#newDirBtn").click(function () {
            layer.prompt({title:"请输入文件夹名称"},function (dirName,index) {
                $.post("/disk/new/dir",{"pId":pId,"fileName":dirName,"accountId":accountId}).done(function (data) {
                    if(data.state == "success") {
                        layer.close(index);
                        getFileList();
                        layer.msg("创建文件夹成功");
                    } else {
                        layer.msg(data.message);
                    }
                }).error(function () {
                    layer.msg("系统异常")
                });
            });
        });

        <!--文件上传-->
        var uploader = WebUploader.create({
            pick:"#picker",
            swf:'/static/plugins/webuploader/Uploader.swf',
            server:'/disk/upload',
            auto:true,
            fileVal:"file",
            formData:{
                "pId":pId,
                "accountId":accountId
            }
        });
        var loadIndex = -1;
        //开始上传
        uploader.on('uploadStart',function (file) {
            loadIndex = layer.load(2);
        });

        uploader.on('uploadSuccess',function (file,data) {
            if(data.state == 'success') {
                layer.msg("文件上传成功");
                appendTr(data);
            } else {
                layer.msg(data.message);
            }
        });

        uploader.on('uploadError',function (file) {
            layer.msg("上传失败，服务器异常");
        });

        uploader.on('uploadComplete',function (file) {
            layer.close(loadIndex);
        });

    });

</script>
</body>
</html>

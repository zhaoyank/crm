<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-客户资料</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <%@include file="../include/css.jsp"%>
    <style>
        .td_title {
            font-weight: bold;
        }
        .form-control {
            border : none;
        }
        .form-control[disabled], .form-control[readonly], fieldset[disabled] .form-control {
            background-color: white;
        }
        .table>tbody>tr>td {
            vertical-align : middle;
        }
    </style>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <!-- 顶部导航栏部分 -->
    <%@include file="../include/header.jsp"%>

    <!-- =============================================== -->

    <!-- 左侧菜单栏 -->
    <jsp:include page="../include/left-side.jsp">
        <jsp:param name="menu" value="customer"/>
    </jsp:include>

    <!-- =============================================== -->

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">
        <!-- Main content -->
        <section class="content">

            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">客户资料</h3>
                    <div class="box-tools">
                        <button id="saveEditBtn" class="btn btn-primary btn-sm" style="display: none;"><i class="fa fa-save"></i> 保存</button>
                        <a href="javascript:;history.back()" class="btn btn-primary btn-sm"><i class="fa fa-arrow-left"></i> 返回列表</a>
                        <button class="btn bg-purple btn-sm" id="editCustomerBtn"><i class="fa fa-pencil"></i> 编辑</button>
                        <button class="btn bg-orange btn-sm"><i class="fa fa-exchange"></i> 转交他人</button>
                        <button class="btn bg-maroon btn-sm"><i class="fa fa-recycle"></i> 放入公海</button>
                        <button class="btn btn-danger btn-sm"><i class="fa fa-trash-o"></i> 删除</button>
                    </div>
                </div>
                <div class="box-body no-padding">
                    <form action="">
                    <table class="table">
                        <tr>
                            <td class="td_title"><span>姓名</span></td>
                            <td><input type="text" disabled name="custName" class="form-control" value="${customer.custName}"></td>
                            <td class="td_title">职位</td>
                            <td><input type="text" class="form-control" name="job" disabled value="${customer.job}"></td>
                            <td class="td_title">联系电话</td>
                            <td><input type="text" class="form-control" name="mobile" disabled value="${customer.mobile}"></td>
                        </tr>
                        <tr>
                            <td class="td_title">所属行业</td>
                            <td>
                                <select name="trade" class="form-control" disabled>
                                    <option value="${customer.trade}" selected>${customer.trade}</option>
                                </select>
                            </td>
                            <td class="td_title">客户来源</td>
                            <td>
                                <select name="source" class="form-control" disabled>
                                    <option value="${customer.source}" selected>${customer.source}</option>
                                </select>
                            </td>
                            <td class="td_title">级别</td>
                            <td>
                                <select name="level" class="form-control" disabled>
                                    <option value="${customer.level}" selected>${customer.level}</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="td_title">地址</td>
                            <td colspan="5"><input type="text" class="form-control" name="address" disabled value="${customer.address}"></td>
                        </tr>
                        <tr>
                            <td class="td_title">备注</td>
                            <td colspan="5"><input type="text" class="form-control" name="mark" disabled value="${customer.mark}"></td>
                        </tr>
                    </table>
                    </form>
                </div>

                <div class="box-footer">
                    <span style="color: #ccc" class="pull-right">创建日期： <fmt:formatDate value="${customer.createTime}"/>&nbsp;&nbsp;&nbsp;&nbsp;
                        最后修改日期：<fmt:formatDate value="${customer.createTime}"/></span>
                </div>

            </div>

            <div class="row">
                <div class="col-md-8">
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">跟进记录</h3>
                        </div>
                        <div class="box-body">

                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">日程安排</h3>
                        </div>
                        <div class="box-body">

                        </div>
                    </div>
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">相关资料</h3>
                        </div>
                        <div class="box-body">

                        </div>
                    </div>
                </div>
            </div>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <!-- 底部 -->
    <%@include file="../include/footer.jsp"%>

</div>
<!-- ./wrapper -->

<%@include file="../include/js.jsp"%>

<script>
    $("#editCustomerBtn").click(function () {
        $("#saveEditBtn").css("display","inline");
        $(".form-control").removeAttr("disabled");
    });

    $("#saveEditBtn").click(function () {
        $("#saveEditBtn").css("display","none");
        $(".form-control").attr("disabled", "disabled")
    });
</script>

</body>
</html>

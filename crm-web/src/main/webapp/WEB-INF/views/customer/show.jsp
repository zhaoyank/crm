<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-客户资料</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">

    <%@include file="../include/css.jsp"%>
    <link rel="stylesheet" href="/static/dist/css/skins/_all-skins.min.css">
    <link rel="stylesheet" href="/static/plugins/datetimepicker/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" href="/static/plugins/datepicker/datepicker3.css">
    <style>
        .td_title {
            font-weight: bold;
        }
        .edit {
            border : none;
        }
        .form-control[disabled], .form-control[readonly], fieldset[disabled] .form-control {
            background-color: white;
        }
        .table>tbody>tr>td {
            vertical-align : middle;
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
        <jsp:param name="menu" value="customer_my"/>
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
                        <a href="/customer/my" class="btn btn-primary btn-sm"><i class="fa fa-arrow-left"></i> 返回列表</a>
                        <button class="btn bg-purple btn-sm" id="editCustomerBtn"><i class="fa fa-pencil"></i> 编辑</button>
                        <button class="btn bg-orange btn-sm" id="tranCustomerBtn"><i class="fa fa-exchange"></i> 转交他人</button>
                        <button class="btn bg-maroon btn-sm" id="publicCustomerBtn"><i class="fa fa-recycle"></i> 放入公海</button>
                        <button class="btn btn-danger btn-sm" id="deleteCustomerBtn"><i class="fa fa-trash-o"></i> 删除</button>
                    </div>
                </div>
                <div class="box-body no-padding">
                    <form method="post" id="editCustomerForm">
                    <table class="table">
                        <tr>
                            <td class="td_title"><span>姓名</span></td>
                            <td><input type="text" disabled name="custName" class="form-control edit" value="${customer.custName}"></td>
                            <td class="td_title">职位</td>
                            <td><input type="text" class="form-control edit" name="job" disabled value="${customer.job}"></td>
                            <td class="td_title">联系电话</td>
                            <td><input type="text" class="form-control edit" name="mobile" disabled value="${customer.mobile}"></td>
                        </tr>
                        <tr>
                            <td class="td_title">所属行业</td>
                            <td>
                                <select name="trade" class="form-control edit" disabled>
                                    <option value="" ${customer.trade == "" ? "selected":""}></option>
                                    <c:forEach items="${tradeList}" var="trade">
                                        <option value="${trade.tradeName}" ${trade.tradeName == customer.trade ? "selected" : ""} >${trade.tradeName}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td class="td_title">客户来源</td>
                            <td>
                                <select name="source" class="form-control edit" disabled>
                                    <option value="" ${customer.source == "" ? "selected":""}></option>
                                    <c:forEach items="${sourceList}" var="source">
                                        <option value="${source.sourceName}" ${source.sourceName == customer.source ? "selected" : ""}>${source.sourceName}</option>
                                    </c:forEach>

                                </select>
                            </td>
                            <td class="td_title">级别</td>
                            <td>
                                <select name="level" class="form-control edit" disabled>
                                    <option value="" ${customer.level == "" ? "selected":""}></option>
                                    <option value="★" ${customer.level == "★" ? "selected":""}>★</option>
                                    <option value="★★" ${customer.level == "★★" ? "selected":""}>★★</option>
                                    <option value="★★★" ${customer.level == "★★★" ? "selected":""}>★★★</option>
                                    <option value="★★★★" ${customer.level == "★★★★" ? "selected":""}>★★★★</option>
                                    <option value="★★★★★" ${customer.level == "★★★★★" ? "selected":""}>★★★★★</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="td_title">地址</td>
                            <td colspan="5"><input type="text" class="form-control edit" name="address" disabled value="${customer.address}"></td>
                        </tr>
                        <tr>
                            <td class="td_title">备注</td>
                            <td colspan="5"><input type="text" class="form-control edit" name="mark" disabled value="${customer.mark}"></td>
                        </tr>
                    </table>
                    </form>
                </div>



                <div class="box-footer">
                    <div>
                        <span style="color: #ccc" class="pull-right">创建日期： <fmt:formatDate value="${customer.createTime}"/>&nbsp;&nbsp;&nbsp;&nbsp;
                        最后修改日期：<fmt:formatDate value="${customer.createTime}"/></span>
                    </div>
                    <div class="box-tools">
                        <button id="saveEditBtn" class="btn btn-primary btn-sm" style="display: none;"><i class="fa fa-save"></i> 保存</button>
                        <button id="cancelEditBtn" type="reset" class="btn btn-primary btn-sm" style="display: none;"><i class="fa fa-save"></i> 取消</button>
                    </div>
                </div>

            </div>

            <div class="row">
                <div class="col-md-8">
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">销售机会</h3>
                        </div>
                        <div class="box-body">
                            <ul class="list-group">
                                <c:forEach items="${saleChanceList}" var="chance">
                                    <li class="list-group-item">
                                        <a href="/sales/my/${chance.id}" target="_blank">${chance.name}</a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">日程安排</h3>
                            <a id="addTaskLink" href="javascript:;" class="pull-right"><i class="fa fa-plus"></i></a>
                        </div>
                        <div class="box-body">
                            <ul class="list-group">
                                <c:forEach items="${taskList}" var="task">
                                    <li class="list-group-item">
                                        <a href="/task">${task.title}</a>
                                        <span class="time pull-right"><i class="fa fa-clock-o"></i><fmt:formatDate value="${task.finishTime}"/></span>
                                    </li>
                                </c:forEach>
                            </ul>
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

    <!-- 模态框 -->
    <div class="modal fade" id="chooseAccountModal">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">请选择要转交的账号</h4>
                </div>
                <div class="modal-body">
                    <select id="userSelect" class="form-control" style="border: 1px solid #ccc">
                        <option value="">--请选择--</option>
                        <c:forEach items="${accountList}" var="account">
                            <c:if test="${account.id != requestScope.accountId}">
                                <option value="${account.id}">${account.userName}(${account.mobile})</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" id="saveTranBtn" class="btn btn-primary">确定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
    <!-- /模态框 -->

    <!-- 模态框 -->
    <div class="modal fade" id="addTaskModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="modal-title">添加计划任务</h4>
                </div>
                <div class="modal-body">
                    <form id="addTaskForm" action="/customer/new/task" method="post">
                        <div class="form-group">
                            <input type="hidden" name="accountId" value="${requestScope.accountId}"/>
                            <input type="hidden" name="custId" value="${requestScope.customer.id}">
                            <label>任务名称</label>
                            <input type="text" name="title" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>完成日期</label>
                            <input type="text" class="form-control finishTime" name="finishTime">
                        </div>
                        <div class="form-group">
                            <label>提醒时间</label>
                            <input type="text" class="form-control remindTime" name="remindTime">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal" id="resetBtn">取消</button>
                    <button type="button" class="btn btn-primary" id="saveTaskBtn">保存</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

</div>
<!-- ./wrapper -->

<%@include file="../include/js.jsp"%>
<script src="/static/plugins/validate/jquery.validate.min.js"></script>
<script src="/static/plugins/datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
<script src="/static/plugins/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script src="/static/plugins/moment/moment.js"></script>
<script src="/static/plugins/datepicker/bootstrap-datepicker.js"></script>
<script src="/static/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js"></script>
<script>

    var customerId = ${customer.id};

    $("#deleteCustomerBtn").click(function () {
        layer.confirm("确定要删除此客户么?",function () {
            window.location.href = "/customer/my/" + customerId + "/delete";
        });
    });



    //将客户放入公海
    $("#publicCustomerBtn").click(function () {
        layer.confirm("确定要将客户放入公海吗?",function (index) {
            layer.close(index);
            window.location.href = "/customer/my/"+customerId+"/public";
        });
    });

    //转交他人
    $("#tranCustomerBtn").click(function () {
        $("#chooseAccountModal").modal({
            show : true,
            backdrop: 'static'
        });
    });
    $("#saveTranBtn").click(function () {
        var toAccountId = $("#userSelect").val();
        var toAccountName = $("#userSelect option:selected").text();
        layer.confirm("确定要将客户转交给"+toAccountName+"吗",function (index) {
            layer.close(index);
            window.location.href = "/customer/my/"+customerId+"/tran/"+toAccountId;
        });
    });


    <!-- 编辑 -->
    $("#editCustomerBtn").click(function () {
        $(".edit").css("border","1px solid #ccc");
        $("#saveEditBtn").css("display","inline");
        $("#cancelEditBtn").css("display","inline");
        $(".edit").removeAttr("disabled");
    });
    $("#cancelEditBtn").click(function () {
        layer.confirm("取消后将不会保修已修改的信息",function (index) {
            $("#editCustomerForm")[0].reset();
            $(".edit").css("border","none");
            $("#saveEditBtn").css("display","none");
            $("#cancelEditBtn").css("display","none");
            $(".edit").attr("disabled", "disabled");
            layer.close(index);
        })
    });
    $("#saveEditBtn").click(function () {
        $("#editCustomerForm").submit();
    });
    $("#editCustomerForm").validate({
        errorClass: "text-danger",
        errorElement : "span",
        rules : {
            custName : {
                required : true
            },
            mobile : {
                required : true,
                digits : true
            }
        },
        messages : {
            custName : {
                required : "请输入姓名"
            },
            mobile : {
                required : "请输入手机号",
                digits : "格式错误(手机号码只能是纯数字组成)"
            }
        }
    });

    <!-- 添加计划任务 -->
    $("#addTaskLink").click(function () {
        $("#addTaskModal").modal({
            show : true,
            backdrop : 'static'
        });
    });
    $("#saveTaskBtn").click(function () {
        $("#addTaskForm").submit();
    });
    $("#addTaskForm").validate({
        errorClass : "text-danger",
        errorElement : "span",
        rules : {
            title : {
                required : true
            },
            finishTime : {
                required : true
            }
        },
        messages : {
            title : {
                required : "请输入任务内容"
            },
            finishTime : {
                required : "请选择任务完成时间"
            }
        }
    });
    $("#resetBtn").click(function () {
        $("#addTaskForm")[0].reset();
    });

    <!-- 时间框 -->
    var picker = $('.finishTime').datepicker({
        format: "yyyy-mm-dd",
        language: "zh-CN",
        autoclose: true,
        todayHighlight: true,
        startDate:"now()"
    });
    picker.on("changeDate",function (date) {
        var today = moment().format("YYYY-MM-DD");
        $('.remindTime').datetimepicker('setStartDate',today);
        $('.remindTime').datetimepicker('setEndDate', date.format('yyyy-mm-dd'));
    });
    var timepicker = $('.remindTime').datetimepicker({
        format: "yyyy-mm-dd hh:ii",
        language: "zh-CN",
        autoclose: true,
        todayHighlight: true,
        startDate:"now()"
    });


</script>

</body>
</html>

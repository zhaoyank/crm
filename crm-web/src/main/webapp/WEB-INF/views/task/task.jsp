<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-计划任务</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">

    <%@include file="../include/css.jsp"%>
    <link rel="stylesheet" href="/static/dist/css/skins/_all-skins.min.css">
    <link rel="stylesheet" href="/static/plugins/datetimepicker/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" href="/static/plugins/datepicker/datepicker3.css">


</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <!-- 顶部导航栏部分 -->
    <%@include file="../include/header.jsp"%>

    <!-- =============================================== -->

    <!-- 左侧菜单栏 -->
    <jsp:include page="../include/left-side.jsp">
        <jsp:param name="menu" value="task"/>
    </jsp:include>

    <!-- =============================================== -->

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">计划任务</h3>

                    <div class="box-tools pull-right">
                        <button id="addTaskBtn" class="btn btn-success btn-sm"><i class="fa fa-plus"></i> 新增任务</button>
                        <button id="viewTask" class="btn btn-primary btn-sm"><i class="fa fa-eye"></i> 显示所有任务</button>
                    </div>
                </div>
                <div class="box-body">

                    <ul class="todo-list">

                        <c:if test="${empty taskList}">
                            <li>
                                <span class="text">暂无计划任务,点击<a href="javascript:;" id="addTaskLink">新增任务</a>添加一条计划任务</span>
                            </li>
                        </c:if>

                        <c:forEach items="${taskList}" var="task">
                            <li class="${task.done == 1 ? 'done' : ''}">
                                <input type="checkbox" class="task_checkbox" ${task.done==1 ? 'checked' : ''} value="${task.id}">
                                <span class="text">${task.title}</span>
                                    <%-- <c:choose>
                                         <c:when test="${not empty task.customer and not empty task.customer.id}">
                                             <a href="/customer/my/${task.customer.id}"><i class="fa fa-user-o"></i> ${task.customer.custName}</a>
                                         </c:when>
                                         <c:when test="${not empty task.saleChance and not empty task.saleChance.id}">
                                             <a href="/sales/my/${task.saleChance.id}"><i class="fa fa-money"></i> ${task.saleChance.name}</a>
                                         </c:when>
                                     </c:choose>--%>
                                <small class="label ${task.isOverTime() ? 'label-danger' : 'label-success'}"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${task.finishTime}"/></small>
                                <div class="tools">
                                    <i class="fa fa-edit edit_task" rel="${task.id}"></i>
                                    <i class="fa fa-trash-o del_task" rel="${task.id}"></i>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
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

    <!-- 模态框 -->
    <div class="modal fade" id="addTaskModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="modal-title">添加计划任务</h4>
                </div>
                <div class="modal-body">
                    <form id="addTaskForm" action="/task/new" method="post">
                        <div class="form-group">
                            <input type="hidden" name="accountId" value="${sessionScope.curr_account.id}">
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

    <!-- 模态框 修改任务-->
    <div class="modal fade" id="editTaskModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">修改计划任务</h4>
                </div>
                <div class="modal-body">
                    <form id="editTaskForm" action="/task/edit" method="post">
                        <div class="form-group">
                            <input type="hidden" name="accountId" value="${sessionScope.curr_account.id}">
                            <input type="hidden" name="id" id="id">
                            <label>任务名称</label>
                            <input type="text" name="title" class="form-control" id="title">
                        </div>
                        <div class="form-group">
                            <label>完成日期</label>
                            <input type="text" class="form-control finishTime" name="finishTime" id="finishTime">
                        </div>
                        <div class="form-group">
                            <label>提醒时间</label>
                            <input type="text" class="form-control remindTime" name="remindTime" id="remindTime">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" id="editTaskBtn">保存</button>
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
    $(function () {

        $(".edit_task").click(function () {
            $("#editTaskForm")[0].reset();
            var id = $(this).attr("rel");
            $.get("/task/"+id+"/task.json").done(function (data) {
                if(data.state == "success") {
                    var task = data.data;
                    $("#id").val(id);
                    $("#title").val(task.title);
                    $("#finishTime").val(moment(task.finishTime).format("YYYY-MM-DD"));
                    if(task.remindTime) {
                        $("#remindTime").val(moment(task.remindTime).format("YYYY-MM-DD HH:mm"));
                    }
                } else {
                    layer.msg("系统异常,请稍候再试");
                }
            });
            $("#editTaskModal").modal({
                show : true,
                backdrop : "static"
            });
        });
        $("#editTaskBtn").click(function () {
            $("#editTaskForm").submit();
        });
        $("#editTaskForm").validate({
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



        <!-- 修改完成状态 -->
        $(".task_checkbox").click(function () {
            var id = $(this).val();
            var checked = $(this)[0].checked;
            if(checked) {
                window.location.href = "/task/"+id+"/state/done";
            } else {
                window.location.href = "/task/"+id+"/state/undone"
            }
        });


        <!-- 删除 -->
        $(".del_task").click(function () {
            var id = $(this).attr("rel");
            layer.confirm("确定要删除吗",function () {
                window.location.href = "/task/"+id+"/delete";
            });
        });


        <!-- 添加 -->
        $("#addTaskBtn").click(function () {
            $("#addTaskModal").modal({
                show : true,
                backdrop : "static"
            });
        });
        $("#addTaskLink").click(function () {
            $("#addTaskModal").modal({
                show : true,
                backdrop : "static"
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

    });

</script>
</body>
</html>

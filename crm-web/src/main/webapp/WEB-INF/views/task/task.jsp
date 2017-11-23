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

    <style>
        #viewTask {
            display: none;
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
                        <button id="hidDoneTask" class="btn btn-primary btn-sm"><i class="fa fa-eye-slash"></i> 隐藏已完成任务</button>
                    </div>
                </div>
                <div class="box-body">

                    <ul class="todo-list">

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
<script src="/static/plugins/art-template/art-template.js"></script>
<script type="text/template" id="emptyListTemplate">
    <li>
        <span>暂无任务,请点击 <a href="javascript:;" id="addTaskLink">新增任务</a>添加一条新的计划任务</span>
    </li>
</script>
<script type="text/template" id="taskTemplate">
    <? if(done == '1')  { ?>
        <li class="done">
            <input type="checkbox" class="task_checkbox" checked value="{{id}}">
    <? } else if (done == '0') { ?>
        <li>
        <input type="checkbox" class="task_checkbox" value="{{id}}">
    <? } ?>

        <span class="text">{{title}}</span>
        <%-- <c:choose>
             <c:when test="${not empty task.customer and not empty task.customer.id}">
                 <a href="/customer/my/${task.customer.id}"><i class="fa fa-user-o"></i> ${task.customer.custName}</a>
             </c:when>
             <c:when test="${not empty task.saleChance and not empty task.saleChance.id}">
                 <a href="/sales/my/${task.saleChance.id}"><i class="fa fa-money"></i> ${task.saleChance.name}</a>
             </c:when>
         </c:choose>--%>
        <? if(overTime) { ?>
        <small class="label label-danger"><i class="fa fa-clock-o"></i>{{finishTime}}</small>
        <? } else { ?>
        <small class="label label-success"><i class="fa fa-clock-o"></i>{{finishTime}}</small>
        <? } ?>
        <div class="tools">
            <i class="fa fa-edit edit_task" rel="{{id}}"></i>
            <i class="fa fa-trash-o del_task" rel="{{id}}"></i>
        </div>
    </li>
</script>
<script>
    $(function () {

        var getTaskList = function () {
            $.get("/task/list.json").done(function (resp) {
                if (resp.state == "success") {
                    if (resp.data.length == 0) {
                        var html = template("emptyListTemplate");
                        $(".todo-list").append(html);
                    } else {
                        appendLi(resp.data);
                    }
                }
            }).error(function () {
                layer.msg("系统异常")
            });
        }

        getTaskList();

        var appendLi = function (data) {
            $(".todo-list").html("");
            for (var i = 0; i < data.length; i++) {
                var obj = data[i];
                obj.finishTime = moment(obj.finishTime).format("YYYY-MM-DD");
                var html = template("taskTemplate", obj);
                $(".todo-list").append(html);
            }
        }

        $("#viewTask").click(function () {
            var done = $(".done");
            for (var i = 0; i < done.length; i++) {
                $(done[i]).css("display","block");
            }
            $(this).css("display","none");
            $("#hidDoneTask").css("display","inline");
        });
        $("#hidDoneTask").click(function () {
           var done = $(".done");
            for (var i = 0; i < done.length; i++) {
                $(done[i]).css("display","none");
            }
            $(this).css("display","none");
            $("#viewTask").css("display","inline");
        });


        $(document).delegate(".edit_task","click",function () {
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
        $(document).delegate(".task_checkbox","click",function () {
            var id = $(this).val();
            var checked = $(this)[0].checked;
            if(checked) {
               var targetUrl = "/task/"+id+"/state/done";
            } else {
                var targetUrl = "/task/"+id+"/state/undone"
            }
            $.get(targetUrl).done(function (resp) {
                if (resp.state == "success") {
                    getTaskList();
                }
            }).error(function () {
                layer.meg("系统异常")
            });
        });


        <!-- 删除 -->
        $(document).delegate(".del_task","click",function () {
            var id = $(this).attr("rel");
            layer.confirm("确定要删除吗",function () {
                $.get("/task/"+id+"/delete").done(function (resp) {
                    if (resp.state == "success") {
                        getTaskList();
                    }
                }).error(function () {
                    layer.msg("系统异常");
                });
            });
        });


        <!-- 添加 -->
        $("#addTaskBtn").click(function () {
            $("#addTaskModal").modal({
                show : true,
                backdrop : "static"
            });
        });
        $(document).delegate("#addTaskLink","click",function () {
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

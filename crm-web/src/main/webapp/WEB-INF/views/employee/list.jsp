<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-首页</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    
    <%@include file="../include/css.jsp"%>
    <link rel="stylesheet" href="/static/plugins/tree/css/metroStyle/metroStyle.css">
    <link rel="stylesheet" href="/static/plugins/datatables/jquery.dataTables.css">

</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <%@include file="../include/header.jsp"%>

    <!-- =============================================== -->

    <!-- 左侧菜单栏 -->
    <jsp:include page="../include/left-side.jsp">
        <jsp:param name="menu" value="employee"/>
    </jsp:include>

    <!-- =============================================== -->

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">

            <div class="row">
                <div class="col-md-2">
                    <div class="box">
                        <div class="box-body">
                            <button id="addDeptBtn" class="btn btn-default" disabled>添加部门</button>
                            <button id="deleteDeptBtn" disabled type="button" class="btn btn-box-tool" title="Collapse">
                                <i class="fa fa-minus"></i> 删除部门
                            </button>
                            <ul id="ztree" class="ztree"></ul>
                        </div>
                    </div>
                </div>
                <div class="col-md-10">
                    <!-- Default box -->
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">员工管理</h3>
                            <div class="box-tools pull-right">

                                <button id="addEmployeeBtn" type="button" class="btn btn-box-tool" title="Collapse">
                                    <i class="fa fa-plus"></i> 添加员工
                                </button>

                            </div>
                        </div>
                        <div class="box-body">
                            <table class="table"  id="dataTable">
                                <thead>
                                <tr>
                                    <th>id</th>
                                    <th>姓名</th>
                                    <th>部门</th>
                                    <th>手机</th>
                                    <th>操作</th>
                                </tr>
                                </thead>

                            </table>
                        </div>
                    </div>
                    <!-- /.box -->
                </div>
            </div>

        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

   <!-- 模态框 -->

    <!-- Modal -->
    <div class="modal fade" id="addEmployeeModal">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">添加账号</h4>
                </div>
                <div class="modal-body">
                    <form method="post" id="addEmployeeForm">
                        <div class="form-group">
                            <label>姓名</label>
                            <input type="text" id="userName" name="userName" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>手机号码</label>
                            <input type="text" id="mobile" name="mobile" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>密码(默认000000)</label>
                            <input type="password" id="password" name="password" value="000000" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>所在部门</label>
                            <div id="deptDiv"></div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" id="saveEmployeeBtn">保存</button>
                </div>
            </div>
        </div>
    </div>
   <!--  /模态框-->

    <!-- 底部 -->
    <%@include file="../include/footer.jsp"%>

</div>
<!-- ./wrapper -->

<%@include file="../include/js.jsp"%>

<script src="/static/plugins/tree/js/jquery.ztree.all.min.js"></script>
<script src="/static/plugins/datatables/jquery.dataTables.js"></script>
<script src="/static/plugins/validate/jquery.validate.min.js"></script>
<script>
    $(function () {
        var deptId;

        /*DataTable*/
        var dataTable = $("#dataTable").DataTable({
            "processing": true,
            "serverSide": true,
            "ajax": {
                "url" : "/employee/load.json",
                "data" : function(data){
                    data.deptId = deptId;
                }
            },
            "lengthChange": false,
            "pageLength": 25,
            "columns" : [
                {"data":"id"},
                {"data":"userName"},
                {"data" : function(row){
                    var deptArray = row.deptList;
                    var str = [];
                    for(var i = 0; i < deptArray.length; i++) {
                        str.push(deptArray[i].deptName);
                    }
                    return str.toString();
                }},
                {"data" : "mobile"},
                {"data" : function(row){
                    return "<a href='javascript:;' rel='"+row.id+"' class='delEmployeeLink'>删除</a>";
                }}

            ],
            "columnDefs": [
                {
                    "targets": [1,2,3,4],
                    "orderable": false
                },
                {
                    "targets": [0],
                    "visible": false
                }
            ],
            "language" : {
                "emptyTable" : "暂无数据",
                "info" : "第 _START_ 到 _END_ 条记录,查询到_TOTAL_条",
                "infoEmpty": "暂无数据",
                "infoFiltered":"(共 _MAX_ 条)",
                "search": "根据姓名搜索:",
                "zeroRecords": "暂无相关记录",
                "processing":"加载中...",
                "paginate": {
                    "first": "首页",
                    "last": "末页",
                    "next":  "下一页",
                    "previous": "上一页"
                },
            }
        });


        /*zTree*/
        var setting = {
            async : {
                enable: true,
                url:"/dept/list.json",
                dataFilter: ajaxDataFilter
            },

            data: {
                simpleData: {
                    enable: true,
                    pIdKey : "pid",
                    idKey : "id",
                    rootPId : 0
                },
                key : {
                    name : "deptName"
                }
            },

            callback: {
                onClick: function (event, treeId, treeNode, clickFlag) {
                    deptId = treeNode.id;
                    $("#deleteDeptBtn").removeAttr("disabled");
                    $("#addDeptBtn").removeAttr("disabled");
                    dataTable.ajax.reload();
                }
            }
        };
        function ajaxDataFilter(treeId, parentNode, responseData) {
            if (responseData) {
                for(var i =0; i < responseData.length; i++) {
                    if(responseData[i].id == 1) {
                        responseData[i].open = true;
                        break;
                    }
                }
            }
            return responseData;
        };

        $.fn.zTree.init($("#ztree"), setting);

        <!-- 删除部门 -->
        $("#deleteDeptBtn").click(function () {
            layer.confirm("确定要删除么?", function (index) {
                $.post("/dept/" + deptId + "/delete").done(function (data) {
                    if(data.state == "success") {
                        layer.close(index);
                        var treeObj = $.fn.zTree.getZTreeObj("ztree");
                        treeObj.reAsyncChildNodes(null, "refresh");
                        $("#deleteDeptBtn").attr('disabled',"disabled");
                        layer.msg("部门删除成功");

                        deptId = null;
                        dataTable.ajax.reload();
                    } else {
                      layer.msg(data.message);
                    }
                }).error(function () {
                    layer.msg("系统忙,请稍候再试");
                });
            });
        });

        <!-- 添加新部门 -->
        $("#addDeptBtn").click(function () {
            layer.prompt({title: "请输入要添加的部门名称"}, function (deptName, index) {
                $.post("/dept/new.json", {deptName: deptName, pid: deptId}).done(function (data) {
                    if (data.state == "success") {
                        var treeObj = $.fn.zTree.getZTreeObj("ztree");
                        treeObj.reAsyncChildNodes(null, "refresh");
                        $("#addDeptBtn").attr("disabled", "disabled");
                        layer.msg("添加成功");

                    } else {
                        layer.msg(data.message);
                    }
                    layer.close(index);
                }).error(function () {
                    layer.msg("系统忙,请稍候再试");
                });

            });
        });

        <!-- 删除账号 -->
        $(document).delegate(".delEmployeeLink","click",function () {
            var id = $(this).attr("rel");
            layer.confirm("确定要删除该账号么?",function (index) {
                layer.close(index);
                $.get("/employee/" + id + "/delete").done(function (data) {
                    if(data.state = "success") {
                        dataTable.ajax.reload();
                        layer.msg("删除成功");
                    } else {
                        layer.msg(data.message);
                    }
                }).error(function () {
                    layer.msg("系统忙,请稍候再试")
                });
            });

        });


        <!-- 添加新账号 -->
        $("#addEmployeeBtn").click(function () {
            $("#deptDiv").html("");
            $.post("/dept/list.json").done(function (data) {
                for (var i = 0; i < data.length; i++) {
                    if (data[i].id == "1") {
                        continue;
                    }
                    var html = '<label class="checkbox-inline"><input type="checkbox" name="deptId" value="' + data[i].id + '">' + data[i].deptName +'</label>';
                    $("#deptDiv").append(html);
                }

            }).error(function () {
                layer.msg("系统忙,请稍候再试")
            });

            $("#addEmployeeModal").modal({
                show:true,
                backdrop:'static'
            });
        });

        $("#saveEmployeeBtn").click(function () {
           $("#addEmployeeForm").submit();
        });

        <!-- 验证帐号信息-->
        $("#addEmployeeForm").validate({
            errorClass : "text-danger",
            errorElement : "span",
            rules : {
                userName : {
                    required : true
                },
                mobile : {
                    required : true,
                    digits : true
                },
                password : {
                    required : true
                },
                deptId : {
                    required : true
                }
            },
            messages : {
                userName : {
                    required : "请输入姓名"
                },
                mobile : {
                    required : "请输入手机号",
                    digits : "手机号码只能是数字"
                },
                password : {
                    required : "请输入密码"
                },
                deptId : {
                    required : "请选择所在部门"
                }
            },
            submitHandler:function(){
                $.post("/employee/new",$("#addEmployeeForm").serialize()).done(function (data) {
                    if(data.state == "success") {
                        dataTable.ajax.reload();
                        $("#addEmployeeModal").modal('hide');
                        layer.msg("添加成功");

                        $("#userName").val("");
                        $("#mobile").val("");
                    } else{
                        layer.msg(data.message);
                    }
                }).error(function () {
                    layer.msg("系统忙,请稍后再试");
                });
            }
        });

    });
</script>
</body>
</html>

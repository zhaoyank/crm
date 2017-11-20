<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-我的客户</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">

    <%@include file="../include/css.jsp" %>

    <style>
        .name-avatar {
            display: inline-block;
            width: 50px;
            height: 50px;
            background-color: #ccc;
            border-radius: 50%;
            text-align: center;
            line-height: 50px;
            font-size: 24px;
            color: #FFF;
        }

        .table > tbody > tr:hover {
            cursor: pointer;
        }

        .table > tbody > tr > td {
            vertical-align: middle;
        }

        .star {
            font-size: 20px;
            color: #ff7400;
        }
        .pink {
            background-color : #ffcfe2;
        }
    </style>

</head>
<body class="hold-transition skin-blue sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">

    <!-- 顶部导航栏部分 -->
    <%@include file="../include/header.jsp" %>

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

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">我的客户</h3>
                    <div class="box-tools pull-right">
                        <button class="btn btn-success btn-sm" id="addCustomerBtn"><i class="fa fa-plus"></i> 新增客户</button>
                        <div class="btn-group">
                            <button type="button" class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i class="fa fa-file-excel-o"></i> 导出Excel <span class="caret"></span>
                            </button>
                            <ul class="dropdown-menu">
                                <li><a href="/customer/my/export.xls">导出为xls文件</a></li>
                                <li><a href="/customer/my/export.csv">导出为csv文件</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="box-header">
                    <div id="searchForm" class="form-inline">
                        <div class="form-group">
                            <input type="text" id="searchInput" name="keys" class="form-control" placeholder="根据姓名查询">
                        </div>
                        <div class="btn btn-primary"><i class="fa fa-search"></i></div>
                    </div>
                </div>
                <div class="box-body no-padding">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th width="80"></th>
                                <th>姓名</th>
                                <th>职位</th>
                                <th>跟进时间</th>
                                <th>级别</th>
                                <th>联系方式</th>
                            </tr>
                        </thead>
                        <tbody id="custTable">
                        </tbody>
                    </table>
                    <%--<ul id="pagination" class="pagination pull-right"></ul>--%>
                </div>


                <!-- /.box-body -->
            </div>
            <!-- /.box -->
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

    <!-- 底部 -->
    <%@include file="../include/footer.jsp" %>


    <!-- 模态框 -->

    <!-- Modal -->
    <div class="modal fade" id="addCustomerModal">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">添加客户</h4>
                </div>
                <div class="modal-body"  style="height: 400px;overflow:scroll">
                    <form method="post" id="addCustomerForm">
                        <div class="form-group">
                            <label>姓名</label>
                            <input type="text" id="custName" name="custName" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>性别</label>
                            <div>
                                <label class="radio-inline">
                                    <input type="radio" name="sex" checked value="男"> 男
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" name="sex" value="女"> 女
                                </label>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>职位</label>
                            <input type="text" id="job" name="job" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>手机号码</label>
                            <input type="text" id="mobile" name="mobile" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>地址</label>
                            <input type="text" id="address" name="address" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>所属行业</label>
                            <select name="trade" id="trade" class="form-control">
                                <option value="">--请选择--</option>
                                <c:forEach items="${tradeList}" var="trade">
                                    <option value="${trade.tradeName}">${trade.tradeName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>客户来源</label>
                            <select name="source" id="source" class="form-control">
                                <option value="">--请选择--</option>
                                <c:forEach items="${sourceList}" var="source">
                                    <option value="${source.sourceName}">${source.sourceName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>级别</label>
                            <select name="level" id="level" class="form-control">
                                <option value="">--请选择--</option>
                                <option value="★">★</option>
                                <option value="★★">★★</option>
                                <option value="★★★">★★★</option>
                                <option value="★★★★">★★★★</option>
                                <option value="★★★★★">★★★★★</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>备注</label>
                            <input type="text" id="mark" name="mark" class="form-control">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="button" class="btn btn-primary" id="saveCustomerBtn">保存</button>
                    <input type="reset" name="reset" style="display: none;"/>
                </div>
            </div>
        </div>
    </div>
    <!--  /模态框-->


</div>
<!-- ./wrapper -->

<%@include file="../include/js.jsp" %>
<script src="/static/plugins/validate/jquery.validate.min.js"></script>
<script src="/static/dist/js/jquery.twbsPagination.min.js"></script>
<script src="/static/plugins/moment/moment.js"></script>
<script>
    $(function () {

        var keys;

        $("#pagination").twbsPagination({
            totalPages: 5,
            visiblePages: 5,
            href: "?p={{number}}",
            first: "首页",
            prev: "上一页",
            next: "下一页",
            last: "末页"
        });

        $(document).delegate(".dataRow","click",function () {
            var id = $(this).attr("rel");
            console.log(id);
            window.location.href = "/customer/my/" + id;
        });


        <!-- 根据姓名搜索 -->
        $("#searchInput").keyup(function () {
            keys = $(this).val();
            getCustomerList();
        });

        <!-- 将获得的json中列表放入table中的函数 -->
        var showRow = function(list) {
            for (var i = 0; i < list.length; i++) {
                var obj = list[i];
                var html = '<tr class="dataRow" rel="'+obj.id+'"><td><span class="name-avatar '+(obj.sex == '女'? 'pink': '')+'">' + obj.custName.substring(0, 1) + '</span></td><td>' + obj.custName + '</td><td>' + obj.job + '</td><td>' + moment(obj.lastContactTime).format("YYYY-MM-DD HH:mm") + '</td><td class="star">' + obj.level + '</td><td><i class="fa fa-phone"></i> ' + obj.mobile + '<br></td></tr>';
                $("#custTable").append(html);
            }
        }

        <!-- 根据条件异步获得客户数据的函数 -->
        var getCustomerList = function () {
            $("#custTable").html("");
            $.get("/customer/list.json",{"_":(new Date()).valueOf(),"keys": keys}).done(function (data) {
                showRow(data.list);
            }).error(function () {
                layer.msg("系统异常,请稍候再试")
            });
        };

        <!-- 页面加载完成后获得客户列表 -->
        getCustomerList();

        <!-- 新增客户 -->
        $("#addCustomerBtn").click(function () {
            $("#addCustomerModal").modal({
                show: true,
                backdrop: 'static'
            });
        });
        $("#saveCustomerBtn").click(function () {
            $("#addCustomerForm").submit();
        });
        $("#addCustomerForm").validate({
            errorClass: "text-danger",
            errorElement: "span",
            rules: {
                custName: {
                    required: true
                },
                mobile: {
                    required: true,
                    digits: true
                }
            },
            messages: {
                custName: {
                    required: "请输入客户姓名"
                },
                mobile: {
                    required: "请输入手机号码",
                    digits: "格式错误(手机号码只能是数字)"
                }
            },
            submitHandler: function () {
                $.post("/customer/new", $("#addCustomerForm").serialize()).done(function (data) {
                    if (data.state == "success") {
                        $("#custTable").html("");
                        $("#addCustomerModal").modal('hide');
                        getCustomerList();
                        layer.msg("新增客户成功");
                        $("input[type=reset]").trigger("click");
                    } else {
                        layer.msg("添加失败");
                    }
                }).error(function () {
                    layer.msg("系统异常,请稍候再试");
                });
            }
        });

    });


</script>

</body>
</html>

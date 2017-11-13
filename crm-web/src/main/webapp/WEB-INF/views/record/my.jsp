<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
        <jsp:param name="menu" value="sales"/>
    </jsp:include>

    <!-- =============================================== -->

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">

        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">我的销售机会</h3>

                    <div class="box-tools pull-right">
                        <button id="addChanceBtn" class="btn btn-box-tool">
                            <i class="fa fa-plus"></i> 添加机会
                        </button>
                    </div>
                </div>
                <div class="box-body">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>机会名称</th>
                                <th>关联客户</th>
                                <th>机会价值</th>
                                <th>当前进度</th>
                                <th>最后跟进时间</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:if test="${sales.size() == 0}">
                                <tr><td colspan="5">暂无销售机会</td></tr>
                            </c:if>

                            <c:forEach items="${sales}" var="sale">
                                <tr class="dataTable" rel="${sale.id}">
                                    <td>${sale.name}</td>
                                    <td>${sale.custName}</td>
                                    <td><fmt:formatNumber value="${sale.worth}"/> </td>
                                    <td>${sale.progress}</td>
                                    <td><fmt:formatDate value="${sale.lastTime}" type="both"/></td>
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

    <!-- 模态框 -->
    <div class="modal fade" id="addSaleChangeModal">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">添加销售机会</h4>
                </div>
                <div class="modal-body">
                    <form method="post" id="addChanceForm">
                        <div class="form-group">
                            <label>机会名称</label>
                            <input type="text" name="name" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>关联客户</label>
                            <select name="custId" class="form-control">
                                <option value="">--请选择--</option>
                                <c:forEach items="${customers}" var="customer">
                                    <option value="${customer.id}">${customer.custName}(${customer.mobile})</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>机会价值</label>
                            <input type="text" name="worth" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>当前进度</label>
                            <select name="progress" id="progress" class="form-control">
                                <option value="初访">初访</option>
                                <option value="意向">意向</option>
                                <option value="报价">报价</option>
                                <option value="成交">成交</option>
                                <option value="暂时搁置">暂时搁置</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>详细内容</label>
                            <textarea name="content" class="form-control"></textarea>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-default" data-dismiss="modal">取消</button>
                    <button class="btn btn-primary" id="saveChanceBtn">确定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
    <!-- /模态框 -->

</div>
<!-- ./wrapper -->

<%@include file="../include/js.jsp"%>

<script>

    $(function () {
        $("#addChanceBtn").click(function () {
            $("#addSaleChangeModal").modal({
                show : true,
                backdrop: 'static'
            });
        });
    });

    $("#saveChanceBtn").click(function () {
        $("#addChanceForm").submit();
    });

    $(".dataTable").click(function () {
        var id = $(this).attr("rel");
        window.location.href = "/sales/my/" + id;
    });

</script>

</body>
</html>

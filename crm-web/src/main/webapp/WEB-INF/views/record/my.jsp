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
                                <tr class="dataRow" rel="${sale.id}">
                                    <td>${sale.name}</td>
                                    <td>${sale.custName}</td>
                                    <td>￥ <fmt:formatNumber value="${sale.worth}"/> </td>
                                    <c:choose>
                                        <c:when test="${sale.progress == '成交'}">
                                            <td><span class="text-success"><i class="fa fa-check"></i>${sale.progress}</span></td>
                                        </c:when>
                                        <c:when test="${sale.progress == '暂时搁置'}">
                                            <td><span class="text-danger"><i class="fa fa-close"></i>${sale.progress}</span></td>
                                        </c:when>
                                        <c:otherwise>
                                            <td>${sale.progress}</td>
                                        </c:otherwise>
                                    </c:choose>
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
                            <input type="text" id="worthInput" name="worth" class="form-control">
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
<script src="/static/plugins/validate/jquery.validate.min.js"></script>
<script>
    $(function () {

        var num_cn = ['零','壹','贰','叁','肆','伍','陆','柒','捌','玖'];
        var digits_cn = ['','十','百','千','万'];

        $("#worthInput").keyup(function () {
            list = [];
            var worth = $(this).val();
            var array = tranWorth(worth);
            var str = '';
            for(var i = 0; i < array.length; i ++) {
                str += array[i] ;
            }
            console.log(str);
        });

        var list = [];
        var tranWorth = function (number) {
            var n = number;
            for(var i = 0; i < number.length; i++) {
                var yu = n % 10;
                if(n > 10) {
                    n = parseInt(n / 10);
                    list.unshift(num_cn[yu]);
                    tranWorth(n);
                } else {
                    list.unshift(num_cn[n]);
                    return list;
                }
            }

        }


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

        $("#addChanceForm").validate({
            errorClass : "text-danger",
            errorElement : "span",
            rules : {
                name : {
                    required : true
                },
                custId : {
                    required : true
                },
                worth : {
                    required : true,
                    number : true,
                    min : 1
                }
            },
            messages : {
                name : {
                    required : "请输入销售机会名称"
                },
                custId : {
                    required : "请选择客户"
                },
                worth : {
                    required : "请输入销售机会价值",
                    number : "销售价值只能是有效数字",
                    min : "销售价值只能是有效数字"
                }
            }
        });



        $(".dataRow").click(function () {
            var id = $(this).attr("rel");
            window.location.href = "/sales/my/" + id;
        });
    });

</script>

</body>
</html>

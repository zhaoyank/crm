<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>凯盛软件CRM-客户报表</title>
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
        <jsp:param name="menu" value="charts_customer"/>
    </jsp:include>

    <!-- =============================================== -->

    <!-- 右侧内容部分 -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                客户报表
            </h1>
        </section>

        <!-- Main content -->
        <section class="content">

            <!-- Default box -->
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">Title</h3>
                    <div class="box-tools pull-right">
                        <button type="button" class="btn btn-box-tool" data-widget="collapse" data-toggle="tooltip" title="Collapse">
                            <i class="fa fa-minus"></i>
                        </button>
                    </div>
                </div>
                <div class="box-body">
                    <div id="level" style="width: 500px;height:350px;margin: auto"></div>
                </div>

                <!-- /.box-body -->
                <div class="box">
                    <div class="box-header with-border">
                        <h3 class="box-title">Title</h3>
                        <div class="box-tools pull-right">
                            <button type="button" class="btn btn-box-tool" data-widget="collapse" data-toggle="tooltip" title="Collapse">
                                <i class="fa fa-minus"></i>
                            </button>
                        </div>
                    </div>
                    <div class="box-body">
                        <div id="progress" style="width: 550px;height:350px;margin: auto"></div>
                    </div>
                </div>

                <div class="box">
                    <div class="box-header with-border">
                        <h3 class="box-title">Title</h3>
                        <div class="box-tools pull-right">
                            <button type="button" class="btn btn-box-tool" data-widget="collapse" data-toggle="tooltip" title="Collapse">
                                <i class="fa fa-minus"></i>
                            </button>
                        </div>
                    </div>
                    <div class="box-body">
                        <div id="source" style="width: 550px;height:350px;margin: auto"></div>
                    </div>
                </div>

                <div class="box">
                    <div class="box-header with-border">
                        <h3 class="box-title">Title</h3>
                        <div class="box-tools pull-right">
                            <button type="button" class="btn btn-box-tool" data-widget="collapse" data-toggle="tooltip" title="Collapse">
                                <i class="fa fa-minus"></i>
                            </button>
                        </div>
                    </div>
                    <div class="box-body">
                        <div id="time" style="width: 550px;height:350px;margin: auto"></div>
                    </div>
                </div>
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
<script src="/static/plugins/chartjs/echarts.min.js"></script>
<script>
    $(function () {

        var timeChart = echarts.init($("#time")[0]);
        var timeOption = {
            title: {
                text: '各月份新增客户数量',
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data:['新增客户']
            },
            toolbox: {
                show: true,
                feature: {
                    dataZoom: {
                        yAxisIndex: 'none'
                    },
                    dataView: {readOnly: false},
                    magicType: {type: ['line', 'bar']},
                    restore: {},
                    saveAsImage: {}
                }
            },
            xAxis:  {
                type: 'category',
                boundaryGap: false,
                data: []
            },
            yAxis: {
                type: 'value',
                axisLabel: {
                    formatter: '{value}'
                }
            },
            series: [
                {
                    name:'新增客户',
                    type:'line',
                    data:[],
                    markPoint: {
                        data: [
                            {type: 'max', name: '最大值'},
                            {type: 'min', name: '最小值'}
                        ]
                    },
                    markLine: {
                        data: [
                            {type: 'average', name: '平均值'}
                        ]
                    }
                }
            ]
        };
        timeChart.setOption(timeOption);
        $.get("/charts/customer/time").done(function (resp) {
            if(resp.state == "success") {
                var monthArr = [];
                var countArr = [];
                for (var i = 0; i < resp.data.length; i++) {
                    var obj = resp.data[i];
                    monthArr.push(obj.name + "月");
                    countArr.push(obj.value);
                }
                timeChart.setOption({
                    xAxis : {
                        data : monthArr
                    },
                    series : [{
                        data : countArr
                    }]
                });
            } else {
                layer.msg("服务器异常,请稍候再试");
            }
        }).error(function () {
            layer.msg("加载数据异常");
        });


        var sourceChart = echarts.init($("#source")[0]);
        var sourceOption = {
            title: {
                text: '客户来源图',
                left: 'center'
            },
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                bottom: 10,
                left: 'center',
                data: []
            },
            series : [
                {
                    type: 'pie',
                    radius : '65%',
                    center: ['50%', '50%'],
                    selectedMode: 'single',
                    data:[],
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };
        sourceChart.setOption(sourceOption);
        $.get("/charts/customer/source").done(function (resp) {
            if (resp.state == "success") {
                var nameArr = [];
                for(var i = 0; i < resp.data.length; i++) {
                    var obj = resp.data[i];
                    nameArr.push(obj.name);
                }
                sourceChart.setOption({
                    legend : {
                        data : nameArr
                    },
                    series : [{
                        data : resp.data
                    }]
                });
            } else {
                layer.msg("服务器异常,请稍候再试");
            }
        }).error(function () {
            layer.msg("加载数据异常");
        });




        var levelChart = echarts.init($("#level")[0]);
        var levelOption = {
            title: {
                text: '客户等级人数表',
                left: 'center'
            },
            tooltip: {},
            legend: {
                data:['人数'],
                left: 'right'
            },
            xAxis: {
                data: []
            },
            yAxis: {},
            series: [{
                name: '人数',
                type: 'bar',
                data: []
            }]
        };
        levelChart.setOption(levelOption);

        $.get("/charts/customer/level").done(function (resp) {
            if(resp.state == "success") {
                var levelArr = [];
                var countArr = [];
                for(var i = 0; i < resp.data.length; i++) {
                    var obj = resp.data[i];
                    levelArr.push(obj.level);
                    countArr.push(obj.count);
                }
                levelChart.setOption({
                    xAxis: {
                        data: levelArr
                    },
                    series: [{
                        data: countArr
                    }]
                });
            } else {
                layer.msg("服务器异常,请稍候再试");
            }
        }).error(function () {
            layer.msg("加载数据异常");
        });

        var progressChart = echarts.init($("#progress")[0]);
        var progressOption = {
            title: {
                text: '销售进度漏斗图'
            },
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c}"
            },
            toolbox: {
                feature: {
                    dataView: {readOnly: false},
                    restore: {},
                    saveAsImage: {}
                }
            },
            legend: {
                bottom: 10,
                data: []
            },
            calculable: true,
            series: [
                {
                    name:'漏斗图',
                    type:'funnel',
                    top: 60,
                    //x2: 80,
                    bottom: 60,
                    width: '70%',
                    // height: {totalHeight} - y - y2,
                    min: 0,
                    minSize: '0%',
                    maxSize: '100%',
                    sort: 'descending',
                    gap: 2,
                    label: {
                        normal: {
                            show: true,
                            position: 'inside'
                        },
                        emphasis: {
                            textStyle: {
                                fontSize: 20
                            }
                        }
                    },
                    labelLine: {
                        normal: {
                            length: 10,
                            lineStyle: {
                                width: 1,
                                type: 'solid'
                            }
                        }
                    },
                    itemStyle: {
                        normal: {
                            borderColor: '#fff',
                            borderWidth: 1
                        }
                    },
                    data: []
                }
            ]
        };
        progressChart.setOption(progressOption);
        $.get("/charts/sale/progress").done(function (resp) {
            if(resp.state == "success") {
                var progressArr = [];
                var max = 0;
                for (var i = 0; i < resp.data.length; i++) {
                    var obj = resp.data[i];
                    progressArr.push(obj.name);
                    if (obj.value > max) {
                        max = obj.value;
                    }
                }

                progressChart.setOption({
                    legend: {
                        data: progressArr
                    },
                    series: [{
                        max: max,
                        data: resp.data
                    }]
                });
            } else {
                layer.msg("服务器异常,请稍候再试");
            }
        }).error(function () {
            layer.msg("加载数据异常");
        });

    });
</script>
</body>
</html>

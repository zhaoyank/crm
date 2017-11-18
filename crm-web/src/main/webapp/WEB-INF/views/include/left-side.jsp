<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!-- 左侧菜单栏 -->
<aside class="main-sidebar">
    <!-- sidebar: style can be found in sidebar.less -->
    <section class="sidebar">
        <!-- 菜单 -->
        <ul class="sidebar-menu">
            <li class="header">系统功能</li>
            <li class="${param.menu == "home" ? "active" : ""}"><a href="/home"><i class="fa fa-home"></i>
                <span>首页</span></a></li>
            <!-- 客户管理 -->
            <li class="treeview ${fn:startsWith(param.menu, 'customer_') ? 'active' : ''}">
                <a href="#">
                    <i class="fa fa-address-book-o"></i> <span>客户管理</span>
                    <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
                </a>
                <ul class="treeview-menu">
                    <li class="${param.menu == "customer_my" ? "active" : ""}"><a href="/customer/my"><i class="fa fa-circle-o"></i> 我的客户</a></li>
                    <li class="${param.menu == "customer_public" ? "active" : ""}"><a href="/customer/public"><i class="fa fa-circle-o"></i> 公海客户</a></li>
                </ul>
            </li>
            <!-- 工作记录 -->
            <li class="treeview ${param.menu == "sales" ? "active" : ""}">
                <a href="/sales/my">
                    <i class="fa fa-bars"></i> <span>工作记录</span>
                </a>
            </li>
            <!-- 待办事项 -->
            <li class="treeview ${param.menu == "task"? "active" : ""}">
                <a href="/task">
                    <i class="fa fa-calendar"></i> <span>待办事项</span>
                </a>
            </li>
            <!-- 统计报表 -->
            <li class="treeview">
                <a href="#">
                    <i class="fa fa-pie-chart"></i> <span>统计报表</span>
                    <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
                </a>
                <ul class="treeview-menu">
                    <li><a href="/task"><i class="fa fa-circle-o"></i> 待办列表</a></li>
                    <li><a href="/task/past"><i class="fa fa-circle-o"></i> 逾期事项</a></li>
                </ul>
            </li>


            <li class="${param.menu == "file"? "active" : ""}"><a href="/disk"><i class="fa fa-share-alt"></i> <span>公司网盘</span></a></li>
            <li class="header">系统管理</li>
            <!-- 部门员工管理 -->
            <li class="treeview ${param.menu == "employee" ? "active" : ""}">
                <a href="/employee">
                    <i class="fa fa-users"></i> <span>员工管理</span>
                </a>

            </li>
        </ul>
    </section>
    <!-- /.sidebar -->
</aside>
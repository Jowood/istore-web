<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
<script src="ui/jquery.js"></script>
<link rel="stylesheet" type="text/css" href="ui/general.css"/><%--通用样式--%>
<meta charset="utf-8"/>
    <title>exception</title> 
    <style type="text/css">
        .exceptionTitle {
            font-size: 16px;
            color: red;
            font-weight: bold;
        }

        .exceptionContent {
            font-size: 16px;
            color: red;
        }

        .u {
            display: inline-block;
            zoom: 1;
            *display: inline; /* IE < 8: fake inline-block */
            vertical-align: top;
        }

        .u-1-2 {
            width: 49.9999999%;
        } 
        #content_a {
        /*非IE的主流浏览器识别的垂直居中的方法*/
            display: table-cell;
            vertical-align: middle; /*设置水平居中*/
            text-align: center; /* 针对IE的Hack */
            *display: block;
            *font-size: 175px; /*约为高度的0.873，200*0.873 约为175*/
            *font-family: Arial; /*防止非utf-8引起的hack失效问题，如gbk编码*/

            width: 200px;
            height: 200px;
            border: 1px solid #eee;
        }

        .u-1-3 {
            width: 49.9999999%;

        }
        .u-1-3 h1 {
            font-size:large;
        }
        h1, h2, h3, h4, h5, h6 {
            font-family: Arial, 'Helvetica Neue', Helvetica, sans-serif;
            font-weight: normal;
            line-height: 1.1;
            margin-bottom: 0.5em;
        }
        p {  
            font-size:small;
            line-height: 1.1;
            margin-bottom: 1.5em;
            margin-top: 1em;
        }
    </style>
    <script type="text/javascript">
        var showException = function() {
            if ($('#exception').css("visibility") == 'visible') {
                $('#exception').css("visibility", "hidden");
            } else { 
                $('#exception').css("visibility", "visible");
            }

        }
    </script>
</head>
<body style="margin:0px">
<div>
    <div id="d1" class="u u-1-2">
        <div id="content_a">
            <img width="380" height="417" src="images/error_info.png" alt=""/>
        </div>
    </div><!-- 避免空白元素
 --><div id="d2" class="u u-1-3">
    <h1>模块没找到</h1>

    <p>对不起，模块没有找到.</p>

    <p>可能是系统环境出现问题，或是文件残缺导致该模块无法使用.请联系系统管理员！</p>
    <a href="javascript:showException()">显示详细信息</a>
    <div id="exception" style="padding:10px;visibility:hidden;">
        <c:choose>
            <c:when test="${exception.class.name=='com.framework.exception.RecordNotExist'}">
                <font class="exceptionContent">${exception.message}</font>
            </c:when>
            <c:when test="${exception.class.name=='com.framework.exception.NotLoginException'}">
                <font class="exceptionContent">${exception.message}</font>
            </c:when>
            <c:when test="${exception.class.name=='com.framework.exception.NotAccessException'}">
                <font class="exceptionContent">${exception.message}</font>
            </c:when>
            <%--
                <c:when test="${exception.class.name=='com.framework.exception.NotAnnotationException'}">
                    <font class="exceptionContent">${exception.message}</font>
                </c:when>
            --%>
            <c:otherwise>
                <font class="exceptionTitle">异常类:</font>
                <font class="exceptionContent">${exception.class}</font>
                <br>
                <font class="exceptionTitle">异常消息:</font>
                <font class="exceptionContent">${exception.message}</font>
            </c:otherwise>
        </c:choose>
    </div>
    </div>
</div> 
</body>
</html>
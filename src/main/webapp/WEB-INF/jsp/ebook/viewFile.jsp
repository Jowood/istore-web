<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/inc/tld.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en">
<head>
	<meta charset="utf-8"/>

	<title>文档管理</title>
	<link rel="stylesheet" href="themes/base/jquery.ui.all.css"/>
	<link rel="styleSheet" type="text/css" href="ui/uploadify/example/css/default.css"/><%--文件上传--%>
    <link rel="styleSheet" type="text/css" href="ui/uploadify/uploadify.css"/><%--文件上传--%>
	<script src="ui/jquery.js"></script>
	<script src="ui/jquery-ui-min.js"></script>
	<script type="text/javascript" src="ui/uploadify/swfobject.js"></script><%--文件上传--%>
	<script type="text/javascript" src="ui/uploadify/jquery.uploadify.v2.1.0.js"></script><%--文件上传--%>
	<script type="text/javascript" src="ui/uploadify.js"></script>
	<link rel="stylesheet" href="./demo/demos.css" />	
	<style>
		body { font-size: 62.5%; }
		<%--label, input { display:block; }--%>
		input.text { margin-bottom:12px; width:95%; padding: .4em; }
		fieldset { padding:0; border:0; margin-top:25px; }
		h1 { font-size: 1.2em; margin: .6em 0; }
		div#users-contain { width: 500px; margin: 20px 0; }
		div#users-contain table { margin: 1em 0; border-collapse: collapse; width: 100%; }
		div#users-contain table td, div#users-contain table th { border: 1px solid #eee; padding: .6em 10px; text-align: left; }
		.ui-dialog .ui-state-error { padding: .3em; }
		.validateTips { border: 1px solid transparent; padding: 0.3em; }
		
		/* overlay page */
#overlay .mask,
#overlay .indicator {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	text-indent: -50em;
	cursor: progress;
	z-index:10000;
}
#overlay .mask {
	
	background: #000;
	opacity:0.5;
	filter:alpha(opacity=50);
}
#overlay .indicator {
	background: url(assets/img/loading.gif) no-repeat scroll 50% 50% transparent;
}
.page {
	position: absolute;
	top: 0;
	left: 0;
	width: 418px;
	height: 498px;
	overflow: hidden;
	text-align: center;
	user-select:none;
	-moz-user-select:-moz-none; /* excluding inputs */
	-webkit-user-select:none;
}		
.transparent {
	visibility: hidden;
}
 
	</style>
	
	<script> 
	$(function() {    	
		$('#pageNum').keydown(function(e){
			if(e.keyCode==13) {
				go($('#pageNum').val());
			}
			alert(e.keyCode);
		});
		
		$( "#select-user" )
		.button()
		.click(function() { 
			$('#page').val(1);
			$('#selectForm').submit();
		}); 	
		 

	});
	function go(page) {
		$('#page').val(page);
		$('#selectName').val('${requestScope.selectName}'); 
		$('#selectForm').submit();
	}
	</script>
</head>
<body>   


<div id="users-contain" class="ui-widget">
<h1><a href="checkUserLogin.login">返回首页</a> > ${requestScope.projectName} > 
<a href="queryFile.ebook?catalogKey=${requestScope.parentKey}&name=${requestScope.parentName}">${requestScope.parentName}</a>
> ${requestScope.name } </h1>
<form name="selectForm" method="post" action="viewFile.ebook?catalogKey=${requestScope.catalogKey}&name=${requestScope.name }" id="selectForm">
	
	<fieldset>
		<div>
		<label for="selectName">文件/目录名称:</label>
		<input type="text" name="selectName" value="${requestScope.selectName}" id="selectName" style="width: 150px;" class="text ui-widget-content ui-corner-all" />
 		  
		<button id="select-user" type="button">查询</button> 
		</div>
	</fieldset>
	<input type="hidden" name="parentName" id="parentName" value="${requestScope.parentName}" />
	<input type="hidden" name="parentKey" id="parentKey" value="${requestScope.parentKey}" />
	<input type="hidden" value="${requestScope.page}" name="page" id="page"/>
	
</form>	
<table id="users" class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header ">
				<th>文件/目录名称</th> 
				<th style="width:60px;">创建时间</th>
				<th style="width:30px;">大小</th>
				<th style="width:30px;">操作</th>
			</tr>
		</thead>
		<tbody> 
			<c:forEach var="item" items="${requestScope.list}" varStatus="status">
			<tr>
				<td>${item.name}</td> 
				<td>${fn:substring(item.key ,0,8)}&nbsp;</td>
				<td>${item.size}&nbsp;</td>				
				<td> 
					<a href="downloadFile.ebook?catalogKey=${requestScope.catalogKey}&key=${item.key}&name=${item.name}" id="shelf-index-button" class="btn"   data-act="external-shelf">下载</a>
				</td>		 
			</tr>
			</c:forEach>
			<c:if test="${requestScope.pages > 1}">
			<tr>
				<td colspan="4" align="right">
				<div style="width:100%" align="right">
				    <c:if test="${requestScope.page > 1 && requestScope.page != 1}">
				    <a href="javascript:go(1)">首页</a>
				    </c:if> 
				    <c:if test="${requestScope.page > 1 && requestScope.page != 1}">
					<a href="javascript:go(${requestScope.page}-1)">上一页</a>
					</c:if> 
					第<input type="text"  value="${requestScope.page }" id="pageNum" name="pageNum" 
					onkeyup="this.value=this.value.replace(/[^\d&:]/g,'')" 
					onblur="this.value=this.value.replace(/[^\d&:]/g,'')" 
					onafterpaste="this.value=this.value.replace(/[^\d&:]/g,'')"
					style="width:30px;text-align: right;" size="20"></input>页 / 共${requestScope.pages }页
					<c:if test="${requestScope.page != requestScope.pages}">
					<a href="javascript:go(${requestScope.page}+1)">下一页</a>
					</c:if>
				    <c:if test="${requestScope.page != requestScope.pages}">
				    <a href="javascript:go(${requestScope.pages})">末页</a>
				    </c:if> 
				</div>

				</td> 
			</tr>	
			</c:if>
		
		</tbody>
	</table>
</div>


</div><!-- End demo -->
 

</body>
</html>
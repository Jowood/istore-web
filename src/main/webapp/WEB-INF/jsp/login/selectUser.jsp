<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/inc/tld.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en">
<head>
	<meta charset="utf-8"/>

	<title>用户管理</title>
	<link rel="stylesheet" href="themes/base/jquery.ui.all.css"/>
		<script src="ui/jquery.js"></script>
		<script src="ui/jquery-ui-min.js"></script>
	<link rel="stylesheet" href="./demo/demos.css" />	
	<style>
		body { font-size: 62.5%; }
		label, input { display:block; }
		input.text { margin-bottom:12px; width:95%; padding: .4em; }
		fieldset { padding:0; border:0; margin-top:25px; }
		h1 { font-size: 1.2em; margin: .6em 0; }
		div#users-contain { width: 500px; margin: 20px 0; }
		div#users-contain table { margin: 1em 0; border-collapse: collapse; width: 100%; }
		div#users-contain table td, div#users-contain table th { border: 1px solid #eee; padding: .6em 10px; text-align: left; }
		.ui-dialog .ui-state-error { padding: .3em; }
		.validateTips { border: 1px solid transparent; padding: 0.3em; }
	</style>
	<script>
	var deleteUser;
	var 		updateUser = function(loginNameVal, nameVal){
		$( "#nameUpdate" ).val(nameVal);
		$( "#loginNameUpdate" ).val(loginNameVal); 
		
		$( "#dialog-form-update" ).dialog( "open" );
	}	;
	$(function() {
		// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
		$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		var name = $( "#name" ),
		    loginName = $( "#loginName" ),
			password = $( "#password" ),
			allFields = $( [] ).add( name ).add( loginName ).add( password ),
			
			nameUpdate = $( "#nameUpdate" ),
			loginNameUpdate = $( "#loginNameUpdate" ),
			passwordUpdate = $( "#passwordUpdate" ),
			allFieldsUpdate = $( [] ).add( nameUpdate ).add( loginNameUpdate ).add( passwordUpdate ),	
			
		updateUser = function(loginNameVal, nameVal){
			$( "#nameUpdate" ).val(nameVal);
			$( "#loginNameUpdate" ).val(loginNameVal); 
			
			$( "#dialog-form-update" ).dialog( "open" );
		}	

		deleteUser = function(loginName) {
			$( "#dialog-confirm p" ).html(
					'<span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>'+
					'是否确认删除用户名为' + loginName + '的用户?');
			$( "#dialog-confirm" ).dialog({ 
				resizable: false,
				height:140,
				modal: true,
				buttons: {
					"确认": function() {
						 $.ajax({ 
							 url: "deleteUser.login"
							 , context: document.body 
						     , complete :function(r){
						    	 var obj = eval('(' + r.responseText + ')');
						    	 
						    	 if (obj.success) {
						    		 window.location.href='selectUser.login';
						    	 } else {
						    		 alert(obj.errorMsg); 
						    	 }
						     }
							 ,data :{ 
								 loginName:loginName
							 }
						     ,type:'post'
						 	 ,dataType:'json'
						 });
						 $( this ).dialog( "close" );
					},
					"取消": function() {
						$( this ).dialog( "close" );
					}
				}
			});	
		}
		function updateTips(id, t ) {
			var tips = $( id + " .validateTips" );
			tips
				.text( t )
				.addClass( "ui-state-highlight" );
			setTimeout(function() {
				tips.removeClass( "ui-state-highlight", 1500 );
			}, 500 );
		}

		function checkLength(id, o, n, min, max ) {
			if ( o.val().length > max || o.val().length < min ) {
				o.addClass( "ui-state-error" );
				updateTips(id, n + "长度必须在" + min + "到" + max + "之间！" );
				return false;
			} else {
				return true;
			}
		}

		function checkRegexp(id, o, regexp, n ) {
			if ( !( regexp.test( o.val() ) ) ) {
				o.addClass( "ui-state-error" );
				updateTips(id, n );
				return false;
			} else {
				return true;
			}
		}
		$( "#dialog-form-update" ).dialog({
			autoOpen: false,
			height: 300,
			width: 350,
			modal: true,
			buttons: {
				"确认": function() {
					var bValid = true;
					allFieldsUpdate.removeClass( "ui-state-error" );

					bValid = bValid && checkLength("#dialog-form-update", nameUpdate, "姓名", 2, 10 );
					bValid = bValid && checkLength("#dialog-form-update", loginNameUpdate, "登录名", 3, 16 );
					bValid = bValid && checkLength("#dialog-form-update", passwordUpdate, "密码", 3, 16 );

					bValid = bValid && checkRegexp("#dialog-form-update", loginNameUpdate, /^[a-z]([0-9a-z_])+$/i, "Username may consist of a-z, 0-9, underscores, begin with a letter." );
					bValid = bValid && checkRegexp("#dialog-form-update", passwordUpdate, /^([0-9a-zA-Z])+$/, "Password field only allow : a-z 0-9" );

					if ( bValid ) { 
						 $.ajax({ 
							 url: "updateUser.login" 
						     , complete :function(r){
						    	 var obj = eval('(' + r.responseText + ')');
						    	 if (obj.success) {
						    		 window.location.href='selectUser.login';
						    	 } else {
						    		 updateTips("#dialog-form-update", obj.errorMsg); 
						    	 }
						     }
							 ,data :{
								   name:nameUpdate.val()
								   ,loginName:loginNameUpdate.val()
								   ,password:passwordUpdate.val()
							 }
						     ,type:'post'
						 	 ,dataType:'json'
						 });
 
					}
				},
				"取消": function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				allFieldsUpdate.val( "" ).removeClass( "ui-state-error" );
			}
		});		
		
		$( "#dialog-form" ).dialog({
			autoOpen: false,
			height: 300,
			width: 350,
			modal: true,
			buttons: {
				"确认": function() {
					var bValid = true;
					allFields.removeClass( "ui-state-error" );

					bValid = bValid && checkLength("#dialog-form", name, "姓名", 2, 10 );
					bValid = bValid && checkLength("#dialog-form", loginName, "登录名", 3, 16 );
					bValid = bValid && checkLength("#dialog-form", password, "密码", 3, 16 );

					bValid = bValid && checkRegexp("#dialog-form", loginName, /^[a-z]([0-9a-z_])+$/i, "登录名是 a-z, 0-9中的字符，必须以字母开头,长度必须在3-16之间." );
					bValid = bValid && checkRegexp("#dialog-form", password, /^([0-9a-zA-Z])+$/, "密码必须是 a-z,0-9中的字符,长度必须在3-16之间." );

					if ( bValid ) { 
						 $.ajax({ 
							 url: "insertUser.login"
							 , context: document.body 
						     , complete :function(r){
						    	 var obj = eval('(' + r.responseText + ')');
						    	 if (obj.success) {
						    		 window.location.href='selectUser.login';
						    	 } else {
						    		 updateTips("#dialog-form", obj.errorMsg); 
						    	 }
						     }
							 ,data :{
								   name:name.val()
								   ,loginName:loginName.val()
								   ,password:password.val()
							 }
						     ,type:'post'
						 	 ,dataType:'json'
						 });
 
					}
				},
				"取消": function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				allFields.val( "" ).removeClass( "ui-state-error" );
			}
		});
		$( "#go-back" )
		.button()
		.click(function() { 
			window.location.href='checkUserLogin.login';
		});		
		$( "#select-user" )
		.button()
		.click(function() { 
			$('#page').val(1);
			$('#selectForm').submit();
		});
		$( "#rest-user" )
		.button()
		.click(function() { 
			$('#selectName').val('');
			$('#selectLoginName').val('');
		});		
		
		$( "#create-user" )
			.button()
			.click(function() { 
				$( "#dialog-form" ).dialog( "open" );
		});

	});
	function go(page) {
		$('#page').val(page);
		$('#selectName').val('${requestScope.selectName}');
		$('#selectLoginName').val('${requestScope.selectLoginName}');
		$('#selectForm').submit();
	}
	</script>
</head>
<body>

<div class="demo">
<div id="dialog-confirm" title="删除用户">
	<p></p>
</div>
<div id="dialog-form" title="创建用户">
	<p class="validateTips">表单中所有字段都必须填写.</p>

	<form id="inserForm">
	<fieldset>
		<label for="name">用户名</label>
		<input type="text" name="name" id="name" class="text ui-widget-content ui-corner-all" />
		<label for="loginName">登录名</label>
		<input type="text" name="loginName" id="loginName" value="" class="text ui-widget-content ui-corner-all" />
		<label for="password">密码</label>
		<input type="password" name="password" id="password" value="" class="text ui-widget-content ui-corner-all" />
	</fieldset>
	</form>
</div>

<div id="dialog-form-update" title="修改用户">
	<p class="validateTips">表单中所有字段都必须填写.</p>

	<form>
	<fieldset>
		<label for="nameUpdate">用户名</label>
		<input type="text" name="nameUpdate" id="nameUpdate" class="text ui-widget-content ui-corner-all" />
		<label for="loginNameUpdate">登录名</label>
		<input type="text" readonly="readonly" name="loginNameUpdate" id="loginNameUpdate" value="" class="text ui-widget-content ui-corner-all" />
		<label for="passwordUpdate">密码</label>
		<input type="password" name="passwordUpdate" id="passwordUpdate" value="" class="text ui-widget-content ui-corner-all" />
	</fieldset>
	</form>
</div>

<div id="users-contain" class="ui-widget">
<form name="selectForm" method="post" action="selectUser.login" id="selectForm">
	<fieldset>
		<label for="selectName">姓名</label>
		<input type="text" name="selectName" value="${requestScope.selectName}" id="selectName" style="width: 150px;" class="text ui-widget-content ui-corner-all" />
		<label for="selectLoginName">登录名</label>
		<input type="text" value="${requestScope.selectLoginName}" name="selectLoginName" id="selectLoginName" style="width: 150px;" value="" class="text ui-widget-content ui-corner-all" /> 
		<button id="create-user" type="button" style="float: right;">创建用户</button>
		<button id="go-back" type="button" style="float: right;">返回</button> 
		<button id="rest-user" type="button" style="float: right;">清除</button>  
		<button id="select-user" type="button" style="float: right;">查询</button> 
		
	</fieldset>
	
	<input type="hidden" value="${requestScope.page}" name="page" id="page"/>
	
</form>		
	<table id="users" class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header ">
				<th>登录名</th>
				<th width="100">用户名</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody> 
			<c:forEach var="item" items="${requestScope.list}" varStatus="status">
			<tr>
				<td>${item.loginName}</td>
				<td>${item.name}</td>
				<td> 
					<a href="javascript:updateUser('${item.loginName}','${item.name}')" id="shelf-index-button" class="btn" title="用户管理" data-act="external-shelf">修改</a>
					<a href="javascript:deleteUser('${item.loginName}')" id="shelf-index-button" class="btn" title="用户管理" data-act="external-shelf">删除</a>
				</td>
			</tr>
			</c:forEach>
			<c:if test="${requestScope.pages > 1}">
			<tr>
				<td colspan="3" align="right">
				<div style="width:100%" align="right">
				    <c:if test="${requestScope.page > 1 && requestScope.page != 1}">
				    <a href="javascript:go(1)">首页</a>
				    </c:if> 
				    <c:if test="${requestScope.page > 1 && requestScope.page != 1}">
					<a href="javascript:go(${requestScope.page}-1)">上一页</a>
					</c:if> 
					第${requestScope.page }页 / 共${requestScope.pages }页
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

<!-- End demo-description -->

</body>
</html>
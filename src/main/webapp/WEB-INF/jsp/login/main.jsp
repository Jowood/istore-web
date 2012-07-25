<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/inc/tld.jsp" %>
<!doctype html>
<html>
	<head>
		<meta charset="utf-8">
		<title>资料库</title> 
		<style type="text/css">  
		
		label, input { display:block; }
		input.text { margin-bottom:12px; width:95%; padding: .4em; }
		fieldset { padding:0; border:0; margin-top:25px; }
		h1 { font-size: 1.2em; margin: .6em 0; }
		div#users-contain { width: 350px; margin: 20px 0; }
		div#users-contain table { margin: 1em 0; border-collapse: collapse; width: 100%; }
		div#users-contain table td, div#users-contain table th { border: 1px solid #eee; padding: .6em 10px; text-align: left; }
		.ui-dialog .ui-state-error { padding: .3em; }
		.validateTips { border: 1px solid transparent; padding: 0.3em; }
		
		</style>
		<link rel="stylesheet" href="./demo/demos.css" />
		<link rel="stylesheet" href="./assets/css/reset.src.css" />
		<link rel="stylesheet" href="./assets/css/app.src.css" />
		<link rel="stylesheet" href="themes/base/jquery.ui.all.css"/>
	</head>
	<body>
		<h1>资料库</h1>
		<div id="shelf" class="page">
			<div class="header">
			<c:if test="${sessionScope.loginUser.name eq 'admin' }">
				<a href="manageProject.ebook" id="shelf-index-button" class="btn" title="文档管理" data-act="external-shelf"><s class="btn-l"></s><span>文档管理</span><s class="btn-r"></s></a>
				<a href="search.emp" id="shelf-index-button" class="btn" title="人员去向" data-act="external-shelf"><s class="btn-l"></s><span>人员去向</span><s class="btn-r"></s></a>
				<a href="selectUser.login" id="shelf-index-button" class="btn" title="用户管理" data-act="external-shelf"><s class="btn-l"></s><span>用户管理</span><s class="btn-r"></s></a>
			</c:if>
				<a href="javascript:updatePassword()" id="logout" class="btn" title="修改密码" data-act="external-shelf"><s class="btn-l"></s><span>修改密码</span><s class="btn-r"></s></a>
				<a href="logout.login" id="logout" class="btn" title="退出" data-act="external-shelf"><s class="btn-l"></s><span>退出</span><s class="btn-r"></s></a>
			</div>
			<div id="stack" style="width:100%;padding: 0 2px 0 2px;">
				<div id="stack-wrap" class="clearfix" style="width:100%;">

				
				</div>
			</div>
			<p class="pagination" style="visibility: hidden;" id="shelf-pagination"></p>
		</div>
		 
		<div id="overlay" class="page transparent">
			<div class="mask">遮罩层</div>
			<div class="indicator">正在读取中...</div>
		</div> 
		<div id="dialog-form" title="修改密码">
			<p class="validateTips"></p>
		
			<form>
			<fieldset>
				<label for="password">新密码</label>
				<input type="password" name="password" id="password" value="" class="text ui-widget-content ui-corner-all" />
				<label for="passConfim">确认密码</label>
				<input type="password" name="passConfim" id="passConfim" value="" class="text ui-widget-content ui-corner-all" />				
			</fieldset>
			</form>
		</div>
		<script src="ui/jquery.js"></script>
		<script src="ui/jquery-ui-min.js"></script>
		
		<script src="./assets/js/global.src.js"></script>
		<script src="./assets/js/app.src.js"></script>
		<script> 
		    function updatePassword(){
		    	$( "#dialog-form" ).dialog( "open" );
		    }
			$(function(){
				var password = $( "#password" ),
				passConfim = $( "#passConfim" )
				allFields = $( [] ).add(passConfim).add( password );
				function checkLength(id, o, n, min, max ) {
					if ( o.val().length > max || o.val().length < min ) {
						o.addClass( "ui-state-error" );
						updateTips(id, n + "长度必须在" + min + "到" + max + "之间！" );
						return false;
					} else {
						return true;
					}
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
				function chkPasswordconfirm(){
					var p = $( "#password" ).val();
					var passwordconfirm = $("#passConfim").val();
					if(passwordconfirm==''){
					$("#passConfim").addClass("ui-state-error");
					updateTips("#dialog-form","请再次输入密码");
					return false;
					}
					if(p != passwordconfirm){
					updateTips("#dialog-form","两次输入的密码不一致");
					return false;
					}else { 
					return true;
					}
				} 
				$( "#dialog-form" ).dialog({
					autoOpen: false,
					height: 300,
					width: 350,
					modal: true,
					buttons: {
						"提交": function() { 
							var bValid = true;
							allFields.removeClass( "ui-state-error" );
							bValid = bValid && checkLength("#dialog-form", password, "密码", 3, 16 ); 
							if(bValid) {
								bValid = bValid && chkPasswordconfirm();
								if(bValid) {
									var win = $( this );
									 $.ajax({ 
										 url: "updateUser.login" 
									     , complete :function(r){
									    	 var obj = eval('(' + r.responseText + ')');
									    	 if (obj.success) { 
									    		 
									    		 alert("修改成功");
									    	 } else {
									    		 updateTips("#dialog-form", obj.errorMsg); 
									    	 }
									     }
										 ,data :{
											   loginName:'${sessionScope.loginUser.name}'
											   ,password:password.val()
										 }
									     ,type:'post'
									 	 ,dataType:'json'
									 });
									 win.dialog( "close" );
								}
							}
							
						},
						"取消": function() {
							$( this ).dialog( "close" );
						}
					},
					close: function() { 
						var tips = $("#dialog-form .validateTips" );
						tips.text('');
						allFields.val( "" ).removeClass( "ui-state-error" );
					}
				});
				
				
				$('#shelf').width($(window).width());
				$('#shelf').height($(window).height()+20);
				//$('#book').width($(window).width());
				//$('#stack').height(1600);		
				$('#overlay').width($(window).width());
				$('#overlay').height($(window).height());
 			
				// 初始化   
 				DEMO.EBOOK.init({

					'NAME' : '我的电子书架'
					
				});   
			});
		</script>
	</body>
</html>

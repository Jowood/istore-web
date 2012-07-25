<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/inc/tld.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en">
<head>
	<meta charset="utf-8"/>

	<title>文档管理</title>
	<link rel="stylesheet" href="themes/base/jquery.ui.all.css"/>
		<script src="ui/jquery.js"></script>
		<script src="ui/jquery-ui-min.js"></script>
	<link rel="stylesheet" href="./demo/demos.css" />	
	<style>
		body { font-size: 62.5%; }  
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
	var deleteCatalog;
	var 		updateCatalog = function(key, nameVal){
		$( "#nameUpdate" ).val(nameVal);
		$( "#key" ).val(key); 
		
		$( "#dialog-form-update" ).dialog( "open" );
	}	;	
	$(function() {
		// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
		$('#overlay').width($(window).width());
		$('#overlay').height($(window).height());
		$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
		var name = $( "#name" ), 
			allFields = $( [] ).add( name ),
			
			nameUpdate = $( "#nameUpdate" ), 
			allFieldsUpdate = $( [] ).add( nameUpdate ),				
			
			tips = $( ".validateTips" );

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
		
		deleteCatalog = function(key, name) {
			$( "#dialog-confirm p" ).html(
					'<span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>'+
					'是否确认删除项目名为' + name + '的项目?');
			$( "#dialog-confirm" ).dialog({ 
				resizable: false,
				height:140,
				modal: true,
				buttons: {
					"确认": function() {
						 $.ajax({ 
							 url: "deleteProject.ebook"
							 , context: document.body 
						     , complete :function(r){
						    	 var obj = eval('(' + r.responseText + ')');
						    	 $( this ).dialog( "close" );
						    	 if (obj.success) {
						    		 window.location.href='manageProject.ebook';
						    	 } else {
						    		 alert(obj.errorMsg); 
						    	 }
						     }
							 ,data :{ 
								 key:key
							 }
						     ,type:'post'
						 	 ,dataType:'json'
						 });
						 $( this ).dialog( "close" );
						 $('#overlay').removeClass('transparent');
						 
					},
					"取消": function() {
						$( this ).dialog( "close" );
					}
				}
			});	
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

					bValid = bValid && checkLength("#dialog-form-update", nameUpdate, "项目名称", 3, 6 );
					
					if ( bValid ) { 
						 $.ajax({ 
							 url: "updateProject.ebook" 
						     , complete :function(r){
						    	 var obj = eval('(' + r.responseText + ')');
						    	 if (obj.success) {
						    		 window.location.href='manageProject.ebook';
						    	 } else {
						    		 updateTips("#dialog-form-update", obj.errorMsg); 
						    	 }
						     }
							 ,data :{
								   name:nameUpdate.val()
								   ,key:$( "#key" ).val() 
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

					bValid = bValid && checkLength("#dialog-form", name, "项目名称", 3, 6 );
					if ( bValid ) { 
						 $.ajax({ 
							 url: "createProject.ebook" 
						     , complete :function(r){
						    	 var obj = eval('(' + r.responseText + ')');
						    	 if (obj.success) {
						    		 window.location.href='manageProject.ebook';
						    	 } else {
						    		 updateTips("#dialog-form", obj.errorMsg); 
						    	 }
						     }
							 ,data :{
								   name:name.val() 
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
 	
		$( "#select-user" )
		.button()
		.click(function() { 
			$('#page').val(1);
			$('#selectForm').submit();
		}); 	
		
		$( "#create-user" )
			.button()
			.click(function() { 
				$( "#dialog-form" ).dialog( "open" );
		});
	});
	</script>
</head>
<body>
<div id="overlay" class="page transparent">
	<div class="mask">遮罩层</div>
	<div class="indicator"> </div>
</div>  
<div class="demo">
<div id="dialog-confirm" title="删除项目">
	<p></p>
</div>
<div id="dialog-form" title="创建项目">
	<p class="validateTips">请填写项目名称.</p>

	<form>
	<fieldset>
		<label for="name">项目名称</label>
		<input type="text" name="name" id="name" class="text ui-widget-content ui-corner-all" /> 
	</fieldset>
	</form>
</div>

<div id="dialog-form-update" title="修改项目">
	<p class="validateTips">请填写项目名称.</p>

	<form>
	<fieldset>
		<label for="name">项目名称</label>
		<input type="text" name="nameUpdate" id="nameUpdate" class="text ui-widget-content ui-corner-all" /> 
		<input type="hidden" name="key" id="key"/>
	</fieldset>
	</form>
</div>


<div id="users-contain" class="ui-widget">
<h1><a href="checkUserLogin.login">返回目录</a></h1>
<button id="create-user" type="button" style="float: right;">创建项目</button>   
<table id="users" class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header ">
				<th>项目名称</th> 
				<th>操作</th>
			</tr>
		</thead>
		<tbody> 
			<c:forEach var="item" items="${requestScope.list}" varStatus="status">
			<tr>
				<td><a href="manageCatalog.ebook?selectProjectKey=${item.key}&projectName=${item.name}">${item.name}</a></td> 
				<td> 
					<a href="javascript:updateCatalog('${item.key}','${item.name}')" id="shelf-index-button" class="btn"   data-act="external-shelf">修改</a>
					<a href="javascript:deleteCatalog('${item.key}','${item.name}')" id="shelf-index-button" class="btn"   data-act="external-shelf">删除</a>
				</td>
			</tr>
			</c:forEach>  
		</tbody>
	</table>
</div>
 

</div><!-- End demo -->
 

</body>
</html>
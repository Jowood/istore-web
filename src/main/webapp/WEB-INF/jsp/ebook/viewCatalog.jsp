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
		
		$( "#catalog-form-update" ).dialog( "open" );
	}	;	
	$(function() {
		$('#pageNum').keydown(function(e){
			if(e.keyCode==13) {
				go($('#pageNum').val());
			}
		});
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
		$( "#catalog-confirm p" ).html(
				'<span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>'+
				'是否确认删除目录名为' + name + '的目录?');
		$( "#catalog-confirm" ).dialog({ 
			resizable: false,
			height:140,
			modal: true,
			buttons: {
				"确认": function() {
					 $.ajax({ 
						 url: "deleteCatalog.ebook"
						 , context: document.body 
					     , complete :function(r){
					    	 var obj = eval('(' + r.responseText + ')');
					    	 
					    	 if (obj.success) {
					    		 window.location.reload();
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
		
		
		$("#uploadify").myUpLoadify({
			script:'uploadHandler.ebook?catalogKey=${requestScope.catalogKey}'
			,_delUrl:'delLocalFile.ebook?catalogKey=${requestScope.catalogKey}'		
		});		 
		deleteFile = function(key, name) {
			$( "#dialog-confirm p" ).html(
					'<span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>'+
					'是否确认删除文件名为' + name + '的文件?');
			$( "#dialog-confirm" ).dialog({ 
				resizable: false,
				height:140,
				modal: true,
				buttons: {
					"确认": function() {
						 $.ajax({ 
							 url: "delFile.ebook?catalogKey=${requestScope.catalogKey}"
							 , context: document.body 
						     , complete :function(r){ 
						    	 window.location.reload();
						     }
							 ,data :{ 
								 key:key
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
		
		
		
		
		$( "#dialog-form" ).dialog({
			autoOpen: false,
			height: 300,
			width: 350,
			modal: true,
			buttons: {
				"确认": function() {
					if($('#sf_ids').val() == '') {
						alert("请选择文件上传！");
						return;
					}
					 $.ajax({ 
						 url: "uploadFiles.ebook?catalogKey=${requestScope.catalogKey}"
						 , context: document.body 
					     , complete :function(r){
					    	 var obj = eval('(' + r.responseText + ')');
					    	 $('#overlay').addClass('transparent');
					    	 if (!obj.success) {
					    		 alert(obj.errorMsg); 
					    	 } else {
					    		 alert('上传成功！');
					    		 window.location.reload();
					    	 }
					    	 
					     }
						 ,data :{ 
							 keys:$('#sf_ids').val()
						 	 ,names:$('#sf_names').val()
						 }
					     ,type:'post'
					 	 ,dataType:'json'
					 });
					 $( this ).dialog( "close" );	
					 $('#overlay').removeClass('transparent');
					 		
 				},
				"取消": function() {
					$('#files span').remove();
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				$('#files span').remove();
			}
		});

		$( "#catalog-form-update" ).dialog({
			autoOpen: false,
			height: 300,
			width: 350,
			modal: true,
			buttons: {
				"确认": function() {
					var bValid = true;
					allFieldsUpdate.removeClass( "ui-state-error" );

					bValid = bValid && checkLength("#dialog-form-update", nameUpdate, "目录名称", 2, 8 );
					
					if ( bValid ) { 
						 $.ajax({ 
							 url: "updateCatalog.ebook?projectKey=${requestScope.selectProjectKey }" 
						     , complete :function(r){
						    	 var obj = eval('(' + r.responseText + ')');
						    	 if (obj.success) {
						    		 window.location.reload();
						    	 } else {
						    		 updateTips("#catalog-form-update", obj.errorMsg); 
						    	 }
						     }
							 ,data :{
								   name:nameUpdate.val()
								   ,parent:'${requestScope.catalogKey }'
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
		
		$( "#catalog-form" ).dialog({
			autoOpen: false,
			height: 300,
			width: 350,
			modal: true,
			buttons: {
				"确认": function() {
					var bValid = true;
					allFields.removeClass( "ui-state-error" );

					bValid = bValid && checkLength("#dialog-form", name, "目录名称", 2, 8 );
					if ( bValid ) { 
						 $.ajax({ 
							 url: "createCatalog.ebook?projectKey=${requestScope.selectProjectKey }"
							 , context: document.body 
						     , complete :function(r){
						    	 var obj = eval('(' + r.responseText + ')');
						    	 if (obj.success) {
						    		 window.location.reload();
						    	 } else {
						    		 updateTips("#catalog-form", obj.errorMsg); 
						    	 }
						     }
							 ,data :{
								   name:name.val() 
								   ,parent:'${requestScope.catalogKey }'
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
				$('#files span').remove();
				$( "#dialog-form" ).dialog( "open" );
				

		});
		$( "#create-catalog" )
			.button()
			.click(function() {  
				$( "#catalog-form" ).dialog( "open" );
				
	
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
<div id="overlay" class="page transparent">
	<div class="mask">遮罩层</div>
	<div class="indicator"> </div>
</div>  
<div class="demo">
<div id="dialog-confirm" title="删除文件">
	<p></p>
</div>
<div id="catalog-confirm" title="删除目录">
	<p></p>
</div>
<div id="dialog-form" title="上传文件">
	<p class="validateTips"> </p>

	<form>
	<fieldset>
		<label for="file">上传文件</label>
		<div id="files">&nbsp;</div>
		<input type="file" style="width:0px" id="uploadify"/>
	</fieldset>
	</form>
</div> 
<div id="catalog-form" title="创建目录">
	<p class="validateTips">请填写目录名称.</p>

	<form>
	<fieldset>
		<label for="name">目录名称</label>
		<input type="text" name="name" id="name" class="text ui-widget-content ui-corner-all" /> 
	</fieldset>
	</form>
</div>

<div id="catalog-form-update" title="修改目录">
	<p class="validateTips">请填写目录名称.</p> 
	<form>
	<fieldset>
		<label for="name">目录名称</label>
		<input type="text" name="nameUpdate" id="nameUpdate" class="text ui-widget-content ui-corner-all" /> 
		<input type="hidden" name="key" id="key"/>
	</fieldset>
	</form>
</div>

<div id="users-contain" class="ui-widget">
<h1><a href="checkUserLogin.login">返回目录</a> > <a href="manageProject.ebook">项目</a> > <a href="manageCatalog.ebook?projectName=${requestScope.projectName }&selectProjectKey=${requestScope.selectProjectKey }">${requestScope.projectName }</a> > ${requestScope.name } </h1>
<form name="selectForm" method="post" action="viewCatalog.ebook?catalogKey=${requestScope.catalogKey}&projectName=${requestScope.projectName }&selectProjectKey=${requestScope.selectProjectKey }&name=${requestScope.name }" id="selectForm">
	<fieldset>
		<label for="selectName">文件/目录名称</label>
		<input type="text" name="selectName" value="${requestScope.selectName}" id="selectName" style="width: 150px;" class="text ui-widget-content ui-corner-all" />
 		<button id="select-user" type="button" >查询</button> 
 		<button id="create-user" type="button" >上传文件</button>  
		<button id="create-catalog" type="button"  >创建目录</button> 
		
	</fieldset>
	
	<input type="hidden" value="${requestScope.page}" name="page" id="page"/>
	
</form>	
<table id="users" class="ui-widget ui-widget-content">
		<thead>
			<tr class="ui-widget-header ">
				<th>文件/目录名称</th> 
				<th style="width:60px;">创建时间</th>
				<th style="width:30px;">大小</th>
				<th style="width:60px;">操作</th>
			</tr>
		</thead>
		<tbody> 
			<c:forEach var="item" items="${requestScope.list}" varStatus="status">
			<tr>
			<c:if test="${item.parent eq requestScope.catalogKey }">
			<td><a href="viewChildren.ebook?projectName=${requestScope.projectName }&selectProjectKey=${requestScope.selectProjectKey }&parentKey=${requestScope.catalogKey }&parentName=${requestScope.name }&catalogKey=${item.key}&name=${item.name}">${item.name}</a></td> 
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td> 
				<a href="javascript:updateCatalog('${item.key}','${item.name}')" id="shelf-index-button" class="btn"   data-act="external-shelf">修改</a>
				<a href="javascript:deleteCatalog('${item.key}','${item.name}')" id="shelf-index-button" class="btn"   data-act="external-shelf">删除</a>
			</td>
			</c:if>
			<c:if test="${item.parent eq null }">
			<td>${item.name}</td> 
			<td>${fn:substring(item.key ,0,8)}&nbsp;</td>
			<td>${item.size}&nbsp;</td>
			<td> 
				<a href="downloadFile.ebook?catalogKey=${requestScope.catalogKey}&key=${item.key}&name=${item.name}" id="shelf-index-button" class="btn"   data-act="external-shelf">下载</a>
				<a href="javascript:deleteFile('${item.key}','${item.name}')" id="shelf-index-button" class="btn"   data-act="external-shelf">删除</a>
			</td>			
			</c:if>

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
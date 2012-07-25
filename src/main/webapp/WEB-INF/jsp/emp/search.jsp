<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
	<title>人员去向</title>
	<link rel="stylesheet" type="text/css" href="ext/resources/css/ext-all.css" />
	<!-- GC -->
 	<!-- LIBS -->
 	<script src="ui/jquery.js"></script>
 	<script type="text/javascript" src="ext/adapter/ext/ext-base.js"></script>
 	<!-- ENDLIBS -->

     <script type="text/javascript" src="ext/ext-all.js"></script>     
     <script type="text/javascript" src="ext/ext-lang-zh_CN.js"></script>  
     <link rel="stylesheet" type="text/css" href="ext/resources/css/grid-examples.css" />  
     <link rel="stylesheet" type="text/css" href="ext/resources/css/examples.css" />

    <style type="text/css">
    h1 {
	    font-size: 13px;
	    height:16px;
	    font-family: Verdana,Arial,sans-serif; 
	}
	.add {
	    background-image: url("images/add.gif") !important;
	} 
	.remove {
	    background-image: url("images/delete.gif") !important;
	}
	.save {
	    background-image: url("images/save.gif") !important;
	}
	.search {
	    background-image: url("images/zoom_in.png") !important;
	}
    </style> 
    <script type="text/javascript">
    function formatDate(value){
    	value = value||'';
    	if (Ext.isDate(value)) {
    		return value.dateFormat('Y-m-d');
    	}
        return value;
    }
    Ext.onReady(function(){

        /**
         * Handler specified for the 'Available' column renderer
         * @param {Object} value
         */ 
         <c:if test="${requestScope.lock != 'true' }">
         var urlRefresh = 'search.emp';
         </c:if>
         <c:if test="${requestScope.lock eq 'true' }">
         var urlRefresh = 'search.emp?lock=true';
         </c:if>
         var formId = 'selectForm'; 

         var pageForm = $('#' + formId);
         var tableId = 'the-table';
         var table = Ext.get(tableId);
         var cf = [], ch = [];
         var fields = [], cols = [new Ext.grid.RowNumberer()]; 
         if (window.checkboxSelectionModel) {
             cols.push(checkboxSelectionModel);
         }
         var headers = table.query("thead td");
         for (var i = 0, h; h = headers[i]; i++) {
             var text = h.innerHTML;
             //var name = 'tcol-' + i;
             var name = Ext.get(h).getAttribute('field');
             if (Ext.get(h).getAttribute('fieldType') == 'date') {
            	cf[i] = {type: 'date', dateFormat:Ext.get(h).getAttribute('formate')} 
             } 
             fields.push(Ext.applyIf(cf[i] ||
             {}, {
                 name: name,
                 mapping: 'td:nth(' + (i + 1) + ')/@innerHTML'
             }));
             if (Ext.get(h).getAttribute('export')=='true')  continue;
             cols.push(Ext.applyIf(ch[i] ||
             {}, {
                 'header': text,
                 'locked':!!Ext.get(h).getAttribute('locked')||false,
                 'dataIndex': name,
                 'width': Ext.get(h).getAttribute('width')!=''?parseInt(Ext.get(h).getAttribute('width')):h.offsetWidth,
                 'tooltip': h.title,
                 'renderer': Ext.get(h).getAttribute('renderer')!=''?eval(Ext.get(h).getAttribute('renderer')):null,
                 'menuDisabled':!!Ext.get(h).getAttribute('menuDisabled')||false,
                 'hideable':Ext.get(h).getAttribute('hideable')==='false' ? false :true,
                 '_sortField':Ext.get(h).getAttribute('sortField'),
                 'editor':Ext.get(h).getAttribute('editor')!=''?eval(Ext.get(h).getAttribute('editor')):null,
                 'sortable': !!Ext.get(h).getAttribute('sortField')||false
             }));
         }

         var store = new Ext.data.Store({
             reader: new Ext.data.XmlReader({
                 record: 'tbody tr'
             }, fields)
         });


        // shorthand alias
        var fm = Ext.form;

        // the column model has information about grid columns
        // dataIndex maps the column to the specific data field in
        // the data store (created below)
        
 
			store.totalLength = ${requestScope.count};
            var start = ${requestScope.page};
            var end  = ${requestScope.pageEnd};
            var count = ${requestScope.count};
            var size = ${requestScope.pageNum};
            var str = String.format('显示 {0}- {1}条，共 {2} 条', start, end, count);
            var searchName = new Ext.form.TextField({
            	allowBlank: true
            	,value:'${requestScope.name}'
            });
        // create the editor grid
        <c:if test="${sessionScope.loginUser.name eq 'admin' }">
        <c:if test="${requestScope.lock != 'true' }">
        var grid = new Ext.grid.EditorGridPanel({
        </c:if>
        <c:if test="${requestScope.lock eq 'true' }">
        var grid = new Ext.grid.GridPanel({
        </c:if>
        </c:if>	
        <c:if test="${sessionScope.loginUser.name != 'admin' }">
        var grid = new Ext.grid.GridPanel({
        </c:if>	        
            store: store,
            colModel: new Ext.grid.ColumnModel(cols),
            renderTo: 'editor-grid',
            width: 800,
            height: 500, 
            title: '人员去向',
            frame: true,
            clicksToEdit: 1,
            bbar: new Ext.PagingToolbar({
                pageSize: size,
                store: store,
                displayInfo: true,
                displayMsg: str,
                doRefresh:function() {
                    window.location.href = urlRefresh;
                },
                getPageData : function() {
                    var cursor = pageForm.find('input:hidden[name=page]').val();
                    store.totalLength = ${requestScope.count};
                    var total = store.getTotalCount();
                    return {
                        total : count,
                        activePage : cursor,
                        pages :  total < this.pageSize ? 1 : Math.ceil(total/this.pageSize)
                    };
                },
                onPagingKeyDown : function(field, e) {
                    var k = e.getKey(), d = this.getPageData(), pageNum;
                    if (k == e.RETURN) {
                        e.stopEvent();
                        pageNum = this.readPage(d);
                        if (pageNum !== false) {
                            pageNum = Math.min(Math.max(1, pageNum), d.pages);
                            pageForm.find('input:hidden[name=page]').val(pageNum);
                            pageForm.submit();
                        }
                    } else if (k == e.HOME || k == e.END) {
                        e.stopEvent();
                        pageNum = k == e.HOME ? 1 : d.pages;
                        field.setValue(pageNum);
                    } else if (k == e.UP || k == e.PAGEUP || k == e.DOWN || k == e.PAGEDOWN) {
                        e.stopEvent();
                        if ((pageNum = this.readPage(d))) {
                            var increment = e.shiftKey ? 10 : 1;
                            if (k == e.DOWN || k == e.PAGEDOWN) {
                                increment *= -1;
                            }
                            pageNum += increment;
                            if (pageNum >= 1 & pageNum <= d.pages) {
                                field.setValue(pageNum);
                            }
                        }
                    }
                },
                moveFirst : function(){
                    pageForm.find('input:hidden[name=page]').val(1);
                    pageForm.submit();
                },


                movePrevious : function(){
                    var value = pageForm.find('input:hidden[name=page]').val();
                    pageForm.find('input:hidden[name=page]').val(parseInt(value) - 1);
                    pageForm.submit();
                },


                moveNext : function(){
                    var value = pageForm.find('input:hidden[name=page]').val();
                    pageForm.find('input:hidden[name=page]').val(parseInt(value) + 1);
                    pageForm.submit();
                },
                moveLast : function(){
                    var value = (count % size == 0) ? count / size : count / size + 1;
                    pageForm.find('input:hidden[name=page]').val(parseInt(value));
                    pageForm.submit();
                },
                emptyMsg: "没有数据"
            }),   
            tbar: ['人员姓名:'
            ,searchName
            ,{
            	text:'查询', 
            	iconCls:'search',
            	handler : function(){ 
            		$('#name').val(searchName.getValue());
            		pageForm.submit();
            	}
            } 
            <c:if test="${sessionScope.loginUser.name eq 'admin' }">
            <c:if test="${requestScope.lock != 'true' }">
            ,'-'      
            ,{
            	text:'添加',
            	tooltip:'添加一条记录',
            	iconCls:'add',
            	handler : function(){
                    // access the Record constructor through the grid's store
                    var Plant = grid.getStore().recordType;
                    var p = new Plant({ 
                    });
                    grid.stopEditing();
                    store.insert(store.getCount() , p);
                    grid.startEditing(0, 0);
                }
            },  '-'
            , {
            	text:'删除',
            	iconCls:'remove',
            	handler:function() {
            		var sm = grid.getSelectionModel();
	            	if (sm.getSelectedCell()!= null){
	            		var cell = sm.getSelectedCell();
	            		var record = store.getAt(cell[0]);
	            		var name = record.get('name')||'';
	            		Ext .Msg.confirm('信息','确定要删除 ' + name + '？',function(btn) {
	                    	if(btn == 'yes') { 
	                    		if(record.get('key')) {
	                    			$('#key').val(record.get('key'));
	                    			$('#delete').submit();
	                    		} else {
	                    			store.remove(record); 
	                    		}
	                    	
	                    	}});
	            	}
            	}
            },{
            	text:'保存',
            	tooltip:'批量保存',
            	iconCls:'save',
            	handler : function(){
					var records = store.getModifiedRecords();
					var insert = [];
					var update = [];
					if (!records)return;
					if (records.length == 0) return;
					Ext.each(records, function(r){
						if (r.get('key')) {
							var item = {
								key:r.get('key')	
								,name:r.get('name')
								,org:r.get('org')||''
								,start:''
								,end:''		
								,visa:r.get('visa')||''
								,address:r.get('address')||''
							}
							if (r.get('start')) {
								item['start']=r.get('start').format('Y-m-d'); 
							}
							if (r.get('end')) {
								item['end']=r.get('end').format('Y-m-d');
							}
							update.push(item);
						} else if (r.get('name')){
							var item = {
								name:r.get('name')||''
								,org:r.get('org')||''
								,start:''
								,end:''						
								,visa:r.get('visa')||''
								,address:r.get('address')||''
							}
							if (r.get('start')) {
								item['start']=r.get('start').format('Y-m-d'); 
							}
							if (r.get('end')) {
								item['end']=r.get('end').format('Y-m-d');
							}
							insert.push(item);							
						}
		            });  
					if (insert.length > 0 || update.length > 0) {
						$('#insert').val((Ext.encode(insert)));
						$('#update').val((Ext.encode(update)));
						$('#batch').submit();						
					}

                }
           	}
            </c:if>
            </c:if>	 
            ]
        });
        store.loadData(table.dom);
        table.setStyle('display','none');
    });
    </script>

</head>
<body>
    <h1><a href="checkUserLogin.login">返回目录</a> > 人员去向</h1>
<form name="selectForm" method="post" id="selectForm"> 
	<input type="hidden" value="${requestScope.name}" name="name" id="name"/> 
	<input type="hidden" value="${requestScope.page}" name="page" id="page"/> 
</form>
<form name="delete" method="post" action="delete.emp" id="delete"> 
	<input type="hidden" name="key" id="key"/>  
</form> 	
<form name="batch" method="post" action="batch.emp" id="batch"> 
	<input type="hidden" name="insert" id="insert"/> 
	<input type="hidden" name="update" id="update"/>
</form>    
    <div id="editor-grid">
    <table style="visibility: hidden;" cellspacing="0" id="the-table">
        <thead>
            <tr style="background:#eeeeee;">
            	<td width="60"  export="true"  menuDisabled="false" hideable="false" editor="new Ext.form.TextField({allowBlank: false})" field="key" nowrap>主键</td>
	            <td width="60"   menuDisabled="false" hideable="false" editor="new Ext.form.TextField({allowBlank: false})" field="name" nowrap>人员姓名</td>
	            <td width="200"  menuDisabled="false" hideable="false" editor="new Ext.form.TextField({allowBlank: false})" field="org" nowrap>单位/部门</td>
	            <td width="120" fieldType="date" formate="Y-m-d"  menuDisabled="false" hideable="false" renderer="formatDate" editor="new Ext.form.DateField({
                format: 'Y-m-d' 
            })" field="start" nowrap>出发日期</td>
	            <td width="120" fieldType="date" formate="Y-m-d" menuDisabled="false" hideable="false" renderer="formatDate" editor="new Ext.form.DateField({
                format: 'Y-m-d' 
            })" field="end" nowrap>返回日期</td>
	            <td width="120"  menuDisabled="false" hideable="false" editor="new Ext.form.TextField({allowBlank: true})" field="visa" nowrap>签证类型</td>
	            <td width="150"  menuDisabled="false" hideable="false" editor="new Ext.form.TextField({allowBlank: true})" field="address" nowrap>去向地点</td>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${requestScope.list}" varStatus="status">
            <tr>
            
            	<td>${item.key }</td>
                <td>${item.name }</td>
                <td>${item.org }</td>
                <td>${item.start }</td> 
                <td>${item.end }</td>
                <td>${item.visa }</td>
                <td>${item.address }</td>
              
            </tr> 
        </c:forEach> 
        </tbody>
    </table>
    </div>
</body>
</html>
<%@ include file="../inc/tld.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    var pageForm = $('#' + formId);

    var table = Ext.get(tableId);
    var cf = [], ch = [];
    var fields = [], cols = []; 
    if (window.checkboxSelectionModel) {
        cols.push(checkboxSelectionModel);
    }
    var headers = table.query("thead td");
    for (var i = 0, h; h = headers[i]; i++) {
        var text = h.innerHTML;
        var name = 'tcol-' + i;
        if (Ext.get(h).getAttribute('export')=='true')  continue;
        fields.push(Ext.applyIf(cf[i] ||
        {}, {
            name: name,
            mapping: 'td:nth(' + (i + 1) + ')/@innerHTML'
        }));
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
            'sortable': !!Ext.get(h).getAttribute('sortField')||false
        }));
    }

    var ds = new Ext.data.Store({
        reader: new Ext.data.XmlReader({
            record: 'tbody tr'
        }, fields)
    });

    ds.loadData(table.dom);
    table.setStyle('display','none');
    //table.remove();
    ds.totalLength = 5;
    var start = 1;
    var end  = 5;
    var count = 5;
    var size = 1;
    var str = String.format('显示 {0}- {1}条，共 {2} 条', start, end, count);
    // create the Grid
    // To use locking functionality we must explicitly specify the LockingColumnModel and the LockingGridView
    var config = {
        store: ds,
        colModel: new Ext.ux.grid.LockingColumnModel(cols),
        stripeRows: true,
        height: document.body.clientHeight*height,
        width: document.body.clientWidth*width,
        // paging bar on the bottom
        bbar: new Ext.PagingToolbar({
            pageSize: size,
            store: ds,
            displayInfo: true,
            displayMsg: str,
            doRefresh:function() {
                window.location.href = urlRefresh;
            },
            getPageData : function() {
                var cursor = pageForm.find('input:hidden[name=currentPage]').val();
                var total = ds.getTotalCount();
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
                        pageForm.find('input:hidden[name=currentPage]').val(pageNum);
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
                pageForm.find('input:hidden[name=currentPage]').val(1);
                pageForm.submit();
            },


            movePrevious : function(){
                var value = pageForm.find('input:hidden[name=currentPage]').val();
                pageForm.find('input:hidden[name=currentPage]').val(parseInt(value) - 1);
                pageForm.submit();
            },


            moveNext : function(){
                var value = pageForm.find('input:hidden[name=currentPage]').val();
                pageForm.find('input:hidden[name=currentPage]').val(parseInt(value) + 1);
                pageForm.submit();
            },
            moveLast : function(){
                var value = (count % size == 0) ? count / size : count / size + 1;
                pageForm.find('input:hidden[name=currentPage]').val(parseInt(value));
                pageForm.submit();
            },
            emptyMsg: "没有数据"
        }),
        view: new Ext.ux.grid.LockingGridView({
            onHeaderClick : function(g, index){
                if(this.headersDisabled || !this.cm.isSortable(index)){
                    return;
                }
                g.stopEditing(true); 
                var field = g.store.fields.get(this.cm.getDataIndex(index));

                var name = field.name;
                var dir;

                //g.store.sort(this.cm.getDataIndex(index));

                var fieldOrder = $.trim(pageForm.find('input:hidden[name=fieldOrder]').val());
                var sortField; 
                if (fieldOrder.length > 0) {   //得到排序的字段和方向
                    var _index = fieldOrder.indexOf(' ');
                    if (_index > 0) {
                        sortField = fieldOrder.substring(0, _index);
                        var sortDir = $.trim(fieldOrder.substr(_index));
                        dir = sortDir.toLowerCase().indexOf("asc") >= 0 ? " desc" : " asc";
                    }
                    else {
                        sortField = fieldOrder;
                        dir = " asc";
                    }
                } else {
                    dir = (g.store.sortToggle[name] || 'ASC').toggle(' ASC', ' DESC');
                }

                $('#fieldOrder').val(this.cm.config[index]._sortField + dir);
                pageForm.submit();
            }
            ,handleHdMenuClick : function(item){
                var index = this.hdCtxIndex,
                    cm = this.cm,
                    ds = this.ds,
                    id = item.getItemId();
                var llen = cm.getLockedCount();
                switch(id){
                    case 'asc':
                        $('#fieldOrder').val(cm.config[index]._sortField + ' ASC');
                        pageForm.submit();
                        break;
                    case 'desc':
                        $('#fieldOrder').val(cm.config[index]._sortField + ' DESC');
                        pageForm.submit();
                        break;
                    case 'lock':
                        if(cm.getColumnCount(true) <= llen + 1){
                            this.onDenyColumnLock();
                            return;
                        }
                        cm.setLocked(index, true);
                        if(llen != index){
                            cm.moveColumn(index, llen);
                            this.grid.fireEvent('columnmove', index, llen);
                        }
                    break;
                    case 'unlock':
                        if(llen - 1 != index){
                            cm.setLocked(index, false, true);
                            cm.moveColumn(index, llen - 1);
                            this.grid.fireEvent('columnmove', index, llen - 1);
                        }else{
                            cm.setLocked(index, false);
                        }
                    break;
                    default:
                        index = cm.getIndexById(id.substr(4));
                        if(index != -1){
                            if(item.checked && cm.getColumnsBy(this.isHideableColumn, this).length <= 1){
                                this.onDenyColumnHide();
                                return false;
                            }
                            cm.setHidden(index, item.checked);
                        }
                }
                return true;
            }
        })
    };
    if (window.myconfig) {
        Ext.apply(config,myconfig);
    }
    var grid = new Ext.grid.GridPanel(config);

    // render the grid to the specified div in the page
    grid.render('center');
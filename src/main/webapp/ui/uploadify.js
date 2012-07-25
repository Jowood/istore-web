(function($) {
    /** 通用效果渲染 */
    $.fn.myUpLoadify = function (opts) {
        /*操作对象集*/
        var objs = {
            _fileQueue:"fileQueue",
            _files:"files",
            _sf_ids:"sf_ids",
            _sf_names:"sf_names",
            _subButton:"subButton",
            _delUrl:'delFile.ebook'	
        };
        var defaults = {
                'uploader': 'ui/uploadify/uploadify.swf',
                'script': 'uploadHandler.ebook',
                'cancelImg': 'ui/uploadify/cancel.png',
                'folder': 'UploadFile',
                'queueID': objs._fileQueue,
                'wmode':'transparent',
                'auto': true,
                'multi': true,
                'fileDesc':'支持格式:*.*',
                'fileExt': '*.*',
                'buttonImg':'images/upload.jpg',
                'onSelect':function() {
                    $('#' + objs._subButton).attr('disabled', true);
                    $('#' + objs._fileQueue).show();
                },
                'onCancel':function() {
                    $('#' + objs._subButton).attr('disabled', false);
                    $('#' + objs._fileQueue).hide();
                },
                'onComplete': function(e, queueId, fileObj, response) {
                    $('#' + objs._fileQueue).hide();
                    var row = $("<span>&nbsp;&nbsp;" + fileObj.name + "<img sn='" + fileObj.name + "' id='" + response + "' st='" + fileObj.type + "' style='cursor:pointer;' src='images/del.gif' title='取消附件'/></span>");
                    row.find("img").click(function() {
                        var json = {
                            key:response,
                            sf_type:fileObj.type
                        }
/*                        $.ajaxPost(objs._delUrl, [json], function() {
                        }, null, 'json');*/
                        $.ajax({ 
							 url: objs._delUrl  
							 ,data :json
						     ,type:'post' 
						 });
                        
                        
                        $(row).remove();
                        resetFiles();
                        //workcamp_taskService.deleteSub(json, function() {
                        //});
                    });
                    $('#' + objs._files).append(row);
                },
                'onAllComplete':function(e, data) {
                    $('#' + objs._subButton).attr('disabled', false);
                    resetFiles();
                }
            };
        if (opts != null) {
        	if (opts.script) defaults.script=opts.script;
        	if (opts._delUrl) objs._delUrl=opts._delUrl;
            if (opts.objname != null) {
                if (opts.objname.fileQueue != null)
                    objs._fileQueue = opts.objname.fileQueue;
                if (opts.objname.files != null)
                    objs._files = opts.objname.files;
                if (opts.objname.sf_ids != null)
                    objs._sf_ids = opts.objname.sf_ids;
                if (opts.objname.subButton != null)
                    objs._subButton = opts.objname.subButton;
            }
            
        }

        

        var resetFiles = function() {
            var _sf_ids = "";
            var _sf_names = "";
            $('#' + objs._files + ' img').each(function(i, item) {
                if (_sf_ids == "") {
                    _sf_ids = $(item).attr('id');
                    _sf_names = $(item).attr('sn');
                } else {
                    _sf_ids += ";" + $(item).attr('id');
                    _sf_names += ";" + $(item).attr('sn');
                }
            });
            $('#' + objs._sf_ids).val(_sf_ids);
            $('#' + objs._sf_names).val(_sf_names);
        };

        /*判断是否存在 input id为sf_ids */
        if ($('#' + objs._sf_ids).length == 0) {
            var sf_ids = $("<input type='hidden' id='" + objs._sf_ids + "' name='" + objs._sf_ids + "'/>");
            $(this).after(sf_ids);
        }
        if ($('#' + objs._sf_names).length == 0) {
            var sf_names = $("<input type='hidden' id='" + objs._sf_names + "' name='" + objs._sf_names + "'/>");
            $(this).after(sf_names);
        }     
        
        
        /*判断是否存在 div id为fileQueue */
        if ($('#' + objs._fileQueue).length == 0) {
            var fileQueue = $("<div id='" + objs._fileQueue + "' style='width:380px;height:80px;top:30%;left:15%;'></div>");
            $(this).after(fileQueue);
        }

        $('#' + objs._fileQueue).css("border", "0px solid").css("overflow", "auto").css("position", "absolute").css("display", "none").css("background-color", "white").css("text-align", "left");

        /*下载文件渲染*/
        $('#' + objs._files).find("upfile").each(function() {
            var r = $("<span>&nbsp;&nbsp;" + $(this).attr("name") + "<img id='' st='' style='cursor:pointer;' src='images/del.gif' title='取消附件'/></span>");
            $(r).find("img").attr("id", $(this).attr("id"));
            $(r).find("img").attr("st", $(this).attr("st"));
            $(r).find("img").click(function() {
                var json = {
                    sf_id:$(this).attr("id"),
                    sf_type:$(this).attr("st")
                }
                $.ajax({ 
					 url: objs._delUrl  
					 ,data :json
				     ,type:'post' 
				 });                
/*                $.ajaxPost(objs._delUrl, [json], function() {
                }, null, 'json');*/
                
                $(r).remove();
            });
            $(this).after(r);
            $(this).remove();
        });

        resetFiles();
        $(this).uploadify(defaults);
    }
})(jQuery);
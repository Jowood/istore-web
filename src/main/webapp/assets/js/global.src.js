/**
 * @name			书库 
 * @namespace		DEMO.EBOOK
 * @author			Chappell Qu <chappell(dot)wat(at)gmail(dot)com>
 * @version			0.0.1
 */

;(function(win, doc, $, undefined){

	// 命名空间管理
	$.namespace = function(ns) {
		// not exist
		if (!ns || !ns.length) {
			return null;
		}

		var lvArr = ns.split('.'),
			nsObj = win;
		for (var i = 0, j = lvArr.length;  i < j; i ++) {
			nsObj[lvArr[i]] = nsObj[lvArr[i]] || {};
			nsObj = nsObj[lvArr[i]];
		}

		return nsObj;
	};

	// 新建命名空间
	var APP = $.namespace('DEMO');

	// 环境变量
	APP.ENV = {
		'IS_IE'			:	true === $.browser.msie,
		'IS_EMBEDED'	:	self !== top
	};
	APP.ENV.IS_IE6 = APP.ENV.IS_IE && 0 === $.browser.version.indexOf('6') && !$.support.style; // ie6

	// 通用快捷方式
	APP.REF = {
		'$WIN'  : $(win),
		'$DOC'  : $(doc),
		'$BODY' : $('body')
	};

	// 通用 Class 名称
	APP.CLS = {
		'clearfix'		:	'clearfix',
		'invisible'		:	'invisible',
		'transparent'	:	'transparent'
	};

})(window, document, jQuery);

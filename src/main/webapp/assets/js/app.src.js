/**
 * @name			书库 
 * @namespace		DEMO.EBOOK
 * @author			Chappell Qu <chappell(dot)wat(at)gmail(dot)com>
 * @version			0.0.1
 */

;(function(win, doc, APP, $, undefined){

	// 命名空间
	var NS = $.namespace('DEMO.EBOOK');

	// 默认配置信息
	NS.CFG = {				
		//'CGI'			:	'./cgi.php',	// Ajax CGI 地址
		//'NAME'			:	'eBook Shelf',	// 应用名称
		//'WIDTH'			:	418,			// 窗口宽度
		//'HEIGHT'		:	498,			// 窗口高度
		
		
		'CGI'			:	'./queryCatalog.ebook',
		'WIDTH'			:	$(window).width(),			 
		'HEIGHT'		:	$(window).height(),
		'NAME' : '我的电子书架',	
		'MAX_PG'		:	5,				// 书库最大页数
		'PER_PG'		:	10,				// 每页书籍数量
		'STACK_WIDTH'	:	380				// 每页书籍列表的宽度
	};

	// 扩展快捷方式
	$.extend(APP.REF, {
		'$shelfContainer'	:	$('#shelf'),
		'$bookContainer'	:	$('#book'),
		'$overlayContainer'	:	$('#overlay'),
		'$shelfPagination'	:	$('#shelf-pagination'),
		'$stackContainer'	:	$('#stack'),
		'$stackWrap'		:	$('#stack-wrap'),
		'$bookContent'		:	$('#content'),
		'$bookPageNum'		:	$('#book-page-num')
	});

	// Ajax 设置
	$.ajaxSetup({
		type		:	'GET',
		url			:	NS.CFG.CGI,
		dataType	:	'json',
		timeout		:	5000
	});
	APP.REF.$overlayContainer.bind('ajaxStart', function(){
		APP.REF.$overlayContainer.removeClass(APP.CLS.transparent);
	}).bind('ajaxStop', function(){
		APP.REF.$overlayContainer.addClass(APP.CLS.transparent);
	});

	/**
	 * 绘制页面导航
	 *
	 * @return	{void}
	 */
	var initPageNav = function () {
		// 状态
		var isFailed = true;
		
		// 模版
		var shelfItemBlankTpl = '<div class="cols" style="width:{width}"></div>',
			shelfPaginationItemTpl = '<a href="###" title="第 {page} 页" data-pg="{page}" data-act="shelf-nav" data-loaded="{loaded}">{page}</a>';

		// 发起请求
		$.ajax({
			url			:	NS.CFG.CGI,
			data		:	{
				'act'	:	'init'
			},
			success		:	function (res) {
				if (undefined === res || 0 !== res.retcode) {
					return;
				}

				// 获取数据
				var totalPageNum = res.result.total_pg;
				NS.CFG.MAX_PG = res.result.max_pg||NS.CFG.MAX_PG;
				if (0 === totalPageNum) {
					return;
				}

				var paginationHtml = [];
				for (var i = 0; i < NS.CFG.MAX_PG; i ++) {
					paginationHtml.push(shelfPaginationItemTpl.replace(/{(\w+)}/g, function (nul, key) {
						var retVal = '';
						switch (key) {
						case 'page':
							retVal = i + 1;
							break;
						case 'loaded':
							// 对无需发请求加载数据的页数事先设置为 true ，IE6 由于需要书架阴影则需要写入所有结构
							retVal = APP.ENV.IS_IE6 ? 'false' : i >= totalPageNum;
							break;
						}
						return retVal.toString();
					}));
				}

				// 更新HTML
				if (0 === paginationHtml.length) {
					return;
				}
				APP.REF.$shelfPagination.html(paginationHtml.join(''));

				// 插入置位标签
				var placeholderArr = [];
				placeholderArr.push('<div class="cols" style="width:' + APP.REF.$stackWrap.css('width') + '"></div>');
				for (var i = 0, j = NS.CFG.MAX_PG; i < j; i ++) {
					placeholderArr.push(shelfItemBlankTpl.replace(/{(\w+)}/g, function (nul, key) {
						var retVal = '';
						switch (key) {
						case 'width':
							retVal = APP.REF.$stackWrap.css('width');
							break; 
						}
						return retVal.toString();
					}));
				}
				APP.REF.$stackWrap.html(placeholderArr.join(''));

				// 扩大书库的尺寸
				NS.CFG.STACK_WIDTH =  parseInt(APP.REF.$stackWrap.css('width'), 10);
				APP.REF.$stackWrap.css('width', NS.CFG.STACK_WIDTH );

				// 更新状态
				isFailed = false;
			},
			error		:	function (foo, bar) {
			},
			complete	:	function () {
				if (isFailed) {
					// 读取出错
					alert('读取出错');
					return;
				}

				// 读取图书首页
				changeShelfPage();
			}
		});
	};

	/**
	 * 书库翻页
	 *
	 * @param	{int}		pg	页面编号
	 * @return	{void}
	 */
	var changeShelfPage = function (pg) {
		// 获取页数
		if (pg) {
			pg = parseInt(pg, 10);
			if (isNaN(pg) || 0 >= pg || NS.CFG.MAX_PG < pg) {
				return;
			}
		} else {
			pg = 1;
		}

		// 获得相对应的页面链接
		var $pageTrigger = APP.REF.$shelfPagination.find('[data-pg="' + pg + '"]:first');
		if (0 === $pageTrigger.length) {
			return;
		}

		// 检查该页是否已被读取过
		if ('true' !== $pageTrigger.attr('data-loaded')) {
			loadShelfPage(pg);
			return;
		}

		// 设置为当前的页面
		var selectedClassName = 'selected';
		APP.REF.$shelfPagination.find('.' + selectedClassName).removeClass(selectedClassName);
		$pageTrigger.addClass(selectedClassName);

		// 动画
		APP.REF.$stackWrap.animate({
			'marginLeft'	:	(1 - pg) * NS.CFG.STACK_WIDTH
		}, 300);
	}

	/**
	 * 读取指定页的内容
	 *
	 * @param	{int}		pg	页面编号
	 * @return	{void}
	 */
	var loadShelfPage = function (pg) {
		// 状态
		var isFailed = true;
		
		// 模版
		var bookTpl = '<a href="{url}"><div class="outer" {icon}><div class="middle" ><div class="inner greenBorder">{name}</div></div></div></a>',
			bookBlankTpl = '<div></div>';

		// 发起请求
		$.ajax({
			url			:	NS.CFG.CGI,
			data		:	{
				'act'	:	'list',
				'pg'	:	pg
			},
			success		:	function (res) {
				if (undefined === res || 0 !== res.retcode) {
					return;
				}

				// 获取数据
				var bookArr = res.result; 

				var bookStackHtml = [];
				var ul = 0;
				var projectFlag = false;
				
				var i = 0;
				bookStackHtml.push(i in bookArr ? bookTpl.replace(/{(\w+)}/g, function (nul, key) {
					var retVal = '';
					switch (key) {
					case 'idx':
						retVal = (pg - 1) * NS.CFG.PER_PG + i + 1; // # of book
						break;
					case 'name':
						retVal = bookArr[i].name;
						break;
					case 'url':
						retVal = bookArr[i].url;
						break;	
					case 'icon':
						if ('icon' in bookArr[i]) {
							retVal = ' style="background:url(' + bookArr[i].icon + ')  no-repeat scroll 50% 50% transparent"';
						}
						break;
					}
					return retVal.toString();
				}) : bookBlankTpl);
				
				for(var i = 1; i < bookArr.length; i++){
					if(bookArr[i].url == '#') {
						projectFlag = true;
					} else {
						projectFlag = false;
					}
					if (projectFlag) {  
						APP.REF.$stackWrap.find('div.cols:eq(' + ul + ')').addClass(APP.CLS.clearfix).html(bookStackHtml.join(''));
						ul++;
						bookStackHtml = []; 
					}
					bookStackHtml.push(i in bookArr ? bookTpl.replace(/{(\w+)}/g, function (nul, key) {
						var retVal = '';
						switch (key) {
						case 'idx':
							retVal = (pg - 1) * NS.CFG.PER_PG + i + 1; // # of book
							break;
						case 'name':
							retVal = bookArr[i].name;
							break;
						case 'url':
							retVal = bookArr[i].url;
							break;	
						case 'icon':
							if ('icon' in bookArr[i]) {
								retVal = ' style="background:url(' + bookArr[i].icon + ')  no-repeat scroll 50% 50% transparent"';
							}
							break;
						}
						return retVal.toString();
					}) : bookBlankTpl);
				} 
				if (bookStackHtml.length) {
					APP.REF.$stackWrap.find('div.cols:eq(' + ul + ')').addClass(APP.CLS.clearfix).html(bookStackHtml.join(''));
				}
				
				// 更新HTML
				//APP.REF.$stackWrap.find('ul:eq(' + (pg - 1) + ')').addClass(APP.CLS.clearfix).html(bookStackHtml.join(''));
				//APP.REF.$stackWrap.find('ul:eq(' + (1) + ')').addClass(APP.CLS.clearfix).html(bookStackHtml.join(''));
				//APP.REF.$stackWrap.find('ul:eq(' + (2) + ')').addClass(APP.CLS.clearfix).html(bookStackHtml.join(''));
				// 设置为已读取
				APP.REF.$shelfPagination.find('[data-pg="' + pg + '"]:first').attr('data-loaded', 'true');

				// 翻页
				changeShelfPage(pg);

				// 更新状态
				isFailed = false;
			},
			error		:	function (foo, bar) {
			},
			complete	:	function () {
				if (isFailed) {
					// 读取出错
					alert('读取出错');
					return;
				}
			}
		});
	};

	/**
	 * 书本翻页
	 *
	 * @param	{int}		idx		书本编号
	 * @param	{int}		offset	偏移（向前翻页是首部偏移；向后翻页则为尾部偏移）
	 * @param	{bool}		next	是否向后翻页
	 * @return	{void}
	 */
	var flipToPage = function (idx, offset, next) {
		// 状态
		var isFailed = true;

		// 发起请求
		$.ajax({
			type		:	'POST',
			data		:	{
				'act'	:	'read',
				'idx'	:	idx,
				'offset':	offset,
				'next'	:	next ? 1 : 0
			},
			success		:	function (res) {
				if (undefined === res || 0 !== res.retcode) {
					return;
				}

				// 更新HTML
				APP.REF.$bookContent.html(toSafeHTML(res.result.content));

				// 更新偏移数据
				APP.REF.$bookContent.attr('data-start', res.result.start).attr('data-end', res.result.end);

				// 更新页数
				var pageNum = parseInt(APP.REF.$bookContent.attr('data-pg'), 10);
				if (!isNaN(pageNum)) {
					pageNum += next ? 1 : -1;
					if (0 < pageNum) {
						APP.REF.$bookPageNum.html(pageNum)
						APP.REF.$bookContent.attr('data-pg', pageNum);
					}
				}

				// 是否读到末页
				APP.REF.$bookContent.attr('data-eof', 'eof' in res.result ? '1' : '');

				// 更新状态
				isFailed = false;
			},
			error		:	function (foo, bar) {
			},
			complete	:	function () {
				if (isFailed) {
					// 读取出错
					alert('读取出错');
					return;
				}
			}
		});
	};

	/**
	 * 讲 HTML 代码安全转义
	 *
	 * @param	{string}		str		HTML 代码
	 * @return	{string}				安全转义后的代码
	 */
	var toSafeHTML = function (str) {
		return str.replace(/&/g, '&#38;').replace(/</g, '&#60;').replace(/>/g, '&#62;') // 标签
					.replace(/\r\n|\r|\n/g, '<br/>').replace(/\s/g, '&#160;'); // 换行符和空格
	};

	/**
	 * 初始化
	 *
	 * @return	{void}
	 */
	var init = function () {
		// 初始化页码
		initPageNav();

		// 处理点击行为
		APP.REF.$DOC.bind('click', function (evt) {
			// 获取事件目标对象
			var curTar = evt.target;
			if (!curTar || 1 !== curTar.nodeType) {
				return;
			}

			// 获取最近的 A 元素
			var actionAttr = 'data-act';
			var $trigger = $(curTar).closest('a[' + actionAttr + ']');
			if (0 === $trigger.length) {
				return;
			}

			// 过滤行为
			var curAction = $trigger.attr(actionAttr);
			switch (curAction) {
			case 'external-shelf':	// 打开外部书库
				try {
					// open with the build-in explorer in webqq
					alloy.system.openURL({
						'url'	:	$trigger[0].href
					});
				} catch (e) {
					return; // open a new window
				}
				break;
			case 'return-shelf':	// 返回书库
				// 隐藏书本
				APP.REF.$bookContainer.addClass(APP.CLS.transparent);
				break;
			case 'shelf-nav':		// 书库翻页
				changeShelfPage($trigger.attr('data-pg'));
				break;
			case 'open-book':		// 打开图书
				// 显示书本
				//APP.REF.$bookContainer.removeClass(APP.CLS.transparent); // 显示书本界面
				//APP.REF.$bookContent.attr('data-idx', $trigger.attr('data-idx')).attr('data-start', '0').attr('data-end', '0').attr('data-pg', '0').html(''); // 重置书本内容
				//APP.REF.$bookPageNum.html(''); // 重置页码
				window.location.href=$trigger.attr('href');
			case 'book-nav':		// 图书翻页
				// 获取书本编号
				var bookIdx = parseInt(APP.REF.$bookContent.attr('data-idx'), 10);
				if (isNaN(bookIdx) || 0 >= bookIdx) {
					break;
				}

				// 获取偏移值
				var gotoNextPg = 'prev' !== $trigger.attr('data-nav');
				if (gotoNextPg) {
					if ('1' === APP.REF.$bookContent.attr('data-eof')) {
						alert('已达末页');
						break;
					}
				} else {
					var pageNum = parseInt(APP.REF.$bookContent.attr('data-pg'), 10);
					if (isNaN(pageNum) || 1 >= pageNum) {
						alert('已达首页');
						break;
					}
				}

				var offsetVal = APP.REF.$bookContent.attr('data-' + (gotoNextPg ? 'end' : 'start'));
				if (!/^[0-9A-Fa-f]+$/.test(offsetVal)) { // HEX
					break;
				}

				flipToPage(bookIdx, offsetVal, gotoNextPg);
				break;
			default:
				return;
			}

			// 取消默认动作和冒泡
			evt.preventDefault();
			evt.stopPropagation();
		});
 

		// 禁止右键
/*		APP.REF.$DOC.bind('contextmenu', function (evt) {
			evt.preventDefault();
		});*/

		// 禁止拖拽
		APP.ENV.IS_IE && APP.REF.$DOC.bind('selectstart', function (evt) {
			if ('input' !== evt.target.tagName.toLowerCase()) {
				evt.preventDefault();
			}
		});

	};

	/**
	 * 外部入口
	 *
	 * @return	{void}
	 */
	NS.init = function (opt) {

		// 更新配置信息
		NS.CFG = $.extend({}, NS.CFG, opt);
		opt = null;

		// 页面 DOM 构建完成时执行
		APP.REF.$DOC.ready(init);

	};

})(window, document, DEMO, jQuery);

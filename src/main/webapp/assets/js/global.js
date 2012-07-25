/**
 * @name			书库 
 * @namespace		DEMO.EBOOK
 * @author			Chappell Qu <chappell(dot)wat(at)gmail(dot)com>
 * @version			0.0.1
 */
(function(f,g,a){a.namespace=function(b){if(!b||!b.length)return null;b=b.split(".");for(var c=f,d=0,h=b.length;d<h;d++){c[b[d]]=c[b[d]]||{};c=c[b[d]]}return c};var e=a.namespace("DEMO");e.ENV={IS_IE:true===a.browser.msie,IS_EMBEDED:self!==top};e.ENV.IS_IE6=e.ENV.IS_IE&&0===a.browser.version.indexOf("6")&&!a.support.style;e.REF={$WIN:a(f),$DOC:a(g),$BODY:a("body")};e.CLS={clearfix:"clearfix",invisible:"invisible",transparent:"transparent"}})(window,document,jQuery);

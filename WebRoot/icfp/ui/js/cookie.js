icfp.cookie = {};

icfp.cookie.setcookie =  function(cookieName, cookieValue, seconds, path, domain, secure) {
	var expires = new Date();
	expires.setTime(expires.getTime() + seconds);
	document.cookie = escape(cookieName) + '=' + escape(cookieValue)
	+ (expires ? '; expires=' + expires.toGMTString() : '')
	+ (path ? '; path=' + path : '/')
	+ (domain ? '; domain=' + domain : '')
	+ (secure ? '; secure' : '');
}

icfp.cookie.delcookie =  function(cookieName) {
	var expires = new Date();
	expires.setTime(expires.getTime() - 100);
	document.cookie = escape(cookieName) + '=' + escape('11')
	+ (expires ? '; expires=' + expires.toGMTString() : '');
}

icfp.cookie.getcookie = function(name) {
	var cookie_start = document.cookie.indexOf(name);
	var cookie_end = document.cookie.indexOf(";", cookie_start);
	return cookie_start == -1 ? '' : unescape(document.cookie.substring(cookie_start + name.length + 1, (cookie_end > cookie_start ? cookie_end : document.cookie.length)));
} 
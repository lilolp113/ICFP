var icfp = {};

icfp.cid = null;

icfp.roleid = null;

icfp.sysdate = null;

icfp.win =  parent || window;

icfp.isString = function (it) {
	return typeof it == "string" || it instanceof String;
};

icfp.isNumber = function (it) {
	return typeof it == "number" || it instanceof Number;
};

icfp.isArray = function (it) {
	return it && it instanceof Array || typeof it == "array";
};

icfp.isFunction = function(it){
	var _isFunction = function(it){
		return typeof it == "function" || it instanceof Function;
	};
	
};

icfp.isObject = function(it){
	return it !== undefined && (it === null || typeof it == "object" || icfp.isArray(it) || icfp.isFunction(it));
};

icfp.fromJson = function(/*String*/json){
	try {
		return eval("(" + json + ")");
	}catch(e){
		return json;
	}
};

icfp._escapeString = function (str) {
	return ("\"" + str.replace(/(["\\])/g, "\\$1") + "\"").replace(/[\f]/g, "\\f").replace(/[\b]/g, "\\b").replace(/[\n]/g, "\\n").replace(/[\t]/g, "\\t").replace(/[\r]/g, "\\r");
};

icfp.toJsonIndentStr = "\t";

icfp.toJson = function (it,prettyPrint,_indentStr) {
	_indentStr = _indentStr || "";
	var nextIndent = (prettyPrint ? _indentStr + icfp.toJsonIndentStr : "");
	var newLine = (prettyPrint ? "\n" : "");
	var objtype = typeof (it);
	if (objtype == "undefined") {
		return "undefined";
	} else if ((objtype == "number") || (objtype == "boolean")) {
		return it + "";
	} else if (it === null) {
		return "null";
	}
	if (icfp.isString(it)) {
		return icfp._escapeString(it);
	}
	if (it.nodeType && it.cloneNode) {
		return "";
	}
	var recurse = arguments.callee;
	var newObj;
	if (typeof it.__json__ == "function") {
		newObj = it.__json__();
		if (it !== newObj) {
			return recurse(newObj, prettyPrint, nextIndent);
		}
	}
	if (typeof it.json == "function") {
		newObj = it.json();
		if (it !== newObj) {
			return recurse(newObj, prettyPrint, nextIndent);
		}
	}
	if (icfp.isArray(it)) {
		var res = [];
		for(var i= 0;i <it.length;i=i+1) {
			var val = recurse(it[i], prettyPrint, nextIndent);
			if (typeof (val) != "string") {
				val = "undefined";
			}
			res.push(newLine + nextIndent + val);
		}
		return "[" + res.join(", ") + newLine + _indentStr + "]";
	}

	if (objtype == "function") {
		return null;
	}
	var output = [];
	for ( var key in it) {
		var keyStr;
		if (typeof (key) == "number") {
			keyStr = '"' + key + '"';
		} else if (typeof (key) == "string") {
			keyStr = icfp._escapeString(key);
		} else {
			continue;
		}
		val = recurse(it[key], prettyPrint, nextIndent);
		if (typeof (val) != "string") {
			continue;
		}
		output.push(newLine + nextIndent + keyStr + ": " + val);
	}
	return "{" + output.join(", ") + newLine + _indentStr + "}";
};

/**
 * 定义成功事件类型
 */
icfp.ResultEvent = 1;

/**
 * 失败事件类型
 */
icfp.FaultEvent = 0;

icfp.Action = {};

/**
 * 构建请求的URL地址
 */
icfp.Action.buidurl = (function(data){
	var _url = "";
	for(var _t in data.parameters){
		if(_t == "requestId"){
			_url+="&" + _t +"=" + encodeURI(data.parameters[_t]);
		}else if(_t == "bizId"){
			_url+="&" + _t +"=" + encodeURI(data.parameters[_t]);
		}
	} 
	if(data.url.lastIndexOf("?")>0){
		_url = data.url + _url;
	}else{
		_url = data.url + "?"+(_url==""?"":_url.substring(1));
	}
	return _url;
});

icfp.Action.statusCode = {
	ok:0,          //成功标志
	error:-1,      //系统错误
	syserror:100,  //业务错误
	timeout:200,   //session过期
	expired:-100   //注册码过期
};

icfp.Action.InitBackGrounp = function(){
	var bk = $('#__background___');
	if(bk.size()<1){
		$("body").append('<div id="__background___" style="display: block;width: 100%;height: 100%;'+
				'opacity: 0.4;filter: alpha(opacity = 40);background: #d0d0d0;position: absolute;top: 0;left: 0;'+
				'z-index: 2000;"></div><div id="__pageloading___" style="display: block;width: 148px;height: 28px;'+
				'position: fixed;top: 50%;left: 50%;margin-left: -74px;margin-top: -14px;padding: 10px 10px 10px 50px;'+
				'text-align: left;line-height: 27px;font-weight: bold;position: absolute;z-index: 2001;border: solid 2px #86a5ad;'+
				'font-size: 12px;background: #FFF url(icfp/ui/skins/default/images/loading.gif) no-repeat 10px 10px;">数据加载中，请稍等...</div>');
	}
}

icfp.Action.ShowBackGrounp = function(){
	icfp.Action.InitBackGrounp();
	$('#__background___').show();
	$('#__pageloading___').show();
}

icfp.Action.HideBackGrounp = function(){
	$('#__background___').hide();
	$('#__pageloading___').hide();
}

icfp.Action.ajaxError = (function(xhr, ajaxOptions, thrownError){
	var msg = "对不起，系统错误请联系管理员！Http status: " + xhr.status + " " + xhr.statusText;
	alert(msg);
});

icfp.Action.requestData = function(data,_dataCenter,showLoading){
	var requestURL = icfp.Action.buidurl(data);
	if(icfp.win.icfp.cid != null && icfp.win.icfp.cid != ''){
		_dataCenter.addParameter('userid',icfp.win.icfp.cid);
	}else{
		_dataCenter.addParameter('userid',"");
	}
	
	var injson = _dataCenter && _dataCenter.toJson?_dataCenter.toJson():String(_dataCenter || ""); 
	var indata = [{ name: 'json', value: injson }]
	for(var dd in data.parameters){
		if(dd!='requestId' || dd!='bizId'){
			indata.push({name:dd,value:data.parameters[dd]})
		}
	}
	var ajaxOpt = {
		type:"post",
        dataType:"json",
        url:requestURL,
        data:indata,
        async:data.sync,
        timeout:data.timeout,
        beforeSend:function(XMLHttpRequest){
			if(data.showLoading){
				icfp.Action.InitBackGrounp();
				//$('#s').show();
				//$('#__pageloading___').show();
				$('#__background___').show();
				$('#__pageloading___').show();
			}
		},
		success:function(json){
			var obj = json;
			var dc = new DataCenter(obj);
			if(obj.head.code==icfp.Action.statusCode.error){
				var ers = data.errors;
				if(ers.length>0){
					if(data.error){
						data.error(dc,null,null);
					}
				}else{
					if(obj.head.message ){
						icfp.win.icfp.msg.alert('error','系统错误',"对不起，系统错误请联系管理员！");
					}else{
						icfp.win.icfp.msg.alert('error','系统错误',"对不起，系统错误请联系管理员！");
					}
				}
			}else if(obj.head.code==icfp.Action.statusCode.syserror){
				var ers = data.errors;
				if(ers.length>0){
					if(data.error){
						data.error(dc,null,null);
					}
				}else{
					if(obj.head.message){
						icfp.win.icfp.msg.alert('error','业务错误',obj.head.message.detail);
					}else{
						icfp.win.icfp.msg.alert('error','业务错误',obj.head.message.detail);
					}
				}
			}else if(obj.head.code == icfp.Action.statusCode.timeout){
				if(obj.head.message ){
					icfp.win.icfp.msg.alert('success','登录错误提示','对不起，请求超时请重新登录！');
					icfp.win.icfp.LoginDialog();
				}else{
					icfp.win.icfp.msg.alert('success','登录错误提示','对不起，请求超时请重新登录！');
					icfp.win.icfp.LoginDialog();
				}
			}else if(obj.head.code == icfp.Action.statusCode.expired){
				if(obj.head.message ){
					icfp.win.icfp.msg.alert('warn','警告','许可证过期，请联系供应商更新！');
					icfp.win.icfp.LoginDialog();
				}else{
					icfp.win.icfp.msg.alert('warn','警告','许可证过期，请联系供应商更新！');
					icfp.win.icfp.LoginDialog();
				}
			}else if(obj.head.code == icfp.Action.statusCode.ok){
				if(data.load){
					if(data.callback){
						data.load(dc);
					}
				}
			}
		},
		error:function(xhr,ajaxOptions,thrownError){
			var ers = data.errors;
			if(ers.length>0){
				if(data.error){
					data.error(null,ajaxOptions,xhr);
				}
			}else{
				icfp.Action.ajaxError(xhr,ajaxOptions,thrownError);
			}
		},
        complete:function(XMLHttpRequest,status){  
			if(data.showLoading){
				//setTimeout(function(){
					$('#__background___').hide();
					$('#__pageloading___').hide();
				//},1000);
			}
        }
	};
	$.ajax(ajaxOpt);
};

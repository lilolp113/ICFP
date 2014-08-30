/* datacenter.js */
(function($) {
	DataCenter = function(dataCenter) {
		this._initData = (function() {
			this.head = {
				code : -100,
				message : {
					title : "",
					detail : ""
				}
			};
			this.parameters = {};
			this.dataStores = {};
			this._dataStores = {};
		});
		this._initData();
		if (dataCenter) {
			if (!dataCenter||(icfp.isString(dataCenter)&&!icfp.isObject(dataCenter=icfp.fromJson(dataCenter))))  {
				return;
			}
			if (dataCenter.head) {
				this.head.code = dataCenter.head.code;
				this.head.message.title = dataCenter.head.message.title;
				this.head.message.detail = dataCenter.head.message.detail;
			}
			if (dataCenter.body) {
				this.parameters = dataCenter.body.parameters;
			}
			var _d = dataCenter.body.dataStores;
			for (_dataStore in _d) {
				this.dataStores[_dataStore] = new DataStore(_dataStore,_d[_dataStore]);
				var _ds = new DataStore(_dataStore,_d[_dataStore]);
				var __ds = JSON.stringify(_ds)
				var ___ds = JSON.parse(__ds);
				this._dataStores[_dataStore] = ___ds;
			}
		}
		
		/**
		 * 取得服务器端返回的状态码
		 */
		this.getCode = (function() {
			return this.head.code;
		});
		
		/**
		 * 取得服务器端返回的状态信息标题
		 */
		this.getTitle = (function() {
			return this.head.message.title;
		});
		
		/**
		 * 取得服务器端返回的状态信息的详细描述
		 */
		this.getDetail = (function() {
			return this.head.message.detail;
		});

		/**
		 * 取得头部属性值
		 */
		this.getHeaderAttribute = (function(name) {
			return this.head[name];
		});
		
		/**
		 * 往头部添加属性信息
		 */
		this.addHeaderAttribute = (function(name, value) {
			this.head[name] = value;
		});
		
		/**
		 * 取得服务器端返回的某个参数值
		 */
		this.getParameter = (function(name) {
			var value = this.parameters[name];
			if (value != "undefined") {
				if (typeof value == "array" || value instanceof Array) {
					return value[0];
				}
				return value;
			}
		});
		
		/**
		 * 取得服务器端返回的某个参数信息
		 */
		this.getParameters = (function(name) {
			var value = this.parameters[name];
			if (value != "undefined") {
				if (typeof value == "array" || value instanceof Array) {
					return value;
				}
				return [ value ];
			}
		});
		
		/**
		 * 向DataCenter中添加参数信息
		 */
		this.addParameter = (function(name, value) {
			if (typeof this.parameters[name] == "array" || this.parameters[name] instanceof Array) {
				this.parameters[name].push(value);
			} else {
				this.parameters[name] = value;
			}
		});
		
		this.setParameter = (function(name, value) {
			this.parameters[name] = value;
		});
		
		/**
		 * 删除body.parameters中的变量
		 */
		this.removeParameter = (function(name) {
			delete this.parameters[name];
		});
		
		/**
		 * 取得服务器端返回的某个DataStore
		 */
		this.getDataStore = (function(name) {
			return this.dataStores[name];
		});
		
		/**
		 * 取得服务器端返回的某个原始DataStore
		 */
		this.getODataStore = (function(name) {
			var _ds = new DataStore('name',this._dataStores[name]);
			return _ds;
		});

		/**
		 * 取得所有DataStore对象
		 */
		this.getDataStores = (function() {
			return this.dataStores;
		});
		/**
		 * 获取指定datastore中的rowset对象
		 */
		this.getRowSet = (function(name) {
			return this.dataStores[name] ? this.dataStores[name].getRowSet() : null;
		});
		
		/**
		 * 向DataCenter中添加DataStore
		 */
		this.addDataStore = (function(name, dataStore,type) {
			if (icfp.isObject(arguments[0])) {
				arguments.length == 2 && (type = arguments[1]);
				dataStore = arguments[0];
				name = dataStore.getName();
			}
			dataStore.setName(name);
			type && dataStore.setType(type);
			this.dataStores[name] = dataStore;
		});

		/**
		 * 删除DataCenter中某DataStore
		 */
		this.removeDataStore = (function(name) {
			delete this.dataStores[name];
		});
		
		this.toJson = (function() {
			var store = [], body = [], result = [];
			result.push("{");
			result.push("head:");
			result.push(icfp.toJson(this.head));
			result.push(",");
			result.push("body:{");
			body.push("parameters:".concat(icfp.toJson(this.parameters)));
			for ( var dataStore in this.dataStores) {
				store.push(dataStore.concat(":").concat(
						this.dataStores[dataStore].toJson()));
			}
			body.push("dataStores:{".concat(store.join(",")).concat("}"));
			result.push(body.join(","));
			result.push("}}");
			return result.join("");
		});
	};
})(jQuery);


/* datastore.js */

(function($) {
	DataStore = function(name, dataStore) {
		// 初始化必要数据
		this._initData = (function() {
			/** DataStore的名称 */
			this.name = '';
			/** 请求编号 */
			this.bizId = '';
			/** 数据类型 */
			this.dataType = '';
			/** 总页数 */
			this.totalPage = 1;
			/** 页面显示数 */
			this.pageSize = 20;
			/** 当前页数 */
			this.pageNo = 1;
			/** 总记录数 */
			this.rowCount = 0;
			/** 排序语句 */
			this.ordering = [];
			/** 查询条件 */
			this.sql = '';
			/** 查询参数 */
			this.params = [];
			/**
			 * 数据集,目前采用二维数组实现 Object[][] rowSet =
			 * {{{status},{user}},{status,user},{}....};
			 */
			this.rowSet = [];
			//新加属性 2011-9-17
			this.metaData = null;
			//新加属性二级代码表 2012-8-22
			this.linkData = [];
			
			
		});
		this._initData();
		if (name && dataStore) {
			this.name = name;
			this.bizId = dataStore.bizId;
			this.dataType = dataStore.dataType;
			this.totalPage = dataStore.totalPage;
			this.pageSize = dataStore.pageSize;
			this.pageNo = dataStore.pageNo;
			this.rowCount = dataStore.rowCount;
			this.ordering = dataStore.ordering;
			this.sql = dataStore.sql;
			this.params = dataStore.params;
			this.rowSet = dataStore.rowSet;
		}

		this.setName = (function(name) {
			this.name = name;
		});
		this.getName = (function() {
			return this.name;
		});
		this.getBizId = (function() {
			return this.bizId;
		});
		this.setBizId = (function(bizId) {
			this.bizId = bizId;
		});

		this.getDataType = (function() {
			return this.dataType;
		});
		this.setDataType = (function(dataType) {
			this.dataType = dataType;
		});

		this.getTotalPage = (function() {
			return this.totalPage;
		});
		this.setTotalPage = (function(totalPage) {
			this.totalPage = totalPage;
		});

		this.getPageSize = (function() {
			return this.pageSize;
		});
		this.setPageSize = (function(pageSize) {
			this.pageSize = pageSize;
		});

		this.getPageNo = (function() {
			return this.pageNo;
		});
		this.setPageNo = (function(pageNo) {
			this.pageNo = pageNo;
		});

		this.getRowCount = (function() {
			return this.rowCount;
		});
		this.setRowCount = (function(rowCount) {
			this.rowCount = rowCount;
		});

		this.getOrdering = (function() {
			return this.ordering;
		});
		this.setOrdering = (function(ordering) {
			if(ordering==null){
				ordering=[];
			}
			this.ordering = ordering;
		});

		this.getSql = (function() {
			return this.sql;
		});
		this.setSql = (function(sql) {
			this.sql = sql;
		});

		this.getParams = (function() {
			return this.params;
		});
		
		this.setParams = (function(params) {
			if(params==null){
				params=[];
			}
			this.params = params;
		});
		
		/* 获取某一个参数 方法 */
		this.getParamsIndex = (function(i) {
			if (this.params.length > i) {
				return this.params[i];
			} else {
				return null;
			}
		});

		this.getRowSet = (function() {
			return this.rowSet;
		});
		
		this.setRowSet = (function(rowSet) {
			if(rowSet==null){
				rowSet=[];
			}
			this.rowSet = rowSet;
		});
		
		this.getRowSetIndex = (function(i) {
			if (this.rowSet.length > i) {
				return this.rowSet[i];
			} else {
				return null;
			}
		});
		
		//2012-8-22
		
		this.getlinkData = (function(){
			return this.linkData;
		});
		
		this.setlinkData = (function(linkdata){
			if(linkdata==null){
				linkdata=[];
			}
			this.linkData = linkdata;
		});
		//2012-8-22
		/**
		 * 添加查询值
		 */
		this.addParameter = (function(dataType,dataValue) {
			(this.params?this.params:(this.params=[])).push({type:dataType,value:dataValue});
		});
		
		/**
		 * 将DataStore对象转化为标准数据格式
		 */
		this.toData = (function() {
			var data = {};
			data["bizId"] = this.bizId;
			data["dataType"] = this.dataType;
			data["totalPage"] = this.totalPage;
			data["pageSize"] = this.pageSize;
			data["pageNo"] = this.pageNo;
			data["rowCount"] = this.rowCount;
			data["ordering"] = this.ordering;
			data["sql"] = this.sql;
			data["params"] = this.params;
			data["rowSet"] = this.rowSet;
			//2012-8-22
			data["linkData"] = this.linkData;
			//end 2012-8-22
			return data;
		});
		this.toJson = (function() {
			var result = [];
			result.push("bizId:\"".concat(this.bizId).concat("\""));
			result.push("dataType:\"".concat(this.dataType).concat("\""));
			result.push("totalPage:".concat(this.totalPage));
			result.push("pageSize:".concat(this.pageSize));
			result.push("pageNo:".concat(this.pageNo));
			result.push("rowCount:".concat(this.rowCount));
			result.push("ordering:\"".concat(this.ordering).concat("\""));
			result.push("sql:\"".concat(this.sql).concat("\""));
			result.push("params:".concat(icfp.toJson(this.params)));
			result.push("rowSet:".concat(icfp.toJson(this.rowSet)));
			//2012-8-22
			result.push("linkData:".concat(icfp.toJson(this.linkData)));
			//end 2012-8-22
			return "{".concat(result.join(",").concat("}"));
		});
	};
})(jQuery);

/* httpservcie.js */

(function($) {
	HttpService = function() {
		this._initData = (function() {
			this.dataCenter = new DataCenter();
			this.action = "CoreAction";
			this.queryMethod = "query";
			this.submitMethod = "submit";
			this.callback = [];
			this.errors = [];
			this.parameters = {};
			this.timeout = 3600 * 1000; // 1小时
			this.dataOption = null;
			this.sync = false;
			this.showLoading = true;
			this.pattern = {
				parameters : "all",
				dataStores : "auto"
			};
			this.uploadForm = null;
			// 业务复核datcenter
			this.si_UI_SAVE_TAG_Data = new DataCenter();
		});
		this._initData();
		this.putParameter = (function(key, value) {
			this.dataCenter.addParameter(key, value);
		});

		/**
		 * 设置该http次请求的参数
		 */
		this.putHttpParameter = (function(key, value) {
			this.parameters[key] = value;
		});
		
		/**
		 * 添加DataStore对象 1
		 */
		this.putDataStore = (function(name, store) {
			if (arguments.length == 1) {
				this.dataCenter.addDataStore(arguments[0]);
			} else {
				this.dataCenter.addDataStore(name, store);
			}
		});
		
		this.send = (function(params) {
			this._request(this.action, this.queryMethod, params);
		});
		
		/**
		 * 向服务器端提交数据，类似于http post请求
		 */
		
		this.post = (function(params) {
			this._request(this.action, this.submitMethod, params);
		});
		
		this._request = (function(actionName, methodName, params) {
			this.url = actionName + ".action?method=" + methodName;
			for ( var _s in this.dataCenter.dataStores) {
				var ds = this.dataCenter.dataStores[_s];
				if (!ds.getName() || ds.getName() == "" ) {
					alert("缺少dataStoreName或dataSetName");
					return;
				}
			}
			icfp.Action.requestData(this,this.dataCenter,this.showLoading);
		});
		
		/**
		 * 设置同步异步请求
		 */
		this.setSync = (function(sync) {
			this.sync = sync;
		});
		
		/**
		 * 设置向服务器端发送请求的url信息
		 */
		
		this.setRequestUrl = (function(action, queryMethod) {
			this.action = action;
			this.queryMethod = queryMethod;
		});
		
		/**
		 * 设置向服务器端发送DataStore中Row的选项
		 */
		this.setDataOption = (function(dataOption) {
			this.pattern["dataStores"] = dataOption;
		});
		
		/**
		 * 设置个别DataStore的数据传输状态
		 * 
		 */
		this.setDataStoreOption = (function(name, dataOption) {
			if (typeof (this.pattern["dataStores"]) == "string") {
				this.pattern["dataStores"] = {};
			}
			this.pattern["dataStores"][name] = dataOption;
		});

		this.setDataExclude = (function(data) {
			this.pattern["exclude"] = data;
		});
		
		/**
		 * 设置向服务器端提交数据的url信息
		 */
		this.setPostUrl = (function(action, submitMethod) {
			this.action = action;
			this.submitMethod = submitMethod;
		});

		/**
		 * 设置该http请求成功后的处理函数
		 */
		this.setCallback = (function(fun) {
			this.callback.push(fun);
		});

		this.load = (function(dc) {
			for ( var i = 0; i < this.callback.length; i++) {
				this.callback[i].call(null, dc, null);
			}
		});

		/**
		 * 设置该http请求失败后的处理函数
		 */
		this.setErrorCallback = (function(fun) {
			this.errors.push(fun);
		});

		this.error = (function(text, xhr) {
			for ( var i = 0; i < this.errors.length; i++) {
				this.errors[i].call(this, text, xhr);
			}
		});

		/**
		 * 添加http请求后的处理函数
		 */
		this.addEventListener = (function(type, resultEventListener) {
			if (icfp.ResultEvent == type) {
				this.setCallback(resultEventListener);
			} else {
				this.setErrorCallback(resultEventListener);
			}
		});

		/**
		 * 设置当前http请求的ID标识
		 */
		this.putBusinessRequestId = (function(value) {
			this.putHttpParameter("requestId", value);
		});

		this.putBusinessID = (function(value) {
			this.putHttpParameter("bizId", value);
		});

		/**
		 * 设置http请求的超时时间
		 */
		this.setTimeout = (function(timeout) {
			this.timeout = timeout;
		});

		/**
		 * 获取当前http请求的超时时间
		 */
		this.getTimeout = (function() {
			return this.timeout;
		});
		
		/**
		 * 设置是否显示进度条
		 */
		this.setShowLoading = (function(loading) {
			this.showLoading = loading;

		});
		
		/**
		 * 获取当前是否显示进度条
		 */
		this.getShowLoading = (function() {
			return this.showLoading;
		});
	};

})(jQuery);



(function($) {

	/**
	 * 扩展String方法
	 */
	$.extend(String.prototype, {
		isPositiveInteger:function(){
			return (new RegExp(/^[1-9]\d*$/).test(this));
		},
		isInteger:function(){
			return (new RegExp(/^\d+$/).test(this));
		},
		isNumber: function(value, element) {
			return (new RegExp(/^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/).test(this));
		},
		trim:function(){
			return this.replace(/(^\s*)|(\s*$)|\r|\n/g, "");
		},
		trans:function() {
			return this.replace(/&lt;/g, '<').replace(/&gt;/g,'>').replace(/&quot;/g, '"');
		},
		replaceAll:function(os, ns) {
			return this.replace(new RegExp(os,"gm"),ns);
		},
		replaceTm:function($data) {
			if (!$data) return this;
			return this.replace(RegExp("({[A-Za-z_]+[A-Za-z0-9_]*})","g"), function($1){
				return $data[$1.replace(/[{}]+/g, "")];
			});
		},
		replaceTmById:function(_box) {
			var $parent = _box || $(document);
			return this.replace(RegExp("({[A-Za-z_]+[A-Za-z0-9_]*})","g"), function($1){
				var $input = $parent.find("#"+$1.replace(/[{}]+/g, ""));
				return $input.size() > 0 ? $input.val() : $1;
			});
		},
		isFinishedTm:function(){
			return !(new RegExp("{[A-Za-z_]+[A-Za-z0-9_]*}").test(this)); 
		},
		skipChar:function(ch) {
			if (!this || this.length===0) {return '';}
			if (this.charAt(0)===ch) {return this.substring(1).skipChar(ch);}
			return this;
		},
		isValidPwd:function() {
			return (new RegExp(/^([_]|[a-zA-Z0-9]){6,32}$/).test(this)); 
		},
		isValidMail:function(){
			return(new RegExp(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/).test(this.trim()));
		},
		isSpaces:function() {
			for(var i=0; i<this.length; i+=1) {
				var ch = this.charAt(i);
				if (ch!=' '&& ch!="\n" && ch!="\t" && ch!="\r") {return false;}
			}
			return true;
		},
		isPhone:function() {
			return (new RegExp(/(^([0-9]{3,4}[-])?\d{3,8}(-\d{1,6})?$)|(^\([0-9]{3,4}\)\d{3,8}(\(\d{1,6}\))?$)|(^\d{3,8}$)/).test(this));
		},
		isUrl:function(){
			return (new RegExp(/^[a-zA-z]+:\/\/([a-zA-Z0-9\-\.]+)([-\w .\/?%&=:]*)$/).test(this));
		},
		isExternalUrl:function(){
			return this.isUrl() && this.indexOf("://"+document.domain) == -1;
		}
	});
})(jQuery);

$.fn.noSelect = function(p) {
	if(p == null){
		prevent = true;
	}else{
		prevent = p;
	}
	if(prevent){
		return this.each(function(){
			if ($.browser.msie||$.browser.safari){
				$(this).bind('selectstart',function(){
					return false;
				});
			}else if($.browser.mozilla){
				$(this).css('MozUserSelect','none');
				$('body').trigger('focus');
			}else if ($.browser.opera){
				$(this).bind('mousedown',function(){
					return false;
				});
			}else{
				$(this).attr('unselectable','on');
			}
		});
	}else{
		return this.each(function(){
			if($.browser.msie||$.browser.safari){
				$(this).unbind('selectstart');
			}else if ($.browser.mozilla){
				$(this).css('MozUserSelect','inherit');
			}else if ($.browser.opera){
				$(this).unbind('mousedown');
			}else{
				$(this).removeAttr('unselectable','on');
			}
		});
	}
};

/** 
 * You can use this map like this:
 * var myMap = new Map();
 * myMap.put("key","value");
 * var key = myMap.get("key");
 * myMap.remove("key");
 */
function Map(){

	this.elements = new Array();
	
	this.size = function(){
		return this.elements.length;
	};
	
	this.isEmpty = function(){
		return (this.elements.length < 1);
	};
	
	this.clear = function(){
		this.elements = new Array();
	};
	
	this.put = function(_key, _value){
		this.remove(_key);
		this.elements.push({key: _key, value: _value});
	};
	
	this.remove = function(_key){
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					this.elements.splice(i, 1);
					return true;
				}
			}
		} catch (e) {
			return false;
		}
		return false;
	};
	
	this.get = function(_key){
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) { return this.elements[i].value; }
			}
		} catch (e) {
			return null;
		}
	};
	
	this.element = function(_index){
		if (_index < 0 || _index >= this.elements.length) { return null; }
		return this.elements[_index];
	};
	
	this.containsKey = function(_key){
		try {
			for (i = 0; i < this.elements.length; i++) {
				if (this.elements[i].key == _key) {
					return true;
				}
			}
		} catch (e) {
			return false;
		}
		return false;
	};
	
	this.values = function(){
		var arr = new Array();
		for (i = 0; i < this.elements.length; i++) {
			arr.push(this.elements[i].value);
		}
		return arr;
	};
	
	this.keys = function(){
		var arr = new Array();
		for (i = 0; i < this.elements.length; i++) {
			arr.push(this.elements[i].key);
		}
		return arr;
	};
}


//扩展数组
Array.prototype.remove=function(dx){
	if(isNaN(dx)||dx>this.length){
		return false;
	}
	for(var i=0,n=0;i<this.length;i++){
		if(this[i]!=this[dx]){
			this[n++]=this[i];
		}
	}
	this.length-=1
}


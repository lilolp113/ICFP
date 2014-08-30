icfp.changeBG = function(url){
	if(url && url!=null && url !=''){
		$('#bg img').attr('src',url);
	}
}

//msg--------------------------------------------------------------
icfp.msg = {};

icfp.msg.alertSuccess = function(msg,callback){
	art.dialog({
		lock: true,
	    background: '#000', // 背景色
	    opacity: 0.1,	// 透明度
	    content: msg,
	    width:200,
	    height:45,
	    bingX:false,
	    icon: 'succeed'
	},callback);
};

icfp.msg.alertError = function(msg,callback){
	art.dialog({
		lock: true,
	    background: '#000', // 背景色
	    opacity: 0.1,	// 透明度
	    content: msg,
	    width:200,
	    height:45,
	    icon: 'error'
	},callback);
};
	
icfp.msg.alertWarn = function(msg,callback){
	art.dialog({
		lock: true,
	    background: '#000', // 背景色
	    opacity: 0.1,	// 透明度
	    content: msg,
	    width:200,
	    height:45,
	    icon: 'warning'
	},callback);
};
	
icfp.msg.alertQuestion = function(msg,callback){
	art.dialog({
		lock: true,
	    background: '#000', // 背景色
	    opacity: 0.1,	// 透明度
	    content: msg,
	    width:200,
	    height:45,
	    icon: 'question'
	},callback);
};

icfp.msg.success = function(title,msg){
	art.dialog({
		title:title,
		lock: true,
	    background: '#000', // 背景色
	    opacity: 0.1,	// 透明度
	    content: msg,
	    width:200,
	    height:45,
	    icon: 'succeed'
	});
};
	
icfp.msg.error = function(title,msg){
	art.dialog({
		title:title,
		lock: true,
	    background: '#000', // 背景色
	    opacity: 0.1,	// 透明度
	    content: msg,
	    width:200,
	    height:45,
	    icon: 'error'
	});
};
	
icfp.msg.warn = function(title,msg){
	art.dialog({
		title:title,
		lock: true,
	    background: '#000', // 背景色
	    opacity: 0.1,	// 透明度
	    content: msg,
	    width:200,
	    height:45,
	    icon: 'warning'
	});
};
	
icfp.msg.question = function(title,msg){
	art.dialog({
		title:title,
		lock: true,
	    background: '#000', // 背景色
	    opacity: 0.1,	// 透明度
	    content: msg,
	    width:200,
	    height:45,
	    icon: 'question'
	});
};

icfp.msg.confirm = function(msg,fun){
	art.dialog({
		content:msg,
		lock: true,
	    background: '#000', // 背景色
	    opacity: 0.1,	// 透明度
	    width:200,
		height:45,
	    okVal: '是',
	    cancelVal:'否',
	    ok:fun,
	    cancel: true
	});
};

icfp.msg.tip = function(title,msg,type,width,time){
	var icon; 
	if(type){
		switch (type) {
		case 'error':{
			icon = 'error';
			break;
		}
		case 'sad':{
			icon = 'face-sad';
			break;
		}
		case 'smile':{
			icon = 'face-smile';
			break;
		}
		case 'question':{
			icon = 'question';
			break;
		}
		case 'succeed':{
			icon = 'succeed';
			break;
		}
		case 'warning':{
			icon = 'warning';
			break;
		}
		default:{
			icon = 'face-smile';
			break;
		}
	}
	}else{
		icon = 'face-smile';
	}
	art.dialog.notice({
	    title: title,
	    width: width?width:220,// 必须指定一个像素宽度值或者百分比，否则浏览器窗口改变可能导致artDialog收缩
	    content: msg,
	    icon:icon,
	    time: time?time:5
	});
};

//dialog--------------------------------------------------------------
icfp.dialog = {};

icfp.dialog.data = [];

icfp.dialog.getDialogs = function(){
	return art.dialog.list;
};

icfp.dialog.open = function(id,title,width,height,url,ok,cancel,data,urldate){
	if(data){
		var oo = {};
		oo.id = id;
		oo.data = data;
		icfp.dialog.data.push(oo);
	}else{
		icfp.dialog.delDialogData(id);
	}
	if(url.indexOf('REQ_S')==0){
		if(urldate && urldate != ''){
			url = "CoreAction.do?requestId="+url+"&mode=1&userid=" + icfp.win.icfp.cid + "&"+urldate;
		}else{
			url = "CoreAction.do?requestId="+url+"&mode=1&userid=" + icfp.win.icfp.cid;
		}
	}
	art.dialog({ 
		id:id,
		title:title,
		width:width, 
		height:height,
		lock:true,
		opacity: 0.1,
		padding:'0px 0px',
		content:"<iframe id='"+id+"' name='"+id+"' frameborder='0' src='"+url+"' width='"+width+"' height='"+height+"'></iframe>", 
		button: [
		    {name: '确定',callback:function(obj){
		    	var re = ok(id,obj);
		    	return re;
		    },focus:true},
		    {name: '放弃',callback:cancel }
		]
	});
	
};


icfp.dialog.opend = function(id,title,width,height,url,data,urldate){
	if(data){
		var oo = {};
		oo.id = id;
		oo.data = data;
		icfp.dialog.data.push(oo);
	}else{
		icfp.dialog.delDialogData(id);
	}
	if(url.indexOf('REQ_S')==0){
		if(urldate && urldate != ''){
			url = "CoreAction.do?requestId="+url+"&mode=1&userid=" + icfp.win.icfp.cid + "&"+urldate;
		}else{
			url = "CoreAction.do?requestId="+url+"&mode=1&userid=" + icfp.win.icfp.cid;
		}
	}
	var opt = { 
		id:id,
		title:title,
		width:width,
		height:height,
		lock:true,
		opacity: 0.1,
		padding:'0px 0px',
		content:"<iframe id='"+id+"' name='"+id+"' frameborder='0' src='"+url+"' width='"+width+"' height='"+height+"'></iframe>"
	};
	art.dialog.dhDialog(opt);
};

icfp.dialog.getDialogByID = function(id){
	var dialog = null;
	if(id && id!=''){
		var dds = icfp.dialog.getDialogs();
		$.each(dds,function(index){
			var dd = dds[index];
			if(dd.config.id == id){
				dialog = dd;
				return;
			}
		});
		if(dialog!=null){
			return dialog;
		}else{
			return false;
		}
	}else{
		return false;
	}
};

icfp.dialog.closeDialogByID = function(id){
	var dialog = icfp.dialog.getDialogs();
	if(dialog){
		$.each(dialog,function(index){
			if(index == id){
				var dd = dialog[index];
				dd.close();
			}
		});

	}
};

icfp.dialog.closeDialogs = function(id){
	var dialog = icfp.dialog.getDialogByID(id);
	$.each(dialog,function(index){
		var dd = dialog[index];
		dd.close();
	});
};

icfp.dialog.getDialogData = function(id){
	var ddatas = icfp.dialog.data;
	var redata = null;
	if(ddatas!=[] && ddatas.length>0){
		$.each(ddatas,function(index,data){
			if(data.id == id){
				redata = data.data;
				return;
			}
		});
		if(redata!=null){
			return redata;
		}else{
			return false;
		}
	}else{
		return false;
	}
};

icfp.dialog.delDialogData = function(id){
	var ddatas = icfp.dialog.data;
	var redata = [];
	if(ddatas!=[] && ddatas.length>0){
		$.each(ddatas,function(index,data){
			if(data.id != id){
				redata.push(data);
			}
		});
		icfp.dialog.data = redata;
	}
};

//form--------------------------------------------------------------

icfp.form = {};

icfp.form.setComboBox = function(){
	$.each($('select'),function(selectindex,select) {
		var selectid = select.id;
		if(selectid && selectid.indexOf('_')!=-1){
			var scode = $('#'+selectid).attr('scode');
			if(selectid && !scode){
				var vv = $("#"+selectid+"").val();
				var selectids = selectid.split('_');
				var colname = selectids[1];
				if(vv && vv!='' ){
					var cdates = AA02code[colname];
					if(cdates){
						$("#"+selectid+"").empty();
	    				var oop = "<option value=''>--请选择--</option>";
	    				$(oop).appendTo("#"+selectid+"");
						$.each(cdates,function(k,v){
							if(k!='a'){
								if(vv == k){
									op="<option value='"+k+"' selected='selected'>"+v+"</option>";
								}else{
									op="<option value='"+k+"'>"+v+"</option>";
								}
								$(op).appendTo("#"+selectid+"");
							}
						});
					}
				}else{
					var cdates = AA02code[colname];
					if(cdates){
						$("#"+selectid+"").empty();
	    				var oop = "<option value='' selected='selected'>--请选择--</option>";
	    				$(oop).appendTo("#"+selectid+"");
						$.each(cdates,function(k,v){
							if(k!='a'){
								op="<option value='"+k+"'>"+v+"</option>";
	    						$(op).appendTo("#"+selectid+"");
							}
						});
					}
				}
			}
		}
	});
};

icfp.form.getformData = function(id,table,status){
	var $form = $('#'+id);
	if(status=='insert' || status=='update' || status=='delete'){
		var formdatas = $form.serializeArray();
		var rowsets = [];
		var row = {};
		$.each(formdatas ,function(index, formdata) {
			var tablename=formdata.name;
			var yy = tablename;
			var propoty=formdata.name;
			var pp = propoty.split('_');
			if(tablename.length>3){
				tablename=tablename.substring(0,4);
				propoty=propoty.substring(pp[0].length+1,propoty.length);
				if(table==tablename){
					if(yy.length < 12){
						var vvalue = $('#'+yy+'').val(); 
						if(!vvalue){
							vvalue = '';
						}
						row[propoty] = vvalue;
					}
				}
			}
		});
		var disableds = $form.find('input:disabled,select:disabled,textarea:disabled').not("input[type=button]");
		$.each(disableds,function(index, formdata) {
			var tablename = $(formdata).attr("name");
			var yy = tablename;
			var propoty = $(formdata).attr("name");
			var pp = propoty.split('_');
			if(tablename.length>3){
				tablename=tablename.substring(0,4);
				propoty=propoty.substring(pp[0].length+1,propoty.length);
				if(table==tablename){
					if(yy.length < 12){
						var vvalue = $('#'+yy+'').val(); 
						if(!vvalue){
							vvalue = '';
						}
						row[propoty] = vvalue;
					}
				}
			}
		});
		rowsets.push({'status':status,'cell':row});
		return rowsets;
	}else{
		return false
	}
};

icfp.form.getformDatas = function(id,table,status){
	var $form = $('#'+id);
	if(status=='insert' || status=='update' || status=='delete'){
		var formdatas = $form.serializeArray();
		var rowsets = [];
		var row = {};
		$.each(formdatas ,function(index, formdata) {
			var tablename=formdata.name;
			var yy = tablename;
			var propoty=formdata.name;
			var pp = propoty.split('_');
			if(tablename.length>3){
				tablename=tablename.substring(0,4);
				propoty=propoty.substring(pp[0].length+1,propoty.length);
				if(table==tablename){
					if(yy.length < 12){
						var vvalue = $('#'+yy+'').val(); 
						if(!vvalue){
							vvalue = '';
						}
						row[propoty] = vvalue;
					}
				}
			}
		});
		var disableds = $form.find('input:disabled,select:disabled,textarea:disabled').not("input[type=button]");
		$.each(disableds,function(index, formdata) {
			var tablename = $(formdata).attr("name");
			var yy = tablename;
			var propoty = $(formdata).attr("name");
			var pp = propoty.split('_');
			if(tablename.length>3){
				tablename=tablename.substring(0,4);
				propoty=propoty.substring(pp[0].length+1,propoty.length);
				if(table==tablename){
					if(yy.length < 12){
						var vvalue = $('#'+yy+'').val(); 
						if(!vvalue){
							vvalue = '';
						}
						row[propoty] = vvalue;
					}
				}
			}
		});
		rowsets.push({'status':status,'cell':row});
		return rowsets;
	}else{
		return false
	}
};


/*
 *row {'':'',cell:{'':'',....}}
 * */
icfp.form.setformData = function(id,table,row){
	var $form = $('#'+id+'');
	var formdatas = $form.serializeArray();
	if(row && row.cell){
		$.each(formdatas ,function(index, formdata) {
    		var ppname = formdata.name;
    		var tablename = formdata.name;
    		var propoty=formdata.name;
    		var pp = propoty.split('_');
    		tablename=tablename.substring(0,4);
    		propoty=propoty.substring(pp[0].length+1,propoty.length);
    		if(table==tablename){
    			$form.find('#'+ ppname +'').val('');
    			var vv = row.cell[propoty];
    			if(typeof(vv) == "undefined"){
    				vv = '';
    			}else if(typeof(vv) == 'number'){
    				vv = vv + '';
    			}else if(typeof(vv) == 'string'){
    				vv = vv;
    			}else{
    				vv = '';
    			}
    			//alert(vv.trim());
    			//$form.find('#'+ ppname +'').val(vv.trim());
    			$form.find('#'+ ppname +'').val(vv);
    		}
    	});
		$.each($('#'+id+' select'),function(index, sselect) {
			var ppname = sselect.id;
    		var tablename = sselect.id;
    		var propoty = sselect.id;
    		var pp = propoty.split('_');
    		tablename=tablename.substring(0,4);
    		propoty=propoty.substring(pp[0].length+1,propoty.length);
    		if(table==tablename){
    			var vv = row.cell[propoty];
    			if(typeof(vv) == "undefined"){
    				vv = '';
    			}else if(typeof(vv) == 'number'){
    				vv = vv + '';
    			}else if(typeof(vv) == 'string'){
    				vv = vv;
    			}else{
    				vv = '';
    			}
    			$form.find('#'+ppname+'').attr("value",vv.trim());
    		}
		});
	}
}

icfp.form.rememberValue = function(id){
	$("#"+id+" input, #"+id+" textarea, #"+id+" select").each(function(){ 
		$(this).attr('_value',$(this).val()); 
	});
}

icfp.form.isFormChanged = function(id){
	var is_changed = false;
	$("#"+id+" input, #"+id+" textarea, #"+id+" select").each(function(){
		var _v = $(this).attr('_value');
		if(typeof(_v) == 'undefined'){
			 _v = '';
		}
		if(_v != $(this).val()){ 
			is_changed = true;
			return;
		}
	});
	return is_changed; 
}


icfp.form.LimitLength = function(obj){
	$('input,textarea',obj).each(function(){
		var validate = $(this).attr('validate');
		if(validate){
			var json = eval('(' + validate + ')');
			if(json && json.maxlength!=null && json.maxlength!=''){
				var len = Number(json.maxlength);
				$(this).keyup(function(){
					var value = $(this).val();
					if(value.replace(/[^\x00-\xff]/g,'**').length>len){
						$(this).val(icfp.form.__substr(value,len));
					}
				}).keydown(function(){
					var value = $(this).val();
					if(value.replace(/[^\x00-\xff]/g,'**').length>len){
						$(this).val(icfp.form.__substr(value,len));
					}
				}).blur(function(){
					var value = $(this).val();
					if(value.replace(/[^\x00-\xff]/g,'**').length>len){
						$(this).val(icfp.form.__substr(value,len));
					}
				});
			}
		}
	});
}

icfp.form.__substr = function(str,len){
	if(!str || !len){ 
		return ''; 
	}
	//预期计数：中文2字节，英文1字节
	var a = 0;
	//循环计数
	var i = 0;
	//临时字串
	var temp = '';
	for(i=0;i<str.length;i++){
		if(str.charCodeAt(i)>255){
			//按照预期计数增加2
			a+=2;
		}else{
			a++;
		}
		//如果增加计数后长度大于限定长度，就直接返回临时字符串
		if(a > len){ 
			return temp; 
		}
		//将当前内容加到临时字符串
		temp += str.charAt(i);
	}
	//如果全部是单字节字符，就直接返回源字符串
	return str;
}

/*
 * 合并row 以grow为主 
 * 
 * */
icfp.form.exformDate = function(grow,erow){
	var row = [];
	grow.status = erow.status;
	if(erow.cell){
		$.each(erow.cell,function(k,v){
			if(grow.cell[k] != erow.cell[k]){
				grow.cell[k] = erow.cell[k];
			}
		});
		row.push(grow);
	}
	return row;
};

/*row 比较数据是否发生更改*/

icfp.form.ischangeformDate = function(grow,erow){
	var ischanged = false;
	if(erow.cell && grow.cell){
		$.each(erow.cell,function(k,v){
			if(grow.cell[k] != erow.cell[k]){
				ischanged = true;
				return;
			}
		});
	}
	return ischanged;
};


icfp.form.disableform = function(id,type){
	$(':input','#'+id).not(':button, :submit, :reset, :hidden').attr('disabled',type);
	$('select','#'+id).not(':hidden').attr('disabled',type);
	$('textarea','#'+id).not(':hidden').attr('disabled',type);
};

//frame--------------------------------------------------------------

icfp.frame = {};

icfp.frame.data = null;

icfp.frame.changeurl = function(url,data,urldate){
	var targetIframe = icfp.win.document.getElementById("mainframe");
	if(data){
		icfp.frame.data = data;
	}
	var rr =  Math.random()*10000;
	if (!/^#/.test(url)) {
		if(url.indexOf('REQ_S')==0){
			if(urldate && urldate !=''){
				targetIframe.src = "CoreAction.do?requestId="+url+"&mode=1&userid=" + icfp.win.icfp.cid + '&' + icfp.buidurl(urldate) +'&rr = '+rr;;
			}else{
				targetIframe.src = "CoreAction.do?requestId="+url+"&mode=1&userid=" + icfp.win.icfp.cid +'&rr = '+rr;;
			}
		}else{
			targetIframe.src = url;
		}
	}
}


//tree--------------------------------------------------------------
icfp.tree = {};

icfp.tree.formatData = function(id,rowsets,idkey,pidkey,childkey){
	var temp = [];
	if(rowsets){
		if(rowsets.length>0){
			$.each(rowsets,function(index,rowset){
				if(rowset.cell[pidkey] == id){
					var _temp = icfp.tree.formatData(rowset.cell[idkey],rowsets,idkey,pidkey,childkey);
					if(_temp.length>0){
						rowset[childkey] = _temp;
						temp.push(rowset);
					}else{
						temp.push(rowset);
					}
				}
			});
		}
	}
	return temp;
}

icfp.tree.__formatData = function(id,rowsets,idkey,pidkey,childkey){
	var temp = [];
	if(rowsets){
		if(rowsets.length>0){
			$.each(rowsets,function(index,rowset){
				if(rowset[pidkey] == id){
					var _temp = icfp.tree.__formatData(rowset[idkey],rowsets,idkey,pidkey,childkey);
					if(_temp.length>0){
						rowset[childkey] = _temp;
						temp.push(rowset);
					}else{
						temp.push(rowset);
					}
				}
			});
		}
	}
	return temp;
}

icfp.tree.getFontCss = function(treeId, treeNode){
	return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
};


icfp.tree.setTree = function(id,dc,opt,onclick,checkbox){
	if(dc.getCode() == icfp.Action.statusCode.ok){
		var treeds = dc.getDataStore(id);
		var rowsets = treeds.getRowSet();
		var zNodes = [];
		$.each(rowsets ,function(index, rowset) {
			var row = {};
			if(opt.id){
				var id = rowset.cell[opt.id];
				row.id = id; 
			}
			if(opt.pid){
				var pid = rowset.cell[opt.pid];
				row.pId = pid; 
			}
			if(opt.name){
				var name = rowset.cell[opt.name];
				row.name = name; 
			}
			if(opt.url){
				var url = rowset.cell[opt.url];
				if(url==''||url==null){
					row.open = true; 
				}else{
					row.url = url; 
				}
			}
			zNodes.push(row);
		});
		var setting = {
			data: {
				simpleData: {
					enable: true
				}
			},
			check: {
				enable: checkbox?checkbox:false
			},
			callback: {
				onClick: onclick?onclick:null
			},
			view: {
				fontCss: icfp.tree.getFontCss
			}

		};
		$.fn.zTree.init($("#"+id+""),setting,zNodes);
	}
}

icfp.tree.expandAll = function(id,type){
	var treeObj = $.fn.zTree.getZTreeObj(id);
	if(!type){
		treeObj.expandAll(true);
	}else{
		treeObj.expandAll(false);
	}
};
//menu--------------------------------------------------------------
icfp.menu = {};

icfp.menu.getMenus = function(roleid){
	var munus = null;
	if(icfp.win.icfp.roleid == null){
		return munus;
	}
	if(roleid && roleid != ''){
		$.each(__MenuList,function(index,menu){
			if(menu['roleid'] == roleid){
				munus = menu['menus'];
				return;
			}
		});
	}else{
		$.each(__MenuList,function(index,menu){
			if(menu['roleid'] == icfp.win.icfp.roleid){
				munus = menu['menus'];
				return;
			}
		});
	}
	return munus;
}


//achievement

icfp.achievement = {};

icfp.achievement.show = function(dc){
	var score = dc.getParameter('score');
	if(score!=0){
		var pwidth = $(icfp.win.document).width();
		var pheight = $(icfp.win.document).height();
		var left = (pwidth - 260)*0.5;
		var top = (pheight - 56)*0.5;
		setTimeout(function(){
			$('.popupcredit .pc_inner').html("恭喜您！积分+" + score);
			$('.popupcredit').css({'top':0,'left':left}).show().animate({'top':top},600,'easeInOutBounce');
		},200);
		setTimeout(function(){
			$('.popupcredit').animate({'top':-56},600,function(){
				$('.popupcredit').hide();
			});
		},3000);
		
	}
	var upgrade = dc.getParameter('upgrade');
	if(upgrade!=0){
		var pwidth = $(icfp.win.document).width();
		var pheight = $(icfp.win.document).height();
		var left = (pwidth - 260)*0.5;
		var top = (pheight - 56)*0.5;
		setTimeout(function(){
			$('.popupcredit .pc_inner').html("恭喜您！升到 " + upgrade + " 级啦！");
			$('.popupcredit').css({'top':0,'left':left}).show().animate({'top':top},600,'easeInOutBounce');
		},200);
		setTimeout(function(){
			$('.popupcredit').animate({'top':-56},600,function(){
				$('.popupcredit').hide();
			});
		},3000);
		
	}
	
	var medal = dc.getParameter('medal');
	if(medal!=0){
		var pwidth = $(icfp.win.document).width();
		var pheight = $(icfp.win.document).height();
		var left = (pwidth - 260)*0.5;
		var top = (pheight - 56)*0.5;
		setTimeout(function(){
			$('.popupcredit .pc_inner').html(medal);
			$('.popupcredit').css({'top':0,'left':left}).show().animate({'top':top},600,'easeInOutBounce');
		},200);
		setTimeout(function(){
			$('.popupcredit').animate({'top':-56},600,function(){
				$('.popupcredit').hide();
			});
		},3000);
		
	}
}

icfp.formatNumber = function(num,separator){
	num = num + '';
	/*
	if(!separator){ 
		separator = ",";
	}
    var re = /(\d{3})\B/g;
    return num.replace(re,"$1" + separator);
    */
    if(/[^0-9\.]/.test(num)){
    	return "invalid value";
    }
    num = num.replace(/^(\d*)$/,"$1.");
    num = (num+"00").replace(/(\d*\.\d\d)\d*/,"$1");
    num = num.replace(".",",");
    var re=/(\d)(\d{3},)/;
    while(re.test(num)){
    	num = num.replace(re,"$1,$2");
    }
    num = num.replace(/,(\d\d)$/,".$1");
    return num.replace(/^\./,"0.");
};

icfp.getDays = function(strDateStart,strDateEnd){
	var strSeparator = "-"; //日期分隔符
	var oDate1;
	var oDate2;
	var iDays;
	oDate1= strDateStart.split(strSeparator);
	oDate2= strDateEnd.split(strSeparator);
	var strDateS = new Date(oDate1[0],oDate1[1]*1-1,oDate1[2]);
	var strDateE = new Date(oDate2[0],oDate2[1]*1-1,oDate2[2]);
	iDays = parseInt(Math.abs(strDateS - strDateE ) / 1000 / 60 / 60 /24)//把相差的毫秒数转换为天数 
	return iDays;
};

$(function(){
	//$('body').noSelect();
	$(document).keydown(function(event){
		if( document.activeElement.type=='text' && document.activeElement.readOnly){
			return false
		}
		if( document.activeElement.type=='textarea' && document.activeElement.readOnly){
			return false
		}
		if( document.activeElement.type=='password'){
			return true;
		}
		var inputs = $('input[type=text]');
		var textareas = $('textarea');
		// 去除掉输入框
		for(var i=0 ;i<inputs.length;i++){
			if(document.activeElement.type=='text'){
				return true;
			}
		}
		for(var i=0 ;i<textareas.length;i++){
			if(document.activeElement.type=='textarea'){
				return true;
			}
		}
		if(event.ctrlKey){
			return false;
		}
		if(event.keyCode==8){
			return false;
		}
	});
});
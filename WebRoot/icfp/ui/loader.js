	var json2 = "<script type='text/javascript' src='icfp/ui/jquery/json2.js'></script>";
	document.write(json2);
	//var meta = '<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />';
	//document.write(meta);
	var _plugins = {
		common:{css:'default.css'},
		form:{css:'form.css'},
		aa02:{js:'aa02code.js'},
		menul:{js:'menulist.js'},
		exp:{js:['artDialog/jquery.artDialog.js',
		         'artDialog/plugins/iframeTools.js',
		         'validation/jquery.validate.min.js',
		         'validation/jquery.metadata.js',
		         'validation/messages_cn.js',
		         'My97DatePicker/WdatePicker.js',
		         'ztree/js/jquery.ztree.all-3.5.js'
		        ],
		      css:['artDialog/skins/idialog.css',
		           'ztree/css/zTreeStyle.css'
		           ]},
		ds:{js:['icfp.js','icfplibs.js','ds.js']},
		cookie:{js:'cookie.js'},
		validform:{js:'jquery.validform.js'},
		grid:{js:'jquery.datagrid.js',css:'grid.css'},
		menu:{js:'jquery.smartmenu.js',css:'smartmenu.css'},
		tab:{js:'jquery.tab.js',css:'tab.css'},
		tip:{js:'jquery.tip.js',css:'tip.css'},
		leftbar:{css:'leftbar.css'},
	};
	
	var pathbase = "";
	
	var CssPathbase = "";
	
	var JsPathbase = "";

	var theme = "default";
	
	var scripts = document.getElementsByTagName("script");
	
	for(var i=0;i<scripts.length;i++) {
		var src = scripts[i].src;
		if (!src) {
			continue;
		}
		var m = src.match(/loader\.js(\W|$)/i);
		if (m) {
			pathbase = src.substring(0,m.index);
			JsPathbase = pathbase + 'js/';
			CssPathbase =  pathbase + 'skins/' + theme + '/css/';
			break;
		}
	}
	
	function load(){
		$.each(_plugins,function(k,p){
			if(k=='exp'){
				$.each(p.css,function(index,e){
					var pa = pathbase + 'exp/'+ e;
					_loadcss(pa);
				});
				$.each(p.js,function(index,e){
					var pa = pathbase + 'exp/'+ e;
					_loadjs(pa);
				});
				
			}else if(k=='ds'){
				$.each(p.js,function(index,d){
					var pa = pathbase + 'ds/'+ d;
					_loadjs(pa);
				});
			}else{
				if(p.css && p.css!=''){
					var pa = CssPathbase+p.css;
					_loadcss(pa);
				}
				if(p.js && p.js!=''){
					var pa = JsPathbase+p.js;
					_loadjs(pa);
				}
			}
		});
	}
	
	function _loadjs(path){
		var tmp = "<script type='text/javascript' src='"+path+"'></script>";
		document.write(tmp);
	}
	function _loadcss(path){
		var tmp = "<link rel='stylesheet' href='"+path+"' type='text/css'/>";
		document.write(tmp);
	}
	load();

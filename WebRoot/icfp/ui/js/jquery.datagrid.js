;(function ($,window,document,undefined){
	var pluginName = "DataGrid";
	var defaults = {
		title: false,                           //标题
		height: 'auto',                         //宽度值
		width: 'auto',                          //宽度值
		striped: true,                          //应用奇偶条纹
		novstripe: true,                       //有无边框
		minwidth: 30,                           //列最小宽度
	    minheight: 80,                          //最大高度
	    resizable: false,                       //table是否可伸缩
		errormsg: '链接错误',                   //
		usepager: true,                        //是否分页
		usepages:true,                         //是否翻页工具条
		nowrap: true,                          //是否换行
		usePs: false,                           //使用自定义pagesize
		pagesize: 15,                           //每页条数
		psOptions: [10,15,20,25,40],            //分页条数
        parms: {},                              //提交到服务器的参数
        columns: [],                            //数据源
        rownumbers: false,                       //是否显示行序号
        rownumbersCol:null,
        checkbox: false,                         //是否显示复选框
        checkboxCol:null,
		pagestat: '共 {total} 条',
		pagetext: '第',
		outof: '/',
		procmsg: '正在加载数据请稍等...',
		nomsg: '没有查询到数据!',
		minColToggle: 1,                        //最少保留列
		showToggleBtn: true, 
		hideOnSubmit: true,
		blockOpacity: 0.5,
		onDragCol: false,
		onToggleCol: false,
		onChangeSort: false,                   //排序回调
		onSuccess: false,                      //加载完数据回调
		onError: false,                        //错误回调函数
		onSubmit: false,                       //提交前回调函数
		datastore: null,
		sortname:'',
		sortorder:'asc',
		islocal:false,                          //加载本地数据
		singleSelect:true,                       //单选
		onSelectRow:false,
		unSelectRow:false,
		sscroll:0,
		escroll:0,
		onDblClickRow: false,                     //双击行事件
		busButton:[],
		fullWidth:true,
		showBusbar:true,
		toolbar:[],
		showBusButton:true,
		initChecked: false,                       //复选框 初始化函数
		datacenter:null
	};
	
	function Plugin(element,options){
		this.element = element;
		this.options = $.extend({},defaults,options);
		this._defaults = defaults;
		this._name = pluginName;
		this.dgid = this.element.id?this.element.id:d.getTime();
		this.init();
    }
	
	var browser = function(){
        var isIE=!!window.ActiveXObject;
        var isIE10 = isIE && !!window.atob;
        var isIE9 = isIE && document.addEventListener && !window.atob;
        var isIE8 = isIE && document.querySelector && !document.addEventListener;
        var isIE7 = isIE && window.XMLHttpRequest && !document.querySelector;
        var isIE6 = isIE && !window.XMLHttpRequest;
        return {
            isIE: isIE
            , isIE6: isIE6
            , isIE7: isIE7
            , isIE8: isIE8
            , isIE9: isIE9
            , isIE10: isIE10
        };
    }();
    
    function GetPostion(e) {
    	var x = getX(e);
    	var y = getY(e);
    	return { "x": x, "y": y-60};
    }
    
    function getX(e) {
    	e = e || window.event;
    	return e.pageX || e.clientX + document.documentElement.scrollLeft;
    }
    
    function getY(e) {
    	e = e|| window.event;
    	return e.pageY || e.clientY + document.documentElement.scrollTop;
    }       
    
	Plugin.prototype = {
		init:function(){
			var o = this,p = this.options,dg = this.element;
			o.id = this.element.id?this.element.id:d.getTime();
			$(dg).addClass('dgGrid');
			o._loadCount = 0;
        	o.hasfullWidth = 0;
			o._initHtml();
			if(p.title){
				o._initTitle();
			}
			o._initHead();
			o._initHideShowCol();
			o._initEvent();
			o._setColsWidth();
			if(p.fullWidth){
				o._fullWidth();
	        }
			if(p.usepager){
				o._initPageBar();
			}
			o._loadData();
		},
		_initHtml:function(){
			var o = this,p = this.options,dg = this.element;
			var html = [
				'<style></style>',
				'<div class="dg-headWrapper">',
					'<table class="dg-head" cellspacing="0"></table>',
				'</div>',
				'<div class="dg-colResizePointer"></div>',
				'<div class="dg-colResizePointer-before"></div>',
				'<div class="dg-backboard">',
					'<a class="dg-btnBackboardUp"></a>',
				'</div>',
				'<form>',
				'<div class="dg-bodyWrapper">',
					'<table class="dg-body" cellspacing="0"></table>',
				'</div>',
				'</form>',
				'<a class="dg-btnBackboardDn"></a>',
				'<div class="dg-message">'+ p.nomsg +'</div>',
				'<div class="dg-mask dg-transparent"></div>',
				'<div class="dg-loading">',
					'<div class="dg-loadingImg"></div>',
					'<div class="dg-loadingText">'+ p.procmsg +'</div>',
				'</div>'
			];
			$(dg).append(html.join(''));
			var $dgGrid = $(dg);
			o.$dgGrid = $dgGrid;
			
			o.$style = $dgGrid.find('style');
            o.$headWrapper = $dgGrid.find('.dg-headWrapper');
            o.$head = $dgGrid.find('.dg-head');
            o.$backboard = $dgGrid.find('.dg-backboard');
            o.$bodyWrapper = $dgGrid.find('.dg-bodyWrapper');
            o.$body = $dgGrid.find('.dg-body');
            o._insertEmptyRow();
            o.columns = [];
            // fix in ie6
            if(browser.isIE6 && (!p.width || p.width === 'auto')){
            	$dgGrid.width('100%');
            	$dgGrid.width($dgGrid.width() - ($dgGrid.outerWidth(true) - $dgGrid.width()));
            }else{
            	$dgGrid.width(p.width);
            }

            if(browser.isIE6 && (!p.height || p.height === 'auto')){
            	$dgGrid.height('100%');
            	$dgGrid.height($dgGrid.height() - ($dgGrid.outerHeight(true) - $dgGrid.height()));
            }else{
            	$dgGrid.height(p.height);
            }
            
            //重构表头
            //序号
            if(p.rownumbers){
            	o.columns.push({display:'序号',name:'dg-rownumber',width: 30, align: 'center' ,lockWidth: true,format:function(val,row,index){
            		if(p.islocal){
            			return '<label class="dg-index">' + (index+1) + '</label>';
            		}else{
            			var ds = p.datastore;
						var pageNo = ds.getPageNo();
						var pagesize = ds.getPageSize();
						var ii = (pageNo*1-1)*pagesize + index + 1;
						return '<label class="dg-index">' + ii + '</label>';
            		}
                }});
            }
            
            //checkbox
            if(p.checkbox){
                var chkHtml = !p.singleSelect ?  '<input type="checkbox" class="checkAll" disabled="disabled">' : '<input type="checkbox" disabled="disabled" class="checkAll">';
                o.columns.push({display:chkHtml,name:'dg-checkbox',width: 20, align: 'center' ,lockWidth: true, format:function(val,row,index,isChecked){
                	if(isChecked){
                		return '<input type="checkbox" class="dg-check" checked="checked">';
                	}else{
                		return '<input type="checkbox" class="dg-check">';
                	}
                }});
            }
            
            if(p.columns){
            	$.each(p.columns,function(i,col){
            		o.columns.push(col);
            	});
            }
            
		},
		_insertEmptyRow:function(){
			var o = this,p = this.options,dg = this.element;
			o.$body.empty().html('<tbody><tr class="emptyRow"><td  style="border: 0px;background: none;">&nbsp;</td></tr></tbody>');
		},
		_initTitle:function(){
			var o = this,p = this.options,dg = this.element;
			$(dg).before('<div id="'+o.id+'_dgtitle" class="dgtitle"><div class="title"><img src="icfp/ui/skins/icon/calendar.gif" width="15" height="15"/></div><div class="toolbtn">新增，导出，打印</div></div>');
			o.$title = $('#'+o.id+'_dgtitle');
			//o.$title.width($(dg).width());
			$('.title',o.$title).append(p.title);
			$('.toolbtn',o.$title).html('');
			if(p.toolbar && p.toolbar.length>0){
				$.each(p.toolbar,function(index,bar){
					$a = $('<a href="javascript:void(0);" class="button white medium"><img src="icfp/ui/skins/icon/'+ bar.icon +'" width="15" height="15"/>'+ bar.name +'</a>');
					$('.toolbtn',o.$title).append($a);
					if(bar.fn){
						$a.click(function(){
							bar.fn(this,$('.toolbtn',o.$title));
						});
					}
				});
			}
		},
		_initHead:function(){
			var o = this,p = this.options,dg = this.element;
			if(o.columns){
				var html = ['<thead>'];
				$.each(o.columns,function(i,col){
					html.push('<th class="');
					html.push(o._genColClass(i));
					html.push(' nowrap" style="text-align:center;">');
					html.push('<div class="dg-titleWrapper" >');
					html.push('<span class="dg-title ');
                    if(col.sortable){
                    	html.push('dg-canSort ');
                    }
                    html.push('">');
                    html.push(col.display);
                    html.push('</span><div class="dg-sort"></div>');
                    if(!col.lockWidth){
                    	html.push('<div class="dg-colResize"></div>');
                    }
                    html.push('</div></th>');
				});
				html.push('</thead>');
				o.$head.html(html.join(''));
			}
			$.each(o.$head.find('th'),function(index,th){
				if(!o.columns[index].width){
					o.columns[index].width = 100;
				}
				$.data(th,'col-width',o.columns[index].width);
			});
			o.$bodyWrapper.height(o.$dgGrid.height() - o.$headWrapper.outerHeight(true));
			//初始化排序状态
			if(p.sortname){
				var $ths = o.$head.find('th');
				$.each(o.columns,function(i,col){
					if(col.name === p.sortname){
                        var $th= $ths.eq(i);
                        $.data($th.find('.dg-title')[0],'sortorder',p.sortorder);
                        $th.find('.dg-sort').addClass('dg-'+p.sortorder);
                    }
				});
            }
		},
		/* 生成列类 */
        _genColClass: function(colIndex){
			var o = this,p = this.options,dg = this.element;
            return 'dg'+ o.id +'-col-'+colIndex;
        },
        _initHideShowCol:function(){
        	var o = this,p = this.options,dg = this.element;
        	o.$dgGrid.find('a.dg-btnBackboardDn').css({
        		'top': o.$headWrapper.outerHeight(true)
        	}).slideUp('fast');
        	if(o.columns){
        		var html = ['<h1>显示列</h1>'];
        		$.each(o.columns,function(i,col){
        			if(col.name==='dg-rownumber'){
        				return;
        			}
        			if(col.name==='dg-checkbox'){
        				return;
        			}
        			html.push('<label ');
        			if(col.checkCol){
        				html.push('style="display:none;" ');
        			}
        			html.push('><input type="checkbox"  ');
                	if(!col.hide){
                		html.push('checked="checked"');
                	}
                	if(col.lockDisplay){
                		html.push(' disabled="disabled"');
                	}
                	html.push('/><span>');
                	if(col.display){
                		html.push(col.display);
                	}else{
                		html.push('未命名');
                	}
                	html.push('</span></label>');
        		});
        		o.$backboard.append(html.join(''));
        	}
        },
        _initEvent:function(){
        	var o = this,p = this.options,dg = this.element;
        	 //调整浏览器
            if(p.width === 'auto' || p.height === 'auto' || (typeof p.width === 'string' && p.width.indexOf('%') === p.width.length-1) || typeof p.height === 'string' && p.height.indexOf('%') === p.height.length-1){
                $(window).on('resize', function(){
                    o._resize();
                });
            }
           
            //滚动条事件
            o.$bodyWrapper.on('scroll', function(){
                o.$head.css('left',- $(this).scrollLeft());
                var too_b = $('.gridb',o.$bodyWrapper);
                if(too_b || too_b.size()>1){
                	var left = o.toolbar_left + $(this).scrollLeft();
                	var wl = o.$body.width();
                	var toow = $(too_b).width();
                	if((left+toow)<wl){
                		too_b.css('left',left);
                	}else{
                		
                		too_b.css('left',wl-toow);
                	}
                }
            });
            //向下按钮
            var $btnBackboardDn = o.$dgGrid.find('a.dg-btnBackboardDn').on('click', function(){
                o.$backboard.height(o.$dgGrid.height() - o.$headWrapper.outerHeight(true));
                o.$backboard.slideDown();
                $btnBackboardDn.slideUp('fast');
                o._hideNoData();
            });
            o.$body.on('mouseenter', function(){
                $btnBackboardDn.slideUp('fast');
            });
            o.$dgGrid.on('mouseleave', function(){
                $btnBackboardDn.slideUp('fast');
            });
            o.$headWrapper.on('mouseenter',function(){
                if(o.$backboard.is(':hidden')){
                    $btnBackboardDn.slideDown('fast').css('top',30);
                }
            });
            //向上按钮
            o.$dgGrid.find('a.dg-btnBackboardUp').on('click', function(){
            	o.$backboard.slideUp().queue(function(next){
	            	if(!o._rowsLength() || (o._rowsLength() === 1 && o.$body.find('tr.emptyRow').length === 1)){
	            		o._showNoData();
	            	}
	            	next();
            	});
            });
            
            //隐藏列
            o.$backboard.on('click', ':checkbox', function(){
            	 var index = o.$backboard.find('label').index($(this).parent());
            	 if(p.checkbox){
                     index = index + 1;
                 }
                 if(p.rownumbers){
                     index = index + 1;
                 }
                 if(this.checked){
                	 o.columns[index].hide = false;
                	 o._setColsWidth();
                 }else{
                	 o.columns[index].hide = true;
                	 o._setColsWidth();
                 }
            });
            
            //排序事件
            o.$head.on('click','.dg-title', function(){
            	var $this = $(this);
                var $titles =  o.$head.find('.dg-title');
                //当前列不允许排序
                var sortable = o.columns[$titles.index($this)].sortable;
                if(!sortable){
                    return;
                }
                //取得当前列下一个排序状态
                var sortorder = $.data(this,'sortorder') === 'asc' ? 'desc' : 'asc'
                //清除排序状态
                $.each($titles, function(){
                    $.removeData(this,'sortorder');
                });
                o.$head.find('.dg-sort').removeClass('dg-asc').removeClass('dg-desc');
                //设置当前列排序状态
                $.data(this,'sortorder',sortorder);
                $this.siblings('.dg-sort').addClass('dg-'+sortorder);
                if(p.islocal){
                	o._changeSort($titles.index($this), sortorder);//_nativeSorter
                	o._setStyle();
                }else{
                	p.sortorder = sortorder;
                	var col = o.columns[$titles.index($this)];
                	p.sortname = sortable + "_" +col.name;
                	o._loadData();
                }
            }).on('mousedown','.dg-colResize', function(e){
	           	var $resize = $(this);
	            var start = e.pageX;
	            var $colResizePointer = o.$dgGrid.find('.dg-colResizePointer').css('left', e.pageX - o.$headWrapper.offset().left).show();
	            var scrollLeft = o.$head.position().left;
	            var $colResizePointerBefore = o.$dgGrid.find('.dg-colResizePointer-before').css('left', $resize.parent().parent().position().left + scrollLeft).show();
	            o.leftlineWidth = $resize.parent().parent().position().left + scrollLeft;
	             //取消文字选择
	            document.selection && document.selection.empty && ( document.selection.empty(), 1) || window.getSelection && window.getSelection().removeAllRanges();
	            document.body.onselectstart = function () {
	                return false;
	            };
	            o.$headWrapper.css('-moz-user-select','none');
	            o.$dgGrid.on('mousemove', function(e){
	            	var ww = e.pageX - o.$headWrapper.offset().left;
	            	if((ww-o.leftlineWidth)>p.minwidth){
	            		o.rightlineWidth = ww;
	            		$colResizePointer.css('left',ww);
	            	}
	            }).on('mouseup', function(e){
	                //改变宽度
	                var $th = $resize.parent().parent();
	                var width = o.rightlineWidth - o.leftlineWidth;
	                if(width){
	                    $.data($th[0], 'col-width', width);
	                    o._setColsWidth();
	                }
	                o.$headWrapper.mouseleave();
	            }).on('mouseleave',function(){
	            	o.$dgGrid.off('mouseup').off('mouseleave').off('mousemove');
	                $colResizePointer.hide();
	                $colResizePointerBefore.hide();
	                document.body.onselectstart = function(){
	                    return true;//开启文字选择
	                };
	                o.$headWrapper.css('-moz-user-select','text');
	            });
            });
            
            var too_b = null;
            
            function __inittoo_b(){
            	 too_b = $('.gridb',o.$bodyWrapper);
                 if(!too_b || too_b.size()<1){
                 	var too_html = '<div class="gridb">'+
     				'<div class="le"></div>'+
     				'<div class="btn">'+
     				'<div class="inputb">'+
     				'</div>'+
     				'<div class="line"></div>'+
     				'<div class="inputs"></div>'+
     				'</div>'+
     				'<div class="ri"></div>'+
     				'</div>';
                 	$(o.$bodyWrapper).append(too_html);
                 	too_b = $('.gridb',o.$bodyWrapper);
                 }
                 
                 if(p.busButton.length>0){
                 	$('.inputb',too_b).html('');
                 	$('.inputs',too_b).html('');
                 	$.each(p.busButton,function(index,button){
                 		if(button.type && button.type=='bus'){
                 			var a = $('<a href="javascript:void(0);"></a>');
                 			if(button.id){
                 				$(a).attr('id',button.id);
                 			}
                 			$(a).attr('title',button.name);
                 			$(a).html(button.name);
                 			if(button.fn){
                 				$(a).click(function(){
                 					button.fn(this,$('.inputb',too_b),$(too_b));
                 				});
                 			}
                 			$('.inputb',too_b).append($(a));
                 			if(button.hide){
                 				$(a).hide();
                 			}
                 		}else if(button.type && button.type=='sys'){
                 			var a = $('<a href="javascript:void(0);"></a>');
                 			if(button.id){
                 				$(a).attr('id',button.id);
                 			}
                 			$(a).attr('title',button.name);
                 			$(a).html(button.name);
                 			if(button.fn){
                 				$(a).click(function(){
                 					button.fn(this,$('.inputs',too_b),$(too_b));
                 				});
                 			}
                 			$('.inputs',too_b).append($(a));
                 			if(button.hide){
                 				$(a).hide();
                 			}
                 		}
                 	});
                 }else{
                 	p.showBusButton = false;
                 }
                 $('.le',too_b).removeClass('leU');
                 $('.le',too_b).removeClass('leD');
                 $('.ri',too_b).removeClass('riD');
                 $('.ri',too_b).removeClass('riD');
                 $(too_b).css('width',$(too_b).width());
                 o.too_b = too_b;
            }
            
            //选中事件
            o.$body.on('click','td',function(e){
            	e = e||event;
            	if(o._showEdtPanel){
     				return;
     			}
            	__inittoo_b();
            	var ds = p.datastore;
    			var rowSets = ds.getRowSet();
            	$(too_b).hide();
            	var $this = $(this);
            	var _index = $(this).parent().attr('_index');
            	if(_index){
	                o.$body.triggerHandler('rowSelected',[$.data($this.parent()[0],'item'),$this.parent().index(),$this.index()]);
	                if(!$this.parent().hasClass('selected')){
	                    o._selectRow($this.parent().index());
	                    var pp = o.options
	                    if(pp.showBusButton){
		                    var top = this.offsetTop - 50;
		                    var ptrw = $this.parent().width();
		                    var posw = e.clientX - $this.parent().offset().left;
		                    var wtoo = $(too_b).width();
		                    var left = posw;
		                    if(top<0){
		                    	if(ptrw>(posw+wtoo)){
		                    		left = posw;
		                    		$('.le',too_b).removeClass('leD');
		                    		$('.ri',too_b).removeClass('riD');
		                    		$('.ri',too_b).removeClass('riU');
		                    		$('.le',too_b).addClass('leU');
		                    	}else{
		                    		left = posw - wtoo;
		                    		$('.le',too_b).removeClass('leD');
		                    		$('.le',too_b).removeClass('leU');
		                    		$('.ri',too_b).removeClass('riD');
		                    		$('.ri',too_b).addClass('riU');
		                    		
		                    	}
		                    	top = top + 80;
		                    }else{
		                    	if(ptrw>(posw+wtoo)){
		                    		left = posw;
		                    		$('.le',too_b).removeClass('leU');
		                    		$('.ri',too_b).removeClass('riU');
		                    		$('.ri',too_b).removeClass('riD');
		                    		$('.le',too_b).addClass('leD');
		                    	}else{
		                    		left = posw - wtoo;
		                    		$('.ri',too_b).removeClass('riU');
		                    		$('.le',too_b).removeClass('leU');
		                    		$('.le',too_b).removeClass('leD');
		                    		$('.ri',too_b).addClass('riD');
		                    	}
		                    }
		                    o.toolbar_left = left;
		                    $(too_b).css({
								top:top,
								left:left
							});
		                    $(too_b).slideDown();
	                    }
	                    if(p.onSelectRow && _index){
	    					p.onSelectRow(rowSets[_index],_index,ds);
	    				}
	                }else{
	                	o._unselectRow($this.parent().index());
	                	if(p.unSelectRow && _index){
							p.unSelectRow(rowSets[_index],_index,ds);
						}
	                }
            	}
            });
            
            o.$body.on('dblclick','td',function(){
            	
            });
            
            o.$body.on('click','tr > td .dg-check',function(e){
            	if(o._showEdtPanel){
     				return;
     			}
            	var ds = p.datastore;
    			var rowSets = ds.getRowSet();
            	e.stopPropagation();
				var $this = $(this);
				var _index = $(this).parent().parent().parent().attr('_index');
				if(this.checked){
				    o._selectRow($this.parent().parent().parent().index());
				    if(p.onSelectRow && _index){
						p.onSelectRow(rowSets[_index],_index,ds);
				    }
				}else{
				    o._unselectRow($this.parent().parent().parent().index());
				    if(p.unSelectRow && _index){
				    	p.unSelectRow(rowSets[_index],_index,ds);
				    }
				}
            });
            
            //checkbox列
            if(p.checkCol){
                o.$head.find('th .checkAll').on('click', function(){
                	if(p.singleSelect){
                		$(this).prop('checked','');
                		return;
                	}
                    if(this.checked){
                    	 o._selectRow('all');
                    }else{
                    	 o._unselectRow('all');
                    }
                });
            }
            
            //IE6不支持hover
            if(browser.isIE6){
                o.$body.on('mouseenter','tr', function () {
                    $(this).toggleClass('hover');
                }).on('mouseleave','tr', function () {
                    $(this).toggleClass('hover');
                });
            };
            
            $(document).mousedown(function(e){
            	//alert(e.target.nodeName);
            	if(!$(e.target).parents('.dg-body').length && e.target.nodeName != 'TD' && !$(e.target).parents('.gridb').length){
		    		var too_b = $('.gridb',o.$bodyWrapper);
		    		$(too_b).hide();
		    		//o.clearSelected();
            	}
            });
            
            $(icfp.win.document).mousedown(function(e){
            	if(!$(e.target).parents('.dg-body').length && e.target.nodeName != 'TD' && !$(e.target).parents('.gridb').length){
		    		var too_b = $('.gridb',o.$bodyWrapper);
		    		$(too_b).hide();
		    		//o.clearSelected();
            	}
            });
        },
        _resize:function(){
        	var o = this,p = this.options,dg = this.element;
        	// fix in ie6
            if(browser.isIE6 && (!p.width || p.width === 'auto')){
                o.$dgGrid.width('100%');
                o.$dgGrid.width(o.$dgGrid.width() - (o.$dgGrid.outerWidth(true) - o.$dgGrid.width()));
            }else{
            	o.$dgGrid.width(p.width);
            }

            if(browser.isIE6 && (!p.height || p.height === 'auto')){
            	o.$dgGrid.height('100%');
            	o.$dgGrid.height(o.$dgGrid.height() - (o.$dgGrid.outerHeight(true) - o.$dgGrid.height()));
            }else{
            	o.$dgGrid.height(p.height);
            }
            o.$bodyWrapper.height(o.$dgGrid.height() - o.$headWrapper.outerHeight(true));
            //调整message
            var $message = o.$dgGrid.find('.dg-message');
            if($message.is(':visible')){
                $message.css({
                    'left': (o.$dgGrid.width() - $message.width()) / 2,
                    'top': (o.$dgGrid.height() + o.$headWrapper.height() - $message.height()) / 2
                });
            }
            //调整loading
            var $mask = o.$dgGrid.find('.dg-mask');
            if($mask.is(':visible')){
                $mask.width(o.$dgGrid.width()).height(o.$dgGrid.height());
                var $loadingWrapper = o.$dgGrid.find('.dg-loading');
                $loadingWrapper.css({
                    'left': (o.$dgGrid.width() - $loadingWrapper.width()) / 2,
                    'top': (o.$dgGrid.height() - $loadingWrapper.height()) / 2
                })
            }
            o.$bodyWrapper.trigger('scroll');
            
        },
        _rowsLength:function(){
        	var o = this,p = this.options,dg = this.element;
        	var length = o.$body.find('tr').length;
        	if(length === 1 && o.$body.find('tr.emptyRow').length === 1){
        		return 0;
        	}
        	return length;
        },
        _setColsWidth:function(){
        	var o = this,p = this.options,dg = this.element;
        	var scrollTop = o.$bodyWrapper.scrollTop();
        	var scrollLeft = o.$head.position().left;
        	o.$bodyWrapper.width(9999);
        	o.$body.width('auto');
        	var $ths = o.$head.find('th');
        	var styleText = [];
        	$.each($ths,function(i,th){
        		styleText.push('.dgGrid .'+o._genColClass(i) + ' {');
                var width = $.data(th,'col-width');
                styleText.push('width: '+ width +'px;');
                styleText.push('max-width: '+ width +'px;');
                if(o.columns[i].align){
                    styleText.push('text-align: '+o.columns[i].align+';');
                }
                if(o.columns[i].hide){
                    styleText.push('display: none; ');
                }
                styleText.push(' }');
        	});
        	o.$body.detach();
            try{
                o.$style.text(styleText.join(''));
            }catch(error){
                o.$style[0].styleSheet.cssText = styleText.join('');//IE fix
            }
            o.$body.width(o.$head.width());
            o.$bodyWrapper.width('100%');
            o.$bodyWrapper.append(o.$body);
            
            //调整滚动条
            o.$bodyWrapper.scrollLeft(-scrollLeft);
            if(o.$bodyWrapper.scrollLeft() === 0){
                o.$head.css('left', 0);
            }
            o.$bodyWrapper.scrollTop(scrollTop);
        },
        _fullWidth:function(){
        	var o = this,p = this.options,dg = this.element;
        	var scrollWidth = o.$bodyWrapper.width() - o.$bodyWrapper[0].clientWidth;
            if(scrollWidth && browser.isIE){
                scrollWidth = scrollWidth + 1;
            }
            var fitWidth =  o.$dgGrid.width() - o.$head.width() - scrollWidth - 17;
            if(fitWidth < -20){
                return;
            }
            var thsArr = [];
            var $ths = o.$head.find('th');
            $.each(o.columns,function(i,col){
            	var $th = $ths.eq(i);
            	if(!col.lockWidth && !col.hide){
            		thsArr.push($th);
            	}
            });
            var increaseWidth =  Math.floor(fitWidth / thsArr.length);
            var maxColWidthIndex = 0;
            $.each(thsArr,function(i,th){
            	 var colWidth = $.data(th[0],'col-width') + increaseWidth;
                 $.data(th[0],'col-width',colWidth);
                 var maxColWidth = $.data(thsArr[maxColWidthIndex][0], 'col-width');
                 if(maxColWidth < colWidth){
                     maxColWidthIndex = i;
                 }
            });
            var remainWidth =  fitWidth -  increaseWidth * thsArr.length;
            var maxColWidth = $.data(thsArr[maxColWidthIndex][0], 'col-width');
            $.data(thsArr[maxColWidthIndex][0], 'col-width', maxColWidth + remainWidth);
            o._setColsWidth();
        },
        _initPageBar:function(){
        	var o = this,p = this.options,dg = this.element;
        	var html = [
        	    '<div id="'+o.id+'-pagebar" class="pagebar" style="background-color: #fff;"><div id="'+o.id+'-heji" class="dgheji"></div>',
        	    '<ul class="pageList">',
				'<li class="btn"><div class="pPrev pButton"><span></span></div></li>',
				'<li class="yema"><span>1</span>&nbsp;<span>/</span>&nbsp;<span>1</span></li>',
				'<li class="btn"><div class="pNext pButton"><span></span></div></li>',
				'</ul>',
				'<div class="totalCountLabel">共<span>0</span>条</div>',
				'<div class="limit"><select></select></div>',
				'</div>'
			];
        	o.$dgGrid.after($(html.join('')));
        	
        	//$("#"+o.id+"-pagebar").width($(dg).width());
        	o.$totalCountLabel = $("#"+o.id+"-pagebar").find('.totalCountLabel');
            o.$pageList = $("#"+o.id+"-pagebar").find('.pageList');
            o.$limitList = $("#"+o.id+"-pagebar").find('.limit select');
            if(p.usePs){
            	$.each(p.psOptions, function(){
            		var $option = $('<option></option>').prop('value',this).text("每页"+this+"条");
            		o.$limitList.append($option);
            	});
				o.$limitList.change(function(){
					o._changePage('select',this);
				});
            }else{
            	$("#"+o.id+"-pagebar").find('.limit').remove();
            }
            $('.pPrev',o.$pageList).click(function(){
            	o._changePage('prev',this);
            });
            $('.pNext',o.$pageList).click(function(){
            	o._changePage('next',this);
            });
            if(!p.usepages){
            	o.$pageList.html('');
        	}
        },
        _loadData:function(){
        	var o = this,p = this.options,dg = this.element;
        	$(o.too_b).hide();
			o._hideMessage();
			o._showLoading();
			o._loadCount = o._loadCount + 1 ;
			if(p.datastore==null){
				o._hideLoading();
				return false;
			}
			if(!(p.datastore instanceof Object)){
				o._hideLoading();
				return false;
			}
			if(!('getRowSet' in p.datastore)){
				o._hideLoading();
				return false;
			}
			o._queryData();
        },
        _queryData:function(){
        	var o = this,p = this.options,dg = this.element;
			var ds = p.datastore;
			if(p.islocal){
				var rowsets = ds.getRowSet();
				if(rowsets!=null && rowsets.length && rowsets.length >0){
					ds.setRowCount(rowsets.length);
					o._showData();
				}else{
					o._insertEmptyRow();
					o._showNoData();
					o._buildpager();
					o._hideLoading();
					return false;
				}
			}else{
				var dspp = ds.getParams();
				if(!dspp.length){
					ds.setParams(null);
				}
				if(p.sortname && p.sortorder) {
					//var sortnamee = p.sortname.split('_');
					//if (sortnamee.length > 1) {
						//ds.setOrdering(sortnamee[1] + ' ' + p.sortorder);
					//} else {
						ds.setOrdering(p.sortname + ' ' + p.sortorder);
					//}
				}
				
				function suc(dc){
					p.datacenter = dc;
					var ddss = dc.getDataStore(o.id);
					if(ddss){
						p.datastore = ddss;
						o._showData();
					}else{
						o._hideLoading();
					}
				}
				
				function fal(dc,ajaxOptions,xhr){
					if(p.onError){
						o._hideLoading();
						p.onError(dc,ajaxOptions,xhr);
					}else{
						o._hideLoading();
					}
				}
				
				var httpService = new HttpService();
				httpService.putBusinessRequestId(ds.getBizId());
				httpService.putDataStore(o.dgid,ds);
				if(p.parms != null || p.parms != ''){
					$.each(p.parms,function(k,v){
						httpService.putHttpParameter(k,v);
					});
				}
				httpService.addEventListener(icfp.ResultEvent,suc);
				httpService.addEventListener(icfp.FaultEvent,fal);
				//httpService.setShowLoading(false);
				httpService.post();
				
			}
        },
        _showData:function(){
        	var o = this,p = this.options,dg = this.element;
			var ds = p.datastore;
			var rowsets = ds.getRowSet();
			o._hideNoData();
			if(rowsets && rowsets.length !== 0 && o.columns){
				var bodyHtml = [];
                bodyHtml.push('<tbody>');
                var spages = 0;
				var epages = 0;
				if(p.islocal){
					var pageno = ds.getPageNo();
					var pagesize = ds.getPageSize();
					spages = (pageno-1)*pagesize;
					epages = pageno*pagesize;
					var totalpage = ds.getTotalPage();
					var rowcount = ds.getRowCount();
					if(totalpage == pageno){
						epages = rowcount;
					}
				}else{
					spages = 0;
					epages = ds.getPageSize();
				}
				for(var i=spages;i<epages;i++){
					if(rowsets[i]){
						if(rowsets[i].status != 'delete'){
							if(p.initChecked){
								var isChecked = p.initChecked(rowsets[i],i);
								if(isChecked){
									bodyHtml.push(o._getRowHtml(rowsets[i].cell,i,isChecked));
								}else{
									bodyHtml.push(o._getRowHtml(rowsets[i].cell,i,false));
								}
							}else{
								bodyHtml.push(o._getRowHtml(rowsets[i].cell,i));
							}
						}
					}
				}
				bodyHtml.push('</tbody>');
                o.$body.empty().html(bodyHtml.join(''));
                o._buildpager();
			}else{
				o._insertEmptyRow();
				o._showNoData();
			}
			o._setStyle();
			if(p.fullWidth){
				o._fullWidth();
	        }
            o._hideLoading();
            if(p.onSuccess){
            	p.onSuccess(p.datacenter);
            }
        },
        _getRowHtml:function(row,iindex,isChecked){
        	var o = this,p = this.options,dg = this.element;
			var trHtml = [];
			trHtml.push('<tr style="height:30px;" _index="'+iindex+'"');
			if(isChecked){
				trHtml.push(' class="selected" ');
			}
			trHtml.push('>');
			$.each(o.columns,function(index,col){
				trHtml.push('<td class="');
				trHtml.push(o._genColClass(index));
				if(p.nowrap){
				    trHtml.push(' nowrap');
				}
				if(p.novstripe){
					trHtml.push(' nowrap novstripe');
				}else{
					trHtml.push(' nowrap vstripe');
				}
				
				trHtml.push('"><span class="');
				if(p.nowrap){
				    trHtml.push('nowrap');
				}
				trHtml.push('"');
				if(col.formatstyle){
					var _style = col.formatstyle(row[col.name],row,iindex);
					if(_style){
						var _stylestr = [];
						$.each(_style,function(k,v){
							_stylestr.push(k+":"+v);
						});
						trHtml.push(' style="'+_stylestr.join(";")+'" ');
					}
				}
				trHtml.push('>');
				var vv = '';
				if(col.format){
					if(isChecked){
						vv = col.format(row[col.name],row,iindex,isChecked);
					}else{
						vv = col.format(row[col.name],row,iindex);
					}
				}else{
					vv = row[col.name];
				}
				if(col.istrans){	
					var tansv = row[col.name];
					var tansCodeName = col.name;
					var tansCode = AA02code[tansCodeName];
					if(tansCode){
						vv = tansCode[tansv];
					}
				}
				if(icfp.isString(vv)){
					if(vv!=''){
						trHtml.push(vv);
					}else{
						trHtml.push('&nbsp;');
					}
				}else if(icfp.isNumber(vv)){
					trHtml.push(vv);
				}else{
					trHtml.push('&nbsp;');
				}
				trHtml.push(' </span></td>');
			});
			trHtml.push('</tr>');
			return trHtml.join('');
        },
        _setStyle:function(){
        	var o = this,p = this.options,dg = this.element;
        	var $ths = o.$head.find('th');
			//head
			$ths.eq(0).addClass('first');
			$ths.eq(-1).addClass('last');
			//body
			o.$body.find('tr,td').removeClass('even').removeClass('colSelected').removeClass('colSelectedEven');
			o.$body.find('tr:odd').addClass('even');
			var sortIndex = o.$head.find('.dg-title').index(o.$head.find('.dg-title').filter(function(){
				return $.data(this,'sortorder') === 'asc' || $(this).data('sortorder') === 'desc';
			}));
			o.$body.find('tr > td:nth-child('+(sortIndex+1)+')').addClass('colSelected').filter(':odd').addClass('colSelectedEven');
			o.$body.find('tr').mouseover(function(e){
				if($(this).hasClass('selected')){
					if(o.too_b && p.showBusButton){
						if($(o.too_b).css('display') == 'none'){
							var top = this.offsetTop - 50;
		                    var ptrw = $(this).width();
		                    var posw = e.clientX - $(this).parent().offset().left;
		                    var wtoo = $(o.too_b).width();
		                    var left = posw;
		                    if(top<0){
		                    	if(ptrw>(posw+wtoo)){
		                    		left = posw;
		                    		$('.le',o.too_b).removeClass('leD');
		                    		$('.ri',o.too_b).removeClass('riD');
		                    		$('.ri',o.too_b).removeClass('riU');
		                    		$('.le',o.too_b).addClass('leU');
		                    	}else{
		                    		left = posw - wtoo;
		                    		$('.le',o.too_b).removeClass('leD');
		                    		$('.le',o.too_b).removeClass('leU');
		                    		$('.ri',o.too_b).removeClass('riD');
		                    		$('.ri',o.too_b).addClass('riU');
		                    		
		                    	}
		                    	top = top + 80;
		                    }else{
		                    	if(ptrw>(posw+wtoo)){
		                    		left = posw;
		                    		$('.le',o.too_b).removeClass('leU');
		                    		$('.ri',o.too_b).removeClass('riU');
		                    		$('.ri',o.too_b).removeClass('riD');
		                    		$('.le',o.too_b).addClass('leD');
		                    	}else{
		                    		left = posw - wtoo;
		                    		$('.ri',o.too_b).removeClass('riU');
		                    		$('.le',o.too_b).removeClass('leU');
		                    		$('.le',o.too_b).removeClass('leD');
		                    		$('.ri',o.too_b).addClass('riD');
		                    	}
		                    }
		                    o.toolbar_left = left;
		                    $(o.too_b).css({
								top:top,
								left:left
							});
							$(o.too_b).slideDown();
						}
					}
				}
			});
        },
        _buildpager:function(){
        	var o = this,p = this.options,dg = this.element;
			var ds = p.datastore;
			var pageNo = ds.getPageNo();
			var totalPage = ds.getTotalPage();
			var pagesize = ds.getPageSize();
			var rowcount = ds.getRowCount();
			$('.yema span:eq(0)',o.$pageList).html(pageNo);
			$('.yema span:eq(2)',o.$pageList).html(totalPage);
			o.$totalCountLabel.html('共<span>'+rowcount+'</span>条');
			if(!p.usepages){
            	o.$pageList.html('');
        	}
        },
        _changeSort:function(colIndex,sortStatus){
        	var o = this,p = this.options,dg = this.element;
			var col = o.columns[colIndex];
			o.$body.find('tr > td:nth-child('+(colIndex+1)+')').sortElements(function(a, b){
				var av = $.text($(a));
				var bv = $.text($(b));
                //排序前转换
                if(col.type === 'number'){
                    av = parseFloat(av);
                    bv = parseFloat(bv);
                }else{
                    //各个浏览器localeCompare的结果不一致
                    return sortStatus === 'desc' ? -av.localeCompare(bv)  : av.localeCompare(bv);
                }
                return av > bv ? (sortStatus === 'desc' ? -1 : 1) : (sortStatus === 'desc' ? 1 : -1);
            }, function(){
                return this.parentNode;
            });
        },
        _changePage:function(ctype,obj){
			var o = this,p = this.options,dg = this.element;
			var ds = p.datastore;
			var pageNo = ds.getPageNo();
			var totalPage = ds.getTotalPage();
			var pagesize = ds.getPageSize();
			var rowcount = ds.getRowCount();
			switch(ctype){
				case 'prev': 
					if(pageNo>1){
						ds.setPageNo(parseInt(pageNo) - 1);
						this._loadData();
					}else{
						icfp.win.icfp.msg.tip("提示","没有上一页了！",'sad');
					}
					break;
				case 'next': 
					if(pageNo<totalPage){
						ds.setPageNo(parseInt(pageNo) + 1); 
						this._loadData();
					}else{
						icfp.win.icfp.msg.tip("提示","没有下一页了！",'sad');
					}
					break;
				case 'select':
					var ps = $(obj).val();
					if(ps==pagesize){
						break;
					}
					ds.setPageSize(ps);
					this._loadData();
					break;
			}
			
		},
        _selectRow:function(args){
        	var o = this,p = this.options,dg = this.element;
			if(typeof args === 'number'){
                var $tr = o.$body.find('tr').eq(args);
                if(p.singleSelect){
                    o.$body.find('tr.selected').removeClass('selected');
                    if(p.checkbox){
                       o. $body.find('tr > td').find('.dg-check').prop('checked','');
                    }
                }
                if(!$tr.hasClass('selected')){
                    $tr.addClass('selected');
                    if(p.checkbox){
                        $tr.find('td .dg-check').prop('checked','checked');
                    }
                }
            }else if(typeof args === 'function'){
                $.each($body.find('tr'), function(index){
                    if(args($.data(this, 'rowdata'), index)){
                        var $this = $(this);
                        if(!$this.hasClass('selected')){
                            $this.addClass('selected');
                            if(p.checkbox){
                                $this.find('td .dg-check').prop('checked','checked');
                            }
                        }
                    }
                });
            }else if(args === undefined || (typeof args === 'string' && args === 'all')){
                o.$body.find('tr.selected').removeClass('selected');
                o.$body.find('tr').addClass('selected');
                o.$body.find('tr > td').find('.dg-check').prop('checked','checked');
            }
        },
        _unselectRow:function(args){
        	var o = this,p = this.options,dg = this.element;
			if(typeof args === 'number'){
				o.$body.find('tr').eq(args).removeClass('selected');
				if(p.checkbox){
					o.$body.find('tr').eq(args).find('td .dg-check').prop('checked','');
			    }
			}else if(typeof args === 'function'){
				$.each(o.$body.find('tr'), function(index){
					if(args($.data(this, 'checkbox'),index)){
						$(this).removeClass('selected');
						if(p.checkbox){
							$(this).find('td .dg-check').prop('checked','');
			            }
			        }
			    });
			}else if(args === undefined || (typeof args === 'string' && args === 'all')){
				o.$body.find('tr.selected').removeClass('selected');
				if(p.checkbox){
					o.$body.find('tr > td').find('.dg-check').prop('checked','');
			    }
			}
        },
        _showLoading: function(){
        	var o = this,p = this.options,dg = this.element;
        	o.$dgGrid.find('.dg-mask').show();
        	var $loading = o.$dgGrid.find('.dg-loading');
        	$loading.css({
        		'left': (o.$dgGrid.width() - $loading.width()) / 2,
        		'top': (o.$dgGrid.height() - $loading.height()) / 2
        	}).show();
        }, 
        _hideLoading: function(){
        	var o = this,p = this.options,dg = this.element;
        	o.$dgGrid.find('.dg-mask').hide();
        	o.$dgGrid.find('.dg-loading').hide();
        }, 
        _showNoData: function(){
        	var o = this,p = this.options,dg = this.element;
            o._showMessage(p.nomsg);
        }, 
        _hideNoData: function(){
        	var o = this,p = this.options,dg = this.element;
            o._hideMessage();
        }, 
        _showMessage: function(msg){
        	var o = this,p = this.options,dg = this.element;
            var $message = o.$dgGrid.find('.dg-message');
            $message.css({
                'left': (o.$dgGrid.width() - $message.width()) / 2,
                'top': (o.$dgGrid.height() + o.$headWrapper.height()  - $message.height()) / 2
            }).text(msg).show();
        }, 
        _hideMessage: function(){
        	var o = this,p = this.options,dg = this.element;
        	o.$dgGrid.find('.dg-message').hide();
        },
        //监听div隐藏显示的
        ldivHideShow:function(){
        	var o = this,p = this.options,dg = this.element;
            o._resize();
            if(p.fullWidth && o.hasfullWidth<1){
            	o.hasfullWidth = o.hasfullWidth + 1; 
				o._fullWidth();
	       }
        },
		showEdtPanel:function(){
        	var o = this,p = this.options,dg = this.element;
        	if(!p.singleSelect){
        		return;
        	}
        	var $tr = o.$body.find('tr.selected');
        	if($tr && $tr.size()>0){
        		o._showEdtPanel = true;
        		$($tr).attr('_showEdtPanel',true);
        		var _edtIndex = $($tr).attr('_index');
				var ds = p.datastore;
				var sslrows = ds.getRowSet();
				var row = sslrows[_edtIndex];
				$.each($tr.find('td'),function(index,td){
					if(!o.columns[index].hide && o.columns[index].edtpanel){
						$(this).children('span').clone().prependTo(this);
						$('span:eq(1)',td).attr('style',"");
						$('span:eq(1)',td).removeClass('nowrap');
						var vv = row.cell[o.columns[index].name];
						var w = $(td).width();
						var $el = $(o.columns[index].edtpanel);
						if($el[0].tagName === 'SELECT'){
							var tansCode = AA02code[o.columns[index].name];
							if(tansCode){
								$.each(tansCode,function(k,v){
									if(k!='a'){
										var $option = $option = $('<option></option>').prop('value',k).text(v);
										$el.append($option);
									}
								});
							}
							$el.css('width',w);
						}else if($el[0].tagName === 'INPUT'){
							$el.css('width',w-7);
						}
						$el.val(vv);
						$('span:eq(0)',td).hide();
						$('span:eq(1)',td).html($el);
					}
				});
				$('form',o.$dgGrid).validForm();
        	}
		},
		endEdtPanel:function(tag){
			var o = this,p = this.options,dg = this.element;
			var $tr = o.$body.find('tr.selected');
			var change = false;
			if($tr && $tr.size()>0){
				var _edtIndex = $($tr).attr('_index');
				var ds = p.datastore;
				var sslrows = ds.getRowSet();
				var row = sslrows[_edtIndex];
				if(o._showEdtPanel){
					$.each($tr.find('td'),function(index,td){
						if(!o.columns[index].hide && o.columns[index].edtpanel){
							if(!tag){
								var $el = $('span:eq(1)',td);
								var $iel = $(':first-child',$el);
								var vv = '';
								var svv = '';
								if($iel[0].tagName == 'INPUT'){
									vv = $($iel).val();
								}else if($iel[0].tagName == 'SELECT'){
									svv = $($iel).val();
									vv = $($iel).find("option:selected").text();
								}
								var _vv = $('span:eq(0)',td).html().trim();
								if(!_vv || _vv ==='&nbsp;'){
									_vv = '';
								}
								if(_vv!=vv){
									change = true;
									if(o.columns[index].format){
										$('span:eq(0)',td).html(o.columns[index].format(vv,sslrows[_edtIndex],_edtIndex));
									}else{
										$('span:eq(0)',td).html(vv);
									}
									if(svv!=''){
										sslrows[_edtIndex].cell[o.columns[index].name] = svv;
									}else{
										sslrows[_edtIndex].cell[o.columns[index].name] = vv;
									}
								}
								$('span:eq(0)',td).show();
								$('span:eq(1)',td).remove();
							}else{
								$('span:eq(0)',td).show();
								$('span:eq(1)',td).remove();
							}
						}
					});
					if(change){
						var re = [];
						re.push(sslrows[_edtIndex]);
						re[0]['_index'] = _edtIndex;
						$($tr).attr('_showEdtPanel',false);
						o._showEdtPanel = false;
						return re;
					}else{
						$($tr).attr('_showEdtPanel',false);
						o._showEdtPanel = false;
						return false;
					}
				}else{
					$($tr).attr('_showEdtPanel',false);
					o._showEdtPanel = false;
					return false;
				}
			}else{
				return false;
			}
			
		},
		validRow:function(){
			var o = this,p = this.options,dg = this.element;
			return $('form',o.$dgGrid).validForm('valid');
		},
		changeValue:function(data){
			if(!data){
				return;
			}
			if(!data.name){
				return;
			}
			var colName = data.name;
			var value = data.value;
			var o = this,p = this.options,dg = this.element;
			var $tr = o.$body.find('tr.selected');
			if($tr && $tr.size()>0){
				var _edtIndex = $($tr).attr('_index');
				var ds = p.datastore;
				var sslrows = ds.getRowSet();
				var row = sslrows[o._edtIndex];
				$.each($tr.find('td'),function(index,td){
					if(o.columns[index].name == colName){
						var vv = value;
						if(o.columns[index].istrans){
							var tansv = vv;
							var tansCodeName = o.columns[index].name;
							var tansCode = AA02code[tansCodeName];
							if(tansCode){
								vv = tansCode[tansv];
							}
						}
						$('span:eq(0)',td).html(vv);
						if(data.status && data.status!=''){
							if(data.status == 'insert'){
								sslrows[_edtIndex].status = 'insert';
							}
							if(data.status == 'update'){
								sslrows[_edtIndex].status = 'update';
							}
						}
						sslrows[_edtIndex].cell[o.columns[index].name] = vv;
					}
				});
			}
		},
		changeStyle:function(opt){
			/*
			 * opt = {};
			 * opt.style = {"color":"red","":""};
			 * opt.colname = "";
			 * */
			if(opt.style && opt.style !="" && opt.colname && opt.colname != ""){
				var o = this,p = this.options,dg = this.element;
				var $tr = o.$body.find('tr.selected');
				if($tr && $tr.size()>0){
					$.each($tr.find('td'),function(index,td){
						if(o.columns[index].name == opt.colname){
							var _stylestr = [];
							$.each(opt.style,function(k,v){
								_stylestr.push(k+":"+v);
							});
							$('span:eq(0)',td).attr('style',_stylestr.join(";"));
							return false;
						}
					});
				}
			}else{
				return;
			}
		},
		getSelectedRow:function(){
			var o = this,p = this.options,dg = this.element;
			var ds = p.datastore;
			var sslrows = ds.getRowSet();
			var re = [];
			$('tr.selected',o.$body).each(function(){
				var _index = $(this).attr('_index');
				if(_index && _index!=''){
					re.push(sslrows[_index]);
				}
			});
			if(re.length>0){
				return re;
			}else{
				return false;
			}
		},
		SelectedRow:function(){
			var o = this,p = this.options,dg = this.element;
			var ds = p.datastore;
			var sslrows = ds.getRowSet();
			var re = [];
			$('tr.selected',o.$body).each(function(){
				var _index = $(this).attr('_index');
				if(_index && _index!=''){
					re.push(sslrows[_index]);
				}
			});
			if(re.length>0){
				return re;
			}else{
				alert('请选择一条数据');
				return false;
			}
		},
		clearSelected:function(){
			var o = this,p = this.options,dg = this.element;
			$('tr.selected',o.$body).removeClass('selected');
			$('tr input[type=checkbox]',o.$body).attr('checked',false);
			var too_b = $('.gridb',o.$bodyWrapper);
    		$(too_b).hide();
		},
		addRow:function(row){
			/*
			 * row:{"status":"insert","cell":{.......}}
			 * */
			var o = this,p = this.options,dg = this.element;
			var ds = p.datastore;
			if(row.status && row.cell){
				row.status = 'insert';
				var rowsets = ds.getRowSet();
				rowsets.push(row);
				o._loadData();
			}else{
				return false;
			}
		},
		addRows:function(rows){
			/*
			 * row:[{"status":"insert","cell":{.......}},
			 *      {"status":"insert","cell":{.......}},
			 *      {"status":"insert","cell":{.......}},
			 *      {"status":"insert","cell":{.......}},
			 *      {"status":"insert","cell":{.......}}
			 * 	   ]
			 * */
			var o = this,p = this.options,dg = this.element;
			if(rows!=null && rows!=[] && rows.length>0){
				$(rows).each(function(index,row){
					o.addRow(row);
				});
			}
		},
		updateRow:function(row){
			/*
			 * 更新数据  刷新数据
			 * row:{"status":"insert","cell":{.......}}
			 * */
			var o = this,p = this.options,dg = this.element;
			var ds = p.datastore;
			var rowsets = ds.getRowSet();
			var trs = $('tr.selected',o.$body);
			if(trs.size()>0 && trs.size()<2){
				$.each(trs,function(){
					var index = $(this).attr('_index');
					if(index && index!=''){
						if(row.status == 'update'){
							rowsets[index].status = 'update';
						}else{
							rowsets[index].status = row.status;
						}
						rowsets[index].cell = row.cell;
					}
				});
				o._loadData();
			}
		},
		updateRowN:function(row){
			/*
			 * 更新数据  不刷新数据
			 * row:{"status":"insert","cell":{.......}}
			 * */
			var o = this,p = this.options,dg = this.element;
			var ds = p.datastore;
			var rowsets = ds.getRowSet();
			var trs = $('tr.selected',o.$body);
			if(trs.size()>0 && trs.size()<2){
				$.each(trs,function(){
					var index = $(this).attr('_index');
					if(index && index!=''){
						rowsets[index].status = 'update';
						rowsets[index].cell = row.cell;
					}
				});
				//o._loadData();
			}
		},
		delRow:function(){
			/*
			 * row:{"status":"insert","cell":{.......}}
			 * */
			var o = this,p = this.options,dg = this.element;
			var ds = p.datastore;
			var rowsets = ds.getRowSet();
			var trs = $('tr.selected',o.$body);
			if(trs.size()>0){
				$.each(trs,function(){
					var index = $(this).attr('_index');
					if(index && index!=''){
						rowsets[index].status = 'delete';
					}
				});
				o._loadData();
			}
		},
		ReldelRow:function(){
			/*
			 * row:{"status":"insert","cell":{.......}}
			 * */
			var o = this,p = this.options,dg = this.element;
			var ds = p.datastore;
			var rowsets = ds.getRowSet();
			var trs = $('tr.selected',o.$body);
			if(trs.size()>0){
				$.each(trs,function(){
					var index = $(this).attr('_index');
					if(index && index!=''){
						//delete rowsets[index];
						rowsets.remove(index);
					}
				});
				o._loadData();
			}
		},
		getInsertRows:function(){
			var o = this,p = this.options,dg = this.element;
			var ds = p.datastore;
			var rowsets = ds.getRowSet();
			var insertrow = [];
			$.each(rowsets,function(index,rowset){
				if(rowset.status == 'insert'){
					insertrow.push(rowset);
				}
			});
			if(insertrow.length>0){
				return insertrow;
			}else{
				return false;
			}
		},
		getDeleteRows:function(){
			var o = this,p = this.options,dg = this.element;
			var ds = p.datastore;
			var rowsets = ds.getRowSet();
			var deleterow = [];
			$.each(rowsets,function(index,rowset){
				if(rowset.status == 'delete'){
					deleterow.push(rowset);
				}
			});
			if(deleterow.length>0){
				return deleterow;
			}else{
				return false;
			}
		},
		getUpdateRows:function(){
			var o = this,p = this.options,dg = this.element;
			var ds = p.datastore;
			var rowsets = ds.getRowSet();
			var updaterow = [];
			$.each(rowsets,function(index,rowset){
				if(rowset.status == 'update'){
					updaterow.push(rowset);
				}
			});
			if(updaterow.length>0){
				return updaterow;
			}else{
				return false;
			}
		},
		getRows:function(){
			var o = this,p = this.options,dg = this.element;
			var ds = p.datastore;
			var rowsets = ds.getRowSet();
			var updaterow = [];
			$.each(rowsets,function(index,rowset){
				updaterow.push(rowset);
			});
			if(updaterow.length>0){
				return updaterow;
			}else{
				return false;
			}
		},
		getEdtPanel:function(){
			var o = this,p = this.options,dg = this.element;
			if(o._showEdtPanel){
				return true;
			}else{
				return false;
			}
		},
		reload:function(opt){
			var o = this,p = this.options,dg = this.element;
			$(o.too_b).hide();
			if(opt){
				this.options = $.extend({},p,opt);
				o._loadData();
			}else{
				o._loadData();
			}
		},
		reloadByParms:function(parms){
			var o = this,p = this.options,dg = this.element;
			$(o.too_b).hide();
			if(parms && parms!=null && parms!=''){
				this.options.parms = parms;
				o._loadData();
			}
		},
		showBusbars:function(tag){
			var o = this,p = this.options,dg = this.element;
			p.showBusButton = true;
			if(!tag){
				$('.toolbtn a',o.$title).show();
			}
		},
		hideBusbars:function(tag){
			var o = this,p = this.options,dg = this.element;
			p.showBusButton = false;
			if(!tag){
				$('.toolbtn a',o.$title).hide();
			}
		},
		showTitbars:function(){
			var o = this,p = this.options,dg = this.element;
			$('.toolbtn a',o.$title).show();
		},
		hideTitbars:function(){
			var o = this,p = this.options,dg = this.element;
			$('.toolbtn a',o.$title).hide();
		},
		getParms:function(){
			var o = this,p = this.options,dg = this.element;
			if(p.parms && p.parms!='' && p.parms != {} && p.parms!='{}'){
				return p.parms;
			}else{
				return false;
			}
		},
		getOrding:function(){
			var o = this,p = this.options,dg = this.element;
			if(p.sortname && p.sortorder) {
				return p.sortname + ' ' + p.sortorder;
			}else{
				return false;
			}
		},
		setFootCon:function(con){
			var o = this,p = this.options,dg = this.element;
			var heji = $("#"+o.id+"-pagebar").find('.dgheji');
			if(con && con!=''){
				$(heji).html('');
				$(heji).html(con);
			}
		},
		getDataCenter:function(){
			var o = this,p = this.options,dg = this.element;
			if(p.datacenter && p.datacenter != null){
				return p.datacenter;
			}else{
				return false;
			}
		},
		clearDataStore:function(){
			var o = this,p = this.options,dg = this.element;
			
		}
	};
	
	$.fn[pluginName] = function(options,params){
		var obj = null;
		this.each(function(){
			if (!$.data(this,"plugin_" + pluginName)){
				obj = $.data(this,"plugin_" + pluginName,new Plugin(this,options));
			}else{
				obj = $.data(this,"plugin_" + pluginName);
			}
		});
		if (typeof options == "string") {
			return obj[options](params);
		}
		return obj;
	};
	
	$.fn.sortElements = (function(){
		var sort = [].sort;
		return function(comparator, getSortable){
			getSortable = getSortable || function(){return this;};
			var placements = this.map(function(){
				var sortElement = getSortable.call(this),
				parentNode = sortElement.parentNode,
				nextSibling = parentNode.insertBefore(
						document.createTextNode(''),
						sortElement.nextSibling
				);
				return function(){
					if (parentNode === this){
						throw new Error("You can't sort elements if any one is a descendant of another.");
	                   }
					parentNode.insertBefore(this, nextSibling);
					parentNode.removeChild(nextSibling);
				};
			});
			return sort.call(this, comparator).each(function(i){
				placements[i].call(getSortable.call(this));
			});
		};
	})();
})(jQuery,window,document);
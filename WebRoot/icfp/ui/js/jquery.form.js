;(function ($,window,document,undefined){
	
	var pluginName = "dataform";
	
	var defaults = {
		
	};
	
	var HideSelect = function(oTarget){
		var o = this,p = this.options,df = this.element;
		var ulVisible = $('.selectleft ul:visible');
		ulVisible.each(function(){
			var oSelect = $(this).parents(".selectleft:first").find("select").get(0);
			if(!(oTarget && oSelect.oLabel && oSelect.oLabel.get(0) == oTarget.get(0)) ){
				$(this).hide();
			}
		});
	};
	
	var CheckExternalClick = function(event){
		var o = this,p = this.options,df = this.element;
		if ($(event.target).parents('.selectleft').length === 0) { 
			HideSelect($(event.target)); 
		}
	};
	
	function Plugin(element,options){
		this.element = element;
		this.options = $.extend({},defaults,options);
		this._defaults = defaults;
		this._name = pluginName;
		this.init();
    }
	
	Plugin.prototype = {
		init:function(){
			var o = this,p = this.options,df = this.element;
			var inputs = $('input:text, input:password',df);
			var selects = $('select',df);
			var textareas = $('textarea',df);
			if(inputs && inputs.size()>0){
				o.init_input(inputs);
			}
			if(selects && selects.size()>0){
				o.init_select(selects);
				$(document).mousedown(CheckExternalClick);
			}
			if(textareas && textareas.size()>0){
				o.init_textarea(textareas);
			}
		},
		init_input:function(inputs){
			var o = this,p = this.options,df = this.element;
			$.each(inputs,function(index,input){
				if($(input).hasClass('hasdone')||!$(input).is('input')) {
					return;
				}
				$(input).addClass('hasdone');
				$(input).wrap('<div class="inputleft"><div class="inputright"><div></div></div></div>');
				var $wrapper = $(input).parent().parent().parent();
				$(input).focus(function(){
					$wrapper.addClass("inputleft_focus");
				}).blur(function(){
					$wrapper.removeClass("inputleft_focus");
				}).hover(function(){
					$wrapper.addClass("inputleft_hover");
				},function(){
					$wrapper.removeClass("inputleft_hover");
				});
				if($.browser.safari){
					$wrapper.addClass('inputSafari');
					$wrapper.css('width','95%');
					$(input).css('width','95%');
				}
				if($.browser.mozilla){
					$wrapper.addClass('inputSafari');
					$wrapper.css('width','95%');
					$(input).css('width','95%');
				}
			});
		},
		init_select:function(selects){
			var o = this,p = this.options,df = this.element;
			$.each(selects,function(index,select){
				if($(select).hasClass('selectHidden')){
					return;
				}
				if($(select).attr('multiple')){
					return;
				}
				var $wrapper = $(select).addClass('selectHidden').wrap('<div class="selectleft"></div>').parent().css({zIndex:10-index});
				$wrapper.prepend('<div><span></span><a href="javascript:void(0);" class="selectright"></a></div><ul></ul>');
				var $ul = $('ul', $wrapper).css('width','95%').hide();
				$('option',select).each(function(i){
					var oLi = $('<li><a href="#" index="'+ i +'">'+ $(this).html() +'</a></li>');
					$ul.append(oLi);
				});
				$ul.find('a').click(function(){
					$('a.selected', $wrapper).removeClass('selected');
					$(this).addClass('selected');
					if($(select)[0].selectedIndex != $(this).attr('index') && $(select)[0].onchange) { 
						$(select)[0].selectedIndex = $(this).attr('index'); 
						$(select)[0].onchange(); 
					}
					$(select)[0].selectedIndex = $(this).attr('index');
					$('span:eq(0)',$wrapper).html($(this).html());
					$ul.hide();
					return false;
				});
				$('a:eq('+ select.selectedIndex +')', $ul).click();
				$('span:first',$wrapper).click(function(){
					$("a.selectright",$wrapper).trigger('click');
				});
				$('a.selectright',$wrapper).click(function(){
					if($ul.css('display') == 'none'){
						HideSelect();
					} 
					if($(select).attr('disabled')){
						return false;
					}
					$ul.slideToggle('fast',function(){					
						var offSet = ($('a.selected', $ul).offset().top - $ul.offset().top);
						$ul.animate({scrollTop: offSet});
					});
					return false;
				});
				var oSpan = $('span:first',$wrapper);
				$wrapper.css('width','96.5%');
				$ul.css('width','99%');
				oSpan.css({width:'96.5%'});
				if($.browser.safari){
					$wrapper.css('width','95%');
					$ul.css('width','99%');
					oSpan.css({width:'95%'});
				}
				if($.browser.mozilla){
					$wrapper.css('width','95%');
					$ul.css('width','99%');
					oSpan.css({width:'95%'});
				}
			});
		},
		init_textarea:function(textareas){
			$.each(textareas,function(index,textarea){
				
			});
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
	
})(jQuery,window,document);
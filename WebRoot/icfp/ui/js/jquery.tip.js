;(function ($,window,document,undefined){
	
	var pluginName = "tip";
	
	var defaults = {
		position : "t",	//箭头指向上(t)、箭头指向下(b)、箭头指向左(l)、箭头指向右(r)
		value : 23      //小箭头偏离左边和上边的位置			
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
			var o = this,p = this.options,tp = this.element;
			var $btip = $(".v_tip");
			var offset,h ,w ;	
			var timer;
			if($btip.size()<1){
				var html = [
					'<div class="v_tip">',
						'<i class="triangle-'+p.position+'"></i>',
						'<div class="tl">',
							'<div class="inner">',
								'<div class="cont"></div>',
							'</div>',
						'</div>',
						'<div class="tr"></div>',
						'<div class="bl"></div>',
					'</div>'
				];
				$("body").prepend(html.join(''));
				$btip = $(".v_tip");
			}
			$(tp).die().live("mousemove",function(){
				clearInterval(timer);
				offset = $(this).offset();
				h = $(this).height();
				w = $(this).width();
				var _tips = $(tp).attr('_vtip');
				if(_tips == null || _tips.trim()===''){
					return;
				}
				$(".cont",$btip).html('<img src="icfp/ui/exp/artDialog/skins/icons/warning.png" width="20" height="20">&nbsp;&nbsp;'+_tips);
				switch(p.position){
					case "t" ://当它是上面的时候
						$(".triangle-t").css('left',p.value);
						$btip.css({
							"left":offset.left,
							"top":offset.top+h+14
						}).show();
						break;
					case "b" ://当它是下面的时候
						$(".triangle-b").css('left',p.value);
						$btip.css({ 
							"left":offset.left,  
							"top":offset.top-h-7-$btip.height()  
						}).show();
						break;
					case "l" ://当它是左边的时候		
						$(".triangle-l").css('top',p.value);
						$btip.css({ 
							"left":offset.left+w+10, 
							"top":offset.top+h/2-7-p.value 
						}).show();
						break;
					case "r" ://当它是右边的时候			
						$(".triangle-r").css('top',p.value);
						$btip.css({ 
							"left":offset.left-20-$btip.width(),  
							"top":offset.top+h/2-7-p.value 
						}).show();
						break;
				}
			});
			
			$(tp).live("mouseout",function(){
				$btip.hide();
			});
				
			$btip.live("mousemove",function(){
				clearInterval(window.timer);
				$btip.show();
			});
			
			$btip.live("mouseout",function(){
				$btip.hide();
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
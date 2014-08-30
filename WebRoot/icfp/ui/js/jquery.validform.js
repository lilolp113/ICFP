;(function ($,window,document,undefined){
	
	var pluginName = "validForm";
	
	var defaults = {
		
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
			var o = this,p = this.options,vf = this.element;
			var selects = $('select',vf);
			//if(selects && selects.size()>0){
				//$.each(selects,function(index,select){
					//$(select).wrap('<span></span>');
				//});
			//}
			$.metadata.setType("attr","validate");
			icfp.form.LimitLength(vf);
			$("input[type='text'],textarea,select",vf).each(function(){
				$(this).tip();
			});
			var opt = {
				errorPlacement: function(lable,element){
				  	if(!element.attr("id")){
				  		var id = new Date().getTime()
	                    element.attr("id", id);
	                    element.attr("name",id);
	                }
					if($(element).is('input')){
						element.css('border-color','#FF2D2D');
					}
					if($(element).is('textarea')){
						element.css('border-color','#FF2D2D');
					}
					if($(element).is('select')){
						if($.browser.safari){
							element.css('border-color','#FF2D2D');
						}else if($.browser.mozilla){
							element.css('border-color','#FF2D2D');
						}else{
							//if($(element).parent().is('span')){
								//$(element).parent().css('border','solid 1px #FF2D2D');
							//}
							element.css('border-color','#FF2D2D');
						}
					}
					var tiphtml = lable.html();
					$(element).attr("_vtip",tiphtml);
					var $btip = $(".v_tip");
					if($btip.size()>0){
						if(tiphtml!=null && tiphtml.trim()!=''){
							$(".cont",$btip).html('<img src="icfp/ui/exp/artDialog/skins/icons/warning.png" width="20" height="20">&nbsp;&nbsp;'+tiphtml);
						}else{
							$btip.hide();
						}
					}
				},
				success: function(lable){
					var element = $("#" + lable.attr("for"));
					if($(element).is('input')){
						element.css('border','1px solid #6688AA');
						element.css('border-color','#89a #bcd #bcd #9ab');
					}
					if($(element).is('textarea')){
						element.css('border','1px solid #6688AA');
						element.css('border-color','#89a #bcd #bcd #9ab');
					}
					if($(element).is('select')){
						if($.browser.safari){
							element.css('border','1px solid #6688AA');
							element.css('border-color','#89a #bcd #bcd #9ab');
						}else if($.browser.mozilla){
							element.css('border','1px solid #6688AA');
							element.css('border-color','#89a #bcd #bcd #9ab');
						}else{
							//if($(element).parent().is('span')){
								//$(element).parent().css('border','none');
							//}
							element.css('border','1px solid #6688AA');
							element.css('border-color','#89a #bcd #bcd #9ab');
						}
					}
				}
			};
			$(vf).validate(opt);
		},
		valid:function(){
			var o = this,p = this.options,vf = this.element;
			return $(vf).valid();
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
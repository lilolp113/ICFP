;
(function($) {
	$.fn.SmohanPopLayer = function(options) {
		var Config = {
			Shade : true,
			Event : "click",
			Content : "Content",
			Title : "Smohan.net"
		};
		var options = $.extend(Config, options);
		var layer_width = $('#' + options.Content).outerWidth(true);
		var layer_height = $('#' + options.Content).outerHeight(true)
		var layer_top = (layer_height + 40) / 2;
		var layer_left = (layer_width + 40) / 2;
		var load_left = (layer_width - 36) / 2;
		var load_top = (layer_height - 100) / 2;
		var layerhtml = "";
		if (options.Shade == true) {
			layerhtml += '<div class="Smohan_Layer_Shade" style="display:none;"></div>';
		}
		layerhtml += '<div class="Smohan_Layer_box" style="width:'
				+ layer_width + 'px;height:' + layer_height
				+ 'px; margin-top:-' + layer_top + 'px;margin-left:-'
				+ layer_left + 'px;display:none;" id="layer_' + options.Content
				+ '">';
		layerhtml += '<h3><b class="text">' + options.Title + '</b><a href="javascript:void(0)" class="close"></a></h3>';
		layerhtml += '<div class="layer_content">';
		layerhtml += '<div class="loading" style="left:' + load_left
				+ 'px;top:' + load_top + 'px;"></div>';
		layerhtml += '<div id="' + options.Content
				+ '" style="display:block;">' + $("#" + options.Content).html()
				+ '</div>';
		layerhtml += '</div>';
		layerhtml += '</div>';
		$('body').prepend(layerhtml);
		if (options.Event == "unload") {
			$('#layer_' + options.Content).animate( {
				opacity : 'show',
				marginTop : '-' + layer_top + 'px'
			}, "slow", function() {
				$('.Smohan_Layer_Shade').show();
				$('.Smohan_Layer_box .loading').hide();
			});
		} else {
			$(this).live(options.Event, function(e) {
				if(e.target.tagName == 'DIV'){
					return;
				}
				$('#layer_' + options.Content).animate( {
					opacity : 'show',
					marginTop : '-' + layer_top + 'px'
				}, "slow", function() {
					$('.Smohan_Layer_Shade').show();
					$('.Smohan_Layer_box .loading').hide();
				});
			});
		}
		$('.Smohan_Layer_box .close').click(function(e) {
			$('.Smohan_Layer_box').animate( {
				opacity : 'hide',
				marginTop : '-300px'
			}, "slow", function() {
				$('.Smohan_Layer_Shade').hide();
				$('.Smohan_Layer_box .loading').show();
			});
		});
	};
})(jQuery);

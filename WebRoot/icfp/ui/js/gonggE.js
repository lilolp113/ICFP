var pwidth = 0;
var pheight = 0;
var width = 0;
var height = 0;
$(function(){
	if($.browser.msie){
		$('.setup-pop').hide();
		$('.smart-push-pre').hide();
	}
	pwidth = $(icfp.win.document).width();
	pheight = $(icfp.win.document).height();
	width = $(document).width();
	height = $(document).height();
	$('.grid ul').html('');
	var tileTmplStr = '<li class="tile">'+
	'<div class="box">'+
		'<a href="javascript:void(0);">'+
			'<div class="tile-add">'+
				'<img src="icfp/ui/skins/default/images/gongg/tile_add.png">'+
				'<img src="icfp/ui/skins/default/images/gongg/tile_addhov.png" style="display:none;">'+
			'</div>'+
		'</a>'+
		'<a href="javascript:void(0);" class="link">'+
			'<div class="tile-logo">'+
				'<img/>'+
				'<div class="remove" title="删除"></div>'+
				//'<div class="tile-tit"></div>'+
			'</div>'+
		'</a>'+
	'</div>'+
	'</li>';
	var menutree = icfp.win.icfp.menu.getMenus();
	var data = icfp.tree.__formatData('0',menutree,'SAE001','SAE004','children');
	initgongg(data);
	function initgongg(data){
		if(data.length==1){
			var rows = data[0]['children'];
			if(rows.length>0){
				$.each(rows,function(index,row){
					var $gg = $(tileTmplStr);
					$('.tile-logo img',$gg).attr('src',"icfp/ui/skins/default/menu/"+row.SAE009+".png");
					$('.tile-tit',$gg).html('<i></i>&nbsp;&nbsp;'+row.SAE002);
					$('.link',$gg).attr('_img',row.SAE009);
					if(row['children']){
						$('.link',$gg).attr('_hasChild',true);
						$('.link',$gg).attr('_SAE001',row.SAE001);
						$('.link',$gg).attr('_index',index);
					}else{
						$('.link',$gg).attr('_hasChild',false);
						$('.link',$gg).attr('_link',row.SAE007);
					}
					$('.link',$gg).attr('_title',row.SAE002);
					$('.grid ul').append($gg);
				});
			}
		}
	}

	(function(){
		if(screen.width == 1024 && screen.height == 768){
			$('.wrap').width('72%');
		}else if(screen.width == 1366 && screen.height == 768){
			$('.wrap').width('56%');
		}else if(screen.width == 1280 && screen.height == 800){
			$('.wrap').width('66%');
		}else if(screen.width == 1280 && screen.height == 1024){
			$('.wrap').width('80%');
		}else if(screen.width == 1680 && screen.height == 1050){
			$('.wrap').width('70%');
		}else if(screen.width == 1440 && screen.height == 900){
			$('.wrap').width('70%');
		}
	})();
	
	var timerResize;
	var noResize = false;
	function wrapResize(animate,callback){
		if(noResize && !animate){
			return true;
		};
		if($.browser.mozilla){
			$('.tile').css('width','24%');
		}
		$('.tile .box, .box img').css('height', imgHeight = Math.floor($('.tile .box').width() * 0.7027) + 'px');
		if($.browser.mozilla){
			$('.tile').css('height', parseInt(imgHeight) + 0 + 'px');
		}else{
			$('.tile').css('height', parseInt(imgHeight) + 6 + 'px');
		}
		var pageHeight = window.innerHeight;
		if(typeof pageWidth != "number"){
			if(document.compatMode == "number"){
				pageHeight = document.documentElement.clientHeight; 
			}else{
				pageHeight = document.body.clientHeight; 
			}
		}
		if(pageHeight >= 616){
			if($(document).height() > pageHeight){
				$('.wrap').css({width:'57%'});
				$('.tile .box, .box img').css('height', imgHeight = Math.floor($('.tile .box').width() * 0.7027) + 'px');
				if($.browser.mozilla){
					$('.tile').css('height', parseInt(imgHeight) + 0 + 'px');
				}else{
					$('.tile').css('height', parseInt(imgHeight) + 6 + 'px');
				}
			}
		}
		var top = left = 0;
		if ($('.wrap .tile').length) {
			top = Math.max(pageHeight / 2 - $('.wrap').height() / 2, 50) + 'px';
			left = Math.max($(window).width() / 2 - $('.wrap').width() / 2, 10) + 'px'
	    } else {
		    top = pageHeight / 2 - $('.wrap').height() + 'px'
		    left = $(window).width() / 2 - $('.wrap').width() / 2 + 'px';
	    }
	    if (animate === true) {
		    $('.wrap').animate({
			    'top': top,
			    'left': left
			}, 400, function(){
				callback && callback();
				icfp.win.icfp.Action.HideBackGrounp();
			})
		}else{
			$('.wrap').css({
				'top': top,
				'left': left
			});
			icfp.win.icfp.Action.HideBackGrounp();
		}
		if (!timerResize) {
			timerResize = setTimeout(function(){
				clearTimeout(timerResize);
				timerResize = null;
				$('.wrap').css('opacity', 1);
			},200);
		}
		return arguments.callee;
	}
	$(window).on('resize', wrapResize(true));
	
	//------------------------------------------------------

	$('.tile-add').live('mouseover', function() {
	    $("img:eq(0)", this).hide();
	    $("img:eq(1)", this).show();
	}).live('mouseleave', function() {
	    $("img:eq(0)", this).show();
	    $("img:eq(1)", this).hide();
	});
	
	$(".tile-add").live('click', function(e) {
		//alert('add')
	});

	$(".setup-tit").click(function(e) {
		var setupPop = $('.setup-pop');
		if($.browser.msie){
			if(setupPop.css('display') == 'none'){
				setupPop.slideDown();
			}else{
				setupPop.slideUp();
			}
		}else{
			setupPop.toggleClass('setup-pop-end');
		}
	});

	$('.smart-push').click(function(e) {
		var setupPush = $('.smart-push-pre');
		if($.browser.msie){
			if(setupPush.css('display') == 'none'){
				setupPush.slideDown();
			}else{
				setupPush.slideUp();
			}
		}else{
			if(setupPush.hasClass('smart-push-pre-end')){
				setupPush.removeClass('smart-push-pre-end');
			}else{
				setupPush.addClass('smart-push-pre-end');
			}
		}
	});

	$('.setup-cls').click(function(){
		var setupPop = $('.setup-pop');
		if($.browser.msie){
			if(setupPop.css('display') == 'none'){
				setupPop.slideDown();
			}else{
				setupPop.slideUp();
			}
		}else{
			setupPop.toggleClass('setup-pop-end');
		}
	});
	
	$(document).mousedown(function(e){
		if(!$(e.target).parents('.setup-pop').length && e.target.className != 'setup-tit'){
			if($('.setup-pop').is(':visible') && !$('.setup-pop').is(':animated')){
				var setupPop = $('.setup-pop');
				setupPop.removeClass('setup-pop-end');
			}
		}
	});
	
	$(icfp.win.document).mousedown(function(e){
		if(!$(e.target).parents('.setup-pop').length && e.target.className != 'setup-tit'){
			if($('.setup-pop').is(':visible') && !$('.setup-pop').is(':animated')){
				var setupPop = $('.setup-pop');
				setupPop.removeClass('setup-pop-end');
			}
		}
	});

	function clearWallpaperSelect(){
		$.each($('.wallpaper ul li'),function(){
			if($(this).hasClass('on')){
				$(this).removeClass('on');
				return;
			}
		});
	}
	
	$('.wallpaper ul li').click(function(){
		if($(this).hasClass('on')){
			return;
		}
		var imgurl = $(this).attr('_img');
		clearWallpaperSelect();
		$(this).addClass('on');
		icfp.win.icfp.changeBG(imgurl);
	});

	function cleargridSelect(){
		$.each($('.gridcount li'),function(){
			if($(this).hasClass('active')){
				$(this).removeClass('active');
				return;
			}
		});
	}

	$('.gridcount li').click(function(){
		if($(this).hasClass('active')){
			return;
		}else{
			var id = $(this).html();
			cleargridSelect();
			$(this).addClass('active');
			changgg(id);
		}
	});

	function changgg(id){
		if(id=='0'){
			for(var i=0;i<20;i++){
				$('.grid ul li:eq('+i+')').hide();
			}
		}else if(id=='8'){
			for(var i=0;i<20;i++){
				if(i>=8){
					$('.grid ul li:eq('+i+')').hide();
				}else{
					$('.grid ul li:eq('+i+')').show();
				}
			}
			changeH(8);
		}else if(id=='12'){
			for(var i=8;i<20;i++){
				if(i>=12){
					$('.grid ul li:eq('+i+')').hide();
				}else{
					$('.grid ul li:eq('+i+')').show();
				}
			}
			changeH(12);
		}else if(id=='16'){
			for(var i=8;i<20;i++){
				if(i>=16){
					$('.grid ul li:eq('+i+')').hide();
				}else{
					$('.grid ul li:eq('+i+')').show();
				}
			}
		}else if(id=='20'){
			for(var i=8;i<20;i++){
				$('.grid ul li:eq('+i+')').show();
			}
		}
	}

	function changeH(id){
		var pageHeight = window.innerHeight;
		if(typeof pageWidth != "number"){
			if(document.compatMode == "number"){
				pageHeight = document.documentElement.clientHeight; 
			}else{
				pageHeight = document.body.clientHeight; 
			}
		}
		var top = 0;
		if ($('.wrap .tile').length) {
			top = Math.max(pageHeight / 2 - $('.wrap').height() / 2, 50);
	    } else {
		    top = pageHeight / 2 - $('.wrap').height();
	    }
		if(id=='8'){
			$('.wrap').css({'top':top});
		}else if(id=='12'){
			$('.wrap').css({'top':top});
		}
	}
	
	$('.link').live('click',function(e){
		if(e.target.tagName == 'DIV'){
			return;
		}
		var $this = this;
		var child = $(this).attr('_hasChild');
		if(child && child == 'true'){
			var rows = data[0]['children'];
			var index =  $(this).attr('_index');
			var SAE001 =  $(this).attr('_SAE001');
			var ch = rows[index];
			var tit = $(this).attr('_title');
			if(ch['SAE001'] == SAE001){
				var chs = ch['children'];
				$('._Share ul').html('');
				$.each(chs,function(i,c){
					$('._Share ul').append('<li><a href="javascript:void(0);" _url="'+c['SAE007']+'">'+c['SAE002']+'</a></li>');
				});
				$('._Share ul li a').click(function(){
					var url = $(this).attr('_url');
					if(url && url!='' && url !='null'){
						//icfp.win.icfp.frame.changeurl(url);
						ejclickmenu($this,url);
					}else{
						icfp.win.icfp.msg.alertWarn("此功能正在努力开发中！",function(){});
					}
				});
				initejMenu(true,tit,$this,false);
			}
		}else{
			var url = $(this).attr('_link');
			if(url && url!='' && url !='null'){
				//icfp.win.icfp.frame.changeurl(url);
				initejMenu(true,'',$this,url);
			}else{
				icfp.win.icfp.msg.alertWarn("此功能正在努力开发中！",function(){});
			}
		}
	});

	$('.remove').live('click',function(e){
	    var tile = $(this).parents('li.tile');
	    removeTile(tile,true);
	    e.preventDefault();
	});

	function removeTile(tile,animate){
		tile = $(tile);
		tile.find('.box').addClass('empty');
		tile.find('.remove').hide();
		var logo = tile.find('.tile-logo');
		if(animate){
			logo.addClass('hide-tit').css({position:'absolute', top:0}).animate({top:-logo.height()}, 400);
		}
	}
	
	/*
	
	function initejMenu(Shade,title){
		var layer_width = $('._Share').outerWidth(true);
		var layer_height = $('._Share').outerHeight(true)
		var layer_top = (layer_height + 40) / 2;
		var layer_left = (layer_width + 40) / 2;
		var load_left = (layer_width - 36) / 2;
		var load_top = (layer_height - 100) / 2;
		$('.Smohan_Layer_box').css({
			'width':layer_width,
			'height':layer_height,
			'margin-top':-layer_top,
			'margin-left':-layer_left
		});
		$('.Smohan_Layer_box .text').html(title);
		$('.Smohan_Layer_box .loading').css({
			'left':load_left,
			'top':load_top
		});
		$('.Smohan_Layer_box').animate( {
			opacity : 'show',
			marginTop : '-' + layer_top + 'px'
		},"slow",function(){
			$('.Smohan_Layer_Shade').show();
			$('.Smohan_Layer_box .loading').hide();
		});
		
		$('.Smohan_Layer_box .close').click(function(e) {
			$('.Smohan_Layer_box').animate( {
				opacity : 'hide',
				marginTop : '-300px'
			}, "slow", function() {
				$('.Smohan_Layer_Shade').hide();
				$('.Smohan_Layer_box .loading').show();
			});
		});
	}

	*/
	function initejMenu(Shade,title,obj,url){
		var li = $(obj).parent().parent();
		var img = $(obj).attr('_img');
		var _top = $(li).offset().top;
		var _left = $(li).offset().left;
		var _width = $(li).width();
		var _height = $(li).height();
		if(url){
			$('#ejmenu').html('');
		}
		$('#ejmenu .text').html(title);
		$('#ejmenu').css({
			top:_top,
			left:_left,
			width:_width,
			height:_height,
			background:"url('icfp/ui/skins/default/menu/"+img+''+img+".png')"
		});
		$(li).animate( {
			opacity : '.1'
		},"slow",function(){
			$(li).css('visibility','hidden');
		});
		var ww = $(document).width();
		var hh = $(document).height();
		var ll = (ww-580)*0.5;
		var tt = (hh-240)*0.5;
		$('.Smohan_Layer_Shade').show();
		$('#ejmenu').animate( {
			opacity : 'show',
			top:tt,
			left:ll,
			width:580,
			height:240
		},"slow",'easeOutBounce_restore',function(){
			if(url){
				$('#ejmenu').animate( {
					opacity : '0',
					top:0,
					left:0,
					width:ww,
					height:hh
				},'slow',function(){
					icfp.win.icfp.frame.changeurl(url);
				});
			}
		});
		$('.Smohan_Layer_Shade').not('#ejmenu').unbind( "click");
		
		$('.Smohan_Layer_Shade').not('#ejmenu').click(function(){
			$('#ejmenu').animate( {
				top:_top,
				left:_left,
				width:_width,
				height:_height
			},"slow",'easeOutBounce_restore',function(){
				$('.Smohan_Layer_Shade').hide();
				$('#ejmenu').hide();
				$(li).css({'visibility':'visible','opacity':'1'});
			});
		});
		
		$('#ejmenu a.close').unbind( "click");
		
		$('#ejmenu a.close').click(function(){
			$('#ejmenu').animate( {
				top:_top,
				left:_left,
				width:_width,
				height:_height
			},"slow",'easeOutBounce_restore',function(){
				$('.Smohan_Layer_Shade').hide();
				$('#ejmenu').hide();
				$(li).css({'visibility':'visible','opacity':'1'});
			});
		});
		
	}
	
	
	function ejclickmenu(obj,url){
		var ww = $(document).width();
		var hh = $(document).height();
		$('#ejmenu').animate( {
			opacity : '0',
			top:0,
			left:0,
			width:ww,
			height:hh
		},'slow',function(){
			icfp.win.icfp.frame.changeurl(url);
		});
	}
	
	$('body').noSelect();
});
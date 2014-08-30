$(function(){
	$("#bg").ezBgResize();
	var height = $('body').height();
	$('#comment').css('height',height-100);
	$(window).resize(function(){
		var _height = $('body').height();
		$('#comment').css('height',_height-100);
	});
	$('#menubar').click(function(){
		if($('#menubar').hasClass('tog')){
			menudown();
		}else if($('#menubar').hasClass('togclose')){
			menuup();
		}
	});

	function menudown(){
		if($('#menubar').hasClass('tog')){
			$('#menubar').animate({top:'70px'},300).addClass("togclose").removeClass("tog").html('<span>关闭吧</span>');
			$('.tog_contact').slideDown(300);
		}
	}

	function menuup(){
		if($('#menubar').hasClass('togclose')){
			$('#menubar').animate({top:'0px'},300).addClass("tog").removeClass("togclose").html('<span>点一下</span>');
			$('.tog_contact').slideUp(300);
		}
	}

	icfp.win.icfp.cid = "${args['_cid']!''}";
	icfp.win.icfp.roleid = "${args['roleid']!''}";
	icfp.win.icfp.sysdate = "${args['sysdate']!''}";
	var menutrees = icfp.win.icfp.menu.getMenus();
	var tree = icfp.tree.__formatData('0',menutrees,'SAE001','SAE004','children');
	initTopmenu(tree);
	function initTopmenu(tree){
		if(tree.length==1){
			var menutree = tree[0]['children'];
			if(menutree){
				$.each(menutree,function(index,menu){
					var a = $('<a></a>');
					if(menu['children']){
						$(a).attr('_haschild','true');
					}else{
						$(a).attr('_haschild','false');
					}
					if(menu['SAE007'] != ''){
						$(a).attr('_url',menu['SAE007']);
					}
					$(a).html(menu['SAE002']);
					$(a).click(function(){
						if(menu['children']){
							initSTopmenu(menu['children'],null,this);
						}else{
							initSTopmenu(false,menu['SAE007'],this);
						}
					});
					$('.miaov_box_foot').append(a);
				});
			}
		}
	}

	function clearTopmenuSelect(){
		$.each($('.miaov_box_foot a'),function(){
			if($(this).hasClass("show")){
				$(this).removeClass("show");
				return;
			}
		});
	}
	
	function initSTopmenu(child,url,obj){
		var left = $(obj).offset().left+($(obj).width()*0.5);
		$('.miaov_box_foot span').animate({'left':left},300,function(){
			clearTopmenuSelect();
			$(obj).addClass('show');
		});
		if(child){
			$('.miaov_box').html('<ul class="miaov_box_head"></ul>');
			$.each(child,function(k,v){
				var li = $('<li></li>');
				var a = $('<a></a>');
				$(a).html(v['SAE002']);
				$(a).click(function(){
					var url = v['SAE007'];
					if(url && url!=null && url!='' && url!='null'){
						icfp.frame.changeurl(url);
						menuup();
					}else{
						icfp.msg.alertWarn("此功能正在努力开发中！",function(){});
					}
				});
				$(li).html($(a));
				$('.miaov_box_head').append($(li));
			});
			var liw = $('.miaov_box_head li').width();
			var sleft = $('.miaov_box').width()/2 - (liw*child.length)/2;
			$('.miaov_box_head').animate({'margin-left':sleft},300);
		}else{
			$('.miaov_box').html('暂无下级子菜单');
			if(url && url!=null && url!='' && url!='null'){
				icfp.frame.changeurl(url);
				menuup();
			}else{
				icfp.msg.alertWarn("此功能正在努力开发中！",function(){});
			}
		}
	}

	$('#bind_home').click(function(){
		icfp.frame.changeurl('REQ_S_999_03');
	});

	$('#bind_logout').click(function(){
		icfp.win.icfp.msg.confirm("是否确认返回登陆页？",function(v){
			if(v){
				location.href = 'CoreAction.do?requestId=REQ_S_999_04&mode=1';
			}
		});
	});
	
	var uu = Math.random()*10000
	$('#mainframe').attr('src',"CoreAction.do?requestId=REQ_S_999_03&mode=1&uu="+uu);

	$('.leftbar a').hover(function(){
		$('.stores').html('');
		var o = this;
		var top = $(o).offset().top;
		$(this).stop().animate({left:"0px"},300,'easeOutBack',function(){
			$('.stores').stop().animate({'marginLeft':'0px','top':top+35},600,'easeInOutBack',function(){
				$('.stores').html($('.leftbar a').index(o));
			});
		});
		
		
	},function(){
		$(this).stop().animate({left:"-110px"},300,'easeOutBack',function(){
			$('.stores').stop().animate({'marginLeft':'-650px'},600,'easeOutBack');
		});
	});
	
});
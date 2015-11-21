$(function(){
	var htmltemp = [
	    '<li class="reward-item curreward-item reward-unfinished clearfix handpointer">',
		    '<div class="item-left item-icon">',
				'<img src="" title="" alt="" class="item-img">',
			'</div>',
			'<div class="item-center item-det clearfix">',
				'<div class="det-title">',
					'<h4 class="title-h4"></h4>',
					'<span class="title-des"></span>',
				'</div>',
				'<div class="level-sco clearfix">',
					'<div class="det-score">',
						'<span class="sco-total clearfix">',
							'<i class="sco-left sco-process"></i>',
							'<i class="sco-center sco-process"></i>', 
							'<i class="sco-right sco-process"></i>',
						'</span>',
						'<span class="sco-current clearfix">', 
							'<i class="sco-left sco-process"></i>', 
							'<i class="sco-center sco-process" style="width: 0px"></i>',
							'<i class="sco-right sco-process"></i>',
						'</span>',
						'<span class="sco-detail">',
							'<i class="detail-cur"></i>',
						'</span>',
					'</div>',
				'</div>',
				'<div class="r-center-show clearfix">',
				'</div>',
			'</div>',
			'<div class="item-right item-des">',
				'<a class="forie6" href="" onclick="return false;">',
					'<div class="des-wrapper des-notime">',
						'<span class="des-score">10</span>',
						'<span class="des-time"></span>',
					'</div>',
					'<div class="des-show r-desshow-norepeat"><span>最大级别</span></div>',
				'</a>',
			'</div>',
			'<div class="item-bottom item-slid">',
				'<a class="slid-btn slid-down" title="点击查看详情"></a>',
			'</div>',
		'</li>'
	];
	var httpService = new HttpService();
	httpService.putBusinessRequestId("REQ_Q_800_01");
	httpService.putBusinessID("BSN800");
	httpService.addEventListener(icfp.FaultEvent,fal);
	httpService.addEventListener(icfp.ResultEvent,suc);
	httpService.post();
	var zd01rowset = null;
	var zd05rowset = null;
	var zd05rowsetall = null;
	var zd06rowset = null;
	
	function fal(dc){
		var msg = dc.getDetail();
     	icfp.win.icfp.msg.alertError(msg,function(){});
	}
	function suc(dc){
		if(dc.getCode() == icfp.Action.statusCode.ok){
			var zd01ds = dc.getDataStore('zd01ds');
			var zd05ds = dc.getDataStore('zd05ds');
			var zd06ds = dc.getDataStore('zd06ds');
			var zd05dsall = dc.getDataStore('zd05dsAll');
			zd01rowset = zd01ds.getRowSet();
			zd05rowset = zd05ds.getRowSet();
			zd06rowset = zd06ds.getRowSet();
			zd05rowsetall = zd05dsall.getRowSet();
			init_Chengjiu(zd05rowset);
			init_MyChengjiu(zd06rowset);
		}
	}
	
	$('span.select-tag').click(function(){
		var left = $(this).offset().left - $('div.s-select-curreward').offset().left;
		$('div.select-vernier').stop().animate({'left':left},300);
		if($(this).hasClass('all-tag')){
			ChangeTabChengjiu("all");
		}else if($(this).hasClass('cur-tag')){
			ChangeTabChengjiu("cur");
		}
	});
	
	
	function init_Chengjiu(rowsets){
		if(rowsets!=null && rowsets.length>0){
			var length = rowsets.length;
			$('span.all-tag .num').html(length);
			$('ul.reward-ul').html('');
			$.each(rowsets,function(index,rowset){
				var $temp = $(htmltemp.join(" "));
				var title = rowset.cell['ZDE013'];
				var img =  rowset.cell['ZDE017'];
				var con = rowset.cell['ZDE016'];
				$('div.item-icon img',$temp).attr("src","achievement/skin/icons/"+img);
				$('div.det-title h4.title-h4',$temp).html(title);
				$('div.det-title span.title-des',$temp).html(con+"。");
				
				var hlevel = rowset.cell['ZDE005'];
				var shenj = rowset.cell['ZDE003'];
				if(shenj == '0'){
					$('div.level-sco',$temp).html('');
					$('.des-show span',$temp).html("勋章状态");
					$('.des-wrapper .des-score',$temp).html("未获得");
				}else if(hlevel != "0"){
					//$('.des-show span',$temp).html("最大");
					//$('.des-wrapper .des-score',$temp).html("LV"+rowset.cell['ZDE005']);
					$('.des-show span',$temp).html("勋章状态");
					$('.des-wrapper .des-score',$temp).html("未获得");
					$('div.level-sco i.detail-cur',$temp).html("LV0/LV"+hlevel+"");
					$('div.level-sco',$temp).attr('hlevel',hlevel);
					if(zd05rowsetall!=null && zd05rowsetall.length>0){
						var ul = ["<ul>"];
						$.each(zd05rowsetall,function(_index,rowseta){
							if(title == rowseta.cell['ZDE013']){
								var li = "<li>等级LV"+rowseta.cell['ZDE005']+"："+rowseta.cell['ZDE011']+"</li>";
								ul.push(li);
							}
						});
						ul.push("</ul>");
						$('div.r-center-show',$temp).html(ul.join(" "));
					}
				}
				$($temp).click(function(){
					if($('a.slid-btn',this).hasClass('slid-down')){
						$('a.slid-btn',this).removeClass('slid-down');
						$('a.slid-btn',this).addClass('slid-up');
						var _height = $('div.r-center-show',this).height() + 10;
						$('div.item-center',this).animate({'height':70+_height},300);
					}else if($('a.slid-btn',this).hasClass('slid-up')){
						$('a.slid-btn',this).removeClass('slid-up');
						$('a.slid-btn',this).addClass('slid-down');
						$('div.item-center',this).animate({'height':70},300);
					}
				});
				$('ul.reward-ul').append($temp);
			});
		}else{
			$('ul.reward-ul').html('');
		}
	}
	
	function init_MyChengjiu(rowsets){
		if(rowsets!=null && rowsets.length>0){
			var length = rowsets.length;
			$('span.cur-tag .num').html(length);
			$.each(rowsets,function(index,rowset){
				var title = rowset.cell['ZDE013'];
				var $li = getli(title);
				if($li){
					$li.attr('_tag','my');
					if($li.hasClass('curreward-item')){
						$li.removeClass('curreward-item');
					}
					if($li.hasClass('reward-unfinished')){
						$li.removeClass('reward-unfinished');
					}
					$li.addClass('reward-done');
					var date = rowset.cell['ZDF002'];
					date = date.substring(0,10);
					$('.des-wrapper .des-time',$li).html(date);
					var lv = rowset.cell['ZDE005'];
					var shenj = rowset.cell['ZDE003'];
					if(shenj == '0'){
						$('.des-wrapper .des-score',$li).html("已获得");
						$('.des-show span',$li).html("勋章状态及获得时间");
					}else{
						$('.des-wrapper .des-score',$li).html("LV"+lv);
						$('div.det-title span.title-des',$li).append("(当前"+title+"为："+rowset.cell['ZDF003']+")");
						$('.des-show span',$li).html("当前获得级别及时间");
						var hlevel = $('div.level-sco',$li).attr('hlevel');
						if(hlevel){
							$('div.level-sco i.detail-cur',$li).html("LV"+lv+"/LV"+hlevel+"");
							lv = lv*1;
							hlevel = hlevel*1;
							$('div.level-sco span.sco-current i.sco-center',$li).css('width',(lv/hlevel)*410);
						}
					}
					if($('.des-wrapper',$li).hasClass('des-notime')){
						$('.des-wrapper',$li).removeClass('des-notime');
					}
				}
			});
		}else{
			$('span.cur-tag .num').html("0");
		}
	}
	
	function getli(title){
		var re = false;
		$.each($('ul.reward-ul li.reward-item'),function(index,li){
			var _title = $('div.det-title h4.title-h4',li).html();;
			if(_title!=null && _title !='' && _title == title){
				re = $(this);
				return false;
			}
		});
		return re;
	}
	
	function ChangeTabChengjiu(tag){
		if(tag=="all"){
			$.each($('#rList li.reward-item'),function(index,li){
				$(this).show();
			});
		}else if(tag=="cur"){
			$.each($('#rList li.reward-item'),function(index,li){
				var _tag = $(this).attr('_tag');
				if(_tag!=null && _tag !='' && _tag == 'my'){
					$(this).show();
				}else{
					$(this).hide();
				}
			});
		}
	}
	
});
$(function(){
	$(".menutab ul.tabtitle li a").wrapInner('<span class="out"></span>');
	$(".menutab ul.tabtitle li a").each(function() {
		$( '<span class="over">' +  $(this).text() + '</span>' ).appendTo( this );
	});
	$(".menutab ul.tabtitle li a").hover(function(){
		$(".out",this).stop().animate({'top':'45px'},200); // move down - hide
		$(".over",this).stop().animate({'top':'0px'},200); // move down - show
	
	},function(){
		$(".out",this).stop().animate({'top':'0px'},200); // move up - show
		$(".over",this).stop().animate({'top':'-45px'},200); // move up - hide
	});
});


function changeTab(obj){
	var $that = $(obj);
	var _tabt = $($that).attr('id');
	if(!_tabt || _tabt ==''){
		return;
	}
	if($($that).hasClass('current')){
		return;
	}
	if(_tabt.indexOf('-') != '-1'){
		var __tabt = _tabt.split('-');
		_tabt = __tabt[1];
	}
	var __id = 'tabi-' + _tabt;
	$.each($($that).parent().find('li'),function(index){
		if($(this).hasClass('current')){
			$(this).removeClass('current');
		}
	});
	$.each($(".tabinfo > div",$($that).parent().parent()),function(index){
		if($(this).css('display')==='block'){
			$(this).hide();
		}
	});
	$($that).addClass('current');
	$('#'+ __id,$($that).parent().parent()).show(function(){
		var dg = $(this).find('.dgGrid');
		$.each(dg,function(){
			$(this).DataGrid('ldivHideShow');
		});
		
	});
}
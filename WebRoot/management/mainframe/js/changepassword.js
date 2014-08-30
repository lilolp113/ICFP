$(function(){
	var height = $(document).height();
	$('.formcontent').height(height-40);
	$('.formbar').height(40);
	
	$('#edtform').validForm();
	icfp.form.rememberValue('edtform');
	
	$("#save").click(function(){
		if($('#edtform').validForm('valid')){
			var key1 = $('#key1').val();
			var key2 = $('#key2').val();
			var key3 = $('#key3').val();
			if(key2 != key3){
				$('#key2').val('');
				$('#key3').val('');
				icfp.win.icfp.msg.alertWarn("对不起，两次输入新密码不一致！",function(){});
				return false;
			}
			function fal(){
				$('#key1').val('');
				$('#key2').val('');
				$('#key3').val('');
				var msg = dc.getDetail();
	         	icfp.win.icfp.msg.alertError(msg,function(){});
		   	}
		   	function suc(dc){
		   		if(dc.getCode() == icfp.Action.statusCode.ok){
		   			var tag = dc.getParameter("tag");
		   			if(tag && tag=='0'){
		   				$('#key1').val('');
						$('#key2').val('');
						$('#key3').val('');
		   				icfp.win.icfp.msg.alertError("对不起，旧密码输入错误！",function(){});
		   			}else if(tag && tag=='1'){
		   				var msg = dc.getDetail();
	    	         	icfp.win.icfp.msg.alertSuccess(msg,function(){
	    	         		icfp.win.icfp.dialog.closeDialogByID('BSN999_EDT');
	    	         		icfp.win.icfp.cookie.setcookie("tuichu",'11',365 * 24 * 60 * 60 * 1000);
	    	         		icfp.win.location.href = 'CoreAction.do?requestId=REQ_S_999_04&mode=1';
	    	            });
		   			}
    	         
    			}
		   	}
			var httpService = new HttpService();
			httpService.putBusinessRequestId("REQ_E_999_01");
			httpService.putBusinessID("BSN999");
			httpService.putParameter('key1',key1);
			httpService.putParameter('key3',key3);
			httpService.addEventListener(icfp.FaultEvent,fal);
			httpService.addEventListener(icfp.ResultEvent,suc);
			httpService.post();
		}else {
			icfp.win.icfp.msg.alertWarn("当前页面存在校验未通过的数据，请修改！",function(){});
		}
	});
	
	$('#colse').click(function(){
		if(icfp.form.isFormChanged('edtform')){
			icfp.win.icfp.msg.confirm("当前窗口存在变更数据，是否执行该操作？",function(v){
				if(v){
					icfp.win.icfp.dialog.closeDialogByID('BSN999_EDT');
				}
			});
		}else{
			icfp.win.icfp.dialog.closeDialogByID('BSN999_EDT');
		}
		
	});
	
});
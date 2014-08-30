$(function(){
	var height = $(document).height();
	$('.formcontent').height(height-40);
	$('.formbar').height(40);
	icfp.form.setComboBox();
	var data = icfp.win.icfp.dialog.getDialogData('BSN996_ADD');
	if(data.SAE001 && data.SAE001 != '' && data.SAE002 && data.SAE002 != ''){
		$('#SA09_SAE004').val(data.SAE001);
		$('#shangji').val(data.SAE002);
	}else{
		$('#SA09_SAE004').val("0");
		$('#shangji').val("根节点");
	}
	icfp.form.rememberValue('addform');
	$('#addform').validForm();
	$('#save').click(function(){
		if($('#addform').validForm('valid')){
			var data = icfp.form.getformData('addform','SA09','insert');
			var ds = new DataStore();
			ds.setDataType("com.management.entity.SA09");
			ds.setRowSet(data);
			function fal(dc){
    			var msg = dc.getDetail();
	         	icfp.win.icfp.msg.alertError(msg,function(){});
    		}
    		
    		function suc(dc){
    			if(dc.getCode() == icfp.Action.statusCode.ok){
    	         	var msg = dc.getDetail();
    	         	icfp.win.icfp.msg.alertSuccess(msg,function(){
    	         		icfp.win.icfp.dialog.closeDialogByID('BSN996_ADD');
    	         		icfp.win.icfp.frame.changeurl('REQ_S_996_01');
    	            });
    			}
    		}
			var httpService = new HttpService();
    		httpService.putBusinessRequestId("REQ_E_996_01");
    		httpService.putBusinessID("BSN996");
    		httpService.putDataStore("ds",ds);
    		httpService.addEventListener(icfp.FaultEvent,fal);
    		httpService.addEventListener(icfp.ResultEvent,suc);
    		httpService.post();
		}else{
			icfp.win.icfp.msg.alertWarn("当前页面存在校验未通过的数据，请修改！",function(){});
		}
	});
	
	$('#cancel').click(function(){
		if(icfp.form.isFormChanged('addform')){
			icfp.win.icfp.msg.confirm("当前窗口存在变更数据，是否执行该操作？",function(v){
				if(v){
					icfp.win.icfp.dialog.closeDialogByID('BSN996_ADD');
				}
			});
		}else{
			icfp.win.icfp.dialog.closeDialogByID('BSN996_ADD');
		}
		
	});
	
	$('#colse').click(function(){
		if(icfp.form.isFormChanged('addform')){
			icfp.win.icfp.msg.confirm("当前窗口存在变更数据，是否关闭窗口？",function(v){
				if(v){
					icfp.win.icfp.dialog.closeDialogByID('BSN996_ADD');
				}
			});
		}else{
			icfp.win.icfp.dialog.closeDialogByID('BSN996_ADD');
		}
		
	});
});
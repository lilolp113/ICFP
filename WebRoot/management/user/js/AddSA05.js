$(function(){
	var height = $(document).height();
	$('.formcontent').height(height-40);
	$('.formbar').height(40);
	icfp.form.setComboBox();
	icfp.form.rememberValue('addform');
	$('#addform').validForm();
	
	$('#save').click(function(){
		if($('#addform').validForm('valid')){
			var data = icfp.form.getformData('addform','SA05','insert');
			var ds = new DataStore();
			ds.setDataType("com.management.entity.SA05");
			ds.setRowSet(data);
			function fal(dc){
    			var msg = dc.getDetail();
	         	icfp.win.icfp.msg.alertError(msg,function(){});
    		}
    		
    		function suc(dc){
    			if(dc.getCode() == icfp.Action.statusCode.ok){
    	         	var msg = dc.getDetail();
    	         	icfp.win.icfp.msg.alertSuccess(msg,function(){
    	         		icfp.win.icfp.dialog.closeDialogByID('BSN997_ADD');
    	         		icfp.win.icfp.frame.changeurl('REQ_S_997_01');
    	            });
    			}
    		}
			var httpService = new HttpService();
    		httpService.putBusinessRequestId("REQ_E_997_01");
    		httpService.putBusinessID("BSN997");
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
					icfp.win.icfp.dialog.closeDialogByID('BSN997_ADD');
				}
			});
		}else{
			icfp.win.icfp.dialog.closeDialogByID('BSN997_ADD');
		}
		
	});
	
	$('#colse').click(function(){
		if(icfp.form.isFormChanged('addform')){
			icfp.win.icfp.msg.confirm("当前窗口存在变更数据，是否关闭窗口？",function(v){
				if(v){
					icfp.win.icfp.dialog.closeDialogByID('BSN997_ADD');
				}
			});
		}else{
			icfp.win.icfp.dialog.closeDialogByID('BSN997_ADD');
		}
		
	});
});
$(function(){
	var height = $(document).height();
	$('.formcontent').height(height-40);
	$('.formbar').height(40);
	icfp.form.setComboBox();
	var id = icfp.win.icfp.dialog.getDialogData('BSN998_EDT');
	function fal(dc){
		var msg = dc.getDetail();
     	icfp.win.icfp.msg.alertError(msg,function(){});
	}
	var crowSets;
	function suc(dc){
		if(dc.getCode() == icfp.Action.statusCode.ok){
			var ds = dc.getDataStore('ds');
			var rowSets = ds.getRowSet();
			if(rowSets && rowSets.length>0){
				crowSets = rowSets;
				icfp.form.setformData('edtform','SA03',rowSets[0]);
				icfp.form.rememberValue('edtform');
			}
		}
	}
	var httpService = new HttpService();
	httpService.putBusinessRequestId("REQ_Q_998_02");
	httpService.putBusinessID("BSN998");
	httpService.putParameter('SAB001',id);
	httpService.addEventListener(icfp.FaultEvent,fal);
	httpService.addEventListener(icfp.ResultEvent,suc);
	httpService.post();
	$('#edtform').validForm();
	
	icfp.form.disableform('edtform',true);
	var disable = true;
	
	$('#save').click(function(){
		if(disable){
			disable = false;
			$(this).attr('value','保存');
			$('#delete').attr('disabled',true);
			icfp.form.disableform('edtform',false);
		}else{
			disable = true;
			save();
		}
	});
	
	function save(){
		if($('#edtform').validForm('valid')){
			var data = icfp.form.getformData('edtform','SA03','update');
			if(icfp.form.ischangeformDate(crowSets[0],data[0])){
				var ds = new DataStore();
				ds.setDataType("com.management.entity.SA03");
				ds.setRowSet(icfp.form.exformDate(crowSets[0],data[0]));
				function fal(dc){
	    			var msg = dc.getDetail();
		         	icfp.win.icfp.msg.alertError(msg,function(){});
	    		}
	    		
	    		function suc(dc){
	    			if(dc.getCode() == icfp.Action.statusCode.ok){
	    	         	var msg = dc.getDetail();
	    	         	icfp.win.icfp.msg.alertSuccess(msg,function(){
	    	         		icfp.win.icfp.dialog.closeDialogByID('BSN998_EDT');
	    	         		icfp.win.icfp.frame.changeurl('REQ_S_998_01');
	    	            });
	    			}
	    		}
				var httpService = new HttpService();
	    		httpService.putBusinessRequestId("REQ_E_998_02");
	    		httpService.putBusinessID("BSN998");
	    		httpService.putDataStore("ds",ds);
	    		httpService.addEventListener(icfp.FaultEvent,fal);
	    		httpService.addEventListener(icfp.ResultEvent,suc);
	    		httpService.post();
			}else{
				icfp.win.icfp.msg.alertWarn("当前页面数据未发生变更，无法保存！",function(){});
			}
		}else{
			icfp.win.icfp.msg.alertWarn("当前页面存在校验未通过的数据，请修改！",function(){});
		}
	}
	
	$('#delete').click(function(){
		icfp.win.icfp.msg.confirm("是否确认删除此条数据？",function(v){
			if(v){
				var data = [];
				data.push(crowSets[0]);
				data[0].status = 'delete';
				var ds = new DataStore();
				ds.setDataType("com.management.entity.SA03");
				ds.setRowSet(data);
				function fal(dc){
	    			var msg = dc.getDetail();
    	         	icfp.win.icfp.msg.alertError(msg,function(){});
	    		}
	    		
	    		function suc(dc){
	    			if(dc.getCode() == icfp.Action.statusCode.ok){
	    	         	var msg = dc.getDetail();
	    	         	icfp.win.icfp.msg.alertSuccess(msg,function(){
	    	         		icfp.win.icfp.dialog.closeDialogByID('BSN998_EDT');
	    	         		icfp.win.icfp.frame.changeurl('REQ_S_998_01');
	    	            });
	    			}
	    		}
				var httpService = new HttpService();
	    		httpService.putBusinessRequestId("REQ_E_998_03");
	    		httpService.putBusinessID("BSN998");
	    		httpService.putDataStore("ds",ds);
	    		httpService.addEventListener(icfp.FaultEvent,fal);
	    		httpService.addEventListener(icfp.ResultEvent,suc);
	    		httpService.post();
			}
		});
	});
	
	$('#colse').click(function(){
		if(icfp.form.isFormChanged('edtform')){
			icfp.win.icfp.msg.confirm("当前窗口存在变更数据，是否关闭窗口？",function(v){
				if(v){
					icfp.win.icfp.dialog.closeDialogByID('BSN998_EDT');
				}
			});
		}else{
			icfp.win.icfp.dialog.closeDialogByID('BSN998_EDT');
		}
		
	});
});
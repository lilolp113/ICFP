$(function(){
	var opt = {};
	opt.title = "用户信息列表";
	opt.height = 333;
	opt.rownumbers = true;
	opt.columns =[
	     {display: '用户姓名', name: 'SAC004', width: 100, align: 'left' },
	     {display: '用户角色', name: 'SAB002', width: 80 , align: 'left'},
	     {display: '登录账号', name: 'SAC002', width: 80 , align: 'left'},
	     {display: '用户描述', name: 'SAC005', width: 140, align: 'left'},
	     {display: '创建人',   name: 'ZZB001', width: 80, align: 'left'},
	     {display: '创建时间', name: 'ZZB002', width: 100 }
  	];
	var ds = new DataStore();
	ds.setBizId("REQ_Q_997_01");
	ds.setPageSize(10);
	ds.setDataType("com.management.entity.SA05");
	opt.datastore = ds;
	opt.toolbar = [
  		{name:'新增',icon:'add.gif',fn:function(){
  			icfp.win.icfp.dialog.opend('BSN997_ADD','新增用户',700,300,'REQ_S_997_02');
  		}}
  	];
	opt.busButton = [
   		{name:'详细信息',type:'sys',fn:function(){
 			var re = $("#SA05list").DataGrid('getSelectedRow');
 			if(re){
 				var sac001 = re[0].cell['SAC001'];
 				icfp.win.icfp.dialog.opend('BSN997_EDT','修改用户',700,300,'REQ_S_997_03',sac001);
 			}else{
 				icfp.win.icfp.msg.alertWarn("请选择一个用户！",function(){});
 			}
   		}},
   		{name:'删除',type:'sys',fn:function(){
   			var re = $("#SA05list").DataGrid('getSelectedRow');
 			if(re){
 				icfp.win.icfp.msg.confirm("是否确认删除此条数据？",function(v){
 					if(v){
 						var data = [];
 						data.push(re[0]);
 						data[0].status = 'delete';
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
 			    	         		icfp.win.icfp.frame.changeurl('REQ_S_997_01');
 			    	            });
 			    			}
 			    		}
 						var httpService = new HttpService();
 			    		httpService.putBusinessRequestId("REQ_E_997_03");
 			    		httpService.putBusinessID("BSN997");
 			    		httpService.putDataStore("ds",ds);
 			    		httpService.addEventListener(icfp.FaultEvent,fal);
 			    		httpService.addEventListener(icfp.ResultEvent,suc);
 			    		httpService.post();
 					}
 				});
 			}else{
 				icfp.win.icfp.msg.alertWarn("请选择一个用户！",function(){});
 			}
   		}}
   	];
 	
 	$('#SA05list').DataGrid(opt);
 	
 	var seach_tag = 0;
	
	$('#seach_do').click(function(){
		var SAB002 = $('#SA03_SAB002').val();
		var SAB003 = $('#SA03_SAB003').val();
		if(SAB002.trim()=='' && SAB003.trim()==''){
			icfp.win.icfp.msg.alertWarn("请至少输入一个查询条件！",function(){});
		}else{
			seach_tag = 1;
			var data = {};
			data.SAB002 = SAB002.trim();
			data.SAB003 = SAB003.trim();
			$('#SA03list').DataGrid('reloadByParms',data);
		}
	});
	
	$('#seach_cl').click(function(){
		$('#SA03_SAB002').val('');
		$('#SA03_SAB003').val('');
		if(seach_tag == 1){
			seach_tag = 0;
			var data = {};
			data.SAB002 = "";
			data.SAB003 = "";
			$('#SA03list').DataGrid('reloadByParms',data);
		}
	});
});
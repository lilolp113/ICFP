$(function(){
	
	var clickData = {};
	
	function munutreeClick(event,treeId,treeNode){
		if (!treeNode.id){
  			return;
  		}
		clickData.SAE001 = treeNode.id;
		clickData.SAE002 = treeNode.name;
		clickData.SAE004 = treeNode.pid;
		$('#SA09list').DataGrid('reloadByParms',clickData);
	}
	
	function tree_suc(dc){
   		var opt = {};
   		opt.id="SAE001";
   		opt.pid="SAE004";
   		opt.name="SAE002";
   		icfp.tree.setTree('SA09tree',dc,opt,munutreeClick);
   		icfp.tree.expandAll('SA09tree');
   	}
   	function tree_fal(dc){
   		
   	}
	var tree_ds = new DataStore();
	tree_ds.setDataType('com.management.entity.SA09');
	tree_ds.setPageSize(-1);
	var tree_httpService = new HttpService();
	tree_httpService.putBusinessRequestId("REQ_Q_996_03");
	tree_httpService.putBusinessID("BSN996");
	tree_httpService.putDataStore("SA09tree",tree_ds);
	tree_httpService.addEventListener(icfp.ResultEvent,tree_suc);
	tree_httpService.addEventListener(icfp.FaultEvent,tree_fal);
	tree_httpService.post();
	
	var opt = {};
	opt.title = "菜单信息列表";
	opt.height = 348;
	opt.rownumbers = true;
	opt.columns =[
	      {display: '菜单名称', name: 'SAE002', width: 80, align: 'left' },
	      {display: '菜单描述', name: 'SAE003', width: 130, align: 'left'  },
	      {display: '菜单路径', name: 'SAE007', width: 170, align: 'left' },
	      {display: '创建人', name: 'ZZB001', width: 80 },
	      {display: '创建时间', name: 'ZZB002', width: 80 }
  	];
	
	var ds = new DataStore();
	ds.setBizId("REQ_Q_996_01");
	ds.setPageSize(10);
	ds.setDataType("com.management.entity.SA09");
	opt.datastore = ds;
	opt.toolbar = [
  		{name:'新增',icon:'add.gif',fn:function(){
  			icfp.win.icfp.dialog.opend('BSN996_ADD','新增菜单',700,300,'REQ_S_996_02',clickData);
  		}}
  	];
	opt.busButton = [
   		{name:'详细信息',type:'sys',fn:function(){
 			var re = $("#SA09list").DataGrid('getSelectedRow');
 			if(re){
 				var sae001 = re[0].cell['SAE001'];
 				icfp.win.icfp.dialog.opend('BSN996_EDT','修改菜单',700,300,'REQ_S_996_03',sae001);
 			}else{
 				icfp.win.icfp.msg.alertWarn("请选择一个菜单！",function(){});
 			}
   		}},
   		{name:'删除',type:'sys',fn:function(){
   			var re = $("#SA09list").DataGrid('getSelectedRow');
 			if(re){
 				icfp.win.icfp.msg.confirm("是否确认删除此条数据？",function(v){
 					if(v){
 						var data = [];
 						data.push(re[0]);
 						data[0].status = 'delete';
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
 			    	         		icfp.win.icfp.frame.changeurl('REQ_S_996_01');
 			    	            });
 			    			}
 			    		}
 						var httpService = new HttpService();
 			    		httpService.putBusinessRequestId("REQ_E_996_03");
 			    		httpService.putBusinessID("BSN996");
 			    		httpService.putDataStore("ds",ds);
 			    		httpService.addEventListener(icfp.FaultEvent,fal);
 			    		httpService.addEventListener(icfp.ResultEvent,suc);
 			    		httpService.post();
 					}
 				});
 			}else{
 				icfp.win.icfp.msg.alertWarn("请选择一个菜单！",function(){});
 			}
   		}}
   	];
 	
 	$('#SA09list').DataGrid(opt);
});
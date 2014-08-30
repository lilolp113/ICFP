$(function(){
	var opt = {};
	opt.title = "角色信息列表";
	opt.height = 348;
	opt.rownumbers = true;
	opt.columns =[
	    {display: '角色名称', name: 'SAB002', width: 80, align: 'left' },
	    {display: '角色描述', name: 'SAB003', width: 140, align: 'left' },
		{display: '创建人',   name: 'ZZB001', width: 80, align: 'left' },
		{display: '创建时间', name: 'ZZB002', width: 100 }
  	];
	var cdatatree = [];
	var ds = new DataStore();
	ds.setBizId("REQ_Q_995_01");
	ds.setPageSize(10);
	ds.setDataType("com.management.entity.SA03");
	opt.datastore = ds;
	opt.onSelectRow = function(rowset,index,ds){
		function S_fal(dc){
			var msg = dc.getDetail();
	     	icfp.win.icfp.msg.alertError(msg,function(){});
		}
		function S_suc(dc){
			var obj = $.fn.zTree.getZTreeObj('SA09tree');
			obj.checkAllNodes(false);
			cdatatree = [];
			var ds = dc.getDataStore('SA09menu');
			var rowsets = ds.getRowSet();
			if(rowsets && rowsets!=null && rowsets.length>0){
				$.each(rowsets,function(index,rowset){
					var SAE001 = rowset.cell['SAE001'];
					var node = obj.getNodesByParam('id',SAE001);
					if(node && node!=null){
						var nodeobj = node[0];
						cdatatree.push(nodeobj['id']);
						obj.checkNode(nodeobj,true,false);
					}
				});
			}
		}
		var SAB001 = rowset.cell['SAB001'];
		var S_httpService = new HttpService();
		S_httpService.putBusinessRequestId("REQ_Q_995_03");
		S_httpService.putBusinessID("BSN996");
		S_httpService.putParameter('SAB001',SAB001);
		S_httpService.addEventListener(icfp.FaultEvent,S_fal);
		S_httpService.addEventListener(icfp.ResultEvent,S_suc);
		S_httpService.post();
	};
	opt.unSelectRow = function(rowset,index,ds){
		var obj = $.fn.zTree.getZTreeObj('SA09tree');
		obj.checkAllNodes(false);
	};
	
	opt.toolbar = [
 		{name:'保存授权',icon:'save.gif',fn:function(){
 			var re = $("#SA03list").DataGrid('getSelectedRow');
 			if(re){
 				var treeObj = $.fn.zTree.getZTreeObj("SA09tree");
 				var nodes = treeObj.getCheckedNodes(true);
 				var menus = [];
 				$.each(nodes,function(index,node){
 					menus.push(node['id']);
 				});
 				var xiangtong = [];
 				for(var s in cdatatree){
 					for(var x in menus){
 						if(cdatatree[s]==menus[x]){       
 							xiangtong.push(cdatatree[s]);  
 						}
 					}
 				}
 				if(cdatatree.length == menus.length){
	 				if(cdatatree.length==xiangtong.length){
	 					icfp.win.icfp.msg.alertWarn("当前页面数据未发生变更，无法保存！",function(){});
	 					return;
	 				}
 				}
 				function fal(dc){
 					var msg = dc.getDetail();
 			     	icfp.win.icfp.msg.alertError(msg,function(){});
 				}
 				function suc(dc){
 					if(dc.getCode() == icfp.Action.statusCode.ok){
	    	         	var msg = dc.getDetail();
	    	         	icfp.win.icfp.msg.alertSuccess(msg,function(){});
	    			}
 				}
 				var httpService = new HttpService();
	    		httpService.putBusinessRequestId("REQ_E_995_01");
	    		httpService.putBusinessID("BSN995");
	    		httpService.putParameter('roleid',re[0].cell['SAB001']);
	    		httpService.putParameter('menus',menus.join(","));
	    		httpService.addEventListener(icfp.FaultEvent,fal);
	    		httpService.addEventListener(icfp.ResultEvent,suc);
	    		httpService.post();
 			}else{
 				icfp.win.icfp.msg.alertWarn("请选择一个角色！",function(){});
 			}
 		}}
 	];
 	$('#SA03list').DataGrid(opt);
 	
	function munutreeClick(event,treeId,treeNode){
		var obj = $.fn.zTree.getZTreeObj('SA09tree');
		if(!treeNode.checked){
			obj.checkNode(treeNode,true,true);
		}else{
			obj.checkNode(treeNode,false,true);
		}
	}
 	
 	function tree_suc(dc){
   		var opt = {};
   		opt.id="SAE001";
   		opt.pid="SAE004";
   		opt.name="SAE002";
   		icfp.tree.setTree('SA09tree',dc,opt,munutreeClick,true);
   		icfp.tree.expandAll('SA09tree');
   	}
   	function tree_fal(dc){
   		var msg = dc.getDetail();
     	icfp.win.icfp.msg.alertError(msg,function(){});
   	}
	var tree_ds = new DataStore();
	tree_ds.setDataType('com.management.entity.SA09');
	tree_ds.setPageSize(-1);
	var tree_httpService = new HttpService();
	tree_httpService.putBusinessRequestId("REQ_Q_995_02");
	tree_httpService.putBusinessID("BSN995");
	tree_httpService.putDataStore("SA09tree",tree_ds);
	tree_httpService.addEventListener(icfp.ResultEvent,tree_suc);
	tree_httpService.addEventListener(icfp.FaultEvent,tree_fal);
	tree_httpService.post();
});
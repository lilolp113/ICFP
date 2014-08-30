$(function(){
	var height = $(window).height();
	var hheight = $('#header').height();
	var mheight = $('#main').height();
	var ltop = (height - hheight - mheight)*0.5-30;
	if(ltop>0){
		$('#main').css('margin-top',ltop);
	}
	$('.vcodepic').click(function(){
		$('img',this).attr('src','icfp/ui/include/rancode.jsp?'+Math.random()*10000);
	});
	
	document.onkeydown = function(e){
		if(!e) e = window.event;//火狐中是 window.event
		if((e.keyCode || e.which) == 13){
			$("#btn_safelogin").click();
	    }
	};
	

	
	$('#btn_safelogin').click(function(){
		var username = $("#username").val();
		var password = $("#password").val();
		if (username == ""){
			icfp.msg.tip('提示','对不起，用户名不能为空！');
			$("#username").focus();
			return;
		}
		if (password == ""){
			icfp.msg.tip('提示','对不起，用户密码不能为空！');
			$("#password").focus();
			return;
		}
		
		function suc(dc){
			var type = dc.getParameter("loginflag");
			if(type == "0"){
				var msg = "对不起，用户名或密码错误！";
				icfp.msg.tip('提示',msg);
				$("#password").val('');
				$("#rancode").val('');
				$("#password").focus();
			}else if(type == "2"){
				var userid = dc.getParameter("userid");
				icfp.win.icfp.cid = userid;
				location.href="CoreAction.do?requestId=REQ_S_999_02&mode=1&_cid="+userid;
			}
		}
		
		function fal(dc){
			
		}
		
		var httpService = new HttpService();
		httpService.putBusinessRequestId("REQ_Q_999_01");
		httpService.putBusinessID("UCN007");
		httpService.putParameter("username",username);
		httpService.putParameter("password",password);
		httpService.putParameter("rancode","wugang");
		httpService.addEventListener(icfp.ResultEvent,suc);
		httpService.addEventListener(icfp.FaultEvent,fal);
		httpService.post();
		
		
		
	});
});
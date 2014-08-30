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
	
	var uu = icfp.cookie.getcookie('username');
	var pp = icfp.cookie.getcookie('password');
	var al = icfp.cookie.getcookie('autologin');
	var tc = icfp.cookie.getcookie('tuichu');
	
	if(uu!=null&&uu.trim()!=''&&pp!=null&&pp.trim()!=''){
		$("#username").val(uu);
		$("#password").val(pp);
		$('#checkbox_jizhu').attr('checked',true);
	}
	if(al!=null&&al.trim()!=''){
		$('#checkbox_zidon').attr('checked',true);
	}
	
	$('#username').keyup(function(){
		var u = $("#username").val();
		var p = $('#password').val();
		if(p!=''){
			$('#password').val('');
		}
		if(uu!=null&&uu!='null'&&pp!=null&&pp!='null'){
			if(u==uu){
				$('#password').val(pp);
			}
		}
	});
	
	if($('#checkbox_zidon').attr('checked')){
		if(tc!=null&&tc.trim()!=''){
		}else{
			alogin();
		}
	}
	
	$('#btn_safelogin').click(function(){
		var username = $("#username").val();
		var password = $("#password").val();
		var rancode = $("#rancode").val();
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
		if (rancode == ""){
			icfp.msg.tip('提示','对不起，验证码不能为空！');
			$("#rancode").focus();
			return;
		}
		var httpService = new HttpService();
		httpService.putBusinessRequestId("REQ_Q_999_01");
		httpService.putBusinessID("UCN007");
		httpService.putParameter("username",username);
		httpService.putParameter("password",password);
		httpService.putParameter("rancode",rancode);
		httpService.addEventListener(icfp.ResultEvent,suc);
		httpService.addEventListener(icfp.FaultEvent,fal);
		httpService.post();
		function suc(dc){
			var type = dc.getParameter("loginflag");
			if(type == "0"){
				var msg = "对不起，用户名或密码错误！";
				icfp.msg.tip('提示',msg);
				$("#password").val('');
				$("#rancode").val('');
				$("#password").focus();
			}else if(type == "1"){
				var msg = "对不起，验证码输入错误！";
				icfp.msg.tip('提示',msg);
				$("#rancode").val('');
				$("#rancode").focus();
			}else if(type == "2"){
				var userid = dc.getParameter("userid");
				icfp.win.icfp.cid = userid;
				if($('#checkbox_jizhu').attr('checked')){
					icfp.cookie.setcookie("username",username,365 * 24 * 60 * 60 * 1000);
					icfp.cookie.setcookie("password",password,365 * 24 * 60 * 60 * 1000);
				}else{
					icfp.cookie.delcookie("username");
					icfp.cookie.delcookie("password");
				}
				if($('#checkbox_zidon').attr('checked')){
					icfp.cookie.setcookie("autologin",username,365 * 24 * 60 * 60 * 1000);
				}else{
					icfp.cookie.delcookie("autologin");
				}
				icfp.cookie.delcookie("tuichu");
				location.href="CoreAction.do?requestId=REQ_S_999_02&mode=1&_cid="+userid;
			}
		}
		function fal(dc){
			
		}
	});
	
	function alogin(){
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
		var httpService = new HttpService();
		httpService.putBusinessRequestId("REQ_Q_999_01");
		httpService.putBusinessID("UCN007");
		httpService.putParameter("username",username);
		httpService.putParameter("password",password);
		httpService.putParameter("rancode",'wugang');
		httpService.addEventListener(icfp.ResultEvent,asuc);
		httpService.addEventListener(icfp.FaultEvent,afal);
		httpService.post();
		function asuc(dc){
			var type = dc.getParameter("loginflag");
			if(type == "0"){
				var msg = "对不起，用户名或密码错误！";
				icfp.msg.tip('提示',msg);
				$("#password").val('');
				$("#rancode").val('');
				$("#password").focus();
			}else if(type == "2"){
				icfp.cookie.delcookie("tuichu");
				var userid = dc.getParameter("userid");
				icfp.win.icfp.cid = userid;
				location.href="CoreAction.do?requestId=REQ_S_999_02&mode=1&_cid="+userid;
			}
		}
		function afal(){
			
		}
	}
});
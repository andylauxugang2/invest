<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="base.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <head>
  <title>Log in</title>
  <meta name="generator" content="editplus" />
  <meta name="author" content="" />
  <meta name="keywords" content="" />
  <meta charset="utf-8" />
  <meta name="description" content="" />
  <style>
	body{background-color:#a9a9a9;}
	.login{
		position:fixed;
		left:50%;top:50%;
		margin-left:-247px;
		width:494px;
		border-radius:5px;
		-moz-border-radius:5px;
		-webkit-border-radius:5px;
		background:#fff;
		height:382px;
		margin-top:-191px; 
		_position:absolute;
		_bottom:auto;
		_margin-left:0;
		_margin-top:0;
		_left:expression(eval(document.documentElement.scrollLeft+document.documentElement.clientWidth-this.offsetWidth)-(parseInt(this.currentStyle.marginLeft,10)||0)-(parseInt(this.currentStyle.marginRight,10)||0));
		_top:expression(eval(document.documentElement.scrollTop+document.documentElement.clientHeight-this.offsetHeight-(parseInt(this.currentStyle.marginTop,10)||0)-(parseInt(this.currentStyle.marginBottom,10)||0)));
	}
	.login .hd{padding-top:89px;height:35px;padding-left:258px;text-align:center;line-height:35px;font-weight:bold;color:#4d4d4d;background:url(images/login/login_hd_new.jpg) no-repeat center;border-radius:5px 5px 0 0;-moz-border-radius:5px 5px 0 0;-webkit-border-radius:5px 5px 0 0;}
	.login .bd{height:188px;padding:18px 46px;}
	.login .bd .p{height:33px;line-height:33px;color:#4d4d4d;}
	.login .bd .p input{width:100%;height:26px;line-height:26px;}
	.login .bd .submit{padding-top:20px;}
	.login .bd .submit input{padding:0;margin:0;width:122px;height:26px;float:right;text-align:center;}
	.login .ft{text-align:center;font-size:12px;height:35px;line-height:35px;background:#ededed;border-radius: 0 0 5px 5px;-moz-border-radius: 0 0 5px 5px;-webkit-border-radius: 0 0 5px 5px;color:#4d4d4d;}
  </style>
 </head>

 <body>
<form id="userForm" name="userForm" method="post" action="login.action">
	<div class="login">
		<!-- 
		<div class="hd">
			登录
		</div>
		 -->
		 <div class="hd">
		</div>
		<div class="bd">
			<div class="p">
				<lable>用户名:</lable>
			</div>
			<div class="p">
				<input type="text" name="username" />
			</div>
			<div class="p">
			<lable>密码:</lable>
			</div>
			<div class="p">
				<input type="password" name="password" />
			</div>
			<div class="submit">
				<span style="color:red;">${error}</span>
				<input type="submit" name="submit" value="提交" />
			</div>
		</div>
		<div class="ft">Copyright &copy; 2013-2015</div>
	</div>
</form>
 </body>
</html>
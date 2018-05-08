<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<style>
<!--
body {
  background-image: url(images/login/content_bg_main2.jpg);
  background-repeat: repeat-x;
  margin: 0px;
  padding: 0px;
  font-size: 12px;
  font-family: Arial, Helvetica, sans-serif;
}

.title_14 {
  font-size: 14px;
  font-weight: bold;
  padding-top: 25px;
}
.welcome_link {
  padding-left: 30px;
  font-size: 13px;
}
-->
</style>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<script type='text/javascript' src='js/constantnoext.js'></script>
<body>
	<c:forEach var="user" items="${sessionScope.UserInfo.roles}" varStatus="status">
		<div style="position: absolute; top: 50%;left:40%;margin:auto auto;font-size: 15px;">
		<c:if test="${user.id==1 }">
			<script>
			var idtemp = ${sessionScope.UserInfo.ratelevel.id};
			var temp = writer_level_name[idtemp-1];
			</script>
			
			Hi, <font color="green">${sessionScope.UserInfo.engname}</font>, <font color="green"><script>document.write(temp);</script></font><br/>
			Welcome to LohasWriters.<br/>
			You are now <font color="green">${sessionScope.UserInfo.ratelevel.levelName}</font>.<br/>
			Your maximum quota of tasks is <font color="green">${sessionScope.UserInfo.maxGetOrders}</font>.<br/>
			You still have <font color="green">${sessionScope.UserInfo.nosubOrdersBySelf+sessionScope.UserInfo.nosubOrders}</font> tasks outstanding.<br/>
		</c:if>
		<c:if test="${user.id==2 }">
			Hi, <font color="green">${sessionScope.UserInfo.engname}</font>. <br/>
			Welcome to LohasWriters.<br/>
		</c:if>
		<c:if test="${user.id==3 }">
			Hi, <font color="green">${sessionScope.UserInfo.engname}</font>. <br/>
			Welcome to LohasWriters.<br/>
		</c:if>
		</div>
	</c:forEach>
</body>
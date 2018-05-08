<%@ include file="../views/base.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<!-- 
 <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" /> 
 -->
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<title>
</title>
  <link rel='stylesheet' type='text/css' href='<%=basePath%>/assets/ext-3.2.1/resources/css/ext-all.css' />
  <link rel='stylesheet' type='text/css' href='<%=basePath%>/assets/ext-3.2.1/resources/css/xtheme-gray.css' />
  <link rel='stylesheet' type='text/css' href='<%=basePath%>/assets/css/common.css' />
</head>
<body>
      <script type="text/javascript" src="<%=basePath%>/assets/ext-3.2.1/adapter/ext/ext-base.js"></script>
      <script type='text/javascript' src='<%=basePath%>/assets/ext-3.2.1/ext.js'></script>
      <script type='text/javascript' src='<%=basePath%>/assets/ext-3.2.1/locale/ext-lang-zh_cn.js'></script>
      <script type="text/javascript" src="<%=basePath%>/assets/js/zh.js"></script>
      <script type="text/javascript" src="<%=basePath%>/assets/js/common.js"></script>
      <script type="text/javascript" src="<%=basePath%>/assets/js/constant.js"></script>
      <script type="text/javascript" src="<%=basePath%>/assets/js/template.js"></script>
      <script type='text/javascript' src='<%=basePath%>/dwr/engine.js'></script>

      <script>
          var user = new Object();
          user['id'] = '${sessionScope.user.id}';
          user['name'] = '${sessionScope.user.name}';
          user['menus'] = ${sessionScope.user.menus};
      </script>

</body>
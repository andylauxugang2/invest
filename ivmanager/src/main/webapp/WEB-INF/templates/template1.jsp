<%@ include file="../views/base.jsp"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<html>
<!--
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" /> 
 -->
 <meta http-equiv="X-UA-Compatible" content="IE=7" />
<head>
  <title></title>
    <link rel='stylesheet' type='text/css' href='<%=basePath%>/assets/ext-3.2.1/resources/css/ext-all.css' />
    <link rel='stylesheet' type='text/css' href='<%=basePath%>/assets/ext-3.2.1/resources/css/xtheme-gray.css' />
    <link rel='stylesheet' type='text/css' href='<%=basePath%>/assets/css/common.css' />
    <style type=text/css>
      html,body{margin:0; padding:0; height:100%;}
      #loading-mask{
          position:absolute;
          left:0;
          top:0;
          width:100%;
          height:100%;
          z-index:20000;
      }
      #loading{
          position:absolute;
          left:45%;
          top:40%;
          padding:2px;
          z-index:20001;
          height:auto;
      }
      #loading .loading-indicator{
          color:#444;
          font:bold 13px tahoma,arial,helvetica;
          padding:10px;
          margin:0;
          height:auto;
      }
      #loading-msg {
          font: normal 10px arial,tahoma,sans-serif;
      }
    </style>
</head>
<body>
<div id="loading">
    <div class="loading-indicator"><span id="loading-msg" style="padding:10px">Loading...</span></div>
</div>
  <%String icon = request.getParameter("icon")==null?"":request.getParameter("icon"); %>
  <%String title = request.getParameter("title")==null?"":request.getParameter("title"); %>
  <script>
    var _icon = '<%=icon%>';
    var _title = '<%=new String(title.getBytes("iso8859-1"),"utf-8")%>';
  </script>
  <script type="text/javascript" src="<%=basePath%>/assets/ext-3.2.1/adapter/ext/ext-base.js"></script>
  <script type='text/javascript' src='<%=basePath%>/assets/ext-3.2.1/ext.js'></script>
  <script type='text/javascript' src='<%=basePath%>/assets/ext-3.2.1/locale/ext-lang-zh_cn.js'></script>
  <script type='text/javascript' src='<%=basePath%>/dwr/engine.js'></script>
  <script type='text/javascript' src='<%=basePath%>/assets/js/zh.js'></script>
  <script type='text/javascript' src='<%=basePath%>/assets/js/constant.js'></script>
  <script type='text/javascript' src='<%=basePath%>/assets/js/common.js'></script>
  
  <tiles:insertAttribute name="content" ignore="true"/>
  
</body>
</html>
	  
<script>
  Ext.BLANK_IMAGE_URL = '<%=basePath%>/assets/ext-3.2.1/resources/images/default/s.gif';
  Ext.QuickTips.init();
  var actionMask=null;
  DWREngine.setErrorHandler(function(msg, ex){
    var message = msg;
    var tmp = this.req.responseText;
    if(tmp != null && tmp.indexOf('com.game.dwr.session.null')>0){
      message = common.reloginContent;
    }
    if(ex.errorMessage) message = ex.errorMessage['zh_cn'];
    if(message){
      var index = message.indexOf("##");
      if(index > 0 ) message = message.substring(index+2, message.length);
    }
    else message = msg;
    Ext.MessageBox.show({
      title: 'ERROR',
      width: 300,
      msg: message,
      buttons: Ext.MessageBox.OK,
      icon: Ext.MessageBox.ERROR,
      fn:function(){
   	    if(actionMask) {
            actionMask.unmask();
            actionMask=null;
       	}
        if(message && message.indexOf("session is overtime") >= 0){
	        try{
	        	window.parent.parent.location="${basePath}";
	        }catch(e){
	        }
        }
      },
      animEl: window.event?window.event.srcElement:null
    });
  });
  
  setTimeout(function(){
    Ext.get('loading').remove();
  }, 250);
</script>
Ext.onReady(function() {
	Ext.BLANK_IMAGE_URL = 'assets/ext-3.2.1/resources/images/default/s.gif';
	Ext.QuickTips.init();
	init(user['menus']);
	
	function init(menus) {
		var trees = [];
		for ( var i = 0; i < menus.length; i++) {
			var tree = createTree(menus[i]);
			tree.on('click', function(node, e) {
				if (node.isLeaf()) {
					e.stopEvent();
					var frm = Ext.get('main');
					var o = {
						icon : node.attributes.iconCls,
						title : node.attributes.text
					};
					frm.dom.src = node.attributes.href + '?' + Ext.urlEncode(o);
				}
			});
			trees.push(tree);
		}
		
		var tb = new Ext.Toolbar({
			cls : 'top-toolbar'
		});
		tb.add({ iconCls: 'home',
			 handler: function(){
			 var frm = Ext.get('main');
			 frm.dom.src = 'welcome';
			 }
		});
		tb.add('->');
		tb.add("当前用户 : " + user['name']);
		tb.add('-');
		tb.add({
			iconCls : 'logout',
//			tooltip : common.logouttip,
			handler : function() {
				Ext.MessageBox.confirm(common.msg, common.logoutinfo,
						function(btn) {
							if (btn == 'yes')
								//document.location.href = "logout";
								alert("暂未实现");
						});
			}
		});
		
		/*var logo = new Ext.Panel({
			html : '<table width="100%" border="0" cellspacing="0" cellpadding="0"><tr><td height="84" background="assets/images/login/top_bg.jpg" valign="top"><img height="80"  src="assets/images/login/banner_logo.png"/></td></tr></table>',
			border : false,
			height : 94,
			frame : true
		});*/
		
		var mainPanel = new Ext.Panel({
			id : 'main-panel',
			border : false,
			region : 'center',
			margins : '0 5 5 0',
			html : '<iframe id="main" frameborder="no" height="100%" width="100%" src="welcome"></iframe>',
			autoScroll : true
		});
		
		var viewport = new Ext.Viewport({
			layout : 'border',
			items : [{
				region : 'north',
				border : false,
				layout : 'anchor',
				margins : '5 5 0 5',
				height : 30,
				items : [ tb ]
			}, {
				region : 'west',
				title : common.title,
				iconCls : 'fav',
				split : true,
				width : 150,
				minSize : 120,
				maxSize : 250,
				collapsible : true,
				margins : '0 0 5 5',
				cmargins : '0 5 5 5',
				layout : 'accordion', 
				layoutConfig : {
					animate : true
				},
				items : trees
			}, mainPanel ]
		});
		
		viewport.doLayout();
	}; // end function init

	function createTree(root) {
		var tree = new Ext.tree.TreePanel({
			id : root['id'],
			title : root['modleName'],
			iconCls : root['icon'],
			loader : new Ext.tree.TreeLoader(),
			rootVisible : false,
			border : false,
			lines : false,
			autoScroll : true
		});
		var subroots = [];
		var leaves = root['leaves'];
		if (leaves) {
		      var subroot = new Object();
			  subroot['text'] = root['modleName'];
			  subroot['iconCls'] = root['icon'];
			  subroot['expanded'] = true;
		      var srs = [];
		      var ls = leaves;
		      if(ls){
		        for(var j=0;j<ls.length;j++){
		          var l = ls[j];
		          var sr={
		            text:l['modleName'],
		            iconCls:l['icon'],
		            href:l['modleAction'],
		            leaf:true
		          };
		          srs.push(sr);
		        }
		      }
		      subroot['children'] = srs;
		      subroots.push(subroot);
		}
		var node = new Ext.tree.AsyncTreeNode({
			text : 'Online',
			children : subroots
		});
		tree.setRootNode(node);
		return tree;
	} // end function createTree
});
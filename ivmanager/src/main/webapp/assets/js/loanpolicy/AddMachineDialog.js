/**
 * 定义AddMachineDialog对话框 添加机器
 * @param config
 * @returns {Ext.AddMachineDialog}
 */
Ext.AddMachineDialog = function(config) {
	this.ds = config.ds;
	this.pageStart = config.pageStart;
	this.machine = getDetailMachine({dialog: this});
	this.items = [this.machine];
	this.width = common.wwidth * 0.50;
//	this.height = 600;
	this.iconCls = 'form';
	this.title = '添加机器窗口';
	this.bodyStyle = 'padding:0px;';
	this.buttonAlign = 'right';
	this.plain = true;
	this.modal = true;
	Ext.AddMachineDialog.superclass.constructor.call(this, config);
//	this.addButton('返回列表页', this.close, this);
	this.addButton('保存', this.save, this);
};

Ext.extend(Ext.AddMachineDialog, Ext.Window, {
	showDialog : function(e, record, ds){
		this.show(e, function(){});
	},
	close:function(){ // 关闭触发调用
		var dialog = this;
		dialog.hide();
		if(dialog.closeAction == 'refresh'){ // 点击过保存修改按钮 刷新列表
			dialog.ds.reload({params:{start:dialog.pageStart, limit:common.pageSize}});
		}
	},
	save:function(){
		var dialog = this;
		dialog.machine.save();
	}
});

/**
 * 获得机器详细信息
 * @returns {panel}
 */
function getDetailMachine(config){
	var ip = new Ext.form.TextField({
		fieldLabel: '机器ip*',
		allowBlank: false,
		regexText:"请填写正确形式的ip",
		anchor:'90%'
	});
	var name = new Ext.form.TextField({
		fieldLabel: '机器名称*',
		allowBlank: false,
		anchor:'90%'
	});
	var desc = new Ext.form.TextField({
		fieldLabel: '机器描述',
		allowBlank: true,
		anchor:'90%'
	});
	var responsiblePerson = new Ext.form.TextField({
		fieldLabel: '机器负责人*',
		allowBlank: false,
		anchor:'90%'
	});
    
	var label = new Ext.form.Label({
		text: '',
		style: {
            marginLeft: '10px',
            color: 'red'
        }
	});
	
	var resultpanel = {
			xtype: 'panel',
			height: common.wheight*0.4,
			autoScroll : true,
		    layout: 'form',
		    items: [{
		    	layout:'column',
		    	border:false,
		    	bodyStyle: 'padding-left:5px;padding-top:5px;',
		    	defaults:{labelWidth:120},
		    	tbar: ['基本信息',label],
		    	items:[{
		    		columnWidth: .5, // 该列在整行中所占百分比
		    		layout: 'form',
		    		border:false,
		    		items: [ip,desc]
		        },{
		        	columnWidth: .5,
		        	layout: 'form',
		        	border:false,
		        	items: [name,responsiblePerson]
		        }]
		    }],
		    save: function(){
	    		if(!ip.isValid() || !name.isValid() || !desc.isValid() || !responsiblePerson.isValid()){
	    			label.setText('请正确填写带*号的输入项');
	    			return;
	    		}
	    		label.setText('');
	    		
	    		var obj = new Object();
	    		obj.name = name.getValue();
	    		obj.desc = desc.getValue();
	    		obj.ip = ip.getValue();
	    		obj.responsiblePerson = responsiblePerson.getValue();
	    		Ext.MessageBox.confirm(constant.confirm, '确认添加吗？', function(btn){
	    			if(btn=='yes') {
	    				actionMask = config.dialog.body;
	    				actionMask.mask('正在保存...', 'x-mask-loading');
	    	    		machineService.addOneMachine(obj, function(o) {
	    	    			actionMask.unmask();
	    	    			config.dialog.hide();
	    	    			config.dialog.ds.reload({params:{start:config.dialog.pageStart, limit:common.pageSize}});
	      				});
	    	        }
	    		});
		    }
		};
	return resultpanel;
};
/** end getDetailMachine function*/
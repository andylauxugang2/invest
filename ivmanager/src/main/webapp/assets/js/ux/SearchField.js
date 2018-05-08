/*!
 * Ext JS Library 3.2.1
 * Copyright(c) 2006-2010 Ext JS, Inc.
 * licensing@extjs.com
 * http://www.extjs.com/license
 */
Ext.ns('Ext.ux.form');

Ext.ux.form.ClientEmailSearchField = Ext.extend(Ext.form.TwinTriggerField, {
    initComponent : function(){
        Ext.ux.form.ClientEmailSearchField.superclass.initComponent.call(this);
        this.on('specialkey', function(f, e){
            if(e.getKey() == e.ENTER){
                this.onTrigger2Click();
            }
        }, this);
    },
    
    validationEvent:true,
    validateOnBlur:true,
    trigger1Class:'x-form-clear-trigger',
    trigger2Class:'x-form-search-trigger',
//    hideTrigger1:true,

    onTrigger1Click : function(){
    	var field = this;
    	field.reset();
    },

    onTrigger2Click : function(){
    	var field = this;
    	field.store.load();	
    	var comboBox = new Ext.form.ComboBox({
        	editable: true,
        	store: field.store,
        	displayField:'email',
        	valueField:'email',
        	typeAhead: true,
        	emptyText: '请选择',
        	mode: 'remote',
        	forceSelection: true,
        	triggerAction: 'all', // 每次显示所有可选数据
        	selectOnFocus:true,
        	width: field.getWidth() - 8
        });
    	comboBox.on("select",function(el){
			field.setValue(el.getRawValue());
    	});
    	var fileMenu = new Ext.menu.SearchMenu({  
    		shadow : 'frame', 
    		width:  field.getWidth(),
//    		height: field.menuHeight,
    		autoHeight : true,
    		showSeparator: false,
    		plain: true,
    		items: [comboBox]
    	});
    	
    	fileMenu.show(this.el);
    }
});

Ext.menu.SearchMenu = Ext.extend(Ext.menu.Menu, {
	
});

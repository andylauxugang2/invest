/*!
 * Ext JS Library 3.2.1
 * Copyright(c) 2006-2010 Ext JS, Inc.
 * licensing@extjs.com
 * http://www.extjs.com/license
 */
Ext.ns('Ext.ux.form');

Ext.ux.form.ValidateUsernameField = Ext.extend(Ext.form.TwinTriggerField, {
    initComponent : function(){
        Ext.ux.form.ValidateUsernameField.superclass.initComponent.call(this);
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
    hideTrigger1:false,

    onTrigger1Click : function(){
    	var field = this;
    	field.reset();
    	field.isValid();
    },
    
    onTrigger2Click : function(el){
    	var field = this;
    	var v = field.getValue();
    	field.isValid();
    	if(!v){
    		return;
    	}
    	var obj = new Object();
    	obj['username'] = v;
    	alert(field.id.substring(0, 3));
    	if(field.id.substring(0, 3) != 'ext'){
    		obj['whereClause'] = ' and id != ' + field.id.substring(7);
    	}
    	userService.findUser(obj, function(o) {
			if(o){
				field.markInvalid("该用户代码已经存在");
				field.validationEvent = false;
			}else{
				Ext.MessageBox.alert(constant.info, '恭喜您，该用户代码可以使用', function(){
					field.clearInvalid();
					field.validationEvent = true;
				});
			}
    	});
    }
});
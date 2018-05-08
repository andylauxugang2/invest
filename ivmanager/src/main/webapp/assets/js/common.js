Ext.namespace('common');
common.wwidth=window.screen.availWidth;
common.wheight=window.screen.availHeight;
common.pageSize = 20;
common.defaultSort = 'id';
common.defaultDir = 'ASC';
common.photo = 'images/fam/image_add.png';
common.doc = 'images/fam/cog.png';
common.elt = 'images/fam/grid.png';
common.upload = 'images/fam/table_refresh.png';
common.download = 'images/icons/download.png';
common.unread = 'images/icons/unread.png';
common.read = 'images/icons/read.png';
common.del = 'images/delete.png';
common.attach = 'images/icons/attachment.gif';
common.upload = 'images/icons/upload.png';
common.disable = 'background:#f1f2f4;';
common.icon = [['1day'],['abiword'],['about'],['access'],['accordian'],['acroread'],['addgroup'],['addtsk'],['aim'],['aktion'],['alarmd'],['alias'],['alltopics'],['amsn'],['ant'],['application'],['applicationedit'],['applicationmagnify'],['areacharticon'],['book'],['collapse'],['deploy'],['docs'],['editor'],['example'],['expand'],['family'],['form'],['grid'],['layer'],['member'],['mount'],['presentation'],['report'],['sheet'],['tabs'],['text'],['tool'],['word'],['print'],['key']];

common.createWindow = function(el, items){
	  var win = new Ext.Window({
	                el:el,
	                layout:'fit',
	                frame:false,
	                constrainHeader: false,
	                animCollapse:false,
	                shim:false,
	                border:false,
	                minimizable: false,
	                maximizable: false,  
	                closable: false,  
	                maximized : true,       
	                items: items
	            });
	  win.show();
};

common.createWindowExpand= function(el, items){
  try{
  if(items.getColumnModel()){
    var cols = items.getColumnModel();
    for(var i=0;i<cols.getColumnCount();i++){
      var colid = cols.getColumnId(i);
      var col =  cols.getColumnById(colid);
//        alert(cols.isHidden(i));
//      	cols.setHidden(i, true);
      //col.menuDisabled = true;
      col.menuDisabled = false;
    }
  }
  }catch(e){}
  items.render(el);
};

common.showWarning = function(msg, animEl,processResult){
  Ext.MessageBox.show({
    title: common.warning,
    width: 300,
    msg: msg,
    buttons: Ext.MessageBox.OK,
    icon: Ext.MessageBox.WARNING,
    animEl: animEl,
    fn: processResult
  });
};

/**
 *  buttons: button ext-id
 */
common.getImgButtons = function(buttons){
	var content = '';
	if(buttons != null){
		for(var i=0;i<buttons.length;i++){
			content = content + '<span style="padding:5px;color:blue" id="' + buttons[i] + '"></span>';
	    }
	}
	content = content + '';
	return content;
};  

Ext.data.ObjectReader = function(meta, recordType){
  Ext.data.ObjectReader.superclass.constructor.call(this, meta, recordType); 
}; 

Ext.extend(Ext.data.ObjectReader, Ext.data.DataReader, {
  read : function(response){
    var sid = this.meta ? this.meta.id : null; 
    var recordType = this.recordType, fields = recordType.prototype.fields; 
    var records = []; 
    var root = response; 
    for(var i = 0; i < root.length; i++){ 
      var obj = root[i]; 
      var values = {}; 
      var id = obj[sid]; 
      for(var j = 0, jlen = fields.length; j < jlen; j++){
        var f = fields.items[j]; 
        var k = f.mapping !== undefined && f.mapping !== null ? f.mapping : f.name;
        var v = obj[k] !== undefined ? obj[k] : f.defaultValue; 
        v = f.convert(v); 
        values[f.name] = v; 

      }
      var record = new recordType(values, id); 
      records[records.length] = record; 
    }
    return {
      records : records, 
      totalRecords : records.length 
    }; 
  } 
});

Ext.data.DWRProxy = function(dwrCall, pagingAndSort){
  Ext.data.DWRProxy.superclass.constructor.call(this);
  this.dwrCall = dwrCall;
  this.pagingAndSort = (pagingAndSort!=undefined ? pagingAndSort : true);
};

Ext.extend(Ext.data.DWRProxy, Ext.data.DataProxy, {
  load : function(params, reader, callback, scope, arg) {
    if(this.fireEvent("beforeload", this, params) != false) {
      var delegate = this.loadResponse.createDelegate(this, [reader, callback, scope, arg], 1);
      var callParams = new Array();
      if(arg.arg) {
        callParams = arg.arg.slice();
      }
      if(this.pagingAndSort) {
        callParams.push(params.start);
        callParams.push(params.limit);
        if(params.sort != undefined && params.dir != undefined){
	        callParams.push(params.sort);
	        callParams.push(params.dir);
        }
      }
      callParams.push(delegate);
      this.dwrCall.apply(this, callParams);
    } else {
      callback.call(scope || this, null, arg, false);
    }
  },

  loadResponse : function(listRange, reader, callback, scope, arg) {
    var result;
    try {
      result = reader.read(listRange);
    } catch(e) {
      this.fireEvent("loadexception", this, null, response, e);
      callback.call(scope, null, arg, false);
      return;
    }
    callback.call(scope, result, arg, true);
  },

  update : function(dataSet){},

  updateResponse : function(dataSet)
  {}
});

Ext.data.ListRangeReader = function(meta, recordType){
    Ext.data.ListRangeReader.superclass.constructor.call(this, meta, recordType);
    this.recordType = recordType;
};
Ext.extend(Ext.data.ListRangeReader, Ext.data.DataReader, {
  getJsonAccessor: function(){
      var re = /[\[\.]/;
      return function(expr) {
          try {
              return(re.test(expr))
                  ? new Function("obj", "return obj." + expr)
                  : function(obj){
                      return obj[expr];
                  };
          } catch(e){}
          return Ext.emptyFn;
      };
  }(),
  
  read : function(o){
    var recordType = this.recordType, fields = recordType.prototype.fields;

    //Generate extraction functions for the totalProperty, the root, the id, and for each field
    if (!this.ef) {
      if(this.meta.totalProperty) {
        this.getTotal = this.getJsonAccessor(this.meta.totalProperty);
      }
    
      if(this.meta.successProperty) {
        this.getSuccess = this.getJsonAccessor(this.meta.successProperty);
      }

      if (this.meta.id) {
        var g = this.getJsonAccessor(this.meta.id);
        this.getId = function(rec) {
          var r = g(rec);
          return (r === undefined || r === "") ? null : r;
        };
      } else {
        this.getId = function(){return null;};
      }
      this.ef = [];
      for(var i = 0; i < fields.length; i++){
        f = fields.items[i];
        var map = (f.mapping !== undefined && f.mapping !== null) ? f.mapping : f.name;
        this.ef[i] = this.getJsonAccessor(map);
      }
    }

    var records = [];
    var root = o.data, c = root.length, totalRecords = c, success = true;

    if(this.meta.totalProperty){
      var v = parseInt(this.getTotal(o), 10);
      if(!isNaN(v)){
        totalRecords = v;
      }
    }

    if(this.meta.successProperty){
      var v = this.getSuccess(o);
      if(v === false || v === 'false'){
        success = false;
      }
    }

    for(var i = 0; i < c; i++){
      var n = root[i];
      var values = {};
      var id = this.getId(n);
      for(var j = 0; j < fields.length; j++){
        f = fields.items[j];
        var v = this.ef[j](n);            
        values[f.name] = f.convert((v !== undefined) ? v : f.defaultValue);
      }
      var record = new recordType(values, id);
      records[i] = record;
    }

    return {
       success : success,
       records : records,
       totalRecords : totalRecords
    };
  }
});
Ext.grid.CheckboxEditModel = Ext.extend(Ext.grid.CheckboxSelectionModel, {
    // private
    initEvents : function(){
        this.rowNav = new Ext.KeyNav(this.grid.getGridEl(), {
            "up" : function(e){
                if(!e.shiftKey){
                    this.selectPrevious(e.shiftKey);
                }else if(this.last !== false && this.lastActive !== false){
                    var last = this.last;
                    this.selectRange(this.last,  this.lastActive-1);
                    this.grid.getView().focusRow(this.lastActive);
                    if(last !== false){
                        this.last = last;
                    }
                }else{
                    this.selectFirstRow();
                }
            },
            "down" : function(e){
                if(!e.shiftKey){
                    this.selectNext(e.shiftKey);
                }else if(this.last !== false && this.lastActive !== false){
                    var last = this.last;
                    this.selectRange(this.last,  this.lastActive+1);
                    this.grid.getView().focusRow(this.lastActive);
                    if(last !== false){
                        this.last = last;
                    }
                }else{
                    this.selectFirstRow();
                }
            },
            scope: this
        });

        var view = this.grid.view;
        view.on("refresh", this.onRefresh, this);
        view.on("rowupdated", this.onRowUpdated, this);
        view.on("rowremoved", this.onRemove, this);
        this.grid.on('render', function(){
            var view = this.grid.getView();
            view.mainBody.on('mousedown', this.onMouseDown, this);
            Ext.fly(view.innerHd).on('mousedown', this.onHdMouseDown, this);

        }, this);
    },

    // private
    onMouseDown : function(e, t){
        if(t.className == 'x-grid3-row-checker'){
            e.stopEvent();
            var row = e.getTarget('.x-grid3-row');
            if(row){
                var index = row.rowIndex;
                if(this.isSelected(index)){
                    this.deselectRow(index);
                }else{
                    this.selectRow(index, true);
                }
            }
        }
    },
    selectAll : function(){
        if(this.locked) return;
        this.selections.clear();
        for(var i = 0, len = this.grid.store.getCount(); i < len; i++){
        var inner = this.grid.getView().getCell(i,1).innerHTML;
          if(inner != null && inner.indexOf('x-grid3-row-checker')>=0) this.selectRow(i, true);
        }
    },
    // private
    renderer : function(val, meta, record, r ,c , store){
        if(val) return '<div class="x-grid3-row-checker">&#160;</div>';
        else return '<div class="x-grid3-row-disable">&#160;</div>';
        
    }
});
Ext.form.IconComboBox = function(config) {
    Ext.form.IconComboBox.superclass.constructor.call(this, config);
    this.tpl = config.tpl ||
          '<tpl for=".">'
        + '<table class="x-combo-list-item" width="100%"><tbody><tr>'
        + '<td width="10%">'
        + '<div class="{' + this.iconClsField + '} x-icon-combo-icon"></div>'
        + '</td><td><span class="x-icon-combo-font">{' + this.displayField + '}</span></td>'
        + '</tr></tbody></table>'
        + '</tpl>';
    this.on({
        render:{scope:this, fn:function() {
            var wrap = this.el.up('div.x-form-field-wrap');
            this.wrap.applyStyles({position:'relative'});
            this.el.addClass('x-icon-combo-input');
            this.flag = Ext.DomHelper.append(wrap, {
                tag: 'div', style:'position:absolute'
            });
        }}
    });
};

Ext.extend(Ext.form.IconComboBox, Ext.form.ComboBox, {
    setIconCls: function() {
        var rec = this.store.query(this.valueField, this.getValue()).itemAt(0);
        if(rec) {
            this.flag.className = 'x-icon-combo-icon ' + rec.get(this.iconClsField);
        }
    },
    setValue: function(value) {
        Ext.form.IconComboBox.superclass.setValue.call(this, value);
        this.setIconCls();
    }
}); 

/**
 * 
 * @param renderTo: button ext-id
 * @param config
 * @returns {Ext.Link}
 */
Ext.Link = function(renderTo, config){
  this.value = config.value;
  this.handler = config.handler;
  this.tooltip = config.tooltip;
  this.renderTo = renderTo;
  try{
  Ext.Link.superclass.constructor.call(this, renderTo, config);
  }catch(e){}
};


Ext.extend(Ext.Link, Ext.Button, {
    render : function(renderTo){
        this.tooltip = this.tooltip || "";
        var tplHTML = '<a style="cursor:pointer;" qtip="'+this.tooltip +'"';
        tplHTML = tplHTML + '> {value:htmlEncode}</a>';
        var tpl = new Ext.Template(tplHTML);
        var btn = tpl.append(this.renderTo, 
                            {
                             value: this.value || "", 
                             cursor: this.disabled ? "none" : "hand",
                             tooltip: this.tooltip || ""
                            },true);
        btn.on("click", this.onClick, this, true);          
        if(this.cls){
            btn.addClass(this.cls);
        }
        this.el = btn;
        if(this.hidden){
            this.hide();
        }
    }
});
Ext.ux.TabCloseMenu = function(){
    var tabs, menu, ctxItem;
    this.init = function(tp){
        tabs = tp;
        tabs.on('contextmenu', onContextMenu);
    };

    function onContextMenu(ts, item, e){
        if(!menu){ // create context menu on first right click
            menu = new Ext.menu.Menu([{
                id: tabs.id + '-close',
                text: 'Close Tab',
                handler : function(){
                    tabs.remove(ctxItem);
                }
            },{
                id: tabs.id + '-close-others',
                text: 'Close Other Tabs',
                handler : function(){
                    tabs.items.each(function(item){
                        if(item.closable && item != ctxItem){
                            tabs.remove(item);
                        }
                    });
                }
            }]);
        }
        ctxItem = item;
        var items = menu.items;
        items.get(tabs.id + '-close').setDisabled(!item.closable);
        var disableOthers = true;
        tabs.items.each(function(){
            if(this != item && this.closable){
                disableOthers = false;
                return false;
            }
        });
        items.get(tabs.id + '-close-others').setDisabled(disableOthers);
        menu.showAt(e.getPoint());
    }
};

/*!
 * Ext JS Library 3.4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */
Ext.ns('Ext.ux.form');

/**
 * @class Ext.ux.form.FileUploadField
 * @extends Ext.form.TextField
 * Creates a file upload field.
 * @xtype fileuploadfield
 */
Ext.ux.form.FileUploadField = Ext.extend(Ext.form.TextField,  {
    /**
     * @cfg {String} buttonText The button text to display on the upload button (defaults to
     * 'Browse...').  Note that if you supply a value for {@link #buttonCfg}, the buttonCfg.text
     * value will be used instead if available.
     */
    buttonText: 'Browse...',
    /**
     * @cfg {Boolean} buttonOnly True to display the file upload field as a button with no visible
     * text field (defaults to false).  If true, all inherited TextField members will still be available.
     */
    buttonOnly: false,
    /**
     * @cfg {Number} buttonOffset The number of pixels of space reserved between the button and the text field
     * (defaults to 3).  Note that this only applies if {@link #buttonOnly} = false.
     */
    buttonOffset: 3,
    /**
     * @cfg {Object} buttonCfg A standard {@link Ext.Button} config object.
     */

    // private
    readOnly: true,

    /**
     * @hide
     * @method autoSize
     */
    autoSize: Ext.emptyFn,

    // private
    initComponent: function(){
        Ext.ux.form.FileUploadField.superclass.initComponent.call(this);

        this.addEvents(
            /**
             * @event fileselected
             * Fires when the underlying file input field's value has changed from the user
             * selecting a new file from the system file selection dialog.
             * @param {Ext.ux.form.FileUploadField} this
             * @param {String} value The file value returned by the underlying file input field
             */
            'fileselected'
        );
    },

    // private
    onRender : function(ct, position){
        Ext.ux.form.FileUploadField.superclass.onRender.call(this, ct, position);

        this.wrap = this.el.wrap({cls:'x-form-field-wrap x-form-file-wrap'});
        this.el.addClass('x-form-file-text');
        this.el.dom.removeAttribute('name');
        this.createFileInput();

        var btnCfg = Ext.applyIf(this.buttonCfg || {}, {
            text: this.buttonText
        });
        this.button = new Ext.Button(Ext.apply(btnCfg, {
            renderTo: this.wrap,
            cls: 'x-form-file-btn' + (btnCfg.iconCls ? ' x-btn-icon' : '')
        }));

        if(this.buttonOnly){
            this.el.hide();
            this.wrap.setWidth(this.button.getEl().getWidth());
        }

        this.bindListeners();
        this.resizeEl = this.positionEl = this.wrap;
    },
    
    bindListeners: function(){
        this.fileInput.on({
            scope: this,
            mouseenter: function() {
                this.button.addClass(['x-btn-over','x-btn-focus'])
            },
            mouseleave: function(){
                this.button.removeClass(['x-btn-over','x-btn-focus','x-btn-click'])
            },
            mousedown: function(){
                this.button.addClass('x-btn-click')
            },
            mouseup: function(){
                this.button.removeClass(['x-btn-over','x-btn-focus','x-btn-click'])
            },
            change: function(){
                var v = this.fileInput.dom.value;
                this.setValue(v);
                this.fireEvent('fileselected', this, v);    
            }
        }); 
    },
    
    createFileInput : function() {
        this.fileInput = this.wrap.createChild({
            id: this.getFileInputId(),
            name: this.name||this.getId(),
            cls: 'x-form-file',
            tag: 'input',
            type: 'file',
            size: 1
        });
    },
    
    reset : function(){
        if (this.rendered) {
            this.fileInput.remove();
            this.createFileInput();
            this.bindListeners();
        }
        Ext.ux.form.FileUploadField.superclass.reset.call(this);
    },

    // private
    getFileInputId: function(){
        return this.id + '-file';
    },

    // private
    onResize : function(w, h){
        Ext.ux.form.FileUploadField.superclass.onResize.call(this, w, h);

        this.wrap.setWidth(w);

        if(!this.buttonOnly){
            var w = this.wrap.getWidth() - this.button.getEl().getWidth() - this.buttonOffset;
            this.el.setWidth(w);
        }
    },

    // private
    onDestroy: function(){
        Ext.ux.form.FileUploadField.superclass.onDestroy.call(this);
        Ext.destroy(this.fileInput, this.button, this.wrap);
    },
    
    onDisable: function(){
        Ext.ux.form.FileUploadField.superclass.onDisable.call(this);
        this.doDisable(true);
    },
    
    onEnable: function(){
        Ext.ux.form.FileUploadField.superclass.onEnable.call(this);
        this.doDisable(false);

    },
    
    // private
    doDisable: function(disabled){
        this.fileInput.dom.disabled = disabled;
        this.button.setDisabled(disabled);
    },


    // private
    preFocus : Ext.emptyFn,

    // private
    alignErrorIcon : function(){
        this.errorIcon.alignTo(this.wrap, 'tl-tr', [2, 0]);
    }

});

Ext.reg('fileuploadfield', Ext.ux.form.FileUploadField);

// backwards compat
Ext.form.FileUploadField = Ext.ux.form.FileUploadField;


Ext.ImageButton = function(renderTo, config){
  this.imgPath = config.imgPath;
  this.handler = config.handler;
  this.tooltip = config.tooltip;
  try{
  Ext.ImageButton.superclass.constructor.call(this, renderTo, config);
  }catch(e){}
};


Ext.extend(Ext.ImageButton, Ext.Button, {
    render : function(renderTo){
        this.disabledImgPath = this.disabledImgPath || this.imgPath;
        this.tooltip = this.tooltip || "";
        var tplHTML = '<span><img style="cursor:pointer;" src="{imgPath}" border=0 qtip="'+this.tooltip +'"';
        tplHTML = tplHTML + '/> {imgText:htmlEncode}</span>';
        var tpl = new Ext.Template(tplHTML);
        var btn = tpl.append(renderTo, 
                            {imgPath: this.disabled ? this.disabledImgPath : this.imgPath, 
                             imgText: this.text || "", 
                             cursor: this.disabled ? "none" : "hand",
                             tooltip: this.tooltip || ""
                            },true);
        btn.on("click", this.onClick, this, true);          
        if(this.cls){
            btn.addClass(this.cls);
        }
        this.el = btn;
        if(this.hidden){
            this.hide();
        }
    },
    disable : function(newImgPath){
      var replaceImgPath = newImgPath || this.disabledImgPath;
      if (replaceImgPath)
        this.el.dom.firstChild.src = replaceImgPath;
      Ext.fly(this.el.dom.firstChild).setStyle('cursor', 'none');
      this.disabled = true;
    },
    enable : function(newImgPath){
      var replaceImgPath = newImgPath || this.imgPath;
      if (replaceImgPath)
        this.el.dom.firstChild.src = replaceImgPath;
      Ext.fly(this.el.dom.firstChild).setStyle('cursor', 'hand');
      this.disabled = false;
    }
});
/**
 * @class Ext.ux.form.StaticTextField
 * @extends Ext.BoxComponent
 * Base class to easily display static text in a form layout.
 * @constructor
 * Creates a new StaticTextField Field
 * @param {Object} config Configuration options
 * @author Based on MiscField by Nullity with modifications by Aparajita Fishman
 */
Ext.namespace('Ext.ux.form');

Ext.ux.form.StaticTextField = function(config){
    this.name = config.name || config.id;
    Ext.ux.form.StaticTextField.superclass.constructor.call(this, config);
};

Ext.extend(Ext.ux.form.StaticTextField, Ext.BoxComponent,  {
    /**
     * @cfg {String/Object} autoCreate A DomHelper element spec, or true for a default element spec (defaults to
     * {tag: "div"})
     */
    defaultAutoCreate : {tag: "div"},

    /**
     * @cfg {String} fieldClass The default CSS class for the field (defaults to "x-form-field")
     */
    fieldClass : "x-form-text",

    // private
    isFormField : true,

    /**
     * @cfg {Boolean} postValue True to create a hidden field that will post the field's value during a submit
     */
    submitValue : false,

    /**
     * @cfg {Mixed} value A value to initialize this field with.
     */
    value : undefined,

    /**
     * @cfg {Boolean} disableReset True to prevent this field from being reset when calling Ext.form.Form.reset()
     */
    disableReset: false,

    // private
    field: null,
    
    /**
     * Returns the name attribute of the field if available
     * @return {String} name The field name
     */
    getName: function(){
         return this.name;
    },

    // private
    onRender : function(ct, position){
        Ext.ux.form.StaticTextField.superclass.onRender.call(this, ct, position);
        if(!this.el){
            var cfg = this.getAutoCreate();
            this.el = ct.createChild(cfg, position);
        
            if (this.submitValue) {
                this.field = ct.createChild({tag:'input', type:'hidden', name: this.getName(), id: ''}, position);
            }
        }

        this.el.addClass([this.fieldClass, this.cls, 'ux-form-statictextfield']);
        this.initValue();
    },

    // private
    afterRender : function(ct, position){
        Ext.ux.form.StaticTextField.superclass.afterRender.call(this);
        this.initEvents();
    },

    // private
    initValue : function(){
        if(this.value !== undefined){
            this.setValue(this.value);
        }else if(this.el.dom.innerHTML.length > 0){
            this.setValue(this.el.dom.innerHTML);
        }
    },

    /**
     * Returns true if this field has been changed since it was originally loaded.
     */
    isDirty : function() {
        return false;
    },

    /**
     * Resets the current field value to the originally-loaded value
     * @param {Boolean} force Force a reset even if the option disableReset is true
     */
    reset : function(force){
        if(!this.disableReset || force === true){
            this.setValue(this.originalValue);
        }
    },

    // private
    initEvents : function(){
        // reference to original value for reset
        this.originalValue = this.getRawValue();
    },

    /**
     * Returns whether or not the field value is currently valid
     * Always returns true, not used in StaticTextField.
     * @return {Boolean} True
     */
    isValid : function(){
        return true;
    },

    /**
     * Validates the field value
     * Always returns true, not used in StaticTextField.  Required for Ext.form.Form.isValid()
     * @return {Boolean} True
     */
    validate : function(){
        return true;
    },

    processValue : function(value){
        return value;
    },

    // private
    // Subclasses should provide the validation implementation by overriding this
    validateValue : function(value){
        return true;
    },

    /**
     * Mark this field as invalid
     * Not used in StaticTextField.   Required for Ext.form.Form.markInvalid()
     */
    markInvalid : function(){
        return;
    },

    /**
     * Clear any invalid styles/messages for this field
     * Not used in StaticTextField.   Required for Ext.form.Form.clearInvalid()
     */
    clearInvalid : function(){
        return;
    },

    /**
     * Returns the raw field value.
     * @return {Mixed} value The field value
     */
    getRawValue : function(){
       return (this.rendered) ? this.value : null;
    },

    /**
     * Returns the clean field value.
     * @return {String} value The field value
     */
    getValue : function(){
        return this.getRawValue();
    },

    /**
     * Sets the raw field value. The display text is <strong>not</strong> HTML encoded.
     * @param {Mixed} value The value to set
     */
    setRawValue : function(v){
        this.value = v;
        if(this.rendered){
            this.el.dom.innerHTML = v;
            if(this.field){
                this.field.dom.value = v;
            }
        }
    },

    /**
     * Sets the field value. The display text is HTML encoded.
     * @param {Mixed} value The value to set
     */
    setValue : function(v){
        this.value = v;
        if(this.rendered){
          if(this.link){
            this.el.dom.innerHTML = '<a href="'+this.link+'">'+Ext.util.Format.htmlEncode(v)+'</a>';
          }else{
            this.el.dom.innerHTML = Ext.util.Format.htmlEncode(v);
          }
            if(this.field){
                this.field.dom.value = v;
            }
        }
    }
});

Ext.reg('statictextfield', Ext.ux.form.StaticTextField);       
Ext.ns('Ext.ux.grid');

Ext.ux.grid.ColumnHeaderGroup = Ext.extend(Ext.util.Observable, {

    constructor: function(config){
        this.config = config;
    },

    init: function(grid){
        Ext.applyIf(grid.colModel, this.config);
        Ext.apply(grid.getView(), this.viewConfig);
    },

    viewConfig: {
        initTemplates: function(){
            this.constructor.prototype.initTemplates.apply(this, arguments);
            var ts = this.templates || {};
            if(!ts.gcell){
                ts.gcell = new Ext.XTemplate('<td class="x-grid3-hd x-grid3-gcell x-grid3-td-{id} ux-grid-hd-group-row-{row} {cls}" style="{style}">', '<div {tooltip} class="x-grid3-hd-inner x-grid3-hd-{id}" unselectable="on" style="{istyle}">', this.grid.enableHdMenu ? '<a class="x-grid3-hd-btn" href="#"></a>' : '', '{value}</div></td>');
            }
            this.templates = ts;
            this.hrowRe = new RegExp("ux-grid-hd-group-row-(\\d+)", "");
        },

        renderHeaders: function(){
            var ts = this.templates, headers = [], cm = this.cm, rows = cm.rows, tstyle = 'width:' + this.getTotalWidth() + ';';

            for(var row = 0, rlen = rows.length; row < rlen; row++){
                var r = rows[row], cells = [];
                for(var i = 0, gcol = 0, len = r.length; i < len; i++){
                    var group = r[i];
                    group.colspan = group.colspan || 1;
                    var id = this.getColumnId(group.dataIndex ? cm.findColumnIndex(group.dataIndex) : gcol), gs = Ext.ux.grid.ColumnHeaderGroup.prototype.getGroupStyle.call(this, group, gcol);
                    cells[i] = ts.gcell.apply({
                        cls: 'ux-grid-hd-group-cell',
                        id: id,
                        row: row,
                        style: 'width:' + gs.width + ';' + (gs.hidden ? 'display:none;' : '') + (group.align ? 'text-align:' + group.align + ';' : ''),
                        tooltip: group.tooltip ? (Ext.QuickTips.isEnabled() ? 'ext:qtip' : 'title') + '="' + group.tooltip + '"' : '',
                        istyle: group.align == 'right' ? 'padding-right:16px' : '',
                        btn: this.grid.enableHdMenu && group.header,
                        value: group.header || '&nbsp;'
                    });
                    gcol += group.colspan;
                }
                headers[row] = ts.header.apply({
                    tstyle: tstyle,
                    cells: cells.join('')
                });
            }
            headers.push(this.constructor.prototype.renderHeaders.apply(this, arguments));
            return headers.join('');
        },

        onColumnWidthUpdated: function(){
            this.constructor.prototype.onColumnWidthUpdated.apply(this, arguments);
            Ext.ux.grid.ColumnHeaderGroup.prototype.updateGroupStyles.call(this);
        },

        onAllColumnWidthsUpdated: function(){
            this.constructor.prototype.onAllColumnWidthsUpdated.apply(this, arguments);
            Ext.ux.grid.ColumnHeaderGroup.prototype.updateGroupStyles.call(this);
        },

        onColumnHiddenUpdated: function(){
            this.constructor.prototype.onColumnHiddenUpdated.apply(this, arguments);
            Ext.ux.grid.ColumnHeaderGroup.prototype.updateGroupStyles.call(this);
        },

        getHeaderCell: function(index){
            return this.mainHd.query(this.cellSelector)[index];
        },

        findHeaderCell: function(el){
            return el ? this.fly(el).findParent('td.x-grid3-hd', this.cellSelectorDepth) : false;
        },

        findHeaderIndex: function(el){
            var cell = this.findHeaderCell(el);
            return cell ? this.getCellIndex(cell) : false;
        },

        updateSortIcon: function(col, dir){
            var sc = this.sortClasses, hds = this.mainHd.select(this.cellSelector).removeClass(sc);
            hds.item(col).addClass(sc[dir == "DESC" ? 1 : 0]);
        },

        handleHdDown: function(e, t){
            var el = Ext.get(t);
            if(el.hasClass('x-grid3-hd-btn')){
                e.stopEvent();
                var hd = this.findHeaderCell(t);
                Ext.fly(hd).addClass('x-grid3-hd-menu-open');
                var index = this.getCellIndex(hd);
                this.hdCtxIndex = index;
                var ms = this.hmenu.items, cm = this.cm;
                ms.get('asc').setDisabled(!cm.isSortable(index));
                ms.get('desc').setDisabled(!cm.isSortable(index));
                this.hmenu.on('hide', function(){
                    Ext.fly(hd).removeClass('x-grid3-hd-menu-open');
                }, this, {
                    single: true
                });
                this.hmenu.show(t, 'tl-bl?');
            }else if(el.hasClass('ux-grid-hd-group-cell') || Ext.fly(t).up('.ux-grid-hd-group-cell')){
                e.stopEvent();
            }
        },

        handleHdMove: function(e, t){
            var hd = this.findHeaderCell(this.activeHdRef);
            if(hd && !this.headersDisabled && !Ext.fly(hd).hasClass('ux-grid-hd-group-cell')){
                var hw = this.splitHandleWidth || 5, r = this.activeHdRegion, x = e.getPageX(), ss = hd.style, cur = '';
                if(this.grid.enableColumnResize !== false){
                    if(x - r.left <= hw && this.cm.isResizable(this.activeHdIndex - 1)){
                        cur = Ext.isAir ? 'move' : Ext.isWebKit ? 'e-resize' : 'col-resize'; // col-resize
                                                                                                // not
                                                                                                // always
                                                                                                // supported
                    }else if(r.right - x <= (!this.activeHdBtn ? hw : 2) && this.cm.isResizable(this.activeHdIndex)){
                        cur = Ext.isAir ? 'move' : Ext.isWebKit ? 'w-resize' : 'col-resize';
                    }
                }
                ss.cursor = cur;
            }
        },

        handleHdOver: function(e, t){
            var hd = this.findHeaderCell(t);
            if(hd && !this.headersDisabled){
                this.activeHdRef = t;
                this.activeHdIndex = this.getCellIndex(hd);
                var fly = this.fly(hd);
                this.activeHdRegion = fly.getRegion();
                if(!(this.cm.isMenuDisabled(this.activeHdIndex) || fly.hasClass('ux-grid-hd-group-cell'))){
                    fly.addClass('x-grid3-hd-over');
                    this.activeHdBtn = fly.child('.x-grid3-hd-btn');
                    if(this.activeHdBtn){
                        this.activeHdBtn.dom.style.height = (hd.firstChild.offsetHeight - 1) + 'px';
                    }
                }
            }
        },

        handleHdOut: function(e, t){
            var hd = this.findHeaderCell(t);
            if(hd && (!Ext.isIE || !e.within(hd, true))){
                this.activeHdRef = null;
                this.fly(hd).removeClass('x-grid3-hd-over');
                hd.style.cursor = '';
            }
        },

        handleHdMenuClick: function(item){
            var index = this.hdCtxIndex, cm = this.cm, ds = this.ds, id = item.getItemId();
            switch(id){
                case 'asc':
                    ds.sort(cm.getDataIndex(index), 'ASC');
                    break;
                case 'desc':
                    ds.sort(cm.getDataIndex(index), 'DESC');
                    break;
                default:
                    if(id.substr(0, 5) == 'group'){
                        var i = id.split('-'), row = parseInt(i[1], 10), col = parseInt(i[2], 10), r = this.cm.rows[row], group, gcol = 0;
                        for(var i = 0, len = r.length; i < len; i++){
                            group = r[i];
                            if(col >= gcol && col < gcol + group.colspan){
                                break;
                            }
                            gcol += group.colspan;
                        }
                        if(item.checked){
                            var max = cm.getColumnsBy(this.isHideableColumn, this).length;
                            for(var i = gcol, len = gcol + group.colspan; i < len; i++){
                                if(!cm.isHidden(i)){
                                    max--;
                                }
                            }
                            if(max < 1){
                                this.onDenyColumnHide();
                                return false;
                            }
                        }
                        for(var i = gcol, len = gcol + group.colspan; i < len; i++){
                            if(cm.config[i].fixed !== true && cm.config[i].hideable !== false){
                                cm.setHidden(i, item.checked);
                            }
                        }
                    }else{
                        index = cm.getIndexById(id.substr(4));
                        if(index != -1){
                            if(item.checked && cm.getColumnsBy(this.isHideableColumn, this).length <= 1){
                                this.onDenyColumnHide();
                                return false;
                            }
                            cm.setHidden(index, item.checked);
                        }
                    }
                    item.checked = !item.checked;
                    if(item.menu){
                        var updateChildren = function(menu){
                            menu.items.each(function(childItem){
                                if(!childItem.disabled){
                                    childItem.setChecked(item.checked, false);
                                    if(childItem.menu){
                                        updateChildren(childItem.menu);
                                    }
                                }
                            });
                        }
                        updateChildren(item.menu);
                    }
                    var parentMenu = item, parentItem;
                    while(parentMenu = parentMenu.parentMenu){
                        if(!parentMenu.parentMenu || !(parentItem = parentMenu.parentMenu.items.get(parentMenu.getItemId())) || !parentItem.setChecked){
                            break;
                        }
                        var checked = parentMenu.items.findIndexBy(function(m){
                            return m.checked;
                        }) >= 0;
                        parentItem.setChecked(checked, true);
                    }
                    item.checked = !item.checked;
            }
            return true;
        },

        beforeColMenuShow: function(){
            var cm = this.cm, rows = this.cm.rows;
            this.colMenu.removeAll();
            for(var col = 0, clen = cm.getColumnCount(); col < clen; col++){
                var menu = this.colMenu, title = cm.getColumnHeader(col), text = [];
                if(cm.config[col].fixed !== true && cm.config[col].hideable !== false){
                    for(var row = 0, rlen = rows.length; row < rlen; row++){
                        var r = rows[row], group, gcol = 0;
                        for(var i = 0, len = r.length; i < len; i++){
                            group = r[i];
                            if(col >= gcol && col < gcol + group.colspan){
                                break;
                            }
                            gcol += group.colspan;
                        }
                        if(group && group.header){
                            if(cm.hierarchicalColMenu){
                                var gid = 'group-' + row + '-' + gcol;
                                var item = menu.items.item(gid);
                                var submenu = item ? item.menu : null;
                                if(!submenu){
                                    submenu = new Ext.menu.Menu({
                                        itemId: gid
                                    });
                                    submenu.on("itemclick", this.handleHdMenuClick, this);
                                    var checked = false, disabled = true;
                                    for(var c = gcol, lc = gcol + group.colspan; c < lc; c++){
                                        if(!cm.isHidden(c)){
                                            checked = true;
                                        }
                                        if(cm.config[c].hideable !== false){
                                            disabled = false;
                                        }
                                    }
                                    menu.add({
                                        itemId: gid,
                                        text: group.header,
                                        menu: submenu,
                                        hideOnClick: false,
                                        checked: checked,
                                        disabled: disabled
                                    });
                                }
                                menu = submenu;
                            }else{
                                text.push(group.header);
                            }
                        }
                    }
                    text.push(title);
                    menu.add(new Ext.menu.CheckItem({
                        itemId: "col-" + cm.getColumnId(col),
                        text: text.join(' '),
                        checked: !cm.isHidden(col),
                        hideOnClick: false,
                        disabled: cm.config[col].hideable === false
                    }));
                }
            }
        },

        renderUI: function(){
            this.constructor.prototype.renderUI.apply(this, arguments);
            Ext.apply(this.columnDrop, Ext.ux.grid.ColumnHeaderGroup.prototype.columnDropConfig);
            Ext.apply(this.splitZone, Ext.ux.grid.ColumnHeaderGroup.prototype.splitZoneConfig);
        }
    },

    splitZoneConfig: {
        allowHeaderDrag: function(e){
            return !e.getTarget(null, null, true).hasClass('ux-grid-hd-group-cell');
        }
    },

    columnDropConfig: {
        getTargetFromEvent: function(e){
            var t = Ext.lib.Event.getTarget(e);
            return this.view.findHeaderCell(t);
        },

        positionIndicator: function(h, n, e){
            var data = Ext.ux.grid.ColumnHeaderGroup.prototype.getDragDropData.call(this, h, n, e);
            if(data === false){
                return false;
            }
            var px = data.px + this.proxyOffsets[0];
            this.proxyTop.setLeftTop(px, data.r.top + this.proxyOffsets[1]);
            this.proxyTop.show();
            this.proxyBottom.setLeftTop(px, data.r.bottom);
            this.proxyBottom.show();
            return data.pt;
        },

        onNodeDrop: function(n, dd, e, data){
            var h = data.header;
            if(h != n){
                var d = Ext.ux.grid.ColumnHeaderGroup.prototype.getDragDropData.call(this, h, n, e);
                if(d === false){
                    return false;
                }
                var cm = this.grid.colModel, right = d.oldIndex < d.newIndex, rows = cm.rows;
                for(var row = d.row, rlen = rows.length; row < rlen; row++){
                    var r = rows[row], len = r.length, fromIx = 0, span = 1, toIx = len;
                    for(var i = 0, gcol = 0; i < len; i++){
                        var group = r[i];
                        if(d.oldIndex >= gcol && d.oldIndex < gcol + group.colspan){
                            fromIx = i;
                        }
                        if(d.oldIndex + d.colspan - 1 >= gcol && d.oldIndex + d.colspan - 1 < gcol + group.colspan){
                            span = i - fromIx + 1;
                        }
                        if(d.newIndex >= gcol && d.newIndex < gcol + group.colspan){
                            toIx = i;
                        }
                        gcol += group.colspan;
                    }
                    var groups = r.splice(fromIx, span);
                    rows[row] = r.splice(0, toIx - (right ? span : 0)).concat(groups).concat(r);
                }
                for(var c = 0; c < d.colspan; c++){
                    var oldIx = d.oldIndex + (right ? 0 : c), newIx = d.newIndex + (right ? -1 : c);
                    cm.moveColumn(oldIx, newIx);
                    this.grid.fireEvent("columnmove", oldIx, newIx);
                }
                return true;
            }
            return false;
        }
    },

    getGroupStyle: function(group, gcol){
        var width = 0, hidden = true;
        for(var i = gcol, len = gcol + group.colspan; i < len; i++){
            if(!this.cm.isHidden(i)){
                var cw = this.cm.getColumnWidth(i);
                if(typeof cw == 'number'){
                    width += cw;
                }
                hidden = false;
            }
        }
        return {
            width: (Ext.isBorderBox || (Ext.isWebKit && !Ext.isSafari2) ? width : Math.max(width - this.borderWidth, 0)) + 'px',
            hidden: hidden
        };
    },

    updateGroupStyles: function(col){
        var tables = this.mainHd.query('.x-grid3-header-offset > table'), tw = this.getTotalWidth(), rows = this.cm.rows;
        for(var row = 0; row < tables.length; row++){
            tables[row].style.width = tw;
            if(row < rows.length){
                var cells = tables[row].firstChild.firstChild.childNodes;
                for(var i = 0, gcol = 0; i < cells.length; i++){
                    var group = rows[row][i];
                    if((typeof col != 'number') || (col >= gcol && col < gcol + group.colspan)){
                        var gs = Ext.ux.grid.ColumnHeaderGroup.prototype.getGroupStyle.call(this, group, gcol);
                        cells[i].style.width = gs.width;
                        cells[i].style.display = gs.hidden ? 'none' : '';
                    }
                    gcol += group.colspan;
                }
            }
        }
    },

    getGroupRowIndex: function(el){
        if(el){
            var m = el.className.match(this.hrowRe);
            if(m && m[1]){
                return parseInt(m[1], 10);
            }
        }
        return this.cm.rows.length;
    },

    getGroupSpan: function(row, col){
        if(row < 0){
            return {
                col: 0,
                colspan: this.cm.getColumnCount()
            };
        }
        var r = this.cm.rows[row];
        if(r){
            for(var i = 0, gcol = 0, len = r.length; i < len; i++){
                var group = r[i];
                if(col >= gcol && col < gcol + group.colspan){
                    return {
                        col: gcol,
                        colspan: group.colspan
                    };
                }
                gcol += group.colspan;
            }
            return {
                col: gcol,
                colspan: 0
            };
        }
        return {
            col: col,
            colspan: 1
        };
    },

    getDragDropData: function(h, n, e){
        if(h.parentNode != n.parentNode){
            return false;
        }
        var cm = this.grid.colModel, x = Ext.lib.Event.getPageX(e), r = Ext.lib.Dom.getRegion(n.firstChild), px, pt;
        if((r.right - x) <= (r.right - r.left) / 2){
            px = r.right + this.view.borderWidth;
            pt = "after";
        }else{
            px = r.left;
            pt = "before";
        }
        var oldIndex = this.view.getCellIndex(h), newIndex = this.view.getCellIndex(n);
        if(cm.isFixed(newIndex)){
            return false;
        }
        var row = Ext.ux.grid.ColumnHeaderGroup.prototype.getGroupRowIndex.call(this.view, h),
            oldGroup = Ext.ux.grid.ColumnHeaderGroup.prototype.getGroupSpan.call(this.view, row, oldIndex),
            newGroup = Ext.ux.grid.ColumnHeaderGroup.prototype.getGroupSpan.call(this.view, row, newIndex),
            oldIndex = oldGroup.col;
            newIndex = newGroup.col + (pt == "after" ? newGroup.colspan : 0);
        if(newIndex >= oldGroup.col && newIndex <= oldGroup.col + oldGroup.colspan){
            return false;
        }
        var parentGroup = Ext.ux.grid.ColumnHeaderGroup.prototype.getGroupSpan.call(this.view, row - 1, oldIndex);
        if(newIndex < parentGroup.col || newIndex > parentGroup.col + parentGroup.colspan){
            return false;
        }
        return {
            r: r,
            px: px,
            pt: pt,
            row: row,
            oldIndex: oldIndex,
            newIndex: newIndex,
            colspan: oldGroup.colspan
        };
    }
});

function showButtonLight(e){
	if (!this.disabled) {
		var internal = e.within(this.el, true); // ����¼���Ŀ����el����������ӽڵ� ��������
		if (!internal) {
			this.el.addClass('x-btn-over');
			if (!this.monitoringMouseOver) {
				this.doc.on('mouseover', this.monitorMouseOver, this);
				this.monitoringMouseOver = true;
			}
			this.fireEvent('mouseover', this, e);
		}
		if (this.isMenuTriggerOver(e, internal)) {
			this.fireEvent('menutriggerover', this, this.menu, e);
		}
	}
}

function getAutoIncreasedYearStr(yearstr, pastdate) {
	var gaps = getYearsFromPastToNow(pastdate);
	for (var i = 0; i < rx.applicant.rs.carexpdata.length; i++) {
		if (yearstr == rx.applicant.rs.carexpdata[i]) {
				if ((i + gaps) > rx.applicant.rs.carexpdata.length - 1) {
					return rx.applicant.rs.carexpdata[rx.applicant.rs.carexpdata.length - 1];
				}else
					return rx.applicant.rs.carexpdata[i + gaps];
		}
	}
}

function getYearsFromPastToNow(pastdate){
	var now = new Date();
	var nyear = now.getFullYear(); // ��ǰ��
	var nmonth = now.getMonth() + 1; // ��ǰ��
	var nday = now.getDate(); // ��ǰ��
	
	var pyear = pastdate.getFullYear(); // ��
	var pmonth = pastdate.getMonth() + 1; // ��
	var pday = pastdate.getDate(); // ��
	var count = 0;
	if(nyear >= pyear){
		if(nmonth > pmonth){
			count = nyear - pyear;
		}else if(nmonth == pmonth){
			if(nday >= pday){
				count = nyear - pyear;
			}else{
				count = nyear - pyear -1;
			}
		}else{
			count = nyear - pyear -1;
		}
	}
	return count;
}

function download(id){
    var cform = document.getElementById("download");
    cform.id.value = id;
    cform.submit();
}

function downloadWriter(id){
	var cform = document.getElementById("downloadWriter");
	cform.writerId.value = id;
	cform.submit();
}

function downloadfile(url){
    var cform = document.getElementById("downloadfile");
    cform.action = url;
    cform.submit();
}

function handleFormError(frm, action){
	switch (action.failureType) {
    case Ext.form.Action.CLIENT_INVALID:
        Ext.Msg.alert('Failure', 'Form fields may not be submitted with invalid values');
        break;
    case Ext.form.Action.CONNECT_FAILURE:
        Ext.Msg.alert('Failure', 'Ajax communication failed');
        break;
    case Ext.form.Action.SERVER_INVALID:
       Ext.Msg.alert('Failure', action.result.message);
}
}


common.replaceString = function(org, params){
	  for(var i=0;i<params.length;i++){
	    var param = '<'+i+'>';
	    org = org.replace(param, params[i]);
	  }
	  return org;
	}

/**
 * 扩展Ext.example功能.
 * @return {TypeName} 
 */
Ext.MsgTip = function(){
    var msgCt;
    function createBox(t, s){
        return ['<div class="msg">',
                '<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
                '<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc" style="font-size=12px;"><h3>', t, '</h3>', s, '</div></div></div>',
                '<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
                '</div>'].join('');
    }
    return {
        msg : function(title, message,fnt,autoHide,pauseTime){
            if(!msgCt){
                msgCt = Ext.DomHelper.insertFirst(document.body, {id:'msg-div22',style:'position:absolute;top:10px;width:300px;margin:0 auto;z-index:20000;'}, true);
            }
//            msgCt.alignTo(Ext.getBody(), "b-br", [-150, -100]);
            msgCt.alignTo(Ext.getBody(), "tr-tr");
            //给消息框右下角增加一个关闭按钮
            message+='<br><span style="text-align:right;font-size:12px; width:100%;">' +
              '<font color="blank"><u style="cursor:pointer;" onclick="Ext.MsgTip.hide(this,'+fnt+');">知道了</u></font></span>'
            var m = Ext.DomHelper.append(msgCt, {html:createBox(title,message)}, true);
//                m.slideIn('b');
                m.slideIn('t');
            if(!Ext.isEmpty(autoHide)&&autoHide==true){
              if(Ext.isEmpty(pauseTime)){
                 pauseTime=5;
              }
              m.pause(pauseTime).ghost("tr", {remove:true});
            }
       },
       hide:function(v,fnt){
          var msg=Ext.get(v.parentElement.parentElement.parentElement.parentElement.parentElement.parentElement);
          msg.ghost("t", {remove:true});
          fnt();
       }
    };
}();
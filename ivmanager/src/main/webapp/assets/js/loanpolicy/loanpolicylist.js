Ext.onReady(function () {

    init();

    /**
     * 初始化方法
     */
    function init() {
        var p = new Ext.grid.LoanPolicyGrid({
            title: '散标策略面板',
            iconCls: 'grid'
        });
        common.createWindow('loanpolicylist-win', p);
        p.load();
    }
});

/**
 * 定义LoanPolicyGrid对象
 * @param config
 * @returns {Ext.grid.LoanPolicyGrid}
 */
Ext.grid.LoanPolicyGrid = function (config) {
    var sm = new Ext.grid.CheckboxSelectionModel();
    // 数据源记录
    var Plant = Ext.data.Record.create([
        {name: 'id'},
        {name: 'userId'},
        {name: 'name'},
        {name: 'policyType'},
        {name: 'status'},
        {name: 'createTime'},
        {name: 'updateTime'}
    ]);
    // 定义数据源 获取DWRProxy代理数据
    this.ds = new Ext.data.Store({
        proxy: new Ext.data.DWRProxy(loanPolicyService.getLoanPolicies, false), // 分页排序选项设为true
        reader: new Ext.data.ListRangeReader({ // 数据解析
            totalProperty: 'totalSize'
        }, Plant),
        remoteSort: false
    });

    // 定义表头信息
    this.cm = new Ext.grid.ColumnModel([
        sm,
        new Ext.grid.RowNumberer(), // 列序号
        {
            header: '策略编号',
            width: 100,
            sortable: true,
            menuDisabled: true,
            dataIndex: 'id'
        },
        {
            header: '策略创建者',
            width: 100,
            sortable: true,
            menuDisabled: true,
            dataIndex: 'userId'
        },
        {
            header: '策略名称',
            width: 200,
            sortable: true,
            menuDisabled: true,
            dataIndex: 'name'
        },
        {
            header: '策略类型',
            width: 100,
            sortable: true,
            menuDisabled: true,
            dataIndex: 'policyType',
            renderer: function (val, meta, record, r, c, store) {
                if (val == 1) {
                    return '系统策略';
                } else if (val == 2) {
                    return '自定义策略';
                }
                return "";
            }
        },
        {
            header: '策略状态',
            width: 100,
            sortable: true,
            menuDisabled: true,
            dataIndex: 'status',
            renderer: function (val, meta, record, r, c, store) {
                if (val == 1) {
                    return '上架';
                } else if (val == 0) {
                    return '下架';
                }
                return "";
            }
        },
        {
            header: '创建时间',
            width: 150,
            sortable: true,
            menuDisabled: true,
            renderer: function (val, meta, record, r, c, store) {
                if (val) {
                    return val.dateFormat(constant.dateformat_ZH_HI_EN);
                }
            },
            dataIndex: 'createTime'
        },
        {
            header: '修改时间',
            width: 150,
            sortable: true,
            menuDisabled: true,
            renderer: function (val, meta, record, r, c, store) {
                if (val) {
                    return val.dateFormat(constant.dateformat_ZH_HI_EN);
                }
            },
            dataIndex: 'updateTime'
        }
    ]);

    var grid = this;
    var ds = this.ds;
    this.autoScroll = true;
    this.sm = sm;
    this.trackMouseOver = true;
    this.enableRowBody = true;
    this.animCollapse = true;
    this.collapsible = true;
    this.stripeRows = true; // 让grid相邻两行背景色不同 
    this.loadMask = true;
//    this.viewConfig = {forceFit:true};

    var policyTypeStore = new Ext.data.SimpleStore({
        fields: ['value', 'name'],
        data: [['1', '系统策略'], ['2', '自定义策略'], [null, '所有类型']]
    });

    // 查询条件组合框
    var policyTypeCombox = new Ext.form.ComboBox({
        editable: false,
        store: policyTypeStore,
        displayField: 'name',
        valueField: 'value',
        width: 130,
        typeAhead: true,
        emptyText: '请选择',
        mode: 'local',
        forceSelection: true,
        triggerAction: 'all', // 每次显示所有可选数据
        selectOnFocus: true,
//        resizable: true,
        listeners: {
            change: function () {
            }
        }
    });

    //用户ID
    var userIdTextField = new Ext.form.TextField({
        emptyText: '输入用户ID',
        allowBlank: true,
        width: 180
    });

    this.tbar = ['策略类型:', policyTypeCombox, '-', '用户ID:', userIdTextField, {
        text: '查询',
        tooltip: '根据条件查询信息',
        iconCls: 'search',
        handler: function () {
            var policyType = policyTypeCombox.getValue();
            var userId = userIdTextField.getValue();
            ds.reload({
                params: {start: 0, limit: common.pageSize, sort: common.defaultSort, dir: common.defaultDir},
                arg: [{userId: userId, policyType: policyType, status: null}]
            });
        }
    }, '-', {
        text: '清空',
        tooltip: '清空查询条件',
        iconCls: 'clear',
        handler: function (e) {
            userIdTextField.setValue(null);
            policyTypeCombox.setValue(null);
        }
    }, {
        text: '添加系统策略',
        iconCls: 'add',
        handler: function (el) {
            var loanPolicyDialog = new Ext.LoanPolicyDialog({ds: ds, pageStart: pageStart});
            loanPolicyDialog.showDialog(el, null, ds);
        }
    }, {
        text: '批量上架',
        icon: 'assets/images/menus/eye.png',
        handler: function (el) {
            var records = sm.getSelections();
            if (records == null || records.length == 0) {
                Ext.MessageBox.alert(constant.info, '请选择行', function () {
                });
                return;
            }

            Ext.MessageBox.confirm(constant.confirm, '上架后用户可以看到策略并使用,确认上架策略吗？', function (btn) {
                if (btn == 'yes') {
                    var ids = new Array;
                    for (var i = 0; i < records.length; i++) {
                        ids.push(records[i].get('id'));
                    }
                    loanPolicyService.updateLoanPolicyStatusByIds(1, ids, 1, function (data) {
                        ds.reload({
                            params: {
                                start: pageStart,
                                limit: common.pageSize,
                                sort: common.defaultSort,
                                dir: common.defaultDir
                            }
                        });
                    });
                }
            });
        }
    }, {
        text: '批量下架',
        iconCls: 'hide',
        handler: function (el) {
            var records = sm.getSelections();
            if (records == null || records.length == 0) {
                Ext.MessageBox.alert(constant.info, '请选择行', function () {
                });
                return;
            }

            Ext.MessageBox.confirm(constant.confirm, '下架后用户不能看到策略并使用,确认下架策略吗？', function (btn) {
                if (btn == 'yes') {
                    var ids = new Array;
                    for (var i = 0; i < records.length; i++) {
                        ids.push(records[i].get('id'));
                    }
                    loanPolicyService.updateLoanPolicyStatusByIds(1, ids, 0, function (data) {
                        ds.reload({
                            params: {
                                start: pageStart,
                                limit: common.pageSize,
                                sort: common.defaultSort,
                                dir: common.defaultDir
                            }
                        });
                    });
                }
            });
        }
    }];

    var pageStart = 0;
    this.bbar = new Ext.PagingToolbar({
        pageSize: common.pageSize,
        store: ds,
        doLoad: function (start) {
            this.store.reload({
                params: {
                    start: start,
                    limit: common.pageSize,
                    sort: common.defaultSort,
                    dir: common.defaultDir
                }
            });
            pageStart = start;
        },
        displayInfo: true,
        items: ['-']
    });

    // 调用构造函数创建子类实例
    Ext.grid.LoanPolicyGrid.superclass.constructor.call(this, config);

    // 添加单元格双击事件
    grid.on("celldblclick", function (g, r, c, el) {
        var record = g.getStore().getAt(r);
        var loanPolicyDialog = new Ext.LoanPolicyDialog({
            policyId: record.get('id'),
            ds: ds
        });
        loanPolicyDialog.showDialog(null, record, null, ds);
    });
};

// 继承Ext.grid.GridPanel
Ext.extend(Ext.grid.LoanPolicyGrid, Ext.grid.GridPanel, {
    load: function () {
        this.getStore().load({
            params: {
                start: 0,
                limit: common.pageSize,
                sort: common.defaultSort,
                dir: common.defaultDir
            }, arg: [{}]
        });
    }
});
/*** end LoanPolicyGrid ***/
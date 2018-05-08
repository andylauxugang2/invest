Ext.onReady(function () {

    init();

    /**
     * 初始化方法
     */
    function init() {
        var p = new Ext.grid.UserGrid({
            title: '标专家用户面板',
            iconCls: 'grid'
        });
        common.createWindow('userlist-win', p);
        p.load();
    }
});

/**
 * 定义UserGrid对象
 * @param config
 * @returns {Ext.grid.UserGrid}
 */
Ext.grid.UserGrid = function (config) {
    // 数据源记录
    var Plant = Ext.data.Record.create([
        {name: 'id'},
        {name: 'mobile'},
        {name: 'password'},
        {name: 'referrerMobile'},
        {name: 'nick'},
        {name: 'userToken'},
        {name: 'lastLoginTime'},
        {name: 'createTime'}
    ]);
    // 定义数据源 获取DWRProxy代理数据
    this.ds = new Ext.data.Store({
        proxy: new Ext.data.DWRProxy(ivmanagerUserService.getUsers, false), // 分页排序选项设为true
        reader: new Ext.data.ListRangeReader({ // 数据解析
            totalProperty: 'totalSize'
        }, Plant),
        remoteSort: false
    });

    var sm = new Ext.grid.CheckboxSelectionModel();
    // 定义表头信息
    this.cm = new Ext.grid.ColumnModel([
        sm,
        new Ext.grid.RowNumberer(), // 列序号
        {
            header: '用户编号',
            width: 100,
            sortable: true,
            menuDisabled: true,
            dataIndex: 'id'
        },
        {
            header: '手机号',
            width: 120,
            sortable: true,
            menuDisabled: true,
            dataIndex: 'mobile'
        },
        {
            header: '推荐人',
            width: 120,
            sortable: true,
            menuDisabled: true,
            dataIndex: 'referrerMobile'
        },
        {
            header: '昵称',
            width: 150,
            sortable: true,
            menuDisabled: false,
            dataIndex: 'nick'
        },
        {
            header: '用户令牌',
            width: 150,
            sortable: false,
            menuDisabled: true,
            dataIndex: 'userToken'
        },
        {
            header: '最后登录时间',
            width: 150,
            sortable: true,
            menuDisabled: true,
            renderer: function (val, meta, record, r, c, store) {
                if (val) {
                    return val.dateFormat(constant.dateformat_ZH_HI_EN);
                }
            },
            dataIndex: 'lastLoginTime'
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
        }
    ]);

    var grid = this;
    var ds = this.ds;
    this.sm = sm;
    this.autoScroll = true;
    this.trackMouseOver = true;
    this.enableRowBody = true;
    this.animCollapse = true;
    this.collapsible = true;
    this.stripeRows = true; // 让grid相邻两行背景色不同 
    this.loadMask = true;

    var mobileTextField = new Ext.form.TextField({
        emptyText: '输入手机号',
        allowBlank: true,
        width: 120
    });
    var userIdTextField = new Ext.form.NumberField({
        emptyText: '输入用户ID',
        allowDecimals: false, // 是否小数
        allowBlank: true,
        width: 120
    });

    this.tbar = ['手机号:', mobileTextField, '-', '用户编号:', userIdTextField, {
        text: '查询',
        tooltip: '根据条件查询信息',
        iconCls: 'search',
        handler: function () {
            var mobile = mobileTextField.getValue();
            var userId = userIdTextField.getValue();
            if (mobile == "") mobile = null;
            ds.reload({
                params: {start: 0, limit: common.pageSize, sort: common.defaultSort, dir: common.defaultDir},
                arg: [{userId: userId, mobile: mobile}]
            });
        }
    }, '-', {
        text: '清空',
        tooltip: '清空查询条件',
        iconCls: 'clear',
        handler: function (e) {
            mobileTextField.setValue(null);
            userIdTextField.setValue(null);
        }
    }, {
        text: '一键登录',
        iconCls: 'application_go',
        id: 'loginOneKeyBtn',
        handler: function (el) {
            var records = sm.getSelections();
            if (records == null || records.length == 0) {
                Ext.MessageBox.alert(constant.info, '请选择行', function () {
                });
                return;
            }

            if (records.length > 1) {
                Ext.MessageBox.alert(constant.info, '只可选择一行', function () {
                });
                return;
            }
            grid.body.mask('loading...', 'x-mask-loading');
            var username = records[0].get('mobile');
            //Ext.getCmp('loginOneKeyBtn').disable();
            el.disable();
            ivmanagerUserService.loginOneKey({username: username}, function (data) {
                if (data.success) {
                    grid.body.unmask();
                    el.enable();
                    window.open("http://www.biaozhuanjia.com/user/autologin?token=" + data.token);
                } else {
                    grid.body.unmask();
                    el.enable();
                    Ext.MessageBox.alert(constant.info, '请求出错', function () {
                    });
                }
            });
        }
    },{
        text: '查看用户授权账户',
        iconCls: 'user',
        handler: function (el) {
            var records = sm.getSelections();
            if (records == null || records.length == 0) {
                Ext.MessageBox.alert(constant.info, '请选择行', function () {
                });
                return;
            }

            if (records.length > 1) {
                Ext.MessageBox.alert(constant.info, '只可选择一行', function () {
                });
                return;
            }
            grid.body.mask('loading...', 'x-mask-loading');
            var username = records[0].get('mobile');
            //Ext.getCmp('loginOneKeyBtn').disable();
            el.disable();
            ivmanagerUserService.getUserThirdUUIDInfo({username: username}, function (data) {
                if (data.success) {
                    grid.body.unmask();
                    el.enable();
                    Ext.MessageBox.alert(constant.info, 'sssss', function () {
                    });
                } else {
                    grid.body.unmask();
                    el.enable();
                    Ext.MessageBox.alert(constant.info, '请求出错', function () {
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
    Ext.grid.UserGrid.superclass.constructor.call(this, config);

    // 添加单元格双击事件
    grid.on("celldblclick", function (g, r, c, el) {

    });
};

// 继承Ext.grid.GridPanel
Ext.extend(Ext.grid.UserGrid, Ext.grid.GridPanel, {
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
/*** end UserGrid ***/
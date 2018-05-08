/**
 * @param config
 * @returns {Ext.LoanPolicyDialog}
 */
Ext.LoanPolicyDialog = function (config) {
    this.parentConfig = config;
    this.policy = getLoanPolicy({
        dialog: this,
        policyId: config.policyId
    });
    this.items = [this.policy];
    this.width = common.wwidth * 0.75;
    this.iconCls = 'form';
    this.title = '信息浏览窗口';
    this.bodyStyle = 'padding:0px;';
    this.buttonAlign = 'right';
    this.plain = true;
    this.modal = true;
    this.resizable = true;
    Ext.LoanPolicyDialog.superclass.constructor.call(this, config);
    this.addButton('保存策略', this.savePolicy, this);
    this.addButton('删除策略', this.delPolicy, this);
    this.addButton('返回列表', this.close, this);
};

Ext.extend(Ext.LoanPolicyDialog, Ext.Window, {
    showDialog: function (e, record, ds) {
        this.record = record;
        this.ds = ds;
        var dialog = this;
        this.show(e, function () {
            if (record == null) {
                return;
            }
            dialog.body.mask('loading...', 'x-mask-loading');
            loanPolicyService.findLoanPolicyDetailById(record.get('id'), function (policy) {
                if (policy && policy['id']) {
                    dialog.policy.load(policy);
                }
                dialog.body.unmask();
            });
        });
    },
    close: function () { // 关闭触发调用
        var dialog = this;
        dialog.hide();
        if (dialog.closeAction == 'refresh') { // 点击过保存修改按钮 刷新列表
            dialog.parentConfig.ds.reload({
                params: {
                    start: dialog.parentConfig.pageStart,
                    limit: common.pageSize,
                    sort: common.defaultSort,
                    dir: common.defaultDir
                }
            });
        }
    },
    savePolicy: function () {
        var dialog = this;
        Ext.MessageBox.confirm(constant.confirm, '保存策略会触发投标策略的实时生效，确认修改该策略吗？', function (btn) {
            if (btn == 'yes') {
                Ext.MessageBox.confirm(constant.confirm, '保存不要太频繁,频繁的操作会导致投标策略更新性能下降，确认操作吗？', function (btn) {
                    if (btn == 'yes') {
                        dialog.policy.save();
                    }
                });
            }
        });
    },
    delPolicy: function () {
        var dialog = this;
        Ext.MessageBox.confirm(constant.confirm, '删除策略会导致用户已选策略全部失效，确认删除该策略吗？', function (btn) {
            if (btn == 'yes') {
                Ext.MessageBox.confirm(constant.confirm, '删除不要太频繁,频繁的操作会导致投标策略更新性能下降，确认操作吗？', function (btn) {
                    if (btn == 'yes') {
                        loanPolicyService.dropLoanPolicyById(dialog.parentConfig.policyId, function (data) {
                            dialog.hide();
                            dialog.parentConfig.ds.reload({
                                params: {
                                    start: dialog.parentConfig.pageStart,
                                    limit: common.pageSize,
                                    sort: common.defaultSort,
                                    dir: common.defaultDir
                                }
                            });
                        });
                    }
                });
            }
        });
    }
});


function getLoanPolicy(config) {

    var dialog = config.dialog;

    var id = Ext.id();

    var userId = new Ext.ux.form.StaticTextField({ // 静态输入框
        fieldLabel: '用户ID',
        readOnly: true,
        anchor: '90%'
    });
    var policyType = new Ext.ux.form.StaticTextField({ // 静态输入框
        fieldLabel: '策略类型',
        readOnly: true,
        anchor: '90%'
    });

    var blank = new Ext.ux.form.StaticTextField({ // 静态输入框
        fieldLabel: '',
        readOnly: true,
        anchor: '90%'
    });
    var policyId = new Ext.ux.form.StaticTextField({ // 静态输入框
        fieldLabel: '策略编号',
        readOnly: true,
        anchor: '90%'
    });
    var policyName = new Ext.form.TextField({
        fieldLabel: '策略名称',
        allowBlank: false,
        anchor: '90%'
    });
    var monthBegin = new Ext.form.NumberField({
        fieldLabel: '期限(起始月份)',
        allowDecimals: false, // 是否小数
        minValue: 0,
        maxValue: 36,
        anchor: '90%'
    });
    var monthEnd = new Ext.form.NumberField({
        fieldLabel: '期限(截止月份)',
        allowDecimals: false, // 是否小数
        minValue: 0,
        maxValue: 36,
        anchor: '90%'
    });
    var rateBegin = new Ext.form.NumberField({
        fieldLabel: '起始借款利率',
        allowDecimals: true, // 是否小数
        decimalPrecision: 2,
        anchor: '90%'
    });
    var rateEnd = new Ext.form.NumberField({
        fieldLabel: '最大借款利率',
        allowDecimals: true, // 是否小数
        decimalPrecision: 2,
        anchor: '90%'
    });
    var amountBegin = new Ext.form.NumberField({
        fieldLabel: '起始借款金额',
        allowDecimals: false, // 是否小数
        minValue: 0,
        anchor: '90%'
    });
    var amountEnd = new Ext.form.NumberField({
        fieldLabel: '最大借款金额',
        allowDecimals: false, // 是否小数
        minValue: 0,
        anchor: '90%'
    });

    var creditCode = new Ext.form.CheckboxGroup({
        xtype: 'checkboxgroup',
        fieldLabel: '魔镜等级',
        itemCls: 'x-check-group-alt',
        // Put all controls in a single column with width 100%
        columns: 8,
        anchor: '90%',
        columnWidth: 0.7,
        items: [
            {boxLabel: 'AAA', name: 'certificate', inputValue: 32},
            {boxLabel: 'AA', name: 'certificate', inputValue: 64},
            {boxLabel: 'A', name: 'certificate', inputValue: 128},
            {boxLabel: 'B', name: 'certificate', inputValue: 256},
            {boxLabel: 'C', name: 'certificate', inputValue: 512},
            {boxLabel: 'D', name: 'certificate', inputValue: 1024},
            {boxLabel: 'E', name: 'certificate', inputValue: 2048},
            {boxLabel: 'F', name: 'certificate', inputValue: 4096}
        ]
    });

    var ageBegin = new Ext.form.NumberField({
        fieldLabel: '起始年龄',
        allowDecimals: false, // 是否小数
        minValue: 0,
        anchor: '90%'
    });
    var ageEnd = new Ext.form.NumberField({
        fieldLabel: '最大年龄',
        allowDecimals: false, // 是否小数
        minValue: 0,
        anchor: '90%'
    });

    /*var sex = {
     xtype: 'radiogroup',
     fieldLabel: '借款人性别',
     columnWidth: 0.7,
     items: [
     {boxLabel: '男', name: 'sex', inputValue: 1, id: 'man'},
     {boxLabel: '女', name: 'sex', inputValue: 0},
     {boxLabel: '全部', name: 'sex', inputValue: null}
     ],
     anchor: '90%'
     };*/

    var sex = new Ext.form.RadioGroup({
        id: 'sexRadioGroup' + id,
        name: "sexRadioGroup",
        fieldLabel: '借款人性别',
        columnWidth: 0.7,
        anchor: '90%',
        items: [
            new Ext.form.Radio({
                id: "manId" + id,
                name: "sex",
                inputValue: 1,
                boxLabel: '男'
            }), new Ext.form.Radio({
                id: "femaleId" + id,
                name: "sex",
                inputValue: 0,
                boxLabel: '女'
            }), new Ext.form.Radio({
                id: "allsex" + id,
                name: "sex",
                inputValue: null,
                boxLabel: '全部'
            })
        ]

    })

    var certificate = new Ext.form.CheckboxGroup({
        xtype: 'checkboxgroup',
        fieldLabel: '学历认证',
        itemCls: 'x-check-group-alt',
        // Put all controls in a single column with width 100%
        columns: 8,
        anchor: '90%',
        items: [
            {boxLabel: '专科', name: 'creditCode', inputValue: 1},
            {boxLabel: '本科', name: 'creditCode', inputValue: 2},
            {boxLabel: '研究生', name: 'creditCode', inputValue: 4},
            {boxLabel: '硕士', name: 'creditCode', inputValue: 8},
            {boxLabel: '博士', name: 'creditCode', inputValue: 16}
        ]
    });
    var studyStyle = new Ext.form.CheckboxGroup({
        xtype: 'checkboxgroup',
        fieldLabel: '学习形式',
        itemCls: 'x-check-group-alt',
        // Put all controls in a single column with width 100%
        columns: 8,
        anchor: '90%',
        columnWidth: 0.9,
        items: [
            {boxLabel: '普通', name: 'studyStyle', inputValue: 8192},
            {boxLabel: '函授', name: 'studyStyle', inputValue: 16384},
            {boxLabel: '网络教育', name: 'studyStyle', inputValue: 32768},
            {boxLabel: '自考', name: 'studyStyle', inputValue: 65536},
            {boxLabel: '成人', name: 'studyStyle', inputValue: 131072}
        ]
    });
    var graduateSchoolType = new Ext.form.CheckboxGroup({
        xtype: 'checkboxgroup',
        fieldLabel: '学校等级',
        itemCls: 'x-check-group-alt',
        // Put all controls in a single column with width 100%
        columns: 8,
        anchor: '90%',
        items: [
            {boxLabel: '985', name: 'graduateSchoolType', inputValue: 134217728},
            {boxLabel: '211', name: 'graduateSchoolType', inputValue: 268435456},
            {boxLabel: '一本', name: 'graduateSchoolType', inputValue: 536870912},
            {boxLabel: '二本', name: 'graduateSchoolType', inputValue: 1073741824},
            {boxLabel: '三本', name: 'graduateSchoolType', inputValue: 2147483648},
            {boxLabel: '职高', name: 'graduateSchoolType', inputValue: 4294967296}
        ]
    });
    var thirdAuthInfo = new Ext.form.CheckboxGroup({
        xtype: 'checkboxgroup',
        fieldLabel: '第三方认证',
        itemCls: 'x-check-group-alt',
        // Put all controls in a single column with width 100%
        columns: 8,
        anchor: '90%',
        items: [
            {boxLabel: '学历认证', name: 'thirdAuthInfo', inputValue: 262144},
            {boxLabel: '征信认证', name: 'thirdAuthInfo', inputValue: 524288},
            {boxLabel: '视频认证', name: 'thirdAuthInfo', inputValue: 1048576},
            {boxLabel: '手机认证', name: 'thirdAuthInfo', inputValue: 2097152},
            {boxLabel: '户籍认证', name: 'thirdAuthInfo', inputValue: 4194304},
            {boxLabel: '学籍认证', name: 'thirdAuthInfo', inputValue: 8388608}
        ]
    });

    var loanerSuccessCountBegin = new Ext.form.NumberField({
        fieldLabel: '成功借款次数',
        allowDecimals: false, // 是否小数
        minValue: 0,
        emptyText: '起始值',
        anchor: '90%'
    });
    var loanerSuccessCountEnd = new Ext.form.NumberField({
        fieldLabel: '成功借款次数',
        allowDecimals: false, // 是否小数
        minValue: 0,
        emptyText: '最大值',
        anchor: '90%'
    });
    var wasteCountBegin = new Ext.form.NumberField({
        fieldLabel: '流标次数',
        allowDecimals: false, // 是否小数
        minValue: 0,
        emptyText: '起始值',
        anchor: '90%'
    });
    var wasteCountEnd = new Ext.form.NumberField({
        fieldLabel: '流标次数',
        allowDecimals: false, // 是否小数
        minValue: 0,
        emptyText: '最大值',
        anchor: '90%'
    });
    var normalCountBegin = new Ext.form.NumberField({
        fieldLabel: '正常还款次数',
        allowDecimals: false, // 是否小数
        minValue: 0,
        emptyText: '起始值',
        anchor: '90%'
    });
    var normalCountEnd = new Ext.form.NumberField({
        fieldLabel: '正常还款次数',
        allowDecimals: false, // 是否小数
        minValue: 0,
        emptyText: '最大值',
        anchor: '90%'
    });
    var overdueLessCountBegin = new Ext.form.NumberField({
        fieldLabel: '逾期(1-15)还清次数',
        allowDecimals: false, // 是否小数
        minValue: 0,
        emptyText: '起始值',
        anchor: '90%'
    });
    var overdueLessCountEnd = new Ext.form.NumberField({
        fieldLabel: '逾期(1-15)还清次数',
        allowDecimals: false, // 是否小数
        minValue: 0,
        emptyText: '最大值',
        anchor: '90%'
    });
    var overdueMoreCountBegin = new Ext.form.NumberField({
        fieldLabel: '逾期(15天以上)还清次数',
        allowDecimals: false, // 是否小数
        minValue: 0,
        emptyText: '起始值',
        anchor: '90%'
    });
    var overdueMoreCountEnd = new Ext.form.NumberField({
        fieldLabel: '逾期(15天以上)还清次数',
        allowDecimals: false, // 是否小数
        minValue: 0,
        emptyText: '最大值',
        anchor: '90%'
    });

    var totalPrincipalBegin = new Ext.form.NumberField({
        fieldLabel: '累计借款金额',
        allowDecimals: false, // 是否小数
        minValue: 0,
        emptyText: '起始值',
        anchor: '90%'
    });
    var totalPrincipalEnd = new Ext.form.NumberField({
        fieldLabel: '累计借款金额',
        allowDecimals: false, // 是否小数
        minValue: 0,
        emptyText: '最大值',
        anchor: '90%'
    });
    var owingPrincipalBegin = new Ext.form.NumberField({
        fieldLabel: '待还金额',
        allowDecimals: false, // 是否小数
        emptyText: '起始值',
        minValue: 0,
        anchor: '90%'
    });
    var owingPrincipalEnd = new Ext.form.NumberField({
        fieldLabel: '待还金额',
        allowDecimals: false, // 是否小数
        minValue: 0,
        emptyText: '最大值',
        anchor: '90%'
    });
    var amountToReceiveBegin = new Ext.form.NumberField({
        fieldLabel: '待收金额',
        allowDecimals: false, // 是否小数
        minValue: 0,
        emptyText: '起始值',
        anchor: '90%'
    });
    var amountToReceiveEnd = new Ext.form.NumberField({
        fieldLabel: '待收金额',
        allowDecimals: false, // 是否小数
        minValue: 0,
        emptyText: '最大值',
        anchor: '90%'
    });
    var amountOwingTotalBegin = new Ext.form.NumberField({
        fieldLabel: '借款总额度',
        allowDecimals: false, // 是否小数
        minValue: 0,
        emptyText: '起始值',
        anchor: '90%'
    });
    var amountOwingTotalEnd = new Ext.form.NumberField({
        fieldLabel: '借款总额度',
        allowDecimals: false, // 是否小数
        minValue: 0,
        emptyText: '最大值',
        anchor: '90%'
    });
    var lastSuccessBorrowDaysBegin = new Ext.form.NumberField({
        fieldLabel: '距最后一次借款成功天数',
        allowDecimals: false, // 是否小数
        minValue: 0,
        emptyText: '起始值',
        anchor: '90%'
    });
    var lastSuccessBorrowDaysEnd = new Ext.form.NumberField({
        fieldLabel: '距最后一次借款成功天数',
        allowDecimals: false, // 是否小数
        minValue: 0,
        emptyText: '最大值',
        anchor: '90%'
    });
    /*var borrowFrequencyBegin = new Ext.form.NumberField({
     fieldLabel: '借款频率',
     allowDecimals: false, // 是否小数
     minValue: 0,
     emptyText: '起始值',
     anchor: '90%'
     });
     var borrowFrequencyEnd = new Ext.form.NumberField({
     fieldLabel: '借款频率',
     allowDecimals: false, // 是否小数
     minValue: 0,
     emptyText: '最大值',
     anchor: '90%'
     });*/
    var registerBorrowMonthsBegin = new Ext.form.NumberField({
        fieldLabel: '本次借款距注册时间月数',
        allowDecimals: false, // 是否小数
        minValue: 0,
        emptyText: '起始值',
        anchor: '90%'
    });
    var registerBorrowMonthsEnd = new Ext.form.NumberField({
        fieldLabel: '本次借款距注册时间月数',
        allowDecimals: false, // 是否小数
        minValue: 0,
        emptyText: '最大值',
        anchor: '90%'
    });
    var owingHighestDebtRatioBegin = new Ext.form.NumberField({
        fieldLabel: '待还金额/历史最高负债',
        allowDecimals: true, // 是否小数
        minValue: 0,
        emptyText: '起始值',
        anchor: '90%'
    });
    var amtDebtRatBg = new Ext.form.NumberField({
        fieldLabel: '本次借款/历史最高负债',
        allowDecimals: true, // 是否小数
        minValue: 0,
        emptyText: '起始值',
        anchor: '90%'
    });
    var amtDebtRatEd = new Ext.form.NumberField({
        fieldLabel: '本次借款/历史最高负债',
        allowDecimals: true, // 是否小数
        minValue: 0,
        emptyText: '最大值',
        anchor: '90%'
    });
    var owingHighestDebtRatioEnd = new Ext.form.NumberField({
        fieldLabel: '待还金额/历史最高负债',
        allowDecimals: true, // 是否小数
        minValue: 0,
        emptyText: '最大值',
        anchor: '90%'
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
        height: common.wheight * 0.65,
        autoScroll: true,
        layout: 'form',
        items: [{
            layout: 'column',
            border: false,
            bodyStyle: 'padding-left:5px;padding-top:5px;',
            defaults: {labelWidth: 100},
            tbar: ['借款基本信息', label],
            items: [{
                columnWidth: 0.2, // 该列在整行中所占百分比
                layout: 'form',
                border: false,
                items: [policyId]
            }, {
                columnWidth: 0.3,
                layout: 'form',
                border: false,
                items: [policyName]
            }, {
                columnWidth: 0.2,
                layout: 'form',
                border: false,
                items: [userId]
            }, {
                columnWidth: 0.2,
                layout: 'form',
                border: false,
                items: [policyType]
            }]
        }, {
            layout: 'column',
            border: false,
            bodyStyle: 'padding-left:5px;padding-top:5px;',
            defaults: {labelWidth: 100},
            tbar: ['借款基本信息', label],
            items: [{
                columnWidth: 0.2, // 该列在整行中所占百分比
                layout: 'form',
                border: false,
                items: [monthBegin, amountBegin]
            }, {
                columnWidth: 0.2,
                layout: 'form',
                border: false,
                items: [monthEnd, amountEnd]
            }, {
                columnWidth: 0.2,
                layout: 'form',
                border: false,
                items: [rateBegin]
            }, {
                columnWidth: 0.2,
                layout: 'form',
                border: false,
                items: [rateEnd]
            }]
        }, {
            layout: 'column',
            bodyStyle: 'padding-left: 5px;',
            border: false,
            defaults: {labelWidth: 100},
            items: [{
                columnWidth: .9,
                layout: 'form',
                border: false,
                items: [creditCode]
            }]
        }, {
            layout: 'column',
            border: false,
            bodyStyle: 'padding-left:5px;padding-top:5px;',
            defaults: {labelWidth: 100},
            tbar: ['借款人信息', label],
            items: [{
                columnWidth: 0.2, // 该列在整行中所占百分比
                layout: 'form',
                border: false,
                items: [ageBegin]
            }, {
                columnWidth: 0.2,
                layout: 'form',
                border: false,
                items: [ageEnd]
            }, {
                columnWidth: 0.3,
                layout: 'form',
                border: false,
                items: [sex]
            }]
        }, {
            layout: 'column',
            border: false,
            bodyStyle: 'padding-left:5px;padding-top:5px;',
            defaults: {labelWidth: 100},
            items: [{
                columnWidth: .9, // 该列在整行中所占百分比
                layout: 'form',
                border: false,
                items: [certificate, studyStyle, graduateSchoolType, thirdAuthInfo]
            }]
        }, {
            layout: 'column',
            border: false,
            bodyStyle: 'padding-left:5px;padding-top:5px;',
            defaults: {labelWidth: 100},
            tbar: ['信用记录', label],
            items: [{
                columnWidth: 0.25, // 该列在整行中所占百分比
                layout: 'form',
                border: false,
                items: [loanerSuccessCountBegin, normalCountBegin, overdueMoreCountBegin]
            }, {
                columnWidth: 0.25,
                layout: 'form',
                border: false,
                items: [loanerSuccessCountEnd, normalCountEnd, overdueMoreCountEnd]
            }, {
                columnWidth: 0.25,
                layout: 'form',
                border: false,
                items: [wasteCountBegin, overdueLessCountBegin]
            }, {
                columnWidth: 0.25,
                layout: 'form',
                border: false,
                items: [wasteCountEnd, overdueLessCountEnd]
            }]
        }, {
            layout: 'column',
            border: false,
            bodyStyle: 'padding-left:5px;padding-top:5px;',
            defaults: {labelWidth: 100},
            tbar: ['借款状态', label],
            items: [{
                columnWidth: 0.25, // 该列在整行中所占百分比
                layout: 'form',
                border: false,
                items: [totalPrincipalBegin, amountToReceiveBegin, lastSuccessBorrowDaysBegin]
            }, {
                columnWidth: 0.25,
                layout: 'form',
                border: false,
                items: [totalPrincipalEnd, amountToReceiveEnd, lastSuccessBorrowDaysEnd]
            }, {
                columnWidth: 0.25,
                layout: 'form',
                border: false,
                items: [owingPrincipalBegin, amountOwingTotalBegin, registerBorrowMonthsBegin]
            }, {
                columnWidth: 0.25,
                layout: 'form',
                border: false,
                items: [owingPrincipalEnd, amountOwingTotalEnd, registerBorrowMonthsEnd]
            }]
        }, {
            layout: 'column',
            border: false,
            bodyStyle: 'padding-left:5px;padding-top:5px;',
            defaults: {labelWidth: 160},
            tbar: ['高级指标设置', label],
            items: [{
                columnWidth: 0.35, // 该列在整行中所占百分比
                layout: 'form',
                border: false,
                items: [owingHighestDebtRatioBegin, amtDebtRatBg]
            }, {
                columnWidth: 0.35,
                layout: 'form',
                border: false,
                items: [owingHighestDebtRatioEnd,amtDebtRatEd]
            }]
        }],

        load: function (o) {
            if (o == null) {
                return;
            }
            policyType.setValue(o['policyType']);
            userId.setValue(o['userId']);
            policyId.setValue(o['id']);
            policyName.setValue(o['name']);
            monthBegin.setValue(o['monthBegin']);
            monthEnd.setValue(o['monthEnd']);
            rateBegin.setValue(o['rateBegin']);
            rateEnd.setValue(o['rateEnd']);
            amountBegin.setValue(o['amountBegin']);
            amountEnd.setValue(o['amountEnd']);
            var collection = creditCode.items;
            for (var i = 0; i < collection.items.length; i++) {
                //alert(collection.items[i].checked);
                var inputValue = collection.items[i].inputValue;
                if (parseInt(o['creditCode'] & inputValue) == parseInt(inputValue)) {
                    collection.items[i].setValue(true);
                }
            }
            ageBegin.setValue(o['ageBegin']);
            ageEnd.setValue(o['ageEnd']);

            var sexCode = o['sex'];
            if (sexCode == null) {
                Ext.getCmp("allsex" + id).setValue(true);
            } else {
                if (sexCode == 1) {
                    Ext.getCmp("manId" + id).setValue(true);
                } else {
                    Ext.getCmp("femaleId" + id).setValue(true);
                }
            }

            var certificateCollection = certificate.items;
            for (var i = 0; i < certificateCollection.items.length; i++) {
                //alert(collection.items[i].checked);
                var inputValue = certificateCollection.items[i].inputValue;
                if (parseInt(o['certificate'] & inputValue) == parseInt(inputValue)) {
                    certificateCollection.items[i].setValue(true);
                }
            }

            var studyStyleCollection = studyStyle.items;
            for (var i = 0; i < studyStyleCollection.items.length; i++) {
                var inputValue = studyStyleCollection.items[i].inputValue;
                if (parseInt(o['studyStyle'] & inputValue) == parseInt(inputValue)) {
                    studyStyleCollection.items[i].setValue(true);
                }
            }

            var graduateSchoolTypeCollection = graduateSchoolType.items;
            if (o['graduateSchoolTypeFlagList'] != null) {
                for (var j = 0; j < o['graduateSchoolTypeFlagList'].length; j++) {
                    if (o['graduateSchoolTypeFlagList'][j] == true) {
                        graduateSchoolTypeCollection.items[j].setValue(true);
                    }
                }
            }


            var thirdAuthInfoCollection = thirdAuthInfo.items;
            for (var i = 0; i < thirdAuthInfoCollection.items.length; i++) {
                var inputValue = thirdAuthInfoCollection.items[i].inputValue;
                if (parseInt(o['thirdAuthInfo'] & inputValue) == parseInt(inputValue)) {
                    thirdAuthInfoCollection.items[i].setValue(true);
                }
            }

            loanerSuccessCountBegin.setValue(o['loanerSuccessCountBegin']);
            loanerSuccessCountEnd.setValue(o['loanerSuccessCountEnd']);
            wasteCountBegin.setValue(o['wasteCountBegin']);
            wasteCountEnd.setValue(o['wasteCountEnd']);
            normalCountBegin.setValue(o['normalCountBegin']);
            normalCountEnd.setValue(o['normalCountEnd']);
            overdueLessCountBegin.setValue(o['overdueLessCountBegin']);
            overdueLessCountEnd.setValue(o['overdueLessCountEnd']);
            overdueMoreCountBegin.setValue(o['overdueMoreCountBegin']);
            overdueMoreCountEnd.setValue(o['overdueMoreCountEnd']);

            totalPrincipalBegin.setValue(o['totalPrincipalBegin']);
            totalPrincipalEnd.setValue(o['totalPrincipalEnd']);
            owingPrincipalBegin.setValue(o['owingPrincipalBegin']);
            owingPrincipalEnd.setValue(o['owingPrincipalEnd']);
            amountToReceiveBegin.setValue(o['amountToReceiveBegin']);
            amountToReceiveEnd.setValue(o['amountToReceiveEnd']);
            amountOwingTotalBegin.setValue(o['amountOwingTotalBegin']);
            amountOwingTotalEnd.setValue(o['amountOwingTotalEnd']);
            lastSuccessBorrowDaysBegin.setValue(o['lastSuccessBorrowDaysBegin']);
            lastSuccessBorrowDaysEnd.setValue(o['lastSuccessBorrowDaysEnd']);
            //borrowFrequencyBegin.setValue(o['borrowFrequencyBegin']);
            //borrowFrequencyEnd.setValue(o['borrowFrequencyEnd']);
            registerBorrowMonthsBegin.setValue(o['registerBorrowMonthsBegin']);
            registerBorrowMonthsEnd.setValue(o['registerBorrowMonthsEnd']);

            owingHighestDebtRatioBegin.setValue(o['owingHighestDebtRatioBegin']);
            owingHighestDebtRatioEnd.setValue(o['owingHighestDebtRatioEnd']);
            amtDebtRatBg.setValue(o['amtDebtRatBg']);
            amtDebtRatEd.setValue(o['amtDebtRatEd']);

        },
        save: function () {
            if (!policyName.isValid() || !monthBegin.isValid() || !monthEnd.isValid() || !rateBegin.isValid() || !rateEnd.isValid()) {
                label.setText('请正确填写输入项');
                return;
            }
            label.setText('');

            var obj = new Object();
            obj.id = policyId.getValue();
            obj.policyType = policyType.getValue();
            obj.userId = userId.getValue();
            obj.userPolicyName = policyName.getValue();
            obj.monthBegin = monthBegin.getValue();
            obj.monthEnd = monthEnd.getValue();
            obj.rateBegin = rateBegin.getValue();
            obj.rateEnd = rateEnd.getValue();
            obj.amountBegin = amountBegin.getValue();
            obj.amountEnd = amountEnd.getValue();
            obj.ageBegin = ageBegin.getValue();
            obj.ageEnd = ageEnd.getValue();

            var sexCollection = sex.items;
            for (var i = 0; i < sexCollection.items.length; i++) {
                var checked = sexCollection.items[i].checked;
                if (checked) {
                    var inputValue = sexCollection.items[i].inputValue;
                    if (inputValue != null) {
                        obj.sex = parseInt(inputValue);
                    }
                }
            }

            var creditCodeArray = new Array;
            var creditCodeCollection = creditCode.items;
            for (var i = 0; i < creditCodeCollection.items.length; i++) {
                if (creditCodeCollection.items[i].checked) {
                    var inputValue = creditCodeCollection.items[i].inputValue;
                    creditCodeArray.push(inputValue);
                }
            }
            obj.creditCode = creditCodeArray;

            var certificateArray = new Array;
            var certificateCollection = certificate.items;
            for (var i = 0; i < certificateCollection.items.length; i++) {
                if (certificateCollection.items[i].checked) {
                    var inputValue = certificateCollection.items[i].inputValue;
                    certificateArray.push(inputValue);
                }
            }
            obj.certificate = certificateArray;

            var studyStyleArray = new Array;
            var studyStyleCollection = studyStyle.items;
            for (var i = 0; i < studyStyleCollection.items.length; i++) {
                if (studyStyleCollection.items[i].checked) {
                    var inputValue = studyStyleCollection.items[i].inputValue;
                    studyStyleArray.push(inputValue);
                }
            }
            obj.studyStyle = studyStyleArray;

            var graduateSchoolTypeArray = new Array;
            var graduateSchoolTypeCollection = graduateSchoolType.items;
            for (var i = 0; i < graduateSchoolTypeCollection.items.length; i++) {
                if (graduateSchoolTypeCollection.items[i].checked) {
                    var inputValue = graduateSchoolTypeCollection.items[i].inputValue;
                    graduateSchoolTypeArray.push(inputValue);
                }
            }
            obj.graduateSchoolType = graduateSchoolTypeArray;

            var thirdAuthInfoArray = new Array;
            var thirdAuthInfoCollection = thirdAuthInfo.items;
            for (var i = 0; i < thirdAuthInfoCollection.items.length; i++) {
                if (thirdAuthInfoCollection.items[i].checked) {
                    var inputValue = thirdAuthInfoCollection.items[i].inputValue;
                    thirdAuthInfoArray.push(inputValue);
                }
            }
            obj.thirdAuthInfo = thirdAuthInfoArray;

            obj.loanerSuccessCountBegin = loanerSuccessCountBegin.getValue();
            obj.loanerSuccessCountEnd = loanerSuccessCountEnd.getValue();
            obj.wasteCountBegin = wasteCountBegin.getValue();
            obj.wasteCountEnd = wasteCountEnd.getValue();
            obj.normalCountBegin = normalCountBegin.getValue();
            obj.normalCountEnd = normalCountEnd.getValue();
            obj.overdueLessCountBegin = overdueLessCountBegin.getValue();
            obj.overdueLessCountEnd = overdueLessCountEnd.getValue();
            obj.overdueMoreCountBegin = overdueMoreCountBegin.getValue();
            obj.overdueMoreCountEnd = overdueMoreCountEnd.getValue();
            obj.totalPrincipalBegin = totalPrincipalBegin.getValue();
            obj.totalPrincipalEnd = totalPrincipalEnd.getValue();
            obj.owingPrincipalBegin = owingPrincipalBegin.getValue();
            obj.owingPrincipalEnd = owingPrincipalEnd.getValue();
            obj.amountToReceiveBegin = amountToReceiveBegin.getValue();
            obj.amountToReceiveEnd = amountToReceiveEnd.getValue();
            obj.amountOwingTotalBegin = amountOwingTotalBegin.getValue();
            obj.amountOwingTotalEnd = amountOwingTotalEnd.getValue();
            obj.lastSuccessBorrowDaysBegin = lastSuccessBorrowDaysBegin.getValue();
            obj.lastSuccessBorrowDaysEnd = lastSuccessBorrowDaysEnd.getValue();
            obj.registerBorrowMonthsBegin = registerBorrowMonthsBegin.getValue();
            obj.registerBorrowMonthsEnd = registerBorrowMonthsEnd.getValue();
            obj.owingHighestDebtRatioBegin = owingHighestDebtRatioBegin.getValue();
            obj.owingHighestDebtRatioEnd = owingHighestDebtRatioEnd.getValue();
            obj.amtDebtRatBg = amtDebtRatBg.getValue();
            obj.amtDebtRatEd = amtDebtRatEd.getValue();

            dialog.body.mask('正在保存修改，请稍等...', 'x-mask-loading');
            loanPolicyService.saveLoanPolicy(obj, function (o) {
                dialog.body.unmask();
                dialog.parentConfig.ds.reload({
                    params: {
                        start: dialog.parentConfig.pageStart,
                        limit: common.pageSize,
                        sort: common.defaultSort,
                        dir: common.defaultDir
                    }
                });
                dialog.hide();
                config.dialog.closeAction = 'refresh';
            });

            /*obj.rateBegin = rateBegin.getValue();

             obj.remark = remark.getValue();
             obj.remarkAdmin = remarkadmin.getValue();
             //	    		obj.longOrder = islongorder.getValue() ? 1 : 0;
             obj.autoDispatch = isautodispatch.getValue() ? 1 : 0;
             obj.refundOrder = isbackorder.getValue() ? 1 : 0;
             obj.visibility = visibilityComb.getValue();
             var client = new Object();
             client.name = clientname.getValue();
             client.email = clientemail.getValue();
             obj.client = client;

             var checkbill = new Object();
             checkbill.num = ordernum.getValue();
             checkbill.id = checkbillid.getValue();
             checkbill.clientFirstpayCode = clientfirstpaycode.getValue();
             checkbill.clientFirstpay = clientfirstpay.getValue();
             checkbill.clientOverpayCode = clientoverpaycode.getValue();
             checkbill.clientOverpay = clientoverpay.getValue();
             // checkbill.writerRewardpay = reward.getValue();
             // checkbill.writerOverpay = percent70pay.getValue();
             checkbill.order = obj;
             obj.cb = checkbill;


             orderService.updateOrder(obj, function (o) {
             //	    	    			checkbillid.setValue(o['cb']['id']);
             //	    	    			writerRewardpay.setValue(o['cb']['writerRewardpay']);
             dialog.body.unmask();
             dialog.parentConfig.ds.reload({
             params: {
             start: dialog.parentConfig.pageStart,
             limit: common.pageSize,
             sort: common.defaultSort,
             dir: common.defaultDir
             }
             });
             dialog.hide();
             //	    	    			config.dialog.closeAction = 'refresh';
             });*/
        }
    };
    return resultpanel;
};
/** end getLoanPolicy function*/
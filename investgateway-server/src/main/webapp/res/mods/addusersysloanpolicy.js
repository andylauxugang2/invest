'use strict'; //ECMAScript5 严格模式,JSON

layui.config({
    base: "res/mods/"
}).use(['form', 'layer', 'jquery', 'element'], function () {
    var element = layui.element;
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        laypage = layui.laypage,
        $ = layui.jquery;
    var table = layui.table;

    //全局的ajax访问，处理ajax清求时session超时
    $.ajaxSetup({
        contentType:"application/x-www-form-urlencoded;charset=utf-8",
        complete:function(XMLHttpRequest,textStatus){
            var httpStatus = XMLHttpRequest.status;
            if(httpStatus == xmlhttprequest_status_session_timeout){
                parent.parent.location.href = "/login";
            }
            //alert(XMLHttpRequest.readyState);
        }
    });

    var userId = $('#userIdData').val();
    var thirdUserUUID = $('#thirdUserUUID').val();

    if (!userId && !thirdUserUUID) {
        layer.msg("页面基础参数获取失败,请重新登录或联系客服解决");
        return;
    }

    var index = layer.msg('页面加载中，请稍候...', {icon: 16, time: false, shade: 0.8});

    //设置下拉框 风险等级
    $.ajax({
        type: "get",
        url: "/policy/getLoanRiskLevel",
        async: false,
        success: function (data) {
            if (data.code == 0) {
                var htmlData = '';
                for (var i = 0; i < data.data.length; i++) {
                    htmlData += '<option value="' + data.data[i].code + '">' + data.data[i].desc + '</option>'
                }
                $("#riskLevelSelect").append(htmlData);
                form.render("select");
            } else {
                layer.msg(data.msg);
            }
        },
        error: function (e) {
            layer.msg("未知异常:风险等级获取失败!");
        }
    });

    function searchPolicy(thirdUserUUID, riskLevel, index) {
        var param = {userId: userId};
        if (thirdUserUUID) {
            param.thirdUserUUID = thirdUserUUID;
        }
        if (riskLevel) {
            param.riskLevel = riskLevel;
        }
        param.policyType = policy_type_sysloan;

        table.render({
            id: 'sysLaonPolicyTable',
            elem: '#sysLaonPolicyTable', //指定原始表格元素选择器（推荐id选择器）
            url: '/policy/getLoanPolicies',
            where: param,
            method: 'get',
            height: "auto",
            page: false,
            loading: true,
            cols: [[
                {checkbox: true, LAY_CHECKED: false},
                {
                    field: 'id',
                    title: '策略编号',
                    width: 70,
                    sort: true
                },
                {
                    field: 'name',
                    title: '策略名称',
                    width: 130,
                    sort: true
                },
                {
                    field: 'riskLevel',
                    title: '风险等级',
                    width: 90,
                    sort: true
                },
                {
                    field: 'amount',
                    title: '借款金额',
                    width: 100,
                    sort: true
                },
                {
                    field: 'month',
                    title: '期限',
                    width: 80,
                    sort: true
                },
                {
                    field: 'rate',
                    title: '利率',
                    width: 80,
                    sort: true
                },
                {
                    field: 'creditCode',
                    title: '魔镜等级',
                    width: 120,
                    sort: true
                },
                {
                    field: 'thirdAuthInfo',
                    title: '第三方认证',
                    width: 120,
                    sort: true
                },
                {
                    field: 'age',
                    title: '年龄',
                    width: 80,
                    sort: true
                },
                {
                    field: 'sex',
                    title: '性别',
                    width: 80,
                    sort: true
                },
                {
                    field: 'certificate',
                    title: '学历认证',
                    width: 120,
                    sort: true
                },
                {
                    field: 'studyStyle',
                    title: '学习形式',
                    width: 120,
                    sort: true
                },
                {
                    field: 'graduateSchoolType',
                    title: '学校分类',
                    width: 100,
                    sort: true
                },
                {
                    field: 'loanerSuccessCount',
                    title: '成功借款次数',
                    width: 80,
                    sort: true
                },
                {
                    field: 'wasteCount',
                    title: '流标次数',
                    width: 80,
                    sort: true
                },
                {
                    field: 'normalCount',
                    title: '正常还款次数',
                    width: 80,
                    sort: true
                },
                {
                    field: 'overdueLessCount',
                    title: '逾期(1-15)还清次数',
                    width: 80,
                    sort: true
                },
                {
                    field: 'overdueMoreCount',
                    title: '逾期(15天以上)还清次数',
                    width: 80,
                    sort: true
                }, {
                    field: 'totalPrincipal',
                    title: '累计借款金额',
                    width: 100,
                    sort: true
                }, {
                    field: 'owingPrincipal',
                    title: '待还金额',
                    width: 100,
                    sort: true
                },
                {
                    field: 'amountToReceive',
                    title: '待收金额',
                    width: 100,
                    sort: true
                },
                {
                    field: 'lastSuccessBorrowDays',
                    title: '距最后一次借款成功天数',
                    width: 80,
                    sort: true
                },
                {
                    field: 'registerBorrowMonths',
                    title: '本次借款距注册时间月数',
                    width: 80,
                    sort: true
                },
                {
                    field: 'owingHighestDebtRatio',
                    title: '待还金额/历史最高负债',
                    width: 80,
                    sort: true
                },
                {
                    field: 'amtDebtRat',
                    title: '本次借款/历史最高负债',
                    width: 80,
                    sort: true
                },
                {
                    field: 'validTime',
                    title: '过期时间',
                    width: 120,
                    sort: true
                },
                {
                    field: 'createTime',
                    title: '创建时间',
                    width: 120,
                    sort: true
                }
            ]], //设置表头
            done: function (res, curr, count) {
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                //得到当前页码
                //得到数据总量

                if(index){
                    setTimeout(function () {
                        layer.close(index);
                    }, 500);
                }
            }
        });
    }


    //加载页面数据
    searchPolicy(thirdUserUUID, null, index);

    setTimeout(function () {
        layer.close(index);
    }, 1000);

    //查询
    $("#searchMyPolicy-btn").click(function () {
        var index = layer.msg('查询中，请稍候...', {icon: 16, time: false, shade: 0.8});
        var riskLevel = $("#riskLevelSelect").find("option:selected").attr("value");
        searchPolicy(thirdUserUUID, riskLevel, index);
    });

    //批量添加
    $("#batchAddSysPolicy-btn").click(function () {
        var checkStatus = table.checkStatus('sysLaonPolicyTable'); //参数id设定的值
        var riskLevel = $("#riskLevelSelect").find("option:selected").attr("value");

        if (checkStatus.data.length > 0) {
            layer.confirm('确定添加选中的策略？', {icon: 3, title: '提示信息'}, function (index) {
                var index = layer.msg('添加中，请稍候', {icon: 16, time: false, shade: 0.8});
                //添加数据
                var dataIdArray = new Array();
                for (var j = 0; j < checkStatus.data.length; j++) {
                    var dataId = checkStatus.data[j].id;
                    dataIdArray.push(dataId);
                }
                if (dataIdArray.length == 0) {
                    layer.msg("请选择要添加的策略");
                    return;
                }

                $.ajax({
                    type: "post",
                    url: "/policy/addBatchUserLoanPolicy",
                    data: JSON.stringify({'dataIds': dataIdArray, 'userId': userId, 'thirdUserUUID': thirdUserUUID}),
                    dataType: "json",
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    success: function (data) {
                        if (data.code == 0) {
                            layer.msg("添加成功,请到回退到散标策略页面里查看");
                            layer.close(index);
                            searchPolicy(thirdUserUUID, riskLevel, null);
                        } else {
                            setTimeout(function () {
                                layer.close(index);
                                layer.msg(data.msg);
                            }, 500);
                        }
                    },
                    error: function (e) {
                        setTimeout(function () {
                            layer.close(index);
                            layer.msg("未知异常!");
                        }, 1000);
                    }
                });
            })
        } else {
            layer.msg("请选择要添加的策略");
        }
    });

})

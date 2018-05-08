'use strict'; //ECMAScript5 严格模式,JSON

layui.config({
    base: "res/mods/record/"
}).use(['form', 'layer', 'jquery', 'element', 'laytpl', 'table', 'laydate'], function () {
    var element = layui.element;
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery;
    var laydate = layui.laydate;
    var table = layui.table;

    //全局的ajax访问，处理ajax清求时session超时
    $.ajaxSetup({
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        complete: function (XMLHttpRequest, textStatus) {
            var httpStatus = XMLHttpRequest.status;
            if (httpStatus == xmlhttprequest_status_session_timeout) {
                parent.location.href = "/login";
            }
            //alert(XMLHttpRequest.readyState);
        }
    });

    var userId = $('#userIdData').val();

    var index = layer.msg('页面加载中，请稍候...', {icon: 16, time: false, shade: 0.8});

    //执行一个laydate实例
    laydate.render({
        elem: '#bidLoanBeginTime', //指定元素
        type: 'datetime'
    });
    laydate.render({
        elem: '#bidLoanEndTime', //指定元素
        type: 'datetime'
    });

    //设置下拉框 第三方账号数据
    $.ajax({
        type: "get",
        url: "/policy/getPolicyType",
        async: false,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        success: function (data) {
            if (data.code == 0) {
                var htmlData = '';
                for (var i = 0; i < data.data.length; i++) {
                    htmlData += '<option value="' + data.data[i].code + '">' + data.data[i].desc + '</option>'
                }
                $("#policyTypeSelect").append(htmlData);
                form.render("select");
            } else {
                layer.msg(data.msg);
            }
        },
        error: function (e) {
            layer.msg("未知异常!");
        }
    });

    //设置下拉框 策略类型
    $.ajax({
        type: "get",
        url: "/userauth/thirdAuthInfo?userId=" + userId,
        async: false,
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        success: function (data) {
            if (data.code == 0) {
                var htmlData = '';
                for (var i = 0; i < data.data.length; i++) {
                    htmlData += '<option value="' + data.data[i].thirdUserUUID + '">' + data.data[i].thirdUserUUID + '</option>'
                }
                $("#thirdUserUUIDSelect").append(htmlData);
                form.render("select");
            } else {
                layer.msg(data.msg);
            }
        },
        error: function (e) {
            layer.msg("未知异常!");
        }
    });

    function searchInvestRecord(thirdUserUUID, policyType, bidLoanBeginTime, bidLoanEndTime, index) {
        var param = {userId: userId};
        if (thirdUserUUID) {
            param.thirdUserUUID = thirdUserUUID;
        }
        if (policyType) {
            param.policyType = policyType;
        }
        if (bidLoanBeginTime) {
            param.bidLoanBeginTime = bidLoanBeginTime;
        }
        if (bidLoanEndTime) {
            param.bidLoanEndTime = bidLoanEndTime;
        }

        table.render({
            id: 'investRecordTable',
            elem: '#investRecordTable', //指定原始表格元素选择器（推荐id选择器）
            url: '/invest/getInvestRecord',
            where: param,
            method: 'get',
            height: 400,
            page: true,
            loading: true,
            cols: [[
                /*{
                    field: 'id',
                    title: '编号',
                    width: 70,
                    sort: true
                },*/
                {
                    title: '标的ID',
                    width: 120,
                    templet: '#loanListingDetailLinkTpl',
                    sort: true
                },
                {
                    field: 'username',
                    title: '第三方账号',
                    width: 150,
                    sort: true
                },
                {
                    field: 'policyType',
                    title: '策略类型',
                    width: 100,
                    sort: true
                },
                {
                    field: 'policyId',
                    title: '策略编号',
                    width: 100,
                    sort: true
                },
                {
                    field: 'name',
                    title: '策略名称',
                    width: 150,
                    sort: true
                },
                {
                    field: 'amount',
                    title: '投资金额',
                    width: 100,
                    sort: true
                },
                {
                    field: 'couponStatus',
                    title: '使用优惠券',
                    width: 100,
                    sort: true
                },
                {
                    field: 'createTime',
                    title: '投资时间',
                    width: 180,
                    sort: true
                },
                {
                    title: '操作',
                    width: 150,
                    templet: '#optTpl'
                }
            ]], //设置表头
            done: function (res, curr, count) {

                setTimeout(function () {
                    layer.close(index);
                }, 500);
            }
        });
    }


    searchInvestRecord(null, null, null, null, index);

    //查询
    $("#searchInvestRecord-btn").click(function () {
        var index = layer.msg('查询中，请稍候...', {icon: 16, time: false, shade: 0.8});
        var bidLoanBeginTime = $("#bidLoanBeginTime").val();
        var bidLoanEndTime = $("#bidLoanEndTime").val();
        searchInvestRecord($("#thirdUserUUIDSelect").val(), $("#policyTypeSelect").val(), bidLoanBeginTime, bidLoanEndTime, index);
    });

    //投资详情
    $("body").on("click", ".search-invest-detail", function () {
        var _this = $(this);
        var index_ = layer.msg('查询中，请稍候', {icon: 16, time: false, shade: 0.8});
        var loanId = _this.parents("tr").find(".loanId").attr("data-id");

        var listingIds = new Array;
        listingIds.push(loanId);
        $.ajax({
            type: "post",
            url: "/ppd/loan/batchDetailList",
            data: JSON.stringify({
                'userId': userId,
                'userName': userId,
                'listingIds': listingIds
            }),
            dataType: "json",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            success: function (data) {
                if (data.code == 0) {
                    layer.close(index_);
                    var index = layui.layer.open({
                        title: "标的详情",
                        type: 1, //0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
                        content: getDetailHtml(data.data)
                    });
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
                    layer.msg("未知异常,请检查网络是否断开!");
                }, 1000);
            }
        });
    });

    function getDetailHtml(data) {
        var result = '<div width="500px">';
        for (var i = 0; i < data.length; i++) {
            result += "标的ID:" + data[i]['ListingId'] + '</br>';
            result += "借款金额:" + data[i]['Amount'] + '</br>';
            result += "期限:" + data[i]['Months'] + '</br>';
            result += "利率:" + data[i]['CurrentRate'] + '</br>';
            result += "借款人的用户名:" + data[i]['BorrowName'] + '</br>';
            result += "性别:" + data[i]['Gender'] + '</br>';
            result += "借款人年龄:" + data[i]['Age'] + '</br>';
            result += "学历:" + data[i]['EducationDegree'] + '</br>';
            result += "毕业学校:" + data[i]['GraduateSchool'] + '</br>';
            result += "学习形式:" + data[i]['StudyStyle'] + '</br>';
            result += "成功借款次数:" + data[i]['SuccessCount'] + '</br>';
            result += "首次投资时间:" + data[i]['FistBidTime'] + '</br>';
            result += "末笔投资时间:" + data[i]['LastBidTime'] + '</br>';
            result += "投标人数:" + data[i]['LenderCount'] + '</br>';
            result += "成交日期:" + data[i]['AuditingTime'] + '</br>';
            result += "剩余可投金额:" + data[i]['RemainFunding'] + '</br>';
            result += "截止时间:" + data[i]['DeadLineTimeOrRemindTimeStr'] + '</br>';
            result += "流标次数:" + data[i]['WasteCount'] + '</br>';
            result += "撤标次数:" + data[i]['CancelCount'] + '</br>';
            result += "失败次数:" + data[i]['FailedCount'] + '</br>';
            result += "正常还清次数:" + data[i]['NormalCount'] + '</br>';
            result += "逾期(1-15)还清次数:" + data[i]['OverdueLessCount'] + '</br>';
            result += "逾期(15天以上)还清次数:" + data[i]['OverdueMoreCount'] + '</br>';
            result += "剩余待还本金:" + data[i]['OwingPrincipal'] + '</br>';
            result += "待还金额:" + data[i]['OwingAmount'] + '</br>';
            result += "待收金额:" + data[i]['AmountToReceive'] + '</br>';
            result += "第一次成功借款时间:" + data[i]['FirstSuccessBorrowTime'] + '</br>';
            result += "最后一次成功借款时间:" + data[i]['LastSuccessBorrowTime'] + '</br>';
            result += "学历认证:" + data[i]['CertificateValidate'] + '</br>';
            result += "户籍认证:" + data[i]['NciicIdentityCheck'] + '</br>';
            result += "手机认证:" + data[i]['PhoneValidate'] + '</br>';
            result += "视频认证:" + data[i]['VideoValidate'] + '</br>';
            result += "征信认证:" + data[i]['CreditValidate'] + '</br>';
            result += "学籍认证:" + data[i]['EducateValidate'] + '</br>';
            result += "单笔最高借款金额:" + data[i]['HighestPrincipal'] + '</br>';
            result += "历史最高负债:" + data[i]['HighestDebt'] + '</br>';
            result += "累计借款金额:" + data[i]['TotalPrincipal'] + '</br>';
        }
        result += "</div>";
        return result;
    }
})

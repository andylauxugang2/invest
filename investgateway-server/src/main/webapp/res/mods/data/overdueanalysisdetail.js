'use strict'; //ECMAScript5 严格模式,JSON

layui.config({
    base: "res/mods/"
}).use(['form', 'layer', 'jquery', 'element'], function () {
    var element = layui.element;
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery;
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
    var analysisId = $('#analysisIdData').val();
    var overdueType = $('#overdueTypeData').val();

    var index = layer.msg('页面加载中，请稍候...', {icon: 16, time: false, shade: 0.8});

    //加载第三方用户散标策略列表
    function searchOverdueAnalysisDetailTable(analysisId, overdueType, index) {
        var param = {userId: userId};
        param.analysisId = analysisId;
        param.overdueType = overdueType;

        table.render({
            id: 'overdueAnalysisDetailTable',
            elem: '#overdueAnalysisDetailTable',
            url: '/data/getOverdueAnalysisDetail',
            where: param,
            method: 'get',
            height: "auto",
            page: false,
            loading: true,
            cols: [[
                {
                    field: 'username',
                    title: '第三方账号',
                    width: 120,
                    sort: true
                },
                {
                    field: 'listingId',
                    title: '标的ID',
                    width: 90,
                    sort: true
                },
                {
                    field: 'month',
                    title: '借款期限',
                    width: 90,
                    sort: true
                },
                {
                    field: 'creditCode',
                    title: '魔镜等级',
                    width: 90,
                    sort: true
                },
                {
                    field: 'rate',
                    title: '借款利率',
                    width: 100,
                    sort: true
                },
                {
                    field: 'amount',
                    title: '借款金额',
                    width: 100,
                    sort: true
                },
                {
                    field: 'bidAmount',
                    title: '投标金额',
                    width: 90,
                    sort: true
                },
                {
                    field: 'policyId',
                    title: '策略编号',
                    width: 100,
                    sort: true
                },
                {
                    field: 'policyName',
                    title: '策略名称',
                    width: 100,
                    sort: true
                },
                {
                    field: 'bidTime',
                    title: '投标时间',
                    width: 100,
                    sort: true
                },
                {
                    field: 'overdueDays',
                    title: '逾期天数',
                    width: 100,
                    sort: true
                },
                {
                    field: 'repay',
                    title: '已收本金+利息',
                    width: 120,
                    sort: true
                },
                {
                    field: 'owing',
                    title: '未收本金+利息',
                    width: 120,
                    sort: true
                },
                {
                    field: 'owingOverdue',
                    title: '逾期利息',
                    width: 100,
                    sort: true
                }
            ]], //设置表头
            done: function (res, curr, count) {
                if (index) {
                    setTimeout(function () {
                        layer.close(index);
                    }, 500);
                }
            }
        });
    }

    searchOverdueAnalysisDetailTable(analysisId, overdueType, index);
})

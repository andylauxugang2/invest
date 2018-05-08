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

    var index = layer.msg('页面加载中，请稍候...', {icon: 16, time: false, shade: 0.8});

    //设置下拉框 第三方账号数据
    $.ajax({
        type: "get",
        url: "/userauth/thirdAuthInfo?userId=" + userId,
        async: false,
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

    //加载第三方用户散标策略列表
    function searchOverdueAnalysisTable(thirdUserUUID, index) {
        var param = {userId: userId};
        if (thirdUserUUID) {
            param.username = thirdUserUUID;
        }

        table.render({
            id: 'overdueAnalysisTable',
            elem: '#overdueAnalysisTable',
            url: '/data/getOverdueAnalysis',
            where: param,
            method: 'get',
            height: "auto",
            page: false,
            loading: true,
            cols: [[
                {
                    field: 'month',
                    title: '投标月份',
                    width: 80,
                    sort: true
                },
                {
                    field: 'username',
                    title: '第三方账号',
                    width: 120,
                    sort: true
                },
                {
                    field: 'bidAmountTotal',
                    title: '总金额',
                    width: 100,
                    sort: true
                },
                {
                    field: 'bidCountTotal',
                    title: '总笔数',
                    width: 80,
                    sort: true
                },
                {
                    field: 'bidRateAvg',
                    title: '投标平均利率',
                    width: 100,
                    sort: true
                },
                {
                    field: 'bidMonthAvg',
                    title: '借款平均月数',
                    width: 100,
                    sort: true
                },
                /*{
                 field: 'bidAgeAvg',
                 title: '借款人平均年龄',
                 width: 130,
                 sort: true
                 },*/
                /*{
                 field: 'bidLenderCountAvg',
                 title: '投资人平均个数',
                 width: 130,
                 sort: true
                 },*/
                {
                    templet: '#overdue10DetailLinkTpl',
                    title: '10天逾期量',
                    width: 120,
                    sort: true
                },
                {
                    templet: '#overdue30DetailLinkTpl',
                    title: '30天逾期量',
                    width: 120,
                    sort: true
                },
                {
                    templet: '#overdue60DetailLinkTpl',
                    title: '60天逾期量',
                    width: 120,
                    sort: true
                },
                {
                    templet: '#overdue90DetailLinkTpl',
                    title: '90以上天逾期量',
                    width: 120,
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

    searchOverdueAnalysisTable(null, index);

    //查询
    $("#searchOverdueAnalysis-btn").click(function () {
        var index = layer.msg('查询中，请稍候...', {icon: 16, time: false, shade: 0.8});
        var thirdUserUUID = $("#thirdUserUUIDSelect").find("option:selected").attr("value");
        searchOverdueAnalysisTable(thirdUserUUID, index);
    });

    //查看逾期详情
    $("body").on("click", ".overdueDetail", function () {
        var _this = $(this);
        var id = _this.attr("data-id");
        var title = _this.attr("data-title");
        var overdueType = _this.attr("data-overduetype");
        if (!id) {
            layer.msg("参数错误");
            return;
        }

        $(window).one("resize", function () {
            //弹出新页面
            layer.open({
                title: title,
                type: 2, //0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
                shadeClose: true,
                shade: false,
                maxmin: true, //开启最大化最小化按钮
                scrollbar: true, // 父页面 滚动条 禁止
                area: ['80%', '80%'],
                //offset: 'rb', //右下角弹出
                //time: 2000, //2秒后自动关闭
                //anim: 2,
                content: ["/overduedatadetailview?analysisId=" + id + "&overdueType=" + overdueType, 'yes'] //iframe的url，no代表不显示滚动条
            });

        }).resize();

    });
})

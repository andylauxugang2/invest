'use strict'; //ECMAScript5 严格模式,JSON

layui.config({
    base: "res/mods/"
}).use(['form', 'layer', 'laydate'], function () {
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

    //加载页面数据
    var index = layer.msg('页面加载中，请稍候...', {icon: 16, time: false, shade: 0.8});
    var userId = $('#userIdData').val();

    function searchThirdUserInfo(index) {
        var param = {userId: userId};

        table.render({
            id: 'thirdUserInfoTable',
            elem: '#thirdUserInfoTable',
            url: '/userauth/getThirdUserInfo',
            where: {userId: userId},
            method: 'get',
            height: 300,
            page: false,
            loading: true,
            cols: [[
                {
                    field: 'thirdUserUUID',
                    title: '第三方账号',
                    width: 300,
                    sort: true
                },
                {
                    field: 'thirdUserBalance',
                    title: '拍拍贷账户余额',
                    width: 200,
                    sort: true
                }
            ]],
            done: function (res, curr, count) {
                if (index) {
                    setTimeout(function () {
                        layer.close(index);
                    }, 500);
                }
            }
        });
    }

    searchThirdUserInfo(index);
})

'use strict'; //ECMAScript5 严格模式,JSON
layui.use(['jquery', 'layer', 'element'], function () {
    window.jQuery = window.$ = layui.jquery;
    window.layer = layui.layer;
    var element = layui.element;
});


layui.config({
    base: "res/mods/"
}).use(['form', 'layer', 'jquery', 'element', 'laytpl'], function () {
    var laytpl = layui.laytpl;
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery;
    var table = layui.table;

    //全局的ajax访问，处理ajax清求时session超时
    $.ajaxSetup({
        contentType:"application/x-www-form-urlencoded;charset=utf-8",
        complete:function(XMLHttpRequest,textStatus){
            var httpStatus = XMLHttpRequest.status;
            if(httpStatus == xmlhttprequest_status_session_timeout){
                parent.location.href = "/login";
            }
            //alert(XMLHttpRequest.readyState);
        }
    });

    //加载页面数据
    var index = layer.msg('页面加载中，请稍候...', {icon: 16, time: false, shade: 0.8});
    var userId = $('#userIdData').val();

    //设置下拉框 第三方账号数据
    //alert($("#thirdUserUUIDSelect"));
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
                var htmlData = '<option value="">全部</option>';
                for (var i = 0; i < data.data.length; i++) {
                    htmlData += '<option value="' + data.data[i].thirdUserUUID + '">' + data.data[i].thirdUserUUID + '</option>'
                }
                $("#thirdUserUUIDSelect").append(htmlData);
                form.render("select");
            } else {
                layer.msg(data.msg);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.close(index);
            var httpStatus = XMLHttpRequest.status;
            if(httpStatus == xmlhttprequest_status_session_timeout){
                parent.location.href = "/login";
                return;
            }
            //alert(XMLHttpRequest.readyState);
            layer.msg("未知异常!");
        }
    });

    function searchPolicy(index, userId, thirdUserUUID) {
        table.render({
            id: 'mainPolicyTable',
            elem: '#mainPolicyTable', //指定原始表格元素选择器（推荐id选择器）
            url: '/policy/getUserMainPolicy',
            where: {userId: userId, thirdUserUUID: thirdUserUUID},
            method: 'get',
            height: 300,
            page: true,
            loading: true,
            cols: [[
                {
                    field: 'name',
                    title: '主策略名称',
                    width: 400,
                    sort: true
                }, //这里的templet值是模板元素的选择器
                {
                    field: 'userMainPolicyCreateTimeFormat',
                    title: '主策略添加时间',
                    width: 200,
                    sort: true
                },
                {
                    title: '用户策略开启状态',
                    width: 200,
                    sort: true,
                    templet: '#mainPolicyStatusTpl'
                },
                {
                    title: '操作',
                    width: 200,
                    templet: '#setMainPolicyTpl'
                }
            ]], //设置表头
            done: function (res, curr, count) {
                setTimeout(function () {
                    layer.close(index);
                }, 500);
            }
        });

    }

    searchPolicy(index, userId, null);

    form.on("select(thirdUserUUIDSelectFilter)", function (data) {
        var _this = $(this);
        var index = layer.msg('页面加载中，请稍候...', {icon: 16, time: false, shade: 0.8});
        searchPolicy(index, userId, data.value);

    });


    //操作 设置主策略
    $("body").on("click", ".main_policy_set", function () {
        var _this = $(this);
        var thirdUserUUID = $("#thirdUserUUIDSelect").find("option:selected").attr("value");
        if (!thirdUserUUID) {
            layer.msg("请选择要设置的第三方授权账号");
            return;
        }
        var mainPolicyId = _this.parents("tr").find(".main_policy_set").attr("data-id");

        //弹出新页面
        //添加
        //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
        $(window).one("resize", function () {
            var index = layui.layer.open({
                title: "设置主策略",
                type: 2, //0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
                content: "/ivgatepolicy/setusermainpolicyview?userId=" + userId + "&thirdUserUUID=" + thirdUserUUID + "&mainPolicyId=" + mainPolicyId,
                success: function (layero, index) {
                    setTimeout(function () {
                        layui.layer.tips('点击此处返回主策略列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    }, 500);
                },
                cancel: function (index, layero) {
                    if (confirm('确定要关闭么')) {
                        var index_ = layer.msg('页面加载中，请稍候...', {icon: 16, time: false, shade: 0.8});
                        searchPolicy(index_, userId, thirdUserUUID);
                    } else {
                        return false;
                    }
                    return true;
                }
            })
            layui.layer.full(index);
        }).resize();

    });

})

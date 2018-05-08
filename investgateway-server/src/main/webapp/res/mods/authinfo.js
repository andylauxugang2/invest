layui.config({
    base: "res/mods/"
}).use(['form', 'layer', 'jquery', 'element'], function () {
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

    function searchThirdInfo(index) {
        var param = {userId: userId};
        param.policyType = policy_type_userloan;

        table.render({
            id: 'thirdAuthInfoTable',
            elem: '#thirdAuthInfoTable',
            url: '/userauth/thirdAuthInfo',
            where: {userId: userId},
            method: 'get',
            height: 300,
            page: true,
            loading: true,
            cols: [[
                {
                    field: 'thirdUserUUID',
                    title: '第三方账号',
                    width: 300,
                    sort: true
                },
                {
                    field: 'createTimeFormat',
                    title: '授权时间',
                    width: 200,
                    sort: true
                },
                {
                    field: 'expiredTimeFormat',
                    title: '有效期',
                    width: 200,
                    sort: true
                },
                /*{
                 title: '是否停用',
                 width: 100,
                 sort: true,
                 templet: '#thirdAuthStatusTpl'
                 },*/
                {
                    title: '操作',
                    width: 200,
                    templet: '#gotoThirdAuthTpl'
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

    searchThirdInfo(index);

    setTimeout(function () {
        layer.close(index);
    }, 1000);

    //去授权
    $(".gotoAuth").click(function () {
        var index = layer.msg('跳转中，请稍候...', {icon: 16, time: false, shade: 0.8});
        setTimeout(function () {
            $.ajax({
                type: "post",
                url: "/userauth/login",
                data: JSON.stringify({'userId': userId}),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                dataType: "json",
                success: function (data) {
                    layer.close(index);
                    if (data.code == 0) {
                        parent.location.href = data.data;
                    } else {
                        layer.msg(data.msg);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    layer.close(index);
                    var httpStatus = XMLHttpRequest.status;
                    if (httpStatus == xmlhttprequest_status_session_timeout) {
                        parent.location.href = "/login";
                        return;
                    }
                    //alert(XMLHttpRequest.readyState);
                    layer.msg("未知异常!");
                }
            });
        }, 1000);
    });

    $("body").on("click", ".refresh", function () {
        var index = layer.msg('页面刷新中，请稍候...', {icon: 16, time: false, shade: 0.8});
        searchThirdInfo(index);
    });

    //解除绑定
    $("body").on("click", ".third_auth_del", function () {
        var _this = $(this);
        layer.confirm('如果您只重新授权,请直接点击上面的[去授权]按钮即可,无需解绑', {
            icon: 3,
            title: '提示信息'
        }, function (index) {
            layer.confirm('确定解除绑定吗？解除后所有主策略设置全部失效,所有子策略设置会保留,重新授权后需要手动开启主策略即可进行自动投标,请备份好您的主策略设置', {
                icon: 3,
                title: '提示信息'
            }, function (index) {
                var index = layer.msg('解绑中，请稍候', {icon: 16, time: false, shade: 0.8});
                //主键id
                var id = _this.parents("tr").find(".third_auth_del").attr("data-id");
                if (!id) {
                    layer.close(index);
                    layer.msg("参数错误");
                }

                $.ajax({
                    type: "get",
                    url: "/userauth/delThirdAuthInfo?id=" + id + "&userId=" + userId,
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    success: function (data) {
                        if (data.code == 0) {
                            setTimeout(function () {
                                layer.msg("解绑成功");
                                searchThirdInfo(index);
                            }, 500);
                        } else {
                            setTimeout(function () {
                                layer.close(index);
                                layer.msg(data.msg);
                            }, 500);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        layer.close(index);
                        var httpStatus = XMLHttpRequest.status;
                        if (httpStatus == xmlhttprequest_status_session_timeout) {
                            parent.location.href = "/login";
                            return;
                        }
                        //alert(XMLHttpRequest.readyState);
                        layer.msg("未知异常!");
                    }
                });
            });
        });
    });


})

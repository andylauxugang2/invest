'use strict'; //ECMAScript5 严格模式,JSON

layui.config({
    base: "res/mods/"
}).use(['form', 'layer', 'jquery', 'element', 'laytpl'], function () {
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
            layer.msg("未知异常!");
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
            id: 'userSysLaonPolicyTable',
            elem: '#userSysLaonPolicyTable', //指定原始表格元素选择器（推荐id选择器）
            url: '/policy/getUserLoanPolicies',
            where: param,
            method: 'get',
            height: "auto",
            page: false,
            loading: true,
            cols: [[
                {checkbox: true, LAY_CHECKED: false},
                {
                    field: 'username',
                    title: '第三方账号',
                    width: 180,
                    sort: true
                },
                {
                    field: 'id',
                    title: '策略编号',
                    width: 120,
                    sort: true
                },
                {
                    templet: '#policyDetailLinkTpl',
                    title: '系统策略名称',
                    width: 200,
                    sort: true
                },

                {
                    title: '投标金额',
                    width: 100,
                    sort: true,
                    templet: '#bidAmountTpl'
                },
                {
                    title: '子策略开启状态',
                    width: 150,
                    sort: true,
                    templet: '#policyStatusTpl'
                },
                {
                    field: 'userPolicyId', //record id
                    style: 'display:none'
                },
                {
                    title: '操作',
                    width: 200,
                    templet: '#optTpl'
                }
            ]], //设置表头
            done: function (res, curr, count) {
                //如果是异步请求数据方式，res即为你接口返回的信息。
                //如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
                //得到当前页码
                //得到数据总量

                $("#userSysLaonPolicyTable thead").ready(function () {
                    $(this).find('th').each(function(j){
                        //$(this).css("color","red");
                        var attr = $(this).attr("data-field");
                        if(attr == "userPolicyId") $(this).css("display","none");
                    });
                });

                setTimeout(function () {
                    layer.close(index);
                }, 500);
            }
        });
    }


    searchPolicy(null, null, index);

    //查询
    $("#searchMyPolicy-btn").click(function () {
        var index = layer.msg('查询中，请稍候...', {icon: 16, time: false, shade: 0.8});

        searchPolicy($("#thirdUserUUIDSelect").val(), $("#riskLevelSelect").val(), index);
        /*tableIns.reload({
         where: {
         userId: userId,
         thirdUserUUID: $("#thirdUserUUIDSelect").val(),
         riskLevel: $("#riskLevelSelect").val()
         },
         done: function (res, curr, count) {
         setTimeout(function () {
         layer.close(index);
         }, 500);
         }
         });*/
    })

    //添加新策略
    $("#addMyPolicy-btn").click(function () {
        var _this = $(this);
        var thirdUserUUID = $("#thirdUserUUIDSelect").find("option:selected").attr("value");
        if (!thirdUserUUID) {
            layer.msg("请选择要添加策略的第三方授权账号");
            return;
        }

        //判断是否开启主策略
        $.ajax({
            type: "get",
            url: "/policy/getUserMainPolicyOne?userId=" + userId + "&thirdUserUUID=" + thirdUserUUID + "&mainPolicyId=" + main_policy_type_sysloan,
            async: false,
            success: function (data) {
                if (data.code == 0) {
                    if (data.data != null) {
                        //弹出新页面
                        //选择添加新策略
                        //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
                        $(window).one("resize", function () {
                            var index = layui.layer.open({
                                title: "添加系统散标子策略",
                                type: 2, //0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
                                content: "/ivgatepolicy/addusersysloanpolicyview?userId=" + userId + "&thirdUserUUID=" + thirdUserUUID,
                                success: function (layero, index) {
                                    setTimeout(function () {
                                        layui.layer.tips('点击此处返回散标策略列表', '.layui-layer-setwin .layui-layer-close', {
                                            tips: 3
                                        });
                                    }, 500);
                                },
                                cancel: function (index, layero) {
                                    /*layer.confirm('确定关闭？', {icon: 3, title: '提示信息'}, function (index) {
                                     layer.close(index);
                                     searchPolicy();
                                     });*/
                                    if (confirm('确定要关闭么')) { //只有当点击confirm框的确定时，该层才会关闭
                                        layer.close(index);
                                        searchPolicy();
                                    } else {
                                        return false;
                                    }
                                    return true;
                                }
                            });
                            layui.layer.full(index);
                        }).resize();
                    } else {
                        setTimeout(function () {
                            layer.close(index);
                            layer.msg("添加失败,请先添加并开启系统散标主策略");
                        }, 500);
                    }
                } else {
                    setTimeout(function () {
                        layer.close(index);
                        layer.msg(data.message);
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
    });

    //删除
    $("#delMyPolicy-btn").click(function () {
        var _this = $(this);
        var checkStatus = table.checkStatus('userSysLaonPolicyTable'); //参数id设定的值
        if (checkStatus.data.length > 0) {
            layer.confirm('确定删除选中的策略？', {icon: 3, title: '提示信息'}, function (index) {
                var index = layer.msg('删除中，请稍候', {icon: 16, time: false, shade: 0.8});
                var dataIdArray = new Array();
                for (var j = 0; j < checkStatus.data.length; j++) {
                    var dataId = checkStatus.data[j].userPolicyId;
                    dataIdArray.push(dataId);
                }
                if (dataIdArray.length == 0) {
                    layer.msg("请选择要删除的策略");
                    return;
                }

                $.ajax({
                    type: "post",
                    url: "/policy/dettachUserThirdLoanPolicy",
                    data: JSON.stringify({
                        'userLoanPolicyIds': dataIdArray,
                        'userId': userId
                    }),
                    dataType: "json",
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    success: function (data) {
                        if (data.code == 0) {
                            setTimeout(function () {
                                layer.close(index);
                                layer.msg("删除成功,可重新点击添加按钮添加策略");
                                searchPolicy();
                            }, 500);
                            //$('.syslaon_policy_list thead input[type="checkbox"]').prop("checked", false);
                            //form.render();
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
            layer.msg("请选择要删除的策略");
        }

    })

    form.on('switch(isShow)', function (data) {
        var yes = data.elem.checked;
        if (yes) yes = 1;
        else yes = 0;
        var _this = $(this);
        _this.parents("tr").find(".status-is-show").html(yes);
    });

    //查看策略详情
    $("body").on("click", ".policyDetail", function () {
        var _this = $(this);
        var policyId = _this.attr("data-id");
        if (!policyId) {
            layer.msg("参数错误");
        }

        $(window).one("resize", function () {
            //弹出新页面
            var index = layui.layer.open({
                title: "散标策略详情",
                type: 2, //0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
                maxmin: true,
                resize: true,
                area: ['400px', '400px'],
                content: "/ivgatepolicy/loanpolicydetailview?userId=" + userId + "&policyId=" + policyId,
                success: function (layero, index) {
                    setTimeout(function () {
                        layui.layer.tips('点击此处返回散标策略列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    }, 500);
                },
                cancel: function (index, layero) {
                    return true;
                }
            });
        }).resize();

    });

    //操作 保存修改
    $("body").on("click", ".save-policy-modify", function () {
        var _this = $(this);
        layer.confirm('确定保存策略修改？', {icon: 3, title: '提示信息'}, function (index) {
            var index = layer.msg('保存中，请稍候', {icon: 16, time: false, shade: 0.8});
            var userSysLoanPolicyId = _this.parents("tr").find(".save-policy-modify").attr("data-id");
            var thirdUserUUID = _this.parents("tr").find(".third-user-uuid").html();
            //var status = _this.parents("tr").find(".user-sys-policy-status").attr('checked');
            var userSysPolicyStatus = _this.parents("tr").find(".status-is-show").html();
            var policyId = _this.parents("tr").find(".policy-id").attr("data-id");
            var bidAmount = _this.parents("tr").find(".bid-amount").val();

            $.ajax({
                type: "post",
                url: "/policy/saveModifyUserLoanPolicy",
                data: JSON.stringify({
                    'userId': userId,
                    'thirdUserUUID': thirdUserUUID,
                    'userLoanPolicyId': userSysLoanPolicyId,
                    'userPolicyStatus': userSysPolicyStatus,
                    'bidAmount': bidAmount,
                    'policyId': policyId
                }),
                dataType: "json",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                success: function (data) {
                    if (data.code == 0) {
                        setTimeout(function () {
                            layer.close(index);
                            layer.msg("保存成功");
                        }, 500);
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
    });

})

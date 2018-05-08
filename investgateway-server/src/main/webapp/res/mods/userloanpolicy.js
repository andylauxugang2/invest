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
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.close(index);
            /*var httpStatus = XMLHttpRequest.status;
             if (httpStatus == xmlhttprequest_status_session_timeout) {
             parent.location.href = "/login";
             return;
             }*/
            //alert(XMLHttpRequest.readyState);
            layer.msg("未知异常!");
        }
    });

    form.on("select(thirdUserUUIDSelectFilter)", function (data) {
        var _this = $(this);
        var index = layer.msg('第三方账号切换中，请稍候...', {icon: 16, time: false, shade: 0.8});
        searchUserThirdPolicy(data.value, index);
    });

    function searchPolicy(index) {
        var param = {userId: userId};
        param.policyType = policy_type_userloan;

        table.render({
            id: 'userLoanPolicyTable',
            elem: '#userLoanPolicyTable', //指定原始表格元素选择器（推荐id选择器）
            url: '/policy/getLoanPolicies',
            where: param,
            method: 'get',
            height: 300,
            page: false,
            loading: true,
            cols: [[
                {checkbox: true, LAY_CHECKED: false},
                {
                    field: 'id',
                    title: '编号',
                    width: 70,
                    sort: true
                },
                {
                    title: '操作',
                    width: 150,
                    templet: '#optTpl'
                },
                {
                    field: 'name',
                    title: '策略名称',
                    width: 130,
                    sort: true
                }, //这里的templet值是模板元素的选择器
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
                },
                {
                    field: 'totalPrincipal',
                    title: '累计借款金额',
                    width: 100,
                    sort: true
                },
                {
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
                    field: 'amountOwingTotal',
                    title: '借款总额度',
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
                if (index) {
                    setTimeout(function () {
                        layer.close(index);
                    }, 500);
                }
            }
        });
    }

    //加载第三方用户散标策略列表
    function searchUserThirdPolicy(thirdUserUUID, index) {
        var param = {userId: userId};
        param.policyType = policy_type_userloan;
        if (thirdUserUUID) {
            param.thirdUserUUID = thirdUserUUID;
        }

        table.render({
            id: 'userThirdUUIDLoanPolicyTable',
            elem: '#userThirdUUIDLoanPolicyTable',
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
                    width: 150,
                    sort: true
                },
                {
                    templet: '#policyDetailLinkTpl',
                    title: '策略名称',
                    width: 200,
                    sort: true
                },
                {
                    field: 'id',
                    title: '散标编号',
                    width: 90,
                    sort: true
                },
                {
                    field: 'userPolicyCreateTimeFormat',
                    title: '添加时间',
                    width: 150,
                    sort: true
                },
                {
                    title: '投标金额',
                    width: 100,
                    templet: '#bidAmountTpl'
                },
                {
                    title: '子策略开启状态',
                    width: 120,
                    templet: '#policyStatusTpl'
                },
                {
                    title: '操作',
                    width: 200,
                    templet: '#userThirdOptTpl'
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

    searchPolicy(index);
    searchUserThirdPolicy(null, null);

    //操作 保存修改
    $("body").on("click", ".save-user-policy-modify", function () {
        var _this = $(this);
        //var pageIndex = layer.msg('拉取更新信息，请稍候', {icon: 16, time: false, shade: 0.8});
        var loanPolicyId = _this.parents("tr").find(".save-user-policy-modify").attr("data-id");
        if (!loanPolicyId) {
            layer.close(pageIndex);
            layer.msg("参数错误");
        }
        //弹出新页面
        //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
        $(window).one("resize", function () {
            var index = layui.layer.open({
                title: "添加自定义散标",
                type: 2, //0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
                content: "/ivgatepolicy/adduserloanpolicyview?userId=" + userId + "&mainPolicyId=" + 2 + "&loanPolicyId=" + loanPolicyId,
                success: function (layero, index) {
                    //layer.close(pageIndex);
                    setTimeout(function () {
                        layui.layer.tips('点击此处返回自定义散标列表', '.layui-layer-setwin .layui-layer-close', {
                            tips: 3
                        });
                    }, 500);
                },
                cancel: function (index, layero) {
                    if (confirm('确定要关闭么')) { //只有当点击confirm框的确定时，该层才会关闭
                        layer.close(index);
                        var index = layer.msg('页面加载中，请稍候...', {icon: 16, time: false, shade: 0.8});
                        searchPolicy(index);
                    } else {
                        return false;
                    }
                    return true;
                }
            });
            layui.layer.full(index);
        }).resize();
    });

    //添加新策略
    $("#addMyPolicy-btn").click(function () {
        var _this = $(this);

        //获取开启主策略的第三方用户 带到下一页面
        $.ajax({
            type: "get",
            url: "/policy/getUserMainPolicies?userId=" + userId + "&mainPolicyId=" + 2,
            async: false,
            success: function (data) {
                if (data.code == 0) {
                    if (data.data != null) {
                        //弹出新页面
                        //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
                        $(window).one("resize", function () {
                            var index = layui.layer.open({
                                title: "添加自定义散标",
                                type: 2, //0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
                                content: "/ivgatepolicy/adduserloanpolicyview?userId=" + userId + "&mainPolicyId=" + 2,
                                success: function (layero, index) {
                                    setTimeout(function () {
                                        layui.layer.tips('点击此处返回自定义散标列表', '.layui-layer-setwin .layui-layer-close', {
                                            tips: 3
                                        });
                                    }, 500);
                                },
                                cancel: function (index, layero) {
                                    if (confirm('确定要关闭么')) { //只有当点击confirm框的确定时，该层才会关闭
                                        layer.close(index);
                                        var index = layer.msg('页面加载中，请稍候...', {icon: 16, time: false, shade: 0.8});
                                        searchPolicy(index);
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
                            layer.msg("添加失败,请先至少为一个第三方授权账户设置主策略-自定义散标");
                        }, 1000);
                    }
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
    });

    //删除
    $("#delMyPolicy-btn").click(function () {
        var _this = $(this);
        var checkStatus = table.checkStatus('userLoanPolicyTable'); //参数id设定的值
        if (checkStatus.data.length > 0) {
            layer.confirm('确定删除选中的策略？', {icon: 3, title: '提示信息'}, function (index) {
                var index = layer.msg('删除中，请稍候', {icon: 16, time: false, shade: 0.8});
                var dataIdArray = new Array();
                for (var j = 0; j < checkStatus.data.length; j++) {
                    var dataId = checkStatus.data[j].id;
                    dataIdArray.push(dataId);
                }
                if (dataIdArray.length == 0) {
                    layer.msg("请选择要删除的策略");
                    return;
                }

                $.ajax({
                    type: "post",
                    url: "/policy/removeLoanPolicy",
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
                                searchPolicy(index);
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
                            layer.msg("未知异常!");
                        }, 1000);
                    }
                });
            })
        } else {
            layer.msg("请选择要删除的策略");
        }

    });

    //解挂
    $("#dettachMyPolicy-btn").click(function () {
        var _this = $(this);
        var checkStatus = table.checkStatus('userThirdUUIDLoanPolicyTable'); //参数id设定的值
        if (checkStatus.data.length > 0) {
            layer.confirm('确定解挂选中的策略？', {icon: 3, title: '提示信息'}, function (index) {
                var index = layer.msg('策略解挂中，请稍候', {icon: 16, time: false, shade: 0.8});
                var dataIdArray = new Array();
                for (var j = 0; j < checkStatus.data.length; j++) {
                    var dataId = checkStatus.data[j].userPolicyId;
                    dataIdArray.push(dataId);
                }
                if (dataIdArray.length == 0) {
                    layer.msg("请选择要解挂的策略");
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
                                layer.msg("解挂成功,可重新点击挂载为第三方账号添加自定义策略");
                                var thirdUserUUID = $("#thirdUserUUIDSelect").find("option:selected").attr("value");
                                searchUserThirdPolicy(thirdUserUUID, index);
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
                            layer.msg("未知异常!");
                        }, 1000);
                    }
                });
            })
        } else {
            layer.msg("请选择要解挂的策略");
        }

    });

    form.on('switch(isShow)', function (data) {
        var yes = data.elem.checked;
        if (yes) yes = 1;
        else yes = 0;
        var _this = $(this);
        _this.parents("tr").find(".status-is-show").html(yes);
    });

    //操作 保存修改
    $("body").on("click", ".save-policy-modify", function () {
        var _this = $(this);
        layer.confirm('确定保存更新？', {icon: 3, title: '提示信息'}, function (index) {
            var index = layer.msg('保存中，请稍候', {icon: 16, time: false, shade: 0.8});
            var id = _this.parents("tr").find(".save-policy-modify").attr("data-id");
            if (!id) {
                layer.close(index);
                layer.msg("参数错误");
            }

            var userThirdLoanPolicyStatus = _this.parents("tr").find(".status-is-show").html();
            var policyId = _this.parents("tr").find(".policy-id").attr("data-id");
            var bidAmount = _this.parents("tr").find(".bid-amount").val();
            $.ajax({
                type: "post",
                url: "/policy/saveUserThirdLoanPolicy",
                data: JSON.stringify({
                    'userId': userId,
                    'userThirdLoanPolicyId': id,
                    'userThirdLoanPolicyStatus': userThirdLoanPolicyStatus,
                    'bidAmount': bidAmount
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
                            layer.msg("保存修改成功");
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

    //挂载操作
    $("body").on("click", ".attach-loan-policy", function () {
        var _this = $(this);
        var policyId = _this.parents("tr").find(".attach-loan-policy").attr("data-id");
        if (!policyId) {
            layer.msg("参数错误");
        }

        //查询未添加过该策略的第三方账号
        $.ajax({
            type: "get",
            url: "/policy/getUserMainPolicies?userId=" + userId + "&mainPolicyId=" + main_policy_type_userloan,
            async: false,
            success: function (data) {
                if (data.code == 0) {
                    if (data.data != null && data.data.length > 0) {
                        $.ajax({
                            type: "get",
                            url: "/policy/getNoSelPolicyThirdUUIDList?userId=" + userId + "&policyId=" + policyId,
                            async: false,
                            success: function (data) {
                                if (data.code == 0) {
                                    if (data.data.length == 0) {
                                        layer.msg("该策略无挂满可用的第三方账号,请去解挂或添加第三方账号");
                                        return;
                                    } else {
                                        var thirdUUIDListHtml = getThirdUUIDListHtml(data.data);
                                        //弹出列表

                                        /*layer.msg(thirdUUIDListHtml,{time: -1,btn: ['已确认,可以挂载', '取消'],
                                         yes: function (index, layero) {alert($('.thirdUUIDCheckBoxList'));
                                         alert(layero[0].childNodes[0].children[0].children.length);
                                         var arr =  layero[0].childNodes[0].children[0].children;
                                         for(var i = 0;i < arr.length; i++){
                                         if(arr[i].checked) alert(arr[i].defaultValue);
                                         }
                                         //debugger;

                                         }
                                         });*/

                                        layer.msg(thirdUUIDListHtml, {
                                            shade: 0.8,
                                            time: false,
                                            //area: ['500px', '300px']
                                            btn: ['已确认,可以挂载', '取消'],
                                            yes: function (index, layero) {
                                                var userThirdBindInfoList = new Array;
                                                var arr = layero[0].childNodes[0].children[0].children;
                                                for (var i = 0; i < arr.length; i++) {
                                                    if (arr[i].checked) userThirdBindInfoList.push(arr[i].defaultValue);
                                                }
                                                if (userThirdBindInfoList.length == 0) {
                                                    layer.msg("请勾选要挂载的第三方账号!");
                                                    return;
                                                }
                                                var param = JSON.stringify({
                                                    'policyId': policyId,
                                                    'userId': userId,
                                                    'thirdUUIDs': userThirdBindInfoList
                                                });
                                                //alert(param);
                                                var index = layer.msg('挂载中，请稍候', {icon: 16, time: false, shade: 0.8});

                                                $.ajax({
                                                    type: "post",
                                                    url: "/policy/attachUserThirdLoanPolicy",
                                                    data: param,
                                                    dataType: "json",
                                                    headers: {
                                                        'Accept': 'application/json',
                                                        'Content-Type': 'application/json'
                                                    },
                                                    success: function (data) {
                                                        if (data.code == 0) {
                                                            setTimeout(function () {
                                                                layer.msg("挂载中成功,可点击解挂删除策略");
                                                                var thirdUserUUID = $("#thirdUserUUIDSelect").find("option:selected").attr("value");
                                                                searchUserThirdPolicy(thirdUserUUID, index);
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
                                                            layer.msg("未知异常!");
                                                        }, 1000);
                                                    }
                                                });
                                            },
                                            btn2: function () {

                                            }
                                        });
                                    }


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
                    } else {
                        setTimeout(function () {
                            layer.msg("挂载失败,请先添加并开启自定义散标主策略");
                        }, 500);
                    }
                } else {
                    layer.msg(data.msg);
                }
            },
            error: function (e) {
                layer.msg("未知异常!");
            }
        });

    });

    function getThirdUUIDListHtml(thirdUUIDList) {
        var result = '';
        result += '<div class="thirdUUIDCheckBoxList" style="text-align: left">请勾选已授权的第三方账号:<br/>';
        for (var i = 0; i < thirdUUIDList.length; i++) {
            result += '<input type="checkbox" id="userThirdBindInfoList" name="userThirdBindInfoList" value="' + thirdUUIDList[i] + '"><span style="margin-left: 5px;">' + thirdUUIDList[i] + '</span><br/>';
        }
        result += '</div>';
        return result;
    }

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
})

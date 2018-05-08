'use strict'; //ECMAScript5 严格模式,JSON

var buycount_type_1 = 1;
var buycount_type_2 = 2;
var buycount_type_3 = 3;
var buycount_type_4 = 4;
var buycount_type_5 = 5;
var buycount_type_6 = 6;
var buycount_type_10 = 10;

layui.config({
    base: "res/mods/"
}).use(['form', 'layer', 'jquery', 'element', 'laytpl', 'table', 'laydate'], function () {

    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery;
    var element = layui.element;
    var laydate = layui.laydate;

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

    var userId = $('#userIdData').val();
    if (!userId) {
        layer.msg("页面基础参数获取失败,请重新登录或联系客服解决");
        return;
    }

    function getUserAccountBalance() {
        var index = layer.msg('充值页面加载中，请稍候', {icon: 16, time: false, shade: 0.8});
        //设置下拉框 产品类型
        $.ajax({
            type: "get",
            url: "/account/getUserAccountInfo?userId=" + userId,
            async: false,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            success: function (data) {
                setTimeout(function () {
                    layer.close(index);
                    if (data.code == 0) {
                        $("#zhuobaobiBalance").html(data.data.zhuobaoBalance);
                        $("#bidAmountBalance").html(data.data.bidAmountBalance);
                    } else {
                        layer.msg(data.msg);
                    }
                }, 500);
            },
            error: function (e) {
                setTimeout(function () {
                    layer.msg("未知异常!");
                }, 500);
            }
        });
    }

    getUserAccountBalance();

    //自定义验证规则
    form.verify({
        buyCountOther: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "请正确填写要购买的捉宝币个数";
            }
            value = parseInt(value);
            if (value <= 0) {
                return '要购买的捉宝币个数不能为0';
            }
        }
    });


    var buyCount = 0;
    var price = 0;

    form.on('radio(buyCountFilter)', function (data) {
        var buyCountType = data.value;
        if (buycount_type_10 == buyCountType) { //其他数目
            buyCount = 0;
            price = 0;
            $('.form-item-display').css('display', 'inline');
            $('#buyCountOther').val('');
        } else {
            $('.form-item-display').css('display', 'none');
            switch (parseInt(buyCountType)) {
                case buycount_type_1:
                    buyCount = 120;
                    price = 100;
                    break;
                case buycount_type_2:
                    buyCount = 240;
                    price = 200;
                    break;
                case buycount_type_3:
                    buyCount = 360;
                    price = 300;
                    break;
                case buycount_type_4:
                    buyCount = 480;
                    price = 400;
                    break;
                case buycount_type_5:
                    buyCount = 600;
                    price = 500;
                    break;
                case buycount_type_6:
                    buyCount = 960;
                    price = 800;
                    break;
                default:
                    ;
            }
        }
        //更新页面值
        $('#price').html(price);
        $('#buyCount').html(buyCount);
    });

    $("#buyCountOther").on("input propertychange", function () {
        buyCount = $(this).val();
        var pattern = /^\d*$/;
        if (!pattern.test(buyCount)) {
            layer.msg("请正确填写要购买的捉宝币个数");
            buyCount = 0;
            price = 0;
            //更新页面值
            $('#price').html(price);
            $('#buyCount').html(buyCount);
            return;
        }
        buyCount = parseInt(buyCount);
        if (buyCount <= 0) {
            layer.msg("要购买的捉宝币个数不能为0");
            buyCount = 0;
            price = 0;
            //更新页面值
            $('#price').html(price);
            $('#buyCount').html(buyCount);
            return;
        }

        if (!buyCount) {
            price = 0;
            buyCount = 0;
        }
        //算钱 1个捉宝币=1元钱
        if (buyCount > 0 && buyCount < 100) {
            price = buyCount;
        } else if (buyCount >= 100) {
            price = buyCount;
            buyCount = buyCount + parseInt(0.2 * buyCount); //送 20%
        }
        if (!buyCount) buyCount = 0;
        //更新页面值
        $('#price').html(price);
        $('#buyCount').html(buyCount);
    });

    form.on('checkbox(agreeFilter)', function (data) {
        var agree = data.elem.checked;
        if (agree) {
            $('#btnPayOver').removeClass('layui-btn-disabled');
        } else {
            $('#btnPayOver').addClass('layui-btn-disabled');
        }
    });

    //监听提交
    form.on('submit(payOverFilter)', function (data) {
        var agreeArray = $('.agree input[type="checkbox"][name="agree"]:checked');
        if (!agreeArray.length) {
            return false;
        }
        var index = layer.msg('支付结果处理中，请稍候', {icon: 16, time: false, shade: 0.8});
        var paramJson = data.field;
        paramJson.userId = userId;

        var paramStr = JSON.stringify(paramJson); //把一个对象转成一个序列化的JSON字符串
        $.ajax({
            type: "post",
            url: "/pay/payOver", //临时方案 线下支付
            data: paramStr,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            dataType: "json",
            success: function (data) {
                if (data.code == 0) {
                    setTimeout(function () {
                        layer.close(index);
                        layer.open({
                            type: 1,
                            skin: 'layui-layer-rim', //加上边框
                            area: ['420px', '240px'], //宽高
                            content: '<div style="margin: 10px;">恭喜您，捉宝币充值支付处理成功，您的订单号是【<span>' + data.data + '</span>】，请查看充值订单处理结果：<a href="#">交易查询</a></div>'
                        });

                    }, 500);
                } else {
                    setTimeout(function () {
                        layer.close(index);
                        layer.msg(data.msg);
                    }, 500);
                }
            },
            error: function (e) {
                layer.msg("未知异常!");
            }
        });
        return false;
    });

    //监听Tab切换
    element.on('tab(accountTabFilter)', function (data) {
        /*console.log(this); //当前Tab标题所在的原始DOM元素
         console.log(data.index); //得到当前Tab的所在下标 0 1 2
         console.log(data.elem); //得到当前的Tab大容器*/
        var tabIndex = data.index;

        if (tabIndex == 1) { //交易查询
            var index = layer.msg('页面加载中，请稍候...', {icon: 16, time: false, shade: 0.8});
            searchOrder(index, null, null, null, null);
        } else if (tabIndex == 0) {
            $("#zhuobaobiBalance").html("获取中...");
            getUserAccountBalance();
        }

    });

    //---------------------------------交易查询------------------------------------
    //执行一个laydate实例
    laydate.render({
        elem: '#orderBeginTime', //指定元素
        type: 'datetime'
    });
    laydate.render({
        elem: '#orderEndTime', //指定元素
        type: 'datetime'
    });

    //设置下拉框 产品类型
    $.ajax({
        type: "get",
        url: "/order/getOrderType?userId=" + userId,
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
                $("#orderTypeSelect").append(htmlData);
                form.render("select");
            } else {
                layer.msg(data.msg);
            }
        },
        error: function (e) {
            layer.msg("未知异常!");
        }
    });

    //设置下拉框 支付状态
    $.ajax({
        type: "get",
        url: "/pay/getPayStatus?userId=" + userId,
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
                $("#payStatusSelect").append(htmlData);
                form.render("select");
            } else {
                layer.msg(data.msg);
            }
        },
        error: function (e) {
            layer.msg("未知异常!");
        }
    });

    var table = layui.table;

    function searchOrder(index, orderType, payStatus, orderBeginTime, orderEndTime) {
        var param = {userId: userId};
        if (orderType) {
            param.orderType = orderType;
        }
        if (payStatus) {
            param.payStatus = payStatus;
        }
        if (orderBeginTime) {
            param.orderBeginTime = orderBeginTime;
        }
        if (orderEndTime) {
            param.orderEndTime = orderEndTime;
        }

        table.render({
            id: 'orderTable',
            elem: '#orderTable', //指定原始表格元素选择器（推荐id选择器）
            url: '/order/getUserOrder',
            where: param,
            method: 'get',
            height: 'auto',
            page: true,
            loading: true,
            cols: [[
                //{checkbox: true, LAY_CHECKED: false},
                {
                    field: 'id',
                    style: 'display:none'
                },
                {
                    title: '订单号',
                    width: 180,
                    style: 'text-align:left',
                    templet: '#orderDetailTpl'
                },
                {
                    title: '产品',
                    field: 'orderType',
                    width: 80,
                    sort: true
                },
                {
                    field: 'createTime',
                    title: '创建时间',
                    width: 150,
                    sort: true
                },
                {
                    field: 'payTime',
                    title: '支付时间',
                    width: 150,
                    sort: true
                },
                {
                    templet: '#payStatusTpl',
                    title: '支付状态',
                    width: 90,
                    sort: true
                },
                {
                    templet: '#orderStatusTpl',
                    title: '订单状态',
                    width: 120,
                    sort: true
                },
                {
                    templet: '#priceTpl',
                    title: '原价',
                    width: 100,
                    sort: true
                },
                {
                    templet: '#payPriceTpl',
                    title: '应付金额',
                    width: 100,
                    sort: true
                },
                {
                    field: 'payway',
                    title: '支付方式',
                    width: 80,
                    sort: true
                }
            ]], //设置表头
            done: function (res, curr, count) {
                $("#orderTable thead").ready(function () {
                    $(this).find('th').each(function (j) {
                        //$(this).css("color","red");
                        var attr = $(this).attr("data-field");
                        if (attr == "id") $(this).css("display", "none");
                    });
                });

                if (index) {
                    setTimeout(function () {
                        layer.close(index);
                    }, 500);
                }
            }
        });
    }

    //查询
    $("#searchOrder-btn").click(function () {
        var index = layer.msg('查询中，请稍候...', {icon: 16, time: false, shade: 0.8});
        var orderBeginTime = $("#orderBeginTime").val();
        var orderEndTime = $("#orderEndTime").val();
        searchOrder(index, $("#orderTypeSelect").val(), $("#payStatusSelect").val(), orderBeginTime, orderEndTime);
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
})

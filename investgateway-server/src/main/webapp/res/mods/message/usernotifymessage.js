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

    function searchMessage(index, tabIndex) {
        var tableId, messageStatus;
        if (tabIndex == 0) {
            tableId = "unReadMessageTable";
            messageStatus = 0;
        } else if (tabIndex == 1) {
            tableId = "readedMessageTable";
            messageStatus = 1;
        } else if (tabIndex == 2) {
            tableId = "allMessageTable";
            messageStatus = null;
        }

        var param = {userId: userId};
        param.messageStatus = messageStatus;

        table.render({
            id: tableId,
            elem: '#' + tableId, //指定原始表格元素选择器（推荐id选择器）
            url: '/message/getUserNotifyMessage',
            where: param,
            method: 'get',
            height: 'auto',
            page: true,
            loading: true,
            cols: [[
                {checkbox: true, LAY_CHECKED: false},
                {
                    field: 'id',
                    style: 'display:none'
                },
                {
                    title: '标题内容',
                    width: 500,
                    style: 'text-align:left',
                    templet: '#titleTpl',
                    sort: true
                },
                {
                    field: 'createTime',
                    title: '提交时间',
                    width: 200,
                    sort: true
                },
                {
                    field: 'type',
                    title: '消息类型',
                    width: 150,
                    sort: true
                }
            ]], //设置表头
            done: function (res, curr, count) {
                $("#" + tableId + " thead").ready(function () {
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

    searchMessage(index, 0);

    //监听Tab切换
    element.on('tab(messageTabFilter)', function (data) {
        /*console.log(this); //当前Tab标题所在的原始DOM元素
         console.log(data.index); //得到当前Tab的所在下标 0 1 2
         console.log(data.elem); //得到当前的Tab大容器*/
        var tabIndex = data.index;

        var index = layer.msg('页面加载中，请稍候...', {icon: 16, time: false, shade: 0.8});
        searchMessage(index, tabIndex);
    });


    $("body").on("click", ".read-message", function () {
        var _this = $(this);

        var userMessageId = _this.attr("data-id");
        var messageId = _this.attr("data-messageId");
        var href = _this.attr("data-href");
        var status = _this.attr("data-status");

        if (href == "null") { //无需跳转
            //弹出新页面
            //选择添加新策略
            //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
            $(window).one("resize", function () {
                var index = layui.layer.open({
                    title: "用户消息",
                    type: 2, //0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
                    content: "/usernotifymessagedetailview?userId=" + userId +"&messageId=" + messageId + "&userMessageId=" + userMessageId,
                    success: function (layero, index) {
                        setTimeout(function () {
                            layui.layer.tips('点击此处返回专家消息', '.layui-layer-setwin .layui-layer-close', {
                                tips: 3
                            });
                            $.ajax({
                                type: "post",
                                url: "/message/readUserNotifyMessage",
                                data: JSON.stringify({
                                    'userId': userId,
                                    'userMessageId': userMessageId
                                }),
                                dataType: "json",
                                headers: {
                                    'Accept': 'application/json',
                                    'Content-Type': 'application/json'
                                },
                                success: function (data) {
                                },
                                error: function (e) {
                                    setTimeout(function () {
                                        layer.msg("未知异常,请检查网络是否断开!");
                                    }, 1000);
                                }
                            });
                        }, 500);
                    },
                    cancel: function (index, layero) {
                        if (confirm('确定要关闭么')) { //只有当点击confirm框的确定时，该层才会关闭
                            var index = layer.msg('页面加载中，请稍候...', {icon: 16, time: false, shade: 0.8});
                            searchMessage(index, 0);
                        } else {
                            return false;
                        }
                        return true;
                    }
                });
                layui.layer.full(index);
            }).resize();

            /*$.ajax({
                type: "get",
                url: "/message/getUserNotifyMessageDetail?messageId=" + messageId,
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                success: function (data) {
                    if (data.code == 0) {
                        setTimeout(function () {
                            layer.msg(data.data.content);

                        }, 500);
                    } else {
                        setTimeout(function () {
                            layer.msg(data.msg);
                        }, 500);
                    }
                },
                error: function (e) {
                    setTimeout(function () {
                        layer.msg("未知异常,请检查网络是否断开!");
                    }, 1000);
                }
            });*/
            return;
        }

        var index = layer.msg('跳转中，请稍候', {icon: 16, time: false, shade: 0.8});

        if (status == 1) { //已读直接跳转
            setTimeout(function () {
                layer.close(index);
                window.open(href, '_blank');
            }, 500);
            return;
        }
        $.ajax({
            type: "post",
            url: "/message/readUserNotifyMessage",
            data: JSON.stringify({
                'userId': userId,
                'userMessageId': userMessageId
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
                        window.open(href, '_blank');
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

    //全部已读
    $("#doRead-btn").click(function () {
        var _this = $(this);
        var checkStatus = table.checkStatus('unReadMessageTable'); //参数id设定的值
        if (checkStatus.data.length > 0) {
            var dataIdArray = new Array();
            for (var j = 0; j < checkStatus.data.length; j++) {
                var dataId = checkStatus.data[j].id;
                dataIdArray.push(dataId);
            }
            if (dataIdArray.length == 0) {
                layer.msg("请选择要操作的记录行");
                return;
            }
            $.ajax({
                type: "post",
                url: "/message/readUserNotifyMessageBatch",
                data: JSON.stringify({
                    'userMessageIds': dataIdArray,
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
                            searchMessage(index, 0);
                        }, 500);
                    } else {
                        setTimeout(function () {
                            layer.msg(data.msg);
                        }, 500);
                    }
                },
                error: function (e) {
                    setTimeout(function () {
                        layer.msg("未知异常!");
                    }, 1000);
                }
            });
        } else {
            layer.msg("请选择要已读的记录行");
        }

    });

})

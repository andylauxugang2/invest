'use strict'; //ECMAScript5 严格模式,JSON

layui.config({
    base: "res/mods/"
}).use(['form', 'layer', 'jquery'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        laypage = layui.laypage,
        $ = layui.jquery;

    //全局的ajax访问，处理ajax清求时session超时
    $.ajaxSetup({
        contentType:"application/x-www-form-urlencoded;charset=utf-8",
        complete:function(XMLHttpRequest,textStatus){
            var httpStatus = XMLHttpRequest.status;
            if(httpStatus == xmlhttprequest_status_session_timeout){
                parent.parent.location.href = "/login";
            }
            //alert(XMLHttpRequest.readyState);
        }
    });

    //自定义验证规则
    form.verify({
        amountStart: function (value) {
            if (value.length == 0) {
                return '请输入起始投标金额';
            }
            if (value.length >= 8) {
                return '起始投标金额输入过大,请不要超过8位数字';
            }
        },
        amountMax: function (value) {
            if (value.length == 0) {
                return '请输入最大投标金额';
            }
            if (value.length >= 8) {
                return '最大投标金额输入过大,请不要超过8位数字';
            }
        },
        accountRemain: function (value) {
            if (value.length == 0) {
                return '请输入账户保留金额';
            }
            if (value.length >= 8) {
                return '账户保留金额输入过大,请不要超过8位数字';
            }
        }
        //, phone: [/^1[3|4|5|7|8]\d{9}$/, '手机必须11位，只能是数字！']
        //, email: [/^[a-z0-9._%-]+@([a-z0-9-]+\.)+[a-z]{2,4}$|^1[3|4|5|7|8]\d{9}$/, '邮箱格式不对']
    });

    //监听提交
    form.on('submit(addUserMainPolicy)', function (data) {
        layer.confirm('确定保存主策略设置吗？', {icon: 3, title: '提示信息'}, function (index) {
            var index = layer.msg('保存中，请稍候', {icon: 16, time: false, shade: 0.8});
            /*layer.alert(JSON.stringify(data.field), {
             title: '最终的提交信息'
             });*/
            var paramStr = JSON.stringify(data.field); //把一个对象转成一个序列化的JSON字符串
            $.ajax({
                type: "post",
                url: "/policy/saveUserMainPolicy",
                data: paramStr,
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                dataType: "json",
                success: function (data) {
                    if (data.code == 0) {
                        if(data.data){
                            //写回到页面 下次用作更新操作
                            setTimeout(function () {
                                layer.close(index);
                                layer.msg("保存主策略设置成功");
                            }, 500);
                        }
                    } else {
                        setTimeout(function () {
                            layer.close(index);
                            layer.msg(data.msg);
                        }, 500);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    layer.close(index);
                    layer.msg("未知异常!");
                }
            });
        });
        return false;
    });
})

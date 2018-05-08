'use strict'; //ECMAScript5 严格模式,JSON

layui.config({
    base: "res/mods/"
}).use(['form', 'layer', 'upload', 'laydate'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery;
    var upload = layui.upload;

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
    var index0 = null;
    /*layui.upload({
        url: "/user/changeHeadImg/" + userId,
        ext: 'jpg|png|gif',
        before: function(input){
            //返回的参数item，即为当前的input DOM对象
            index0 = layer.msg('头像上传中，请稍候...', {icon: 16, time: false, shade: 0.8});
        },
        success: function (res) {
            $('#userFace').attr('src', 'data:image/png;base64,' + res.data.imgOrgBase64);
            $('#topUserFace', window.parent.document).attr('src', 'data:image/png;base64,' + res.data.imgOrgBase64);
            window.sessionStorage.setItem('userFace', res.data.imgOrgBase64);
            setTimeout(function () {
                layer.close(index0);
                layer.msg("头像修改成功");
            }, 500);
        }
    });*/

    layui.use('upload', function(){
        var upload = layui.upload;
        //执行实例
        var uploadInst = upload.render({
            elem: '#changeHeadImgBtn', //绑定元素
            field: 'userFace',
            url: "/user/changeHeadImg/" + userId, //上传接口
            accept: 'images',
            exts: 'png|jpg|gif',//允许上传的文件后缀
            //drag: true,
            size: '2000',//设置文件可允许上传的最大值，单位 KB。不支持ie8/9
            before: function(input){
                //返回的参数item，即为当前的input DOM对象
                index0 = layer.msg('头像上传中，请稍候...', {icon: 16, time: false, shade: 0.8});
            },
            done: function(res){
                //上传完毕回调
                $('#userFace').attr('src', 'data:image/png;base64,' + res.data.imgOrgBase64);
                $('#topUserFace', window.parent.document).attr('src', 'data:image/png;base64,' + res.data.imgOrgBase64);
                window.sessionStorage.setItem('userFace', res.data.imgOrgBase64);
                setTimeout(function () {
                    layer.close(index0);
                    layer.msg("头像修改成功");
                }, 500);
            },
            error: function(){
                layer.close(index0);
                layer.msg("未知异常");
            }
        });
    });

    //自定义验证规则
    form.verify({
        nick: function (value) {
            if (value.length >= 45) {
                return '您输入的昵称过长,请重新输入';
            }
        }
    });

    //监听提交
    form.on('submit(setUser)', function (data) {
        var index = layer.msg('保存中，请稍候', {icon: 16, time: false, shade: 0.8});
        /*layer.alert(JSON.stringify(data.field), {
         title: '最终的提交信息'
         });*/
        var paramStr = JSON.stringify(data.field); //把一个对象转成一个序列化的JSON字符串
        $.ajax({
            type: "post",
            url: "/user/saveUserInfo",
            data: paramStr,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'CLIENT_PLATFORM': 3,
                'IVGATEWAY-SOURCE': 'USER_CONSOLE'
                //'filter-key': 'filter-header'
            },
            dataType: "json",
            success: function (data) {
                if (data.code == 0) {
                    setTimeout(function () {
                        layer.close(index);
                        layer.msg("保存个人信息成功");
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
})

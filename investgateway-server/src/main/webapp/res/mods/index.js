var global_navtab = null;

'use strict';
layui.use(['jquery', 'layer', 'element'], function () {
    window.jQuery = window.$ = layui.jquery;
    window.layer = layui.layer;
    var element = layui.element;

    // 左侧导航栏向左折叠
    $('.andy-side-menu').click(function () {
        var sideWidth = $('#andy-side').width();
        if (sideWidth === 200) {
            $('#andy-body').animate({
                left: '0'
            }); // admin-footer
            $('#andy-footer').animate({
                left: '0'
            });
            $('#andy-side').animate({
                width: '0'
            });
        } else {
            $('#andy-body').animate({
                left: '200px'
            });
            $('#andy-footer').animate({
                left: '200px'
            });
            $('#andy-side').animate({
                width: '200px'
            });
        }
    });

    $(function () {

    });

});

layui.config({
    base: '/res/mods/'
}).use(['jquery', 'element', 'layer', 'navtab'], function () {
    window.jQuery = window.$ = layui.jquery;
    window.layer = layui.layer;
    var element = layui.element;
    var navtab = layui.navtab({
        openTabNum: "5",  //最大可打开窗口数量
        elem: '.andy-tab-box'
    });

    global_navtab = navtab;

    // iframe自适应
    $(window).on('resize', function () {
        var $content = $('#andy-tab .layui-tab-content');
        $content.height($(this).height() - 163);
        $content.find('iframe').each(function () {
            $(this).height($content.height());
        });
        var tab_W = $('#andy-tab').width();
        // larry-footer：p-admin宽度设定
        var larryFoot = $('#andy-footer').width();
        $('#andy-footer p.p-admin').width(larryFoot - 300);
    }).resize();

    // 左侧菜单导航-->tab
    $(function () {
        $('#andy-nav-side').click(function () {
            if ($(this).attr('lay-filter') !== undefined) {
                $(this).children('ul').find('li').each(function () {
                    var $this = $(this);
                    if ($this.find('dl').length > 0) {
                        var $dd = $this.find('dd').each(function () {
                            $(this).click(function () {
                                var $a = $(this).children('a');
                                var href = $a.data('url');
                                var icon = $a.children('i:first').data('icon');
                                var title = $a.children('span').text();
                                var data = {
                                    href: href,
                                    icon: icon,
                                    title: title
                                }
                                navtab.tabAdd(data);
                            });
                        });
                    } else {
                        $this.click(function () {
                            var $a = $(this).children('a');
                            var href = $a.data('url');
                            var icon = $a.children('i:first').data('icon');
                            var title = $a.children('span').text();
                            var data = {
                                href: href,
                                icon: icon,
                                title: title
                            }
                            navtab.tabAdd(data);
                        });
                    }
                });
            }
        })
    });

    // top 页面 添加新窗口
    $(function () {
        /*$(".andy-header-nav .layui-nav .layui-nav-item a").click(function () {
         });*/
        $(".andy-header-nav").children('ul').find('li').each(function () {
            var $this = $(this);
            if ($this.find('dl').length > 0) {
                var $dd = $this.find('dd').each(function () {
                    $(this).click(function () {
                        var $a = $(this).children('a');
                        var href = $a.data('url');
                        var icon = $a.children('i:first').data('icon');
                        var title = $a.children('span').text();
                        var data = {
                            href: href,
                            icon: icon,
                            title: title
                        }
                        navtab.tabAdd(data);
                    });
                });
            } else {
                $this.click(function () {
                    var $a = $(this).children('a');
                    var href = $a.data('url');

                    if (!href) {
                        return;
                    }
                    var icon = $a.children('i:first').data('icon');
                    if (!icon) {
                        icon = $a.data('icon');
                    }
                    var title = $a.children('span').text();
                    var data = {
                        href: href,
                        icon: icon,
                        title: title
                    }
                    navtab.tabAdd(data);
                });
            }
        });
    });

    var showWelcomeTipData = $('#showWelcomeTipData').val();
    if (showWelcomeTipData && eval(showWelcomeTipData.toLowerCase())) {
        //第一步
        layer.tips('Hi,亲爱的，欢迎您加入标专家，使用前请先按第一步：在【第三方信息管理】中去授权第三方账户', $('#gotoUserAuthHref'), {
            tips: [1, '#3595CC'],
            time: 0,
            closeBtn: [0, true],
            shade: 0.8,
            fix: true,
            zIndex: 19891014,
            end: function () {

                //第二步
                layer.tips('第二步：在【标专家策略管理】【主策略】中开启并设置主策略', $('#gotoMainPolicyHref'), {
                    tips: [1, '#3595CC'],
                    time: 0,
                    closeBtn: [0, true],
                    shade: 0.8,
                    fix: true,
                    zIndex: 19891014,
                    end: function () {
                        //第三步
                        layer.tips('第三步：在【标专家策略管理】【系统推荐散标】中选择系统散标并开启', $('#gotoSysLoanPolicyHref'), {
                            tips: [1, '#3595CC'],
                            time: 0,
                            closeBtn: [0, true],
                            shade: 0.8,
                            fix: true,
                            zIndex: 19891014,
                            end: function () {
                                layer.tips('恭喜您学会了使用标专家,子策略开启之后,系统以最快的速度按照你的策略自动投标,投标结果在【投资管理】【投资记录查询】中进行查看', $('#viewLoanRecordHref'), {
                                    tips: [1, '#3595CC'],
                                    time: 0,
                                    closeBtn: [0, true],
                                    shade: 0.8,
                                    fix: true,
                                    zIndex: 19891014,
                                    end: function () {
                                        //提醒
                                        layer.tips('日常使用过程中请关注【专家提醒】，祝您旅途愉快！', $('#zhuanjiaNoticeHref'), {
                                            tips: [1, '#3595CC'],
                                            time: 0,
                                            closeBtn: [0, true],
                                            shade: 0.8,
                                            fix: true,
                                            zIndex: 19891014
                                        });
                                    }
                                });
                            }
                        });
                    }
                });

            }
        });
    }


});
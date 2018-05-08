'use strict'; //ECMAScript5 严格模式,JSON

layui.config({
    base: "res/mods/"
}).use(['form', 'layer', 'jquery', 'laypage', 'element'], function () {
    var element = layui.element;
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        laypage = layui.laypage,
        $ = layui.jquery;

    /*layer.tips('我是另外一个tips，只不过我长得跟之前那位稍有些不一样。', '#sysloanHover', {
     tips: [1, '#3595CC'],
     time: 0
     });*/

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

    $('#closeInfo').on('click', function () {
        //$('#infoSwitch').hide()
    });

    $('.panel .tools .iconpx-chevron-down').click(function () {
        var el = $(this).parents(".panel").children(".panel-body");
        if ($(this).hasClass("iconpx-chevron-down")) {
            $(this).removeClass("iconpx-chevron-down").addClass("iconpx-chevron-up");
            el.slideUp(200)
        } else {
            $(this).removeClass("iconpx-chevron-up").addClass("iconpx-chevron-down");
            el.slideDown(200)
        }
    });

    $('#shortcut section a').on('click', function () {
        var title = $(this).data('title');
        var href = $(this).data('href');
        var icon = $(this).data('icon');
        var data = {href: href, icon: icon, title: title};
        if (!href) {
            return;
        }
        parent.global_navtab.tabAdd(data);
    });

    /*$(window).on('resize', function () {
     var myChart = echarts.init(document.getElementById('larry-seo-stats'));
     var option = {
     title: {text: '投资策略收益率分析统计', subtext: '最近一个月', x: 'center'},
     tooltip: {trigger: 'item', formatter: "{a} <br/>{b} : {c} ({d}%)"},
     legend: {orient: 'vertical', left: 'left', data: ['系统散标', '用户自定义', '债权', '跟踪', '自定义债权']},
     series: [{
     name: '访问来源',
     type: 'pie',
     radius: '55%',
     center: ['50%', '60%'],
     data: [{value: 335, name: '系统散标'}, {value: 310, name: '用户自定义'}, {value: 234, name: '债权'}, {
     value: 135,
     name: '跟踪'
     }, {value: 1548, name: '自定义债权'}],
     itemStyle: {emphasis: {shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.5)'}}
     }]
     };
     myChart.setOption(option)
     }).resize();*/
    form.render()
})

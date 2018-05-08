'use strict'; //ECMAScript5 严格模式,JSON

layui.config({
    base: "res/mods/"
}).use(['form', 'layer', 'jquery', 'element'], function () {
    var element = layui.element;
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
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

    var userId = $('#userIdData').val();

    if (!userId) {
        layer.msg("页面基础参数获取失败,请重新登录或联系客服解决");
        return;
    }

    /*var index = layer.msg('页面加载中，请稍候...', {icon: 16, time: false, shade: 0.8});

     //加载页面数据

     setTimeout(function () {
     layer.close(index);
     }, 1000);*/


    //自定义验证规则
    var monthBeginVal, rateBeginVal, amountBeginVal, ageBeginVal, loanerSuccessCountBeginVal, wasteCountBeginVal, normalCountBeginVal, overdueLessCountBeginVal,
        overdueMoreCountBeginVal,totalPrincipalBeginVal,owingPrincipalBeginVal,amountToReceiveBeginVal,lastSuccessBorrowDaysBeginVal,registerBorrowMonthsBeginBeginVal,
        amountOwingTotalBeginVal,owingHighestDebtRatioBeginVal,amtDebtRatBgVal;
    form.verify({
        monthBegin: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return " 月份只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 36) {
                return '起始月份超出限制,请输入36以内月份';
            }
            monthBeginVal = parseInt(value);
        },
        monthEnd: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "月份只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 36) {
                return '截止月份超出限制,请输入36以内月份';
            }
            if (monthBeginVal && value && parseInt(value) < monthBeginVal) {
                return "截止月份必须大于开始月份";
            }
        },
        rateBegin: function (value) {
            if (!value) {
                return;
            }
            var pattern = /^\d+(\.\d+)?$/;
            if (!pattern.test(value)) {
                return "利率只能填写数字";
            }
            value = parseFloat(value);
            if (value < 0 || value > 100) {
                return '起始利率超出限制,请输入1-100以内利率';
            }
            rateBeginVal = parseFloat(value);
        },
        rateEnd: function (value) {
            if (!value) {
                return;
            }
            var pattern = /^\d+(\.\d+)?$/;
            if (!pattern.test(value)) {
                return "利率只能填写数字";
            }
            value = parseFloat(value);
            if (value < 0 || value > 100) {
                return '截止利率超出限制,请输入1-100以内利率';
            }
            if (rateBeginVal && value && parseFloat(value) < rateBeginVal) {
                return "截止利率必须大于起始利率";
            }
        },
        amountBegin: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "借款金额只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 10000000) {
                return '起始借款金额超出限制';
            }
            amountBeginVal = parseInt(value);
        },
        amountEnd: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "借款金额只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value.length > 10000000) {
                return '最大借款金额超出限制';
            }
            if (amountBeginVal && value && parseInt(value) <= amountBeginVal) {
                return "最大借款金额必须大于起始借款金额";
            }
        },
        ageBegin: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "年龄只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 150) {
                return '起始年龄超出限制';
            }
            ageBeginVal = value;
        },
        ageEnd: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "年龄只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 150) {
                return '最大年龄超出限制';
            }
            if (ageBeginVal && value && value <= ageBeginVal) {
                return "最大年龄必须大于起始年龄";
            }
        },
        loanerSuccessCountBegin: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "成功借款次数只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 10000) {
                return '起始成功借款次数超出限制';
            }
            loanerSuccessCountBeginVal = value;
        },
        loanerSuccessCountEnd: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "成功借款次数只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 10000) {
                return '最大成功借款次数超出限制';
            }
            if (loanerSuccessCountBeginVal && value && value <= loanerSuccessCountBeginVal) {
                return "最大成功借款次数必须大于起始成功借款次数";
            }
        },
        wasteCountBegin: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "流标次数只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 10000) {
                return '起始流标次数超出限制';
            }
            wasteCountBeginVal = value;
        },
        wasteCountEnd: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "流标次数只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 10000) {
                return '最大流标次数超出限制';
            }
            if (wasteCountBeginVal && value && value <= wasteCountBeginVal) {
                return "最大流标次数必须大于起始流标次数";
            }
        },
        normalCountBegin: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "正常还款次数只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 100000) {
                return '起始正常还款次数超出限制';
            }
            normalCountBeginVal = value;
        },
        normalCountEnd: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "正常还款次数只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 100000) {
                return '最大正常还款次数超出限制';
            }
            if (normalCountBeginVal && value && value <= normalCountBeginVal) {
                return "最大正常还款次数必须大于起始正常还款次数";
            }
        },
        overdueLessCountBegin: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "逾期(1-15)还清次数只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 10000) {
                return '起始逾期(1-15)还清次数超出限制';
            }
            overdueLessCountBeginVal = value;
        },
        overdueLessCountEnd: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "逾期(1-15)还清次数只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 10000) {
                return '最大逾期(1-15)还清次数超出限制';
            }
            if (overdueLessCountBeginVal && value && value <= overdueLessCountBeginVal) {
                return "最大逾期(1-15)还清次数必须大于起始逾期(1-15)还清次数";
            }
        },
        overdueMoreCountBegin: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "逾期(15天以上)还清次数只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 10000) {
                return '逾期(15天以上)还清次数超出限制';
            }
            overdueMoreCountBeginVal = value;
        },
        overdueMoreCountEnd: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "逾期(15天以上)还清次数只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 10000) {
                return '逾期(15天以上)还清次数超出限制';
            }
            if (overdueMoreCountBeginVal && value && value <= overdueMoreCountBeginVal) {
                return "最大逾期(15天以上)还清次数必须大于起始次数";
            }
        },
        totalPrincipalBegin: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "起始累计借款金额只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 10000000) {
                return '起始累计借款金额超出限制';
            }
            totalPrincipalBeginVal = value;
        },
        totalPrincipalEnd: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "最大累计借款金额只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 10000000) {
                return '最大累计借款金额超出限制';
            }
            if (totalPrincipalBeginVal && value && value <= totalPrincipalBeginVal) {
                return "最大累计借款金额必须大于起始金额";
            }
        },
        owingPrincipalBegin: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "起始待还金额只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 10000000) {
                return '起始待还金额超出限制';
            }
            owingPrincipalBeginVal = value;
        },
        owingPrincipalEnd: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "最大待还金额只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 10000000) {
                return '最大待还金额超出限制';
            }
            if (owingPrincipalBeginVal && value && value <= owingPrincipalBeginVal) {
                return "最大待还金额必须大于起始金额";
            }
        },
        amountToReceiveBegin: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "起始待收金额只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 10000000) {
                return '起始待收金额超出限制';
            }
            amountToReceiveBeginVal = value;
        },
        amountToReceiveEnd: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "最大待收金额只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 10000000) {
                return '最大待收金额超出限制';
            }
            if (amountToReceiveBeginVal && value && value <= amountToReceiveBeginVal) {
                return "最大待收金额必须大于起始金额";
            }
        },
        amountOwingTotalBegin: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "起始借款总额度只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 10000000) {
                return '起始借款总额度超出限制';
            }
            amountOwingTotalBeginVal = value;
        },
        amountOwingTotalEnd: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "最大借款总额度只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 10000000) {
                return '最大借款总额度超出限制';
            }
            if (amountOwingTotalBeginVal && value && value <= amountOwingTotalBeginVal) {
                return "最大借款总额度必须大于起始额度";
            }
        },

        lastSuccessBorrowDaysBegin: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "起始距最后一次借款成功天数只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 10000) {
                return '起始距最后一次借款成功天数超出限制';
            }
            lastSuccessBorrowDaysBeginVal = value;
        },
        lastSuccessBorrowDaysEnd: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "最大距最后一次借款成功天数只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 10000) {
                return '最大距最后一次借款成功天数超出限制';
            }
            if (lastSuccessBorrowDaysBeginVal && value && value <= lastSuccessBorrowDaysBeginVal) {
                return "最大距最后一次借款成功天数必须大于起始天数";
            }
        },

        registerBorrowMonthsBegin: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "起始本次借款距注册时间月数只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 10000) {
                return '起始本次借款距注册时间月数超出限制';
            }
            registerBorrowMonthsBeginBeginVal = value;
        },
        registerBorrowMonthsEnd: function (value) {
            var pattern = /^\d*$/;
            if (!pattern.test(value)) {
                return "最大本次借款距注册时间月数只能填写数字";
            }
            value = parseInt(value);
            if (value < 0 || value > 10000) {
                return '最大本次借款距注册时间月数超出限制';
            }
            if (registerBorrowMonthsBeginBeginVal && value && value <= registerBorrowMonthsBeginBeginVal) {
                return "最大本次借款距注册时间月数必须大于起始月数";
            }
        },

        owingHighestDebtRatioBegin: function (value) {
            if (!value) {
                return;
            }
            var pattern = /^\d+(\.\d+)?$/;
            if (!pattern.test(value)) {
                return "起始待还金额/历史最高负债只能填写数字";
            }
            value = parseFloat(value);
            if (value < 0 || value > 1) {
                return '起始待还金额/历史最高负债超出限制,请输入0-1以内数字';
            }
            owingHighestDebtRatioBeginVal = parseFloat(value);
        },
        owingHighestDebtRatioEnd: function (value) {
            if (!value) {
                return;
            }
            var pattern = /^\d+(\.\d+)?$/;
            if (!pattern.test(value)) {
                return "最大待还金额/历史最高负债只能填写数字";
            }
            value = parseFloat(value);
            if (value < 0 || value > 1) {
                return '最大待还金额/历史最高负债超出限制,请输入0-1以内数字';
            }
            if (owingHighestDebtRatioBeginVal && value && parseFloat(value) < owingHighestDebtRatioBeginVal) {
                return "最大待还金额/历史最高负债必须大于起始待还金额/历史最高负债";
            }
        },
        amtDebtRatBg: function (value) {
            if (!value) {
                return;
            }
            var pattern = /^\d+(\.\d+)?$/;
            if (!pattern.test(value)) {
                return "起始本次借款/历史最高负债只能填写数字";
            }
            value = parseFloat(value);
            if (value < 0 || value > 1) {
                return '起始本次借款/历史最高负债超出限制,请输入0-1以内数字';
            }
            amtDebtRatBgVal = parseFloat(value);
        },
        amtDebtRatEd: function (value) {
            if (!value) {
                return;
            }
            var pattern = /^\d+(\.\d+)?$/;
            if (!pattern.test(value)) {
                return "最大本次借款/历史最高负债只能填写数字";
            }
            value = parseFloat(value);
            if (value < 0 || value > 1) {
                return '最大本次借款/历史最高负债超出限制,请输入0-1以内数字';
            }
            if (amtDebtRatBgVal && value && parseFloat(value) < amtDebtRatBgVal) {
                return "最大本次借款/历史最高负债必须大于起始本次借款/历史最高负债";
            }
        }
        //, phone: [/^1[3|4|5|7|8]\d{9}$/, '手机必须11位，只能是数字！']
        //, email: [/^[a-z0-9._%-]+@([a-z0-9-]+\.)+[a-z]{2,4}$|^1[3|4|5|7|8]\d{9}$/, '邮箱格式不对']
    });

    //监听提交
    form.on('submit(addUserPolicy)', function (data) {

        var paramJson = data.field;
        var creditCodeArray = $('.creditCode input[type="checkbox"][name="creditCode"]:checked');
        var creditCodeList = new Array;
        for (var i = 0; i < creditCodeArray.length; i++) {
            creditCodeList.push(creditCodeArray[i].value)
        }
        paramJson.creditCode = creditCodeList;

        var certificateArray = $('.certificate input[type="checkbox"][name="certificate"]:checked');
        var certificateList = new Array;
        for (var i = 0; i < certificateArray.length; i++) {
            certificateList.push(certificateArray[i].value)
        }
        paramJson.certificate = certificateList;

        var studyStyleArray = $('.studyStyle input[type="checkbox"][name="studyStyle"]:checked');
        var studyStyleList = new Array;
        for (var i = 0; i < studyStyleArray.length; i++) {
            studyStyleList.push(studyStyleArray[i].value)
        }
        paramJson.studyStyle = studyStyleList;

        var graduateSchoolTypeArray = $('.graduateSchoolType input[type="checkbox"][name="graduateSchoolType"]:checked');
        var graduateSchoolTypeList = new Array;
        for (var i = 0; i < graduateSchoolTypeArray.length; i++) {
            graduateSchoolTypeList.push(graduateSchoolTypeArray[i].value)
        }
        paramJson.graduateSchoolType = graduateSchoolTypeList;

        var thirdAuthInfoArray = $('.thirdAuthInfo input[type="checkbox"][name="thirdAuthInfo"]:checked');
        var thirdAuthInfoList = new Array;
        for (var i = 0; i < thirdAuthInfoArray.length; i++) {
            thirdAuthInfoList.push(thirdAuthInfoArray[i].value)
        }
        paramJson.thirdAuthInfo = thirdAuthInfoList;

        /*var userThirdBindInfoArray = $('.userThirdBindInfoList input[type="checkbox"][name="userThirdBindInfoList"]:checked');
         var userThirdBindInfoList = new Array;
         for (var i = 0; i < userThirdBindInfoArray.length; i++) {
         userThirdBindInfoList.push(userThirdBindInfoArray[i].value)
         }
         if (userThirdBindInfoList.length == 0) {
         layer.msg("请至少选择一个第三方授权账号", {shade: 0.1});
         return;
         }
         paramJson.userThirdBindInfoList = userThirdBindInfoList;*/
        paramJson.userId = userId;
        paramJson.policyType = policy_type_userloan;

        var showPolicy = getShowPolicy(paramJson);
        var formData = data;
        //配置一个透明的询问框
        layer.msg('请您确认设置的散标自定义策略:<br/>' + showPolicy, {
            time: 200000, //200s后自动关闭
            //area: ['500px', '300px']
            btn: ['已确认,可以保存', '有问题,需要马上修改'],
            yes: function () {
                var index = layer.msg('保存中，请稍候', {icon: 16, time: false, shade: 0.8});
                $.ajax({
                    type: "post",
                    url: "/policy/addLoanPolicy",
                    data: JSON.stringify(paramJson),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    dataType: "json",
                    success: function (data) {
                        if (data.code == 0) {
                            var id = data.data.id;
                            var optType = data.data.optType;
                            $("#userLoanPolicyId").val(id);
                            var msg = optType == "insert" ? "添加" : "更新";
                            setTimeout(function () {
                                layer.close(index);
                                layer.msg(msg + "自定义成功");
                                //刷新保存项 TODO
                            }, 500);
                        } else {
                            setTimeout(function () {
                                layer.close(index);
                                layer.msg(data.msg, {shade: 0.8});
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

        return false;
    });

    function getShowPolicy(paramJson) {
        var resultHTML = '<fieldset class="layui-elem-field">';
        resultHTML += '<legend>' + '策略:' + paramJson.userPolicyName + '</legend>';
        resultHTML += '<div class="layui-field-box" style="font-size: 12px;">';
        //resultHTML += '单笔投资金额:' + paramJson.bidAmount + '<br/>';
        //resultHTML += '第三方账户:' + paramJson.userThirdBindInfoList + '<br/>';
        var month;
        if (paramJson.monthBegin && paramJson.monthEnd) month = paramJson.monthBegin + '-' + paramJson.monthEnd;
        else if (paramJson.monthBegin) month = '>=' + paramJson.monthBegin;
        else if (paramJson.monthEnd) month = '<=' + paramJson.monthEnd;
        if (month) resultHTML += '期限:' + month + '<br/>';
        var rate;
        if (paramJson.rateBegin && paramJson.rateEnd) rate = paramJson.rateBegin + '-' + paramJson.rateEnd;
        else if (paramJson.rateBegin) rate = '>=' + paramJson.rateBegin;
        else if (paramJson.rateEnd) rate = '<=' + paramJson.rateEnd;
        if (rate) resultHTML += '利率:' + rate + '<br/>';
        var amount;
        if (paramJson.amountBegin && paramJson.amountEnd) amount = paramJson.amountBegin + '-' + paramJson.amountEnd;
        else if (paramJson.amountBegin) amount = '>=' + paramJson.amountBegin;
        else if (paramJson.amountEnd) amount = '<=' + paramJson.amountEnd;
        if (amount) resultHTML += '借款金额:' + amount + '<br/>';
        var creditCodeArray = $('.creditCode input[type="checkbox"][name="creditCode"]:checked');
        if (creditCodeArray && creditCodeArray.length > 0) {
            var creditCode = new Array;
            for (var i = 0; i < creditCodeArray.length; i++) {
                creditCode.push(creditCodeArray[i].title);
            }
            resultHTML += '魔镜等级:' + creditCode + '<br/>';
        }
        var age;
        if (paramJson.ageBegin && paramJson.ageEnd) age = paramJson.ageBegin + '-' + paramJson.ageEnd;
        else if (paramJson.ageBegin) age = '>=' + paramJson.ageBegin;
        else if (paramJson.ageEnd) age = '<=' + paramJson.ageEnd;
        if (age) resultHTML += '年龄:' + age + '<br/>';
        var sex = paramJson.sex;
        if (sex == "1") sex = "男"; else if (sex == '0') sex = "女"; else sex = "包含全部";
        resultHTML += '性别:' + sex + '<br/>';
        var certificateArray = $('.certificate input[type="checkbox"][name="certificate"]:checked');
        if (certificateArray && certificateArray.length > 0) {
            var certificate = new Array;
            for (var i = 0; i < certificateArray.length; i++) {
                certificate.push(certificateArray[i].title);
            }
            resultHTML += '学历认证:' + certificate + '<br/>';
        }
        var studyStyleArray = $('.studyStyle input[type="checkbox"][name="studyStyle"]:checked');
        if (studyStyleArray && studyStyleArray.length > 0) {
            var studyStyle = new Array;
            for (var i = 0; i < studyStyleArray.length; i++) {
                studyStyle.push(studyStyleArray[i].title);
            }
            resultHTML += '学习形式:' + studyStyle + '<br/>';
        }
        var graduateSchoolTypeArray = $('.graduateSchoolType input[type="checkbox"][name="graduateSchoolType"]:checked');
        if (graduateSchoolTypeArray && graduateSchoolTypeArray.length > 0) {
            var graduateSchoolType = new Array;
            for (var i = 0; i < graduateSchoolTypeArray.length; i++) {
                graduateSchoolType.push(graduateSchoolTypeArray[i].title);
            }
            resultHTML += '学校等级:' + graduateSchoolType + '<br/>';
        }
        var thirdAuthInfoArray = $('.thirdAuthInfo input[type="checkbox"][name="thirdAuthInfo"]:checked');
        if (thirdAuthInfoArray && thirdAuthInfoArray.length > 0) {
            var thirdAuthInfo = new Array;
            for (var i = 0; i < thirdAuthInfoArray.length; i++) {
                thirdAuthInfo.push(thirdAuthInfoArray[i].title);
            }
            resultHTML += '第三方认证信息:' + thirdAuthInfo + '<br/>';
        }

        var loanerSuccess;
        if (paramJson.loanerSuccessCountBegin && paramJson.loanerSuccessCountEnd) loanerSuccess = paramJson.loanerSuccessCountBegin + '-' + paramJson.loanerSuccessCountEnd;
        else if (paramJson.loanerSuccessCountBegin) loanerSuccess = '>=' + paramJson.loanerSuccessCountBegin;
        else if (paramJson.loanerSuccessCountEnd) loanerSuccess = '<=' + paramJson.loanerSuccessCountEnd;
        if (loanerSuccess) resultHTML += '成功借款次数:' + loanerSuccess + '<br/>';

        var wasteCount;
        if (paramJson.wasteCountBegin && paramJson.wasteCountEnd) wasteCount = paramJson.wasteCountBegin + '-' + paramJson.wasteCountEnd;
        else if (paramJson.wasteCountBegin) wasteCount = '>=' + paramJson.wasteCountBegin;
        else if (paramJson.wasteCountEnd) wasteCount = '<=' + paramJson.wasteCountEnd;
        if (wasteCount) resultHTML += '流标次数:' + wasteCount + '<br/>';

        var normalCount;
        if (paramJson.normalCountBegin && paramJson.normalCountEnd) normalCount = paramJson.normalCountBegin + '-' + paramJson.normalCountEnd;
        else if (paramJson.normalCountBegin) normalCount = '>=' + paramJson.normalCountBegin;
        else if (paramJson.normalCountEnd) normalCount = '<=' + paramJson.normalCountEnd;
        if (normalCount) resultHTML += '正常还款次数:' + normalCount + '<br/>';

        var overdueLessCount;
        if (paramJson.overdueLessCountBegin && paramJson.overdueLessCountEnd) overdueLessCount = paramJson.overdueLessCountBegin + '-' + paramJson.overdueLessCountEnd;
        else if (paramJson.overdueLessCountBegin) overdueLessCount = '>=' + paramJson.overdueLessCountBegin;
        else if (paramJson.overdueLessCountEnd) overdueLessCount = '<=' + paramJson.overdueLessCountEnd;
        if (overdueLessCount) resultHTML += '逾期(1-15)还清次数:' + overdueLessCount + '<br/>';

        var overdueMoreCount;
        if (paramJson.overdueMoreCountBegin && paramJson.overdueMoreCountEnd) overdueMoreCount = paramJson.overdueMoreCountBegin + '-' + paramJson.overdueMoreCountEnd;
        else if (paramJson.overdueMoreCountBegin) overdueMoreCount = '>=' + paramJson.overdueMoreCountBegin;
        else if (paramJson.overdueMoreCountEnd) overdueMoreCount = '<=' + paramJson.overdueMoreCountEnd;
        if (overdueMoreCount) resultHTML += '逾期(15天以上)还清次数:' + overdueMoreCount + '<br/>';

        var totalPrincipal;
        if (paramJson.totalPrincipalBegin && paramJson.totalPrincipalEnd) totalPrincipal = paramJson.totalPrincipalBegin + '-' + paramJson.totalPrincipalEnd;
        else if (paramJson.totalPrincipalBegin) totalPrincipal = '>=' + paramJson.totalPrincipalBegin;
        else if (paramJson.totalPrincipalEnd) totalPrincipal = '<=' + paramJson.totalPrincipalEnd;
        if (totalPrincipal) resultHTML += '累计借款金额:' + totalPrincipal + '<br/>';

        var owingPrincipal;
        if (paramJson.owingPrincipalBegin && paramJson.owingPrincipalEnd) owingPrincipal = paramJson.owingPrincipalBegin + '-' + paramJson.owingPrincipalEnd;
        else if (paramJson.owingPrincipalBegin) owingPrincipal = '>=' + paramJson.owingPrincipalBegin;
        else if (paramJson.owingPrincipalEnd) owingPrincipal = '<=' + paramJson.owingPrincipalEnd;
        if (owingPrincipal) resultHTML += '待还金额:' + owingPrincipal + '<br/>';

        var amountToReceive;
        if (paramJson.amountToReceiveBegin && paramJson.amountToReceiveEnd) amountToReceive = paramJson.amountToReceiveBegin + '-' + paramJson.amountToReceiveEnd;
        else if (paramJson.amountToReceiveBegin) amountToReceive = '>=' + paramJson.amountToReceiveBegin;
        else if (paramJson.amountToReceiveEnd) amountToReceive = '<=' + paramJson.amountToReceiveEnd;
        if (amountToReceive) resultHTML += '待收金额:' + amountToReceive + '<br/>';

        var amountOwingTotal;
        if (paramJson.amountOwingTotalBegin && paramJson.amountOwingTotalEnd) amountOwingTotal = paramJson.amountOwingTotalBegin + '-' + paramJson.amountOwingTotalEnd;
        else if (paramJson.amountOwingTotalBegin) amountOwingTotal = '>=' + paramJson.amountOwingTotalBegin;
        else if (paramJson.amountOwingTotalEnd) amountOwingTotal = '<=' + paramJson.amountOwingTotalEnd;
        if (amountOwingTotal) resultHTML += '借款总额度:' + amountOwingTotal + '<br/>';

        var lastSuccessBorrowDays;
        if (paramJson.lastSuccessBorrowDaysBegin && paramJson.lastSuccessBorrowDaysEnd) lastSuccessBorrowDays = paramJson.lastSuccessBorrowDaysBegin + '-' + paramJson.lastSuccessBorrowDaysEnd;
        else if (paramJson.lastSuccessBorrowDaysBegin) lastSuccessBorrowDays = '>=' + paramJson.lastSuccessBorrowDaysBegin;
        else if (paramJson.lastSuccessBorrowDaysEnd) lastSuccessBorrowDays = '<=' + paramJson.lastSuccessBorrowDaysEnd;
        if (lastSuccessBorrowDays) resultHTML += '距最后一次借款成功天数:' + lastSuccessBorrowDays + '<br/>';

        var registerBorrowMonths;
        if (paramJson.registerBorrowMonthsBegin && paramJson.registerBorrowMonthsEnd) registerBorrowMonths = paramJson.registerBorrowMonthsBegin + '-' + paramJson.registerBorrowMonthsEnd;
        else if (paramJson.registerBorrowMonthsBegin) registerBorrowMonths = '>=' + paramJson.registerBorrowMonthsBegin;
        else if (paramJson.registerBorrowMonthsEnd) registerBorrowMonths = '<=' + paramJson.registerBorrowMonthsEnd;
        if (registerBorrowMonths) resultHTML += '距最后一次借款成功天数:' + registerBorrowMonths + '<br/>';

        var owingHighestDebtRatio;
        if (paramJson.owingHighestDebtRatioBegin && paramJson.owingHighestDebtRatioEnd) owingHighestDebtRatio = paramJson.owingHighestDebtRatioBegin + '-' + paramJson.owingHighestDebtRatioEnd;
        else if (paramJson.owingHighestDebtRatioBegin) owingHighestDebtRatio = '>=' + paramJson.owingHighestDebtRatioBegin;
        else if (paramJson.owingHighestDebtRatioEnd) owingHighestDebtRatio = '<=' + paramJson.owingHighestDebtRatioEnd;
        if (owingHighestDebtRatio) resultHTML += '待还金额/历史最高负债:' + owingHighestDebtRatio + '<br/>';

        var amtDebtRat;
        if (paramJson.amtDebtRatBg && paramJson.amtDebtRatEd) amtDebtRat = paramJson.amtDebtRatBg + '-' + paramJson.amtDebtRatEd;
        else if (paramJson.amtDebtRatBg) amtDebtRat = '>=' + paramJson.amtDebtRatBg;
        else if (paramJson.amtDebtRatEd) amtDebtRat = '<=' + paramJson.amtDebtRatEd;
        if (amtDebtRat) resultHTML += '本次借款/历史最高负债:' + amtDebtRat + '<br/>';

        resultHTML += '</div>';
        resultHTML += '</fieldset>';
        return resultHTML;
    }

})

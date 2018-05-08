<#import "../common.ftl" as common>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>捉宝网-标专家</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="keywords" content="ppd,拍拍贷,投资,理财">
    <meta name="description" content="标专家为广大投资用户提供自动投标，全面服务于拍拍贷平台投资者">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="format-detection" content="telephone=no">
<@common.includeCommonCss/>
    <link rel="stylesheet" href="/res/css/user/accountinfo.css">
</head>

<body class="childrenBody">
<input id="userIdData" value="${userId}" hidden="true"/>

<div class="layui-tab layui-tab-card"  lay-filter="accountTabFilter">
    <ul class="layui-tab-title">
        <li class="layui-this">充值</li>
    <#--<li>VIP套餐</li>-->
        <li>交易查询</li>
    <#--<li>代金券</li>-->
    </ul>
    <div class="layui-tab-content" style="height: auto;">
        <div class="layui-tab-item layui-show">
            <div>
                <fieldset class="layui-elem-field layui-field-title">
                    <legend class="legend">账户余额</legend>
                    <div>
                        <div class="layui-form" name="a" action="">
                            <div class="layui-form-item">
                                <label class="layui-form-label form-label"><a href="#">捉宝币</a>：</label>
                                <div class="layui-input-block">
                                    <div class="layui-form-mid">
                                        <a id="zhuobaobiBalance" class="balance">获取中...</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="layui-form" name="a" action="">
                            <div class="layui-form-item">
                                <label class="layui-form-label form-label"><a href="#">剩余可投金额</a>：</label>
                                <div class="layui-input-block">
                                    <div class="layui-form-mid">
                                        <a id="bidAmountBalance" class="small_balance">获取中...</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </fieldset>
                <fieldset class="layui-elem-field layui-field-title">
                    <legend class="legend">充值捉宝币</legend>
                    <div class="layui-field-box">
                        <div>
                            <form class="layui-form" action="">
                                <div class="layui-form-item">
                                    <label class="layui-form-label form-label" >捉宝币数：</label>
                                    <div class="layui-input-block">
                                        <input type="radio" name="buyCountType" value="1" lay-filter="buyCountFilter" title='100捉宝币<span class="price">+20&nbsp;&nbsp;</span>'>
                                        <input type="radio" name="buyCountType" value="2" lay-filter="buyCountFilter" title='200捉宝币<span class="price">+40</span>'>
                                        <input type="radio" name="buyCountType" value="3" lay-filter="buyCountFilter" title='300捉宝币<span class="price">+60</span>'>
                                        <input type="radio" name="buyCountType" value="4" lay-filter="buyCountFilter" title='400捉宝币<span class="price">+80</span>'>
                                        <input type="radio" name="buyCountType" value="5" lay-filter="buyCountFilter" title='500捉宝币<span class="price">+100</span>' checked>
                                        <input type="radio" name="buyCountType" value="6" lay-filter="buyCountFilter" title='800捉宝币<span class="price">+160</span>'>
                                        <input type="radio" name="buyCountType" value="10" lay-filter="buyCountFilter" title='其他数目'>
                                    </div>
                                </div>
                                <div class="layui-form-item form-item-display">
                                    <div class="layui-inline">
                                        <label class="layui-form-label form-label"></label>
                                        <div class="layui-input-inline"  style="width: 100px;">
                                            <input type="text" id="buyCountOther" name="buyCountOther" lay-verify="buyCountOther" placeholder="捉宝币个数"
                                                   autocomplete="off" class="layui-input" value="">
                                        </div>
                                        <div class="layui-form-mid more_price">+充值捉宝币数目的20%（要求：充值捉宝币>=100）</div>
                                    </div>
                                </div>

                                <div class="layui-form-item">
                                    <div class="layui-inline">
                                        <label class="layui-form-label form-label">需要支付金额：</label>
                                        <div class="layui-form-mid"><span id="price" class="price">500</span>元<div>[<span id="buyCount" class="more_price">800</span>个捉宝币]</div></div>
                                        <div class="layui-form-mid layui-word-aux"><i class="iconfont icon-anquanshezhi1" style="margin-right: 3px;"></i>1个捉宝币=1元钱，1个捉宝币累计可投500元理财产品</div>
                                    </div>
                                </div>
                                <hr class="layui-bg-gray">
                                <div class="layui-form-item">
                                    <div class="layui-inline">
                                        <label class="layui-form-label form-label">支付方式：</label>
                                        <div class="layui-input-inline">
                                            <input type="radio" name="payway" value="1" title="&nbsp;" checked>
                                            <label class="layui-form-label bank-alipay"></label>
                                        </div>
                                        <div class="layui-input-inline">
                                            <input type="radio" name="payway" value="2" title="&nbsp;">
                                            <label class="layui-form-label bank-wx"></label>
                                        </div>
                                    </div>
                                </div>

                                <div class="layui-form-item" style="margin-top: 10px;">
                                    <div class="layui-input-block agree">
                                        <input type="checkbox" id="agree" name="agree" lay-filter="agreeFilter"
                                               title='<span style="font-size: 12px;">我已了解：目前充值仅支持线下结算，请核对上面的支付二维码，避免错误转账，请您先加好友后转账，确保谨慎选择充值/付款账号。 </span>' value="" lay-skin="primary">
                                    <#--<input type="checkbox" id="agree" name="agree" lay-filter="agreeFilter"
                                           title='<span style="font-size: 12px;">我已了解：捉宝币目前给固定支付宝账号转账，线下结算手工充币，请您谨慎选择充值/付款账号。 <a href="#" style="margin-top:-4px;color:#1E90FF;">更多详情</a></span>' value="" lay-skin="primary">-->
                                    </div>
                                </div>
                            <#--<div class="layui-form-item" style="margin-top: 10px;">
                                <div class="layui-input-block">
                                    <button id="btnPayOver" class="layui-btn layui-btn-warm layui-btn-disabled" lay-submit="" lay-filter="payOverFilter">支付完，点我</button>
                                </div>
                            </div>-->
                            </form>
                        </div>
                    </div>
                    <hr class="layui-bg-gray">

                </fieldset>
            </div>

        </div>
        <div class="layui-tab-item">
            <blockquote class="layui-elem-quote news_search">
                <div class="layui-inline">
                    <div class="layui-form-mid layui-word-aux">
                        <i class="iconfont icon-tags" style="margin-right: 5px;"></i><b>温馨提示:</b>
                        <span>亲爱的拍客，充值完毕后可查询系统为您创建的订单，查询到订单号后，如有问题可咨询客服并提供唯一订单号进行跟踪问题并为您实时解决</span>
                    </div>
                </div>
                <div class="layui-inline">
                    <form class="layui-form" action="" style="myform">
                        <div class="layui-inline">
                            <div class="layui-input-inline">
                                <select name="orderType" lay-filter="orderTypeSelectFilter" id="orderTypeSelect" lay-search="" class="order-type-select">
                                    <option value="">产品类型</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-inline">
                            <div class="layui-input-inline">
                                <select name="payStatus" lay-filter="payStatusSelectFilter" id="payStatusSelect" lay-search="" class="paystatus-select">
                                    <option value="">支付状态</option>
                                </select>
                            </div>
                        </div>
                        <div class="layui-inline">
                            <input type="text" class="layui-input" id="orderBeginTime" placeholder="生单起始时间">
                        </div>
                        <div class="layui-inline">
                            <input type="text" class="layui-input" id="orderEndTime" placeholder="生单结束时间">
                        </div>
                        <div class="layui-inline">
                            <a class="layui-btn layui-btn-normal" id="searchOrder-btn">查询订单</a>
                        </div>
                    </form>
                </div>

            </blockquote>

            <table class="layui-table" id="orderTable"></table>
        </div>
    </div>
</div>

</body>
</html>

<@common.includeCommonJs/>
<script src="/res/mods/user/accountinfo.js"></script>

<script type="text/html" id="orderDetailTpl">
    <!-- 订单详情 -->
    <a class="orderDetail" style="color:#1E90FF;text-decoration:none;cursor:pointer" data-id="{{d.id}}">{{d.orderNo}}</a>
</script>

<script type="text/html" id="priceTpl">
    <span style="color: #FF7200;">{{d.price}}</span>
</script>
<script type="text/html" id="payPriceTpl">
    <span style="color: #FF7200;">{{d.payPrice}}</span>
</script>
<script type="text/html" id="payStatusTpl">
    <span style="color: green;">{{d.payStatus}}</span>
</script>
<script type="text/html" id="orderStatusTpl">
    <span style="color: green;">{{d.orderStatus}}</span>
</script>

</body>

</html>

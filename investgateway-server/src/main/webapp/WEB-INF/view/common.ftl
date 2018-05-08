<#macro includeCommonCss>
<link rel="stylesheet" href="/res/layui/css/layui.css">
<link rel="stylesheet" href="/res/css/global.css">
</#macro>
<#macro includeCommonJs>
<script src="/res/layui/layui.js"></script>
<script src="/res/layui/layui.all.js"></script>
<script src="/res/mods/constant.js"></script>
</#macro>
<#--
<#macro breadcrumb crumbList>
	<div style="margin:15px; ">
		<span class="layui-breadcrumb">
			<#list crumbList as item>
				<#if item?is_last>
					<a href="${item.href!''}">
						<cite>
							${item.title!''}
						</cite>
					</a>
				<#else>
					<a href="${item.href!''}">
						${item.title!''}
					</a>
				</#if>
			</#list>
		</span>
	</div>
</#macro>-->

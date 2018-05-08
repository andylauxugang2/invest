package com.invest.ivgateway.freemarker.model;

import com.invest.ivppad.common.PPDBinExpConstants;
import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * FreeMarker自定义宏
 * Created by xugang on 2017/8/27.do best.
 */
public class BinExpressDirective implements TemplateDirectiveModel {
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        // 检查参数是否传入
        if (params.isEmpty()) {
            throw new TemplateModelException("This directive doesn't allow parameters.");
        }
        /*if (loopVars.length != 0) {
            throw new TemplateModelException("This directive doesn't allow loop variables.");
        }*/
        Long flag = ((SimpleNumber)params.get("flag")).getAsNumber().longValue();
        Long testMatch = ((SimpleNumber)params.get("testMatch")).getAsNumber().longValue();

        boolean result = PPDBinExpConstants.testFlagMatch(flag, testMatch);

        env.setVariable("appRankList", ObjectWrapper.BEANS_WRAPPER.wrap(result));
        if (body != null) {
            body.render(env.getOut());
        }
        /*// 是否有非空的嵌入内容
        if (body != null) {
            // 执行嵌入体部分，和FTL中的<#nested>一样，除了
            body.render(new UpperCaseFilterWriter(env.getOut()));
        } else {
            throw new RuntimeException("missing body");
        }*/
    }

    /**
     * {@link Writer}改变字符流到大写形式，
     * 而且把它发送到另外一个{@link Writer}中。
     */
    private static class UpperCaseFilterWriter extends Writer {
        private final Writer out;

        UpperCaseFilterWriter(Writer out) {
            this.out = out;
        }

        public void write(char[] cbuf, int off, int len) throws IOException {
            char[] transformedCbuf = new char[len];
            for (int i = 0; i < len; i++) {
                transformedCbuf[i] = Character.toUpperCase(cbuf[i + off]);
            }
            out.write(transformedCbuf);
        }

        public void flush() throws IOException {
            out.flush();
        }

        @Override
        public void close() throws IOException {
            out.close();
        }
    }
}

package com.invest.ivbatch.listeners;

import org.springframework.batch.core.SkipListener;

/**
 * Created by xugang on 2017/8/5.
 */
public class TestFirstJobSkipListener implements SkipListener {

    @Override
    public void onSkipInRead(Throwable throwable) {
        System.out.println("skip的异常信息" + throwable);
    }

    @Override
    public void onSkipInWrite(Object o, Throwable throwable) {
        System.out.println("InWrite时skip,o=" + o + ",异常信息="+ throwable);
    }

    @Override
    public void onSkipInProcess(Object o, Throwable throwable) {
        System.out.println("InProcess时skip,o=" + o + ",异常信息="+ throwable);
    }
}

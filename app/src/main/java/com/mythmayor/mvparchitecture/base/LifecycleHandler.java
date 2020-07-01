package com.mythmayor.mvparchitecture.base;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * Created by mythmayor on 2020/6/30.
 * 自定义Handler，实现LifecycleObserver进行生命周期监听，会自动在onDestroy里移除消息，避免产生内存泄漏
 */
public class LifecycleHandler extends Handler implements LifecycleObserver {

    private LifecycleOwner lifecycleOwner;

    public LifecycleHandler(final LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
        addObserver();
    }

    public LifecycleHandler(final Callback callback, final LifecycleOwner lifecycleOwner) {
        super(callback);
        this.lifecycleOwner = lifecycleOwner;
        addObserver();
    }

    public LifecycleHandler(final Looper looper, final LifecycleOwner lifecycleOwner) {
        super(looper);
        this.lifecycleOwner = lifecycleOwner;
        addObserver();
    }

    public LifecycleHandler(final Looper looper, final Callback callback, final LifecycleOwner lifecycleOwner) {
        super(looper, callback);
        this.lifecycleOwner = lifecycleOwner;
        addObserver();
    }

    private void addObserver() {
        notNull(lifecycleOwner);
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy() {
        removeCallbacksAndMessages(null);
        lifecycleOwner.getLifecycle().removeObserver(this);
    }

    private void notNull(Object o) {
        notNull(o, "Can't be null!");
    }

    private void notNull(Object o, @NonNull String msg) {
        if (o == null) {
            throw new NullPointerException(msg);
        }
    }
}

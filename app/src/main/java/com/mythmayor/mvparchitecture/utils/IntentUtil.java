package com.mythmayor.mvparchitecture.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mythmayor.mvparchitecture.MyConstant;

/**
 * Created by mythmayor on 2020/6/30.
 * Activity跳转工具类
 */
public class IntentUtil {

    public static void startActivity(Context context, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivity(intent);
    }

    public static void startActivityWithExtra(Context context, Bundle bundle, Class<?> cls) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(context, cls);
        context.startActivity(intent);
    }

    public static void startActivityClearTask(Context context, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startActivityClearTaskWithExtra(Context context, Bundle bundle, Class<?> cls) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startInDevelopmentActivity(Context context, String title, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(MyConstant.INTENT_EXTRA_TITLE, title);
        context.startActivity(intent);
    }

    public static void startHtml5Activity(Context context, String title, String url, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(MyConstant.INTENT_EXTRA_TITLE, title);
        intent.putExtra(MyConstant.INTENT_EXTRA_URL, url);
        context.startActivity(intent);
    }

    public static void startActivityByPackage(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }
}

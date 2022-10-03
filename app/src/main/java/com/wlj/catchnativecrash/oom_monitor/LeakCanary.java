package com.wlj.catchnativecrash.oom_monitor;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 当前类的注释: 监控activity和fragment 有没有释放
 * 作者：WangLiJian on 2022/10/1.
 * 邮箱：wanglijian1214@gmail.com
 */
public class LeakCanary {
    // 监控 activity 和 fragment 有没有被释放
    // onDestroy 是来观测这个对象有没有回收，被动方式
    public static final void init(Application application){
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks(){
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                // 观察当前对象有没有被回收
                Watcher.getInstance().addWatch(activity);
            }
        });
    }

}

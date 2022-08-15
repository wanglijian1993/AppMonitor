package com.wlj.catchnativecrash.app_crash;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public final class ActivityManager {
    private static final String LOG_TAG = "ActivityManager";
    private volatile boolean isForeground = true;

    @NonNull
    private final WeakStack<Activity> activityStack = new WeakStack<>();

    /**
     * Create and register a new instance.
     */
    public ActivityManager() {
        LifecycleCallback.getInstance().register(new IForeBackInterface() {
            @Override
            public void onForeground(@NonNull Activity activity) {
                isForeground = true;
            }

            @Override
            public void onBackground(@NonNull Activity activity) {
                isForeground = false;
            }

            @Override
            public void onCreate(@NonNull Activity activity) {
                activityStack.add(activity);
            }

            @Override
            public void onResume(@NonNull Activity activity) {
            }

            @Override
            public void onStop(@NonNull Activity activity) {
            }

            @Override
            public void onDestroy(@NonNull Activity activity) {
                synchronized (activityStack) {
                    activityStack.remove(activity);
                    activityStack.notify();
                }
            }
        });
    }

    /**
     * 从集合中取出最后一个activity返回.
     *
     * @return last created activity, if any
     */
    @Nullable
    public Activity getLastActivity() {
        return activityStack.peek();
    }

    /**
     * 获取所有未销毁activity的集合.
     *
     * @return a list of activities in the current process
     */
    @NonNull
    public List<Activity> getLastActivities() {
        return new ArrayList<>(activityStack);
    }

    /**
     * clear saved activities.
     */
    public void clearLastActivities() {
        activityStack.clear();
    }

    /**
     * wait until the last activity is stopped.
     *
     * @param timeOutInMillis timeout for wait
     */
    public void waitForAllActivitiesDestroy(int timeOutInMillis) {
        synchronized (activityStack) {
            long start = System.currentTimeMillis();
            long now = start;
            while (!activityStack.isEmpty() && start + timeOutInMillis > now) {
                try {
                    activityStack.wait(start - now + timeOutInMillis);
                } catch (InterruptedException ignored) {
                    Log.w(LOG_TAG, "activityStack wait may be error");
                }
                now = System.currentTimeMillis();
            }
            Log.i(LOG_TAG, "now killed all activities.");
        }
    }

    /**
     * 判断crash时Activity的前后台状态
     *
     * @return true为前台，false为后台
     */
    public boolean isForeground() {
        return isForeground;
    }
}

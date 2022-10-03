package com.wlj.catchnativecrash;

import android.app.Activity;
import android.app.Application;

import com.wlj.catchnativecrash.oom_monitor.LeakCanary;

import java.util.ArrayList;
import java.util.List;

/**
 * 当前类的注释:
 * 作者：WangLiJian on 2022/10/1.
 * 邮箱：wanglijian1214@gmail.com
 */
public class MyApplication extends Application {
   private List<Activity> mCurrentActivity = new ArrayList<>();
   public static MyApplication application;
   @Override
   public void onCreate() {
      super.onCreate();
      application=this;
      LeakCanary.init(this);
   }

   public void addCurrentActivity(Activity activity){
      mCurrentActivity.add(activity);
   }

   public Activity getCurrentActivity() {
      return mCurrentActivity.get(0);
   }
}


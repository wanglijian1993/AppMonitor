package com.wlj.catchnativecrash.oom_monitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wlj.catchnativecrash.MyApplication;
import com.wlj.catchnativecrash.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 当前类的注释:
 * 作者：WangLiJian on 2022/10/2.
 * 邮箱：wanglijian1214@gmail.com
 */
public class OomActivity extends AppCompatActivity {

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_oom);
      MyApplication application = (MyApplication) getApplication();
      application.addCurrentActivity(this);
   }
   public void onClick(View view) {
      Intent intent = new Intent(this, OomActivity.class);
      startActivity(intent);
   }

   @Override
   protected void onDestroy() {
      super.onDestroy();
      // 但是对象不一定会被释放，其实我们肯定是希望回收
   }
}

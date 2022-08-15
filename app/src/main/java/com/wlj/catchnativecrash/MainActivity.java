package com.wlj.catchnativecrash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wlj.catchnativecrash.app_crash.CrashMonitor;
import com.wlj.catchnativecrash.databinding.ActivityMainBinding;
import com.wlj.catchnativecrash.native_crash.CrashHandlerListener;
import com.wlj.catchnativecrash.native_crash.NativeCrashMonitor;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    private ActivityMainBinding binding;

    private NativeCrashMonitor nativeCrashMonitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        tv.setOnClickListener(this::onClick);
        // 在首页的空闲时候触发 捕获异常
        CrashMonitor.getInstance().init(getApplication());
        nativeCrashMonitor=new NativeCrashMonitor();
        nativeCrashMonitor.init(new CrashHandlerListener() {
            @Override
            public void onCrash(String sthreadName, Error error) {
                Log.d("111","threadName:"+sthreadName+"---msg:"+error.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        nativeCrashMonitor.nativeCrash();
//        nativeCrashMonitor.testCrashHandlerCallback(new CrashHandlerListener() {
//            @Override
//            public void onCrash(String sthreadName, Error error) {
//                Log.e("wlj","threadName:"+sthreadName+"--error:"+error);
//            }
//        });

    }




}
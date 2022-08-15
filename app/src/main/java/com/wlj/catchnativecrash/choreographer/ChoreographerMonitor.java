package com.wlj.catchnativecrash.choreographer;

import android.util.Log;
import android.view.Choreographer;

public class ChoreographerMonitor {

    private long nowTime=1;
    private int sm=1;
    private int smResult=60;


    public void start(){
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                Choreographer.getInstance().postFrameCallback(this::doFrame);

                //当前的帧率，如果有掉帧堆栈信息怎么拿，怎么解决掉帧问题
                plusSM();
            }
        });
    }


    private void plusSM(){

        long t=System.currentTimeMillis();
        if(nowTime==1){
            nowTime=t;
        }
        if(nowTime/1000==t/1000){
            sm++;
        }else if(t/1000 - nowTime/1000>=1){
            smResult=sm;
            Log.e("TAG","smResult -> "+smResult);
            sm = 1;
            nowTime = t;
        }

    }
}

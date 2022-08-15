package com.wlj.catchnativecrash.native_crash;

public interface CrashHandlerListener {


    void onCrash(String sthreadName,Error error);

}

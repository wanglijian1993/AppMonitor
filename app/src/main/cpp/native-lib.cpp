#include <jni.h>
#include <string>
#include <unistd.h>
#include "SignalHandler.h"
#include "JNIBridge.h"
#include "CrashAnalyser.h"

extern "C"
JNIEXPORT void JNICALL
Java_com_wlj_catchnativecrash_native_1crash_NativeCrashMonitor_nativeInit(JNIEnv *env, jobject nativeCrashMonitor,
                                                             jobject callback) {
    //  主要是把 callback 保存起来，方便监听监听到异常时回调给 java 层
    // 做一个线程的监听
    callback = env->NewGlobalRef(callback);
    JavaVM *javaVm;
    env->GetJavaVM(&javaVm);
    //为了避免子线程为空的情况
    jclass nativeCrashMonitorClass=env->GetObjectClass(nativeCrashMonitor);
    nativeCrashMonitorClass=(jclass)env->NewGlobalRef(nativeCrashMonitorClass);
    jclass jclass_error= env->FindClass("java/lang/Error");
    jclass_error=(jclass)env->NewGlobalRef(jclass_error);
    JNIBridge *jniBridge = new JNIBridge(javaVm, callback,nativeCrashMonitorClass,jclass_error);
    // 创建一个线程去监听是否有异常
    initCondition();
    pthread_t pthread;
    int ret = pthread_create(&pthread, nullptr, threadCrashMonitor, jniBridge);
    if(ret){
        LOGE("pthread_create error, ret: %d", ret);
    }
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wlj_catchnativecrash_native_1crash_NativeCrashMonitor_nativeSetup(JNIEnv *env, jobject thiz) {
    // 设置监听信号量回调处理
    installSignalHandlers();
    // 设置额外的栈空间，让信号处理在单独的栈中处理
    installAlternateStack();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wlj_catchnativecrash_native_1crash_NativeCrashMonitor_nativeCrash(JNIEnv *env, jclass clazz) {
    int *num = (int*)0x100;// 0x0
    *num = 100;
}


extern "C"
JNIEXPORT void JNICALL
Java_com_wlj_catchnativecrash_native_1crash_NativeCrashMonitor_testCrashHandlerCallback(JNIEnv *env, jobject thiz,
                                                                          jobject callback) {
//
//    jclass error=env->FindClass("java/lang/Error");
//    jmethodID initId= env -> GetMethodID(error,"<init>","(Ljava/lang/String;)V");
//    jobject jerror=env->NewObject(error,initId,env->NewStringUTF("错误信息错误信息错误信息错误信息错误信息错误信息"));
//
//    const char *sig="(Ljava/lang/String;Ljava/lang/Error;)V";
//    jmethodID jmethodId= env->GetMethodID(env->GetObjectClass(callback), "onCrash", sig);
//
//    env->CallVoidMethod(callback,jmethodId,env->NewStringUTF("线程a"),jerror);


    const char* char1="abc";
    const char* char2="abc";
    const char* char3="abc";
    const char* char4="abc";
    char s1[10] = "abc";                        // 指针类型无法扩展长度，需标识为数组形式
    char *s2 = "def";

    snprintf(s1+3, strlen(s2)+1, "%s", s2);     // s1+3 = s1+3*sizeof(char)
    char charArrays[2048];

    strcat(charArrays,char1);
    strcat(charArrays,char2);
    strcat(charArrays,char3);
    strcat(charArrays,char4);
    strcat(charArrays,s1);
    char * tempNativeError;
    sprintf(tempNativeError,"native crash #%s", "1111");
    strcat(charArrays,tempNativeError);
    sprintf(tempNativeError,"native crash #%s", "2222");
    strcat(charArrays,tempNativeError);
    sprintf(tempNativeError,"native crash #%s", "3333");
    strcat(charArrays,tempNativeError);

    LOGE("charArrays:%s",charArrays);
}

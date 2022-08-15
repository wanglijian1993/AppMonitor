//
// Created by darrenzeng on 2021/3/7.
//
// 主要把异常往上抛
#include "JNIBridge.h"
#include <string>
#include <string.h>
using namespace std;
JNIBridge::JNIBridge(JavaVM *javaVm, jobject callbackObj, jclass nativeCrashMonitorClass, jclass error) {
    this->javaVm = javaVm;
    this->callbackObj = callbackObj;
    this->nativeCrashMonitorClass = nativeCrashMonitorClass;
    this->error=error;
}


void JNIBridge::throwException2Java(native_handler_context *handlerContext) {

    LOGD("throwException2Java");
    string errorMsg;
    // java 的线程名 -> 当前Java的堆栈，怎么实现？
    JNIEnv *env=NULL;
    if(this->javaVm->AttachCurrentThread(&env,NULL)!=JNI_OK){
        LOGE("AttachCurrentThread failed!");
    }
    // 这样写会有问题，这是一个坑，子线程中不能这么调用
    // jclass  nativeCrashMonitorClass = env->FindClass("com/darren/optimize/day06/NativeCrashMonitor");
    const char *sig= "(Ljava/lang/String;)Ljava/lang/String;";
    jmethodID getStackInfoByThreadNameId=env->GetStaticMethodID(this->nativeCrashMonitorClass,"getStackInfoByThreadName",sig);
    jstring  jThreadName=env->NewStringUTF(handlerContext->threadName);
    jobject javaStackInfo=env->CallStaticObjectMethod(this->nativeCrashMonitorClass,getStackInfoByThreadNameId,jThreadName);
    const char * javaExceptionStackInfo= env->GetStringUTFChars((jstring)javaStackInfo,JNI_FALSE);
    errorMsg+=javaExceptionStackInfo;
    LOGE("%s", javaExceptionStackInfo);
    // c++ 堆栈信息，重点讲一下，原理大家要：理解编译四步骤，so 加载到内存是怎样，C进阶的内容
    int frame_size=handlerContext->frame_size;
    for(int index=0;index<frame_size;++index){
        uintptr_t pc=handlerContext->frames[index];
        Dl_info info;
        void *const addr = (void*)(pc);
        if(dladdr(addr,&info)!=0 &&info.dli_fname!=NULL ){
            const uintptr_t near=(uintptr_t)info.dli_saddr;
            const uintptr_t  offs=pc-near;
            const uintptr_t addr_rel=pc-(uintptr_t)info.dli_fbase;
            const uintptr_t addr_to_use =is_so(info.dli_fname)?addr_rel:pc;
            // 没办法获得是哪一行，需要用 addr2line 工具根据 pc 值获取在哪一行
            // bugly 会上传一个地址堆栈配置文件
            errorMsg+=("native crash #%02lx pc 0x%016lx %s (%s+0x%lx)",index, addr_to_use, info.dli_fname, info.dli_sname, offs);
            LOGD("native crash #%02lx pc 0x%016lx %s (%s+0x%lx)", index, addr_to_use, info.dli_fname, info.dli_sname, offs);
        }

    }
    //把java异常堆栈和c++异常堆栈一起抛到回调监听函数
    jclass error=env->FindClass("java/lang/Error");
    jmethodID initId= env -> GetMethodID(error,"<init>","(Ljava/lang/String;)V");
    jobject jerror=env->NewObject(error,initId,env->NewStringUTF(errorMsg.data()));
    const char *sigCrash="(Ljava/lang/String;Ljava/lang/Error;)V";
    jmethodID jmethodId= env->GetMethodID(env->GetObjectClass(this->callbackObj), "onCrash", sigCrash);
    env->CallVoidMethod(this->callbackObj,jmethodId,env->NewStringUTF(handlerContext->threadName),jerror);


}


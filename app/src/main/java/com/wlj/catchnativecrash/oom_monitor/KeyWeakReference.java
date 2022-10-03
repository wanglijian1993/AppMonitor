package com.wlj.catchnativecrash.oom_monitor;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * 当前类的注释:
 * 作者：WangLiJian on 2022/10/1.
 * 邮箱：wanglijian1214@gmail.com
 */
class KeyWeakReference extends WeakReference<Object> {
    public String key;
    //线下做监控上报，误报率肯定要降低，这种方案可能某些机型有问题
    public KeyWeakReference(Object referent) {
        super(referent);
    }

    public KeyWeakReference(String key,Object referent, ReferenceQueue<? super Object> q) {
        //queue 的作用就是当触发 gc回收这个对象的时候会把该对象 push 到 queue
        super(referent, q);
        this.key=key;
    }
}

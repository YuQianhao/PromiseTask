package com.yuqianhao.promise.core;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Promise使用的线程池
 */
public class PromiseThreadPool extends ThreadPoolExecutor {


    private static PromiseThreadPool PROMISE_THREAD_POOL=null;

    public PromiseThreadPool(int corePoolSize, int maximumPoolSize) {
        super(corePoolSize, maximumPoolSize, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }

    /**
     * 配置一个线程池
     * @param corePoolSize      线程池核心线程数量
     * @param maximumPoolSize   线程池最大线程数量
     */
    public static void config(int corePoolSize, int maximumPoolSize) throws Exception {
        if(PROMISE_THREAD_POOL!=null){
            throw new Exception("Promise的线程池已经被初始化过一次了。");
        }
        PROMISE_THREAD_POOL=new PromiseThreadPool(corePoolSize,maximumPoolSize);
    }

    protected static PromiseThreadPool get(){
        if(PROMISE_THREAD_POOL==null){
            int availableProcessors=Runtime.getRuntime().availableProcessors();
            try {
                config(availableProcessors*2,
                        availableProcessors*4);
            } catch (Exception ignored) {
            }
        }
        return PROMISE_THREAD_POOL;
    }
}

package com.yuqianhao.promise;

import com.yuqianhao.promise.core.*;

/**
 * 异步任务控制器，通过这个控制器操作
 * 异步任务接口{@link com.yuqianhao.promise.core.IPromise}
 */
public class Promise {


    /**
     * 执行并立即返回异步任务的结果
     * @param task 任务执行过程
     * @param <ResultType> 任务执行完毕后返回的结果类型
     * @return 返回一个异步结果，当调用异步结果get时，会阻塞线程并等待返回
     */
    public static <ResultType> Result<ResultType> async(IPromiseTask<ResultType> task) {
        return PromiseImpl.getImpl().async(task);
    }

    /**
     * 立即执行一个异步任务，不需要获取返回值
     * @param runnable 任务执行过程
     */
    public static void async(Runnable runnable){
        PromiseImpl.getImpl().async(runnable);
    }

    /**
     * 执行一个任务单元，异步回调的方式获取结果
     * @param callback 当任务执行结束时会通过这个接口类回调执行结果
     * @param task 任务执行过程
     * @param <ResultType> 任务执行完毕后返回的结果类型
     */
    public static <ResultType> void async(IPromiseTask<ResultType> task,IAsyncResult<ResultType> callback){
        PromiseImpl.getImpl().async(callback,task);
    }

    /**
     * 同时异步执行多个异步任务，并且等待所有异步任务结果返回后返回
     * @param task 多个任务执行过程
     * @return 所有任务异步执行结束后的结果返回组，返回的结果顺序和任务参数顺序一直
     */
    public static PromiseArrayResult all(IRunnableTask... task){
        return PromiseImpl.getImpl().all(task);
    }

    /**
     * 配置一个线程池
     * @param corePoolSize      线程池核心线程数量
     * @param maximumPoolSize   线程池最大线程数量
     */
    public static void config(int corePoolSize, int maximumPoolSize) throws Exception {
        PromiseThreadPool.config(corePoolSize,maximumPoolSize);
    }
}

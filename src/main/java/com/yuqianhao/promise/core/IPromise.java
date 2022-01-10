package com.yuqianhao.promise.core;

public interface IPromise {

    /**
     * 立即执行一个异步任务，不需要获取返回值
     * @param runnable 任务执行过程
     */
    void async(Runnable runnable);

    /**
     * 执行并立即返回异步任务的结果
     * @param task 任务执行过程
     * @param <ResultType> 任务执行完毕后返回的结果类型
     * @return 返回一个异步结果，当调用异步结果get时，会阻塞线程并等待返回
     */
    <ResultType> Result<ResultType> async(IPromiseTask<ResultType> task) ;

    /**
     * 执行一个任务单元，异步回调的方式获取结果
     * @param callback 当任务执行结束时会通过这个接口类回调执行结果
     * @param task 任务执行过程
     * @param <ResultType> 任务执行完毕后返回的结果类型
     */
    <ResultType> void async(IAsyncResult<ResultType> callback, IPromiseTask<ResultType> task);

    /**
     * 同时异步执行多个异步任务，并且等待所有异步任务结果返回后返回
     * @param task 多个任务执行过程
     * @return 所有任务异步执行结束后的结果返回组，返回的结果顺序和任务参数顺序一直
     */
    PromiseArrayResult all(IRunnableTask ...task);

}

package com.yuqianhao.promise.core;

/**
 * Promise异步任务的返回值
 * @param <ResultType>
 */
public interface IAsyncResult<ResultType> {

    /**
     * 异步任务执行完毕并且执行成功未发生日常的回调
     * @param result 异步任务执行结束的结果
     */
    void onCall(ResultType result);


}

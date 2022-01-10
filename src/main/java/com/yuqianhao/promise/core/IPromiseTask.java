package com.yuqianhao.promise.core;

/**
 * Promise的单一任务接口
 * 相对于{@link IRunnableTask}，这个接口能够直接明确
 * 返回值的类型，更适用于单一的异步任务。
 * @param <ResponseType> 任务返回结果的类型
 */
public interface IPromiseTask<ResponseType> {

    ResponseType run() ;

}

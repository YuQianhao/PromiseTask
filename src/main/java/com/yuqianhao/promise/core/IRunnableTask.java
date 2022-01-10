package com.yuqianhao.promise.core;

/**
 * Promise的任务接口，该接口只适用于多任务并行时的接口
 * 相对{@link IPromiseTask}这个任务接口不限定单一的
 * 返回值类型，更适用于多并行任务。
 */
public interface IRunnableTask {

    Object run();

}

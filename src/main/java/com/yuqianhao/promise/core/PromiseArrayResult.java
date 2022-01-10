package com.yuqianhao.promise.core;

import java.util.concurrent.CountDownLatch;

/**
 * 批量异步任务的返回结果，这个返回结果能将
 * 所有在同一个执行批次的任务组中有顺序的
 * 组装返回结果，适用于{@link IPromise#all(IRunnableTask...)}
 * 方法的返回值。
 */
public class PromiseArrayResult {

    /**
     * 所有任务的执行结果数组
     */
    protected final Object[] results;

    /**
     * 任务结果总量
     */
    public final int allCount;

    /**
     * 当前已经完成的任务结果数量
     */
    private int completeCount;

    /**
     * 运行状态
     */
    private boolean running;

    /**
     * 运行完成
     */
    private boolean done;


    private final CountDownLatch countDownLatch;


    public PromiseArrayResult(Object[] results, int allCount) {
        this.results = results;
        this.allCount = allCount;
        this.countDownLatch=new CountDownLatch(allCount);
    }

    /**
     * 同步获取任务组任务执行的结果，这个结果会按照任务
     * 通过{@link IPromise#all(IRunnableTask...)}方法
     * 添加的顺序返回。
     * 这个方法是有在所有任务全部执行完毕后才能返回，如果
     * 有任务没有执行完毕，会等待当前线程，直到任务完成
     * @return 返回所有的任务结果
     * @throws InterruptedException 线程等待引起的异常
     */
    public Object[] get() throws InterruptedException {
        if (!done) {
            countDownLatch.await();
        }
        return results;
    }

    /**
     * 获取任务组中所有的任务数量
     * @return 任务组中所有的任务数量
     */
    public int getTaskCount(){
        return allCount;
    }

    /**
     * 获取任务组已经完成的任务的数量
     * @return 已经完成的任务数量
     */
    public int getCompleteCount() {
        return completeCount;
    }

    protected synchronized void addCompleteCount() {
        this.completeCount ++;
        this.countDownLatch.countDown();
    }

    /**
     * 判断任务组是否还在继续工作
     * @return 任务组继续工作的状态
     */
    public boolean isRunning() {
        return running;
    }

    protected void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * 判断任务组是否已经完成工作
     * @return 任务组完成工作的状态
     */
    public boolean isDone() {
        return done;
    }

    protected void setDone(boolean done) {
        this.done = done;
    }
}

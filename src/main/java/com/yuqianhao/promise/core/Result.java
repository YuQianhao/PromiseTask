package com.yuqianhao.promise.core;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Promise任务的返回结果
 * @param <ResultType>
 */
public class Result<ResultType> {

    /**
     * 返回的结果
     */
    private ResultType result;

    /**
     * 运行状态
     */
    private boolean running;

    /**
     * 任务完成状态
     */
    private boolean done;

    private CountDownLatch countDownLatch;

    private final ReentrantLock reentrantLock;

    public Result() {
        reentrantLock=new ReentrantLock();
    }

    /**
     * 获取执行结果，如果当前的任务执行结束了，结果会立即返回，
     * 否则将会等待任务结束后将结果返回。
     * @return 返回任务之行的结果
     * @throws InterruptedException 此方法使用了线程等待的实现方式，可能会在等待时发生异常
     */
    public ResultType get() throws InterruptedException {
        reentrantLock.lock();
        if (!done) {
            countDownLatch = new CountDownLatch(1);
            reentrantLock.unlock();
            countDownLatch.await();
        }
        return result;
    }

    protected void setResult(ResultType result) {
        reentrantLock.lock();
        this.result = result;
        if(countDownLatch!=null && countDownLatch.getCount()!=0){
            countDownLatch.countDown();
        }
        reentrantLock.unlock();
    }

    /**
     * 任务是否正在运行
     * @return 返回任务运行状态
     */
    public boolean isRunning() {
        return running;
    }

    protected void setRunning(boolean running) {
        reentrantLock.lock();
        this.running = running;
        reentrantLock.unlock();
    }

    /**
     * 任务是否已经执行结束
     * @return 返回任务执行结束状态
     */
    public boolean isDone() {
        return done;
    }

    protected void setDone(boolean done) {
        reentrantLock.lock();
        this.done = done;
        reentrantLock.unlock();
    }


}

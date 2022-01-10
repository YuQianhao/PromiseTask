package com.yuqianhao.promise.core;

public class PromiseImpl implements IPromise{

    private final PromiseThreadPool promiseThreadPool=PromiseThreadPool.get();

    private static final IPromise PROMISE_IMPL=new PromiseImpl();

    public static IPromise getImpl(){
        return PROMISE_IMPL;
    }

    private PromiseImpl(){}


    @Override
    public void async(Runnable runnable) {
        promiseThreadPool.execute(runnable);
    }

    @Override
    public <ResultType> Result<ResultType> async(IPromiseTask<ResultType> task) {
        Result<ResultType> result=new Result<>();
        promiseThreadPool.execute(()->{
            result.setRunning(true);
            result.setDone(false);
            result.setResult(task.run());
            result.setRunning(false);
            result.setDone(true);
        });
        return result;
    }

    @Override
    public <ResultType> void async(IAsyncResult<ResultType> callback, IPromiseTask<ResultType> task) {
        promiseThreadPool.execute(()->{
            callback.onCall(task.run());
        });
    }

    @Override
    public PromiseArrayResult all(IRunnableTask... tasks) {
        PromiseArrayResult promiseAllResult=new PromiseArrayResult(new Object[tasks.length],tasks.length);
        for (int i = 0; i < tasks.length; i++) {
            final int _thread_index=i;
            final IRunnableTask task=tasks[i];
            promiseThreadPool.execute(()->{
                promiseAllResult.results[_thread_index]=task.run();
                promiseAllResult.addCompleteCount();
            });
        }
        return promiseAllResult;
    }
}

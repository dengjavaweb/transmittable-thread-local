package com.oldratlee.mtc.threadpool;

import com.oldratlee.mtc.MtContextCallable;
import com.oldratlee.mtc.MtContextRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author ding.lid
 */
public class ExecutorServiceMtcWrapper extends ExecutorMtcWrapper implements ExecutorService {
    final ExecutorService executorService;

    public ExecutorServiceMtcWrapper(ExecutorService executorService) {
        super(executorService);
        this.executorService = executorService;
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return executorService.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return executorService.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return executorService.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executorService.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return executorService.submit(MtContextCallable.get(task));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return executorService.submit(MtContextRunnable.get(task), result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return executorService.submit(MtContextRunnable.get(task));
    }

    private <T> List<Callable<T>> copy(Collection<? extends Callable<T>> tasks) {
        if (null == tasks) {
            return null;
        }
        List<Callable<T>> copy = new ArrayList<Callable<T>>();
        for (Callable<T> task : tasks) {
            copy.add(MtContextCallable.get(task));
        }
        return copy;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return executorService.invokeAll(copy(tasks));
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return invokeAll(copy(tasks), timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return invokeAny(copy(tasks));
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return invokeAny(copy(tasks), timeout, unit);
    }
}
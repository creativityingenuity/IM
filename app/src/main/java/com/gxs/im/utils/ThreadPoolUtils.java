package com.gxs.im.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池相关工具类
 * Call:vipggxs@163.com
 * Created by YT
 */

public class ThreadPoolUtils {
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    private static Executor sExecutor = Executors.newSingleThreadExecutor();

    public static void runOnSubThread(Runnable runnable){
        sExecutor.execute(runnable);
    }

    public static void runOnMainThread(Runnable runnable){
        sHandler.post(runnable);

    }
    private ThreadPoolUtils() {
    }

    public enum Type {
        FixedThread,
        CachedThread,
        SingleThread,
    }

    private ExecutorService exec;

    /**
     * 构造 初始化线程池
     *
     * @param type
     * @param tSize
     */
    public ThreadPoolUtils(Type type, int tSize) {
        switch (type) {
            case FixedThread:
                // 构造一个固定线程数目的线程池
                exec = Executors.newFixedThreadPool(tSize == 0 ? 3 : tSize);
                break;
            case SingleThread:
                // 构造一个单一线程的线程池
                exec = Executors.newSingleThreadExecutor();
                break;
            case CachedThread:
                // 造一个缓冲功能的线程池
                exec = Executors.newCachedThreadPool();
                break;
        }
    }

    /**
     * 执行一个异步任务
     *
     * @param runnable
     */
    public void runOnSubThread1(Runnable runnable) {
        exec.execute(runnable);
    }

  /*  *//**
     * 在主线程中执行
     * @param runnable
     *//*
    public void runOnMainThread(Runnable runnable) {
        handler.post(runnable);
    }*/

    /**
     * 待任务执行完毕后关闭线程池
     */
    public void shutDown() {
        exec.shutdown();
    }

    /**
     * 试图停止所有正在执行的活动任务
     * <p>试图停止所有正在执行的活动任务，暂停处理正在等待的任务，并返回等待执行的任务列表。</p>
     * <p>无法保证能够停止正在处理的活动执行任务，但是会尽力尝试。</p>
     *
     * @return 等待执行的任务的列表
     */
    public List<Runnable> shutDownNow() {
        return exec.shutdownNow();
    }

    /**
     * 判断线程池是否已关闭
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public boolean isShutDown() {
        return exec.isShutdown();
    }
}

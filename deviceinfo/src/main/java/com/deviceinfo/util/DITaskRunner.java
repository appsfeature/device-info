package com.deviceinfo.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DITaskRunner {
    private final Executor executor = Executors.newCachedThreadPool(); // change according to your requirements
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static DITaskRunner taskRunner ;

    private DITaskRunner() {
    }

    public static DITaskRunner getInstance(){
        if ( taskRunner == null ){
            taskRunner = new DITaskRunner();
        }
        return taskRunner ;
    }

    public interface Callback<R> {
        void onComplete(R result);
    }

    public interface CallbackWithError<R> {
        void onComplete(R result);
        void onError(Exception e);
    }

    public <R> void executeAsync(Callable<R> callable, Callback<R> callback) {
        executor.execute(() -> {
            try {
                final R result = callable.call();
                handler.post(() -> {
                    if (callback != null) {
                        callback.onComplete(result);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(DITaskRunner.class.getSimpleName(), e.toString());
                handler.post(() -> {
                    if (callback != null) {
                        callback.onComplete(null);
                    }
                });
            }
        });
    }

    public <R> void executeAsync(Callable<R> callable, CallbackWithError<R> callback) {
        executor.execute(() -> {
            try {
                final R result = callable.call();
                handler.post(() -> {
                    if (callback != null) {
                        callback.onComplete(result);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(DITaskRunner.class.getSimpleName(), e.toString());
                handler.post(() -> {
                    if (callback != null) {
                        callback.onComplete(null);
                    }
                });
            }
        });
    }

    public <R> void executeAsync(Callable<R> callable) {
        executor.execute(() -> {
            try {
                callable.call();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(DITaskRunner.class.getSimpleName(), e.toString());
            }
        });
    }

    public Handler getHandler() {
        return handler;
    }

    public Executor getExecutor() {
        return executor;
    }
}

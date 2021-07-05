package com.ciecursoandroid.abastecimentoeconomico.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class UtilsAsyncTask<Param, Progress, Result> {
    final Handler handler = new Handler(Looper.getMainLooper());

    public abstract Result doInBackground(Param param);

    public void onProgress(Progress progress) {

    }

    public void publishProgress(Progress progress) {
        handler.post(() -> onProgress(progress));
    }

    public abstract void onPostResult(Result result);

    public void execute(Param param) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            Result r = doInBackground(param);
            handler.post(() -> {
                onPostResult(r);
            });
        });
    }
}

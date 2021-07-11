package com.ciecursoandroid.abastecimentoeconomico.persistencia;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.ciecursoandroid.abastecimentoeconomico.models.Abaste;
import com.ciecursoandroid.abastecimentoeconomico.models.AbastecimentoComVeiculo;
import com.ciecursoandroid.abastecimentoeconomico.utils.UtilsAsyncTask;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AbastecimentoRepository {
    AppDBRoom db;

    public AbastecimentoRepository(Context context) {
        this.db = AppDBRoom.getInstance(context);
    }

    public void insert(Abaste abastecimento, OnInsert listener) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            Exception exception;

            @Override
            public void run() {
                try {
                    abastecimento.setDataAbastecimento(new Timestamp(System.currentTimeMillis()));
                    long id = db.abastecimentoDao().insert(abastecimento);
                    abastecimento.setId(id);
                } catch (Exception e) {
                    exception = e;
                    e.printStackTrace();
                }

                handler.post(() -> {
                    listener.onComplete(exception, abastecimento);
                });
            }
        });
    }

    public void delete(Abaste abastecimento, VeiculoRespository.OnDeleteListener listener) {
        new UtilsAsyncTask<Abaste, Void, Exception>() {
            @Override
            public Exception doInBackground(Abaste abastecimento) {
                try {
                    db.abastecimentoDao().delete(abastecimento);
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return e;
                }
            }

            @Override
            public void onPostResult(Exception e) {
                listener.onComplete(e);
            }
        }.execute(abastecimento);
    }

    public LiveData<List<Abaste>> getAll() {
        return db.abastecimentoDao().getAll();
    }

    public interface OnInsert {
        void onComplete(Exception e, Abaste abastecimento);
    }

    public LiveData<List<AbastecimentoComVeiculo>> getAbastecimentoComVeiculos() {
        return db.abastecimentoDao().getAbastecimentosComVeiculos();
    }
}

package com.ciecursoandroid.abastecimentoeconomico.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Handler;
import android.os.Looper;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.models.Veiculo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VeiculoRespository {
    AppDBRoom db;
    VeiculoDao dao;
    Context context;

    public VeiculoRespository(Context context) {
        this.db = AppDBRoom.getInstance(context);
        this.dao = this.db.veiculoDao();
    }

    public void insert(Veiculo veiculo, OnInsert listener) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            Exception ex;

            @Override
            public void run() {
                try {
                    long veiculoId = dao.insert(veiculo);
                    veiculo.setId(veiculoId);
                } catch (SQLiteConstraintException e) {
                    ex = e;
                    e.printStackTrace();
                } catch (Exception e) {
                    ex = e;
                    e.printStackTrace();
                }

                handler.post(() -> {
                    listener.onComplete(ex, veiculo);
                });

            }

        });
    }

    public interface OnInsert {
        void onComplete(Exception e, Veiculo veiculo);
    }
}

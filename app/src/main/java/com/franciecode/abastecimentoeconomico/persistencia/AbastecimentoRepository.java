package com.franciecode.abastecimentoeconomico.persistencia;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.franciecode.abastecimentoeconomico.enums.TipoCalculo;
import com.franciecode.abastecimentoeconomico.models.Abastecimento;
import com.franciecode.abastecimentoeconomico.models.AbastecimentoComVeiculo;
import com.franciecode.abastecimentoeconomico.persistencia.databaseViews.AbastecimentoRelatorioGraficoView;
import com.franciecode.abastecimentoeconomico.utils.UtilsAsyncTask;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AbastecimentoRepository {
    AppDBRoom db;

    public LiveData<List<AbastecimentoRelatorioGraficoView>> getRelatorioGraficoAnual(String ano) {
        return db.abastecimentoDao().getRelatorioGraficoAnual(ano);
    }


    public LiveData<List<AbastecimentoRelatorioGraficoView>> getRelatorioGraficoAnualPorTipoCalculo(String ano, TipoCalculo tipoCalculo) {
        return db.abastecimentoDao().getRelatorioGraficoAnualPorTipoCalculo(ano, tipoCalculo);
    }

    public LiveData<List<AbastecimentoRelatorioGraficoView>> getRelatorioGraficoAnualPorVeiculo(String ano, Long veiculoId) {
        return db.abastecimentoDao().getRelatorioGraficoAnualPorVeiculo(ano, veiculoId);
    }

    public AbastecimentoRepository(Context context) {
        this.db = AppDBRoom.getInstance(context);
    }

    public void insert(Abastecimento abastecimento, OnInsert listener) {

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

    public void delete(Abastecimento abastecimento, VeiculoRespository.OnDeleteListener listener) {
        new UtilsAsyncTask<Abastecimento, Void, Exception>() {
            @Override
            public Exception doInBackground(Abastecimento abastecimento) {
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

    public LiveData<List<Abastecimento>> getAll() {
        return db.abastecimentoDao().getAll();
    }

    public interface OnInsert {
        void onComplete(Exception e, Abastecimento abastecimento);
    }

    public LiveData<List<AbastecimentoComVeiculo>> getAbastecimentoComVeiculos() {
        return db.abastecimentoDao().getAbastecimentosComVeiculos();
    }
}

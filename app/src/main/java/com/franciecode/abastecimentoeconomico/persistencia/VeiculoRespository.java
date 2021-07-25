package com.franciecode.abastecimentoeconomico.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.franciecode.abastecimentoeconomico.models.Veiculo;
import com.franciecode.abastecimentoeconomico.utils.UtilsAsyncTask;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VeiculoRespository {
    private final AbastecimentoDao abastecimentoDao;
    private final AppDBRoom db;
    private final VeiculoDao dao;
    private Context context;

    public VeiculoRespository(Context context) {
        this.db = AppDBRoom.getInstance(context);
        this.dao = this.db.veiculoDao();
        this.abastecimentoDao = this.db.abastecimentoDao();
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

    public LiveData<List<Veiculo>> getAll() {
        return dao.getAll();
    }

    public LiveData<List<Veiculo>> getAllDeleted() {
        return dao.getAllDeleted();
    }

    public void update(Veiculo veiculo, OnUpdateListener listener) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            Exception ex;

            @Override
            public void run() {
                try {
                    dao.update(veiculo);
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

    public void trash(Veiculo veiculo, OnTrashListener listener) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            Exception ex;

            @Override
            public void run() {
                try {
                    dao.trash(veiculo.getId());
                    abastecimentoDao.sendTrashByVeiculoId(veiculo.getId());
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

    public LiveData<Integer> getTotalCadastrados() {
        return dao.getTotalCadastrados();
    }

    public LiveData<Integer> getTotalRemovidos() {
        return dao.getTotalRemovidos();
    }

    public void delete(Veiculo veiculo, OnDeleteListener listener) {
        new UtilsAsyncTask<Veiculo, Void, Void>() {
            Exception exception;

            @Override
            public Void doInBackground(Veiculo veiculo) {
                try {
                    dao.delete(veiculo);
                    abastecimentoDao.deleteByVeiculoId(veiculo.getId());
                } catch (Exception e) {
                    exception = e;
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void onPostResult(Void unused) {
                listener.onComplete(exception);
            }
        }.execute(veiculo);
    }

    public void removeFromTrash(Veiculo veiculo, OnDeleteListener listener) {
        new UtilsAsyncTask<Veiculo, Void, Void>() {
            Exception exception;

            @Override
            public Void doInBackground(Veiculo veiculo) {
                try {
                    dao.removeFromTrash(veiculo.getId());
                    abastecimentoDao.removeFromTrashByVeiculoId(veiculo.getId());
                } catch (Exception e) {
                    exception = e;
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void onPostResult(Void unused) {
                listener.onComplete(exception);
            }
        }.execute(veiculo);
    }

    public LiveData<Veiculo> getById(long veiculoId) {
        return dao.getById(veiculoId);
    }

    public LiveData<Veiculo> getByTipo(String veiculoConstantTipo) {
       return dao.getByTipo(veiculoConstantTipo);
    }

    // LISTENERS
    //-----------------------------------------------------------
    public interface OnInsert {
        void onComplete(Exception e, Veiculo veiculo);
    }

    public interface OnUpdateListener {
        void onComplete(Exception e, Veiculo veiculo);
    }

    public interface OnTrashListener {
        void onComplete(Exception e, Veiculo veiculo);
    }

    public interface OnDeleteListener {
        void onComplete(Exception e);
    }

    public interface OnReadSingleVeiculo {
        void onComplete(Exception e, Veiculo veiculo);
    }
}

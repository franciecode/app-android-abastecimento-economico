package com.ciecursoandroid.abastecimentoeconomico.persistencia.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ciecursoandroid.abastecimentoeconomico.models.Veiculo;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.VeiculoRespository;

import java.util.List;

public class VeiculoViewModel extends ViewModel {
    VeiculoRespository respository;

    public void setRespository(VeiculoRespository respository) {
        this.respository = respository;
    }

    public void insertVeiculo(Veiculo veiculo, VeiculoRespository.OnInsert listener) {
        respository.insert(veiculo, listener);
    }

    public LiveData<List<Veiculo>> getAll() {
        return respository.getAll();
    }

    public LiveData<List<Veiculo>> getAllDeleted() {
        return respository.getAllDeleted();
    }

    public void update(Veiculo veiculo, VeiculoRespository.OnUpdateListener listener) {
        respository.update(veiculo, listener);
    }

    public void trash(Veiculo veiculo, VeiculoRespository.OnTrashListener listener) {
        respository.trash(veiculo, listener);
    }

    public LiveData<Integer> getTotalCadastrados() {
        return respository.getTotalCadastrados();
    }

    public LiveData<Integer> getTotalRemovidos() {
        return respository.getTotalRemovidos();
    }
}

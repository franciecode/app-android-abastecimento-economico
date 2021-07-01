package com.ciecursoandroid.abastecimentoeconomico.persistencia.viewModel;

import androidx.lifecycle.ViewModel;

import com.ciecursoandroid.abastecimentoeconomico.models.Veiculo;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.VeiculoRespository;

public class VeiculoViewModel extends ViewModel {
    VeiculoRespository respository;

    public void setRespository(VeiculoRespository respository) {
        this.respository = respository;
    }

    public void insertVeiculo(Veiculo veiculo, VeiculoRespository.OnInsert listener) {
        respository.insert(veiculo, listener);
    }
}

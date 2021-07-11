package com.ciecursoandroid.abastecimentoeconomico.persistencia.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ciecursoandroid.abastecimentoeconomico.models.Abaste;
import com.ciecursoandroid.abastecimentoeconomico.models.AbastecimentoComVeiculo;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.AbastecimentoRepository;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.VeiculoRespository;

import java.util.List;

public class AbastecimentoViewModel extends ViewModel {

    AbastecimentoRepository repository;

    public void setRepository(AbastecimentoRepository repository) {
        this.repository = repository;
    }

    public void insert(Abaste abastecimento, AbastecimentoRepository.OnInsert listener) {
        repository.insert(abastecimento, listener);
    }

    public LiveData<List<Abaste>> getAll() {
        return repository.getAll();
    }

    public void delete(Abaste abastecimento, VeiculoRespository.OnDeleteListener listener) {
        repository.delete(abastecimento, listener);
    }

    public LiveData<List<AbastecimentoComVeiculo>> getAbastecimentoComVeiculos() {
        return repository.getAbastecimentoComVeiculos();
    }

}

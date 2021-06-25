package com.ciecursoandroid.abastecimentoeconomico.persistencia.viewModel;

import androidx.lifecycle.ViewModel;

import com.ciecursoandroid.abastecimentoeconomico.models.Abastecimento;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.AbastecimentoRepository;

public class AbastecimentoViewModel extends ViewModel {
    
    AbastecimentoRepository repository;

    public void setRepository(AbastecimentoRepository repository) {
        this.repository = repository;
    }

    public void insert(Abastecimento abastecimento, AbastecimentoRepository.OnInsert listener) {
        repository.insert(abastecimento, listener);
    }
}

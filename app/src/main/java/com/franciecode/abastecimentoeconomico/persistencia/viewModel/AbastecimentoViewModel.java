package com.franciecode.abastecimentoeconomico.persistencia.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.franciecode.abastecimentoeconomico.enums.TipoCalculo;
import com.franciecode.abastecimentoeconomico.models.Abastecimento;
import com.franciecode.abastecimentoeconomico.models.AbastecimentoComVeiculo;
import com.franciecode.abastecimentoeconomico.persistencia.AbastecimentoRepository;
import com.franciecode.abastecimentoeconomico.persistencia.VeiculoRespository;
import com.franciecode.abastecimentoeconomico.persistencia.databaseViews.AbastecimentoRelatorioGraficoView;

import java.util.List;

public class AbastecimentoViewModel extends ViewModel {

    AbastecimentoRepository repository;

    public LiveData<List<AbastecimentoRelatorioGraficoView>> getRelatorioGraficoAnual(String ano) {
        return repository.getRelatorioGraficoAnual(ano);
    }


    public LiveData<List<AbastecimentoRelatorioGraficoView>> getRelatorioGraficoAnualPorTipoCalculo(String ano, TipoCalculo tipoCalculo) {
        return repository.getRelatorioGraficoAnualPorTipoCalculo(ano, tipoCalculo);
    }

    public LiveData<List<AbastecimentoRelatorioGraficoView>> getRelatorioGraficoAnualPorVeiculo(String ano, Long veiculoId) {
        return repository.getRelatorioGraficoAnualPorVeiculo(ano, veiculoId);
    }

    public void setRepository(AbastecimentoRepository repository) {
        this.repository = repository;
    }

    public void insert(Abastecimento abastecimento, AbastecimentoRepository.OnInsert listener) {
        repository.insert(abastecimento, listener);
    }

    public LiveData<List<Abastecimento>> getAll() {
        return repository.getAll();
    }

    public void delete(Abastecimento abastecimento, VeiculoRespository.OnDeleteListener listener) {
        repository.delete(abastecimento, listener);
    }

    public LiveData<List<AbastecimentoComVeiculo>> getAbastecimentoComVeiculos() {
        return repository.getAbastecimentoComVeiculos();
    }

}

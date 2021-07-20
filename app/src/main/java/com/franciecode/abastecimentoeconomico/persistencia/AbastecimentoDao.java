package com.franciecode.abastecimentoeconomico.persistencia;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.franciecode.abastecimentoeconomico.enums.TipoCalculo;
import com.franciecode.abastecimentoeconomico.models.Abastecimento;
import com.franciecode.abastecimentoeconomico.models.AbastecimentoComVeiculo;
import com.franciecode.abastecimentoeconomico.persistencia.databaseViews.AbastecimentoRelatorioGraficoView;

import java.util.List;

@Dao
public interface AbastecimentoDao {
    @Insert
    long insert(Abastecimento abastecimento);

    @Query("SELECT * FROM table_abastecimento WHERE deleted = 0 ORDER BY dataAbastecimento DESC")
    LiveData<List<Abastecimento>> getAll();

    @Transaction
    @Query("SELECT * FROM table_abastecimento  WHERE deleted = 0 ORDER BY dataAbastecimento DESC")
    LiveData<List<AbastecimentoComVeiculo>> getAbastecimentosComVeiculos();

    @Query("UPDATE table_abastecimento SET deleted = 1 WHERE veiculoId = :veiculoId")
    void sendTrashByVeiculoId(long veiculoId);

    @Query("UPDATE table_abastecimento SET deleted = 0 WHERE veiculoId = :veiculoId")
    void removeFromTrashByVeiculoId(long veiculoId);

    @Query("DELETE FROM table_abastecimento WHERE veiculoId = :veiculoId")
    void deleteByVeiculoId(long veiculoId);

    @Delete
    void delete(Abastecimento abastecimento);

    @Query("SELECT *, sum(somaTotalPago) as somaTotalPago," +
            " sum(somaTotalEconomizado) as somaTotalEconomizado" +
            " FROM abastecimentorelatoriograficoview WHERE ano = :ano and deleted = 0 GROUP BY mes")
    LiveData<List<AbastecimentoRelatorioGraficoView>> getRelatorioGraficoAnual(String ano);

    @Query("SELECT *, sum(somaTotalPago) as somaTotalPago," +
            " sum(somaTotalEconomizado) as somaTotalEconomizado" +
            " FROM abastecimentorelatoriograficoview " +
            "WHERE ano = :ano " +
            "AND tipoCalculo = :tipoCalculo and deleted = 0  GROUP BY mes")
    LiveData<List<AbastecimentoRelatorioGraficoView>> getRelatorioGraficoAnualPorTipoCalculo(String ano, TipoCalculo tipoCalculo);

    @Query("SELECT *, sum(somaTotalPago) as somaTotalPago," +
            " sum(somaTotalEconomizado) as somaTotalEconomizado" +
            " FROM abastecimentorelatoriograficoview " +
            "WHERE ano = :ano " +
            "AND veiculoId = :veiculoId and deleted = 0  GROUP BY mes")
    LiveData<List<AbastecimentoRelatorioGraficoView>> getRelatorioGraficoAnualPorVeiculo(String ano, Long veiculoId);


}

package com.ciecursoandroid.abastecimentoeconomico.persistencia;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ciecursoandroid.abastecimentoeconomico.models.Abaste;
import com.ciecursoandroid.abastecimentoeconomico.models.AbastecimentoComVeiculo;

import java.util.List;

@Dao
public interface AbastecimentoDao {
    @Insert
    long insert(Abaste abastecimento);

    @Query("SELECT * FROM table_abastecimento WHERE deleted = 0 ORDER BY dataAbastecimento DESC")
    LiveData<List<Abaste>> getAll();

    @Transaction
    @Query("SELECT * FROM table_abastecimento")
    LiveData<List<AbastecimentoComVeiculo>> getAbastecimentosComVeiculos();

    @Query("UPDATE table_abastecimento SET deleted = 1 WHERE veiculoId = :veiculoId")
    void sendTrashByVeiculoId(long veiculoId);

    @Query("UPDATE table_abastecimento SET deleted = 0 WHERE veiculoId = :veiculoId")
    void removeFromTrashByVeiculoId(long veiculoId);

    @Query("DELETE FROM table_abastecimento WHERE veiculoId = :veiculoId")
    void deleteByVeiculoId(long veiculoId);

    @Delete
    void delete(Abaste abastecimento);


}

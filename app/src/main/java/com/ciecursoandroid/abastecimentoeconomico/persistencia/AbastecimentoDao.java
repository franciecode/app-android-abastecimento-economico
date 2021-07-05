package com.ciecursoandroid.abastecimentoeconomico.persistencia;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ciecursoandroid.abastecimentoeconomico.models.Abastecimento;

import java.util.List;

@Dao
public interface AbastecimentoDao {
    @Insert
    long insert(Abastecimento abastecimento);

    @Query("SELECT * FROM TABLE_ABASTECIMENTO WHERE deleted = 0 ORDER BY dataAbastecimento DESC")
    LiveData<List<Abastecimento>> getAll();

    @Query("UPDATE TABLE_ABASTECIMENTO SET deleted = 1 WHERE veiculoId = :veiculoId")
    void sendTrashByVeiculoId(long veiculoId);

    @Query("UPDATE TABLE_ABASTECIMENTO SET deleted = 0 WHERE veiculoId = :veiculoId")
    void removeFromTrashByVeiculoId(long veiculoId);

    @Query("DELETE FROM table_abastecimento WHERE veiculoId = :veiculoId")
    void deleteByVeiculoId(long veiculoId);

}

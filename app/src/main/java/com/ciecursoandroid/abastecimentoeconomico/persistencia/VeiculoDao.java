package com.ciecursoandroid.abastecimentoeconomico.persistencia;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ciecursoandroid.abastecimentoeconomico.models.Veiculo;

import java.util.List;

@Dao
public interface VeiculoDao {
    @Insert
    long insert(Veiculo veiculo);

    @Query("SELECT * FROM TABLE_VEICULO")
    LiveData<List<Veiculo>> getAll();

    @Query("SELECT * FROM TABLE_VEICULO WHERE deleted = 1")
    LiveData<List<Veiculo>> getAllDeleted();

    @Update
    void update(Veiculo veiculo);
}

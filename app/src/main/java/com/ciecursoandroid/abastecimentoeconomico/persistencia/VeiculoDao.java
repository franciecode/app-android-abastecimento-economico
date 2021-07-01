package com.ciecursoandroid.abastecimentoeconomico.persistencia;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ciecursoandroid.abastecimentoeconomico.models.Veiculo;

import java.util.List;

@Dao
public interface VeiculoDao {
    @Insert
    long insert(Veiculo veiculo);

    @Query("SELECT * FROM TABLE_VEICULO")
    LiveData<List<Veiculo>> getAll();
}

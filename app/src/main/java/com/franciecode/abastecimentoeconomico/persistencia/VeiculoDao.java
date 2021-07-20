package com.franciecode.abastecimentoeconomico.persistencia;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.franciecode.abastecimentoeconomico.models.Veiculo;

import java.util.List;

@Dao
public interface VeiculoDao {
    @Insert
    long insert(Veiculo veiculo);

    @Insert
    void insertAll(Veiculo... veiculos);

    @Query("SELECT * FROM TABLE_VEICULO WHERE deleted = 0 AND fakeModel = 0 AND fakeModel = 0")
    LiveData<List<Veiculo>> getAll();

    @Query("SELECT * FROM TABLE_VEICULO WHERE deleted = 1 AND fakeModel = 0")
    LiveData<List<Veiculo>> getAllDeleted();

    @Query("SELECT * FROM TABLE_VEICULO WHERE tipo = :veiculoConstantTipo")
    LiveData<Veiculo> getByTipo(String veiculoConstantTipo);

    @Update
    void update(Veiculo veiculo);

    @Query("UPDATE table_veiculo SET deleted = 1 WHERE id = :id")
    void trash(long id);

    @Query("UPDATE table_veiculo SET deleted = 0 WHERE id = :id")
    void removeFromTrash(long id);

    @Query("SELECT COUNT(*) FROM TABLE_VEICULO WHERE deleted = 0 AND fakeModel = 0")
    LiveData<Integer> getTotalCadastrados();

    @Query("SELECT COUNT(*) FROM TABLE_VEICULO WHERE deleted = 1 AND fakeModel = 0")
    LiveData<Integer> getTotalRemovidos();

    @Delete
    void delete(Veiculo veiculo);

    @Query("SELECT * FROM table_veiculo WHERE id =:veiculoId")
    LiveData<Veiculo> getById(long veiculoId);


}

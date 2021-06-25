package com.ciecursoandroid.abastecimentoeconomico.persistencia;

import androidx.room.Dao;
import androidx.room.Insert;

import com.ciecursoandroid.abastecimentoeconomico.models.Abastecimento;

@Dao
public interface AbastecimentoDao {
    @Insert
    long insert(Abastecimento abastecimento);
}

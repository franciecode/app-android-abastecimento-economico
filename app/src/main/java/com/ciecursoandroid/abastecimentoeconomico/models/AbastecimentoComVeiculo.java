package com.ciecursoandroid.abastecimentoeconomico.models;

import androidx.room.Embedded;
import androidx.room.Relation;

public class AbastecimentoComVeiculo {
    @Embedded
    public Abastecimento abastecimento;
    @Relation(parentColumn = "veiculoId", entityColumn = "id")
    public Veiculo veiculo;
}

package com.ciecursoandroid.abastecimentoeconomico.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_veiculo", indices = {@Index(value = {"nome"}, unique = true)})
public class Veiculo {
    @Ignore
    public static final String TIPO_VEICULO_CARRO = "carro";
    @Ignore
    public static final String TIPO_VEICULO_MOTO = "moto";
    @PrimaryKey(autoGenerate = true)
    long id;
    String nome;
    Float kmsLitroCidadeGasolina;
    Float kmsLitroRodoviaGasolina;
    Float kmsLitroCidadeAlcool;
    Float kmsLitroRodoviaAlcool;
    String tipo;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Float getKmsLitroCidadeGasolina() {
        return kmsLitroCidadeGasolina;
    }

    public void setKmsLitroCidadeGasolina(Float kmsLitroCidadeGasolina) {
        this.kmsLitroCidadeGasolina = kmsLitroCidadeGasolina;
    }

    public Float getKmsLitroRodoviaGasolina() {
        return kmsLitroRodoviaGasolina;
    }

    public void setKmsLitroRodoviaGasolina(Float kmsLitroRodoviaGasolina) {
        this.kmsLitroRodoviaGasolina = kmsLitroRodoviaGasolina;
    }

    public Float getKmsLitroCidadeAlcool() {
        return kmsLitroCidadeAlcool;
    }

    public void setKmsLitroCidadeAlcool(Float kmsLitroCidadeAlcool) {
        this.kmsLitroCidadeAlcool = kmsLitroCidadeAlcool;
    }

    public Float getKmsLitroRodoviaAlcool() {
        return kmsLitroRodoviaAlcool;
    }

    public void setKmsLitroRodoviaAlcool(Float kmsLitroRodoviaAlcool) {
        this.kmsLitroRodoviaAlcool = kmsLitroRodoviaAlcool;
    }
}

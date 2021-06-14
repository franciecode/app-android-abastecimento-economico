package com.ciecursoandroid.abastecimentoeconomico.models;

public class Veiculo {
    long id;
    String nome;
    Float kmsLitroCidadeGasolina;
    Float kmsLitroRodoviaGasolina;
    Float kmsLitroCidadeAlcool;
    Float kmsLitroRodoviaAlcool;

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

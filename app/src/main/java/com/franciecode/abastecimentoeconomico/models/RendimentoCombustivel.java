package com.franciecode.abastecimentoeconomico.models;

public class RendimentoCombustivel {
    float precoCombustivel;
    float litrosCombustivel;
    float kmsLitro;
    float precoKm;
    float totalKms;
    float custoTotal;

    public RendimentoCombustivel(float precoCombustivel, float litrosCombustivel, float kmsLitro, float precoKm, float totalKms, float custoTotal) {
        this.precoCombustivel = precoCombustivel;
        this.litrosCombustivel = litrosCombustivel;
        this.kmsLitro = kmsLitro;
        this.precoKm = precoKm;
        this.totalKms = totalKms;
        this.custoTotal = custoTotal;
    }

    public float getCustoTotal() {
        return custoTotal;
    }

    public void setCustoTotal(float custoTotal) {
        this.custoTotal = custoTotal;
    }

    public float getPrecoCombustivel() {
        return precoCombustivel;
    }

    public void setPrecoCombustivel(float precoCombustivel) {
        this.precoCombustivel = precoCombustivel;
    }

    public float getLitrosCombustivel() {
        return litrosCombustivel;
    }

    public void setLitrosCombustivel(float litrosCombustivel) {
        this.litrosCombustivel = litrosCombustivel;
    }

    public float getKmsLitro() {
        return kmsLitro;
    }

    public void setKmsLitro(float kmsLitro) {
        this.kmsLitro = kmsLitro;
    }

    public float getPrecoKm() {
        return precoKm;
    }

    public void setPrecoKm(float precoKm) {
        this.precoKm = precoKm;
    }

    public float getTotalKms() {
        return totalKms;
    }

    public void setTotalKms(float totalKms) {
        this.totalKms = totalKms;
    }
}

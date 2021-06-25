package com.ciecursoandroid.abastecimentoeconomico.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.ciecursoandroid.abastecimentoeconomico.enums.LocalViagem;
import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCalculo;
import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCombustivel;

import java.sql.Timestamp;

@Entity(tableName = "table_abastecimento")
public class Abastecimento {
    @PrimaryKey(autoGenerate = true)
    long id;
    @Ignore
    Veiculo veiculo;
    long veiculoId;
    LocalViagem localViagem;
    TipoCalculo tipoCalculo;
    TipoCombustivel combustivelRecomendado;
    TipoCombustivel combustivelAbastecido;
    float porcentagemEconomia;
    float valorEconomizado;
    float totalPago;
    float totalLitrosAbastecidos;
    float precoGasolina;
    float precoAlcool;
    Timestamp dataAbastecimento;
    float precoKmLitroGasolina;
    float precoKmLitroAlcool;
    float kmsLitroAlcool;
    float kmsLitroGasolina;

    public float getTotalLitrosAbastecidos() {
        return totalLitrosAbastecidos;
    }

    public void setTotalLitrosAbastecidos(float totalLitrosAbastecidos) {
        this.totalLitrosAbastecidos = totalLitrosAbastecidos;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public long getVeiculoId() {
        return veiculoId;
    }

    public void setVeiculoId(long veiculoId) {
        this.veiculoId = veiculoId;
    }

    public LocalViagem getLocalViagem() {
        return localViagem;
    }

    public void setLocalViagem(LocalViagem localViagem) {
        this.localViagem = localViagem;
    }

    public TipoCalculo getTipoCalculo() {
        return tipoCalculo;
    }

    public void setTipoCalculo(TipoCalculo tipoCalculo) {
        this.tipoCalculo = tipoCalculo;
    }

    public TipoCombustivel getCombustivelRecomendado() {
        return combustivelRecomendado;
    }

    public void setCombustivelRecomendado(TipoCombustivel combustivelRecomendado) {
        this.combustivelRecomendado = combustivelRecomendado;
    }

    public TipoCombustivel getCombustivelAbastecido() {
        return combustivelAbastecido;
    }

    public void setCombustivelAbastecido(TipoCombustivel combustivelAbastecido) {
        this.combustivelAbastecido = combustivelAbastecido;
    }

    public float getPorcentagemEconomia() {
        return porcentagemEconomia;
    }

    public void setPorcentagemEconomia(float porcentagemEconomia) {
        this.porcentagemEconomia = porcentagemEconomia;
    }

    public float getValorEconomizado() {
        return valorEconomizado;
    }

    public void setValorEconomizado(float valorEconomizado) {
        this.valorEconomizado = valorEconomizado;
    }

    public float getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(float totalPago) {
        this.totalPago = totalPago;
    }

    public float getPrecoGasolina() {
        return precoGasolina;
    }

    public void setPrecoGasolina(float precoGasolina) {
        this.precoGasolina = precoGasolina;
    }

    public float getPrecoAlcool() {
        return precoAlcool;
    }

    public void setPrecoAlcool(float precoAlcool) {
        this.precoAlcool = precoAlcool;
    }

    public Timestamp getDataAbastecimento() {
        return dataAbastecimento;
    }

    public void setDataAbastecimento(Timestamp dataAbastecimento) {
        this.dataAbastecimento = dataAbastecimento;
    }

    public float getPrecoKmLitroGasolina() {
        return precoKmLitroGasolina;
    }

    public void setPrecoKmLitroGasolina(float precoKmLitroGasolina) {
        this.precoKmLitroGasolina = precoKmLitroGasolina;
    }

    public float getPrecoKmLitroAlcool() {
        return precoKmLitroAlcool;
    }

    public void setPrecoKmLitroAlcool(float precoKmLitroAlcool) {
        this.precoKmLitroAlcool = precoKmLitroAlcool;
    }

    public float getKmsLitroAlcool() {
        return kmsLitroAlcool;
    }

    public void setKmsLitroAlcool(float kmsLitroAlcool) {
        this.kmsLitroAlcool = kmsLitroAlcool;
    }

    public float getKmsLitroGasolina() {
        return kmsLitroGasolina;
    }

    public void setKmsLitroGasolina(float kmsLitroGasolina) {
        this.kmsLitroGasolina = kmsLitroGasolina;
    }
}

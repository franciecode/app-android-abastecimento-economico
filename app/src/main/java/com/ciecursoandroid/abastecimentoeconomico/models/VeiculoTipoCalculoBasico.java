package com.ciecursoandroid.abastecimentoeconomico.models;

public class VeiculoTipoCalculoBasico extends Veiculo {
    public VeiculoTipoCalculoBasico() {
        setTipo(Veiculo.TIPO_VEICULO_BASICO);
        setNome("tipo calculo: basico");
        setKmsLitroCidadeAlcool(7f);
        setKmsLitroRodoviaAlcool(7f);
        setKmsLitroCidadeGasolina(10f);
        setKmsLitroRodoviaGasolina(10f);
        setFakeModel(true);
    }
}

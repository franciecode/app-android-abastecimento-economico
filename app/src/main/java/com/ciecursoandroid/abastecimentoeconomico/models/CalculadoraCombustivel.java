package com.ciecursoandroid.abastecimentoeconomico.models;

import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCombustivel;

public class CalculadoraCombustivel {

    public RendimentoCombustivel calcularRendimento(float precoCombustivel, float kmsLitro, float litros) {
        float totalKms = kmsLitro * litros;
        float precoKm = precoCombustivel / kmsLitro;
        float custoTotal = totalKms * precoKm;
        RendimentoCombustivel rendimento = new RendimentoCombustivel(precoCombustivel, litros, kmsLitro, precoKm, totalKms, custoTotal);
        return rendimento;
    }


    public CombustivelMaisBarato calcularCombustivelMaisBarato(float precoGasolina, float precoAlcool, float kmsGasolina, float kmsAlcool) {
        RendimentoCombustivel rendimentoGasolina = calcularRendimento(precoGasolina, kmsGasolina, 1);
        RendimentoCombustivel rendimentoAlcool = calcularRendimento(precoAlcool, kmsAlcool, 1);
        if (rendimentoAlcool.getPrecoKm() < rendimentoGasolina.getPrecoKm()) {
            float porcentagemEconomia = 1 - (rendimentoAlcool.getPrecoKm() / rendimentoGasolina.getPrecoKm());
            return new CombustivelMaisBarato(TipoCombustivel.ALCOOL, porcentagemEconomia * 100, rendimentoAlcool);
        } else {
            float porcentagemEconomia = 1 - (rendimentoGasolina.getPrecoKm() / rendimentoAlcool.getPrecoKm());
            return new CombustivelMaisBarato(TipoCombustivel.GASOLINA, porcentagemEconomia * 100, rendimentoGasolina);
        }
    }

    public CalculoAbastecimento calcularAbastecimento(float litros, TipoCombustivel abastecido, float precoGasolina, float precoAlcool, float kmsGasolina, float kmsAlcool) {

        RendimentoCombustivel rendimentoGasolina = calcularRendimento(precoGasolina, kmsGasolina, litros);
        RendimentoCombustivel rendimentoAlcool = calcularRendimento(precoAlcool, kmsAlcool, litros);
        float valorEconomizado = 0f;
        if (abastecido == TipoCombustivel.ALCOOL) {
            valorEconomizado = rendimentoGasolina.getPrecoKm() * rendimentoAlcool.getTotalKms() - rendimentoAlcool.getCustoTotal();
        } else {
            valorEconomizado = rendimentoAlcool.getPrecoKm() * rendimentoGasolina.getTotalKms() - rendimentoGasolina.getCustoTotal();
        }
        return new CalculoAbastecimento(rendimentoGasolina, rendimentoAlcool, valorEconomizado);
    }

    public class CalculoAbastecimento {
        RendimentoCombustivel rendimentoGasolina;
        RendimentoCombustivel rendimentoAlcool;
        float valorEconomizado;

        public CalculoAbastecimento(RendimentoCombustivel rendimentoGasolina, RendimentoCombustivel rendimentoAlcool, float valorEconomizado) {
            this.rendimentoGasolina = rendimentoGasolina;
            this.rendimentoAlcool = rendimentoAlcool;
            this.valorEconomizado = valorEconomizado;
        }

        public float getValorEconomizado() {
            return valorEconomizado;
        }

        public RendimentoCombustivel getRendimentoGasolina() {
            return rendimentoGasolina;
        }

        public RendimentoCombustivel getRendimentoAlcool() {
            return rendimentoAlcool;
        }
    }

    public class CombustivelMaisBarato {
        TipoCombustivel combustivelMaisBarato;
        float porcentagemEconomia = 0f;
        RendimentoCombustivel rendimentoCombustivel;

        public CombustivelMaisBarato(TipoCombustivel combustivel, float porcentagemEconomia, RendimentoCombustivel rendimentoCombustivel) {
            this.combustivelMaisBarato = combustivel;
            this.porcentagemEconomia = porcentagemEconomia;
            this.rendimentoCombustivel = rendimentoCombustivel;
        }

        public TipoCombustivel getCombustivelMaisBarato() {
            return combustivelMaisBarato;
        }

        public float getPorcentagemEconomia() {
            return porcentagemEconomia;
        }

        public RendimentoCombustivel getRendimentoCombustivel() {
            return rendimentoCombustivel;
        }
    }
}

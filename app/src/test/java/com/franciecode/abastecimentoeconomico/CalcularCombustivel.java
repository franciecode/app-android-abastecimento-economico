package com.franciecode.abastecimentoeconomico;

import com.franciecode.abastecimentoeconomico.enums.TipoCombustivel;
import com.franciecode.abastecimentoeconomico.models.CalculadoraCombustivel;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CalcularCombustivel {
    @Test
    public void calcularCombustivelMaisBarato() {
        float precoGasolina = 5;
        float kmsGasolina = 10;
        float precoAlcool = 3;
        float kmsAlcool = 7;
        CalculadoraCombustivel calculadora = new CalculadoraCombustivel();
        CalculadoraCombustivel.CombustivelMaisBarato result = calculadora
                .calcularCombustivelMaisBarato(precoGasolina, precoAlcool, kmsGasolina, kmsAlcool);
        assertEquals(TipoCombustivel.ALCOOL, result.getCombustivelMaisBarato());
        assertEquals(14.285713f, result.getPorcentagemEconomia(), 0);
        assertEquals(0.4285714328289032f, result.getRendimentoCombustivel().getPrecoKm(), 0);
    }

    @Test
    public void calcularAbastecimentoAlcool() {
        float litros = 10;
        float precoGasolina = 5;
        float kmsGasolina = 10;
        float precoAlcool = 3;
        float kmsAlcool = 7;
        CalculadoraCombustivel calculadora = new CalculadoraCombustivel();
        CalculadoraCombustivel.CalculoAbastecimento result = calculadora
                .calcularAbastecimento(litros, TipoCombustivel.ALCOOL, precoGasolina, precoAlcool, kmsGasolina, kmsAlcool);

        System.out.println("valor economizado: "+result.getValorEconomizado());
        System.out.println("custo total alcool: "+result.getRendimentoAlcool().getCustoTotal());
        System.out.println("custo total gasolina: "+result.getRendimentoGasolina().getCustoTotal());
        System.out.println("preco kmAlcool: "+result.getRendimentoAlcool().getPrecoKm());
        System.out.println("preco kmGasolina: "+result.getRendimentoGasolina().getPrecoKm());

        assertEquals(5.0f, result.getValorEconomizado(), 0);
        assertEquals(30.0f, result.getRendimentoAlcool().getCustoTotal(), 0);
        assertEquals(50.0f, result.getRendimentoGasolina().getCustoTotal(), 0);
        assertEquals(0.42857143f, result.getRendimentoAlcool().getPrecoKm(), 0);
        assertEquals(0.5f, result.getRendimentoGasolina().getPrecoKm(), 0);

    }
}
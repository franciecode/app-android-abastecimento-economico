package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCombustivel;
import com.ciecursoandroid.abastecimentoeconomico.models.CalculadoraCombustivel;
import com.ciecursoandroid.abastecimentoeconomico.utils.NumeroUtils;

public class CalculoResultadoBasicoActivity extends CalculoResultadoBaseActivity {

    float kmsGasolina = 10;
    float kmsAlcool = 7;
    TextView textViewTotalAPagar;
    TextView textViewValorEconomizado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo_resultado_basico);

        setFields();
        textViewTotalAPagar = findViewById(R.id.textViewTotalAPagar);
        textViewValorEconomizado = findViewById(R.id.textViewvalorEconomizado);
        textViewTotalAPagar.setText(NumeroUtils.formatDinheiro(this, 0f));
        textViewValorEconomizado.setText(NumeroUtils.formatDinheiro(this, 0f));

        calcularCombustivelMaisBarato(precoAlcool, precoGAsolina, 10, 7);
    }

    @Override
    public void calcularAbastecimento(TipoCombustivel abastecer, float precoCombustivel, float litrosAbastecidos) {
        if (abastecer == null) return;
        CalculadoraCombustivel.CalculoAbastecimento result = calculadoraCombustivel
                .calcularAbastecimento(litrosAbastecidos,
                        abastecer, precoGAsolina, precoAlcool, kmsGasolina, kmsAlcool);

        float totalPagar = abastecer == TipoCombustivel.ALCOOL ? result.getRendimentoAlcool().getCustoTotal() : result.getRendimentoGasolina().getCustoTotal();
        textViewTotalAPagar.setText(NumeroUtils.formatDinheiro(this, totalPagar));
        textViewValorEconomizado.setText(NumeroUtils.formatDinheiro(this, result.getValorEconomizado()));
        if (result.getValorEconomizado() < 0) {
            textViewValorEconomizado.setTextColor(getResources().getColor(R.color.color_text_red));
        } else {
            textViewValorEconomizado.setTextColor(getResources().getColor(R.color.color_text_green));
        }

    }

}
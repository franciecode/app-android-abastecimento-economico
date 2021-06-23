package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.os.Bundle;

import com.ciecursoandroid.abastecimentoeconomico.R;

public class CalculoResultadoBasicoActivity extends CalculoResultadoBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo_resultado_basico);

        setFields();

        calcularCombustivelMaisBarato(precoAlcool, precoGAsolina, 10, 7);
    }

}
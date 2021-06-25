package com.ciecursoandroid.abastecimentoeconomico.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCombustivel;
import com.ciecursoandroid.abastecimentoeconomico.models.Abastecimento;

public class CalculoResultadoKmsLitroActivity extends CalculoResultadoBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo_resultado_kms_litro);
    }

    @Override
    public void calcularAbastecimento(TipoCombustivel abastecer, float precoCombustivel, float litrosAbastecidos) {

    }

    @Override
    public void salvarAbastecimento(Abastecimento abastecimento) {

    }
}
package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCombustivel;
import com.ciecursoandroid.abastecimentoeconomico.models.CalculadoraCombustivel;

public abstract class CalculoResultadoBaseActivity extends AppCompatActivity {

    protected float precoGAsolina;
    protected float precoAlcool;
    protected CalculadoraCombustivel calculadoraCombustivel = new CalculadoraCombustivel();
    protected TipoCombustivel combustivelMaisBarato;
    protected RadioGroup radioGroupAbastecimento;
    protected RadioButton radioButtonGasolina;
    protected RadioButton radioButtonAlcool;
    protected EditText editTextLitros;
    protected TextView textViewCombustivelRecomendado;
    private TextView textViewPorcentagemEconomia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void setFields() {
        radioGroupAbastecimento = findViewById(R.id.radiogroupAbastecimento);
        radioButtonGasolina = findViewById(R.id.radioButtonGasolina);
        radioButtonAlcool = findViewById(R.id.radioButtonAlcool);
        editTextLitros = findViewById(R.id.editTextLitros);
        textViewCombustivelRecomendado = findViewById(R.id.textViewCombustivelRecomendado);
        textViewPorcentagemEconomia = findViewById(R.id.textViewPorcentagemEconomia);
        precoGAsolina = getIntent().getFloatExtra("precoGasolina", 0);
        precoAlcool = getIntent().getFloatExtra("precoAlcool", 0);
    }

    void calcularCombustivelMaisBarato(float precoAlcool, float precoGAsolina, float kmsGasolina, float kmsAlcool) {
        CalculadoraCombustivel.CombustivelMaisBarato result = calculadoraCombustivel.calcularCombustivelMaisBarato(precoGAsolina, precoAlcool, kmsGasolina, kmsAlcool);
        combustivelMaisBarato = result.getCombustivel();
        setCombustivelRecomendado(result);
    }

    private void setCombustivelRecomendado(CalculadoraCombustivel.CombustivelMaisBarato combustivelMaisBarato) {
        if (combustivelMaisBarato.getCombustivel() == TipoCombustivel.ALCOOL) {
            textViewCombustivelRecomendado.setText(getText(R.string.alcool));
            int color = getResources().getColor(R.color.color_alcool);
            textViewCombustivelRecomendado.setTextColor(color);
            for (Drawable d : textViewCombustivelRecomendado.getCompoundDrawables()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && d != null) {
                    d.setTint(color);
                }
            }
        } else {
            textViewCombustivelRecomendado.setText(getText(R.string.gasolina));
            int color = getResources().getColor(R.color.color_gasolina);
            textViewCombustivelRecomendado.setTextColor(color);
            for (Drawable d : textViewCombustivelRecomendado.getCompoundDrawables()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && d != null) {
                    d.setTint(color);
                }
            }
        }

        textViewPorcentagemEconomia.setText(String.format("%.2f%s", combustivelMaisBarato.getPorcentagemEconomia(),"%"));

    }


}
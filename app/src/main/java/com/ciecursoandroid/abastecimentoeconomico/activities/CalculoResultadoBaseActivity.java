package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    protected TextView textViewPorcentagemEconomia;
    TipoCombustivel abastecer;
    float litrosAbastecidos = 0f;

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

        radioButtonGasolina.setText(String.format(getString(R.string.radio_preco_gasolina), precoGAsolina));
        radioButtonAlcool.setText(String.format(getString(R.string.radio_preco_alcool), precoAlcool));

        radioGroupAbastecimento.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                abastecer = i == radioButtonAlcool.getId() ? TipoCombustivel.ALCOOL : TipoCombustivel.GASOLINA;
                float precoCombustivel = abastecer == TipoCombustivel.ALCOOL ? precoAlcool : precoGAsolina;
                calcularAbastecimento(abastecer, precoCombustivel, litrosAbastecidos);
            }
        });
        editTextLitros.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                litrosAbastecidos = Float.valueOf(charSequence.length() == 0 ? "0.0" : charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                float precoCombustivel = abastecer == null ? 0f :
                        abastecer == TipoCombustivel.ALCOOL ? precoAlcool : precoGAsolina;
                calcularAbastecimento(abastecer, precoCombustivel, litrosAbastecidos);
            }
        });
    }

    void calcularCombustivelMaisBarato(float precoAlcool, float precoGAsolina, float kmsGasolina, float kmsAlcool) {
        CalculadoraCombustivel.CombustivelMaisBarato result = calculadoraCombustivel.calcularCombustivelMaisBarato(precoGAsolina, precoAlcool, kmsGasolina, kmsAlcool);
        combustivelMaisBarato = result.getCombustivelMaisBarato();
        setCombustivelRecomendado(result);
    }

    private void setCombustivelRecomendado(CalculadoraCombustivel.CombustivelMaisBarato combustivelMaisBarato) {
        if (combustivelMaisBarato.getCombustivelMaisBarato() == TipoCombustivel.ALCOOL) {
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

        textViewPorcentagemEconomia.setText(String.format("%.2f%s", combustivelMaisBarato.getPorcentagemEconomia(), "%"));

    }

    public abstract void calcularAbastecimento(TipoCombustivel abastecer, float precoCombustivel, float litrosAbastecidos);


}
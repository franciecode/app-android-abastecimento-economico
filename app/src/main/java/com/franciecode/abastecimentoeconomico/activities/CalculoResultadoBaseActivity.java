package com.franciecode.abastecimentoeconomico.activities;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.franciecode.abastecimentoeconomico.R;
import com.franciecode.abastecimentoeconomico.enums.TipoCombustivel;
import com.franciecode.abastecimentoeconomico.models.Abastecimento;
import com.franciecode.abastecimentoeconomico.models.CalculadoraCombustivel;
import com.franciecode.abastecimentoeconomico.persistencia.AppPreferencias;

import java.util.Locale;

public abstract class CalculoResultadoBaseActivity extends AppCompatActivity {

    protected float precoGAsolina;
    protected float precoAlcool;
    protected CalculadoraCombustivel calculadoraCombustivel = new CalculadoraCombustivel();
    protected CalculadoraCombustivel.CombustivelMaisBarato combustivelMaisBarato;
    protected TipoCombustivel combustivelRecomendado;
    protected RadioGroup radioGroupAbastecimento;
    protected RadioButton radioButtonGasolina;
    protected RadioButton radioButtonAlcool;
    protected EditText editTextLitros;
    protected TextView textViewCombustivelRecomendado;
    protected TextView textViewPorcentagemEconomia;
    protected TipoCombustivel abastecer;
    protected float litrosAbastecidos = 0f;
    protected AppPreferencias appPreferencias;
    protected TextView textViewDetalheVeiculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appPreferencias = new AppPreferencias(this);

    }


    protected void setBaseFields() {
        textViewDetalheVeiculo = findViewById(R.id.textViewDetalheVeiculo);
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

        // calcular abasteciento >>>>>>>>>>>>>>>>>>>>>>>
        radioGroupAbastecimento.setOnCheckedChangeListener((radioGroup, i) -> {
            abastecer = i == radioButtonAlcool.getId() ? TipoCombustivel.ALCOOL : TipoCombustivel.GASOLINA;
            float precoCombustivel = abastecer == TipoCombustivel.ALCOOL ? precoAlcool : precoGAsolina;
            calcularAbastecimento(abastecer, precoCombustivel, litrosAbastecidos);
        });

        editTextLitros.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                litrosAbastecidos = Float.valueOf(charSequence.length() == 0 ? "0" : charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                float precoCombustivel = abastecer == null ? 0f :
                        abastecer == TipoCombustivel.ALCOOL ? precoAlcool : precoGAsolina;
                calcularAbastecimento(abastecer, precoCombustivel, litrosAbastecidos);
            }
        });
        // calcular abasteciento <<<<<<<<<<<<<<<<<<<

        salvarPrecosCombustiveis();
    }

    void setDetalheVeiculo(String detalhes) {
        textViewDetalheVeiculo.setText(detalhes);
    }

    void calcularCombustivelMaisBarato(float precoAlcool, float precoGAsolina, float kmsGasolina, float kmsAlcool) {
        combustivelMaisBarato = calculadoraCombustivel.calcularCombustivelMaisBarato(precoGAsolina, precoAlcool, kmsGasolina, kmsAlcool);
        combustivelRecomendado = combustivelMaisBarato.getCombustivelMaisBarato();
        setCombustivelRecomendado(combustivelMaisBarato);
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

        textViewPorcentagemEconomia.setText(String.format(Locale.getDefault(), "%.2f%s", combustivelMaisBarato.getPorcentagemEconomia(), "%"));

    }

    private void salvarPrecosCombustiveis() {
        appPreferencias.setPrecoAlcool(precoAlcool);
        appPreferencias.setPrecoGasolina(precoGAsolina);
        appPreferencias.commit();
    }

    public abstract void calcularAbastecimento(TipoCombustivel abastecer, float precoCombustivel, float litrosAbastecidos);

    public abstract void salvarAbastecimento(Abastecimento abastecimento);

    protected boolean validarFormSalvarAbastecimento() {
        boolean erro = false;
        if (radioGroupAbastecimento.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Comb??st??vel n??o informado!", Toast.LENGTH_SHORT).show();
            erro = true;
        }
        if (litrosAbastecidos == 0) {
            Toast.makeText(this, "Total de litros n??o informado!", Toast.LENGTH_SHORT).show();
            erro = true;
        }

        return !erro;
    }

}
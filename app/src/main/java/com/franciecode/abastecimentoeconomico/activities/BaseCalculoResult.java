package com.franciecode.abastecimentoeconomico.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.franciecode.abastecimentoeconomico.R;
import com.franciecode.abastecimentoeconomico.enums.TipoCombustivel;
import com.franciecode.abastecimentoeconomico.models.CalculadoraCombustivel;
import com.franciecode.abastecimentoeconomico.persistencia.AppPreferencias;

import java.util.Locale;

public abstract class BaseCalculoResult {

    protected float precoGAsolina;
    protected float precoAlcool;
    public CalculadoraCombustivel calculadoraCombustivel = new CalculadoraCombustivel();
    public CalculadoraCombustivel.CombustivelMaisBarato combustivelMaisBarato;
    public TipoCombustivel combustivelRecomendado;
    public RadioGroup radioGroupAbastecimento;
    protected RadioButton radioButtonGasolina;
    protected RadioButton radioButtonAlcool;
    protected EditText editTextLitros;
    protected TextView textViewCombustivelRecomendado;
    protected TextView textViewPorcentagemEconomia;
    protected TipoCombustivel abastecer;
    public float litrosAbastecidos = 0f;
    protected AppPreferencias appPreferencias;
    View rootView;
    Context context;

    public BaseCalculoResult(View rootView, Context context, float precoAlcool, float precoGAsolina) {
        this.rootView = rootView;
        this.context = context;
        appPreferencias = new AppPreferencias((Activity) context);
        setFields(precoGAsolina, precoAlcool);
    }

    protected void setFields(float precoGAsolina, float precoAlcool) {
        radioGroupAbastecimento = rootView.findViewById(R.id.radiogroupAbastecimento);
        radioButtonGasolina = rootView.findViewById(R.id.radioButtonGasolina);
        radioButtonAlcool = rootView.findViewById(R.id.radioButtonAlcool);
        editTextLitros = rootView.findViewById(R.id.editTextLitros);
        textViewCombustivelRecomendado = rootView.findViewById(R.id.textViewCombustivelRecomendado);
        textViewPorcentagemEconomia = rootView.findViewById(R.id.textViewPorcentagemEconomia);
        this.precoGAsolina = precoGAsolina;
        this.precoAlcool = precoAlcool;

        radioButtonGasolina.setText(String.format(context.getString(R.string.radio_preco_gasolina), precoGAsolina));
        radioButtonAlcool.setText(String.format(context.getString(R.string.radio_preco_alcool), precoAlcool));

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

    public abstract void calcularAbastecimento(TipoCombustivel abastecer, float precoCombustivel, float litrosAbastecidos);

    private void salvarPrecosCombustiveis() {
        appPreferencias.setPrecoAlcool(precoAlcool);
        appPreferencias.setPrecoGasolina(precoGAsolina);
        appPreferencias.commit();
    }

    protected boolean validarFormSalvarAbastecimento() {
        boolean erro = false;
        if (radioGroupAbastecimento.getCheckedRadioButtonId() == -1) {
            Toast.makeText(context, context.getResources().getString(R.string.combustivel_nao_informado), Toast.LENGTH_SHORT).show();
            erro = true;
        }
        if (litrosAbastecidos == 0) {
            Toast.makeText(context, context.getResources().getString(R.string.litros_nao_informados), Toast.LENGTH_SHORT).show();
            erro = true;
        }

        return !erro;
    }

    public void calcularCombustivelMaisBarato(float precoDoAlcool, float precoDagasolina, float kmsGasolina, float kmsAlcool) {
        combustivelMaisBarato = calculadoraCombustivel.calcularCombustivelMaisBarato(precoDagasolina, precoDoAlcool, kmsGasolina, kmsAlcool);
        combustivelRecomendado = combustivelMaisBarato.getCombustivelMaisBarato();
        setCombustivelRecomendado(combustivelMaisBarato);
    }

    private void setCombustivelRecomendado(CalculadoraCombustivel.CombustivelMaisBarato combustivelMaisBarato) {
        if (combustivelMaisBarato.getCombustivelMaisBarato() == TipoCombustivel.ALCOOL) {
            textViewCombustivelRecomendado.setText(context.getResources().getString(R.string.alcool));
            int color = context.getResources().getColor(R.color.color_alcool);
            textViewCombustivelRecomendado.setTextColor(color);
            for (Drawable d : textViewCombustivelRecomendado.getCompoundDrawables()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && d != null) {
                    d.setTint(color);
                }
            }
        } else {
            textViewCombustivelRecomendado.setText(context.getResources().getString(R.string.gasolina));
            int color = context.getResources().getColor(R.color.color_gasolina);
            textViewCombustivelRecomendado.setTextColor(color);
            for (Drawable d : textViewCombustivelRecomendado.getCompoundDrawables()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && d != null) {
                    d.setTint(color);
                }
            }
        }

        textViewPorcentagemEconomia.setText(String.format(Locale.getDefault(), "%.2f%s", combustivelMaisBarato.getPorcentagemEconomia(), "%"));
    }
}

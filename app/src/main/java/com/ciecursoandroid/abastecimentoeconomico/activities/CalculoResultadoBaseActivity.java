package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.enums.LogTAGS;
import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCombustivel;
import com.ciecursoandroid.abastecimentoeconomico.models.Abastecimento;
import com.ciecursoandroid.abastecimentoeconomico.models.CalculadoraCombustivel;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.AppPreferencias;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.jetbrains.annotations.NotNull;

public abstract class CalculoResultadoBaseActivity extends AppCompatActivity {

    private static InterstitialAd mInterstitialAd;
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

    public static void carregarAnuncioTelaCheia(Activity activity) {
        MobileAds.initialize(activity, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NotNull InitializationStatus initializationStatus) {

            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(activity, "ca-app-pub-2036643128150326/8305722355", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error

                        mInterstitialAd = null;
                    }
                });
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
                litrosAbastecidos = Float.valueOf(charSequence.length() == 0 ? "0.0" : charSequence.toString());
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

        textViewPorcentagemEconomia.setText(String.format("%.2f%s", combustivelMaisBarato.getPorcentagemEconomia(), "%"));

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
            Toast.makeText(this, "Combústível não informado!", Toast.LENGTH_SHORT).show();
            erro = true;
        }
        if (litrosAbastecidos == 0) {
            Toast.makeText(this, "Total de litros não informado!", Toast.LENGTH_SHORT).show();
            erro = true;
        }

        return erro == false;
    }

    public static void mostrarAnuncioTelaCheia(Activity activity, OnFullScreenADListener listener) {
        if (mInterstitialAd != null && BaseMenuActivity.contagemRegressivaMotrarAnuncioTelaCheia <= 0) {
            BaseMenuActivity.contagemRegressivaMotrarAnuncioTelaCheia = 2;
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull @NotNull AdError adError) {
                    listener.done();
                }

                @Override
                public void onAdShowedFullScreenContent() {

                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    listener.done();
                }

                @Override
                public void onAdImpression() {
                }
            });
            mInterstitialAd.show(activity);
        } else {
            listener.done();
        }
    }

    public interface OnFullScreenADListener {
        void done();
    }

}
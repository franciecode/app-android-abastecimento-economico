package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentManager;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.enums.LogTAGS;
import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCalculo;
import com.ciecursoandroid.abastecimentoeconomico.fragments.FormCalcularBaseFragment;
import com.ciecursoandroid.abastecimentoeconomico.fragments.FormCalcularBasicoFragment;
import com.ciecursoandroid.abastecimentoeconomico.fragments.FormCalcularKmsLitroFragment;
import com.ciecursoandroid.abastecimentoeconomico.fragments.FormCalcularVeiculoFragment;
import com.ciecursoandroid.abastecimentoeconomico.models.Veiculo;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.AppPreferencias;
import com.ciecursoandroid.abastecimentoeconomico.widgets.Alerts;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends BaseMenuActivity implements RadioGroup.OnCheckedChangeListener, FormCalcularBaseFragment.Listener {

    private FragmentManager fragmentManager;
    private RadioGroup radioGroupTipoCalculo;
    private ActionBar actionBar;
    private TipoCalculo tipoCalculo;
    Float kmsLitroGasolina = 0f;
    Float kmsLitroAlcool = 0f;
    Veiculo veiculo;
    private EditText editTextPrecoGasolina;
    private EditText editTextPrecoAlcool;
    private AppPreferencias appPreferencias;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_calculo);

        adicionarAnuncio();

        actionBar = getSupportActionBar();
        actionBar.setSubtitle(getString(R.string.alcool_ou_gasolina));

        radioGroupTipoCalculo = findViewById(R.id.radioGroupTipoCalculo);
        radioGroupTipoCalculo.setOnCheckedChangeListener(this);
        radioGroupTipoCalculo.check(R.id.radioButtonCalcularPorVeiculo);
        fragmentManager = getSupportFragmentManager();
        editTextPrecoGasolina = findViewById(R.id.editTextPrecoGasolina);
        editTextPrecoAlcool = findViewById(R.id.editTextPrecoAlcool);

        appPreferencias = new AppPreferencias(this);
        if (appPreferencias.getPrecoGasolina() > 0) {
            editTextPrecoGasolina.setText(String.valueOf(appPreferencias.getPrecoGasolina()));
        }
        if (appPreferencias.getPrecoAlcool() > 0) {
            editTextPrecoAlcool.setText(String.valueOf(appPreferencias.getPrecoAlcool()));
        }

        if (savedInstanceState == null) {
            tipoCalculo = TipoCalculo.VEICULO;
            setFragementTipCalculo(FormCalcularVeiculoFragment.class);
        }

    }

    private void adicionarAnuncio() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.d(LogTAGS.TAG_ADMOB.name(), "onInitializationComplete: " + initializationStatus.getAdapterStatusMap().toString());
            }

        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d(LogTAGS.TAG_ADMOB.name(), "onAdLoaded: ");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                Log.d(LogTAGS.TAG_ADMOB.name(), "onAdFailedToLoad: "+adError.getMessage());
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.d(LogTAGS.TAG_ADMOB.name(), "onAdOpened: ");
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.d(LogTAGS.TAG_ADMOB.name(), "onAdClicked: ");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Log.d(LogTAGS.TAG_ADMOB.name(), "onAdClosed: ");
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int item) {
        switch (item) {
            case R.id.radioButtonCalcularPorVeiculo:
                tipoCalculo = TipoCalculo.VEICULO;
                setFragementTipCalculo(FormCalcularVeiculoFragment.class);
                break;
            case R.id.radioButtonCalcularPorKmsLitro:
                tipoCalculo = TipoCalculo.KMS_LITRO;
                setFragementTipCalculo(FormCalcularKmsLitroFragment.class);
                break;
            case R.id.radioButtonCalcularPorBasico:
                tipoCalculo = TipoCalculo.BASICO;
                setFragementTipCalculo(FormCalcularBasicoFragment.class);
                break;
        }
    }

    private void setFragementTipCalculo(Class<? extends FormCalcularBaseFragment> fragmentClass) {
        String tag = fragmentClass.getSimpleName();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentClass, null, tag)
                .commit();
    }

    @Override
    public void onChangedFormCalcularFragmentListener(Veiculo v, Float kmsGasolina, Float kmsAlcool) {
        veiculo = v;
        kmsLitroGasolina = kmsGasolina;
        kmsLitroAlcool = kmsAlcool;
    }


    public void calcular(View view) {
        if (!validarFormulario()) return;
        Intent i = new Intent();
        switch (tipoCalculo) {
            case BASICO:
                i = new Intent(this, CalculoResultadoBasicoActivity.class);
                setPrecosCombustiveisForIntentCalculo(i);
                startActivity(i);
                break;
            case KMS_LITRO:
                i = new Intent(this, CalculoResultadoKmsLitroActivity.class);
                setPrecosCombustiveisForIntentCalculo(i);
                i.putExtra("kmsGasolina", Float.valueOf(kmsLitroGasolina));
                i.putExtra("kmAlcool", Float.valueOf(kmsLitroAlcool));
                startActivity(i);
                break;
            case VEICULO:
                i = new Intent(this, CalculoResultadoVeiculoActivity.class);
                setPrecosCombustiveisForIntentCalculo(i);
                i.putExtra("veiculo", veiculo);
                startActivity(i);


        }
    }

    private void setPrecosCombustiveisForIntentCalculo(Intent i) {
        i.putExtra("precoGasolina", Float.valueOf(editTextPrecoGasolina.getText().toString()));
        i.putExtra("precoAlcool", Float.valueOf(editTextPrecoAlcool.getText().toString()));
    }

    private boolean validarFormulario() {
        List<String> errors = new LinkedList<>();
        if (TextUtils.isEmpty(editTextPrecoGasolina.getText()))
            errors.add(getString(R.string.infomr_o_preco_gasolina));
        if (TextUtils.isEmpty(editTextPrecoAlcool.getText()))
            errors.add(getString(R.string.informe_preco_alcool));
        switch (tipoCalculo) {
            case KMS_LITRO:
                if (kmsLitroGasolina <= 0)
                    errors.add("Informe o total de kms/litro de gasolina");
                if (kmsLitroAlcool <= 0)
                    errors.add("Informe o total de kms/litro de álcool");
                break;
            case VEICULO:
                if (veiculo == null)
                    errors.add("Selecione um veículo!");
                break;

        }
        if (errors.size() == 0)
            return true;

        StringBuilder sb = new StringBuilder();

        sb.append(errors.get(0));
        for (int i = 1; i < errors.size(); i++) {
            sb.append("\n");
            sb.append(errors.get(i));
        }

        Alerts.alertWaring(this, "Formulário inválido", sb.toString())
                .setPositiveButton("ok", null).show();
        return false;
    }
}
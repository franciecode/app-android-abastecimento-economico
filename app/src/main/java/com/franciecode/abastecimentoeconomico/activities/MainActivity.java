package com.franciecode.abastecimentoeconomico.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentManager;

import com.franciecode.abastecimentoeconomico.R;
import com.franciecode.abastecimentoeconomico.enums.TipoCalculo;
import com.franciecode.abastecimentoeconomico.fragments.FormCalcularBaseFragment;
import com.franciecode.abastecimentoeconomico.fragments.FormCalcularBasicoFragment;
import com.franciecode.abastecimentoeconomico.fragments.FormCalcularKmsLitroFragment;
import com.franciecode.abastecimentoeconomico.fragments.FormCalcularVeiculoFragment;
import com.franciecode.abastecimentoeconomico.models.Veiculo;
import com.franciecode.abastecimentoeconomico.persistencia.AppPreferencias;
import com.franciecode.abastecimentoeconomico.utils.ADMob;
import com.franciecode.abastecimentoeconomico.widgets.Alerts;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends BaseMenuActivity implements RadioGroup.OnCheckedChangeListener, FormCalcularBaseFragment.Listener {
    private final String FRAGMENTO_VEICULO = "veiculo";
    private final String FRAGMENTO_KMS_LITRO = "kmsLitro";
    private final String FRAGMENTO_BASICO = "basico";

    private FragmentManager fragmentManager;
    private RadioGroup radioGroupTipoCalculo;
    private ActionBar actionBar;
    private TipoCalculo tipoCalculo;
    public Float kmsLitroGasolina = 0f;
    public Float kmsLitroAlcool = 0f;
    public Veiculo veiculo;
    private EditText editTextPrecoGasolina;
    private EditText editTextPrecoAlcool;
    private AppPreferencias appPreferencias;
    private ADMob adMob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adMob = new ADMob(this);
        adMob.ancunioMain(findViewById(R.id.frameAnuncioMain));

        actionBar = getSupportActionBar();

        findViewById(R.id.btnCalcular).setOnClickListener(v -> calcular());

        radioGroupTipoCalculo = findViewById(R.id.radioGroupTipoCalculo);
        radioGroupTipoCalculo.setOnCheckedChangeListener(this);
        radioGroupTipoCalculo.check(R.id.radioButtonCalcularPorVeiculo);
        fragmentManager = getSupportFragmentManager();
        editTextPrecoGasolina = findViewById(R.id.editTextPrecoGasolina);
        editTextPrecoAlcool = findViewById(R.id.editTextPrecoAlcool);


        if (savedInstanceState == null) {
            appPreferencias = new AppPreferencias(this);
            if (appPreferencias.getPrecoGasolina() > 0) {
                editTextPrecoGasolina.setText(String.valueOf(appPreferencias.getPrecoGasolina()));
            }
            if (appPreferencias.getPrecoAlcool() > 0) {
                editTextPrecoAlcool.setText(String.valueOf(appPreferencias.getPrecoAlcool()));
            }
            tipoCalculo = TipoCalculo.VEICULO;
            setFragementTipCalculo(FormCalcularVeiculoFragment.class, FRAGMENTO_VEICULO);
        } else {
            tipoCalculo = TipoCalculo.valueOf(savedInstanceState.getString("tipoCalculo"));
            veiculo = savedInstanceState.getParcelable("veiculo");
            if (savedInstanceState.getFloat("precoGasolina") > 0) {
                editTextPrecoGasolina.setText(String.valueOf(savedInstanceState.getFloat("precoGasolina")));
            }
            if (savedInstanceState.getFloat("precoAlcool") > 0) {
                editTextPrecoAlcool.setText(String.valueOf(savedInstanceState.getFloat("precoAlcool")));
            }
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        outState.putString("tipoCalculo", tipoCalculo.name());
        outState.putParcelable("veiculo", veiculo);
        outState.putFloat("precoGasolina", getPrecoGasolina());
        outState.putFloat("precoAlcool", getPrecoAlcool());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int item) {
        switch (item) {
            case R.id.radioButtonCalcularPorVeiculo:
                tipoCalculo = TipoCalculo.VEICULO;
                setFragementTipCalculo(FormCalcularVeiculoFragment.class, FRAGMENTO_VEICULO);
                break;
            case R.id.radioButtonCalcularPorKmsLitro:
                tipoCalculo = TipoCalculo.KMS_LITRO;
                setFragementTipCalculo(FormCalcularKmsLitroFragment.class, FRAGMENTO_KMS_LITRO);
                break;
            case R.id.radioButtonCalcularPorBasico:
                tipoCalculo = TipoCalculo.BASICO;
                setFragementTipCalculo(FormCalcularBasicoFragment.class, FRAGMENTO_BASICO);
                break;
        }
    }

    private void setFragementTipCalculo(Class<? extends FormCalcularBaseFragment> fragmentClass, String nomeFragmento) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentClass, null, nomeFragmento)
                .commit();
    }

    @Override
    public void onChangedFormCalcularFragmentListener(Veiculo v, Float kmsGasolina, Float kmsAlcool) {
        veiculo = v;
        kmsLitroGasolina = kmsGasolina;
        kmsLitroAlcool = kmsAlcool;
    }


    public void calcular() {
        if (!validarFormulario()) return;
        Intent i;
        switch (tipoCalculo) {
            case BASICO:
                i = new Intent(this, CalculoResultadoBasicoActivity.class);
                setPrecosCombustiveisForIntentCalculo(i);
                startActivity(i);
                break;
            case KMS_LITRO:
                i = new Intent(this, CalculoResultadoKmsLitroActivity.class);
                setPrecosCombustiveisForIntentCalculo(i);
                i.putExtra("kmsGasolina", kmsLitroGasolina);
                i.putExtra("kmAlcool", kmsLitroAlcool);
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
        i.putExtra("precoGasolina", getPrecoGasolina());
        i.putExtra("precoAlcool", getPrecoAlcool());
    }

    private float getPrecoGasolina() {
        return Float.valueOf(editTextPrecoGasolina.getText().toString());
    }

    private float getPrecoAlcool() {
        return Float.valueOf(editTextPrecoAlcool.getText().toString());
    }

    private boolean validarFormulario() {
        List<String> errors = new LinkedList<>();
        if (TextUtils.isEmpty(editTextPrecoGasolina.getText()))
            errors.add(getString(R.string.infomr_o_preco_gasolina));
        if (TextUtils.isEmpty(editTextPrecoAlcool.getText()))
            errors.add(getString(R.string.informe_preco_alcool));
        int k = 0;
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
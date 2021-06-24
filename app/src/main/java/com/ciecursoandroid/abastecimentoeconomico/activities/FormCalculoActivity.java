package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCalculo;
import com.ciecursoandroid.abastecimentoeconomico.fragments.FormCalcularBaseFragment;
import com.ciecursoandroid.abastecimentoeconomico.fragments.FormCalcularBasicoFragment;
import com.ciecursoandroid.abastecimentoeconomico.fragments.FormCalcularKmsLitroFragment;
import com.ciecursoandroid.abastecimentoeconomico.fragments.FormCalcularVeiculoFragment;
import com.ciecursoandroid.abastecimentoeconomico.models.Veiculo;
import com.ciecursoandroid.abastecimentoeconomico.widgets.Alerts;

import java.util.LinkedList;
import java.util.List;

public class FormCalculoActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, FormCalcularBaseFragment.Listener {

    private FragmentManager fragmentManager;
    private RadioGroup radioGroupTipoCalculo;
    private ActionBar actionBar;
    private TipoCalculo tipoCalculo;
    Float kmsLitroGasolina = 0f;
    Float kmsLitroAlcool = 0f;
    Veiculo veiculo;
    private EditText editTextPrecoGasolina;
    private EditText editTextPrecoAlcool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_calculo);
        actionBar = getSupportActionBar();
        actionBar.setSubtitle(getString(R.string.alcool_ou_gasolina));

        radioGroupTipoCalculo = findViewById(R.id.radioGroupTipoCalculo);
        radioGroupTipoCalculo.setOnCheckedChangeListener(this);
        radioGroupTipoCalculo.check(R.id.radioButtonCalcularPorVeiculo);
        fragmentManager = getSupportFragmentManager();
        editTextPrecoGasolina = findViewById(R.id.editTextPrecoGasolina);
        editTextPrecoAlcool = findViewById(R.id.editTextPrecoAlcool);

        if (savedInstanceState == null) {
            tipoCalculo = TipoCalculo.VEICULO;
            setFragementTipCalculo(FormCalcularVeiculoFragment.class);
        }

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
    public void onChangedFormCalcularFragment(Veiculo v, Float kmsGasolina, Float kmsAlcool) {
        veiculo = v;
        kmsLitroGasolina = kmsGasolina;
        kmsLitroAlcool = kmsAlcool;
    }


    public void calcular(View view) {
        if (!validarFormulario()) return;
        switch (tipoCalculo) {
            case BASICO:
                Intent i = new Intent(this, CalculoResultadoBasicoActivity.class);
                i.putExtra("precoGasolina", Float.valueOf(editTextPrecoGasolina.getText().toString()));
                i.putExtra("precoAlcool", Float.valueOf(editTextPrecoAlcool.getText().toString()));
                startActivity(i);
        }
    }

    private boolean validarFormulario() {
        List<String> errors = new LinkedList<>();

        if (TextUtils.isEmpty(editTextPrecoGasolina.getText()))
            errors.add(getString(R.string.infomr_o_preco_gasolina));
        if (TextUtils.isEmpty(editTextPrecoAlcool.getText()))
            errors.add(getString(R.string.informe_preco_alcool));
        switch (tipoCalculo) {

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
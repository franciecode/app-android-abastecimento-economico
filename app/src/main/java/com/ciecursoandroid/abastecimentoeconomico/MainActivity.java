package com.ciecursoandroid.abastecimentoeconomico;

import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCalculo;
import com.ciecursoandroid.abastecimentoeconomico.fragments.FormCalcularBasicoFragment;
import com.ciecursoandroid.abastecimentoeconomico.fragments.FormCalcularFragment;
import com.ciecursoandroid.abastecimentoeconomico.fragments.FormCalcularKmsLitroFragment;
import com.ciecursoandroid.abastecimentoeconomico.fragments.FormCalcularVeiculoFragment;
import com.ciecursoandroid.abastecimentoeconomico.models.Veiculo;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, FormCalcularFragment.Listener {

    private FragmentManager fragmentManager;
    private RadioGroup radioGroupTipoCalculo;
    private ActionBar actionBar;
    private TipoCalculo tipoCalculo;
    Float kmsLitroGasolina = 0f;
    Float kmsLitroAlcool = 0f;
    Veiculo veiculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();
        actionBar.setSubtitle(getString(R.string.alcool_ou_gasolina));

        radioGroupTipoCalculo = findViewById(R.id.radioGroupTipoCalculo);
        radioGroupTipoCalculo.setOnCheckedChangeListener(this);
        radioGroupTipoCalculo.check(R.id.radioButtonCalcularPorVeiculo);


        fragmentManager = getSupportFragmentManager();


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

    private void setFragementTipCalculo(Class<? extends FormCalcularFragment> fragmentClass) {
        String tag = fragmentClass.getSimpleName();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentClass, null, tag)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onChangedFormCalcularFragment(Veiculo v, Float kmsGasolina, Float kmsAlcool) {
        veiculo = v;
        kmsLitroGasolina = kmsGasolina;
        kmsLitroAlcool = kmsAlcool;
    }
}
package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProvider;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.models.Veiculo;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.VeiculoRespository;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.viewModel.VeiculoViewModel;
import com.ciecursoandroid.abastecimentoeconomico.widgets.Alerts;

import java.util.LinkedList;
import java.util.List;

public class AddEditVeiculoActivity extends BaseMenuActivity {

    private boolean requestResult;
    EditText editTextNomeVeiculo;
    RadioGroup radioGroupVeiculo;
    RadioButton radioButtonCarro;
    RadioButton radioButtonMoto;
    EditText editTextKmsAlcoolCidade;
    EditText editTextKmsAlcoolRodovia;
    EditText editTextKmsGasolinaRodovia;
    EditText editTextKmsGasolinaCidade;
    String tipoVeiculo;
    VeiculoViewModel veiculoViewModel;
    Veiculo veiculoEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_veiculo);

        findViewById(R.id.buttonSaveVeiculo).setOnClickListener(v -> save());
        findViewById(R.id.buttonCancelSaveVeiculo).setOnClickListener(v -> cancelarSalvar());

        // INTENT EXTRAS
        veiculoEdit = getIntent().getParcelableExtra("veiculo");
        requestResult = getIntent().getBooleanExtra("requestResult", false);

        // Fields
        veiculoViewModel = new ViewModelProvider(this).get(VeiculoViewModel.class);
        veiculoViewModel.setRespository(new VeiculoRespository(this));

        // Fields Views
        editTextNomeVeiculo = findViewById(R.id.editTextNomeVeiculo);
        radioGroupVeiculo = findViewById(R.id.radioGroupVeiculo);
        radioButtonCarro = findViewById(R.id.radioButtonCarro);
        radioButtonMoto = findViewById(R.id.radioButtonMoto);
        editTextKmsAlcoolCidade = findViewById(R.id.editTextKmsAlcoolCidade);
        editTextKmsAlcoolRodovia = findViewById(R.id.editTextKmsAlcoolRodovia);
        editTextKmsGasolinaCidade = findViewById(R.id.editTextKmsGasolinaCidade);
        editTextKmsGasolinaRodovia = findViewById(R.id.editTextKmsGasolinaRodovia);


        // Actions
        radioGroupVeiculo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonCarro:
                        tipoVeiculo = Veiculo.TIPO_VEICULO_CARRO;
                        break;
                    case R.id.radioButtonMoto:
                        tipoVeiculo = Veiculo.TIPO_VEICULO_MOTO;
                        break;
                }
            }
        });
        ActionBar actionBar = getSupportActionBar();
        // Preencher formulário com dados do veiculo a ser editado
        if (veiculoEdit != null) {
            actionBar.setTitle(R.string.atualizar_veiculo);
            editTextNomeVeiculo.setText(veiculoEdit.getNome());
            if (veiculoEdit.getTipo().equals(Veiculo.TIPO_VEICULO_CARRO)) {
                radioButtonCarro.setChecked(true);
            } else {
                radioButtonMoto.setChecked(true);
            }
            editTextKmsAlcoolCidade.setText(veiculoEdit.getKmsLitroCidadeAlcool() + "");
            editTextKmsAlcoolRodovia.setText(veiculoEdit.getKmsLitroRodoviaAlcool() + "");
            editTextKmsGasolinaCidade.setText(veiculoEdit.getKmsLitroCidadeGasolina() + "");
            editTextKmsGasolinaRodovia.setText(veiculoEdit.getKmsLitroRodoviaGasolina() + "");
        }
    }

    private void cancelarSalvar() {
        finish();
    }

    public void save() {
        if (!formValidate()) return;
        if (veiculoEdit == null)
            inserir();
        else
            update();
    }

    private void update() {
        setDataVeiculoFromForm(veiculoEdit);
        veiculoViewModel.update(veiculoEdit, new VeiculoRespository.OnUpdateListener() {
            @Override
            public void onComplete(Exception e, Veiculo veiculo) {
                if (e != null) {
                    String erro;
                    if (e.getMessage().contains("UNIQUE constraint failed: table_veiculo.nome"))
                        erro = getResources().getString(R.string.nom_de_veiculo_ja_cadastrado);
                    else
                        erro = e.getMessage();
                    Alerts.alertWaring(AddEditVeiculoActivity.this,
                            getString(R.string.erro_ao_salvar_veiculo), erro)
                            .setPositiveButton("ok", null)
                            .show();
                } else {
                    Alerts.alertSuccess(AddEditVeiculoActivity.this, "Sucesso",
                            "Veiculo atualizado com sucesso.")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            }).show();

                }
            }
        });
    }

    private void inserir() {
        Veiculo newVeiculo = new Veiculo();
        setDataVeiculoFromForm(newVeiculo);
        veiculoViewModel.insertVeiculo(newVeiculo, new VeiculoRespository.OnInsert() {
            @Override
            public void onComplete(Exception e, Veiculo newVeiculo) {
                if (e != null) {
                    String erro;
                    if (e.getMessage().contains("UNIQUE constraint failed: table_veiculo.nome"))
                        erro = getResources().getString(R.string.nom_de_veiculo_ja_cadastrado);
                    else
                        erro = e.getMessage();
                    Alerts.alertWaring(AddEditVeiculoActivity.this,
                            getString(R.string.erro_ao_salvar_veiculo), erro)
                            .setPositiveButton("ok", null)
                            .show();
                } else {
                    Alerts.alertSuccess(AddEditVeiculoActivity.this, "Sucesso",
                            "Veiculo cadastrado com sucesso.")
                            .setPositiveButton("ok", (dialogInterface, i) -> {
                                if (requestResult) {
                                    Intent intentResult = new Intent();
                                    intentResult.putExtra("veiculoId", newVeiculo.getId());
                                    setResult(Activity.RESULT_OK, intentResult);
                                    finish();
                                } else {
                                    finish();
                                }
                            }).show();

                }
            }
        });
    }

    private void setDataVeiculoFromForm(Veiculo veiculo) {
        veiculo.setTipo(tipoVeiculo);
        veiculo.setNome(editTextNomeVeiculo.getText().toString().trim().toLowerCase());
        veiculo.setKmsLitroCidadeAlcool(Float.parseFloat(editTextKmsAlcoolCidade.getText().toString().trim()));
        veiculo.setKmsLitroRodoviaAlcool(Float.parseFloat(editTextKmsAlcoolRodovia.getText().toString().trim()));
        veiculo.setKmsLitroCidadeGasolina(Float.parseFloat(editTextKmsGasolinaCidade.getText().toString().trim()));
        veiculo.setKmsLitroRodoviaGasolina(Float.parseFloat(editTextKmsGasolinaRodovia.getText().toString().trim()));
    }

    private boolean formValidate() {
        List<String> errors = new LinkedList<>();
        if (tipoVeiculo == null)
            errors.add(getString(R.string.form_error_addEditVeiculo__veiculo_nao_informado));
        if (TextUtils.isEmpty(editTextNomeVeiculo.getText()))
            errors.add(getString(R.string.form_error_addEditVeiculo__nome_veiculo_nao_informado));
        if (TextUtils.isEmpty(editTextKmsAlcoolCidade.getText()))
            errors.add(getString(R.string.form_error_addEditVeiculo__kmsAlcoolCidade_nao_informado));
        else if (Float.valueOf(editTextKmsAlcoolCidade.getText().toString()) <= 0)
            errors.add("Kms/Litro de álcool na cidade deve ser maior que 0");
        if (TextUtils.isEmpty(editTextKmsAlcoolRodovia.getText()))
            errors.add(getString(R.string.form_error_addEditVeiculo__kmsAlcoolRodovia_nao_informado));
        else if (Float.valueOf(editTextKmsAlcoolRodovia.getText().toString()) <= 0)
            errors.add("Kms/Litro de álcool na rodovia deve ser maior que 0");
        if (TextUtils.isEmpty(editTextKmsGasolinaCidade.getText()))
            errors.add(getString(R.string.form_error_addEditVeiculo__kmsGasolinaCidade_nao_informado));
        else if (Float.valueOf(editTextKmsGasolinaCidade.getText().toString()) <= 0)
            errors.add("Kms/Litro gasolina na cidade deve ser maior que 0");
        if (TextUtils.isEmpty(editTextKmsGasolinaRodovia.getText()))
            errors.add(getString(R.string.form_error_addEditVeiculo__kmsGasolinaRodovia_nao_informado));
        else if (Float.valueOf(editTextKmsGasolinaRodovia.getText().toString()) <= 0)
            errors.add("Kms/Litro de gasolina na rodovia deve ser maior que 0");

        if (errors.size() == 0)
            return true;

        StringBuilder sb = new StringBuilder();
        sb.append(errors.get(0));
        for (int i = 1; i < errors.size(); i++) {
            sb.append("\n");
            sb.append(errors.get(i));
        }
        Alerts.alertWaring(this, "Formulário inválido", sb.toString())
                .setPositiveButton("ok", null)
                .show();
        return false;
    }
}
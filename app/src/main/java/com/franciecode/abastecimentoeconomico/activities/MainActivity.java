package com.franciecode.abastecimentoeconomico.activities;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentManager;

import com.flurry.android.FlurryAgent;
import com.franciecode.abastecimentoeconomico.R;
import com.franciecode.abastecimentoeconomico.enums.TipoCalculo;
import com.franciecode.abastecimentoeconomico.fragments.FormCalcularBaseFragment;
import com.franciecode.abastecimentoeconomico.fragments.FormCalcularBasicoFragment;
import com.franciecode.abastecimentoeconomico.fragments.FormCalcularKmsLitroFragment;
import com.franciecode.abastecimentoeconomico.fragments.FormCalcularVeiculoFragment;
import com.franciecode.abastecimentoeconomico.models.Veiculo;
import com.franciecode.abastecimentoeconomico.persistencia.AppPreferencias;
import com.franciecode.abastecimentoeconomico.utils.ADMob;
import com.franciecode.abastecimentoeconomico.utils.RequestCodesApp;
import com.franciecode.abastecimentoeconomico.widgets.Alerts;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends BaseMenuActivity implements RadioGroup.OnCheckedChangeListener, FormCalcularBaseFragment.Listener {
    public final String TAG = MainActivity.class.getCanonicalName();
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
    private Bundle mSavedInstanceState;
    AppUpdateManager appUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appUpdateManager = AppUpdateManagerFactory.create(this);
        // Flurry
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "HQZBPGJRD82GHC3FPHTB");

        mSavedInstanceState = savedInstanceState;

        adMob = new ADMob(this);
        adMob.ancunioMain(findViewById(R.id.frameAnuncioMain));

        actionBar = getSupportActionBar();

        findViewById(R.id.btnCalcular).setOnClickListener(v -> calcular());
        findViewById(R.id.radioButtonCalcularPorVeiculo).setOnClickListener(V -> carregarFramentCalculo(TipoCalculo.VEICULO));
        findViewById(R.id.radioButtonCalcularPorKmsLitro).setOnClickListener(V -> carregarFramentCalculo(TipoCalculo.KMS_LITRO));
        findViewById(R.id.radioButtonCalcularPorBasico).setOnClickListener(V -> carregarFramentCalculo(TipoCalculo.BASICO));

        fragmentManager = getSupportFragmentManager();
        editTextPrecoGasolina = findViewById(R.id.editTextPrecoGasolina);
        editTextPrecoAlcool = findViewById(R.id.editTextPrecoAlcool);


        if (savedInstanceState == null) {
            carregarFramentCalculo(TipoCalculo.VEICULO);

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
    protected void onResume() {
        super.onResume();
        atualizarApp();
    }

    private void atualizarApp() {
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo,
                                AppUpdateType.FLEXIBLE,
                                MainActivity.this, RequestCodesApp.UPDATED_APP_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        appUpdateManager.registerListener(listenerAppUpdate);
    }

    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(android.R.id.content),
                        "Uma atualização acabou de ser baixada.",
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Instalar", view -> appUpdateManager.completeUpdate());
        snackbar.setActionTextColor(getResources().getColor(R.color.snackbar_action_text_color));
        snackbar.show();
    }

    // Create a listener to track request state updates.
    InstallStateUpdatedListener listenerAppUpdate = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(InstallState state) {
            if (appUpdateManager == null) return;
            if (state.installStatus() == InstallStatus.DOWNLOADED)
                popupSnackbarForCompleteUpdate();
        }
    };


    private void carregarFramentCalculo(TipoCalculo tipo) {
        switch (tipo) {
            case VEICULO:
                tipoCalculo = TipoCalculo.VEICULO;
                setFragementTipCalculo(FormCalcularVeiculoFragment.class, FRAGMENTO_VEICULO);
                break;
            case KMS_LITRO:
                tipoCalculo = TipoCalculo.KMS_LITRO;
                setFragementTipCalculo(FormCalcularKmsLitroFragment.class, FRAGMENTO_KMS_LITRO);
                break;
            case BASICO:
                tipoCalculo = TipoCalculo.BASICO;
                setFragementTipCalculo(FormCalcularBasicoFragment.class, FRAGMENTO_BASICO);
                break;
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
        if (TextUtils.isEmpty(editTextPrecoGasolina.getText()))
            return 0f;
        return Float.valueOf(editTextPrecoGasolina.getText().toString());
    }

    private float getPrecoAlcool() {
        if (TextUtils.isEmpty(editTextPrecoAlcool.getText()))
            return 0f;
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
                if (kmsLitroGasolina == null || kmsLitroGasolina <= 0)
                    errors.add("Informe o total de kms/litro de gasolina");
                if (kmsLitroAlcool == null || kmsLitroAlcool <= 0)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        switch (requestCode) {
            case RequestCodesApp.UPDATED_APP_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "Baixando a atualização", Toast.LENGTH_SHORT).show();
                } else {
                    appUpdateManager.unregisterListener(listenerAppUpdate);
                    Toast.makeText(this, "Erro ao atualizar o app. Por favor, tente mais tarde.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        if (appUpdateManager != null) appUpdateManager.unregisterListener(listenerAppUpdate);
        super.onStop();
    }
}
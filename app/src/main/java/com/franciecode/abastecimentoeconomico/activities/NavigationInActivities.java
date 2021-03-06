package com.franciecode.abastecimentoeconomico.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;

import com.franciecode.abastecimentoeconomico.models.Veiculo;

public class NavigationInActivities {
    public static void goAbastecimentos(Activity activity) {
        Intent i = new Intent(activity, AbastecimentosActivity.class);
        activity.startActivity(i);
    }

    public static void goAddVeiculoForResult(Activity activity, ActivityResultLauncher<Intent> launcher) {
        Intent i = new Intent(activity, AddEditVeiculoActivity.class);
        i.putExtra("requestResult", true);
        launcher.launch(i);

    }

    public static void goEditVeiculo(Activity activity, Veiculo veiculo) {
        Intent intent = new Intent(activity, AddEditVeiculoActivity.class);
        intent.putExtra("veiculo", veiculo);
        activity.startActivity(intent);
    }

    public static void goAddVeiculo(Activity activity) {
        Intent i = new Intent(activity, AddEditVeiculoActivity.class);
        activity.startActivity(i);
    }
}

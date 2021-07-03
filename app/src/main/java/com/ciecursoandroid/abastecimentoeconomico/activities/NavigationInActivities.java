package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;

import com.ciecursoandroid.abastecimentoeconomico.models.Veiculo;

public class NavigationInActivities {
    public static void goAbastecimentos(Context context) {
        Intent i = new Intent(context, AbastecimentosActivity.class);
        context.startActivity(i);
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

    public static void goAddVeiculo(VeiculosActivity activity) {
        Intent i = new Intent(activity, AddEditVeiculoActivity.class);
        activity.startActivity(i);
    }
}

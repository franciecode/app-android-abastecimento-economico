package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;

public class ActivitiesNavigation {
    public static void goAbastecimentos(Context context) {
        Intent i = new Intent(context, AbastecimentosActivity.class);
        context.startActivity(i);
    }

    public static void goAddVeiculoForResult(Activity activity, ActivityResultLauncher<Intent> launcher) {
        Intent i = new Intent(activity, AddEditVeiculoActivity.class);
        i.putExtra("requestResult", true);
        launcher.launch(i);

    }
}

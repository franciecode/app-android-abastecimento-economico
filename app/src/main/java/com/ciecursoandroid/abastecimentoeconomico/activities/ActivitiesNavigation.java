package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.FragmentActivity;

public class ActivitiesNavigation {
    public static void goAbastecimentos(Context context) {
        Intent i = new Intent(context, AbastecimentosActivity.class);
        context.startActivity(i);
    }

    public static void goAddVeiculoForResult(FragmentActivity activity) {
        Intent i = new Intent(activity, AddEditVeiculoActivity.class);
        activity.startActivity(i);

    }
}

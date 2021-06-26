package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.content.Context;
import android.content.Intent;

public class ActivitiesNavigation {
    public static void goAbastecimentos(Context context) {
        Intent i = new Intent(context, AbastecimentosActivity.class);
        context.startActivity(i);
    }
}

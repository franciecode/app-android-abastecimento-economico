package com.ciecursoandroid.abastecimentoeconomico.persistencia;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferencias {
    public final String PRECO_GASOLINA = "precoGasolina";
    public final String PRECO_ALCOOL = "precoAlcool";
    private final SharedPreferences.Editor editor;
    SharedPreferences sharePreferences;

    public AppPreferencias(Activity activity) {
        sharePreferences = activity.getSharedPreferences("appPref", Context.MODE_PRIVATE);
        editor = sharePreferences.edit();
    }

    public float getPrecoGasolina() {
        return sharePreferences.getFloat(PRECO_GASOLINA, 0);
    }

    public void setPrecoGasolina(Float valor) {
        editor.putFloat(PRECO_GASOLINA, valor);
    }

    public float getPrecoAlcool() {
        return sharePreferences.getFloat(PRECO_ALCOOL, 0);
    }

    public void setPrecoAlcool(Float valor) {
        editor.putFloat(PRECO_ALCOOL, valor);
    }

    public void commit() {
        editor.commit();
    }
}

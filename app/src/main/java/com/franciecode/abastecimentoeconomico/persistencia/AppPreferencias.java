package com.franciecode.abastecimentoeconomico.persistencia;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferencias {
    public final String PRECO_GASOLINA = "precoGasolina";
    public final String PRECO_ALCOOL = "precoAlcool";
    public final String VEICULO_ID_ULTIMO_ABASTECIMENTO = "veiculoIdUltimoAbastecimento";
    private final SharedPreferences.Editor editor;
    SharedPreferences prefs;

    public AppPreferencias(Activity activity) {
        prefs = activity.getSharedPreferences("appPref", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setVeiculoIdDoUltimoAbastecimentoPorVeiclo(long veiculoId) {
        editor.putLong(VEICULO_ID_ULTIMO_ABASTECIMENTO, veiculoId);
    }

    public long getVeiculoIdDoUltimoAbastecimentoPorVeiclo() {
        return prefs.getLong(VEICULO_ID_ULTIMO_ABASTECIMENTO, -1);
    }

    public float getPrecoGasolina() {
        return prefs.getFloat(PRECO_GASOLINA, 0);
    }

    public void setPrecoGasolina(Float valor) {
        editor.putFloat(PRECO_GASOLINA, valor);
    }

    public float getPrecoAlcool() {
        return prefs.getFloat(PRECO_ALCOOL, 0);
    }

    public void setPrecoAlcool(Float valor) {
        editor.putFloat(PRECO_ALCOOL, valor);
    }

    public void commit() {
        editor.commit();
    }
}

package com.ciecursoandroid.abastecimentoeconomico.widgets;

import android.app.AlertDialog;
import android.content.Context;

import com.ciecursoandroid.abastecimentoeconomico.R;

public class Alerts {
    static public AlertDialog.Builder alertWaring(Context context, String title, String msg) {
        AlertDialog.Builder al = new AlertDialog.Builder(context);
        al.setTitle(title)
                .setMessage(msg)
                .setIcon(R.drawable.ic_warning);
        return al;
    }

    static public AlertDialog.Builder alertSuccess(Context context, String title, String msg) {
        AlertDialog.Builder al = new AlertDialog.Builder(context);
        al.setTitle(title)
                .setMessage(msg)
                .setIcon(R.drawable.ic_success);
        return al;
    }
}

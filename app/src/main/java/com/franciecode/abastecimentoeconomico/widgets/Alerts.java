package com.franciecode.abastecimentoeconomico.widgets;

import android.app.AlertDialog;
import android.content.Context;

import com.franciecode.abastecimentoeconomico.R;

public class Alerts {
    static public AlertDialog.Builder alertWaring(Context context, String title, String msg) {
        AlertDialog.Builder al = new AlertDialog.Builder(context);
        al.setTitle(title)
                .setMessage(msg)
                .setIcon(R.drawable.ic_warning);
        return al;
    }

    static public AlertDialog.Builder alertError(Context context, String title, String msg) {
        AlertDialog.Builder al = new AlertDialog.Builder(context);
        al.setTitle(title)
                .setMessage(msg)
                .setIcon(R.drawable.ic_error);
        return al;
    }

    static public AlertDialog.Builder alertSuccess(Context context, String title, String msg) {
        AlertDialog.Builder al = new AlertDialog.Builder(context);
        al.setTitle(title)
                .setCancelable(false)
                .setMessage(msg)
                .setIcon(R.drawable.ic_success);
        return al;
    }

    static public AlertDialog.Builder confirm(Context context, String title, String msg) {
        AlertDialog.Builder al = new AlertDialog.Builder(context);
        al.setTitle(title)
                .setCancelable(false)
                .setMessage(msg);
        return al;
    }

}

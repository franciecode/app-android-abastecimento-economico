package com.franciecode.abastecimentoeconomico.utils;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class ADMob {
    private final boolean mostrarAnuncioTelaMain = false;
    private final boolean mostrarAnuncioTelaAbastecimentos = false;
    private final boolean mostrarAnuncioTelaVeiculos = false;
    private final boolean mostrarAnuncioTelaChart = false;
    private final boolean mostrarAnuncioTelaAddVeiculo = false;

    Activity activity;

    public ADMob(Activity activity) {
        if (mostrarAnuncioTelaMain ||
                mostrarAnuncioTelaAbastecimentos ||
                mostrarAnuncioTelaVeiculos ||
                mostrarAnuncioTelaAbastecimentos ||
                mostrarAnuncioTelaChart ||
                mostrarAnuncioTelaAddVeiculo
        ) {
            this.activity = activity;
            MobileAds.initialize(activity, initializationStatus -> {
            });
        }
    }

    public void ancunioMain(FrameLayout frameLayout) {
    }

    public void anuncioChart(FrameLayout frame) {
        if (mostrarAnuncioTelaMain == false) return;
        AdView adView = new AdView(activity);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-2036643128150326/9696332242");
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        frame.addView(adView);
        frame.setVisibility(View.INVISIBLE);
    }

    public void anuncioVeiculos() {

    }

    public void anuncioAbastecimentos() {

    }

    private void mostrarAnuncioMain(FrameLayout frameLayout) {

    }

}

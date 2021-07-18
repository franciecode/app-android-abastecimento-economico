package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.enums.LogTAGS;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import org.jetbrains.annotations.NotNull;

public abstract class BaseMenuActivity extends AppCompatActivity {
    public static int contagemRegressivaMotrarAnuncioTelaCheia = 0;
    private static InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NotNull InitializationStatus initializationStatus) {

            }
        });
        Log.d(LogTAGS.TAG_ADMOB.name(), "contagemRegressivaMotrarAnuncioTelaCheia: " + contagemRegressivaMotrarAnuncioTelaCheia);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadAdFullScreen();
    }

    private void loadAdFullScreen() {
        Log.d(LogTAGS.TAG_ADMOB.name(), "loadAdFullScreen: ");
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, "ca-app-pub-2036643128150326/8305722355", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        Log.d(LogTAGS.TAG_ADMOB.name(), "onAdLoaded: ");
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(LogTAGS.TAG_ADMOB.name(), "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(LogTAGS.TAG_ADMOB.name(), loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_abastecimentos:
                if (this instanceof AbastecimentosActivity) return false;
                mostrarAnuncioTelaCheia(() -> {
                    Log.d(LogTAGS.TAG_ADMOB.name(), "actionHere: ");
                    Intent iAbastecimento = new Intent(BaseMenuActivity.this, AbastecimentosActivity.class);
                    BaseMenuActivity.this.startActivity(iAbastecimento);
                });
                break;
            case R.id.menu_abastecer:
                if (this instanceof MainActivity) return false;
                Intent iAbastecer = new Intent(this, MainActivity.class);
                startActivity(iAbastecer);
                break;
            case R.id.menu_veiculos:
                if (this instanceof VeiculosActivity) return false;
                Intent iVeiculos = new Intent(this, VeiculosActivity.class);
                startActivity(iVeiculos);
                break;
            case R.id.menu_chart:
                mostrarAnuncioTelaCheia(() -> {
                    Log.d(LogTAGS.TAG_ADMOB.name(), "actionHere: ");
                    Intent iChart = new Intent(this, ChartActivity.class);
                    startActivity(iChart);
                });
                break;
        }
        contagemRegressivaMotrarAnuncioTelaCheia--;
        return true;
    }

    private void mostrarAnuncioTelaCheia(OnFullScreenADListener listener) {
        if (mInterstitialAd != null && contagemRegressivaMotrarAnuncioTelaCheia <= 0) {
            contagemRegressivaMotrarAnuncioTelaCheia = 3;
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull @NotNull AdError adError) {
                    Log.d(LogTAGS.TAG_ADMOB.name(), "onAdFailedToShowFullScreenContent");
                    listener.done();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    Log.d(LogTAGS.TAG_ADMOB.name(), "onAdShowedFullScreenContent");

                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    Log.d(LogTAGS.TAG_ADMOB.name(), "onAdDismissedFullScreenContent");
                    listener.done();
                }

                @Override
                public void onAdImpression() {
                    Log.d(LogTAGS.TAG_ADMOB.name(), "onAdImpression");
                }
            });
            mInterstitialAd.show(BaseMenuActivity.this);
        } else {
            listener.done();
            Log.d(LogTAGS.TAG_ADMOB.name(), "The interstitial ad wasn't ready yet.");
        }
    }

    private interface OnFullScreenADListener {
        void done();
    }

}

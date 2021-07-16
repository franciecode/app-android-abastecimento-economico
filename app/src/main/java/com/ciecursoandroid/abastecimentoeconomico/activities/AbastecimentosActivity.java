package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.adapters.AbastecimentosAdapter;
import com.ciecursoandroid.abastecimentoeconomico.enums.LogTAGS;
import com.ciecursoandroid.abastecimentoeconomico.models.AbastecimentoComVeiculo;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.AbastecimentoRepository;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.VeiculoRespository;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.viewModel.AbastecimentoViewModel;
import com.ciecursoandroid.abastecimentoeconomico.widgets.Alerts;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.List;

public class AbastecimentosActivity extends BaseMenuActivity implements AbastecimentosAdapter.AdapterObserver {

    private final static String TAG = AbastecimentosActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private AbastecimentosAdapter abastecimentosAdapter;
    private AbastecimentoViewModel abastecimentoViewModel;
    private TextView textViewTotalRegistros;
    private TextView textViewTotaGasto;
    TextView textViewTotalEconomizado;
    private MenuItem search;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abastecimentos);

        adicinarAcuncio();

        textViewTotalRegistros = findViewById(R.id.textViewTotalRegistros);
        textViewTotaGasto = findViewById(R.id.textViewTotalGasto);
        textViewTotalEconomizado = findViewById(R.id.textViewTotalEconomizado);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        abastecimentosAdapter = new AbastecimentosAdapter(this, this);
        recyclerView.setAdapter(abastecimentosAdapter);


        abastecimentoViewModel = new ViewModelProvider(this).get(AbastecimentoViewModel.class);
        abastecimentoViewModel.setRepository(new AbastecimentoRepository(this));
        abastecimentoViewModel.getAbastecimentoComVeiculos().observe(this, new Observer<List<AbastecimentoComVeiculo>>() {
            @Override
            public void onChanged(List<AbastecimentoComVeiculo> abastecimentoComVeiculos) {
                abastecimentosAdapter.setAbastecimentos(abastecimentoComVeiculos);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void adicinarAcuncio() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, "ca-app-pub-2036643128150326/8305722355", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Log.d(LogTAGS.TAG_ADMOB.name(), "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d(LogTAGS.TAG_ADMOB.name(), "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                mInterstitialAd = null;
                                Log.d(LogTAGS.TAG_ADMOB.name(), "The ad was shown.");
                            }
                        });
                        mInterstitialAd.show(AbastecimentosActivity.this);
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

    private void setViewsResumoTotal(List<AbastecimentoComVeiculo> all, List<AbastecimentoComVeiculo> filtrados) {
        if (all.size() > 0) {
            float totalEconomizado = 0, totalGasto = 0;
            List<AbastecimentoComVeiculo> listCalculo = filtrados != null ? filtrados : all;
            for (AbastecimentoComVeiculo abastecimentoComVeiculos : listCalculo) {
                totalGasto += abastecimentoComVeiculos.abastecimento.getTotalPago();
                totalEconomizado += abastecimentoComVeiculos.abastecimento.getValorEconomizado();
            }
            int monstrando = filtrados == null ? all.size() : filtrados.size();
            textViewTotalRegistros.setText(monstrando + "/" + all.size());
            textViewTotaGasto.setText(String.format(getString(R.string.valor_dinheiro), totalGasto));
            textViewTotalEconomizado.setText(String.format(getString(R.string.valor_dinheiro), totalEconomizado));
            if (totalEconomizado < 0) {
                textViewTotalEconomizado.setTextColor(getResources().getColor(R.color.color_text_red));
            } else {
                textViewTotalEconomizado.setTextColor(getResources().getColor(R.color.color_text_green));
            }
        } else {
            textViewTotalRegistros.setText("0/0");
            textViewTotaGasto.setText(getString(R.string.dinheiro_vazio));
            textViewTotalEconomizado.setText(getString(R.string.dinheiro_vazio));
            textViewTotalEconomizado.setTextColor(getResources().getColor(R.color.color_text_green));
        }
    }

    @Override
    public void onItemClick(AbastecimentoComVeiculo abastecimento, int position) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        search = menu.findItem(R.id.app_bar_search);
        search.setVisible(true);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filtrarAbastecimento(s);
                return false;
            }
        });
        return true;
    }

    private void filtrarAbastecimento(String s) {
        abastecimentosAdapter.getFilter().filter(s);
    }

    @Override
    public boolean onLongItemClick(AbastecimentoComVeiculo abastecimentoComVeiculo, int position) {

        String dataAbastecimento = DateFormat.format(getString(R.string.data_hora), abastecimentoComVeiculo.abastecimento.getDataAbastecimento()).toString();
        Alerts.alertWaring(this, getString(R.string.remover_abastecimento),
                getString(R.string.confirma_remover_abastecimento) + dataAbastecimento
        ).setPositiveButton(R.string.remover, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                abastecimentoViewModel.delete(abastecimentoComVeiculo.abastecimento, new VeiculoRespository.OnDeleteListener() {
                    @Override
                    public void onComplete(Exception e) {
                        if (e != null) {
                            Alerts.alertError(AbastecimentosActivity.this,
                                    getString(R.string.erro_ao_remover_abastecimento),
                                    e.getMessage())
                                    .setPositiveButton("ok", null)
                                    .show();
                        } else {
                            Toast.makeText(AbastecimentosActivity.this,
                                    getString(R.string.abasteciment_removido_com_sucesso),
                                    Toast.LENGTH_SHORT).show();
                            abastecimentosAdapter.removeItem(position);
                        }
                    }
                });
            }
        }).setNegativeButton(getString(R.string.cancelar), null)
                .show();
        return false;
    }

    @Override
    public void onChangeListener(List<AbastecimentoComVeiculo> filtrados, List<AbastecimentoComVeiculo> todos) {
        setViewsResumoTotal(todos, filtrados);
    }
}
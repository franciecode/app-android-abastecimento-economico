package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.adapters.VeiculosViewPager2Adapeter;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.VeiculoRespository;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.viewModel.VeiculoViewModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

public class VeiculosActivity extends BaseMenuActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private VeiculoViewModel veiculoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiculos);

        veiculoViewModel = new ViewModelProvider(this).get(VeiculoViewModel.class);
        veiculoViewModel.setRespository(new VeiculoRespository(this));


        // field views
        tabLayout = findViewById(R.id.tabLayoutVeiculos);
        viewPager2 = findViewById(R.id.viewPager2Veiculos);

        // Config
        VeiculosViewPager2Adapeter veiculosViewPager2Adapeter = new VeiculosViewPager2Adapeter(this);
        viewPager2.setAdapter(veiculosViewPager2Adapeter);

        // Actions
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText(R.string.cadastrados);
                    veiculoViewModel.getTotalCadastrados().observe(VeiculosActivity.this, new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            BadgeDrawable badge = tab.getOrCreateBadge();
                            badge.setNumber(integer);
                            badge.setBackgroundColor(ContextCompat.getColor(VeiculosActivity.this, R.color.black));
                        }
                    });
                } else {
                    tab.setIcon(R.drawable.ic_baseline_delete_24);
                    veiculoViewModel.getTotalRemovidos().observe(VeiculosActivity.this, new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            tab.getOrCreateBadge().setNumber(integer);
                        }
                    });
                }
            }
        });
        mediator.attach();
    }

}
package com.ciecursoandroid.abastecimentoeconomico.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.adapters.VeiculosViewPager2Adapeter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

public class VeiculosActivity extends BaseMenuActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiculos);

        tabLayout = findViewById(R.id.tabLayoutVeiculos);

        viewPager2 = findViewById(R.id.viewPager2Veiculos);
        VeiculosViewPager2Adapeter veiculosViewPager2Adapeter = new VeiculosViewPager2Adapeter(this);
        viewPager2.setAdapter(veiculosViewPager2Adapeter);

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull @NotNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText(R.string.cadastrados);
                } else {
                    tab.setIcon(R.drawable.ic_baseline_delete_24);
                }
            }
        }).attach();
    }
}
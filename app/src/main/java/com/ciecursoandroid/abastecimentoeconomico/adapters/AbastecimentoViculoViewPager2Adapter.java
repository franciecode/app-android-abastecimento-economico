package com.ciecursoandroid.abastecimentoeconomico.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ciecursoandroid.abastecimentoeconomico.activities.CalculoResultadoVeiculoActivity;
import com.ciecursoandroid.abastecimentoeconomico.enums.LocalViagem;
import com.ciecursoandroid.abastecimentoeconomico.fragments.CalculoResultadoVeiculoFragment;

import org.jetbrains.annotations.NotNull;

public class AbastecimentoViculoViewPager2Adapter extends FragmentStateAdapter {
    CalculoResultadoVeiculoActivity calculoResultadoVeiculoActivity;

    public AbastecimentoViculoViewPager2Adapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        calculoResultadoVeiculoActivity = (CalculoResultadoVeiculoActivity) fragmentActivity;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return CalculoResultadoVeiculoFragment.newInstance(
                        LocalViagem.CIDADE,
                        calculoResultadoVeiculoActivity.getPrecoGasolina(),
                        calculoResultadoVeiculoActivity.getPreoAlcool(),
                        calculoResultadoVeiculoActivity.getVeiculo());
            case 1:
                return CalculoResultadoVeiculoFragment.newInstance(
                        LocalViagem.RODOVIA,
                        calculoResultadoVeiculoActivity.getPrecoGasolina(),
                        calculoResultadoVeiculoActivity.getPreoAlcool(),
                        calculoResultadoVeiculoActivity.getVeiculo());

        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

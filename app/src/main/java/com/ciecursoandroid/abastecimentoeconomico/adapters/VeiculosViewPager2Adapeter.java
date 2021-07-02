package com.ciecursoandroid.abastecimentoeconomico.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ciecursoandroid.abastecimentoeconomico.fragments.VeiculosFragment;

import org.jetbrains.annotations.NotNull;

public class VeiculosViewPager2Adapeter extends FragmentStateAdapter {

    public VeiculosViewPager2Adapeter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new VeiculosFragment();
        Bundle args = new Bundle();
        args.putBoolean(VeiculosFragment.IS_TRASH, position == 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

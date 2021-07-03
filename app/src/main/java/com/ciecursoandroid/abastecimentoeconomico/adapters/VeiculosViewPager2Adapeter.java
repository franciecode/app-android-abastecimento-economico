package com.ciecursoandroid.abastecimentoeconomico.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ciecursoandroid.abastecimentoeconomico.fragments.VeiculosFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VeiculosViewPager2Adapeter extends FragmentStateAdapter {
    private static final String TAG = VeiculosViewPager2Adapeter.class.getSimpleName();
    List<VeiculosFragment> fragments = new ArrayList<>(2);

    public VeiculosViewPager2Adapeter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragments.add(VeiculosFragment.newInstance(false));
        fragments.add(VeiculosFragment.newInstance(true));
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}

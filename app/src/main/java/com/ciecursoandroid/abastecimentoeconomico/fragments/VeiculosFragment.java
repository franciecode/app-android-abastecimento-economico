package com.ciecursoandroid.abastecimentoeconomico.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ciecursoandroid.abastecimentoeconomico.R;
import com.ciecursoandroid.abastecimentoeconomico.activities.NavigationInActivities;
import com.ciecursoandroid.abastecimentoeconomico.adapters.VeiculosAdapter;
import com.ciecursoandroid.abastecimentoeconomico.models.Veiculo;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.VeiculoRespository;
import com.ciecursoandroid.abastecimentoeconomico.persistencia.viewModel.VeiculoViewModel;
import com.ciecursoandroid.abastecimentoeconomico.widgets.Alerts;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VeiculosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VeiculosFragment extends Fragment {

    public static final String IS_TRASH = "isTrash";
    private Boolean isTrash = false;
    private RecyclerView recyclerView;
    private VeiculoViewModel veiculoViewModel;
    private VeiculosAdapter veiculosAdapter;
    private String TAG = VeiculosFragment.class.getSimpleName();
    private FloatingActionButton floatingActionButton;

    public VeiculosFragment() {

    }

    public static VeiculosFragment newInstance(Boolean isTrash) {
        VeiculosFragment fragment = new VeiculosFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_TRASH, isTrash);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.isTrash = getArguments().getBoolean(IS_TRASH);
        }
        veiculoViewModel = new ViewModelProvider(getActivity()).get(VeiculoViewModel.class);
        veiculoViewModel.setRespository(new VeiculoRespository(getActivity()));
        veiculosAdapter = new VeiculosAdapter(getContext(), new VeiculosAdapter.OnItemClickListener() {
            @Override
            public void onClick(Veiculo veiculo, int position) {
                if (isTrash) {
                    restaurarVeiculo(veiculo);
                } else {
                    editarVeiculo(veiculo);
                }
            }

            @Override
            public boolean onLongClick(Veiculo veiculo, int position) {
                if (isTrash) {
                    deletarPermanentemente(veiculo);
                } else {
                    enviarPraLixeira(veiculo, position);
                }
                return true;
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_veiculos, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        if (isTrash)
            floatingActionButton.setVisibility(View.GONE);
        else
            floatingActionButton.setOnClickListener(v -> inserirVeiculo());

        recyclerView = view.findViewById(R.id.recyclerViewVeiculos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(veiculosAdapter);


        carregarVeiculos();
    }

    private void inserirVeiculo() {
        NavigationInActivities.goAddVeiculo(getActivity());
    }

    private void deletarPermanentemente(Veiculo veiculo) {
        Toast.makeText(getActivity(), "deletarPermanentemente", Toast.LENGTH_SHORT).show();
    }

    private void enviarPraLixeira(Veiculo veiculo, int position) {
        AlertDialog.Builder al = new AlertDialog.Builder(getActivity());
        al.setTitle("Veiculo")
                .setMessage(veiculo.getNome())
                .setPositiveButton("Remover", (dialogInterface, i) -> {
                    veiculoViewModel.trash(veiculo, (e, veiculo1) -> {
                        if (e != null) {
                            Alerts.alertWaring(getActivity(),
                                    "Erro ao remover o veículo",
                                    e.getMessage())
                                    .setPositiveButton("ok", null)
                                    .show();
                        } else {
                            veiculosAdapter.removeItem(position);
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();

    }

    private void restaurarVeiculo(Veiculo veiculo) {
        Toast.makeText(getActivity(), "restaurarVeiculo", Toast.LENGTH_SHORT).show();
    }

    private void editarVeiculo(Veiculo veiculo) {
        AlertDialog.Builder al = new AlertDialog.Builder(getActivity());
        al.setTitle("Veiculo")
                .setMessage(veiculo.getNome())
                .setPositiveButton("Editar", (dialogInterface, i) -> {
                    NavigationInActivities.goEditVeiculo(getActivity(), veiculo);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void carregarVeiculos() {
        Log.d(TAG, "carregarVeiculos: istrash " + isTrash);
        if (!isTrash) {
            veiculoViewModel.getAll().observe(getActivity(), new Observer<List<Veiculo>>() {
                @Override
                public void onChanged(List<Veiculo> veiculos) {
                    veiculosAdapter.setVeiculos(veiculos);
                }
            });
        } else {
            veiculoViewModel.getAllDeleted().observe(getActivity(), new Observer<List<Veiculo>>() {
                @Override
                public void onChanged(List<Veiculo> veiculos) {
                    veiculosAdapter.setVeiculos(veiculos);
                }
            });
        }
    }
}
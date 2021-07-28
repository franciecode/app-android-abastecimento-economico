package com.franciecode.abastecimentoeconomico.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.franciecode.abastecimentoeconomico.R;
import com.franciecode.abastecimentoeconomico.activities.NavigationInActivities;
import com.franciecode.abastecimentoeconomico.adapters.VeiculosAdapter;
import com.franciecode.abastecimentoeconomico.models.Veiculo;
import com.franciecode.abastecimentoeconomico.persistencia.VeiculoRespository;
import com.franciecode.abastecimentoeconomico.persistencia.viewModel.VeiculoViewModel;
import com.franciecode.abastecimentoeconomico.widgets.Alerts;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VeiculosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VeiculosFragment extends Fragment {

    public static final String IS_TRASH = "isTrash";
    private Boolean isTrash = false;
    private VeiculoViewModel veiculoViewModel;
    private VeiculosAdapter veiculosAdapter;

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
                    deletarPermanentemente(veiculo, position);
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
        return inflater.inflate(R.layout.fragment_veiculos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);
        if (isTrash)
            floatingActionButton.setVisibility(View.GONE);
        else
            floatingActionButton.setOnClickListener(v -> inserirVeiculo());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewVeiculos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(veiculosAdapter);


        carregarVeiculos();
    }

    private void inserirVeiculo() {
        NavigationInActivities.goAddVeiculo(getActivity());
    }

    private void deletarPermanentemente(Veiculo veiculo, int position) {
        Alerts.alertWaring(getActivity(),
                getString(R.string.acao_irreversivel),
                getString(R.string.confirma_deletar) +
                        veiculo.getNome() + "?" +
                        getString(R.string.msg_alerta_deletar_veiculo_permanentemente))
                .setPositiveButton("Confirmar", (dialogInterface, i) -> veiculoViewModel.delete(veiculo, e -> {
                    if (e != null) {
                        Alerts.alertError(getActivity(),
                                getString(R.string.erro_ao_deletar_veiculo),
                                e.getMessage())
                                .setPositiveButton("ok", null)
                                .show();
                    } else {
                        Toast.makeText(getActivity(), "Veiculo deletado com sucesso!", Toast.LENGTH_SHORT).show();
                        veiculosAdapter.removeItem(position);
                    }
                })).setNegativeButton("Cancelar", null)
                .show();

    }

    private void enviarPraLixeira(Veiculo veiculo, int position) {

        Alerts.alertWaring(
                getActivity(),
                getString(R.string.remover_veiculo),
                String.format(
                        getString(R.string.confirma_remover_veiculo),
                        veiculo.getNome()))
                .setPositiveButton(getString(R.string.remover), (dialogInterface, i) -> Alerts.alertWaring(getActivity(), getString(R.string.atencao_),
                        getString(R.string.msg_alerta_enviar_veiculo_para_lixeira))
                        .setPositiveButton(R.string.confirmar_remover, (dialogInterface1, i1) -> veiculoViewModel.trash(veiculo, (e, veiculo1) -> {
                            if (e != null) {
                                Alerts.alertWaring(getActivity(),
                                        getString(R.string.erro_ao_remover_veiculo),
                                        e.getMessage())
                                        .setPositiveButton("ok", null)
                                        .show();
                            } else {
                                veiculosAdapter.removeItem(position);
                            }
                        })).setNegativeButton(R.string.cancelar, null)
                        .show())
                .setNegativeButton(getString(R.string.cancelar), null)
                .show();

    }

    private void restaurarVeiculo(Veiculo veiculo) {
        Alerts.confirm(getActivity(), getString(R.string.restaurar_veiculo),
                String.format(
                        getString(R.string.confirma_restaurar_veiculo_da_lixeira),
                        veiculo.getNome()))
                .setPositiveButton(R.string.restaurar, (dialogInterface, i) -> veiculoViewModel.removeFromTrash(veiculo, e -> {
                    if (e != null) {
                        Alerts.alertError(getActivity(), getString(R.string.erro_ao_restaurar_veiculo),
                                e.getMessage()
                        ).setPositiveButton(R.string.ok, null)
                                .show();
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.veiculo_restaurado_com_sucesso),
                                Toast.LENGTH_SHORT).show();
                    }
                }))
                .setNegativeButton("Cancelar", null)
                .show();

    }

    private void editarVeiculo(Veiculo veiculo) {
        AlertDialog.Builder al = new AlertDialog.Builder(getActivity());
        al.setTitle("Veiculo")
                .setMessage(veiculo.getNome())
                .setPositiveButton("Editar", (dialogInterface, i) -> NavigationInActivities.goEditVeiculo(getActivity(), veiculo))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void carregarVeiculos() {
        if (!isTrash) {
            veiculoViewModel.getAll().observe(getActivity(), veiculos -> veiculosAdapter.setVeiculos(veiculos));
        } else {
            veiculoViewModel.getAllDeleted().observe(getActivity(), veiculos -> veiculosAdapter.setVeiculos(veiculos));
        }
    }
}
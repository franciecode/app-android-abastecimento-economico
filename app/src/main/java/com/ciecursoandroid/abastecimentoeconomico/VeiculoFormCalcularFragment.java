package com.ciecursoandroid.abastecimentoeconomico;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.ciecursoandroid.abastecimentoeconomico.enums.TipoCalculo;
import com.ciecursoandroid.abastecimentoeconomico.models.Veiculo;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VeiculoFormCalcularFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VeiculoFormCalcularFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Spinner spinnerVeiculo;
    private Listener listener;

    public VeiculoFormCalcularFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VeiculoFormCalcularFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VeiculoFormCalcularFragment newInstance(String param1, String param2) {
        VeiculoFormCalcularFragment fragment = new VeiculoFormCalcularFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_veiculo_form_calcular, container, false);
        spinnerVeiculo = root.findViewById(R.id.spinnerVeiculoFormCalcular);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item);
        adapter.add("Selecione um veículo...");
        adapter.add("+ NOVO VEÍCULO +");
        spinnerVeiculo.setAdapter(adapter);
        return root;
    }

    public interface Listener {
        void onNotify(TipoCalculo calculo, Veiculo veiculo, Float kmsLitroGasolina, Float KmsLitroAlcool);
    }
}
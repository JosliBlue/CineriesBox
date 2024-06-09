package Fragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import ec.com.josliblue.cineriesbox.databinding.FragmentoPrincipalBinding;

public class FragmentoPrincipal extends Fragment {
    private FragmentoPrincipalBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar y retornar la vista del fragmento utilizando View Binding
        binding = FragmentoPrincipalBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Liberar la referencia del binding
        binding = null;
    }
}

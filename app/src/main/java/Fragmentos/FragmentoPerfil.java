package Fragmentos;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import Auth.ActividadLogin;
import Utilidades.BDFirebase;
import ec.com.josliblue.cineriesbox.databinding.FragmentoPerfilBinding;
import ec.com.josliblue.cineriesbox.databinding.ModalCancelarConfirmarBinding;

public class FragmentoPerfil extends Fragment {
    private FragmentoPerfilBinding binding;
    private ModalCancelarConfirmarBinding dialogBinding;
    private Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentoPerfilBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.txtFPNombreUsuario.setText("Hola " + BDFirebase.getUsuarioActual().getDisplayName());

        // configuracion del modal
        dialogBinding = ModalCancelarConfirmarBinding.inflate(inflater);
        dialog = new Dialog(getActivity());
        dialog.setContentView(dialogBinding.getRoot());
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        esperarIntentoCerrarSesion("¿Cerrar Sesión?", "Estás seguro de salir?");
        esperarVentanaEditarPerfil();

        return view;
    }

    private void esperarIntentoCerrarSesion(String titulo, String descripcion) {
        dialogBinding.lblMCCTitulo.setText(titulo);
        dialogBinding.lblMCCDescripcion.setText(descripcion);
        binding.btnFPCerrarSesion.setOnClickListener(v -> {
            dialog.show();
        });
        dialogBinding.btnMCCCancelar.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialogBinding.btnMCCConfirmar.setOnClickListener(v -> {
            BDFirebase.cerrarSesion();
            startActivity(new Intent(getActivity(), ActividadLogin.class));
            getActivity().finish();
        });
    }

    private void esperarVentanaEditarPerfil() {
        binding.imgFPTarjetaPerfil.setOnClickListener(v -> {
            // Lógica para abrir la ventana de edición de perfil
        });
    }
}

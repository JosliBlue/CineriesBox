package Fragmentos;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import Auth.ActividadLogin;
import Interfaces.FirebaseCallBack;
import Utilidades.BDFirebase;
import Utilidades.Control;
import ec.com.josliblue.cineriesbox.databinding.FragmentoPerfilBinding;
import ec.com.josliblue.cineriesbox.databinding.ModalCancelarConfirmarBinding;
import ec.com.josliblue.cineriesbox.databinding.ModalModificarPerfilBinding;

public class FragmentoPerfil extends Fragment {
    private FragmentoPerfilBinding perfilBinding;
    private ModalCancelarConfirmarBinding confirmarCancelarBinding;
    private Dialog confirmarCancelarDialog;
    private ModalModificarPerfilBinding modificarPerfilBinding;
    private Dialog modificarPerfilDialog;

    @SuppressLint({"MissingInflatedId", "SetTextI18n", "WrongViewCast"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        perfilBinding = FragmentoPerfilBinding.inflate(inflater, container, false);
        View view = perfilBinding.getRoot();

        confirmarCancelarBinding = ModalCancelarConfirmarBinding.inflate(inflater);
        confirmarCancelarDialog = new Dialog(getActivity());
        confirmarCancelarDialog.setContentView(confirmarCancelarBinding.getRoot());
        confirmarCancelarDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        confirmarCancelarDialog.setCancelable(false);

        modificarPerfilBinding = ModalModificarPerfilBinding.inflate(inflater);
        modificarPerfilDialog = new Dialog(getActivity());
        modificarPerfilDialog.setContentView(modificarPerfilBinding.getRoot());
        modificarPerfilDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        modificarPerfilDialog.setCancelable(false);

        // Convertir dp a píxeles
        int marginInDp = 20;
        float scale = getResources().getDisplayMetrics().density;
        int marginInPx = (int) (marginInDp * scale + 0.5f);

        // Ajustar los parámetros del diálogo
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(confirmarCancelarDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        confirmarCancelarDialog.getWindow().setAttributes(layoutParams);

        // Ajustar los márgenes del diálogo de confirmar y cancelar
        confirmarCancelarDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) confirmarCancelarBinding.getRoot().getLayoutParams();
        params.setMargins(marginInPx, 0, marginInPx, 0);
        confirmarCancelarBinding.getRoot().setLayoutParams(params);

        // Ajustar los parámetros del diálogo de modificar perfil
        WindowManager.LayoutParams modificarPerfilLayoutParams = new WindowManager.LayoutParams();
        modificarPerfilLayoutParams.copyFrom(modificarPerfilDialog.getWindow().getAttributes());
        modificarPerfilLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        modificarPerfilLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        modificarPerfilDialog.getWindow().setAttributes(modificarPerfilLayoutParams);

        // Ajustar los márgenes del diálogo de modificar perfil
        modificarPerfilDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ViewGroup.MarginLayoutParams modificarPerfilParams = (ViewGroup.MarginLayoutParams) modificarPerfilBinding.getRoot().getLayoutParams();
        modificarPerfilParams.setMargins(marginInPx, 0, marginInPx, 0);
        modificarPerfilBinding.getRoot().setLayoutParams(modificarPerfilParams);

        perfilBinding.txtFPNombreUsuario.setText("Hola " + BDFirebase.getUsuarioActual().getDisplayName());

        esperarIntentoCerrarSesion();
        esperarVentanaEditarPerfil();

        return view;
    }

    private void esperarIntentoCerrarSesion() {
        confirmarCancelarBinding.lblMCCTitulo.setText("¿Cerrar Sesión?");
        confirmarCancelarBinding.lblMCCDescripcion.setText("Estás seguro de salir?");
        perfilBinding.btnFPCerrarSesion.setOnClickListener(v -> {
            confirmarCancelarDialog.show();
        });
        confirmarCancelarBinding.btnMCCCancelar.setOnClickListener(v -> {
            confirmarCancelarDialog.dismiss();
        });
        confirmarCancelarBinding.btnMCCConfirmar.setOnClickListener(v -> {
            BDFirebase.cerrarSesion();
            startActivity(new Intent(getActivity(), ActividadLogin.class));
            getActivity().finish();
        });
    }

    private void esperarVentanaEditarPerfil() {
        modificarPerfilBinding.txtMMPNuevoNombre.setText(BDFirebase.getUsuarioActual().getDisplayName());

        perfilBinding.btnFPEditarMiPerfil1.setOnClickListener(v -> {
            modificarPerfilDialog.show();
        });
        perfilBinding.btnFPEditarMiPerfil2.setOnClickListener(v -> {
            modificarPerfilDialog.show();
        });
        modificarPerfilBinding.btnMMPCancelar.setOnClickListener(v -> {
            modificarPerfilDialog.dismiss();
        });
        modificarPerfilBinding.btnMMPConfirmar.setOnClickListener(v -> {

            if (!Control.conexInternet(getActivity())) {
                Toast.makeText(getActivity(), "No hay conexión a Internet\nIntenta mas tarde. . .", Toast.LENGTH_LONG).show();
                return;
            }

            String nuevoNombre = modificarPerfilBinding.txtMMPNuevoNombre.getText().toString().trim();
            String claveActual = modificarPerfilBinding.txtMMPClaveActual.getText().toString().trim();
            String claveNueva = modificarPerfilBinding.txtMMPClaveNueva.getText().toString().trim();

            if (!nuevoNombre.isEmpty()) {
                BDFirebase.cambiarNombrePerfil(nuevoNombre, new FirebaseCallBack() {
                    @Override
                    public void onResult(boolean success, String message) {
                        if (success) {
                            actualizarDisplayNameEnBD(nuevoNombre);
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            if (!claveActual.isEmpty() && !claveNueva.isEmpty()) {
                BDFirebase.cambiarClave(claveActual, claveNueva, new FirebaseCallBack() {
                    @Override
                    public void onResult(boolean success, String message) {
                        if (success) {
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void actualizarDisplayNameEnBD(String nuevoNombre) {
        FirebaseUser currentUser = BDFirebase.getUsuarioActual();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            String path = "USUARIOS/" + uid;
            Map<String, Object> data = new HashMap<>();
            data.put("DisplayName", nuevoNombre);

            BDFirebase.actualizarDocumento(path, data, new FirebaseCallBack() {
                @Override
                public void onResult(boolean success, String message) {
                    if (success) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}

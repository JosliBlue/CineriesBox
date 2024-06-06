package Fragmentos;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import Auth.ActividadLogin;
import Utilidades.BDFirebase;
import ec.com.josliblue.cineriesbox.R;

public class FragmentoPerfil extends Fragment {
    private TextView txt_nombreUsuario, modal_textTitulo, modal_textDescripcion;
    private ImageView btn_editarPerfil;
    private Button btn_cerrarSesion, btn_dialogCancel, btn_dialogConfirm;
    private Dialog dialog;

    @SuppressLint({"MissingInflatedId", "SetTextI18n", "WrongViewCast"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmento_perfil, container, false);

        this.txt_nombreUsuario = view.findViewById(R.id.txt_NombreUsuario);
        this.btn_editarPerfil = view.findViewById(R.id.img_TarjetaPerfil);
        this.btn_cerrarSesion = view.findViewById(R.id.btn_CerrarSesion);

        this.dialog = new Dialog(getActivity());
        this.dialog.setContentView(R.layout.modal_cancelar_confirmar);
        this.dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.dialog.setCancelable(false);

        this.modal_textTitulo = this.dialog.findViewById(R.id.lbl_ModalTitulo);
        this.modal_textDescripcion = this.dialog.findViewById(R.id.lbl_ModalDescripcion);
        this.btn_dialogCancel = this.dialog.findViewById(R.id.btn_ModalCancelar);
        this.btn_dialogConfirm = this.dialog.findViewById(R.id.btn_ModalConfirmar);

        this.txt_nombreUsuario.setText("Hola " + BDFirebase.getUsuarioActual().getDisplayName());

        esperarIntentoCerrarSesion("¿Cerrar Sesión?", "Estás seguro de salir?");
        esperarVentanaEditarPerfil();

        return view;
    }

    private void esperarIntentoCerrarSesion(String titulo, String descripcion) {
        this.modal_textTitulo.setText(titulo);
        this.modal_textDescripcion.setText(descripcion);
        this.btn_cerrarSesion.setOnClickListener(v -> {
            this.dialog.show();
        });
        this.btn_dialogCancel.setOnClickListener(v -> {
            this.dialog.dismiss();
        });
        this.btn_dialogConfirm.setOnClickListener(v -> {
            BDFirebase.cerrarSesion();
            startActivity(new Intent(getActivity(), ActividadLogin.class));
            getActivity().finish();
        });
    }

    private void esperarVentanaEditarPerfil() {
        this.btn_editarPerfil.setOnClickListener(v -> {

        });
    }

}
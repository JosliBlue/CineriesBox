package Fragmentos;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Auth.ActividadLogin;
import Utilidades.FirebaseCallBack;
import Utilidades.BDFirebase;
import Utilidades.Control;
import Utilidades.ListaAdapter;
import ec.com.josliblue.cineriesbox.databinding.FragmentoPerfilBinding;
import ec.com.josliblue.cineriesbox.databinding.ModalCancelarConfirmarBinding;
import ec.com.josliblue.cineriesbox.databinding.ModalCrearListaBinding;
import ec.com.josliblue.cineriesbox.databinding.ModalModificarPerfilBinding;

public class FragmentoPerfil extends Fragment {
    private FragmentoPerfilBinding perfilBinding;
    private ModalModificarPerfilBinding modificarPerfilBinding;
    private ModalCancelarConfirmarBinding confirmarCancelarBinding;
    private ModalCrearListaBinding crearListaBinding;
    private Dialog confirmarCancelarDialog;
    private Dialog modificarPerfilDialog;
    private Dialog crearListaDialog;
    private RecyclerView recyclerView;
    private ListaAdapter listaAdapter;
    private List<String> listaItems;

    @SuppressLint({"MissingInflatedId", "SetTextI18n", "WrongViewCast"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        perfilBinding = FragmentoPerfilBinding.inflate(inflater, container, false);
        View view = perfilBinding.getRoot();

        confirmarCancelarBinding = ModalCancelarConfirmarBinding.inflate(inflater);
        confirmarCancelarDialog = new Dialog(requireContext());
        confirmarCancelarDialog.setContentView(confirmarCancelarBinding.getRoot());
        confirmarCancelarDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        confirmarCancelarDialog.setCancelable(false);

        crearListaBinding = ModalCrearListaBinding.inflate(inflater);
        crearListaDialog = new Dialog(requireContext());
        crearListaDialog.setContentView(crearListaBinding.getRoot());
        crearListaDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        crearListaDialog.setCancelable(false);

        modificarPerfilBinding = ModalModificarPerfilBinding.inflate(inflater);
        modificarPerfilDialog = new Dialog(requireContext());
        modificarPerfilDialog.setContentView(modificarPerfilBinding.getRoot());
        modificarPerfilDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        modificarPerfilDialog.setCancelable(false);

        // Convertir dp a píxeles
        int marginInDp = 15;
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

        // Ajustar los parámetros del diálogo de modificar perfil
        WindowManager.LayoutParams crearListaLayoutParams = new WindowManager.LayoutParams();
        crearListaLayoutParams.copyFrom(crearListaDialog.getWindow().getAttributes());
        crearListaLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        crearListaLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        crearListaDialog.getWindow().setAttributes(crearListaLayoutParams);

        // Ajustar los márgenes del diálogo de modificar perfil
        crearListaDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ViewGroup.MarginLayoutParams crearListaParams = (ViewGroup.MarginLayoutParams) crearListaBinding.getRoot().getLayoutParams();
        crearListaParams.setMargins(marginInPx, 0, marginInPx, 0);
        crearListaBinding.getRoot().setLayoutParams(modificarPerfilParams);

        perfilBinding.LblFPNombreUsuario.setText("Hola " + BDFirebase.getUsuarioActual().getDisplayName());

        modalEditarPerfil();
        modalCrearLista();
        intentarMostrarListas();
        modalCerrarSesion();

        return view;
    }

    private void modalEditarPerfil() {
        modificarPerfilBinding.txtMMPNuevoNombre.setText(BDFirebase.getUsuarioActual().getDisplayName());

        perfilBinding.BtnFPEditarMiPerfil.setOnClickListener(v -> {
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
                            modificarPerfilDialog.dismiss();
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
                            modificarPerfilDialog.dismiss();
                            modificarPerfilBinding.txtMMPClaveActual.setText("");
                            modificarPerfilBinding.txtMMPClaveNueva.setText("");
                        } else {
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void modalCrearLista() {
        crearListaBinding.lblMCLTitulo.setText("Nueva Lista");

        perfilBinding.BtnFPNuevaLista.setOnClickListener(v -> crearListaDialog.show());
        crearListaBinding.btnMCLCancelar.setOnClickListener(v -> crearListaDialog.dismiss());

        crearListaBinding.btnMCLConfirmar.setOnClickListener(v -> {
            String nombreLista = crearListaBinding.txtMCLNuevoNombre.getText().toString().trim();

            // Validar que el nombre de la lista no esté vacío
            if (nombreLista.isEmpty()) {
                Toast.makeText(requireContext(), "Debe ingresar un nombre para la lista", Toast.LENGTH_SHORT).show();
                return;
            }

            // Guardar la nueva lista en Firestore con nombre específico
            String uid = BDFirebase.getUsuarioActual().getUid();
            String path = "USUARIOS/" + uid + "/LISTAS/" + nombreLista;

            Map<String, Object> nuevaLista = new HashMap<>();

            BDFirebase.guardarDocumento(path, nuevaLista, (success, message) -> {
                if (success) {
                    Toast.makeText(requireContext(), "Lista creada exitosamente", Toast.LENGTH_SHORT).show();
                    listaItems.add(nombreLista);
                    listaAdapter.notifyDataSetChanged();
                    crearListaDialog.dismiss();
                } else {
                    Toast.makeText(requireContext(), "Error al crear lista: " + message, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void intentarMostrarListas() {
        listaItems = new ArrayList<>();
        recyclerView = perfilBinding.RvFPListas;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        listaAdapter = new ListaAdapter(requireContext(), listaItems, new ListaAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(int position) {
                // Lógica para editar colección en la posición 'position'
                Toast.makeText(requireContext(), "Editar colección en posición " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(int position) {
                // Lógica para eliminar colección en la posición 'position'
                Toast.makeText(requireContext(), "Eliminar colección en posición " + position, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(listaAdapter);

        String uid = BDFirebase.getUsuarioActual().getUid();
        String path = "USUARIOS/" + uid + "/LISTAS";

        BDFirebase.obtenerDocumentos(path, (success, result) -> {
            if (success && result != null) {
                listaItems.addAll(result);
                listaAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(requireContext(), "Error al obtener documentos de LISTAS", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void modalCerrarSesion() {
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

    private void actualizarDisplayNameEnBD(String nuevoNombre) {
        String uid = BDFirebase.getUsuarioActual().getUid();
        String path = "USUARIOS/" + uid;
        Map<String, Object> data = new HashMap<>();
        data.put("DisplayName", nuevoNombre);

        BDFirebase.actualizarDocumento(path, data, (success, message) -> {
            if (success) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

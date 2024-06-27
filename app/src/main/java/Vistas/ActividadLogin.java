package Vistas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import Utilidades.Control;
import Utilidades.BDFirebase;
import ec.com.josliblue.cineriesbox.databinding.ActividadLoginBinding;
import ec.com.josliblue.cineriesbox.databinding.ActividadRegistroBinding;

public class ActividadLogin extends AppCompatActivity {
    private ActividadLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Inicializar View Binding
        binding = ActividadLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        esperarIntentoLogin();
        botonVentanaRegistro();
        botonVentanaRecupercarClave();
    }

    private void esperarIntentoLogin() {
        binding.btnALIniciarSesion.setOnClickListener(v -> {
            if (Control.campoVacio(binding.txtALCorreo) || Control.campoVacio(binding.txtALClave)) {
                Toast.makeText(ActividadLogin.this, "Correo o clave invalidos\nPrueba de nuevo. . .", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Control.conexInternet(ActividadLogin.this)) {
                Toast.makeText(ActividadLogin.this, "No hay conexión a Internet\nIntenta mas tarde. . .", Toast.LENGTH_LONG).show();
                return;
            }
            String correo_string = binding.txtALCorreo.getText().toString().trim();
            String clave_string = binding.txtALClave.getText().toString().trim();
            BDFirebase.intentarLogin(correo_string, clave_string, (success, message) -> {
                if (success) {
                    startActivity(new Intent(ActividadLogin.this, ActividadPantallaPrincipal.class));
                    finish();
                } else {
                    Toast.makeText(ActividadLogin.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void botonVentanaRecupercarClave() {
        binding.lblALClavePerdida.setOnClickListener(v -> {
            startActivity(new Intent(ActividadLogin.this, ActividadRecuperarClave.class));
        });
    }

    private void botonVentanaRegistro() {
        binding.lblALRegistro.setOnClickListener(v -> {
            startActivity(new Intent(ActividadLogin.this, ActividadRegistro.class));
        });
        binding.btnALRegistro.setOnClickListener(v -> {
            startActivity(new Intent(ActividadLogin.this, ActividadRegistro.class));
        });
    }

    public static class ActividadRegistro extends AppCompatActivity {
        private ActividadRegistroBinding binding;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);

            // Inicializar View Binding
            binding = ActividadRegistroBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            esperarIntentoRegistro();
            botonVentanaLogin();
        }

        private void esperarIntentoRegistro() {
            binding.btnARRegistrarme.setOnClickListener(v -> {
                if (Control.campoVacio(binding.txtARCorreo) || Control.campoVacio(binding.txtARNombre) || Control.campoVacio(binding.txtARNewClave) || Control.campoVacio(binding.txtARConfirmaClave)) {
                    Toast.makeText(ActividadRegistro.this, "Llene todos los campos...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Control.campoLength(binding.txtARNewClave, 5)) {
                    Toast.makeText(ActividadRegistro.this, "La contraseña debe ser más de 6 caracteres...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Control.camposEquals(binding.txtARNewClave, binding.txtARConfirmaClave)) {
                    Toast.makeText(ActividadRegistro.this, "Las contraseñas no coinciden...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Control.conexInternet(ActividadRegistro.this)) {
                    Toast.makeText(ActividadRegistro.this, "No hay conexión a Internet\nIntenta mas tarde. . .", Toast.LENGTH_LONG).show();
                    return;
                }
                String correo_string = binding.txtARCorreo.getText().toString().trim();
                String clave_string = binding.txtARNewClave.getText().toString().trim();
                String nuevoNombre = binding.txtARNombre.getText().toString().trim();
                BDFirebase.registrarUsuario(correo_string, clave_string, (success, message) -> {
                    if (success) {
                        FirebaseUser usuarioActual = BDFirebase.getUsuarioActual();
                        if (usuarioActual != null) {
                            BDFirebase.actualizarPerfilUsuario(usuarioActual, nuevoNombre, (profileSuccess, profileMessage) -> {
                                if (profileSuccess) {
                                    String path = "USUARIOS/" + usuarioActual.getUid();
                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put("Identificador", correo_string);
                                    userMap.put("DisplayName", nuevoNombre);

                                    BDFirebase.guardarDocumento(path, userMap, (dbSuccess, dbMessage) -> {
                                        if (dbSuccess) {
                                            Toast.makeText(ActividadRegistro.this, "Usuario creado :)\nInicia sesión para continuar", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(ActividadRegistro.this, dbMessage, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(ActividadRegistro.this, profileMessage, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(ActividadRegistro.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }

        private void botonVentanaLogin() {
            binding.btnARReturnLogin.setOnClickListener(v -> finish());
            binding.lblARReturnLogin.setOnClickListener(v -> finish());
        }
    }
}

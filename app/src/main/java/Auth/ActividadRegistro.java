package Auth;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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
import ec.com.josliblue.cineriesbox.R;

public class ActividadRegistro extends AppCompatActivity {
    private EditText txt_newCorreo, txt_newNombre, txt_newClave, txt_claveConfirm;
    private ImageButton btn_returnLogin;
    private TextView lbl_returnLogin;
    private Button btn_registrar;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.actividad_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        this.txt_newCorreo = findViewById(R.id.txt_Correo);
        this.txt_newNombre = findViewById(R.id.txt_Nombre);
        this.txt_newClave = findViewById(R.id.txt_NewClave);
        this.txt_claveConfirm = findViewById(R.id.txt_ConfirmaClave);
        this.btn_registrar = findViewById(R.id.btn_Registrarme);
        this.lbl_returnLogin = findViewById(R.id.lbl_returnLogin);
        this.btn_returnLogin = findViewById(R.id.btn_returnLogin);

        esperarIntentoRegistro();

        botonVentanaLogin();

    }

    private void esperarIntentoRegistro() {
        btn_registrar.setOnClickListener(v -> {
            if (Control.campoVacio(txt_newCorreo) || Control.campoVacio(txt_newNombre) || Control.campoVacio(txt_newClave) || Control.campoVacio(txt_claveConfirm)) {
                Toast.makeText(ActividadRegistro.this, "Llene todos los campos...", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Control.campoLength(txt_newClave, 5)) {
                Toast.makeText(ActividadRegistro.this, "La contraseña debe ser más de 6 caracteres...", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Control.camposEquals(txt_newClave, txt_claveConfirm)) {
                Toast.makeText(ActividadRegistro.this, "Las contraseñas no coinciden...", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Control.conexInternet(ActividadRegistro.this)) {
                Toast.makeText(ActividadRegistro.this, "No hay conexión a Internet\nIntenta mas tarde. . .", Toast.LENGTH_LONG).show();
                return;
            }
            String correo_string = txt_newCorreo.getText().toString().trim();
            String clave_string = txt_newClave.getText().toString().trim();
            String nuevoNombre = txt_newNombre.getText().toString().trim();
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
        btn_returnLogin.setOnClickListener(v -> finish());
        lbl_returnLogin.setOnClickListener(v -> finish());
    }
}
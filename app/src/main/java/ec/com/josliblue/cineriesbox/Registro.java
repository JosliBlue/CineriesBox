package ec.com.josliblue.cineriesbox;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {
    private EditText correo, nombre, clave, claveConfirm;
    private ImageButton btn_returnLogin;
    private TextView lbl_returnLogin;
    private Button btn_registrar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // desde aqui empieza mi codigo
        this.correo = findViewById(R.id.txt_Correo);
        this.nombre = findViewById(R.id.txt_Nombre);
        this.clave = findViewById(R.id.txt_NewClave);
        this.claveConfirm = findViewById(R.id.txt_ConfirmaClave);
        this.btn_registrar = findViewById(R.id.btn_Registrarme);
        this.lbl_returnLogin = findViewById(R.id.lbl_returnLogin);
        this.btn_returnLogin = findViewById(R.id.btn_returnLogin);

        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();

        esperarIntentoRegistro();

        botonVentanaLogin();

    }

    private void esperarIntentoRegistro() {
        this.btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo_string, clave_string, nuevoNombre;
                correo_string = String.valueOf(correo.getText().toString().trim());
                clave_string = String.valueOf(clave.getText().toString().trim());
                nuevoNombre = String.valueOf(nombre.getText().toString().trim());

                if (correo_string.isEmpty() || nuevoNombre.isEmpty() || clave_string.isEmpty() || claveConfirm.getText().toString().trim().isEmpty()) {
                    Toast.makeText(null, "Llene todos los campos. . .", Toast.LENGTH_SHORT).show();
                    return;
                }

                // PONER CONTROL DE CORREO UNICO
                // parece q este control lo hace automatico firebase
                // PONER CONTROL DE CONTRASEÑA MAS DE 6 CARACTERES
                if (clave_string.length() <= 5) {
                    Toast.makeText(null, "La contraseña debe ser mas de 6 caracteres. . .", Toast.LENGTH_SHORT).show();
                    return;
                }
                //PONER CONTROL DE CLAVE Y CONFIRM_CLAVE IGUALES
                if (!clave_string.equals(claveConfirm.getText().toString().trim())) {
                    Toast.makeText(null, "Las contraseñas no coinciden. . .", Toast.LENGTH_SHORT).show();
                    return;
                }

                // GUARDAR EL USUARIO EN AUTHENTICATION

                mAuth.createUserWithEmailAndPassword(correo_string, clave_string)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // ACTUALIZAR AL USUARIO CON SU NUEVO "DisplayName"
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(nuevoNombre)
                                                .build();

                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            // GUARDAR EL USUARIO EN LA BD

                                                            String userId = user.getUid();
                                                            Map<String, Object> userMap = new HashMap<>();
                                                            userMap.put("Identificador", correo_string);
                                                            userMap.put("DisplayName", nuevoNombre);

                                                                /*
                                                                IMPORTANTE: ANTES DE QUERER USAR LA BD,
                                                                EDITAR LAS REGLAS EN LA PAGINA DE FIREBASE PARA QUE CUALQUIERSA PUEDA INGRESAR
                                                                DE LO CONTRARIO SIEMPRE DENEGARA LAS SOLICITUDES
                                                                */
                                                            db.collection("USUARIOS").document(userId)
                                                                    .set(userMap)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                Toast.makeText(Registro.this, "Usuario creado :)\nInicia Sesion para continuar", Toast.LENGTH_SHORT).show();
                                                                                // Regresa al login para iniciar sesion
                                                                                finish();
                                                                            } else {
                                                                                Toast.makeText(Registro.this, "Error al guardar el usuario en la base de datos", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });
                                                        } else {
                                                            Toast.makeText(Registro.this, "Error al colocar el nombre del usuario", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }

                                } else {
                                    Toast.makeText(Registro.this, "Usuario ya existente. . .", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
    private void botonVentanaLogin() {
        this.btn_returnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        this.lbl_returnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
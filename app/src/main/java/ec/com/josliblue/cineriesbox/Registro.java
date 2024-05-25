package ec.com.josliblue.cineriesbox;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    EditText correo, nombre, clave, claveConfirm;
    ImageButton btn_returnLogin;
    Button btn_registrar;
    FirebaseAuth mAuth;

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

        this.correo = findViewById(R.id.txt_Correo);
        this.nombre = findViewById(R.id.txt_Nombre);
        this.clave = findViewById(R.id.txt_NewClave);
        this.claveConfirm = findViewById(R.id.txt_ConfirmaClave);
        this.btn_registrar = findViewById(R.id.btn_Registrarme);
        this.btn_returnLogin = findViewById(R.id.btn_returnLogin);

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        botonAtras();
        this.btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo_string, clave_string, nuevoNombre;
                correo_string = String.valueOf(correo.getText());
                clave_string = String.valueOf(clave.getText());
                nuevoNombre = String.valueOf(nombre.getText());

                // GUARDAR EL USUARIO EN AUTHENTICATION
                if (comprobar()) {
                    mAuth.createUserWithEmailAndPassword(correo_string, clave_string)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // ACTUALZIAR AL USUARIO CON SU NUEVO "DisplayName"
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

                                                                                    // Iniciar LoginActivity
                                                                                    startActivity(new Intent(Registro.this, Login.class));
                                                                                    finish(); // Opcional: cerrar la actividad actual para que el usuario no pueda volver con el botón "Atrás"
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
            }
        });
    }

    private boolean comprobar() {
        // CONTROLES DE TODOS LOS CAMPOS LLENOS
        if (this.correo.getText().toString().trim().isEmpty() || this.nombre.getText().toString().trim().isEmpty() || this.clave.getText().toString().trim().isEmpty() || this.claveConfirm.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Llene todos los campos. . .", Toast.LENGTH_SHORT).show();
            return false;
        }

        // PONER CONTROL DE CORREO UNICO
        // parece q este control lo hace automatico firebase
        // PONER CONTROL DE CONTRASEÑA MAS DE 6 CARACTERES
        if (this.clave.getText().toString().trim().length() <= 5) {
            Toast.makeText(this, "La contraseña debe ser mas de 6 caracteres. . .", Toast.LENGTH_SHORT).show();
            return false;
        }
        //PONER CONTROL DE CLAVE Y CONFIRM_CLAVE IGUALES
        if (!this.clave.getText().toString().trim().equals(this.claveConfirm.getText().toString().trim())) {
            Toast.makeText(this, "Las contraseñas no coinciden. . .", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void botonAtras() {
        this.btn_returnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registro.this, Login.class));
                finish();
            }
        });
    }
}
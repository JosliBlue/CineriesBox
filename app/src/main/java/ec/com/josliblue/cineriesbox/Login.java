package ec.com.josliblue.cineriesbox;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;

public class Login extends AppCompatActivity {

    private EditText correo, clave;
    private Button btn_ingresar;
    private TextView lbl_registarme,lbl_sin_cuenta;
    FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.correo = findViewById(R.id.txt_Correo);
        this.clave = findViewById(R.id.txt_Clave);
        this.btn_ingresar = findViewById(R.id.btn_Ingresar);

        mAuth = FirebaseAuth.getInstance();
        intentarIngresar();

        this.lbl_registarme = findViewById(R.id.lbl_Registro);
        this.lbl_sin_cuenta = findViewById(R.id.lbl_SinCuenta);
        this.lbl_registarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registro.class));
            }
        });
        this.lbl_sin_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registro.class));
            }
        });
    }

    private void intentarIngresar() {

        this.btn_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo_string = correo.getText().toString().trim();
                String clave_string = clave.getText().toString().trim();

                // Validación de campos
                if (correo_string.isEmpty()) {
                    Toast.makeText(Login.this, "Correo inválido", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (clave_string.isEmpty()) {
                    Toast.makeText(Login.this, "Contraseña inválida", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Autenticar con Firebase Authentication
                mAuth.signInWithEmailAndPassword(correo_string, clave_string)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

                                    //FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(Login.this, "Inciaste sesion", Toast.LENGTH_SHORT).show();

                                } else {

                                    Toast.makeText(Login.this, "No inciaste sesion\nPrueba de nuevo. . .", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
            }
        });
    }

}
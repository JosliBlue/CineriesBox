package ec.com.josliblue.cineriesbox;

import android.annotation.SuppressLint;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class RecuperarClave extends AppCompatActivity {
    private EditText txt_correo;
    private Button btn_enviarCorreo;
    private ImageButton btn_returnLogin;
    private TextView lbl_returnLogin;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recuperar_clave);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.lbl_returnLogin = findViewById(R.id.lbl_returnLogin);
        this.btn_returnLogin = findViewById(R.id.btn_returnLogin);
        this.txt_correo = findViewById(R.id.txt_Correo);
        this.btn_enviarCorreo = findViewById(R.id.btn_EnviarCorreo);
        this.mAuth = FirebaseAuth.getInstance();

        esperarIntentoRecuperarClave();

        botonVentanaLogin();
    }
    private void esperarIntentoRecuperarClave() {
        this.btn_enviarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo_string = txt_correo.getText().toString().trim();

                if (correo_string.isEmpty()) {
                    Toast.makeText(RecuperarClave.this, "Por favor, ingrese su correo electrónico", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.sendPasswordResetEmail(correo_string)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RecuperarClave.this, "Correo de restablecimiento enviado, visita tu bandeja de entrada", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(RecuperarClave.this, "Error al enviar el correo de restablecimiento", Toast.LENGTH_SHORT).show();
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
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

import Utilidades.BDFirebase;
import Utilidades.Control;

public class RecuperarClaveActivity extends AppCompatActivity {
    private EditText txt_correo;
    private Button btn_enviarCorreo;
    private ImageButton btn_returnLogin;
    private TextView lbl_returnLogin;

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

        esperarIntentoRecuperarClave();

        botonVentanaLogin();
    }

    private void esperarIntentoRecuperarClave() {
        this.btn_enviarCorreo.setOnClickListener(v -> {
            if (Control.campoVacio(txt_correo)) {
                Toast.makeText(RecuperarClaveActivity.this, "Por favor, ingrese su correo electrónico", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Control.conexInternet(RecuperarClaveActivity.this)) {
                Toast.makeText(RecuperarClaveActivity.this, "No hay conexión a Internet\nIntenta mas tarde. . .", Toast.LENGTH_LONG).show();
                return;
            }
            String correo_string = txt_correo.getText().toString().trim();
            BDFirebase.enviarCorreoRecuperacion(correo_string, (success, message) -> {
                Toast.makeText(RecuperarClaveActivity.this, message, Toast.LENGTH_SHORT).show();
                if (success) {
                    finish();
                }
            });
        });
    }

    private void botonVentanaLogin() {
        btn_returnLogin.setOnClickListener(v -> finish());
        lbl_returnLogin.setOnClickListener(v -> finish());
    }
}
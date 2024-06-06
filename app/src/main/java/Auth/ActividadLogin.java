package Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import Utilidades.Control;
import Utilidades.BDFirebase;
import Home.ActividadPantallaPrincipal;
import ec.com.josliblue.cineriesbox.R;

public class ActividadLogin extends AppCompatActivity {
    private EditText txt_correo, txt_clave;
    private Button btn_ingresar;
    private TextView lbl_registarme, lbl_clavePerdida;
    private ImageButton btn_registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.actividad_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.txt_correo = findViewById(R.id.txt_Correo);
        this.txt_clave = findViewById(R.id.txt_Clave);
        this.btn_ingresar = findViewById(R.id.btn_IniciarSesion);
        this.lbl_registarme = findViewById(R.id.lbl_Registro);
        this.btn_registro = findViewById(R.id.btn_Registro);
        this.lbl_clavePerdida = findViewById(R.id.lbl_ClavePerdida);

        esperarIntentoLogin();

        botonVentanaRegistro();
        botonVentanaRecupercarClave();
    }

    private void esperarIntentoLogin() {
        this.btn_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Control.campoVacio(txt_correo) || Control.campoVacio(txt_clave)) {
                    Toast.makeText(ActividadLogin.this, "Correo o clave invalidos\nPrueba de nuevo. . .", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Control.conexInternet(ActividadLogin.this)) {
                    Toast.makeText(ActividadLogin.this, "No hay conexión a Internet\nIntenta mas tarde. . .", Toast.LENGTH_LONG).show();
                    return;
                }
                String correo_string = txt_correo.getText().toString().trim();
                String clave_string = txt_clave.getText().toString().trim();
                BDFirebase.intentarLogin(correo_string, clave_string, (success, message) -> {
                    if (success) {
                        startActivity(new Intent(ActividadLogin.this, ActividadPantallaPrincipal.class));
                        finish();
                    } else {
                        Toast.makeText(ActividadLogin.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void botonVentanaRecupercarClave() {
        this.lbl_clavePerdida.setOnClickListener(v -> {
            startActivity(new Intent(ActividadLogin.this, ActividadRecuperarClave.class));
        });
    }

    private void botonVentanaRegistro() {
        this.lbl_registarme.setOnClickListener(v -> {
            startActivity(new Intent(ActividadLogin.this, ActividadRegistro.class));
        });
        this.btn_registro.setOnClickListener(v -> {
            startActivity(new Intent(ActividadLogin.this, ActividadRegistro.class));
        });
    }
}
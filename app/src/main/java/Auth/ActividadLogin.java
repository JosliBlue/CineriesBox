package Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import ec.com.josliblue.cineriesbox.databinding.ActividadLoginBinding;

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
        binding.btnALIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
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
}

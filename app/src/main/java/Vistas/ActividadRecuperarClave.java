package Vistas;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Utilidades.BDFirebase;
import Utilidades.Control;
import ec.com.josliblue.cineriesbox.databinding.ActividadRecuperarClaveBinding;

public class ActividadRecuperarClave extends AppCompatActivity {
    private ActividadRecuperarClaveBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Inicializar View Binding
        binding = ActividadRecuperarClaveBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        esperarIntentoRecuperarClave();
        botonVentanaLogin();
    }

    private void esperarIntentoRecuperarClave() {
        binding.btnARCEnviarCorreo.setOnClickListener(v -> {
            if (Control.campoVacio(binding.txtARCCorreo)) {
                Toast.makeText(ActividadRecuperarClave.this, "Por favor, ingrese su correo electrónico", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Control.conexInternet(ActividadRecuperarClave.this)) {
                Toast.makeText(ActividadRecuperarClave.this, "No hay conexión a Internet\nIntenta mas tarde. . .", Toast.LENGTH_LONG).show();
                return;
            }
            String correo_string = binding.txtARCCorreo.getText().toString().trim();
            BDFirebase.enviarCorreoRecuperacion(correo_string, (success, message) -> {
                Toast.makeText(ActividadRecuperarClave.this, message, Toast.LENGTH_SHORT).show();
                if (success) {
                    finish();
                }
            });
        });
    }

    private void botonVentanaLogin() {
        binding.btnARCReturnLogin.setOnClickListener(v -> finish());
        binding.lblARCReturnLogin.setOnClickListener(v -> finish());
    }
}

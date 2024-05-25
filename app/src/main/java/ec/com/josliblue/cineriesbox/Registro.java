package ec.com.josliblue.cineriesbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Registro extends AppCompatActivity {
    EditText correo, nombre, clave, claveConfirm;
    ImageButton btn_returnLogin;

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

        esperarRetorno();
        comprobar();

    }

    private void comprobar(){
        this.correo = findViewById(R.id.txt_Correo);
        this.nombre = findViewById(R.id.txt_Nombre);
        this.clave = findViewById(R.id.txt_NewClave);
        this.claveConfirm = findViewById(R.id.txt_ConfirmaClave);
    }

    private void esperarRetorno(){
        this.btn_returnLogin = findViewById(R.id.btn_returnLogin);
        this.btn_returnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registro.this, Login.class));
                finish();
            }
        });
    }
}
package Utilidades;

import android.content.Context;
import android.net.ConnectivityManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Map;

import Interfaces.FirebaseCallBack;

/* Aqui solo se consume el servicio de firebase,
   la intanciacion se hizo automatico con ayuda del IDE (tools -> Firebase) */
public class BDFirebase {
    protected static FirebaseAuth auth = FirebaseAuth.getInstance();
    protected static FirebaseFirestore bd = FirebaseFirestore.getInstance();

    private BDFirebase() {
    }

    // AUTENTICACION -------------------------------------------------------------------------------
    public static void intentarLogin(String correo, String clave, FirebaseCallBack callback) {
        auth.signInWithEmailAndPassword(correo, clave).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onResult(true, "Login exitoso");
            } else {
                String errorMessage = "Error al iniciar sesión: " + task.getException().getMessage();
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    errorMessage = "Credenciales inválidas. Por favor verifica tu correo y contraseña.";
                } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                    errorMessage = "Usuario no encontrado. Por favor verifica tu correo.";
                }
                callback.onResult(false, errorMessage);
            }
        });
    }

    public static void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();
    }

    public static void registrarUsuario(String correo, String clave, FirebaseCallBack callback) {
        auth.createUserWithEmailAndPassword(correo, clave).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onResult(true, "Usuario registrado exitosamente");
            } else {
                callback.onResult(false, "Error al registrar el usuario\nPrueba de nuevo. . . " /*+ task.getException().getMessage()*/);
            }
        });
    }

    public static void actualizarPerfilUsuario(FirebaseUser user, String nuevoNombre, FirebaseCallBack callback) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(nuevoNombre).build();

        user.updateProfile(profileUpdates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onResult(true, "Perfil actualizado exitosamente");
            } else {
                callback.onResult(false, "Error al actualizar el perfil: " + task.getException().getMessage());
            }
        });
    }

    public static FirebaseUser getUsuarioActual() {
        return auth.getCurrentUser();
    }

    public static void enviarCorreoRecuperacion(String correo, FirebaseCallBack callback) {
        auth.sendPasswordResetEmail(correo).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onResult(true, "Correo de restablecimiento enviado, visita tu bandeja de entrada");
            } else {
                callback.onResult(false, "Error al enviar el correo de restablecimiento: " + task.getException().getMessage());
            }
        });
    }

    // BASE DE DATOS -------------------------------------------------------------------------------
    public static void guardarDocumento(String path, Map<String, Object> data, FirebaseCallBack callback) {
        bd.document(path).set(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onResult(true, "Documento guardado exitosamente");
            } else {
                String errorMessage = "Error al guardar el documento: " + task.getException().getMessage();
                if (task.getException() instanceof FirebaseFirestoreException) {
                    FirebaseFirestoreException.Code code = ((FirebaseFirestoreException) task.getException()).getCode();
                    if (code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                        errorMessage = "Permisos denegados. Verifica las reglas de seguridad de Firestore.";
                    } else if (code == FirebaseFirestoreException.Code.UNAVAILABLE) {
                        errorMessage = "Servicio no disponible. Inténtalo de nuevo más tarde.";
                    }
                }
                callback.onResult(false, errorMessage);
            }
        });
    }


}

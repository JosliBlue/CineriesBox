package Utilidades;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* Aqui solo se consume el servicio de firebase,
   la intanciacion se hizo automatico con ayuda del IDE (tools -> Firebase) */
public class BDFirebase {
    protected static FirebaseAuth auth = FirebaseAuth.getInstance();
    protected static FirebaseFirestore bd = FirebaseFirestore.getInstance();

    private BDFirebase() {
    }

    // AUTENTICACION -------------------------------------------------------------------------------
    public static FirebaseUser getUsuarioActual() {
        return auth.getCurrentUser();
    }

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

    public static void enviarCorreoRecuperacion(String correo, FirebaseCallBack callback) {
        auth.sendPasswordResetEmail(correo).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onResult(true, "Correo de restablecimiento enviado, visita tu bandeja de entrada");
            } else {
                callback.onResult(false, "Error al enviar el correo de restablecimiento: " + task.getException().getMessage());
            }
        });
    }

    public static void cambiarNombrePerfil(String newDisplayName, FirebaseCallBack callback) {
        FirebaseUser user = getUsuarioActual();
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(newDisplayName).build();

            user.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    callback.onResult(true, "Nombre de perfil actualizado");
                } else {
                    callback.onResult(false, "Error al actualzar el nombre de usuario: " + task.getException().getMessage());
                }
            });
        } else {
            callback.onResult(false, "Error 2 al actualzar el nombre de usuario");
        }
    }

    public static void cambiarClave(String claveActual, String claveNueva, FirebaseCallBack callback) {
        FirebaseUser user = getUsuarioActual();
        if (user != null) {
            // Obtener las credenciales del usuario actual
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), claveActual);

            // Reautenticar al usuario
            user.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Si la reautenticación es exitosa, cambiar la contraseña
                    user.updatePassword(claveNueva).addOnCompleteListener(updatePasswordTask -> {
                        if (updatePasswordTask.isSuccessful()) {
                            callback.onResult(true, "Contraseña actualizada exitosamente");
                        } else {
                            callback.onResult(false, "Error al actualizar la contraseña: " + updatePasswordTask.getException().getMessage());
                        }
                    });
                } else {
                    // Si la reautenticación falla, mostrar un mensaje de error
                    callback.onResult(false, "Error al reautenticar: " + task.getException().getMessage());
                }
            });
        } else {
            callback.onResult(false, "Error al actualizar la contraseña: usuario no encontrado");
        }
    }

    // BASE DE DATOS -------------------------------------------------------------------------------

    public static void obtenerDocumentos(String path, FirebaseCallBacks<List<String>> callback) {
        bd.collection(path).get().addOnSuccessListener(querySnapshot -> {
            List<String> documentos = new ArrayList<>();
            for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                documentos.add(documentSnapshot.getId()); // Agregar el ID del documento a la lista
            }
            callback.onResult(true, documentos);
        }).addOnFailureListener(e -> {
            callback.onResult(false, null);
        });
    }

    public static void obtenerDocumento(String path, FirebaseCallBacks<Map<String, Object>> callback) {
        DocumentReference docRef = bd.document(path);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> data = document.getData();
                    callback.onResult(true, data);
                } else {
                    callback.onResult(false, null);
                }
            } else {
                callback.onResult(false, null);
            }
        });
    }

    public static void guardarDocumento(String path, Map<String, Object> data, FirebaseCallBack callback) {
        bd.document(path).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    String errorMessage = "Ya existe un documento con el mismo nombre";
                    callback.onResult(false, errorMessage);
                } else {
                    // No existe documento con el mismo nombre, proceder a guardarlo
                    bd.document(path).set(data).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            callback.onResult(true, "Documento guardado exitosamente");
                        } else {
                            String errorMessage = "Error al guardar el documento: " + task1.getException().getMessage();
                            if (task1.getException() instanceof FirebaseFirestoreException) {
                                FirebaseFirestoreException.Code code = ((FirebaseFirestoreException) task1.getException()).getCode();
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
            } else {
                // Error al verificar la existencia del documento
                String errorMessage = "Error al verificar la existencia del documento: " + task.getException().getMessage();
                callback.onResult(false, errorMessage);
            }
        });
    }


    public static void actualizarDocumento(String path, Map<String, Object> data, FirebaseCallBack callback) {
        DocumentReference docRef = bd.document(path);
        docRef.update(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (!data.containsKey("DisplayName")) {
                    callback.onResult(true, "Guardado en ");
                }
            } else {
                callback.onResult(false, "Error al actualizar el documento: " + task.getException().getMessage());
            }
        });
    }

    public static void eliminarDocumento(String path, FirebaseCallBack callback) {
        DocumentReference docRef = bd.document(path);
        docRef.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onResult(true, "Documento eliminado exitosamente");
            } else {
                callback.onResult(false, "Error al eliminar el documento: " + task.getException().getMessage());
            }
        });
    }
    public static void eliminarClave(String path, String key, FirebaseCallBack callback) {
        DocumentReference docRef = bd.document(path);
        Map<String, Object> updates = new HashMap<>();
        updates.put(key, FieldValue.delete());

        docRef.update(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                callback.onResult(true, "Eliminado correctamente");
            } else {
                callback.onResult(false, "Error al eliminar la clave: " + task.getException().getMessage());
            }
        });
    }


}

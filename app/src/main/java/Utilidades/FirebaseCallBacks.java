package Utilidades;

public interface FirebaseCallBacks<T> {
    void onResult(boolean success, T result);
}

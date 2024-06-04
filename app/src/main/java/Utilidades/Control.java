package Utilidades;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.EditText;

public class Control {
    private Control() {
    }

    public static boolean campoVacio(EditText editText) {
        if (editText.getText().toString().trim().isEmpty() || editText.getText().toString().trim().equals("")) {
            return true;
        }
        return false;
    }

    public static boolean campoLength(EditText editText, int num) {
        if (editText.getText().toString().trim().length() <= num) {
            return true;
        }
        return false;
    }

    public static boolean camposEquals(EditText editText1, EditText editText2) {
        if (editText1.getText().toString().trim().equals(editText2.getText().toString().trim()) ||
                editText1.getText().toString().trim() == editText2.getText().toString().trim()) {
            return true;
        }
        return false;
    }

    public static boolean conexInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

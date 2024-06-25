package ConAPI;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListFilm {
    @SerializedName("results")
    @Expose
    private List<Datum> data;

    public List<Datum> getData() {
        return data;
    }
}

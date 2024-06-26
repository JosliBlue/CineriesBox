package ConAPI;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListFilm {

    public ListFilm(List<Datum> data) {
        this.data = data;
    }
    public ListFilm() {
    }
    @SerializedName("results")
    @Expose
    private List<Datum> data;

    public List<Datum> getData() {
        return data;
    }
}

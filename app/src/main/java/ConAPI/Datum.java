package ConAPI;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("poster")
    @Expose
    private String poster;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("imdb_rating")
    @Expose
    private String imdbRating;
    @SerializedName("genres")
    @Expose
    private List<String> genres;
    @SerializedName("images")
    @Expose
    private List<String> images;

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getYear() {
        return year;
    }

    public String getCountry() {
        return country;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<String> getImages() {
        return images;
    }
}

package ConAPI;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilmItem {

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
    @SerializedName("rated")
    @Expose
    private String rated;
    @SerializedName("released")
    @Expose
    private String released;
    @SerializedName("runtime")
    @Expose
    private String runtime;
    @SerializedName("director")
    @Expose
    private String director;
    @SerializedName("writer")
    @Expose
    private String writer;
    @SerializedName("actors")
    @Expose
    private String actors;
    @SerializedName("plot")
    @Expose
    private String plot;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("awards")
    @Expose
    private String awards;
    @SerializedName("metascore")
    @Expose
    private String metascore;
    @SerializedName("imdb_rating")
    @Expose
    private String imdbRating;
    @SerializedName("imdb_votes")
    @Expose
    private String imdbVotes;
    @SerializedName("imdb_id")
    @Expose
    private String imdbId;
    @SerializedName("type")
    @Expose
    private String type;
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
    public String getRated() {
        return rated;
    }
    public String getReleased() {
        return released;
    }
    public String getRuntime() {
        return runtime;
    }
    public String getDirector() {
        return director;
    }
    public String getWriter() {
        return writer;
    }
    public String getActors() {
        return actors;
    }
    public String getPlot() {
        return plot;
    }
    public String getCountry() {
        return country;
    }
    public String getAwards() {
        return awards;
    }
    public String getMetascore() {
        return metascore;
    }
    public String getImdbRating() {
        return imdbRating;
    }
    public String getImdbVotes() {
        return imdbVotes;
    }
    public String getImdbId() {
        return imdbId;
    }
    public String getType() {
        return type;
    }
    public List<String> getGenres() {
        return genres;
    }
    public List<String> getImages() {
        return images;
    }
}

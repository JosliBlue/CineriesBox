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
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("runtime")
    @Expose
    private Integer runtime;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("genres")
    @Expose
    private List<Genre> genres;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;

    // Getters
    public Integer getId() { return id; }
    public String getTitle() { return title; }
    public String getPosterPath() { return posterPath; }
    public String getReleaseDate() { return releaseDate; }
    public Integer getRuntime() { return runtime; }
    public String getOverview() { return overview; }
    public List<Genre> getGenres() { return genres; }
    public String getBackdropPath() { return backdropPath; }
    public Double getVoteAverage() { return voteAverage; }
    public Integer getVoteCount() { return voteCount; }

    // Inner class for genres
    public class Genre {
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;

        public Integer getId() { return id; }
        public String getName() { return name; }
    }
}

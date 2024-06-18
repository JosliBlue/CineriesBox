package ConAPI;

import java.util.List;

public class Genres {


    /*
     @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
     */

   private List<GenresItem> genres;

    public void setGenres(List<GenresItem> genres) {
        this.genres = genres;
    }
    public List<GenresItem> getGenres() {
        return genres;
    }

}

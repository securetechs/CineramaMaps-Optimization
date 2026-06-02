package main.com.cineramamaps.model;


public class MovieBeanList {
    String id;
    String title;
    String yearrelease;
    String distributor;
    String actors;
    String description;


    public MovieBeanList(String id, String title, String yearrelease, String distributor, String actors, String description) {
        this.id = id;
        this.title = title;
        this.yearrelease = yearrelease;
        this.distributor = distributor;
        this.actors = actors;
        this.description = description;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYearrelease() {
        return yearrelease;
    }

    public void setYearrelease(String yearrelease) {
        this.yearrelease = yearrelease;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}



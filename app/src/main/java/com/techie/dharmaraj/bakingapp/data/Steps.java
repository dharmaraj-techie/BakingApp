package com.techie.dharmaraj.bakingapp.data;

/**
 * Class which stores all details of the single step
 */
public class Steps {
    public int id;
    public String shortDescription;
    public String description;
    public String videoUrl;
    public String thumbnail;

    public Steps(int id, String shortDescription, String description, String videoUrl, String thumbnail){
        this.id = id;
        this.shortDescription =shortDescription;
        this.description = description;
        this.videoUrl = videoUrl;
        this.thumbnail = thumbnail;
    }

}

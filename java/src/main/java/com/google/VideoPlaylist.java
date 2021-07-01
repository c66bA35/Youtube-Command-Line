package com.google;

import java.util.List;
import java.util.ArrayList;

/** A class used to represent a Playlist */
class VideoPlaylist {
    String name;
    List<Video> videos = new ArrayList<Video>();

    /**
     * constructor for a video playlist object
     * @param name
     */
    public VideoPlaylist(String name) {
        this.name = name;
    }

    /**
     * getter for name attribute
     * @return String
     */
    public String getName(){
        return name;
    }

    /**
     * adds a video object to the playlist
     * @param video
     */
    public void addVideo(Video video){
        videos.add(video);
    }

    /**
     * removes a video object from the playlist
     * @param video
     */
    public void removeVideo(Video video){
        videos.remove(video);
    }

    /**
     * removes all video objects from the playlist
     */
    public void clear(){
        videos.removeAll(videos);
    }

    /**
     * checks if a particular video is in the playlist from a given id
     * @param id
     * @return boolean
     */
    public boolean checkForDuplicates(String id){
        for(Video video: videos){
            if(video.getVideoId().equals(id)){
                return true;
            }
        }
        return false;
    }

    /**
     * checks if the playlist contains any videos
     * @return boolean
     */
    public boolean checkIfEmpty(){
        if(videos.size()==0){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * prints information about each video in the playlist
     */
    public void printVideos(){

        //iterates through each video
        for(Video video: videos){

            //concatenates the title and id in the required format
            String info = (video.getTitle() + " (" + video.getVideoId() + ") [");
           
            //concatenates the tags from the array list
            String tags = "";
            for(String tag: video.getTags()){
              tags = tags + " " + tag;
            }
            String trimmedTags = tags.trim();

            //appends the tags
            info = info + trimmedTags + "]";

            //if the video is flagged then the reason is appended
            if(video.isFlagged()){
                info = info + " - FLAGGED (reason: " + video.getFlagged() + ")";
            }

            System.out.println(info);
            
          }
    }

}

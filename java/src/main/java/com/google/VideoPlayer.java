package com.google;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import java.lang.Integer;
import java.lang.reflect.InvocationTargetException;

public class VideoPlayer {

  /**
   * attribute that stores video objects that are in the library
   */
  private final VideoLibrary videoLibrary;
  /**
   * attribute that stores the video object of the video currently playing
   */
  private Video currentlyPlaying = null;
  /**
   * attribute that represents whether or not the current video is paused
   */
  boolean currentVideoPaused = false;
  /**
   * list that holds a VideoPlaylist object for each playlist created
   */
  List<VideoPlaylist> playlists = new ArrayList<VideoPlaylist>();

  /**
   * constructor method
   */
  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
  }

  /**
   * displays the number of videos in the library
   */
  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  /**
   * displays details of all the videos in the library
   */
  public void showAllVideos() {
    
    System.out.println("Here's a list of all available videos:");
    List<Video> videos = new ArrayList<Video>(videoLibrary.getVideos());

    //uses a comparator class to sort the videos by title
    videos.sort(new TitleSorter());

    //calls a method to format the details of each video and then displays these
    for(Video video: videos){
      
      String info = formatDetailsOfVideo(video);

      //if the video is flagged then the reason is appended
      if(video.isFlagged()){
        info = info + " - FLAGGED (reason: " + video.getFlagged() + ")";
      }
      System.out.println(info);
    }
  }

  /**
   * accepts a video object and returns a formatted string containing the title, id and tags
   * @param video
   * @return
   */
  public String formatDetailsOfVideo(Video video){

    //concatenates the title and id in the required format
    String info = (video.getTitle() + " (" + video.getVideoId() + ") [");

    //concatenates the tags from the array list
    String tags = "";
    for(String tag: video.getTags()){
      tags = tags + " " + tag;
    }
    String trimmedTags = tags.trim();

    //returns all information
    return(info + trimmedTags + "]");
  
  }
  
  
  /**
   * a class that implements the comparator interface and contains a method to compare two 
   * videos by title
   */
  //The following resource was used to research how to write a comparator class
  //https://howtodoinjava.com/java/collections/arraylist/arraylist-sort-objects-by-field/
  public class TitleSorter implements Comparator<Video> {

    public int compare(Video video1, Video video2) {
        return video1.getTitle().compareTo(video2.getTitle());
    }

  };

  /**
   * method used to play a video given the id
   * @param videoId
   */
  public void playVideo(String videoId) {
    
    //checks to ensure the video exists
    if(videoLibrary.getVideo(videoId)!= null){

      //checks if video is flagged
      if(videoLibrary.getVideo(videoId).isFlagged()){
        System.out.println("Cannot play video: Video is currently flagged (reason: " + videoLibrary.getVideo(videoId).getFlagged() + ")");
      }
      else{

        //if there is a video currently being played it is stopped
        if(currentlyPlaying!=null){
          System.out.println("Stopping video: " + currentlyPlaying.getTitle());
        }
        
        //the video is played and relevant attributes are updated
        System.out.println("Playing video: " + videoLibrary.getVideo(videoId).getTitle());
        currentlyPlaying = videoLibrary.getVideo(videoId);
        currentVideoPaused = false;  
      }    

    }
    else{
      System.out.println("Cannot play video: Video does not exist");
    }


  }

  /**
   * method used to stop the video currently being played
   */
  public void stopVideo() {
  
    //if there is no video being played a message is displayed
    if(currentlyPlaying == null){
      System.out.println("Cannot stop video: No video is currently playing");
    }
    else{
      //the video is stopped and the relevant attributes are updated
      System.out.println("Stopping video: " + currentlyPlaying.getTitle());
      currentlyPlaying = null;
      currentVideoPaused = false;
    }
  }

  public void playRandomVideo() {
    
    //if there is a video currently being played the video is stopped
    if(currentlyPlaying!=null){
      System.out.println("Stopping video: " + currentlyPlaying.getTitle());
    }

    //generates a random number
    Random random = new Random();
    int randomInt = random.nextInt(videoLibrary.getVideos().size());

    //checks if video is flagged - need to add check for if they are all flagged
    if(videoLibrary.getVideos().get(randomInt).isFlagged()){
      randomInt = random.nextInt(videoLibrary.getVideos().size());
    }
    else{
      //plays the video at the randomly generated index and updates the relevant variables
      System.out.println("Playing video: " + videoLibrary.getVideos().get(randomInt).getTitle());
      currentlyPlaying = videoLibrary.getVideos().get(randomInt);
      currentVideoPaused = false;

    }    
  }

  /**
   * pauses the current video
   */
  public void pauseVideo() {
    
    //checks if there is a video playing
    if(currentlyPlaying==null){
      System.out.println("Cannot pause video: No video is currently playing");

    }
    else{

      //pauses the video if it is not already paused
      if(!currentVideoPaused){

        currentVideoPaused = true;
        System.out.println("Pausing video: " + currentlyPlaying.getTitle());

      }
      else{
        //displays an error message if video is already paused
        System.out.println("Video already paused: " + currentlyPlaying.getTitle());
      }
    }
  }

  /**
   * continutes a video that has been paused
   */
  public void continueVideo() {
    
    //checks if there is a video playing
    if(currentlyPlaying == null){
      System.out.println("Cannot continue video: No video is currently playing");
    }
    else{

      //checks if video is paused
      if(!currentVideoPaused){
        System.out.println("Cannot continue video: Video is not paused");
      }
      else{
        //continues video and updates variables
        System.out.println("Continuing video: " + currentlyPlaying.getTitle());
        currentVideoPaused = false;
      }
    }
  }

  /**
   * show details of the video currently being played
   */
  public void showPlaying() {

    if(currentlyPlaying != null){
      
      //calls method to extract details and also checks if it is paused
      String info = formatDetailsOfVideo(currentlyPlaying);

      if(currentVideoPaused){
        info = info + " - PAUSED";
      }

      System.out.println("Currently playing: " + info);

    }
    else{
      System.out.println("No video is currently playing");
    }

  }

  /**
   * creates a playlist given a name
   * @param playlistName
   */
  public void createPlaylist(String playlistName) {
    
    for(VideoPlaylist playlist: playlists){

      //checks to see if the playlist already exists
      if(playlist.getName().equalsIgnoreCase(playlistName)){
        System.out.println("Cannot create playlist: A playlist with the same name already exists");
        return;
      }
    }
    //checks to see if the playlist name contains white space
    if(playlistName.contains(" ")){

      System.out.println("Cannot create playist: Playlist name contains whitespace");

    }
    else{

      //creates the playlist
      System.out.println("Successfully created new playlist: " + playlistName);
      playlists.add(new VideoPlaylist(playlistName));

    }

  }

  /**
   * checks to see if a playlist already exists
   * @param playlistName
   * @return boolean
   */
  public boolean ifPlaylistExists(String playlistName){

    for(VideoPlaylist playlist: playlists){
      if(playlist.getName().equalsIgnoreCase(playlistName)){
        return true;
      }
    }
    return false;

  }

  /**
   * checks to see if a video exists in the library
   * @param videoID
   * @return boolean
   */
  public boolean ifVideoExists(String videoId){

    for(Video video: videoLibrary.getVideos()){
      if(video.getVideoId().equalsIgnoreCase(videoId)){
        return true;
      }
    }
    return false;

  }

  /**
   * adds a video to a playlist given the name of the playlist and the video to be added
   * @param playlistName
   * @param videoId
   */
  public void addVideoToPlaylist(String playlistName, String videoId) {
    
    //checks if the playlist exists
    if(!ifPlaylistExists(playlistName)){
      System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
    }
    else{

      //checks if the video exists
      if(!ifVideoExists(videoId)){
        System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
      }
      else{

        //checks if the video is flagged
        if(videoLibrary.getVideo(videoId).isFlagged()){

          System.out.println("Cannot add video to " + playlistName + ": Video is currently flagged (reason: " 
          + videoLibrary.getVideo(videoId).getFlagged() + ")");

        }
        else{

          for(VideoPlaylist playlist: playlists){
            if(playlist.getName().equalsIgnoreCase(playlistName)){

              //checks if the video is already in the playlist
              if(!playlist.checkForDuplicates(videoId)){

                //adds the video to the playlist
                playlist.addVideo(videoLibrary.getVideo(videoId));
                System.out.println("Added video to " + playlistName + ": " + videoLibrary.getVideo(videoId).getTitle());
                return;
              }
              else{
                System.out.println("Cannot add video to " + playlistName + ": Video already added");
              }
            }
          }
        }
      }
    }
  }

  /**
   * displays the name of all playlists
   */
  public void showAllPlaylists() {
    
    //if the list is empty a message is dispalyed
    if(playlists.size()==0){
      System.out.println("No playlists exist yet");
    }else{

      System.out.println("Showing all playlists:");
      //the playlists are sorted and then displayed
      playlists.sort(new PlaylistSorter());
      for(VideoPlaylist playlist: playlists){
        System.out.println(playlist.getName());
      }

    }
  }


  /**
   * a class that implements the comparator interface and contains a method to compare two 
   * playlists by name
   */
  //The following resource was used to research how to write a comparator class
  //https://howtodoinjava.com/java/collections/arraylist/arraylist-sort-objects-by-field/
  public class PlaylistSorter implements Comparator<VideoPlaylist> {

    public int compare(VideoPlaylist playlist1, VideoPlaylist playlist2) {
        return playlist1.getName().compareTo(playlist2.getName());
    }
  };

  /**
   * displays the videos in a playlist
   * @param playlistName
   */
  public void showPlaylist(String playlistName) {
    
    //checks if a playlist exists
    if(!ifPlaylistExists(playlistName)){
      System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
    }
    else{

      System.out.println("Showing playlist: " + playlistName);

      //locates playlist and prints all videos in the playlist
      for(VideoPlaylist playlist: playlists){
        if(playlist.getName().equalsIgnoreCase(playlistName)){

          if(!playlist.checkIfEmpty()){
            playlist.printVideos();
            return;
          }
          else{
            System.out.println("No videos here yet");
            return;
          }

        }
      }
    }

    
  }

  /**
   * removes a video from a playlist
   * @param playlistName
   * @param videoId
   */
  public void removeFromPlaylist(String playlistName, String videoId) {
    
    //checks if a playlist exists
    if(!ifPlaylistExists(playlistName)){
      System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
    }
    else{
      //checks if a video exists
      if(!ifVideoExists(videoId)){
        System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
      }
      else{
        //locates playlist and checks to see if the video is in the playlist
        for(VideoPlaylist playlist: playlists){

          if(playlist.getName().equalsIgnoreCase(playlistName)){

            if(!playlist.checkForDuplicates(videoId)){
              System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
              return;
            }
            else{
              //removes video from playlist
              System.out.println("Removed video from " + playlistName + ": " + videoLibrary.getVideo(videoId).getTitle());
              playlist.removeVideo(videoLibrary.getVideo(videoId));
              return;
            }
          }
        }
      }
    }

  }

  /**
   * removes all videos from a given playlist
   * @param playlistName
   */
  public void clearPlaylist(String playlistName) {
   
    //checks to see if the playlist exists
    if(!ifPlaylistExists(playlistName)){
      System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
    }
    else{

      //removes all videos from the playlist
      for(VideoPlaylist playlist: playlists){
        if(playlist.getName().equalsIgnoreCase(playlistName)){
          playlist.clear();
          System.out.println("Successfully removed all videos from " + playlistName);
        }
      }

    }

  }

  /**
   * deletes a playlist given a name
   * @param playlistName
   */
  public void deletePlaylist(String playlistName) {
    
    //checks if the playlist exists
    if(!ifPlaylistExists(playlistName)){
      System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
    }
    else{

      //removes the playlist
      for(VideoPlaylist playlist: playlists){
        if(playlist.getName().equalsIgnoreCase(playlistName)){
          System.out.println("Deleted playlist: " + playlistName);
          playlists.remove(playlist);
          return;
        }
      }

    }
  }

  /**
   * a method to search for videos given the search term
   * @param searchTerm
   */
  public void searchVideos(String searchTerm) {

    int count = 1;
    List<Video> relevantVideos = new ArrayList<Video>();

    //creates an array list of all videos that are relevant to the search term
    for(Video video: videoLibrary.getVideos()){

      //could use regular expressions here?
      if(video.getTitle().toLowerCase().contains(searchTerm.toLowerCase())){
        relevantVideos.add(video);
      }
      
    }

    //if no videos are found a message is printed
    if(relevantVideos.size()==0){
      System.out.println("No search results for " + searchTerm);
    }
    else{

      //the videos are sorted
      relevantVideos.sort(new TitleSorter());
      System.out.println("Here are the results for " + searchTerm + ":");

      //the information for each video is printed
      for(Video video: relevantVideos){

        System.out.println(count + ") " + formatDetailsOfVideo(video));
        count++;

      }
   
    
      //display a message asking if they would like to play a video 
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
      System.out.println("If your answer is not a valid number, we will assume it's a no.");
      
      //Accepts user input
      Scanner scan = new Scanner(System.in);
      String userInput = scan.nextLine();
      scan.close();

      try{
        //input validation for the users choice
        if(Integer.parseInt(userInput)>0 && Integer.parseInt(userInput)<=count){

          //calls the playVideo method with the id of the users chosen video
          playVideo(relevantVideos.get(Integer.parseInt(userInput)-1).getVideoId());

        }
      }
      catch(NumberFormatException e){};
    }
    

  }

  /**
   * a method to search for videos based on a given tag
   * @param videoTag
   */
  public void searchVideosWithTag(String videoTag) {
    
    int count = 1;
    List<Video> relevantVideos = new ArrayList<Video>();

    //creates an array list of all videos that are relevant to the video tag
    for(Video video: videoLibrary.getVideos()){

      for(String tag: video.getTags()){

        //compares the tags in lower case - could have used regular expression
        if(tag.toLowerCase().contains(videoTag.toLowerCase())){
          relevantVideos.add(video);
        }
      }
      
    }

    //if no videos are found a message is printed
    if(relevantVideos.size()==0){
      System.out.println("No search results for " + videoTag);
    }
    else{

      //the videos are sorted
      relevantVideos.sort(new TitleSorter());
      System.out.println("Here are the results for " + videoTag + ":");

      //the information for each video is printed
      for(Video video: relevantVideos){

        System.out.println(count + ") " + formatDetailsOfVideo(video));
        count++;

      }
   
    
      //display a message asking if they would like to play a video 
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
      System.out.println("If your answer is not a valid number, we will assume it's a no.");
      
      //Accepts user input 
      Scanner scan = new Scanner(System.in);
      String userInput = scan.nextLine();
      scan.close();

      try{
        //input validation for the users choice
        if(Integer.parseInt(userInput)>0 && Integer.parseInt(userInput)<=count){

          //calls the playVideo method with the id of the users chosen video
          playVideo(relevantVideos.get(Integer.parseInt(userInput)-1).getVideoId());

        }
      }
      catch(NumberFormatException e){};
    }
  }

  /**
   * method to flag a video when a reason is not supplies
   * @param videoId
   */
  public void flagVideo(String videoId) {

    // another flagVideo method is called and a string is passed saying that the reason was not supplied
    flagVideo(videoId, "Not supplied");
  }

  /**
   * method to flag a video given an id and a reason
   * @param videoId
   * @param reason
   */
  public void flagVideo(String videoId, String reason) {

    //check if the video exists
    if(!ifVideoExists(videoId)){
      System.out.println("Cannot flag video: Video does not exist");
      
    }
    //check if the video has already been flagged
    else if(videoLibrary.getVideo(videoId).isFlagged()){
      System.out.println("Cannot flag video: Video is already flagged");
    }
    else{

      Video video = videoLibrary.getVideo(videoId);

      //video is flagged and relevant attributes are updated
      video.flagVideo(reason);
      video.updateIsFlagged();

      System.out.println("Successfully flagged video: " + video.getTitle() + " (reason: " + video.getFlagged() + ")");
    }
  }

  public void allowVideo(String videoId) {
    System.out.println("allowVideo needs implementation");
  }
}
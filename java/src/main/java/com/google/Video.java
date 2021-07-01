package com.google;

import java.util.Collections;
import java.util.List;

/** A class used to represent a video. */
class Video {

  private final String title;
  private final String videoId;
  private final List<String> tags;
  private String flagged = "";
  private boolean isFlagged = false;

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }

  /**
   * setter for the reason a video is flagged
   * @param reason
   */
  public void flagVideo(String reason){
    flagged = reason;
  }

  /**
   * getter for the reason a video is flagged
   * @return String
   */
  public String getFlagged(){
    return flagged;
  }

  /**
   * toggles the state of updateIsFlagged
   */
  public void updateIsFlagged(){
    isFlagged = !isFlagged;
  }

  /**
   * getter for isFlagged
   * @return boolean
   */
  public boolean isFlagged(){
    return isFlagged;
  }
}

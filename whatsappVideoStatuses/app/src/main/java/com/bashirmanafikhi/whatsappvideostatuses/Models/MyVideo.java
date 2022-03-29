package com.bashirmanafikhi.whatsappvideostatuses.Models;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Comparator;

@SuppressWarnings("serial")
public class MyVideo implements Comparable<MyVideo> , Serializable {

  private  int id;
  private String videoKey;
  private String videoUrl = "";
  private String videoTitle;
  private String Category;
  private String imageUrl;
  private int likesCount;
  private int videoViewsCount;
  private int downloadsCount;

  public int getLikesCount() {
    return likesCount;
  }

  public void setLikesCount(int likesCount) {
    this.likesCount = likesCount;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getCategory() {
    return Category;
  }

  public void setCategory(String category) {
    Category = category;
  }

  public String getVideoKey() {
    return videoKey;
  }

  public void setVideoKey(String videoKey) {
    this.videoKey = videoKey;
  }

  public String getVideoUrl() {
    return videoUrl;
  }

  public void setVideoUrl(String videoUrl) {
    this.videoUrl = videoUrl;
  }

  public String getVideoTitle() {
    return videoTitle;
  }

  public void setVideoTitle(String videoTitle) {
    this.videoTitle = videoTitle;
  }

  public int getVideoViewsCount() {
    return videoViewsCount;
  }

  public void setVideoViewsCount(int videoViewsCount) {
    this.videoViewsCount = videoViewsCount;
  }

  public MyVideo() {

  }




  @Override
  public int compareTo(MyVideo compareVideo) {
    int vid1Id = Integer.parseInt(((MyVideo) compareVideo).getVideoKey());
    int vid2Id = Integer.parseInt(this.getVideoKey());


    //String compareVideoId = ((MyVideo) compareVideo).getVideoKey();

    //ascending order
    return  vid1Id - vid2Id;
    //return compareVideoId.compareTo(this.getVideoKey());

    //descending order
    //return compareVideoId.compareTo(this.videoId);

  }


  public static Comparator<MyVideo> VideoViewsComparator
          = new Comparator<MyVideo>() {

    public int compare(MyVideo vid1, MyVideo vid2) {

      int vidCount1 = vid1.getVideoViewsCount();
      int vidCount2 = vid2.getVideoViewsCount();

      //ascending order
      return vidCount2 - vidCount1;

      //descending order
      //return vid1.getVideoViewsCount() < vid2.getVideoViewsCount() ? -1 : vid1.getVideoViewsCount() == vid2.getVideoViewsCount() ? 0 : 1;

    }

  };


  public static Comparator<MyVideo> VideoDownloadsComparator
          = new Comparator<MyVideo>() {

    public int compare(MyVideo vid1, MyVideo vid2) {

      int vidCount1 = vid1.getDownloadsCount();
      int vidCount2 = vid2.getDownloadsCount();

      //ascending order
      return vidCount2 - vidCount1;

      //descending order
      //return vid1.getVideoViewsCount() < vid2.getVideoViewsCount() ? -1 : vid1.getVideoViewsCount() == vid2.getVideoViewsCount() ? 0 : 1;

    }

  };


  public static Comparator<MyVideo> VideoLikesComparator
          = new Comparator<MyVideo>() {

    public int compare(MyVideo vid1, MyVideo vid2) {

      int vidCount1 = vid1.getLikesCount();
      int vidCount2 = vid2.getLikesCount();

      //ascending order
      return vidCount2 - vidCount1;

      //descending order
      //return vid1.getVideoViewsCount() < vid2.getVideoViewsCount() ? -1 : vid1.getVideoViewsCount() == vid2.getVideoViewsCount() ? 0 : 1;

    }

  };





  @Override
  public boolean equals(@Nullable Object myVideo) {
    if (myVideo == null)
      return  false;

    if (((MyVideo)myVideo).id == this.id)
      return  true;
    return false;
  }




  public int getDownloadsCount() {
    return downloadsCount;
  }

  public void setDownloadsCount(int downloadsCount) {
    this.downloadsCount = downloadsCount;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}


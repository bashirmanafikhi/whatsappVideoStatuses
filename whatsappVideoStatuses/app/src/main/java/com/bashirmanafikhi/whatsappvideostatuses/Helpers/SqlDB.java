package com.bashirmanafikhi.whatsappvideostatuses.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bashirmanafikhi.whatsappvideostatuses.Models.MyVideo;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class SqlDB extends SQLiteOpenHelper {

  public static final String DBname = "data.db";


  public SqlDB(Context context) {
    super(context, DBname, null, 2);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL("create table likesTable (id INTEGER PRIMARY KEY AUTOINCREMENT, videoKey TEXT, videoUrl TEXT, videoTitle TEXT, category TEXT, imageUrl TEXT, likesCount INT, viewsCount INT, downloadsCount INT)");
    db.execSQL("create table VideosTable (id INTEGER PRIMARY KEY AUTOINCREMENT, videoKey TEXT, videoUrl TEXT, videoTitle TEXT, category TEXT, imageUrl TEXT, likesCount INT, viewsCount INT, downloadsCount INT)");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS likesTable");
    db.execSQL("DROP TABLE IF EXISTS VideosTable");
    onCreate(db);
  }





  // Favorite Functions
  public ArrayList<MyVideo> GetAllFavoriteVideos(){

    ArrayList<MyVideo> videosList = new ArrayList<MyVideo>();
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor res = db.rawQuery("select * from likesTable", null);
    res.moveToFirst();
    while (res.isAfterLast() == false){
      MyVideo video = new MyVideo();

      video.setVideoKey(res.getString(1));
      video.setVideoUrl(res.getString(2));
      video.setVideoTitle(res.getString(3));
      video.setCategory(res.getString(4));
      video.setImageUrl(res.getString(5));
      video.setLikesCount(res.getInt(6));
      video.setVideoViewsCount(res.getInt(7));
      video.setDownloadsCount(res.getInt(8));

      videosList.add(0,video);
      res.moveToNext();
    }
    res.close();
    return  videosList;
  }

  public boolean CheckIfFavorite (String videoKey) {
    SQLiteDatabase db = this.getReadableDatabase();
    String tableName = "likesTable";
    String Query = "Select * from " + tableName + " where videoKey = '" + videoKey + "'";
    Cursor cursor = db.rawQuery(Query, null);
    if(cursor.getCount() <= 0){
      cursor.close();
      return false;
    }
    cursor.close();
    return true;
  }

  public boolean addFavoriteVideo (MyVideo video){
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();


    contentValues.put("videoKey", video.getVideoKey());
    contentValues.put("videoUrl", video.getVideoUrl());
    contentValues.put("videoTitle", video.getVideoTitle());
    contentValues.put("category", video.getCategory());
    contentValues.put("imageUrl", video.getImageUrl());
    contentValues.put("likesCount", video.getLikesCount());
    contentValues.put("viewsCount", video.getVideoViewsCount());
    contentValues.put("downloadsCount", video.getDownloadsCount());

    long result = db.insert("likesTable", null, contentValues);

    if (result == -1)
      return false;
    else
      return true;
  }

  public boolean removeFavoriteVideoByKey(String videoKey){
    SQLiteDatabase db = this.getWritableDatabase();

    long result = db.delete("likesTable","videoKey= ?", new String[]{videoKey});

    if (result == -1)
      return false;
    else
      return true;
  }





  // Videos Functions
  public ArrayList<MyVideo> GetAllVideos(){

    ArrayList<MyVideo> videosList = new ArrayList<MyVideo>();
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor res = db.rawQuery("select * from VideosTable", null);
    res.moveToFirst();
    while (res.isAfterLast() == false){
      MyVideo video = new MyVideo();

      video.setVideoKey(res.getString(1));
      video.setVideoUrl(res.getString(2));
      video.setVideoTitle(res.getString(3));
      video.setCategory(res.getString(4));
      video.setImageUrl(res.getString(5));
      video.setLikesCount(res.getInt(6));
      video.setVideoViewsCount(res.getInt(7));
      video.setDownloadsCount(res.getInt(8));

      videosList.add(video);
      res.moveToNext();
    }
    res.close();
    return  videosList;
  }

  public boolean addNewVideo (MyVideo video){
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();


    contentValues.put("videoKey", video.getVideoKey());
    contentValues.put("videoUrl", video.getVideoUrl());
    contentValues.put("videoTitle", video.getVideoTitle());
    contentValues.put("category", video.getCategory());
    contentValues.put("imageUrl", video.getImageUrl());
    contentValues.put("likesCount", video.getLikesCount());
    contentValues.put("viewsCount", video.getVideoViewsCount());
    contentValues.put("downloadsCount", video.getDownloadsCount());

    long result = db.insert("VideosTable", null, contentValues);

    if (result == -1)
      return false;
    else
      return true;
  }

  public void addNewVideosList (ArrayList<MyVideo> videos){

    try {
      for (MyVideo v : videos){
        addNewVideo(v);
      }
    } catch (ConcurrentModificationException x){

    }

  }

  public boolean CheckIfNoVideos (){
    SQLiteDatabase db = this.getWritableDatabase();
    String count = "SELECT * FROM VideosTable";
    Cursor mcursor = db.rawQuery(count, null);
    int icount = 0;
    if (mcursor.getCount() > 0) {
      mcursor.moveToFirst();
      icount = mcursor.getInt(0);
      mcursor.close();
    }
    if(icount>0)
      return false;
    else
      return true;
  }

  public void updateVideos(ArrayList<MyVideo> videos){
    SQLiteDatabase db = this.getWritableDatabase();
    int i = 1;
    for(MyVideo v : videos){

      ContentValues contentValues = new ContentValues();

      contentValues.put("likesCount", v.getLikesCount());
      contentValues.put("viewsCount", v.getVideoViewsCount());
      contentValues.put("downloadsCount", v.getDownloadsCount());

      db.update("VideosTable", contentValues, "id = " + i, null);

      i++;



    }
  }

  public String GetLastVideoKey(){
    String lastVideoKey;

    SQLiteDatabase db = this.getWritableDatabase();
    String selectQuery  = "SELECT * FROM VideosTable";
    Cursor cursor = db.rawQuery(selectQuery , null);
    cursor.moveToLast();
    lastVideoKey = cursor.getString(1);

    return lastVideoKey;
  }


  // Tables Functions
  public boolean CheckIfTableIsEmpty (String tableName){
    SQLiteDatabase db = this.getWritableDatabase();
    String count = "SELECT count(*) FROM " + tableName;
    Cursor mcursor = db.rawQuery(count, null);
    mcursor.moveToFirst();
    int icount = mcursor.getInt(0);
    if(icount>0)
      return false;
    else
      return true;
  }






    /* boolean insertData(String videoKey, String category){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("videoKey", videoKey);
        contentValues.put("category", category);

        long result = db.insert("likesTable", null, contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }*/


    /*public boolean updateData (String id, String videoKey, String category){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("videoKey", videoKey);
        contentValues.put("category", category);

        long result = db.update("likesTable", contentValues, "id= ?", new String[]{id});

        if (result == -1)
            return false;
        else
            return true;
    }*/


}

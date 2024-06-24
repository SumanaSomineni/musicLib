package com.example.song.service;

import java.util.*;
import com.example.song.model.Song;
import com.example.song.model.SongRowMapper;
import com.example.song.repository.SongRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SongH2Service implements SongRepository {

   @Autowired
   private JdbcTemplate db;

   @Override
   public void deleteSong(int songId) {
      db.update("delete from song where id=?", songId);
   }

   @Override
   public Song updateSong(int songId, Song song) {
      if (song.getSongName() != null) {
         db.update("update song set name =? where id=?", song.getSongName(),songId);
      }
      if (song.getLyricist() != null) {
         db.update("update song set lyricist =? where id=?", song.getLyricist(),songId);
      }
      if (song.getSinger() != null) {
         db.update("update song set singer =? where id=?", song.getSinger(),songId);
      }
      if(song.getMusicDirector() != null){
         db.update("update song set musicDirector =? where id=?", song.getMusicDirector(),songId);
      }
      return getSongById(songId);

   }
   @Override 
   public Song addSong(Song song){
      db.update("insert into song(name,lyricist,singer,musicDirector) values (?,?,?,?)",song.getSongName(),
      song.getLyricist(),song.getSinger(),song.getMusicDirector());
      Song savedSong=db.queryForObject("select * from song where name=? and lyricist=? and singer=? and musicDirector=?",
      new SongRowMapper(), song.getSongName(),song.getLyricist(),song.getSinger(),song.getMusicDirector());
      return savedSong;
   }
   @Override 
   public Song getSongById(int songId){
      try{
         Song song=db.queryForObject("select * from song where id=?",new SongRowMapper(),songId);
         return song;
      }catch (Exception e){
         throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      }
   }
   @Override 
   public ArrayList<Song> getSongs(){
      List<Song> songList=db.query("select * from song",new SongRowMapper());
      ArrayList<Song> songs=new ArrayList<>(songList);
      return songs;
   }
}
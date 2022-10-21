package model;

import java.util.Comparator;

public class Song {
private String name;
    private String artist;
    private String album;
    private String year;

    public Song(){
        this.name = this.artist = this.album = this.year = "";
    }

    public Song(String name, String artist, String album, String year){
        this.name=name;
        this.artist=artist;
        this.album=album;
        this.year=year;
    }

    public void editSong(String name, String artist, String album, String year){
        this.name=name;
        this.artist=artist;
        this.album=album;
        this.year=year;
    }
    

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setArtist(String artist){
        this.artist = artist;
    }

    public String getArtist(){
        return artist;
    }

    public void setAlbum(String album){
        this.album = album;
    }

    public String getAlbum(){
        return album;
    }

    public void setYear(String year){
        this.year = year;
    }

    public String getYear(){
        return year;
    }
    
    public String toString() {
    	return name + "-" + artist;
    }
    
    public static Comparator<Song> sortName = new Comparator<Song>() {

		public int compare(Song track1, Song track2) {
		   String name1 = track1.getName().toLowerCase();
		   String name2 = track2.getName().toLowerCase();
		   return name1.compareTo(name2);
	}};
	
	public static Comparator<Song> sortArtist = new Comparator<Song>() {

		public int compare(Song track1, Song track2) {
		   String artist1 = track1.getArtist().toLowerCase();
		   String artist2 = track2.getArtist().toLowerCase();
		   return artist1.compareTo(artist2);
	}};
	

}

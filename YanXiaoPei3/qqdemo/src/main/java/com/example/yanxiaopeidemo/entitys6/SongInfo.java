package com.example.yanxiaopeidemo.entitys6;


public class SongInfo {
    private int id;
    private String songName;
    private String songArtist;
    private int collect;
    private String songCover;
    private String songFile;

    public SongInfo() {
    }

    public SongInfo(int id, String songName, String songArtist, int collect, String songCover, String songFile) {
        this.id = id;
        this.songName = songName;
        this.songArtist = songArtist;
        this.collect = collect;
        this.songCover = songCover;
        this.songFile = songFile;
    }

    @Override
    public String toString() {
        return "SongInfo{" +
                "id=" + id +
                ", songName='" + songName + '\'' +
                ", songArtist='" + songArtist + '\'' +
                ", collect=" + collect +
                ", songCover='" + songCover + '\'' +
                ", songFile='" + songFile + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public String getSongCover() {
        return songCover;
    }

    public void setSongCover(String songCover) {
        this.songCover = songCover;
    }

    public String getSongFile() {
        return songFile;
    }

    public void setSongFile(String songFile) {
        this.songFile = songFile;
    }
}


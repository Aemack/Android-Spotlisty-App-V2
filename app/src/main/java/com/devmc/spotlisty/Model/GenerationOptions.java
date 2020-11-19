package com.devmc.spotlisty.Model;

public class GenerationOptions {
    private String type;
    private int size;
    private int popularity;
    private String mode;
    private int tempo;
    private int valence;
    private int key;
    private String genre;
    private String seedTracks;

    public GenerationOptions(String type){
        this.type = type;
        genre = "";
        seedTracks = "";
        size = 20;
        popularity = 0;
        mode = null;
        tempo = 0;
        valence = 0;
        key = -1;
    }

    public String getSeedTracks(){
        return seedTracks;
    }

    public void setSeedTracks(String seedTracks){
        this.seedTracks = seedTracks;
    }

    public String getGenre(){
        return genre;
    }

    public void setGenre(String genre){
        this.genre = genre;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public int getValence(){ return valence; }

    public void setValence(int valence){
        this.valence = valence;
    }


    public int getSize(){
        return size;
    }

    public void setSize(int size){
        this.size = size;
    }


    public int getPopularity(){
        return popularity;
    }

    public void setPopularity(int popularity){
        this.popularity = popularity;
    }


    public void setMode(String mode){
        this.mode = mode;
    }

    public String getMode(){
        return mode;
    }


    public void setTempo(int tempo){
        this.tempo = tempo;
    }

    public int getTempo(){
        return tempo;
    }


    public int getKey(){
        return key;
    }

    public void setKey(int key){
        this.key = key;
    }

}


package com.example.wdmobilee;

import java.io.Serializable;
import java.util.ArrayList;

public class Folder implements Serializable {
    String name;
    private String path;
    private String date;

    public Folder(){}
    public Folder(String name, String path, String date) {
        this.name = name;
        this.path = path;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public ArrayList<Folder>getFolders(ArrayList<Folder> searchArray,String search){
        ArrayList<Folder>result=new ArrayList<>();
        for (Folder temp:searchArray){
            if (temp.getName().equals(search)){
                result.add(temp);
            }
        }
        return result;
    }
}

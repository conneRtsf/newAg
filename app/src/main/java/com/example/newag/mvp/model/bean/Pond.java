package com.example.newag.mvp.model.bean;

import com.google.gson.JsonArray;

public class Pond {
    String time;
    SpecificPond specificPond;

    public Pond(String time,SpecificPond specificPond) {
        this.specificPond=specificPond;
        this.time=time;
    }

    public static class SpecificPond{
        int id;
        String name;
        String location;
        double length;
        double width;
        double height;
        double lengthUsed;
        double widthUsed;
        double heightUsed;
        String orientation;
        double volume;
        double volumeUsed;
        String time;
        String product;
        String username;
        public SpecificPond(int id, String name,
                            String location, double length,
                            double width, double height,
                            double lengthUsed, double widthUsed,
                            double heightUsed, String orientation,
                            double volume, double volumeUsed,
                            String time, String product,
                            String username) {
            this.id=id;
            this.name=name;
            this.location=location;
            this.length=length;
            this.width=width;
            this.height=height;
            this.lengthUsed=lengthUsed;
            this.widthUsed=widthUsed;
            this.heightUsed=heightUsed;
            this.orientation=orientation;
            this.volume=volume;
            this.volumeUsed=volumeUsed;
            this.time=time;
            this.product=product;
            this.username=username;
        }
        public int getId() {
            return id;
        }
        public String getName(){
            return name;
        }
        public String getLocation(){
            return location;
        }
        public double getLength(){
            return length;
        }
        public double getWidth(){
            return width;
        }
        public double getHeight(){
            return height;
        }
        public double getLengthUsed(){
            return lengthUsed;
        }
        public double getWidthUsed(){
            return widthUsed;
        }
        public double getHeightUsed(){
            return heightUsed;
        }
        public String getOrientation(){
            return orientation;
        }
        public double getVolume(){
            return volume;
        }
        public double getVolumeUsed(){
            return volumeUsed;
        }
        public String getTime(){
            return time;
        }
        public String getProduct(){
            return  product;
        }
        public String getUsername(){
            return username;
        }
    }
}

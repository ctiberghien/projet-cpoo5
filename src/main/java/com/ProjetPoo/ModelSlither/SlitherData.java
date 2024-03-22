package com.ProjetPoo.ModelSlither;

import java.io.Serializable;
import java.util.LinkedList;

import javafx.geometry.Point2D;

public class SlitherData implements Serializable {
    private long startTime;
    private PointSerialisable pointeur;
    private PointSerialisable pos;
    private PointSerialisable newFood;
    private PointSerialisable foodAccelere;
    private PointSerialisable foodEaten;
    private LinkedList<PointSerialisable> foodBase;
    private LinkedList<PointSerialisable> foodDead;
    private boolean isAccelerating;
    private int taille;

    public SlitherData(long startTime, Point2D ptr, Point2D position, int taille) {
        this.startTime = startTime;
        isAccelerating = false;
        pointeur = new PointSerialisable(ptr);
        pos = new PointSerialisable(position);
        foodDead = new LinkedList<>();
        foodBase = new LinkedList<>();
        this.taille=taille;
    }

    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

    public long getStartTime() {
        return startTime;
    }

    public boolean isAccelerating() {
        return isAccelerating;
    }

    public void setAccelerating(boolean isAccelerating) {
        this.isAccelerating = isAccelerating;
    }

    public PointSerialisable getPointeur() {
        return pointeur;
    }

    public PointSerialisable getPos() {
        return pos;
    }

    public PointSerialisable getFoodEaten() {
        return foodEaten;
    }

    public PointSerialisable getNewFood() {
        return newFood;
    }

     public LinkedList<PointSerialisable> getFoodBase() {
        return foodBase;
    }

    public PointSerialisable getFoodAccelere() {
        return foodAccelere;
    }

    public void setFoodAccelere(PointSerialisable foodAccelere) {
        this.foodAccelere = foodAccelere;
    }
    
    public void setFoodBase(LinkedList<PointSerialisable> foodBase) {
        this.foodBase = foodBase;
    }

     public LinkedList<PointSerialisable> getFoodDead() {
        return foodDead;
    }

    public void setFoodDead(LinkedList<PointSerialisable> foodDead) {
        this.foodDead = foodDead;
    }
 
    public void setId(int startTime) {
        this.startTime = startTime;
    }

    public void setPointeur(PointSerialisable pointeur) {
        this.pointeur = pointeur;
    }

    public void setPos(PointSerialisable pos) {
        this.pos = pos;
    }

    public void setNewFood(PointSerialisable newFood) {
        this.newFood = newFood;
    }

    public void setFoodEaten(PointSerialisable foodEaten) {
        this.foodEaten = foodEaten;
    }

    public static PointSerialisable creePSerialisable(Point2D p2d) {
        return new PointSerialisable(p2d);
    }

    @Override
    public String toString() {
        return "SlitherData [startTime=" + startTime + ", pointeur=" + pointeur + ", pos=" + pos + ", acc=" + isAccelerating + "]";
    }

}

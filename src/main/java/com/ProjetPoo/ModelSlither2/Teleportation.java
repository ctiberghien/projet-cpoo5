package com.ProjetPoo.ModelSlither2;

import java.util.ArrayList;
import java.util.Random;

import com.ProjetPoo.ModelSlither.TeteSlither;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class Teleportation extends Circle{
    private Teleportation arrive;
    static Random rand = new Random();
    private boolean hasPlayer = false;

    public Teleportation(double x, double y, double radius) {
        super(x,y,radius);
    }
    public Teleportation(Pane root) {
        super(rand.nextInt((int)root.getPrefWidth()-200)+100, rand.nextInt((int)root.getPrefHeight()-200)+100, rand.nextInt(10)+15);
    }

    public void setArrive(Teleportation arrive) {
        this.arrive = arrive;
        arrive.setRadius(getRadius());
    }

    public Teleportation getArrive() {
        return arrive;
    }

    public void setHasPlayer(boolean hasPlayer) {
        this.hasPlayer = hasPlayer;
    }
    

    public void Teleporte(TeteSlither tete) {
        tete.setCenterX(arrive.getCenterX());
        tete.setCenterY(arrive.getCenterY());
        tete.setCoordonnee(new Point2D(arrive.getCenterX(), arrive.getCenterY()));
    }

    public void isInTeleporte(ArrayList<TeteSlither> joueurs) {
        for(TeteSlither tete : joueurs) {
            if(tete.getCoordonneeX()>this.getCenterX()-this.getRadius() && tete.getCoordonneeX()<this.getCenterX()+this.getRadius()
            && tete.getCoordonneeY()>this.getCenterY()-this.getRadius() && tete.getCoordonneeY()<this.getCenterY()+this.getRadius()) {
                if(!hasPlayer && this.getRadius()>=tete.getRadius()) {
                    arrive.setHasPlayer(true);
                    Teleporte(tete);  
                }
                return;
            }
        }
        hasPlayer=false;
    }
}

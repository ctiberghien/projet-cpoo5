package com.ProjetPoo.ModelSlither;

import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
public class NourritureSlither extends CorpsSlither {

    Random rand = new Random();
    public NourritureSlither(double x, double y) {
        super(x, y, 5);
        Color fill = Color.rgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255), 1.0);
        setFill(fill);
    }

    public static NourritureSlither creerNourriture(Pane p, double width, double height) {
        Random rand = new Random();
        int randX = (rand.nextInt((int) width-140))+70;
        int randY = (rand.nextInt((int) height-140))+70;
        NourritureSlither newNourriture = new NourritureSlither(randX, randY);
        
        p.getChildren().add(newNourriture);
        return newNourriture;
    }

    public static void creerNourritureCorps(Pane p, double x, double y, int score, ArrayList<NourritureSlither> nourritures, double radius) {
        Random rand = new Random();
        for(int i=0; i<score; i++) {
            double randX = rand.nextDouble(-(radius/2),radius/2);
            double randY = rand.nextDouble(-(radius/2),radius/2);
            NourritureSlither newNourriture = new NourritureSlither(x+randX, y+randY);
            p.getChildren().add(newNourriture);
            nourritures.add(newNourriture);
        }
    }

    public static NourritureSlither creerNourritureAcceleration(Pane p, double x, double y, ArrayList<NourritureSlither> nourritures, double radius) {
        Random rand = new Random();
        double randX = rand.nextDouble(-(radius/2),radius/2);
        double randY = rand.nextDouble(-(radius/2),radius/2);
        NourritureSlither newNourriture = new NourritureSlither(x+randX, y+randY);
        p.getChildren().add(newNourriture);
        nourritures.add(newNourriture);
        return newNourriture;
    }

    @Override
    public String toString() {
        return "x:"+getCoordonneeX()+";y:"+getCoordonneeY();
    }

}

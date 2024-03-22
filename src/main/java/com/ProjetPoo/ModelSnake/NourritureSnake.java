package com.ProjetPoo.ModelSnake;

import java.util.Random;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class NourritureSnake extends CorpsSnake{
    public NourritureSnake(double cooX, double cooY, double width) {
        super(cooX, cooY, width);
        setFill(Color.RED);
    }

    public static NourritureSnake creerNourriture(Pane p, double width, int i, int j) {
        Random rand = new Random();
        NourritureSnake newNourriture = new NourritureSnake(rand.nextInt(i)*width, rand.nextInt(j)*width, width);
        p.getChildren().add(newNourriture);
        return newNourriture;
    }
}

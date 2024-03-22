package com.ProjetPoo.Apps;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;

import java.util.ArrayList;
import com.ProjetPoo.ModelSnake.NourritureSnake;
import com.ProjetPoo.ModelSnake.TeteSnake;
import javafx.animation.Timeline;

import javafx.geometry.Point2D;
import javafx.scene.Scene;

import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.input.*;


public class Snake extends Application {
    protected int i = 0;
    protected int j = 0;
    public static int tailleGrille = 20;
    protected int width = 800;
    protected int height = 600;
    protected ArrayList<NourritureSnake> nourritures;
    protected TeteSnake tete;
    protected Pane root;
    protected Scene scene;

    @Override
    public void start(Stage primaryStage){
        try {
            setTerrain();
            eventTouches(scene, root);

            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {}
    }



    public boolean collisionNourriture(ArrayList<NourritureSnake> nourritures, TeteSnake tete, Pane p)  {
        for(NourritureSnake n : nourritures) {
            if(n.getCoordonnee().equals(tete.getCoordonnee())) {
                p.getChildren().remove(n);
                nourritures.remove(n);
                return true;
            }
        }
        return false;
    } 

    public void setTerrain() {
        root = new Pane();
        for (int y = 0; y < height; y += 20) {
                j++;
                Line ligneHorizontale = new Line(0, y, width, y);
                root.getChildren().add(ligneHorizontale);
        }
        for (int x = 0; x < width; x += 20) {
            i++;
            Line ligneVerticale = new Line(x, 0, x, height);
            root.getChildren().add(ligneVerticale);
        }
        nourritures = new ArrayList<>();
        nourritures.add(NourritureSnake.creerNourriture(root, tailleGrille, i, j));
        tete = new TeteSnake(i/2*tailleGrille, j/2*tailleGrille, tailleGrille);
        root.getChildren().add(tete);
        scene = new Scene(root, 800, 600);
    }

    public void eventTouches(Scene scene, Pane root) {
        scene.setOnKeyPressed(event -> {
            KeyCode keyCode = event.getCode();
            Point2D oldPos = tete.getCoordonnee();

            switch (keyCode) {
                case UP:
                    tete.setY((tete.getCoordonnee().getY()-1)*tailleGrille);
                    tete.setCoordonnee(new Point2D(tete.getCoordonnee().getX(), tete.getCoordonnee().getY()-1));
                    break;
                case DOWN:
                    tete.setY((tete.getCoordonnee().getY()+1)*tailleGrille);
                    tete.setCoordonnee(new Point2D(tete.getCoordonnee().getX(), tete.getCoordonnee().getY()+1));
                    break;
                case LEFT:
                    tete.setX((tete.getCoordonnee().getX()-1)*tailleGrille);
                    tete.setCoordonnee(new Point2D(tete.getCoordonnee().getX()-1, tete.getCoordonnee().getY()));
                    break;
                case RIGHT:
                    tete.setX((tete.getCoordonnee().getX()+1)*tailleGrille);
                    tete.setCoordonnee(new Point2D(tete.getCoordonnee().getX()+1, tete.getCoordonnee().getY()));
                    break;
                default:
            }

            if(collisionNourriture(nourritures, tete, root)) {
                nourritures.add(NourritureSnake.creerNourriture(root, tailleGrille, i, j));
                System.out.println("oui");
                tete.addCorps(root);
            }
            tete.deplacerCorps(oldPos);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}


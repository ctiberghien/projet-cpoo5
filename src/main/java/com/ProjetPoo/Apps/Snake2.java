package com.ProjetPoo.Apps;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

import javax.swing.SwingUtilities;

import com.ProjetPoo.ModelSlither.TeteSlither;
import com.ProjetPoo.ModelSnake.NourritureSnake;
import com.ProjetPoo.ModelSnake.TeteSnake;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.geometry.Point2D;
import javafx.scene.Scene;

import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.input.*;


public class Snake2 extends Snake{
    static Timeline tm;

    @Override
    public void start(Stage primaryStage) {
        super.start(primaryStage);   
        animation(primaryStage);
    }

    @Override
    public void eventTouches(Scene scene, Pane root) {
        scene.setOnKeyPressed(event -> { 
            KeyCode keyCode = event.getCode();
            switch(keyCode) {
                case UP:
                    if(tete.getDirection()!=1)
                    tete.setDirection(0);
                    break;
                case DOWN:
                    if(tete.getDirection()!=0)
                    tete.setDirection(1);
                    break;
                case LEFT:
                    if(tete.getDirection()!=3)
                    tete.setDirection(2);
                    break;
                case RIGHT:
                    if(tete.getDirection()!=2)
                    tete.setDirection(3);
                    break;
                default:
            }
        });
    }

    public void animation(Stage primaryStage) {
        try {
            final Duration oneFrameAmt = Duration.millis(1000 / 8);
            final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        Point2D oldPos = tete.getCoordonnee();
                        if(tete.getDirection()==0) {
                            tete.setY((tete.getCoordonnee().getY()-1)*tailleGrille);
                            tete.setCoordonnee(new Point2D(tete.getCoordonnee().getX(), tete.getCoordonnee().getY()-1));
                        } 
                        else if(tete.getDirection()==1) {
                            tete.setY((tete.getCoordonnee().getY()+1)*tailleGrille);
                            tete.setCoordonnee(new Point2D(tete.getCoordonnee().getX(), tete.getCoordonnee().getY()+1));
                        }
                        else if(tete.getDirection()==2) {
                            tete.setX((tete.getCoordonnee().getX()-1)*tailleGrille);
                            tete.setCoordonnee(new Point2D(tete.getCoordonnee().getX()-1, tete.getCoordonnee().getY()));
                        } 
                        else if(tete.getDirection()==3) {
                            tete.setX((tete.getCoordonnee().getX()+1)*tailleGrille);
                            tete.setCoordonnee(new Point2D(tete.getCoordonnee().getX()+1, tete.getCoordonnee().getY()));
                        }
                        if(collisionNourriture(nourritures, tete, root)) {
                            nourritures.add(NourritureSnake.creerNourriture(root, tailleGrille, i, j));
                            tete.addCorps(root);
                        }
                        tete.deplacerCorps(oldPos);
                        collisionMur();
                        if(tete.collision()) {
                            mort(tm, primaryStage);
                        }
                    }
                }
            );

            tm = new Timeline(8,oneFrame);
            tm.setCycleCount(Animation.INDEFINITE);
            SwingUtilities.invokeLater(() -> {
                tm.play();
            });
        } catch (Exception e) {}
    }

    public void mort(Timeline t, Stage stage) {
        t.stop();
        Snake2 m = new Snake2();
        m.start(stage);
    }

    public void collisionMur() {
        if(tete.getX()<0) {
            tete.setX(width-tailleGrille);
        } else if(tete.getX()>width) {
            tete.setX(0);
        } else if(tete.getY()<0) {
            tete.setY(height-tailleGrille);
        } else if(tete.getY()>height) {
            tete.setY(0);
        }
        tete.setCoordonnee(new Point2D(tete.getX()/tailleGrille, tete.getY()/tailleGrille));
    }

    public static void main(String[] args) {
        launch(args);
    }
}

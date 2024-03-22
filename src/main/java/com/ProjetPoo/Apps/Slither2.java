package com.ProjetPoo.Apps;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.SwingUtilities;

import com.ProjetPoo.Model.AiSlither;
import com.ProjetPoo.ModelSlither.NourritureSlither;
import com.ProjetPoo.ModelSlither.TeteSlither;
import com.ProjetPoo.ModelSlither2.Teleportation;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Slither2 extends Slither{
    private long lastUpdateTp = System.currentTimeMillis();
    Teleportation tpDepart;
    Teleportation tpArrive;

    @Override
    public void start(Stage primaryStage) {
        super.start(primaryStage);
    }

    @Override
    public void setTerrain() {
        super.setTerrain();
        root.setPrefSize(1600,900);
        tpDepart = new Teleportation(root);
        tpArrive = new Teleportation(root);
        tpDepart.setArrive(tpArrive);
        tpArrive.setArrive(tpDepart);
        root.getChildren().addAll(tpArrive, tpDepart);
    }

    @Override
    public void animation(Stage primaryStage) {
        final Duration oneFrameAmt = Duration.millis(1000 / 30);
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
            new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    // System.out.println("x : "+souris.getX()+"\ny : "+souris.getY());
                    tpDepart.isInTeleporte(joueurs);
                    tpArrive.isInTeleporte(joueurs);
                    long elapsedTime = System.currentTimeMillis() - lastUpdate;
                    if(elapsedTime>30000) {
                        root.getChildren().removeAll(tpDepart, tpArrive);
                        tpDepart = new Teleportation(root);
                        tpArrive = new Teleportation(root);
                        tpDepart.setArrive(tpArrive);
                        tpArrive.setArrive(tpDepart);
                        root.getChildren().addAll(tpDepart,tpArrive);
                        lastUpdate = System.currentTimeMillis();
                    }

                    if(souris.getX()!=0 && souris.getY()!=0) {
                        tete.deplacerVersSouris(milieu, souris);
                    } else {
                        tete.deplacerVersSouris(milieu, new Point2D(souris.getX()<=0?1:souris.getX(), souris.getY()<=0?1:souris.getY()));
                    }
                    //System.out.println(souris);
                    for (TeteSlither j : joueurs) {   
                        if(!tete.equals(j)) {
                            if(tete.hasCollide(j)) {
                                mort(tete);
                            }
                        }  
                    }
                    if(tete.getCoordonneeX()>root.getWidth() || tete.getCoordonneeX()<0 
                    || tete.getCoordonneeY()>root.getHeight() || tete.getCoordonneeY()<0) {
                        mort(tete);
                    }


                    for(TeteSlither j : joueurs) {
                        if(j instanceof AiSlither) {
                            AiSlither ai = (AiSlither)j;
                            if(ai.deplacement(nourritures, root, joueurs) 
                            || ai.getCoordonneeX()>root.getWidth() || ai.getCoordonneeX()<0 
                            || ai.getCoordonneeY()>root.getHeight() || ai.getCoordonneeY()<0) {
                                mort(j);
                            }
                        }
                    }
                    tete.hasEaten(nourritures, root);

                       
                    int randomNourriture = rand.nextInt(30);
                    if (randomNourriture==1) {
                        //System.out.println("food added");
                        nourritures.add(NourritureSlither.creerNourriture(root, root.getWidth(), root.getHeight()));
                        //System.out.println(nourritures.getLast().toString());
                    }
                    event.consume();
                }
            }
        );

        tm = new Timeline(30,oneFrame);
        tm.setCycleCount(Animation.INDEFINITE);
        SwingUtilities.invokeLater(() -> {
            tm.play();
        });
    }
}

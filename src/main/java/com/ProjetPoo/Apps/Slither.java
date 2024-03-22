package com.ProjetPoo.Apps;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.input.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

import javax.swing.SwingUtilities;

import com.ProjetPoo.ModelSlither.*;
import com.ProjetPoo.ModelSnake.NourritureSnake;
import com.ProjetPoo.ModelSnake.TeteSnake;
import com.ProjetPoo.Apps.*;
import com.ProjetPoo.Model.AiSlither;
import com.ProjetPoo.Model.Tete;

public class Slither extends Snake2 {

    protected Point2D souris;
    protected Point2D milieu = new Point2D(640, 360);
    protected ArrayList<TeteSlither> joueurs = new ArrayList<>();
    protected ArrayList<TeteSlither> joueursARemove = new ArrayList<>();
    protected ArrayList<NourritureSlither> nourritures = new ArrayList<>();
    protected Random rand = new Random();
    protected TeteSlither tete;
    protected Pane cam;
    //private double zoomLevel = 1.0;
    public int nbIa = 0;
    protected long lastUpdate = System.currentTimeMillis();

    @Override
    public void start(Stage primaryStage) {
        super.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setIa(Pane root) {
        for(int i=0; i<nbIa; i++) {
            AiSlither ai = new AiSlither(rand.nextInt((int)root.getPrefWidth()), rand.nextInt((int)root.getPrefHeight()), 15,root);
            joueurs.add(ai);
            root.getChildren().add(ai);
            ai.deplacerVersSouris(new Point2D(ai.getCoordonneeX(), ai.getCoordonneeY()), new Point2D(rand.nextInt(1280), rand.nextInt(720)));
        }
    }

    @Override
    public void setTerrain() {
        root = new Pane();
        cam = new Pane();
        root.setPrefSize(11520,6480);
        scene = new Scene(cam, 1280, 720);
        root.setId("root");
        souris = new Point2D(rand.nextInt(800), rand.nextInt(600));
        scene.getStylesheets().addAll(getClass().getClassLoader().getResource("style.css").toExternalForm());
        // tete = new TeteSlither(rand.nextInt(70,5050), rand.nextInt(70,2810), 15);
        // ai = new TeteSlither(rand.nextInt(70,5050), rand.nextInt(70,2810), 15);

        //testing purposes 
        tete = new TeteSlither(500, 500, 15, root);

        joueurs.add(tete);
        cam.getChildren().addAll(root);
        root.getChildren().addAll(tete);

        // Ajout des ia
        setIa(root);
        for(int i=0; i<100; i++) {
            nourritures.add(NourritureSlither.creerNourriture(root, root.getPrefWidth(), root.getPrefHeight()));
        }
    }

    /**
     * Gère les événements liés à l'interface utilisateur, tels que le déplacement de la souris et les clics.
     *
     * @param scene La scène principale du jeu.
     * @param root  Le conteneur racine de la scène.
     */
    @Override
    public void eventTouches(Scene scene, Pane root) {
        tete.centerXProperty().addListener((observable, oldValue, newValue) -> {
            double deltaX = scene.getWidth() / 2 - (double) newValue;
            cam.setTranslateX(deltaX);
        });
        tete.centerYProperty().addListener((observable, oldValue, newValue) -> {
            double deltaY = scene.getHeight() / 2 - (double) newValue;
            cam.setTranslateY(deltaY);
        });
        scene.setOnMouseMoved(e -> {
            souris = new Point2D(e.getSceneX(), e.getSceneY());
        });
        scene.setOnMouseDragged(e -> {
            if(e.getButton() == MouseButton.SECONDARY) {
                souris = new Point2D(e.getSceneX(), e.getSceneY());
                tete.setVitesse(tete.getVitesseAccelere());
                tete.changementCouleur(true);
                acceleration();
            }
        });
        scene.setOnMouseReleased(e -> {
            if(e.getButton() == MouseButton.SECONDARY) {
                tete.setVitesse(tete.getVitesseNormal());
                tete.changementCouleur(false);
            }
        });
    }

    /**
     * Gère ce qu'il va se passer à chaque frame
     */
    @Override
    public void animation(Stage primaryStage) {
        final Duration oneFrameAmt = Duration.millis(1000 / 30);
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
            new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    
                    if(souris.getX()!=0 && souris.getY()!=0) {
                        tete.deplacerVersSouris(milieu, souris);
                    } else {
                        tete.deplacerVersSouris(milieu, new Point2D(souris.getX()<=0?1:souris.getX(), souris.getY()<=0?1:souris.getY()));
                    }
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

    /**
     * Traite la mort d'un joueur, envoie les informations au serveur et effectue les actions appropriées.
     *
     * @param j Le joueur décédé.
     */
    public void mort(TeteSlither j) {
        int scoreParCorps = j.getScore()/j.getCorps().size();
        System.out.println(scoreParCorps);
        NourritureSlither.creerNourritureCorps(root, j.getCenterX(), j.getCenterY(), scoreParCorps, nourritures, j.getRadius());
        for(CorpsSlither c : j.getCorps()) {
            NourritureSlither.creerNourritureCorps(root, c.getCenterX(), c.getCenterY(), scoreParCorps, nourritures, j.getRadius());
        }
        j.resetJoueur(root);
    }
    /**
     * Gère la logique d'accélération du joueur.
     */
    public void acceleration() {
        if(tete.getScore()>=10) {
            long elapsedTime = System.currentTimeMillis() - lastUpdate;
            if(elapsedTime>=1000) {
                lastUpdate = System.currentTimeMillis();
                tete.setScore(tete.getScore()-1);
                NourritureSlither.creerNourritureCorps(root, tete.getCorps().getLast().getCenterX(), tete.getCorps().getLast().getCenterY(), 1, nourritures, tete.getRadius());
                if(tete.getScore()%2==0) {
                    tete.removeLastCorps(root);
                }
                if(tete.getScore()%100==0) {
                    tete.reduireTaille();
                }
            }
        } else {
            tete.setVitesse(tete.getVitesseNormal());
            tete.changementCouleur(false);
        }
    }
}

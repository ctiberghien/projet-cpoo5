package com.ProjetPoo.Apps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.SwingUtilities;

import com.ProjetPoo.Model.AiSlither;
import com.ProjetPoo.ModelSlither.CorpsSlither;
import com.ProjetPoo.ModelSlither.NourritureSlither;
import com.ProjetPoo.ModelSlither.PointSerialisable;
import com.ProjetPoo.ModelSlither.SlitherData;
import com.ProjetPoo.ModelSlither.TeteSlither;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
- connexion ; Ok

- deplacement d'un joueur Ok
- envoi du nouveau pointeur de souris à tous les joueurs Ok

- generation d'une nourriture par le serveur Ok
- envoi des données de la nourriture à tous les joueurs Ok

- un joueur mange une nourriture Ok
- pooisé? Ok

- un joueur tue un autre joueur Ok
- deconnexion du joueur mort Ok
- envoi la position du cadavre du joueur mort a tous les joueurs
 */

public class OnlineSlither extends Slither {

    private Socket socket;
    private ObjectOutputStream out;
    private Point2D pointeur;
    private ObjectInputStream in;
    private NourritureSlither foodAccelere;
    private boolean isAccelerating =false;

    public OnlineSlither(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("erreur de connexion au serveur");
            System.exit(0);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        super.start(primaryStage);
        OnlineThread o = new OnlineThread();
        o.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public boolean doesPlayerExists(long newPlayerId) {
        for(TeteSlither tete : joueurs) {
            if (tete.getStartTime()==newPlayerId) return true;
        }
        return false;
    }

    public void setMainFood(LinkedList<PointSerialisable> foodCoord) {
        for (PointSerialisable ps : foodCoord) {
            NourritureSlither newNourriture = new NourritureSlither(ps.getX(), ps.getY());
            nourritures.add(newNourriture);
            root.getChildren().add(newNourriture); 
        } 
    }

    public class OnlineThread extends Thread {
        SlitherData data; 

        @Override
        public void run() {
            Object obj;
            try {
                while ((obj = in.readObject()) != null) {
                    data = (SlitherData) obj;
                    if (!data.getFoodDead().isEmpty()) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                setMainFood(data.getFoodDead());
                            }
                        });
                    }
                    if (data.getFoodAccelere()!=null) {
                        NourritureSlither ns = new NourritureSlither(data.getFoodAccelere().getX(), data.getFoodAccelere().getY());
                        nourritures.add(ns);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                root.getChildren().add(ns);
                            }
                        });
                    }
                    if (!data.getFoodBase().isEmpty()) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                setMainFood(data.getFoodBase());
                            }
                        });
                    }
                    if(data.getNewFood()!=null && data.getNewFood().getX()!=0 && data.getNewFood().getY()!=0) {
                        NourritureSlither ns = new NourritureSlither(data.getNewFood().getX(), data.getNewFood().getY());
                        nourritures.add(ns);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                root.getChildren().add(ns);
                            }
                        });
                    }
                    //verif de si c'est un nouveau joueur, si oui -> creer un teteslither
                    //System.out.println("data; "+data.toString());
                    int cpt = 0;
                    for(TeteSlither tete : joueurs) {
                        cpt+=1;
                        System.out.println("joueur "+cpt+", starttime :"+tete.getStartTime());
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (!doesPlayerExists(data.getStartTime())) {
                                AiSlither nouvJoueurs = new AiSlither(data.getPos().getX(), data.getPos().getY(), 15,root);
                                for (int i = 0; i < (data.getTaille()/2)-20; i+=1) {
                                    nouvJoueurs.addCorps(root);
                                }
                                nouvJoueurs.setStartTime(data.getStartTime());
                                joueurs.add(nouvJoueurs);
                            }
                        }
                    });
                    //TODO deplacement
                    for (TeteSlither joueur : joueurs) {
                        if (joueur.getStartTime()==data.getStartTime() && data.getStartTime()!=tete.getStartTime()) {
                            if (data.getTaille()%2==0 && data.getTaille()!=joueur.getScore()) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        joueur.addCorps(root);
                                    }
                                });
                            }
                            joueur.setScore(data.getTaille());
                            if (data.isAccelerating()) {
                                joueur.setVitesse(tete.getVitesseAccelere());
                            } else {
                                joueur.setVitesse(tete.getVitesseNormal());
                            }
                            if (data.getPos().getX()==-1) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        System.out.println("joueur mort");
                                        removePlayer(joueur);
                                        joueurs.remove(joueur);
                                    }
                                });
                            } else {
                                Point2D posPoint2d = new Point2D(data.getPos().getX(), data.getPos().getY());
                                Point2D ptrPoint2d = new Point2D(data.getPointeur().getX(), data.getPointeur().getY()); 
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        joueur.deplacerVersSouris(posPoint2d, ptrPoint2d);
                                    }
                                });
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setTerrain() {
        root = new Pane();
        cam = new Pane();
        root.setPrefSize(1280,720);
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
    }

    @Override
    public void animation(Stage primaryStage) {
        final Duration oneFrameAmt = Duration.millis(1000 / 30);
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
            new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    // System.out.println("x : "+souris.getX()+"\ny : "+souris.getY());
                    NourritureSlither ns = tete.hasEaten(nourritures, root);
                    for (TeteSlither ts : joueurs) {
                        if (ts!=tete) {
                            ts.hasEaten(nourritures, root);
                        }
                    }
                    if (ns!=null) {
                        try {
                            SlitherData sd = new SlitherData(tete.getStartTime(), souris, tete.getCoordonnee(), tete.getScore());
                            sd.setFoodEaten(new PointSerialisable(ns.getCoordonnee()));
                            if (foodAccelere!=null) {
                                sd.setFoodAccelere(new PointSerialisable(foodAccelere.getCoordonnee()));
                                foodAccelere=null;
                            }
                            sd.setAccelerating(isAccelerating);
                            out.writeObject(sd);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        try {
                            SlitherData sd = new SlitherData(tete.getStartTime(), souris, tete.getCoordonnee(), tete.getScore());
                            if (foodAccelere!=null) {
                                sd.setFoodAccelere(new PointSerialisable(foodAccelere.getCoordonnee()));
                                foodAccelere=null;
                            }
                            sd.setAccelerating(isAccelerating);
                            out.writeObject(sd);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
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
                                disconnect();
                                System.exit(0);
                            }
                        }  
                    }
                    if(tete.getCoordonneeX()>root.getWidth() || tete.getCoordonneeX()<0 
                    || tete.getCoordonneeY()>root.getHeight() || tete.getCoordonneeY()<0) {
                        mort(tete);
                        disconnect();
                        System.exit(0);
                    }
                    
                    event.consume();
                }
            });

        tm = new Timeline(30,oneFrame);
        tm.setCycleCount(Animation.INDEFINITE);
        SwingUtilities.invokeLater(() -> {
            tm.play();
        });
    }

    @Override
    public void mort(TeteSlither j) {
        int scoreParCorps = j.getScore()/j.getCorps().size();
        LinkedList<PointSerialisable> cadavreAEnvoyer = new LinkedList<>();
        cadavreAEnvoyer.addAll(emplacementsNourriture(j.getCenterX(), j.getCenterY(), scoreParCorps, j.getRadius()));
        for(CorpsSlither c : j.getCorps()) {
            cadavreAEnvoyer.addAll(emplacementsNourriture(c.getCenterX(), c.getCenterY(), scoreParCorps, j.getRadius()));
        }
        try {
            SlitherData sd = new SlitherData(j.getStartTime(), souris, j.getCoordonnee(), tete.getScore()/2);
            sd.setFoodDead(cadavreAEnvoyer);
            out.writeObject(sd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LinkedList<PointSerialisable> emplacementsNourriture(double x, double y, int score, double radius) {
        LinkedList<PointSerialisable> res = new LinkedList<>();
        Random rand = new Random();
        for(int i=0; i<score/2; i++) {
            double randX = rand.nextDouble(-(radius/2),radius/2);
            double randY = rand.nextDouble(-(radius/2),radius/2);
            res.add(new PointSerialisable(new Point2D(x+randX, y+randY)));
        }
        return res;
    }

    public void disconnect() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {}
    }

    public void removePlayer(TeteSlither joueur) {
        for (CorpsSlither c : joueur.getCorps()) {
            root.getChildren().remove(c);
        }
        root.getChildren().remove(joueur);
    }

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
                isAccelerating = true;
                tete.setVitesse(tete.getVitesseAccelere());
                tete.changementCouleur(true);
                acceleration();
            }
        });
        scene.setOnMouseReleased(e -> {
            if(e.getButton() == MouseButton.SECONDARY) {
                isAccelerating = false;
                tete.setVitesse(tete.getVitesseNormal());
                tete.changementCouleur(false);
            }
        });
    }

    @Override
    public void acceleration() {
        if(tete.getScore()>=10) {
            long elapsedTime = System.currentTimeMillis() - lastUpdate;
            if(elapsedTime>=1000) {
                lastUpdate = System.currentTimeMillis();
                tete.setScore(tete.getScore()-1);
                NourritureSlither aEnvoyer = NourritureSlither.creerNourritureAcceleration(root, tete.getCorps().getLast().getCenterX(), tete.getCorps().getLast().getCenterY(), nourritures, tete.getRadius());
                foodAccelere=aEnvoyer;
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
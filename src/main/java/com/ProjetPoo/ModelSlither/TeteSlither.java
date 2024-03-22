package com.ProjetPoo.ModelSlither;

import java.util.ArrayList;
import java.util.Random;
import com.ProjetPoo.Model.Tete;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class TeteSlither extends CorpsSlither implements Tete{
    private int score;
    private Point2D direction;
    private ArrayList<CorpsSlither> corps;
    private int vitesse;
    private int vitesseAccelere;
    private int vitesseNormal;
    private Color couleur;
    private long lastUpdate = System.currentTimeMillis();
    //id utilisé pour le mode online
    private long startTime;

    
    public TeteSlither(double x, double y, double taille, Pane root) {
        super(x, y, 15);
        startTime=System.currentTimeMillis();
        direction = new Point2D(0, 0);
        corps = new ArrayList<>();
        corps.add(this);
        Random rand = new Random();
        this.couleur = Color.rgb(
                rand.nextInt(256),
                rand.nextInt(256),
                rand.nextInt(256)
        );
        this.setFill(couleur);
        for (int i = 0; i < 9; i++) {
            this.addCorps(root);
        }
        vitesse=6;
        vitesseAccelere=10;
        vitesseNormal=vitesse;
        score = 20;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long playerId) {
        this.startTime = playerId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setDirectionX(double directionX) {
        this.direction = new Point2D(directionX, this.direction.getY());
    }
    public void setDirectionY(double directionY) {
        this.direction = new Point2D(this.direction.getX(), directionY);
    }
    public double getDirectionX() {
        return direction.getX();
    }
    public double getDirectionY() {
        return direction.getY();
    }
    public Point2D getDirection() {
        return direction;
    }
    public int getVitesse() {
        return vitesse;
    }
    public void setVitesse(int vitesse) {
        this.vitesse = vitesse;
    }
    public int getVitesseAccelere() {
        return vitesseAccelere;
    }
    public int getVitesseNormal() {
        return vitesseNormal;
    }
    public ArrayList<CorpsSlither> getCorps() {
        return corps;
    }
    

    public void augmentTaille() {
        this.setRadius(this.getRadius()+1);
        for(CorpsSlither c : this.corps) {
            c.setRadius(this.getRadius()+1);
        }
    }

    public void reduireTaille() {
        this.setRadius(this.getRadius()-1);
        for(CorpsSlither c : this.corps) {
            c.setRadius(this.getRadius()-1);
        }
    }

    public void resetJoueur(Pane root) {
        root.getChildren().remove(this);
        root.getChildren().removeAll(corps);
        this.setRadius(15);
        Random rand = new Random();
        int randX = rand.nextInt((int)root.getPrefWidth());
        int randY = rand.nextInt((int)root.getPrefHeight()); 
        this.setCoordonneeX(randX);
        this.setCoordonneeY(randY);
        this.setCenterX(randX);
        this.setCenterY(randY);
        direction = new Point2D(0, 0);
        corps = new ArrayList<>();
        corps.add(this);
        for (int i = 0; i < 9; i++) {
            this.addCorps(root);
        }
        vitesse=6;
        vitesseAccelere=10;
        vitesseNormal=vitesse;
        score=10;
    }

    public void addCorps(Pane p) {
        CorpsSlither corpsSlither = new CorpsSlither(corps.get(corps.size()-1).getCoordonneeX(), corps.get(corps.size()-1).getCoordonneeY(), this.getRadius());
        corpsSlither.setFill(couleur);
        this.corps.add(corpsSlither);
        p.getChildren().addAll(corps.get(corps.size()-1));
           
    }

    public void removeLastCorps(Pane p) {
        CorpsSlither c = this.corps.getLast();
        p.getChildren().remove(c);
        this.corps.removeLast();
    }

    public void removeCorps(Pane p) {
        for (CorpsSlither c : corps) {
            p.getChildren().remove(c);
        }
        corps.clear();
    }

    public void deplacerCorps(Point2D oldPos) {
        Point2D old = oldPos;
        for(int i=1; i<corps.size(); i++) {
            CorpsSlither current = corps.get(i);
            Point2D tmp = current.getCoordonnee();
            if(i==corps.size()-1 && current.getCoordonnee().equals(old)) {
                break;
            }
            current.setCoordonnee(old);
            current.setCenterX(current.getCoordonneeX());
            current.setCenterY(current.getCoordonneeY());
            old = tmp;
        }
    }

    public void deplacerVersSouris(Point2D milieu, Point2D souris) {
        Point2D vecteur = new Point2D(milieu.getX() - souris.getX(), milieu.getY() - souris.getY());
        double norme  = Math.sqrt(vecteur.getX()*vecteur.getX()+vecteur.getY()*vecteur.getY());
        Point2D vNorm = new Point2D(-(vecteur.getX()/norme), -(vecteur.getY()/norme));
        Point2D newPoint = new Point2D(milieu.getX()+vitesse*vNorm.getX(), milieu.getY()+vitesse*vNorm.getY());
        Point2D vecteurDirection = new Point2D(newPoint.getX()-milieu.getX(), newPoint.getY()-milieu.getY());
        Point2D oldPos = this.getCoordonnee();

        //verif courbure
        if(!direction.equals(new Point2D(0, 0))) {
            double angleR = Math.atan2(vecteurDirection.getY(),vecteurDirection.getX()) - Math.atan2(direction.getY(), direction.getX());
            if (angleR < 0) {
                angleR += 2 * Math.PI;
            }
            double angleDegree = Math.toDegrees(angleR);
            //System.out.println("degree : " + angleDegree);
            if((angleDegree>15 && angleDegree<180) || (angleDegree<345 && angleDegree>180)) {
                double courbure = angleDegree<180 ? Math.toRadians(15) : Math.toRadians(345);
                vecteurDirection = new Point2D(direction.getX() * Math.cos(courbure) - direction.getY() * Math.sin(courbure) ,
                    direction.getX() * Math.sin(courbure) + direction.getY() * Math.cos(courbure)); 
            }
        }

        this.setCoordonnee(new Point2D(vecteurDirection.getX()+this.getCoordonneeX(), vecteurDirection.getY()+this.getCoordonneeY()));
        this.setCenterX(this.getCoordonneeX());
        this.setCenterY(this.getCoordonneeY());
        deplacerCorps(oldPos);
        //System.out.println("x : "+this.getCoordonneeX()+"\ny : "+this.getCoordonneeY());
        direction = vecteurDirection;
    }
    
    public boolean hasCollide(TeteSlither joueur1) {
        for (CorpsSlither c : joueur1.corps) {
            if (this.getCoordonneeX()>c.getCoordonneeX()-c.getRadius() 
            && this.getCoordonneeX()<c.getCoordonneeX()+c.getRadius()
            && this.getCoordonneeY()>c.getCoordonneeY()-c.getRadius()
            && this.getCoordonneeY()<c.getCoordonneeY()+c.getRadius()) {
                return true;
            }
        }
        return false;
    }

    public void changementCouleur(boolean change) {
        if(change) {
            this.setStroke(Color.DARKBLUE);
            for(CorpsSlither c : corps) {
                c.setStroke(Color.DARKBLUE);
            }
        } else {
            this.setStroke(couleur);
            for(CorpsSlither c : corps) {
                c.setStroke(couleur);
            }
        }
    }

    public NourritureSlither hasEaten(ArrayList<NourritureSlither> nourritures, Pane root) {
        for (NourritureSlither c : nourritures) {
            if (this.getCoordonneeX()>c.getCoordonneeX()-this.getRadius() 
            && this.getCoordonneeX()<c.getCoordonneeX()+this.getRadius()
            && this.getCoordonneeY()>c.getCoordonneeY()-this.getRadius()
            && this.getCoordonneeY()<c.getCoordonneeY()+this.getRadius()) {
                nourritures.remove(c);
                root.getChildren().remove(c);
                this.setScore(this.getScore()+1);
                if(this.getScore()%2==0) {
                    this.addCorps(root);
                }
                if(this.getScore()%100==0) {
                    this.augmentTaille();
                }
                /*zoomLevel-=0.1;
                Duration duration = Duration.seconds(1);
                ScaleTransition scaleTransition = new ScaleTransition(duration, root);
                scaleTransition.setFromX(zoomLevel);
                scaleTransition.setFromY(zoomLevel); 
                scaleTransition.play();*/
                return c;
            }
        }
        return null;
    }
}

package com.ProjetPoo.Model;

import java.util.ArrayList;
import java.util.Random;

import com.ProjetPoo.ModelSlither.NourritureSlither;
import com.ProjetPoo.ModelSlither.TeteSlither;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class AiSlither extends TeteSlither{
    private boolean isEating;
    private Point2D currentAiDirection;
    static Random rand = new Random();
    private long lastUpdate = System.currentTimeMillis();

   public AiSlither(double x, double y, int taille, Pane root) {
        super(x, y, taille, root);
        currentAiDirection = new Point2D(rand.nextInt(5120), rand.nextInt(2880));
    }


    public boolean deplacement(ArrayList<NourritureSlither> nourritures, Pane root, ArrayList<TeteSlither> joueurs) {
        if (hasEaten(nourritures, root)!=null) {
            isEating=true;
        }
        Point2D newSourisBot = eating(nourritures);
        int value = nearBorder(root.getWidth(), root.getHeight());
        if (value != 0) {
            newSourisBot = escapeBorder(value);
            //System.out.println("escaping:"+newSourisBot.toString());
            this.deplacerVersSouris(new Point2D(this.getCoordonneeX(), this.getCoordonneeY()), newSourisBot);
        } else {
            long elapsedTime = System.currentTimeMillis() - lastUpdate;
            if (newSourisBot!=null) {
                // Si l'ia a mangé et qu'il y a une nourriture proche, on va vers cette nourriture
                isEating=false;
                lastUpdate = System.currentTimeMillis();
                this.deplacerVersSouris(new Point2D(this.getCoordonneeX(), this.getCoordonneeY()), newSourisBot);
            } else if (elapsedTime>=2000 && newSourisBot==null) {
                // Si l'ia n'a pas mangé et qu'il n'y a pas de nourriture proche, on va dans une direction aléatoire toute les seconde
                lastUpdate = System.currentTimeMillis();
                currentAiDirection = new Point2D(rand.nextInt(5120), rand.nextInt(2880));
                this.deplacerVersSouris(new Point2D(rand.nextInt(5120), rand.nextInt(2880)),currentAiDirection);;
            } else {
                // Sinon on continue vers la direction de base
                this.deplacerVersSouris(new Point2D(this.getCoordonneeX(), this.getCoordonneeY()), currentAiDirection);
                Point2D vecteur = new Point2D(currentAiDirection.getX() - this.getCoordonneeX(), currentAiDirection.getY() - this.getCoordonneeY());
                double norme  = Math.sqrt(vecteur.getX()*vecteur.getX()+vecteur.getY()*vecteur.getY());
                if(norme<100) {
                    currentAiDirection = new Point2D(rand.nextInt(5120), rand.nextInt(2880));
                }
            }
            for (TeteSlither j : joueurs) {   
                if(!this.equals(j)) {
                    if(this.hasCollide(j)) {
                        return true;
                    }
                }  
            }
        }
        return false;
    }

    public Point2D eating(ArrayList<NourritureSlither> sortedByX) {
        ArrayList<NourritureSlither> visibleFood = new ArrayList<>();
        for (NourritureSlither n : sortedByX) {
            if (n.getCoordonneeY() < this.getCoordonneeY()+360 && n.getCoordonneeY()>this.getCoordonneeY()-360
            && n.getCoordonneeX() < this.getCoordonneeX()+640 && n.getCoordonneeX()>this.getCoordonneeX()-640) {
                visibleFood.add(n);
            }
        }
        if (visibleFood.isEmpty()) return null;
        Point2D vecteurMin = new Point2D(visibleFood.getFirst().getCoordonneeX() - this.getCoordonneeX(), visibleFood.getFirst().getCoordonneeY() - this.getCoordonneeY());
        double min  = Math.sqrt(vecteurMin.getX()*vecteurMin.getX()+vecteurMin.getY()*vecteurMin.getY());
        int minCpt = 0;
        int cpt = -1;
        for (NourritureSlither n : visibleFood) {
            Point2D vecteur = new Point2D(n.getCoordonneeX() - this.getCoordonneeX(), n.getCoordonneeY() - this.getCoordonneeY());
            double norme  = Math.sqrt(vecteur.getX()*vecteur.getX()+vecteur.getY()*vecteur.getY());
            cpt+=1;
            if (norme<min) {
                min=norme;
                minCpt=cpt;
            }
        }
        return new Point2D(visibleFood.get(minCpt).getCoordonneeX(), visibleFood.get(minCpt).getCoordonneeY());
    }

    public int nearBorder(double sizeX, double sizeY) {
        if (this.getCoordonneeX()<=70) {
            return 1;
        } else if (this.getCoordonneeX()>=(sizeX-70)) {
            return 2;
        } else if (this.getCoordonneeY()<=70) {
            return 3;
        } else if (this.getCoordonneeY()>=(sizeY-70)) {
            return 4;
        }
        return 0;
    }

    public Point2D escapeBorder(int borderNumber) {
        switch (borderNumber) {
            case 1:
                return new Point2D(this.getCoordonneeX()+500, this.getCoordonneeY());
            case 2:
                return new Point2D(this.getCoordonneeX()-500, this.getCoordonneeY());
            case 3:
                return new Point2D(this.getCoordonneeX(), this.getCoordonneeY()+250);
            case 4:
                return new Point2D(this.getCoordonneeX(), this.getCoordonneeY()-250);
            default:
                return null;
        }
    }

}

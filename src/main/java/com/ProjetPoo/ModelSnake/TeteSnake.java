package com.ProjetPoo.ModelSnake;

import java.util.ArrayList;
import java.util.Random;

import com.ProjetPoo.Model.Tete;
import com.ProjetPoo.ModelSlither.CorpsSlither;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

public class TeteSnake extends CorpsSnake implements Tete{
    private int longueur;
    private ArrayList<CorpsSnake> corps;
    private int direction;

    public TeteSnake(double cooX, double cooY, double width) {
        super(cooX, cooY, width);
        longueur = 1;
        corps = new ArrayList<>(); 
        corps.add(this);
        Random rand = new Random();
       // direction = rand.nextInt(4);
        direction = 0;
    }

    @Override
    public void addCorps(Pane p) {
        longueur+=1;
        CorpsSnake c = new CorpsSnake(corps.getLast().getCoordonnee().getX()*getWidth(),corps.getLast().getCoordonnee().getY()*getWidth(), getWidth());
        corps.add(c);
        System.out.println(c.getX() + " " + c.getY());
        p.getChildren().add(c);
    }

    @Override
    public void deplacerCorps(Point2D oldPos) {
        Point2D old = oldPos;
        for(int i=1; i<corps.size(); i++) {
            CorpsSnake current = corps.get(i);
            Point2D tmp = current.getCoordonnee();
            if(i==corps.size()-1 && current.getCoordonnee().equals(old)) {
                break;
            }
            current.setCoordonnee(old);
            current.setX(current.getCoordonnee().getX()*getWidth());
            current.setY(current.getCoordonnee().getY()*getWidth());
            old = tmp;
        }
    }

    public boolean collision() {
        for(CorpsSnake c : corps) {
            if(!c.equals(this)) {
                if(this.getCoordonnee().equals(c.getCoordonnee())) return true;
            }
        }
        return false;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
    
}

package com.ProjetPoo.ModelSlither;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

public class CorpsSlither extends Circle{
    private Point2D coordonnee;

    public CorpsSlither(double x, double y, double taille) {
        super(x, y, taille);
        coordonnee = new Point2D(x, y);
    }

    public void setCoordonneeX(double coordonneeX) {
        this.coordonnee = new Point2D(coordonneeX, this.getCoordonneeY());
    }
    public void setCoordonneeY(double coordonneeY) {
        this.coordonnee = new Point2D(this.getCoordonneeX(), coordonneeY);
    }
    public void setCoordonnee(Point2D coordonnee) {
        this.coordonnee = coordonnee;
    }
    public double getCoordonneeX() {
        return coordonnee.getX();
    }
    public double getCoordonneeY() {
        return coordonnee.getY();
    }
    public Point2D getCoordonnee() {
        return coordonnee;
    }
}

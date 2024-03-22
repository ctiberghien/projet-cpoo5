package com.ProjetPoo.ModelSnake;


import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

public class CorpsSnake extends Rectangle {
    private Point2D coordonnee;

    public CorpsSnake(double cooX, double cooY, double width) {
        super(cooX, cooY, width, width);
        coordonnee = new Point2D(cooX/width, cooY/width);
    }

    public Point2D getCoordonnee() {
        return new Point2D(coordonnee.getX(), coordonnee.getY());
    }
    public void setCoordonnee(Point2D coordonnee) {
        this.coordonnee = coordonnee;
    }
}

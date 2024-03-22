package com.ProjetPoo.ModelSlither;

import java.io.Serializable;

import javafx.geometry.Point2D;


    /**
 * PointSerialisable
 */
public class PointSerialisable implements Serializable{
    private double x;
    private double y;

    public PointSerialisable(Point2D p) {
        if (p!=null) {
            x=p.getX();
            y=p.getY();
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "PointSerialisable [x=" + x + ", y=" + y + "]";
    }

    @Override
        public boolean equals(Object obj) {
            return (x==((PointSerialisable) obj).x && y==((PointSerialisable) obj).y);
        }
}
  


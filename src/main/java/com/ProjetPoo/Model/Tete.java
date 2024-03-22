package com.ProjetPoo.Model;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

public interface Tete {
    void addCorps(Pane p);
    void deplacerCorps(Point2D oldPos);
}

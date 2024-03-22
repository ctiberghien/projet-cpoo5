package com.ProjetPoo.Model;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public interface AppJeu{
    public abstract void setTerrain(); 
    public abstract void eventTouches(Scene scene);
}

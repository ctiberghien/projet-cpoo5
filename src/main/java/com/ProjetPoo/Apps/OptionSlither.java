package com.ProjetPoo.Apps;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class OptionSlither extends Application{
    private boolean slither;

    public void setSlither(boolean slither) {
        this.slither = slither;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Label titre = new Label("Option Slither");
            titre.setFont(new Font(20));
            Label valueLabel = new Label("Nombre d'ia : ");
            Slider slider = new Slider(1, 10, 1);
            slider.valueProperty().addListener((observable, oldValue, newValue) -> {
                valueLabel.setText("Nombre d'ia : " + newValue.intValue());
            });

            Button start = new Button("Start");
            start.setOnAction(e -> {
                if(slither) {
                    Slither sl = new Slither();
                    sl.nbIa = (int)slider.getValue();
                    sl.start(primaryStage);
                } else {
                    Slither2 sl = new Slither2();
                    sl.nbIa = (int)slider.getValue();
                    sl.start(primaryStage);
                }
            });

            VBox root = new VBox(10);
            root.setAlignment(Pos.CENTER);
            root.getChildren().addAll(titre, slider, valueLabel, start);
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {}
    }
}

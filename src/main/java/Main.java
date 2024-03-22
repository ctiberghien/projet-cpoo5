
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.ProjetPoo.Apps.OnlineSlither;
import com.ProjetPoo.Apps.OptionSlither;
import com.ProjetPoo.Apps.Snake;
import com.ProjetPoo.Apps.Snake2;
import com.ProjetPoo.Model.Serveur;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class Main extends Application {
    protected Pane root;
    protected Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Button sliButton = new Button();
        Button sli2Button = new Button();
        Button snakeButton = new Button();
        Button snake2Button = new Button();
        Button onlineButton = new Button();
        Button quitterButton = new Button();
        sliButton.getStyleClass().add("slibutton");
        sli2Button.getStyleClass().add("sli2button");
        snakeButton.getStyleClass().add("snakebutton");
        snake2Button.getStyleClass().add("snake2button");
        onlineButton.getStyleClass().add("onlinebutton");
        quitterButton.getStyleClass().add("quitterbutton");
        
        class OnlineMenu extends Application {

            private boolean hostIsChecked = false;

            @Override
            public void start(Stage primaryStage) {
                primaryStage.setTitle("Menu Online");
                CheckBox hostCheckBox = new CheckBox("Host");
                TextField ipTextField = new TextField();
                TextField portTextField = new TextField();
                Button validerButton = new Button("Valider");
                validerButton.getStyleClass().add("valider-button");
                validerButton.setOnAction(e -> {
                    String ip = ipTextField.getText();
                    int port = Integer.parseInt(portTextField.getText());
                    if (hostIsChecked) {
                        Serveur s = new Serveur(port);
                        s.start();
                        primaryStage.close();
                    } else {
                        OnlineSlither os = new OnlineSlither(ip, port);
                        os.start(primaryStage);
                    }
                });
                GridPane gridPane = new GridPane();
                gridPane.setPadding(new Insets(250));
                gridPane.setHgap(10);
                gridPane.setVgap(10);
                gridPane.add(hostCheckBox, 0, 0);
                gridPane.add(new Label("IP:"), 0, 1);
                gridPane.add(ipTextField, 1, 1);
                gridPane.add(new Label("Port:"), 0, 2);
                gridPane.add(portTextField, 1, 2);
                gridPane.add(validerButton, 0, 3);
                hostCheckBox.setOnAction(e -> {
                    if (hostCheckBox.isSelected()) {
                        hostIsChecked=true;
                        gridPane.getChildren().remove(ipTextField);
                    } else {
                        hostIsChecked=false;
                        gridPane.add(ipTextField, 1, 1);
                    }
                });
                Scene scene = new Scene(gridPane, 800, 600);
                scene.getStylesheets().add(getClass().getResource("menuonlinestyle.css").toExternalForm());
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        }
        // Ajout des styles aux boutons
        
        sliButton.setOnAction(e -> {
            OptionSlither o = new OptionSlither();
            o.setSlither(true);
            o.start(primaryStage);
        });
        sli2Button.setOnAction(e -> {
            OptionSlither o = new OptionSlither();
            o.setSlither(false);
            o.start(primaryStage);
        });
        snakeButton.setOnAction(e -> {
            Snake sl = new Snake();
            sl.start(primaryStage);
        });
        snake2Button.setOnAction(e -> {
            Snake2 sl = new Snake2();
            sl.start(primaryStage);
        });
        onlineButton.setOnAction(e -> {
            OnlineMenu om = new OnlineMenu();
            om.start(primaryStage);
        });
        quitterButton.setOnAction(e -> primaryStage.close());
        sliButton.setText("slither");
        sli2Button.setText("slither2");
        snakeButton.setText("snake");
        snake2Button.setText("snake2");
        onlineButton.setText("onlineslither");
        quitterButton.setText("quitter");
        // Création de la mise en page
        VBox layout = new VBox(30);
        layout.setPadding(new Insets(30));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(onlineButton, sliButton, sli2Button, snake2Button, snakeButton, quitterButton);

        // Affichage de la scène
        Scene scene = new Scene(layout, 800, 600);
        scene.getStylesheets().add(getClass().getResource("menustyle.css").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }
}
    

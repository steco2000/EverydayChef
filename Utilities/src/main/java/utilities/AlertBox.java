package utilities;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

//pop up di avviso per l'utente. Dispone di una label e di un semplice pulsante di ok

public class AlertBox {

    private AlertBox(){
    }

    //display del pop up. Accetta titolo e messaggio da visualizzare
    public static void display(String title, String message){
        Stage box = new Stage();
        box.initModality(Modality.APPLICATION_MODAL);
        box.setTitle(title);
        box.setResizable(false);
        box.setMinWidth(450);
        box.setMinHeight(250);

        Label label = new Label();
        label.setText(message);
        label.setTextAlignment(TextAlignment.CENTER);

        Button closeButton = new Button("Ok");
        closeButton.setOnAction(e -> box.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        box.setScene(scene);

        box.showAndWait();
    }

}

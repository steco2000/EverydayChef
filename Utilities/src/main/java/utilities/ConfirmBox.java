package utilities;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static javafx.geometry.Pos.CENTER;

public class ConfirmBox {

    static boolean answer;

    private ConfirmBox(){
    }

    public static boolean display(String title, String message){
        Stage box = new Stage();
        box.initModality(Modality.APPLICATION_MODAL);
        box.setTitle(title);
        box.setResizable(false);
        box.setMinWidth(450);
        box.setMinHeight(250);

        Label label = new Label();
        label.setText(message);
        label.setTextAlignment(TextAlignment.CENTER);

        Button yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
            answer = true;
            box.close();
        });

        Button noButton = new Button("No");
        noButton.setOnAction(e -> {
            answer = false;
            box.close();
        });

        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(yesButton,noButton);
        hbox.setAlignment(CENTER);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label,hbox);
        layout.setAlignment(CENTER);

        Scene scene = new Scene(layout);
        box.setScene(scene);

        box.showAndWait();

        return answer;
    }

}
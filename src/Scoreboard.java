import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import java.util.List;
import java.util.Map;

public class Scoreboard extends VBox {

    private VBox rows = new VBox(6);

    public Scoreboard() {
        setStyle("-fx-background-color: #1e1e1e; -fx-border-color: white; -fx-border-width: 2;");
        setPadding(new Insets(10));
        setSpacing(10);

        Label title = new Label("SCOREBOARD");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        title.setAlignment(Pos.CENTER);

        getChildren().addAll(title, rows);
    }

    public void update(Map<Player, Score> scores) {
        rows.getChildren().clear();

        for (Player p : scores.keySet()) {
            Score s = scores.get(p);

            HBox row = new HBox(6);
            row.setAlignment(Pos.CENTER_LEFT);

            Label name = new Label(p.getName());
            name.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            name.setMinWidth(120);

            // Frame-by-frame boxes
            HBox frames = new HBox(3);
            for (int i = 0; i < 10; i++) {
                String boxText = s.frameToString(i);
                Label box = new Label(boxText);
                box.setStyle("-fx-text-fill: yellow; -fx-font-size: 14px; -fx-border-color: gray; -fx-border-width: 1;");
                box.setPrefWidth(30);
                box.setAlignment(Pos.CENTER);
                frames.getChildren().add(box);
            }

            Label total = new Label("" + s.getScore());
            total.setStyle("-fx-text-fill: #00ff88; -fx-font-size: 18px; -fx-font-weight: bold;");
            total.setMinWidth(50);

            row.getChildren().addAll(name, frames, total);
            rows.getChildren().add(row);
        }
    }
}

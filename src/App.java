import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.*;

public class App extends Application {
    private List<Team> teams;
    private Map<Player, Score> playerScores;
    private int currentTeamIdx = 0;
    private int currentPlayerIdx = 0;
    private RackOfPins rack;
    private Random rand = new Random();

    private Label statusLabel = new Label();
    private Label nowLabel = new Label();
    private Label winnerLabel = new Label();
    private VBox scorePane = new VBox(8);
    private Button rollBtn;

    @Override
    public void start(Stage stage) {
        teams = Arrays.asList(
                new Team("Pump Theorists", Arrays.asList(new Player("Branson", null), new Player("Jordan", null))),
                new Team("Class Skippers", Arrays.asList(new Player("Hamza", null), new Player("Gage", null))));
        playerScores = new HashMap<>();
        for (Team t : teams)
            for (Player p : t.getPlayers())
                playerScores.put(p, new Score());

        rack = new RackOfPins();
        Node rackView = rack.getView();

        rollBtn = new Button("Roll");
        rollBtn.setOnAction(e -> nextRoll());

        statusLabel.setWrapText(true);
        statusLabel.setMaxWidth(600);

        nowLabel.setStyle("-fx-font-size:20px;");
        winnerLabel.setStyle("-fx-font-size:20px;");
        winnerLabel.setVisible(false);

        VBox center = new VBox(12, winnerLabel, nowLabel, rackView, rollBtn, statusLabel);
        center.setAlignment(Pos.CENTER);
        center.setStyle("-fx-padding:20px;");

        scorePane.setStyle("-fx-padding:10px; -fx-background-color:#f7f7f7;");
        scorePane.setPrefWidth(260);

        BorderPane root = new BorderPane();
        root.setCenter(center);
        root.setRight(scorePane);

        updateGui();

        Scene scene = new Scene(root, 1000, 650);
        stage.setScene(scene);
        stage.setTitle("Bowling League Game");
        stage.show();
    }

    private void nextRoll() {
        winnerLabel.setVisible(false);

        Player current = getCurrentPlayer();
        int standing = rack.getStandingCount();
        int knocked = rand.nextInt(standing + 1);
        rack.knockDownRandomPins(knocked);

        Score s = playerScores.get(current);
        s.roll(knocked);

        String msg = s.getRollMessage(current.getName(), knocked);
        statusLabel.setText(msg);

        if (s.isCurrentFrameComplete()) {
            rack.resetRack();
            advanceToNextPlayer();
        }

        updateGui();

        if (gameFinished())
            finishGame();
    }

    private boolean gameFinished() {
        for (Team t : teams)
            for (Player p : t.getPlayers())
                if (playerScores.get(p).getCompletedFramesCount() < 10)
                    return false;
        return true;
    }

    private void finishGame() {
        Team winner = null;
        int best = Integer.MIN_VALUE;
        for (Team t : teams) {
            int sum = t.getPlayers().stream().mapToInt(p -> playerScores.get(p).getScore()).sum();
            if (sum > best) {
                best = sum;
                winner = t;
            }
        }
        String endMsg = "Game over — " + (winner != null ? winner.getName() : "Tie") + " wins (" + best + " pts)";
        statusLabel.setText(endMsg);
        winnerLabel.setText("🏆 " + (winner != null ? winner.getName() : "Tie") + " — " + best + " pts");
        winnerLabel.setVisible(true);
        rollBtn.setDisable(true);
    }

    private void advanceToNextPlayer() {
        currentPlayerIdx++;
        if (currentPlayerIdx >= teams.get(currentTeamIdx).getPlayers().size()) {
            currentPlayerIdx = 0;
            currentTeamIdx = (currentTeamIdx + 1) % teams.size();
        }
    }

    private Player getCurrentPlayer() {
        return teams.get(currentTeamIdx).getPlayers().get(currentPlayerIdx);
    }

    private void updateGui() {
        scorePane.getChildren().clear();
        scorePane.getChildren().add(new Label("SCORES"));

        for (Team t : teams) {
            int teamTotal = 0;
            Label tLabel = new Label(t.getName());
            scorePane.getChildren().add(tLabel);
            for (Player p : t.getPlayers()) {
                int sc = playerScores.get(p).getScore();
                teamTotal += sc;
                String now = (p == getCurrentPlayer()) ? " (Now)" : "";
                scorePane.getChildren().add(new Label("  " + p.getName() + now + ": " + sc));
            }
            scorePane.getChildren().add(new Label("  Team total: " + teamTotal));
        }

        nowLabel.setText("Now bowling: " + getCurrentPlayer().getName());
    }

    public static void main(String[] args) {
        launch();
    }
}
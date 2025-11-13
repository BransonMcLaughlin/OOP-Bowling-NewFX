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
    private VBox scorePane = new VBox(10);
    private Button rollBtn;

    @Override
    public void start(Stage stage) {
        teams = setupTeams();
        playerScores = new HashMap<>();
        for (Team t : teams)
            for (Player p : t.getPlayers())
                playerScores.put(p, new Score());

        rack = new RackOfPins();
        Node rackView = rack.getView();

        rollBtn = new Button("Roll");
        rollBtn.setStyle("-fx-font-size:42px; -fx-padding:25px 80px; -fx-background-color:#4CAF50; -fx-text-fill:white; -fx-font-weight:bold;");
        rollBtn.setOnAction(e -> nextRoll());

        statusLabel.setStyle("-fx-font-size:18px; -fx-padding:10px; -fx-text-alignment:center;");
        statusLabel.setWrapText(true);
        statusLabel.setMaxWidth(600);
        statusLabel.setAlignment(Pos.CENTER);

        nowLabel.setStyle("-fx-font-size:32px; -fx-padding:15px; -fx-font-weight:bold;");
        winnerLabel.setStyle("-fx-font-size:32px; -fx-text-fill:#2196F3; -fx-font-weight:bold; -fx-padding:25px;");
        winnerLabel.setVisible(false);

        VBox centerVBox = new VBox(25, winnerLabel, nowLabel, rackView, rollBtn, statusLabel);
        centerVBox.setAlignment(Pos.CENTER);
        centerVBox.setStyle("-fx-padding:30px;");

        scorePane.setAlignment(Pos.TOP_LEFT);
        scorePane.setStyle("-fx-font-size:18px; -fx-padding:20px; -fx-background-color:#f5f5f5; -fx-border-color:#cccccc; -fx-border-width:0 0 0 2;");
        scorePane.setPrefWidth(280);
        scorePane.setMinWidth(280);

        BorderPane root = new BorderPane();
        root.setCenter(centerVBox);
        root.setRight(scorePane);

        updateGui();

        Scene scene = new Scene(root, 1100, 750);
        stage.setScene(scene);
        stage.setTitle("Simple Bowling Game");
        stage.show();
    }

    private List<Team> setupTeams() {
        Team t1 = new Team("Eagles", Arrays.asList(new Player("Ann", null), new Player("Bob", null)));
        Team t2 = new Team("Sharks", Arrays.asList(new Player("Cara", null), new Player("Dan", null)));
        return Arrays.asList(t1, t2);
    }

    private void nextRoll() {
        winnerLabel.setVisible(false); // Hide until game ends

        Player current = getCurrentPlayer();
        int standing = rack.getStandingCount();
        int pinsThisRoll = rand.nextInt(standing + 1);
        rack.knockDownRandomPins(pinsThisRoll);
        playerScores.get(current).roll(pinsThisRoll);

        int framePins = playerScores.get(current).getPinsInCurrentFrame();

        String msg = current.getName() + " knocked down " + pinsThisRoll + " pins";
        if (framePins > pinsThisRoll) msg += " (total " + framePins + " this frame)";
        msg += ".";
        if (pinsThisRoll == 10) msg += " STRIKE!";
        else if (pinsThisRoll == 0) msg += " Gutter Ball!";

        System.out.println(msg);
        statusLabel.setText(msg);

        updateGui();

        if (frameComplete(current)) {
            rack.resetRack();
            nextPlayer();
        }

        boolean allDone = true;
        for (Team t : teams) {
            for (Player p : t.getPlayers()) {
                if (playerScores.get(p).getCompletedFramesCount() < 10) {
                    allDone = false;
                    break;
                }
            }
            if (!allDone) break;
        }
        if (allDone) {
            Team winner = null;
            int best = Integer.MIN_VALUE;
            for (Team t : teams) {
                int sum = 0;
                for (Player p : t.getPlayers()) sum += playerScores.get(p).getScore();
                if (sum > best) { best = sum; winner = t; }
            }
            String endMsg = "🏆 Game Over! Winner: " + (winner != null ? winner.getName() : "Tie") + " (" + best + " pts)";
            statusLabel.setText(endMsg);
            winnerLabel.setText("🏆 " + (winner != null ? winner.getName() : "Tie") + " wins! (" + best + " pts)");
            winnerLabel.setVisible(true);
            System.out.println(endMsg);
            rollBtn.setDisable(true);
        }
    }

    private boolean frameComplete(Player p) {
        return playerScores.get(p).isCurrentFrameComplete();
    }

    private Player getCurrentPlayer() {
        Team team = teams.get(currentTeamIdx);
        return team.getPlayers().get(currentPlayerIdx);
    }

    private void nextPlayer() {
        currentPlayerIdx++;
        if (currentPlayerIdx >= teams.get(currentTeamIdx).getPlayers().size()) {
            currentPlayerIdx = 0;
            currentTeamIdx = (currentTeamIdx + 1) % teams.size();
        }
        updateGui();
    }

    private void updateGui() {
        scorePane.getChildren().clear();

        Label scoreTitleLabel = new Label("📊 SCORES");
        scoreTitleLabel.setStyle("-fx-font-weight:bold; -fx-font-size:24px; -fx-padding:0 0 15 0;");
        scorePane.getChildren().add(scoreTitleLabel);

        for (Team t : teams) {
            int teamTotal = 0;
            Label tLabel = new Label(t.getName());
            tLabel.setStyle("-fx-font-weight:bold; -fx-font-size:22px; -fx-padding:10 0 5 0; -fx-text-fill:#2196F3;");
            scorePane.getChildren().add(tLabel);

            for (Player p : t.getPlayers()) {
                int score = playerScores.get(p).getScore();
                teamTotal += score;
                boolean isNow = (p == getCurrentPlayer());
                Label pl = new Label((isNow ? "▶ " : "   ") + p.getName() + ": " + score);
                pl.setStyle("-fx-font-size:18px; -fx-padding:3 0 3 5;" + (isNow ? " -fx-font-weight:bold; -fx-text-fill:#4CAF50;" : ""));
                scorePane.getChildren().add(pl);
            }

            Label totalLbl = new Label("Team total: " + teamTotal);
            totalLbl.setStyle("-fx-font-size:16px; -fx-font-style:italic; -fx-padding:5 0 15 5; -fx-text-fill:#666;");
            scorePane.getChildren().add(totalLbl);
        }

        nowLabel.setText("🎳 Bowling: " + getCurrentPlayer().getName());
    }

    public static void main(String[] args) {
        launch();
    }
}

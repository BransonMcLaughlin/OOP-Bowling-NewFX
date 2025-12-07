import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.*;

public class App extends Application {
    private Bracket bracket;
    private List<Team> activeTeams;
    private Map<Player, Score> playerScores;

    private int currentTeamIdx = 0;
    private int currentPlayerIdx = 0;
    private RackOfPins rack;
    private Random rand = new Random();
    private boolean roundOver = false;

    private Label statusLabel = new Label();
    private Label roundLabel = new Label();
    private Label nowLabel = new Label();
    private Label winnerLabel = new Label();
    private VBox scorePane = new VBox(8);
    private Button actionBtn;

    private Scoreboard scoreboard = new Scoreboard();

    @Override
    public void start(Stage stage) {
        List<Team> allTeams = Arrays.asList(
                new Team("Pump Theorists", Arrays.asList(new Player("Branson", null), new Player("Jordan", null))),
                new Team("Class Skippers", Arrays.asList(new Player("Hamza", null), new Player("Gage", null))),
                new Team("Hard Bowlers", Arrays.asList(new Player("Evan", null), new Player("Daniel", null))),
                new Team("Team 4", Arrays.asList(new Player("Jacob", null), new Player("Joe", null)))
        );

        bracket = new Bracket(allTeams);
        playerScores = new HashMap<>();
        rack = new RackOfPins();

        setupGui(stage);
        startNextRound();
    }

    private void setupGui(Stage stage) {
        Node rackView = rack.getView();

        actionBtn = new Button("Roll");
        actionBtn.setOnAction(e -> handleActionButton());
        actionBtn.setStyle("-fx-font-size: 14px; -fx-min-width: 100px;");

        statusLabel.setWrapText(true);
        statusLabel.setMaxWidth(600);
        statusLabel.setAlignment(Pos.CENTER);

        nowLabel.setStyle("-fx-font-size:16px; -fx-font-weight:bold;");
        winnerLabel.setStyle("-fx-font-size:24px; -fx-text-fill: green;");
        winnerLabel.setVisible(false);

        roundLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: blue;");

        VBox center = new VBox(15, roundLabel, winnerLabel, nowLabel, rackView, actionBtn, statusLabel);
        center.setAlignment(Pos.CENTER);
        center.setStyle("-fx-padding:20px;");

        scorePane.setStyle("-fx-padding:10px; -fx-background-color:#f7f7f7;");
        scorePane.setPrefWidth(300);

        ScrollPane rightScroll = new ScrollPane(scorePane);
        rightScroll.setFitToWidth(true);

        BorderPane root = new BorderPane();
        root.setCenter(center);
        root.setRight(rightScroll);

        root.setBottom(scoreboard);

        Scene scene = new Scene(root, 1100, 800);
        stage.setScene(scene);
        stage.setTitle("Bowling Tournament");
        stage.show();
    }

    private void startNextRound() {
        List<Matchup> matchups = bracket.getMatchups();
        activeTeams = new ArrayList<>();

        for (Matchup m : matchups) {
            activeTeams.add(m.getTeam1());
            if (m.getTeam2() != null) activeTeams.add(m.getTeam2());
        }

        playerScores.clear();
        for (Team t : activeTeams) {
            t.setScore(0);
            for (Player p : t.getPlayers()) {
                playerScores.put(p, new Score());
            }
        }

        currentTeamIdx = 0;
        currentPlayerIdx = 0;
        roundOver = false;
        rack.resetRack();

        roundLabel.setText("ROUND " + bracket.getRoundNumber());
        winnerLabel.setVisible(false);
        actionBtn.setText("Roll");
        actionBtn.setDisable(false);
        statusLabel.setText("Ready for Round " + bracket.getRoundNumber() + "!");

        updateGui();
        scoreboard.update(playerScores);
    }

    private void handleActionButton() {
        if (roundOver) {
            if (bracket.isTournamentOver()) {
                actionBtn.setDisable(true);
                statusLabel.setText("Tournament Complete. Restart application to play again.");
            } else {
                startNextRound();
            }
        } else {
            playNextRoll();
        }
    }

    private void playNextRoll() {
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
        scoreboard.update(playerScores);

        if (isRoundFinished()) {
            finishRound();
        }
    }

    private void advanceToNextPlayer() {
        currentPlayerIdx++;
        if (currentPlayerIdx >= activeTeams.get(currentTeamIdx).getPlayers().size()) {
            currentPlayerIdx = 0;
            currentTeamIdx++;
            if (currentTeamIdx >= activeTeams.size()) {
                currentTeamIdx = 0;
            }
        }
    }

    private Player getCurrentPlayer() {
        return activeTeams.get(currentTeamIdx).getPlayers().get(currentPlayerIdx);
    }

    private boolean isRoundFinished() {
        for (Team t : activeTeams) {
            for (Player p : t.getPlayers()) {
                if (playerScores.get(p).getCompletedFramesCount() < 10) return false;
            }
        }
        return true;
    }

    private void finishRound() {
        roundOver = true;

        for (Team t : activeTeams) {
            int teamTotal = t.getPlayers().stream()
                    .mapToInt(p -> playerScores.get(p).getScore())
                    .sum();
            t.setScore(teamTotal);
        }

        bracket.advanceRound();

        updateGui();
        scoreboard.update(playerScores);

        if (bracket.isTournamentOver()) {
            Team champion = bracket.getChampion();
            winnerLabel.setText("🏆 CHAMPION: " + champion.getName() + " 🏆");
            winnerLabel.setVisible(true);
            actionBtn.setText("Game Over");
            actionBtn.setDisable(true);
        } else {
            winnerLabel.setText("Round Complete!");
            winnerLabel.setVisible(true);
            actionBtn.setText("Start Next Round");
        }
    }

    private void updateGui() {
        scorePane.getChildren().clear();
        scorePane.getChildren().add(new Label("TOURNAMENT BOARD"));

        for (Matchup m : bracket.getMatchups()) {
            VBox matchBox = new VBox(5);
            matchBox.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px; -fx-padding: 5px; -fx-background-color: white;");

            matchBox.getChildren().add(createTeamStats(m.getTeam1()));

            Label vsLabel = new Label("VS");
            vsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            vsLabel.setMaxWidth(Double.MAX_VALUE);
            vsLabel.setAlignment(Pos.CENTER);
            matchBox.getChildren().add(vsLabel);

            if (m.getTeam2() != null) {
                matchBox.getChildren().add(createTeamStats(m.getTeam2()));
            } else {
                matchBox.getChildren().add(new Label("(Bye Week)"));
            }

            if (roundOver) {
                Team winner = m.getWinner();
                Label winLabel = new Label("Winner: " + winner.getName());
                winLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                matchBox.getChildren().add(winLabel);
            }

            scorePane.getChildren().add(matchBox);
        }

        if (!roundOver) {
            nowLabel.setText("Up Now: " + getCurrentPlayer().getName() + " (" +
                    getCurrentPlayer().getTeam().getName() + ")");
        } else {
            nowLabel.setText("Round Finished.");
        }
    }

    private Node createTeamStats(Team t) {
        VBox box = new VBox(2);
        Label name = new Label(t.getName());
        name.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        box.getChildren().add(name);

        int teamTotal = 0;
        for (Player p : t.getPlayers()) {
            int s = playerScores.getOrDefault(p, new Score()).getScore();
            teamTotal += s;

            String marker = (!roundOver && p == getCurrentPlayer()) ? " ➤" : "";
            box.getChildren().add(new Label(" " + p.getName() + ": " + s + marker));
        }
        box.getChildren().add(new Label(" Total: " + teamTotal));
        return box;
    }

    public static void main(String[] args) {
        launch();
    }
}

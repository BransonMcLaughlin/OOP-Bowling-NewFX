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
    private List<Team> activeTeams; // Teams playing in the current round
    private Map<Player, Score> playerScores;
    
    // State tracking
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

    @Override
    public void start(Stage stage) {
        // 1. Initialize Teams
        List<Team> allTeams = Arrays.asList(
                new Team("Pump Theorists", Arrays.asList(new Player("Branson", null), new Player("Jordan", null))),
                new Team("Class Skippers", Arrays.asList(new Player("Hamza", null), new Player("Gage", null))),
                new Team("Pin Pals", Arrays.asList(new Player("Alex", null), new Player("Sam", null))),
                new Team("Lane Lizards", Arrays.asList(new Player("Chris", null), new Player("Pat", null)))
        );
        
        // 2. Initialize Bracket
        bracket = new Bracket(allTeams);
        playerScores = new HashMap<>();
        rack = new RackOfPins();

        // 3. Setup GUI
        setupGui(stage);

        // 4. Start the first round
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
        
        // Wrap scorePane in ScrollPane in case there are many teams
        ScrollPane rightScroll = new ScrollPane(scorePane);
        rightScroll.setFitToWidth(true);

        BorderPane root = new BorderPane();
        root.setCenter(center);
        root.setRight(rightScroll);

        Scene scene = new Scene(root, 1100, 700);
        stage.setScene(scene);
        stage.setTitle("Bowling Tournament");
        stage.show();
    }

    /**
     * Resets state for a new tournament round.
     */
    private void startNextRound() {
        // Get the matchups for this round
        List<Matchup> matchups = bracket.getMatchups();
        activeTeams = new ArrayList<>();
        
        // Extract teams from matchups to build our turn order
        for (Matchup m : matchups) {
            activeTeams.add(m.getTeam1());
            if (m.getTeam2() != null) {
                activeTeams.add(m.getTeam2());
            }
        }

        // Reset scores for all active players
        playerScores.clear();
        for (Team t : activeTeams) {
            t.setScore(0); // Reset team total
            for (Player p : t.getPlayers()) {
                playerScores.put(p, new Score());
            }
        }

        // Reset indices and state
        currentTeamIdx = 0;
        currentPlayerIdx = 0;
        roundOver = false;
        rack.resetRack();
        
        // Update GUI
        roundLabel.setText("ROUND " + bracket.getRoundNumber());
        winnerLabel.setVisible(false);
        actionBtn.setText("Roll");
        actionBtn.setDisable(false);
        statusLabel.setText("Ready for Round " + bracket.getRoundNumber() + "!");
        
        updateGui();
    }

    private void handleActionButton() {
        if (roundOver) {
            // If round is over, button acts as "Start Next Round"
            if (bracket.isTournamentOver()) {
                // Game completely done, maybe restart?
                actionBtn.setDisable(true);
                statusLabel.setText("Tournament Complete. Restart application to play again.");
            } else {
                startNextRound();
            }
        } else {
            // Normal gameplay
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

        // Check if frame is done
        if (s.isCurrentFrameComplete()) {
            rack.resetRack();
            advanceToNextPlayer();
        }

        updateGui();

        if (isRoundFinished()) {
            finishRound();
        }
    }

    private void advanceToNextPlayer() {
        currentPlayerIdx++;
        // If we exhausted players on this team, move to next team
        if (currentPlayerIdx >= activeTeams.get(currentTeamIdx).getPlayers().size()) {
            currentPlayerIdx = 0;
            currentTeamIdx++;
            // If we exhausted all teams, loop back to first team
            if (currentTeamIdx >= activeTeams.size()) {
                currentTeamIdx = 0;
            }
        }
    }

    private Player getCurrentPlayer() {
        return activeTeams.get(currentTeamIdx).getPlayers().get(currentPlayerIdx);
    }

    private boolean isRoundFinished() {
        // Round is done when EVERY active player has completed 10 frames
        for (Team t : activeTeams) {
            for (Player p : t.getPlayers()) {
                if (playerScores.get(p).getCompletedFramesCount() < 10) {
                    return false;
                }
            }
        }
        return true;
    }

    private void finishRound() {
        roundOver = true;
        
        // 1. Update Team scores based on player performance
        for (Team t : activeTeams) {
            int teamTotal = t.getPlayers().stream()
                             .mapToInt(p -> playerScores.get(p).getScore())
                             .sum();
            t.setScore(teamTotal);
        }

        // 2. Tell Bracket to resolve winners
        bracket.advanceRound();

        updateGui(); // Refresh to show "Winner" tags

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

        // Iterate through MATCHUPS, not just list of teams
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

            // Highlight winner if round is over
            if (roundOver) {
                Team winner = m.getWinner();
                Label winLabel = new Label("Winner: " + winner.getName());
                winLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                matchBox.getChildren().add(winLabel);
            }

            scorePane.getChildren().add(matchBox);
        }

        if (!roundOver) {
            nowLabel.setText("Up Now: " + getCurrentPlayer().getName() + " (" + getCurrentPlayer().getTeam().getName() + ")");
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
            // Get score from map, or 0 if not initialized yet
            int s = playerScores.containsKey(p) ? playerScores.get(p).getScore() : 0;
            teamTotal += s;
            
            // Marker for current player
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
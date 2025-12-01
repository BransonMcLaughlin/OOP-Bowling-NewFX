import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bracket {
    private List<Team> remainingTeams;
    private List<Matchup> currentMatchups;
    private int roundNumber;

    public Bracket(List<Team> teams) {
        this.remainingTeams = new ArrayList<>(teams);
        this.roundNumber = 0;
        createPairings(); // Initialize Round 1
    }

    /**
     * Takes the remaining teams, shuffles them, and pairs them into Matchups.
     */
    private void createPairings() {
        currentMatchups = new ArrayList<>();
        // Shuffle to ensure random seedings
        Collections.shuffle(remainingTeams);

        for (int i = 0; i < remainingTeams.size(); i += 2) {
            Team t1 = remainingTeams.get(i);
            // Handle odd number of teams (Bye)
            Team t2 = (i + 1 < remainingTeams.size()) ? remainingTeams.get(i + 1) : null;
            
            currentMatchups.add(new Matchup(t1, t2));
        }
        roundNumber++;
    }

    public List<Matchup> getMatchups() {
        return currentMatchups;
    }

    /**
     * Resolves the current round.
     * Calculates winners, updates the remainingTeams list, and prepares the next round.
     */
    public void advanceRound() {
        List<Team> winners = new ArrayList<>();
        
        for (Matchup m : currentMatchups) {
            winners.add(m.getWinner());
        }

        // The winners become the remaining teams for the next round
        this.remainingTeams = winners;
        
        // If we have more than 1 team left, set up the next round
        if (!isTournamentOver()) {
            createPairings();
        }
    }

    public boolean isTournamentOver() {
        return remainingTeams.size() <= 1;
    }

    public Team getChampion() {
        if (isTournamentOver() && !remainingTeams.isEmpty()) {
            return remainingTeams.get(0);
        }
        return null; // Tournament still in progress
    }
    
    public int getRoundNumber() {
        return roundNumber;
    }
}
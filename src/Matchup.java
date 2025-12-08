// Jordan Charlie, Gage Trevino, Hamza Quadri, Evan Ernst, Branson Mclaughlin
// CSCI 3331 - 001
// This class represents a single pairing in the tournament, storing references to two teams and determining the winner based on their scores or handling bye rounds.

public class Matchup {
    private Team team1;
    private Team team2;

    public Matchup(Team team1, Team team2) {
        this.team1 = team1;
        this.team2 = team2;
    }

    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public Team getWinner() {
        if (team2 == null) return team1; // Handle "Bye" weeks (odd number of teams)
        
        // if it is a tie breaker, then default to team 1 (i guess we could improve this later)
        return (team1.getScore() >= team2.getScore()) ? team1 : team2;
    }

    @Override
    public String toString() {
        if (team2 == null) {
            return team1.getName() + " (Bye)";
        }
        return team1.getName() + " vs " + team2.getName();
    }
}
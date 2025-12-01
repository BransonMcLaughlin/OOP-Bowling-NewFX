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
        
        // Tie-breaker logic can be added here, currently defaulting to team1 on ties
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
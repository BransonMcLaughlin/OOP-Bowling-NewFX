import java.util.*;

public class Bracket {
    private List<Team> teams;
    private List<Team> advancingTeams;

    public Bracket(List<Team> teams) {
        this.teams = new ArrayList<>(teams);
        this.advancingTeams = new ArrayList<>();
    }

    public void recordWinner(Team winner) {
        if (!advancingTeams.contains(winner)) {
            advancingTeams.add(winner);
        }
    }

    public List<Team> getAdvancingTeams() {
        return advancingTeams;
    }

    public void printBracket() {
        System.out.println("Current Bracket:");
        for (Team t : teams) {
            String line = t.getName();
            if (advancingTeams.contains(t)) {
                line += " (advanced)";
            }
            System.out.println(line);
        }
    }

    public boolean isFinal() {
        return advancingTeams.size() == 1;
    }

    public Team getChampion() {
        if (isFinal()) {
            return advancingTeams.get(0);
        }
        return null;
    }
}

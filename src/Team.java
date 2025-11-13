import java.util.List;

public class Team {
    private String name;
    private int score;
    private List<Player> players;

    public Team(String name, List<Player> players) {
        this.name = name;
        this.players = players;
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.getName() + " - Team Roster:\n");
        for (Player p : players) {
            s.append(p.getName() + "\n");
        }
        return s.toString();
    }
}

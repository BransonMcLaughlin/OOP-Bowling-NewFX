// Jordan Charlie, Gage Trevino, Hamza Quadri, Evan Ernst, Branson Mclaughlin
// CSCI 3331 - 001
// This class represents a tournament participant, storing their name and associated team information.

public class Player {
    
    String name;
    Team team;

    public Player(String name, Team team) {
        this.name = name;
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "Player [name=" + name + ", team=" + team + "]";
    }
}

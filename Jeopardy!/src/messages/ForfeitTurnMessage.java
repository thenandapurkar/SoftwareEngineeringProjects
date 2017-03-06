package messages;


public class ForfeitTurnMessage implements Message {

    private final String teamName;

    public ForfeitTurnMessage(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }
}



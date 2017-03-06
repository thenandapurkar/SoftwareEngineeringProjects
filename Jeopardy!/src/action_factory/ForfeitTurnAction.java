package action_factory;

import frames.MainGUINetworked;
import game_logic.ServerGameData;
import messages.ForfeitTurnMessage;
import messages.Message;
import server.Client;


public class ForfeitTurnAction extends Action {

    @Override
    public void executeAction(final MainGUINetworked mainGUI, final ServerGameData gameData, final Message message, final Client client) {
        gameData.nextTurn();

        ForfeitTurnMessage forfeitTurnMessage = ForfeitTurnMessage.class.cast(message);
        mainGUI.addUpdate(String.format("Team %s did not click a question in time so they are losing their turn", forfeitTurnMessage.getTeamName()));
        if (gameData.getCurrentTeam().getTeamIndex() == client.getTeamIndex()){
            mainGUI.enableAllButtons();
        } else {
            mainGUI.disableAllButtons();
        }
    }
}



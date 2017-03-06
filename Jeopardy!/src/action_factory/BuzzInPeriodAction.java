package action_factory;

import frames.MainGUINetworked;
import game_logic.ServerGameData;
import messages.Message;
import other_gui.QuestionGUIElementNetworked;
import server.Client;


public class BuzzInPeriodAction extends Action {
    @Override
    public void executeAction(MainGUINetworked mainGUI, ServerGameData gameData, Message message, Client client) {
        QuestionGUIElementNetworked questionGUIElement = client.getCurrentQuestion();
        questionGUIElement.resetTimer(gameData);
        questionGUIElement.showTimerInsteadOfTeam();
        questionGUIElement.toggleWaitingAnimation(gameData);
        questionGUIElement.showBuzzButton();

        mainGUI.hideTeamTimers();

        if(client.getTeamIndex() == gameData.getCurrentTeam().getTeamIndex()){
            gameData.getCurrentTeam().deductPoints(questionGUIElement.getPointValue());
        }
    }
}



package action_factory;

import frames.MainGUINetworked;
import game_logic.ServerGameData;
import messages.Message;
import other_gui.QuestionGUIElementNetworked;
import server.Client;


public class QuestionExpiredAction extends Action {
    @Override
    public void executeAction(MainGUINetworked mainGUI, ServerGameData gameData, Message message, Client client) {
        QuestionGUIElementNetworked questionGUIElementNetworked = client.getCurrentQuestion();
        mainGUI.showMainPanel();
        mainGUI.addUpdate("The correct answer was " + questionGUIElementNetworked.getAnswer());
    }
}



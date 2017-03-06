package action_factory;

import frames.MainGUINetworked;
import game_logic.ServerGameData;
import messages.Message;
import messages.QuestionTimerMessage;
import other_gui.QuestionGUIElementNetworked;
import server.Client;


public class QuestionTimerAction extends Action {

    @Override
    public void executeAction(MainGUINetworked mainGUI, ServerGameData gameData, Message message, Client client) {

        QuestionTimerMessage questionTimerMessage = QuestionTimerMessage.class.cast(message);
        QuestionGUIElementNetworked questionGUIElementNetworked = client.getCurrentQuestion();
        questionGUIElementNetworked.getTimerLabel().setText(Long.toString(questionTimerMessage.getCount()));
        questionGUIElementNetworked.updateAnnouncements(String.format("Answer within %d seconds!", questionTimerMessage.getCount()));
    }
}


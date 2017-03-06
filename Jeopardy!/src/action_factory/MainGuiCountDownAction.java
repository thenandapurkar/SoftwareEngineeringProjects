package action_factory;

import frames.MainGUINetworked;
import game_logic.ServerGameData;
import messages.MainGuiCountDownMessage;
import messages.Message;
import server.Client;


public class MainGuiCountDownAction extends Action {

    @Override
    public void executeAction(MainGUINetworked mainGUI, ServerGameData gameData, Message message, Client client) {
        MainGuiCountDownMessage countDownMessage = MainGuiCountDownMessage.class.cast(message);
        mainGUI.getTimerLabel().setVisible(true);
        mainGUI.getTimerLabel().setText(Long.toString(countDownMessage.getCount()));
    }
}


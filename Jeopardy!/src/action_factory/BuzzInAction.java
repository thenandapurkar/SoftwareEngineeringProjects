package action_factory;

import frames.MainGUINetworked;
import game_logic.ServerGameData;
import messages.BuzzInMessage;
import messages.Message;
import other_gui.QuestionGUIElementNetworked;
import server.Client;

public class BuzzInAction extends Action{

	@Override
	public void executeAction(MainGUINetworked mainGUI, ServerGameData gameData,
			Message message, Client client) {
		BuzzInMessage buzzMessage = (BuzzInMessage) message;
		//update the team on the current question to be the one who buzzed in
		client.getCurrentQuestion().updateTeam(buzzMessage.getBuzzInTeam(), gameData);
		QuestionGUIElementNetworked questionGUIElementNetworked = client.getCurrentQuestion();
		questionGUIElementNetworked.resetTimer(gameData);
		questionGUIElementNetworked.showTeamInsteadOfLabel();
		mainGUI.moveCurrentTurnTimer(buzzMessage.getBuzzInTeam());
	}

}



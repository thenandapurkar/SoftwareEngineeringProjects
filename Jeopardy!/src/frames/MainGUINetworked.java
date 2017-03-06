package frames;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import game_logic.ServerGameData;
import game_logic.User;
import listeners.NetworkedWindowListener;
import messages.MainGuiCountDownMessage;
import messages.ForfeitTurnMessage;
import messages.PlayerLeftMessage;
import messages.RestartGameMessage;
import other_gui.*;
import server.Client;
import timers.JeopardyTimer;
import timers.TimerAction;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//used only for a networked game and inherits from MainGUI
public class MainGUINetworked extends MainGUI {

	private Client client;
	//has a networked game data
	private ServerGameData serverGameData;
	//had a networked FJ panel that I need as a memeber variable
	private FinalJeopardyGUINetworked fjGUI;

	private JLabel timerLabel = new JLabel();
	private JeopardyTimer jeopardyTimer = new JeopardyTimer(1000, 1000, 15);
	private int countDown = 15;
	private Map<Integer, JLabel> userTimeLabelMap = new HashMap<>();
	private long startLong;
	JLabel updatesLabel;

	public MainGUINetworked(ServerGameData gameData, Client client, User loggedInUser) {
		super(loggedInUser);
		this.serverGameData = gameData;
		this.client = client;
		//calls a method in MainGUI that basically acts as a constructor
		//since you can only call the super class's constructor as the first line of the child constructor,
		//but I need to have serverGameData initialized before I can cosntruct my GUI, this is the solution
		make(gameData);

		jeopardyTimer.setStartAction(new TimerAction() {
			@Override
			public void execute() {
				timerLabel.setVisible(true);
				startLong = TimeUnit.SECONDS.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
			}
		});
		jeopardyTimer.setStopAction(new TimerAction() {
			@Override
			public void execute() {
				timerLabel.setVisible(false);
			}
		});
		jeopardyTimer.setRunAction(new TimerAction() {
			@Override
			public void execute() {
			    long endTime = TimeUnit.SECONDS.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
			    if(endTime - startLong >= 15){
                    jeopardyTimer.stop();
                    client.sendMessage(new ForfeitTurnMessage(client.getName()));
                }
                client.sendMessage(new MainGuiCountDownMessage(15 - (endTime - startLong)));
                timerLabel.setText(Long.toString(15 - (endTime - startLong)));
			}
		});

		doTimerAction();
	}

    public JeopardyTimer getJeopardyTimer() {
		return jeopardyTimer;
	}

	public JLabel getTimerLabel(){
		return this.timerLabel;
	}

	private void doTimerAction() {
		if (gameData.getCurrentTeam().getTeamIndex() != client.getTeamIndex()){
			jeopardyTimer.stop();
		} else {
			jeopardyTimer.start();
		}
	}

	
	public FinalJeopardyGUINetworked getFJGUI(){
		return fjGUI;
	}

	//disables all enabled buttons to have enabled icon
	public void disableAllButtons(){
		System.out.println("Disabling");
		for (QuestionGUIElement question : gameData.getQuestions()){
			if (!question.isAsked()){
				question.getGameBoardButton().setDisabledIcon(QuestionGUIElement.getEnabledIcon());
				question.getGameBoardButton().setEnabled(false);
			}
		}
		showCurrentTurnTimer();
	}

	//enables all questions that have not been chosen
	public void enableAllButtons(){
		for (QuestionGUIElement question : gameData.getQuestions()){
			if (!question.isAsked()){
				question.getGameBoardButton().setIcon(QuestionGUIElement.getEnabledIcon());
				question.getGameBoardButton().setEnabled(true);
			}
		}
		//TODO: This needs to sync across clients
		jeopardyTimer.start();
		showCurrentTurnTimer();
	}

	private synchronized void showCurrentTurnTimer(){
        moveCurrentTurnTimer(gameData.getCurrentTeam().getTeamIndex());
    }

    public void moveCurrentTurnTimer(int teamIndex){
	    for(Integer keys : userTimeLabelMap.keySet()){
	        userTimeLabelMap.get(keys).setVisible(false);
        }

        userTimeLabelMap.get(teamIndex).setVisible(true);
    }

	//depending on whether the current team is same at the client's team index, we enable or disable all buttons
	//override showMainPanel from super class in order to always check if we should enable.disbale buttons
	@Override
	public void showMainPanel() {
		if (gameData.getCurrentTeam().getTeamIndex() != client.getTeamIndex()){
			disableAllButtons();
		}
		else {
			enableAllButtons();
			
		}

		super.showMainPanel();
	}

	//override from super class; only add the restart option if the client is the host
	@Override
	protected void createMenu() {

		if (client.isHost()){
			menu.add(restartThisGameButton);
		}

		menu.add(chooseNewGameFileButton);
		menu.add(logoutButton);
		menu.add(exitButton);
		menuBar.add(menu);
		setJMenuBar(menuBar);
	}

	//in the non networked game, this logic happens in the AnsweringLogic class in the QuestionGUIElement
	//but we need to be able to call this from QuestionAnsweredAction class
	public void startFinalJeopardy(){
		gameData.disableRemainingButtons();
		addUpdate("It's time for Final Jeopardy!");
		gameData.determineFinalists();
		//if no one made it show the main panel and show the rating window
		if (gameData.getFinalistsAndEliminatedTeams().getFinalists().size() == 0){
			showMainPanel();
			new WinnersAndRatingGUINetworked(serverGameData, this, client, true).setVisible(true);
		}
		else{
			//if this client did not make it to FJ, show the rating window
			if (gameData.getFinalistsAndEliminatedTeams().getElimindatedTeamsIndices().contains(client.getTeamIndex())){
				showMainPanel();
				client.setElimindated(true);
				new WinnersAndRatingGUINetworked(serverGameData, this, client, false).setVisible(true);
			}
			// create and store a networked fjpanel and switch to it
			else{
				fjGUI = new FinalJeopardyGUINetworked(serverGameData, this, client);
				changePanel(fjGUI);
			}
		}

	}

	//sets the bet for the provided team with the provided bet amount, called from SetBetAction class
	public void setBet(int team, int bet){
		TeamGUIComponents teamData = serverGameData.getTeam(team);
		teamData.setBet(bet);
		fjGUI.updateTeamBet(teamData);
	}

	@Override
	protected JPanel createJeopardyPanel() {
		JPanel jPanel = super.createJeopardyPanel();
		jPanel.add(timerLabel);

		timerLabel.setText(Integer.toString(15));
		return jPanel;
	}

	//since we serialize over the gameData with all GUI objects transient, we need to repopulate them on the client side
	//we override this from the super class in order to add different action listeners to the question object
	//and so we can iterate over the networked questions instead
	@Override
	protected void populateQuestionButtons(){
		for (int x = 0; x<QUESTIONS_LENGTH_AND_WIDTH; x++){
			for (int y = 0; y<QUESTIONS_LENGTH_AND_WIDTH; y++){
				QuestionGUIElementNetworked question = serverGameData.getNetworkedQuestions()[x][y];
				question.setClient(client, gameData.getNumberOfTeams());
				question.addActionListeners(this, serverGameData);
				questionButtons[question.getX()][question.getY()] = question.getGameBoardButton();
				question.getGameBoardButton().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jeopardyTimer.stop();
                    }
                });
			}
		}

	}

	@Override
	protected JPanel createProgressPanel() {
		// create panels
		JPanel pointsPanel = new JPanel(new GridLayout(gameData.getNumberOfTeams(), 2));
		JPanel southEastPanel = new JPanel(new BorderLayout());
		JPanel eastPanel = new JPanel();
		// other local variables
		updatesLabel = new JLabel("Game Progress");
		JScrollPane updatesScrollPane = new JScrollPane(updatesTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// setting appearances
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, southEastPanel, updatesLabel, updatesScrollPane,
				updatesTextArea);
		AppearanceSettings.setSize(400, 400, pointsPanel, updatesScrollPane);
		AppearanceSettings.setTextComponents(updatesTextArea);

		updatesLabel.setFont(AppearanceConstants.fontLarge);
		pointsPanel.setBackground(Color.darkGray);
		updatesLabel.setBorder(BorderFactory.createLineBorder(Color.darkGray));
		updatesScrollPane.setBorder(null);

		updatesTextArea.setText("Welcome to Jeopardy!");
		updatesTextArea.setFont(AppearanceConstants.fontSmall);
		updatesTextArea.append("The team to go first will be " + gameData.getCurrentTeam().getTeamName());

		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.PAGE_AXIS));
		// adding components/containers
        JPanel labelsPanel = new JPanel(new FlowLayout());

		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, updatesLabel, labelsPanel);
        labelsPanel.add(updatesLabel);
        labelsPanel.add(new JLabel(client.getUser().getUsername()));

		southEastPanel.add(labelsPanel, BorderLayout.NORTH);
		southEastPanel.add(updatesScrollPane, BorderLayout.CENTER);

		
		AppearanceSettings.setBackground(AppearanceConstants.lightBlue, updatesLabel);
		// adding team labels, which are stored in the TeamGUIComponents class,
		// to the appropriate panel
		for (int i = 0; i < gameData.getNumberOfTeams(); i++) {
			TeamGUIComponents team = gameData.getTeamDataList().get(i);
			pointsPanel.add(team.getMainTeamNameLabel());

			JLabel clockLabel = new AnimationTimer(AnimationTimer.AnimationTimerConstants.CLOCK);
			this.userTimeLabelMap.put(gameData.getTeamDataList().get(i).getTeamIndex(), clockLabel);
			pointsPanel.add(clockLabel);
            clockLabel.setVisible(false);

			pointsPanel.add(team.getTotalPointsLabel());
		}
		showCurrentTurnTimer();

		eastPanel.add(pointsPanel);
		eastPanel.add(southEastPanel);

		return eastPanel;
	}



	// adding event listeners, override from MainGUI
	@Override
	protected void addListeners() {

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		//add window listener
		addWindowListener(new NetworkedWindowListener(client, MainGUINetworked.this));
		//add action listeners
		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendPlayerLeftMessage();
				System.exit(0);
			}
		});

		restartThisGameButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//choose a different team to start the game
				gameData.chooseFirstTeam();
				client.sendMessage(new RestartGameMessage(gameData.getCurrentTeam().getTeamIndex()));
			}
		});

		chooseNewGameFileButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendPlayerLeftMessage();
				new StartWindowGUI(loggedInUser).setVisible(true);
			}
		});

		logoutButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				sendPlayerLeftMessage();
				new LoginGUI();
			}
		});
	}

	private void sendPlayerLeftMessage(){
		client.sendMessage(new PlayerLeftMessage(client.getTeamIndex()));
		client.close();
		dispose();
	}

    public void hideTeamTimers() {
        for(int key : userTimeLabelMap.keySet()){
            userTimeLabelMap.get(key).setVisible(false);
        }
    }
}




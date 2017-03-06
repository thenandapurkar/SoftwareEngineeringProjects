package other_gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import frames.AnimationTimer;
import frames.MainGUI;
import frames.MainGUINetworked;
import game_logic.GameData;
import game_logic.ServerGameData;
import messages.*;
import server.Client;
import timers.JeopardyTimer;
import timers.TimerAction;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

//inherits from QuestionGUIElement
public class QuestionGUIElementNetworked extends QuestionGUIElement {

    private static final String SHOW_LABEL = "ShowLabel";
    private static final String SHOW_TIMER = "ShowTimer";

    private Client client;
    //very similar variables as in the AnsweringLogic class
    public Boolean hadSecondChance;
    private TeamGUIComponents currentTeam;
    private TeamGUIComponents originalTeam;
    int teamIndex;
    int numTeams;
    //stores team index as the key to a Boolean indicating whether they have gotten a chance to answer the question
    private HashMap<Integer, Boolean> teamHasAnswered;

    private JPanel teamLabelPanel = new JPanel(new CardLayout());

    public JLabel getTimerLabel() {
        return timerLabel;
    }

    private transient JLabel timerLabel;
    private transient AnimationTimer waitingLabel;

    private transient JeopardyTimer jeopardyTimer = new JeopardyTimer(1000, 1000, 15);
    private transient long startTime;
    //private transient JButton buzzInButton;

    public QuestionGUIElementNetworked(String question, String answer, String category, int pointValue, int indexX, int indexY) {
        super(question, answer, category, pointValue, indexX, indexY);
    }

    //set the client and also set the map with the booleans to all have false
    public void setClient(Client client, int numTeams) {
        this.client = client;
        this.numTeams = numTeams;
        teamIndex = client.getTeamIndex();
        teamHasAnswered = new HashMap<>();
        for (int i = 0; i < numTeams; i++) teamHasAnswered.put(i, false);
    }

    //returns whether every team has had a chance at answering this question
    public Boolean questionDone() {
        Boolean questionDone = true;
        for (Boolean currentTeam : teamHasAnswered.values()) questionDone = questionDone && currentTeam;
        return questionDone;
    }

    //overrides the addActionListeners method in super class
    @Override
    public void addActionListeners(MainGUI mainGUI, GameData gameData) {
        passButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //send a buzz in message
                client.sendMessage(new BuzzInMessage(teamIndex));
            }

        });

        gameBoardButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //send a question clicked message
                client.sendMessage(new QuestionClickedMessage(getX(), getY()));
                if (mainGUI instanceof MainGUINetworked) {
                    MainGUINetworked mainGUINetworked = (MainGUINetworked) mainGUI;
                    mainGUINetworked.getJeopardyTimer().stop();
                }
            }
        });

        submitAnswerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //send question answered message
                client.sendMessage(new QuestionAnsweredMessage(answerField.getText()));
                //mainGUI.showMainPanel();
            }

        });
    }

    @Override
    protected void createGUI() {

        //local variables
        JPanel infoPanel = new JPanel(new GridLayout(1, 4));

        JPanel answerPanel = new JPanel(new BorderLayout());
        JPanel southPanel = new JPanel(new BorderLayout());

        JPanel formatErrorPanel = new JPanel();
        JPanel northPanel = new JPanel();
        JPanel passPanel = new JPanel();
        //appearance settings
        AppearanceSettings.setBackground(Color.darkGray, passPanel, gameBoardButton, questionPanel, announcementsLabel, answerPanel, formatErrorPanel, southPanel, passPanel);
        AppearanceSettings.setBackground(AppearanceConstants.darkBlue, teamLabel, pointLabel, categoryLabel, infoPanel);
        AppearanceSettings.setForeground(Color.lightGray, teamLabel, pointLabel, categoryLabel, announcementsLabel);
        AppearanceSettings.setFont(AppearanceConstants.fontLarge, questionLabel, teamLabel, pointLabel, categoryLabel);
        AppearanceSettings.setFont(AppearanceConstants.fontMedium, gameBoardButton, announcementsLabel, submitAnswerButton, answerField, passButton);
        AppearanceSettings.setTextAlignment(teamLabel, pointLabel, categoryLabel, announcementsLabel);

        questionLabel.setText(question);
        questionLabel.setEditable(false);
        //need to do this so the text shows up on the button
        gameBoardButton.setHorizontalTextPosition(JButton.CENTER);
        gameBoardButton.setVerticalTextPosition(JButton.CENTER);
        passButton.setVisible(false);
        // sourced from: http://stackoverflow.com/questions/3213045/centering-text-in-a-jtextarea-or-jtextpane-horizontal-text-alignment
        //centers the text in the question pane
        StyledDocument doc = questionLabel.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        gameBoardButton.setBorder(BorderFactory.createLineBorder(AppearanceConstants.darkBlue));
        gameBoardButton.setOpaque(true);
        answerField.setForeground(Color.gray);
        questionLabel.setBackground(AppearanceConstants.lightBlue);

        //components that need their size set
        gameBoardButton.setPreferredSize(new Dimension(200, 200));
        questionLabel.setPreferredSize(new Dimension(800, 400));
        answerField.setPreferredSize(new Dimension(600, 100));
        infoPanel.setPreferredSize(new Dimension(900, 80));
        formatErrorPanel.setPreferredSize(new Dimension(800, 100));

        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.PAGE_AXIS));
        //add components to the panels
        if (timerLabel == null) {
            timerLabel = new JLabel("Timer Label Here");
        }

        if(teamLabelPanel == null){
            teamLabelPanel = new JPanel(new CardLayout());
        }

        teamLabelPanel.add(teamLabel, SHOW_LABEL);
        teamLabelPanel.add(new AnimationTimer(AnimationTimer.AnimationTimerConstants.CLOCK), SHOW_TIMER);

        AppearanceSettings.setBackground(AppearanceConstants.darkBlue, teamLabelPanel, teamLabel);
        infoPanel.add(timerLabel);
        infoPanel.add(teamLabelPanel);
        infoPanel.add(categoryLabel);
        infoPanel.add(pointLabel);

        answerPanel.add(answerField, BorderLayout.CENTER);
        answerPanel.add(submitAnswerButton, BorderLayout.EAST);

        if(waitingLabel == null){
            waitingLabel = new AnimationTimer(AnimationTimer.AnimationTimerConstants.WAITING);
            waitingLabel.setVisible(false);
        }

        formatErrorPanel.add(waitingLabel);
        formatErrorPanel.add(announcementsLabel);

        northPanel.add(infoPanel);
        northPanel.add(formatErrorPanel);

        southPanel.add(answerPanel, BorderLayout.NORTH);
        southPanel.add(passPanel, BorderLayout.SOUTH);


        passPanel.add(passButton);
        passButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(passButton.getText().equals("Buzz In!")){
                    client.sendMessage(new BuzzInMessage(teamIndex));
                }
            }
        });
        questionPanel.add(northPanel);
        questionPanel.add(questionLabel);
        questionPanel.add(southPanel);
    }

    //override the resetQuestion method
    @Override
    public void resetQuestion() {
        super.resetQuestion();
        hadSecondChance = false;
        currentTeam = null;
        originalTeam = null;
        teamHasAnswered.clear();
        //reset teamHasAnswered map so all teams get a chance to anaswer again
        for (int i = 0; i < numTeams; i++) teamHasAnswered.put(i, false);
    }

    public void toggleWaitingAnimation(ServerGameData gameData){
        if(waitingLabel != null && currentTeam != null){
            waitingLabel.setVisible(teamIndex != gameData.getCurrentTeam().getTeamIndex());
        }
    }

    public void toggleWaitingAnimation(int buzzInTeam){
        waitingLabel.setVisible(teamIndex != buzzInTeam);
    }

    @Override
    public void populate() {
        super.populate();
        passButton.setText("Buzz In!");
    }

    public int getOriginalTeam() {
        return originalTeam.getTeamIndex();
    }

    public void updateAnnouncements(String announcement) {
        announcementsLabel.setText(announcement);
    }

    public void setOriginalTeam(int team, GameData gameData) {
        originalTeam = gameData.getTeamDataList().get(team);
        updateTeam(team, gameData);
    }

    //update the current team of this question
    public void updateTeam(int team, GameData gameData) {
        currentTeam = gameData.getTeamDataList().get(team);
        passButton.setVisible(false);
        answerField.setText("");
        //if the current team is this client
        if (team == teamIndex) {
            AppearanceSettings.setEnabled(true, submitAnswerButton, answerField);
            announcementsLabel.setText("It's your turn to try to answer the question!");
        }
        //if the current team is an opponent
        else {
            AppearanceSettings.setEnabled(false, submitAnswerButton, answerField);
            announcementsLabel.setText("It's " + currentTeam.getTeamName() + "'s turn to try to answer the question.");
        }
        //mark down that this team has had a chance to answer
        teamHasAnswered.replace(team, true);
        hadSecondChance = false;
        teamLabel.setText(currentTeam.getTeamName());
    }

    //called from QuestionAnswerAction when there is a bad answer
    public void illFormattedAnswer() {

        if (currentTeam.getTeamIndex() == teamIndex) {
            announcementsLabel.setText("You had an illformatted answer. Please try again");
        } else {
            announcementsLabel.setText(currentTeam.getTeamName() + " had an illformatted answer. They get to answer again.");
        }
    }

    //set the gui to be a buzz in period, also called from QuestionAnswerAction
    public void setBuzzInPeriod() {

        passButton.setVisible(true);
        teamLabel.setText("");

        if (teamHasAnswered.get(teamIndex)) {
            AppearanceSettings.setEnabled(false, submitAnswerButton, answerField, passButton);
            announcementsLabel.setText("It's time to buzz in! But you've already had your chance..");
        } else {
            announcementsLabel.setText("Buzz in to answer the question!");
            passButton.setEnabled(true);
            AppearanceSettings.setEnabled(false, submitAnswerButton, answerField);
        }
    }


    public TeamGUIComponents getCurrentTeam() {
        return currentTeam;
    }

    public void startTimer() {
        jeopardyTimer = new JeopardyTimer(1000, 1000, 15);
        jeopardyTimer.setStartAction(new StartTimerAction());
        jeopardyTimer.setRunAction(new RunTimerAction() {
            @Override
            protected void executeFinishAction() {

                client.sendMessage(new BuzzInPeriodMessage());
            }
        });
        jeopardyTimer.start();
    }

    public void resetTimer(ServerGameData gameData) {
        if(jeopardyTimer != null && jeopardyTimer.isRunning()){
            jeopardyTimer.stop();
            jeopardyTimer = null;
        }

        if(this.teamIndex == gameData.getCurrentTeam().getTeamIndex()){
            if (jeopardyTimer == null) {
                jeopardyTimer = new JeopardyTimer(1000, 1000, 15);
                jeopardyTimer.setStartAction(new StartTimerAction());
                jeopardyTimer.setRunAction(new RunTimerAction(){

                    @Override
                    protected void executeFinishAction() {
                        client.sendMessage(new QuestionExpiredMessage());
                    }
                });
                jeopardyTimer.start();
            }
        }
    }

    public void showTimerInsteadOfTeam() {
        CardLayout cardLayout = (CardLayout) teamLabelPanel.getLayout();
        
        cardLayout.show(teamLabelPanel, SHOW_TIMER);
    }

    public void showTeamInsteadOfLabel(){
        CardLayout cardLayout = (CardLayout) teamLabelPanel.getLayout();
        AppearanceSettings.setBackground(AppearanceConstants.darkBlue, teamLabelPanel, teamLabel);
        
        cardLayout.show(teamLabelPanel, SHOW_LABEL);
     }

    public void showBuzzButton() {
        passButton.setText("Buzz In!");
        passButton.setVisible(true);
    }

    public void hideWaitingAnimation() {
        waitingLabel.setVisible(false);
    }

    //In both cases, when we start JeopardyTimer, we have the same run action, so we should put the code
    //in once place so that we don't duplicate the code
    private class StartTimerAction implements TimerAction {

        @Override
        public void execute() {
            startTime = TimeUnit.SECONDS.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }
    }

    //Both of our Jeopardy runActions are similar in that they count down from 20 seconds. What they
    //do when the finish is slightly different but we can put the similar code in the same class and then
    //just override the executeFinishAction method
    private abstract class RunTimerAction implements TimerAction {
        @Override
        public void execute() {
            long endTime = TimeUnit.SECONDS.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            long remainingTime = 20 - (endTime - startTime);
            client.sendMessage(new QuestionTimerMessage(remainingTime));

            //timerLabel.setText(Long.toString(remainingTime));
            //updateAnnouncements(String.format("Answer within %d seconds!", remainingTime));

            if (endTime - startTime >= 20) {
                executeFinishAction();
                jeopardyTimer.stop();
                jeopardyTimer = null;
            }
        }

        protected abstract void executeFinishAction();
    }
}




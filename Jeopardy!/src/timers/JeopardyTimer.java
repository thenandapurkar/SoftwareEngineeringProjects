package timers;

import java.util.Timer;
import java.util.TimerTask;

import game_logic.ServerGameData;


public class JeopardyTimer {

    private final int delay; //How long should the timer wait before it starts
    private final int period; //How long between executions (Hint: 1000 is one second)
    private final int numPeriods; //How many times should it run before it stops

    //This is the java.util.Timer object. It handles doing executions at fixed times
    private Timer timer = new Timer();

    //Called when the timer is started
    private TimerAction startAction;
    
    //Called when everytime the timer fires
    private TimerAction runAction;
    
    //Called when the timer stops
    private TimerAction stopAction;

    //Used to track when the timer is running
    private boolean running;

    //Constructor
    //Pass 0 to delay to have it start right away
    //Pass 1000 to make it fire every second
    //Pass 15 for 15 executions
    public JeopardyTimer(final int delay, final int period, final int numPeriods) {
        this.delay = delay;
        this.period = period;
        this.numPeriods = numPeriods;
    }

    public TimerAction getRunAction() {
        return runAction;
    }

    public void setRunAction(final TimerAction runAction) {
        this.runAction = runAction;
    }

    public TimerAction getStartAction() {
        return startAction;
    }

    public void setStartAction(final TimerAction startAction) {
        this.startAction = startAction;
    }

    public TimerAction getStopAction() {
        return stopAction;
    }

    public void setStopAction(final TimerAction stopAction) {
        this.stopAction = stopAction;
    }

    //Use this to start the timer
    public void start(){
        if(!running){
            if(getStartAction() != null){
                //Fire our start action (This is an interface that you implement)
                getStartAction().execute();
            }
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(getRunAction() != null){
                        //Fires every execution
                        getRunAction().execute();
                    }
                }
            }, delay, period);
            running = true;
        }

    }

    //Stop the timer
    public void stop() {
        if (getStopAction() != null) {
            //fire the stop action
            getStopAction().execute();
        }
        //Stop the timer object
        timer.cancel();
        running = false;
    }

    public int getDelay() {
        return delay;
    }

    public int getPeriod() {
        return period;
    }

    public int getNumPeriods() {
        return numPeriods;
    }

    public boolean isRunning() {
        return running;
    }
}



package frames;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class AnimationTimer extends JLabel {

    //We only need to loads the frames one time
    private static final List<ImageIcon> CLOCK_FRAMES = new ArrayList<>();
    private static final List<ImageIcon> WAITING_FRAMES = new ArrayList<>();

    static {
        CLOCK_FRAMES.addAll(loadFrames(142, "images/clockAnimation/frame_%d_delay-0.06s.jpg"));
        WAITING_FRAMES.addAll(loadFrames(7, "images/waitingAnimation/frame_%d_delay-0.1s.jpg"));
    }

    private static List<ImageIcon> loadFrames(int numFrames, String formattedStringPath) {
        List<ImageIcon> frames = new ArrayList<>();
        for(int i = 0; i <= numFrames; i++){
            ImageIcon imageIcon = new ImageIcon(String.format(formattedStringPath, i));
            frames.add(new ImageIcon(imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
        }
        return frames;
    }

    public enum AnimationTimerConstants {
        CLOCK,
        WAITING
    };

    private List<ImageIcon> frames = new ArrayList<>();
    private int currentFrame = 0;


    public AnimationTimer(final AnimationTimerConstants animationTimerConstant){
        switch (animationTimerConstant){
            case CLOCK:
                frames = CLOCK_FRAMES;
                break;
            case WAITING:
                frames = WAITING_FRAMES;
                break;
        }
        setIcon(frames.get(0));
        startAnimation();
    }

    private void startAnimation() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setIcon(frames.get(currentFrame++));
                if (currentFrame >= frames.size()) {
                    currentFrame = 0;
                }
            }
        }, 0, 1000);
    }
}



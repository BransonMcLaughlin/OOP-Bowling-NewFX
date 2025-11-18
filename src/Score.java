import java.util.ArrayList;
import java.util.List;

public class Score {
    private List<Integer> rolls = new ArrayList<>();
    private int completedFrames = 0;
    private boolean firstRollInFrame = true;
    private int firstRollPins = 0;
    private boolean wasFirstRollInFrame = true;

    public void roll(int pins) {
        rolls.add(pins);
        wasFirstRollInFrame = firstRollInFrame; 
        if (firstRollInFrame) {
            firstRollPins = pins;
            if (pins == 10) completedFrames++;
            else firstRollInFrame = false;
        } else {
            completedFrames++;
            firstRollInFrame = true;
        }
    }

    public int getCompletedFramesCount() {
        return completedFrames;
    }

    public int getScore() {
        int score = 0, i = 0;
        for (int frame = 0; frame < 10 && i < rolls.size(); frame++) {
            int first = rolls.get(i);
            if (first == 10) {
                score += 10 + safeGet(i + 1) + safeGet(i + 2);
                i += 1;
            } else {
                int second = safeGet(i + 1);
                int frameScore = first + second;
                if (frameScore == 10) frameScore += safeGet(i + 2);
                score += frameScore;
                i += 2;
            }
        }
        return score;
    }

    private int safeGet(int idx) {
        return (idx < rolls.size()) ? rolls.get(idx) : 0;
    }

    public List<Integer> getRolls() {
        return rolls;
    }

    public String getRollMessage(String playerName, int pinsKnocked) {
        if (!wasFirstRollInFrame) {
            if (pinsKnocked == 0) return playerName + " rolled a gutter ball.";
            return playerName + " knocked down " + pinsKnocked + " pins.";
        } else {
            if (pinsKnocked == 10) return playerName + " hit a STRIKE!";
            if (pinsKnocked == 0) return playerName + " rolled a gutter ball.";
            return playerName + " knocked down " + pinsKnocked + " pins.";
        }
    }

    public boolean isCurrentFrameComplete() {
        return firstRollInFrame;
    }
}

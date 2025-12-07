import java.util.ArrayList;
import java.util.List;

public class Score {
    private List<Frame> frames;
    private int currentFrameIndex;

    public Score() {
        this.frames = new ArrayList<>();
        // Start with the first frame
        frames.add(new Frame());
        currentFrameIndex = 0;
    }

    public void roll(int pins) {
        Frame current = frames.get(currentFrameIndex);

        // If the current frame is done, create a new one and move pointer
        if (current.isComplete()) {
            Frame nextFrame = new Frame();
            frames.add(nextFrame);
            currentFrameIndex++;
            current = nextFrame;
        }

        current.addRoll(pins);
    }

    public int getScore() {
        int totalScore = 0;
        
        // We only score the first 10 frames. 
        // Any frames after index 9 (Frame 10) are just bonus rolls/fill balls.
        int limit = Math.min(frames.size(), 10);

        for (int i = 0; i < limit; i++) {
            Frame f = frames.get(i);
            
            // We can't score a frame until it's complete
            if (!f.isComplete()) break;

            int frameScore = f.getPinCount();

            if (f.isStrike()) {
                // Strike: 10 + next 2 balls
                frameScore += sumNextRolls(i, 2);
            } else if (f.isSpare()) {
                // Spare: 10 + next 1 ball
                frameScore += sumNextRolls(i, 1);
            }

            totalScore += frameScore;
        }
        return totalScore;
    }

    /**
     * Helper to look ahead at subsequent frames to calculate bonuses.
     * This abstracts away the complexity of "Strike followed by Strike".
     */
    private int sumNextRolls(int currentFrameIdx, int rollsToCount) {
        int sum = 0;
        int rollsFound = 0;
        
        // Look at subsequent frames
        for (int i = currentFrameIdx + 1; i < frames.size() && rollsFound < rollsToCount; i++) {
            Frame next = frames.get(i);
            
            // Add first roll
            sum += next.getFirstRoll();
            rollsFound++;
            
            // If we still need more, and the next frame has a second roll...
            if (rollsFound < rollsToCount) {
                // Note: If next frame is a strike, getSecondRoll() returns 0, 
                // so we continue to the NEXT frame in the loop naturally.
                if (!next.isStrike() && next.isComplete()) {
                    sum += next.getSecondRoll();
                    rollsFound++;
                }
            }
        }
        return sum;
    }

    public int getCompletedFramesCount() {
        int count = 0;
        // Only count up to 10 "real" frames
        for (int i = 0; i < Math.min(frames.size(), 10); i++) {
            if (frames.get(i).isComplete()) {
                count++;
            }
        }
        return count;
    }

    public String getRollMessage(String playerName, int pinsKnocked) {
        Frame current = frames.get(currentFrameIndex);

        // We determine message based on the state of the CURRENT frame
        // If it was a strike
        if (current.isStrike()) {
            return playerName + " hit a STRIKE!";
        }
        // If it was a spare (complete, not strike, sum is 10)
        if (current.isSpare()) {
            return playerName + " got a SPARE!";
        }
        // Gutter ball
        if (pinsKnocked == 0) {
            return playerName + " rolled a gutter ball.";
        }
        // Standard hit
        return playerName + " knocked down " + pinsKnocked + " pins.";
    }

    public boolean isCurrentFrameComplete() {
        if (frames.isEmpty()) return false;
        return frames.get(currentFrameIndex).isComplete();
    }

    public String frameToString(int i) {
        if (i >= frames.size()) return " ";

        Frame f = frames.get(i);

        Integer r1 = f.getFirstRoll();
        Integer r2 = f.getSecondRoll();

        // Strike
        if (f.isStrike()) return "X";

        // Spare
        if (f.isSpare()) return (r1 == 0 ? "-" : r1) + " /";

        // Otherwise open frame
        String a = (r1 == 0 ? "-" : r1.toString());
        String b;

        // If second roll not yet thrown -> blank
        if (f.getSecondRoll() == 0 && !f.isComplete()) {
            b = " ";
        } else {
            b = (r2 == 0 ? "-" : r2.toString());
        }

        return a + " " + b;
    }
}
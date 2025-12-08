// Jordan Charlie, Gage Trevino, Hamza Quadri, Evan Ernst, Branson Mclaughlin
// CSCI 3331 - 001
// This class implements the core scoring logic for the bowling game, tracking frame progress, calculating cumulative scores with strike and spare bonuses, and formatting the output for the scoreboard.

import java.util.ArrayList;
import java.util.List;

public class Score {
    private List<Frame> frames;
    private int currentFrameIndex;

    public Score() {
        this.frames = new ArrayList<>();
        // Start with the first frame (index 0), which is NOT final
        frames.add(new Frame(false));
        currentFrameIndex = 0;
    }

    public void roll(int pins) {
        Frame current = frames.get(currentFrameIndex);

        // If the current frame is done, move to the next one
        if (current.isComplete()) {
            // Do not create more than 10 frames
            if (frames.size() >= 10) {
                return;
            }

            // Check if the new frame will be the 10th frame (index 9)
            boolean isNextFinal = (frames.size() == 9);

            Frame nextFrame = new Frame(isNextFinal);
            frames.add(nextFrame);
            currentFrameIndex++;
            current = nextFrame;
        }

        current.addRoll(pins);
    }

    public int getScore() {
        int totalScore = 0;
        // Limit loop to 10 frames
        int limit = Math.min(frames.size(), 10);

        for (int i = 0; i < limit; i++) {
            Frame f = frames.get(i);
            
            // Cannot score an incomplete frame
            if (!f.isComplete()) break;

            int frameScore = f.getPinCount();

            // If it is frames 1-9 (index < 9), we look ahead for bonuses.
            // If it is frame 10 (index 9), we DO NOT look ahead; the bonus is inside the frame itself.
            if (i < 9) {
                if (f.isStrike()) {
                    // Strike: 10 + sum of next 2 rolls
                    frameScore += sumNextRolls(i, 2);
                } else if (f.isSpare()) {
                    // Spare: 10 + sum of next 1 roll
                    frameScore += sumNextRolls(i, 1);
                }
            }

            totalScore += frameScore;
        }
        return totalScore;
    }

    private int sumNextRolls(int currentFrameIdx, int rollsToCount) {
        int sum = 0;
        int rollsFound = 0;
        
        // Loop through subsequent frames to find the necessary bonus rolls
        for (int i = currentFrameIdx + 1; i < frames.size() && rollsFound < rollsToCount; i++) {
            Frame next = frames.get(i);
            
            //Get the first roll of the next frame
            sum += next.getFirstRoll();
            rollsFound++;
            
            //If we still need more rolls
            if (rollsFound < rollsToCount) {
                // If the next frame is the 10th frame, we can take its 2nd roll regardless of strikes.
                // If the next frame is a standard frame, we only take 2nd roll if it wasn't a strike.
                boolean canTakeSecondRoll = (i == 9) || (!next.isStrike());

                if (canTakeSecondRoll) {
                    sum += next.getSecondRoll();
                    rollsFound++;
                }
            }
        }
        return sum;
    }

    public int getCompletedFramesCount() {
        int count = 0;
        for (int i = 0; i < Math.min(frames.size(), 10); i++) {
            if (frames.get(i).isComplete()) {
                count++;
            }
        }
        return count;
    }

    public String getRollMessage(String playerName, int pinsKnocked) {
        Frame current = frames.get(currentFrameIndex);

        // Customize message for strikes/spares
        if (current.isStrike()) {
            return playerName + " hit a STRIKE!";
        }
        if (current.isSpare()) {
            return playerName + " got a SPARE!";
        }
        if (pinsKnocked == 0) {
            return playerName + " rolled a gutter ball.";
        }
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
        Integer r3 = f.getThirdRoll();

        // formatting for 10th frame
        if (i == 9) {
            String s1 = (r1 == 10) ? "X" : r1.toString();
            String s2;
            String s3;

            // Logic for 2nd slot
            if (r2 == 10) {
                s2 = "X";
            } else if (!f.isStrike() && (r1 + r2 == 10)) {
                s2 = "/"; // Spare
            } else {
                s2 = (r2 == 0 && !f.isComplete()) ? " " : r2.toString();
            }

            // Logic for 3rd slot (only visible if we earned it)
            if (f.isStrike() || f.isSpare()) {
                if (r3 == 10) {
                    s3 = "X";
                } else if (f.isStrike() && r2 != 10 && (r2 + r3 == 10)) {
                    s3 = "/"; // Spare in the fill ball
                } else {
                    s3 = (r3 == 0 && !f.isComplete()) ? " " : r3.toString();
                }
            } else {
                s3 = "";
            }

            return (s1 + " " + s2 + " " + s3).trim();
        }

        //normal format for frames 1-9
        if (f.isStrike()) return "X";
        if (f.isSpare()) return (r1 == 0 ? "-" : r1) + " /";

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
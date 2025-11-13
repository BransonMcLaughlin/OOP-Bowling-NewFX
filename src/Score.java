import java.util.ArrayList;
import java.util.List;

public class Score {
    private List<Integer> rolls = new ArrayList<>();

    public void roll(int pins) {
        rolls.add(pins);
    }

    /**
     * Returns true when the player's *current* frame is complete.
     * - If there are no rolls yet -> frame not complete (returns false)
     * - Strike completes the frame (consumes 1 roll)
     * - Otherwise a frame is two rolls; if only one roll is present for the frame -> incomplete
     */
    public boolean isCurrentFrameComplete() {
        if (rolls.size() == 0) return false;

        int i = 0;
        int frame = 0;
        while (frame < 10 && i < rolls.size()) {
            int first = rolls.get(i);
            if (first == 10) { // strike consumes single roll
                i += 1;
            } else {
                if (i + 1 < rolls.size()) {
                    i += 2; // full frame recorded
                } else {
                    // only one roll recorded for this frame and it's not a strike -> incomplete
                    return false;
                }
            }
            frame++;
        }
        // no incomplete frame found
        return true;
    }

    /**
     * Return how many pins have been knocked in the current (possibly incomplete) frame.
     * - If no rolls in the current frame -> 0
     * - After first roll returns that number
     * - After second roll returns the sum of both
     * Works correctly with strikes (returns 10 immediately).
     */
    public int getPinsInCurrentFrame() {
        int i = 0;
        int frame = 0;
        while (frame < 10 && i < rolls.size()) {
            int first = rolls.get(i);
            if (first == 10) { // strike frame complete
                i += 1;
            } else {
                if (i + 1 < rolls.size()) {
                    // full frame: sum both
                    i += 2;
                } else {
                    // only first roll exists for this frame -> return it
                    return rolls.get(i);
                }
            }
            frame++;
        }
        // if we've processed all recorded frames and didn't hit an incomplete frame,
        // then current frame is fresh -> 0 pins so far
        return 0;
    }

    /**
     * Number of complete frames currently recorded for this player (0..10).
     * Useful to tell when a player is done.
     */
    public int getCompletedFramesCount() {
        int i = 0;
        int frame = 0;
        while (frame < 10 && i < rolls.size()) {
            int first = rolls.get(i);
            if (first == 10) {
                i += 1;
            } else {
                if (i + 1 < rolls.size()) i += 2;
                else break; // incomplete frame
            }
            frame++;
        }
        return frame;
    }

    public int getScore() {
        int score = 0;
        int rollIndex = 0;
        for (int frame = 0; frame < 10 && rollIndex < rolls.size(); frame++) {
            int frameScore = 0;
            frameScore += rolls.get(rollIndex);
            if (rollIndex + 1 < rolls.size())
                frameScore += rolls.get(rollIndex + 1);
            score += frameScore;
            rollIndex += 2;
        }
        return score;
    }

    public List<Integer> getRolls() {
        return rolls;
    }
}

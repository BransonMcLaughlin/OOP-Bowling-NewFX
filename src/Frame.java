public class Frame {
    private Integer firstRoll;
    private Integer secondRoll;
    
    public Frame() {
        this.firstRoll = null;
        this.secondRoll = null;
    }

    /**
     * Records a roll in this frame.
     * @param pins The number of pins knocked down.
     */
    public void addRoll(int pins) {
        if (firstRoll == null) {
            firstRoll = pins;
        } else if (secondRoll == null) {
            secondRoll = pins;
        }
    }

    /**
     * @return The number of pins knocked down in the first roll, or 0 if not rolled.
     */
    public int getFirstRoll() {
        return (firstRoll != null) ? firstRoll : 0;
    }

    /**
     * @return The number of pins knocked down in the second roll, or 0 if not rolled.
     */
    public int getSecondRoll() {
        return (secondRoll != null) ? secondRoll : 0;
    }

    /**
     * @return True if all required rolls for this frame are completed.
     */
    public boolean isComplete() {
        // A frame is complete if:
        // 1. It is a strike (first roll is 10)
        // 2. Both rolls have been made
        if (isStrike()) {
            return true;
        }
        return firstRoll != null && secondRoll != null;
    }

    public boolean isStrike() {
        return firstRoll != null && firstRoll == 10;
    }

    public boolean isSpare() {
        return !isStrike() && 
               firstRoll != null && 
               secondRoll != null && 
               (firstRoll + secondRoll == 10);
    }

    /**
     * @return The raw score of pins knocked down in this frame only (no bonuses).
     */
    public int getPinCount() {
        return getFirstRoll() + getSecondRoll();
    }
}
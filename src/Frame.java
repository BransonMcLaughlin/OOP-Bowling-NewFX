// Jordan Charlie, Gage Trevino, Hamza Quadri, Evan Ernst, Branson Mclaughlin
// CSCI 3331 - 001
// This class defines the structure of a single bowling frame, storing roll results and handling completion logic for both standard and final frames.

public class Frame {
    private Integer firstRoll;
    private Integer secondRoll;
    private Integer thirdRoll; 
    //flag for determining third roll 
    private boolean isFinalFrame; 

    public Frame(boolean isFinalFrame) {
        this.firstRoll = null;
        this.secondRoll = null;
        this.thirdRoll = null;
        this.isFinalFrame = isFinalFrame;
    }

    public Frame() {
        this(false);
    }

    public void addRoll(int pins) {
        if (firstRoll == null) {
            firstRoll = pins;
        } else if (secondRoll == null) {
            secondRoll = pins;
        } else if (thirdRoll == null && isFinalFrame) {
            thirdRoll = pins;
        }
    }

    public int getFirstRoll() {
        return (firstRoll != null) ? firstRoll : 0;
    }

    public int getSecondRoll() {
        return (secondRoll != null) ? secondRoll : 0;
    }
    
    public int getThirdRoll() {
        return (thirdRoll != null) ? thirdRoll : 0;
    }

    public boolean isComplete() {
        //If it's NOT the final frame, then continue as normal
        if (!isFinalFrame) {
            if (isStrike()) return true; 
            return firstRoll != null && secondRoll != null;
        }

        //If it is the final frame, then
        //we need at least two rolls.
        if (firstRoll == null || secondRoll == null) return false;

        // If we got a Strike or Spare, we allow a 3rd roll.
        if (isStrike() || isSpare()) {
            return thirdRoll != null; // Complete only after 3rd roll
        }

        // If open frame (no strike/spare), it's complete after 2 rolls.
        return true;
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

    public int getPinCount() {
        // Sum of all rolls (including 3rd if it exists)
        return getFirstRoll() + getSecondRoll() + getThirdRoll();
    }
}
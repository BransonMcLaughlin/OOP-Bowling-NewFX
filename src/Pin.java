// Jordan Charlie, Gage Trevino, Hamza Quadri, Evan Ernst, Branson Mclaughlin
// CSCI 3331 - 001
// This class represents an individual bowling pin, maintaining its identification number and tracking whether it is currently standing or knocked down.

public class Pin{
    private boolean isKnockedDown;
    private int pinNum;

    public Pin(){
        this.isKnockedDown = false;
    }

    public void knockDownPin(){
        this.isKnockedDown = true;
    }

    public boolean isKnockedDown() {
        return isKnockedDown;
    }

    public void setKnockedDown(boolean isKnockedDown) {
        this.isKnockedDown = isKnockedDown;
    }

    public int getPinNum() {
        return pinNum;
    }

    public void setPinNum(int pinNum) {
        this.pinNum = pinNum;
    }
}
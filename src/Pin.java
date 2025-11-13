

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
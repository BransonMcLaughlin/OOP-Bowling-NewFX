import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RackOfPins {
    private final ArrayList<Circle> pins = new ArrayList<>();
    private final Group view = new Group();
    private final Random rand = new Random();

    public RackOfPins() {
        double startX = 450;      // moved right for center of wider window
        double startY = 150;
        double radius = 40;       // MUCH bigger pins!
        int spacing = 80;         // more spacing between pins
        int[] rowCounts = {1, 2, 3, 4};
        int yOff = 0;
        int idx = 0;

        for (int r = 0; r < rowCounts.length; r++) {
            int count = rowCounts[r];
            double rowWidth = (count - 1) * spacing;
            for (int i = 0; i < count; i++) {
                double x = startX - rowWidth / 2 + i * spacing;
                double y = startY + yOff;
                Circle c = new Circle(x, y, radius, Color.LIGHTGRAY);
                c.setStroke(Color.BLACK);
                pins.add(c);
                view.getChildren().add(c);
                idx++;
                if (idx >= 10) break;
            }
            yOff += spacing;
        }
    }

    /** Returns the JavaFX Node for displaying pins */
    public Node getView() {
        return view;
    }

    /** Knocks down a random number of currently standing pins */
    public void knockDownRandomPins(int number) {
        List<Circle> standing = getStandingPins();
        Collections.shuffle(standing, rand);
        int knockCount = Math.min(number, standing.size());
        for (int i = 0; i < knockCount; i++) {
            standing.get(i).setFill(Color.DARKGRAY);
        }
    }

    /** Resets all pins to standing (for next player or frame) */
    public void resetRack() {
        for (Circle c : pins)
            c.setFill(Color.LIGHTGRAY);
    }

    /** Returns all pins still standing */
    public List<Circle> getStandingPins() {
        List<Circle> standing = new ArrayList<>();
        for (Circle c : pins)
            if (c.getFill().equals(Color.LIGHTGRAY))
                standing.add(c);
        return standing;
    }

    /** Returns how many pins are still standing */
    public int getStandingCount() {
        return getStandingPins().size();
    }
}

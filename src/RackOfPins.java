import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RackOfPins {
    private ArrayList<Circle> pins;
    private Group view;
    private Random rand;

    public RackOfPins() {
        pins = new ArrayList<>();
        view = new Group();
        rand = new Random();

        double centerX = 400;
        double startY = 150;
        double radius = 25;
        double spacing = 55;
        int[] rows = {1, 2, 3, 4};

        for (int r = 0; r < rows.length; r++) {
            int count = rows[r];
            double rowWidth = (count - 1) * spacing;
            double y = startY + r * (spacing * 0.8);
            for (int i = 0; i < count; i++) {
                double x = centerX - rowWidth / 2 + i * spacing;
                Circle c = new Circle(x, y, radius);
                c.setFill(Color.LIGHTGRAY);
                c.setStroke(Color.BLACK);
                pins.add(c);
                view.getChildren().add(c);
            }
        }
    }

    public Node getView() {
        return view;
    }

    public void knockDownRandomPins(int number) {
        List<Circle> standing = getStandingPins();
        Collections.shuffle(standing, rand);
        int knockCount = Math.min(number, standing.size());
        for (int i = 0; i < knockCount; i++) {
            standing.get(i).setFill(Color.DARKGRAY);
        }
    }

    public void resetRack() {
        for (Circle c : pins) {
            c.setFill(Color.LIGHTGRAY);
        }
    }

    public List<Circle> getStandingPins() {
        List<Circle> standing = new ArrayList<>();
        for (Circle c : pins) {
            if (Color.LIGHTGRAY.equals(c.getFill())) {
                standing.add(c);
            }
        }
        return standing;
    }

    public int getStandingCount() {
        return getStandingPins().size();
    }
}

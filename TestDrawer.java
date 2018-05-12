import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class TestDrawer extends Application {
    private static List<Rectangle> rectangles;
    private static Map<String, Double> max = new HashMap<>();

    public static void main(String... args) {
        Scanner sc = new Scanner(System.in);
        rectangles = IntStream.range(0, sc.nextInt())
            .mapToObj(i -> new Rectangle(sc.nextDouble(), sc.nextDouble(),
                                         sc.nextDouble(), sc.nextDouble()))
            .collect(Collectors.toList());
        rectangles.stream()
            .forEach(r -> {
                    max.put("Width",
                            Math.max(Optional.ofNullable(max.get("Width"))
                                     .orElse(0d),
                                     r.getX() + r.getWidth()));
                    max.put("Height",
                            Math.max(Optional.ofNullable(max.get("Height"))
                                     .orElse(0d),
                                     r.getY() + r.getHeight()));
                });
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        double widthRate = screen.getWidth() / max.get("Width"),
            heightRate = (screen.getHeight() - 40d) / max.get("Height");
        // System.err.println(screen + ":" + max + ":"
        //                    + widthRate + ":" + heightRate);
        rectangles.stream().forEach(r -> {
                r.setX(r.getX() * Math.min(widthRate, heightRate));
                r.setY(r.getY() * Math.min(widthRate, heightRate));
                r.setWidth(r.getWidth() * Math.min(widthRate, heightRate));
                r.setHeight(r.getHeight() * Math.min(widthRate, heightRate));
            });
        Group group = new Group();
        rectangles.stream().forEach(r -> {
                r.setFill(Color.TRANSPARENT);
                r.setStroke(Color.BLACK);
                group.getChildren().add(r);
            });
        Scene scene = new Scene(group, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

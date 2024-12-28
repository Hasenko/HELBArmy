import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

import java.util.ArrayList;

public class View {
    private final HELBArmy controller;
    private final int WIDTH = 800;
    private final int HEIGHT = WIDTH;

    private GraphicsContext gc;
    private Scene scene;

    public View(HELBArmy controller, Stage primaryStage) {
        this.controller = controller;

        // init base of window
        primaryStage.setTitle("HELBArmy");
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        this.scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        this.gc = canvas.getGraphicsContext2D();
    }

    public int getWidth()
    {
        return this.WIDTH;
    }

    public Scene getScene()
    {
        return this.scene;
    }

    public GraphicsContext getGraphicsContext()
    {
        return this.gc;
    }

    // draw background
    public void drawBackground(GraphicsContext gc)
    {
        for (int i = 0; i < HELBArmy.ROWS; i++) {
            for (int j = 0; j < HELBArmy.COLUMNS; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("AAD751"));
                } else {
                    gc.setFill(Color.web("A2D149"));
                }
                gc.fillRect(i * controller.SQUARE_SIZE, j * controller.SQUARE_SIZE, controller.SQUARE_SIZE, controller.SQUARE_SIZE);
            }
        }
    }

    // draw entity
    public void drawEntity(GraphicsContext gc, ArrayList<Entity> entityList)
    {
        for (Entity entity : entityList) {
            gc.drawImage(new Image(entity.getImagePath()), entity.position.x * controller.SQUARE_SIZE, entity.position.y * controller.SQUARE_SIZE, controller.SQUARE_SIZE * entity.getWidth(), controller.SQUARE_SIZE * entity.getWidth());
        }
    }
}
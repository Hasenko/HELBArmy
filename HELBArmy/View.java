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
    public final int WIDTH = 800;
    public final int HEIGHT = WIDTH;
    private GraphicsContext gc;
    private Scene scene;

    public View(HELBArmy controller, Stage primaryStage) {
        this.controller = controller;

        primaryStage.setTitle("HELBArmy");
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        this.scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        this.gc = canvas.getGraphicsContext2D();

    }

    public Scene getScene()
    {
        return this.scene;
    }

    public GraphicsContext getGraphicsContext()
    {
        return this.gc;
    }

    /*
        Draw background
    */
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

    /*
        Draw entity on the windows
    */
    public void drawEntity(GraphicsContext gc, ArrayList<Entity> entityList)
    {
        for (Entity entity : entityList) {
            gc.drawImage(new Image(entity.getImagePath()), entity.position.x * controller.SQUARE_SIZE, entity.position.y * controller.SQUARE_SIZE, controller.SQUARE_SIZE * entity.getWidth(), controller.SQUARE_SIZE * entity.getWidth());
            
            // if (entity instanceof Collector)
            // {
            //     for (Position position : entity.getAccessibleAdjacentPositions())
            //     {
            //         gc.setFill(Color.web("FFFFFF"));
            //         gc.fillRoundRect(position.x * SQUARE_SIZE, position.y * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 35, 35);
            //     }
            // }

            /*
            // SHOW TREE ACCESSIBLE POSITION
            if (entity instanceof Tree)
            {
                for (Position position : entity.getAccessibleAdjacentPositions())
                {
                    gc.setFill(Color.web("FF0000"));
                    gc.fillRoundRect(position.x * SQUARE_SIZE, position.y * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 35, 35);
                }
            }
            */

            // SHOW PIKEMEN ACCESSIBLE POSITION
            if (entity instanceof Pikemen)
            {
                for (Position position : entity.getAccessibleAdjacentPositions())
                {
                    gc.setFill(Color.web("FF00FF"));
                    gc.fillRoundRect(position.x * controller.SQUARE_SIZE, position.y * controller.SQUARE_SIZE, controller.SQUARE_SIZE - 1, controller.SQUARE_SIZE - 1, 35, 35);
                }
            }

        }
    }
}

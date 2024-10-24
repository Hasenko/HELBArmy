import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class View {
    public final int WIDTH = 800;
    public final int HEIGHT = WIDTH;
    public final int ROWS = 20;
    public final int COLUMNS = ROWS;
    public final int SQUARE_SIZE = WIDTH / ROWS;

    /*
        Draw background
    */
    public void drawBackground(GraphicsContext gc)
    {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("AAD751"));
                } else {
                    gc.setFill(Color.web("A2D149"));
                }
                gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    /*
        Draw entity on the windows
    */
    public void drawEntity(GraphicsContext gc, ArrayList<Entity> entityList)
    {
        for (Entity entity : entityList) {
            gc.drawImage(new Image(entity.getImagePath()), entity.x * SQUARE_SIZE, entity.y * SQUARE_SIZE, SQUARE_SIZE * entity.getWidth(), SQUARE_SIZE * entity.getWidth());
        }
    }
}

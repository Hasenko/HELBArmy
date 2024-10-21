import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class View {
    public final int WIDTH = 800; //
    public final int HEIGHT = WIDTH; //
    public final int ROWS = 20; //
    public final int COLUMNS = ROWS; //
    public final int SQUARE_SIZE = WIDTH / ROWS; //



    public View()
    {
        
    }

    public void drawBackground(GraphicsContext gc) //
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

    public void drawEntity(GraphicsContext gc, ArrayList<Entity> entityList) //
    {
        for (Entity entity : entityList) {
            // draw image here
            gc.drawImage(new Image(entity.getImagePath()), entity.getX() * SQUARE_SIZE, entity.getY() * SQUARE_SIZE, SQUARE_SIZE * entity.WIDTH, SQUARE_SIZE * entity.WIDTH);
        }
    }
}

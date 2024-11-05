import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class View {
    private final HELBArmy CONTROLER;
    public final int WIDTH = 800;
    public final int HEIGHT = WIDTH;

    public View(HELBArmy controler) {
        this.CONTROLER = controler;
    }

    /*
        Draw background
    */
    public void drawBackground(GraphicsContext gc)
    {
        for (int i = 0; i < CONTROLER.ROWS; i++) {
            for (int j = 0; j < CONTROLER.COLUMNS; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("AAD751"));
                } else {
                    gc.setFill(Color.web("A2D149"));
                }
                gc.fillRect(i * CONTROLER.SQUARE_SIZE, j * CONTROLER.SQUARE_SIZE, CONTROLER.SQUARE_SIZE, CONTROLER.SQUARE_SIZE);
            }
        }
    }

    /*
        Draw entity on the windows
    */
    public void drawEntity(GraphicsContext gc, ArrayList<Entity> entityList)
    {
        for (Entity entity : entityList) {
            gc.drawImage(new Image(entity.getImagePath()), entity.position.x * CONTROLER.SQUARE_SIZE, entity.position.y * CONTROLER.SQUARE_SIZE, CONTROLER.SQUARE_SIZE * entity.getWidth(), CONTROLER.SQUARE_SIZE * entity.getWidth());
            
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

        }
    }
}

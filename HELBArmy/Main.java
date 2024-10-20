import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = WIDTH;
    private static final int ROWS = 20;
    private static final int COLUMNS = ROWS;
    private static final int SQUARE_SIZE = WIDTH / ROWS;
    private static final String[] FOODS_IMAGE = new String[]{"/assets/food/ic_orange.png", "/assets/food/ic_apple.png"};

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;

    private GraphicsContext gc;
    private Point snake;
    private Image foodImage;
    private int foodX;
    private int foodY;
    private boolean gameOver;
    private int score = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Snake");
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        gc = canvas.getGraphicsContext2D();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode code = event.getCode();
                if (code == KeyCode.RIGHT || code == KeyCode.D) {
                    // MOVE RIGHT
                    moveRight();
                } else if (code == KeyCode.LEFT || code == KeyCode.Q) {
                    // MOVE LEFT
                    moveLeft();
                } else if (code == KeyCode.UP || code == KeyCode.Z) {
                    // MOVE UP
                    moveUp();
                } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                    // MOVE DOWN
                    moveDown();
                }
            }
        });

        snake = new Point(5, ROWS / 2);
        generateFood();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(130), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void run(GraphicsContext gc) {
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7", 70));
            gc.fillText("Game Over", WIDTH / 3.5, HEIGHT / 2);
            return;
        }
        drawBackground(gc);
        drawFood(gc);
        drawSnake(gc);
        drawScore();

        gameOver();
        eatFood();

        System.out.println("Snake X : " + snake.getX());
        System.out.println("Snake Y : " + snake.getY());

        System.out.println("--------------------------");

        System.out.println("Food X : " + foodX);
        System.out.println("Food Y : " + foodY);

        moveSnakeToFood();
    }

    private void moveSnakeToFood()
    {
        // DIAGONAL
        
        if (snake.getX() < foodX)
        {
            moveRight();
        }
        else if (snake.getX() > foodX)
        {
            moveLeft();
        }

        if (snake.getY() < foodY)
        {
            moveDown();
        }
        else if (snake.getY() > foodY)
        {
            moveUp();
        } 
        

        // X THEN Y

        /*
        if (snake.getX() < foodX)
        {
            moveRight();
        }
        else if (snake.getX() > foodX)
        {
            moveLeft();
        }

        if (snake.getX() == foodX)
        {
            if (snake.getY() < foodY)
            {
                moveDown();
            }
            else if (snake.getY() > foodY)
            {
                moveUp();
            }
        }
        
        */
    }

    private double distance (int x1, int y1, int x2, int y2)
    {
        return Math.sqrt((Math.pow(x2, y2)));
    }

    private void drawBackground(GraphicsContext gc) {
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

    private void generateFood() {
        start:
        while (true) {
            foodX = (int) (Math.random() * ROWS);
            foodY = (int) (Math.random() * COLUMNS);

            if (snake.getX() == foodX && snake.getY() == foodY) {
                continue start;
            }
            foodImage = new Image(FOODS_IMAGE[(int) (Math.random() * FOODS_IMAGE.length)]);
            break;
        }
    }

    private void drawFood(GraphicsContext gc) {
        gc.drawImage(foodImage, foodX * SQUARE_SIZE, foodY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    private void drawSnake(GraphicsContext gc) {
        gc.setFill(Color.web("4674E9"));
        gc.fillRoundRect(snake.getX() * SQUARE_SIZE, snake.getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 35, 35);
    }

    private void moveRight() {
        snake.x++;
    }

    private void moveLeft() {
        snake.x--;
    }

    private void moveUp() {
        snake.y--;
    }

    private void moveDown() {
        snake.y++;
    }

    public void gameOver() {
        if (snake.x < 0 || snake.y < 0 || snake.x * SQUARE_SIZE >= WIDTH || snake.y * SQUARE_SIZE >= HEIGHT) {
            gameOver = true;
        }
    }

    private void eatFood() {
        if (snake.getX() == foodX && snake.getY() == foodY) {
            generateFood();
            score += 5;
        }
    }

    private void drawScore() {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Digital-7", 35));
        gc.fillText("Score: " + score, 10, 35);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

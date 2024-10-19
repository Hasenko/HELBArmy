// Test 01:17 20-10-2024

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
import java.util.Date;

public class Main extends Application {

    private static final int WIDTH = 600;
    private static final int HEIGHT = WIDTH;
    private static final int ROWS = 20;
    private static final int COLUMNS = ROWS;
    private static final int SQUARE_SIZE = WIDTH / ROWS;
    private static final String[] FOODS_IMAGE = new String[]{"/img/ic_orange.png", "/img/ic_apple.png"};

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;

    private static final int TROLL_TIME = 10000; // in milisecond (second * 1000)

    private GraphicsContext gc;
    private List<Point> snakeBody = new ArrayList();
    private Point snakeHead;
    private Image foodImage;
    private int foodX = -10;
    private int foodY = -10;
    private boolean gameOver;
    private int currentDirection;
    private int score = 0;

    private Image hpImage;
    private int hpX = -10;
    private int hpY = -10;
    private int cptHp = 0;
    private boolean canSpawnHP = true;
    private int nbHp = 1;

    private Image poisonImage;
    private int poisonX = -10;
    private int poisonY = -10;

    private Image trollImage;
    private int trollX = -10;
    private int trollY = -10;
    private int cptTroll = 0;
    private boolean canSpawnTroll = false;
    private boolean keyTrolled = false;
    private long resetTime = 0;

    private int refreshRate = 80;
    private Timeline timeline;

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
                if (!keyTrolled)
                {
                    if (code == KeyCode.RIGHT || code == KeyCode.D) {
                        if (currentDirection != LEFT) {
                            currentDirection = RIGHT;
                        }
                    } else if (code == KeyCode.LEFT || code == KeyCode.Q) {
                        if (currentDirection != RIGHT) {
                            currentDirection = LEFT;
                        }
                    } else if (code == KeyCode.UP || code == KeyCode.Z) {
                        if (currentDirection != DOWN) {
                            currentDirection = UP;
                        }
                    } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                        if (currentDirection != UP) {
                            currentDirection = DOWN;
                        }
                    }
                }
                else
                {
                    if (code == KeyCode.LEFT || code == KeyCode.Q) {
                        if (currentDirection != LEFT) {
                            currentDirection = RIGHT;
                        }
                    } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
                        if (currentDirection != RIGHT) {
                            currentDirection = LEFT;
                        }
                    } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                        if (currentDirection != DOWN) {
                            currentDirection = UP;
                        }
                    } else if (code == KeyCode.UP || code == KeyCode.Z) {
                        if (currentDirection != UP) {
                            currentDirection = DOWN;
                        }
                    }
                }
                
            }
        });

        for (int i = 0; i < 3; i++) {
            snakeBody.add(new Point(5, ROWS / 2));
        }
        snakeHead = snakeBody.get(0);
        generateFood();
        generateHp();

        timeline = new Timeline(new KeyFrame(Duration.millis(refreshRate), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void run(GraphicsContext gc) {
        if (nbHp <= 0) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7", 70));
            gc.fillText("Game Over", WIDTH / 3.5, HEIGHT / 2);
            return;
        }
        drawBackground(gc);
        drawArt(gc);

        for (int i = snakeBody.size() - 1; i >= 1; i--) {
            snakeBody.get(i).x = snakeBody.get(i - 1).x;
            snakeBody.get(i).y = snakeBody.get(i - 1).y;
        }

        switch (currentDirection) {
            case RIGHT:
                moveRight();
                break;
            case LEFT:
                moveLeft();
                break;
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
        }

        gameOver();
        eatFood();
        // System.out.println("cptHp : " + cptHp);
        // System.out.println("cptTroll : " + cptTroll);
        System.out.println("refresh rate : " + timeline.getRate());
        if (keyTrolled)
        {
            System.out.println("current time : " + getCurrentTime());
            System.out.println("reset time : " + resetTime);
            if (getCurrentTime() >= resetTime)
                changeKey();
        }
    }

    private void drawBackground(GraphicsContext gc) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
            	if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("AAD751")); // 000000
                } else {
                    gc.setFill(Color.web("A2D149")); // FFFFFF
                }
                gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    private void generateFood() {
        System.out.println("Generating food");
        start:
        while (true) {
            foodX = (int) (Math.random() * ROWS);
            foodY = (int) (Math.random() * COLUMNS);

            for (Point snake : snakeBody) {
                if (snake.getX() == foodX && snake.getY() == foodY) {
                    continue start;
                }
            }
            foodImage = new Image(FOODS_IMAGE[(int) (Math.random() * FOODS_IMAGE.length)]);
            break;
        }
    }

    private void generateHp()
    {
        System.out.println("Generating hp");
        while (true) {
            hpX = (int) (Math.random() * ROWS);
            hpY = (int) (Math.random() * COLUMNS);

            for (Point snake : snakeBody) {
                if (snake.getX() == hpX && snake.getY() == hpY) {
                    continue;
                }
            }
            hpImage = new Image("/img/hearth.png");
            break;
        }
    }

    private void generatePoison()
    {
        System.out.println("Generating poison");
        while (true) {
            poisonX = (int) (Math.random() * ROWS);
            poisonY = (int) (Math.random() * COLUMNS);

            for (Point snake : snakeBody) {
                if (snake.getX() == poisonX && snake.getY() == poisonY) {
                    continue;
                }
            }
            poisonImage = new Image("/img/poison.png");
            break;
        }
    }

    private void generateTroll()
    {
        System.out.println("Generating troll");
        while (true) {
            trollX = (int) (Math.random() * ROWS);
            trollY = (int) (Math.random() * COLUMNS);

            for (Point snake : snakeBody) {
                if (snake.getX() == trollX && snake.getY() == trollY) {
                    continue;
                }
            }

            trollImage = new Image("/img/troll.png");
            break;
        }
    }

    private void drawArt(GraphicsContext gc)
    {
        // SCORE 
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Digital-7", 35));
        gc.fillText("Score: " + score, 10, 35);

        // FOOD
        gc.drawImage(foodImage, foodX * SQUARE_SIZE, foodY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        gc.drawImage(hpImage, hpX * SQUARE_SIZE, hpY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        gc.drawImage(poisonImage, poisonX * SQUARE_SIZE, poisonY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
        gc.drawImage(trollImage, trollX * SQUARE_SIZE, trollY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

        // SNAKE
        gc.setFill(Color.web("4674E9"));
        gc.fillRoundRect(snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 35, 35);

        for (int i = 1; i < snakeBody.size(); i++) {
            gc.fillRoundRect(snakeBody.get(i).getX() * SQUARE_SIZE, snakeBody.get(i).getY() * SQUARE_SIZE, SQUARE_SIZE - 1,
                    SQUARE_SIZE - 1, 20, 20);
        }
    }

    private void moveRight() {
        if (snakeHead.x == COLUMNS)
            snakeHead.x = 0;
        else
            snakeHead.x++;
    }

    private void moveLeft() {
        if (snakeHead.x == 0)
            snakeHead.x = COLUMNS;
        else
            snakeHead.x--;
    }

    private void moveUp() {
        if (snakeHead.y == 0)
            snakeHead.y = ROWS;
        else
            snakeHead.y--;
    }

    private void moveDown() {
        if (snakeHead.y == ROWS)
            snakeHead.y = 0;
        else
            snakeHead.y++;
    }

    public void gameOver() {
        for (int i = 1; i < snakeBody.size(); i++) {
            if (snakeHead.x == snakeBody.get(i).getX() && snakeHead.getY() == snakeBody.get(i).getY()) {
                nbHp --;
                break;
            }
        }
    }

    private void eatFood() {
        // FOOD
        if (snakeHead.getX() == foodX && snakeHead.getY() == foodY) {
            snakeBody.add(new Point(-1, -1));
            System.out.println("Eating food");
            generateFood();
            generatePoison();
            score += 5;
            if (!canSpawnHP)
                cptHp++;
            if (cptHp != 0 && cptHp%5==0)
            {
                canSpawnHP = true;
                generateHp();
            }

            if (!canSpawnTroll)
                cptTroll++;
            if (cptTroll != 0 && cptTroll%10==0)
            {
                canSpawnTroll = true;
                generateTroll();
            }
            timeline.setRate(timeline.getRate() + timeline.getRate() / 10);


        }

        // HP
        if (snakeHead.getX() == hpX && snakeHead.getY() == hpY) {
            System.out.println("Eating hp");
            hpX = -10;
            hpY = -10;
            canSpawnHP=false;
            nbHp++;
        }

        // POISON
        if (snakeHead.getX() == poisonX && snakeHead.getY() == poisonY) {
            System.out.println("Eating poison");
            poisonX = -10;
            poisonY = -10;
            nbHp--;
        }

        // TROLL
        if (snakeHead.getX() == trollX && snakeHead.getY() == trollY) {
            System.out.println("Eating troll");
            trollX = -10;
            trollY = -10;
            canSpawnTroll = false;

            keyTrolled = true;
            System.out.println("Starting timer");

            resetTime = getCurrentTime() + TROLL_TIME;
        }

    }

    private long getCurrentTime()
    {
        return new Date().getTime();
    }

    private void changeKey()
    {
        keyTrolled = false;
        System.out.println("End of timer");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

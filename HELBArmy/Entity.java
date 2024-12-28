import java.util.ArrayList;
import java.util.Map;

public abstract class Entity {
    private final static int DEFAULT_WIDTH = 1;
    protected final int DEFAULT_ADJACENT_RADIUS = 1;

    private String side;
    private int width;
    private String imagePath;

    protected HELBArmy gameBoard;
    
    public Position position;
    
    public Entity(Position position, String imagePath, HELBArmy gameBoard)
    {
        this(position, "neutral", DEFAULT_WIDTH, imagePath, gameBoard);
    }

    public Entity(Position position, String side, String imagePath, HELBArmy gameBoard)
    {
        this(position, side, DEFAULT_WIDTH, imagePath, gameBoard);
    }

    public Entity(Position position, String side, int width, String imagePath, HELBArmy gameBoard)
    {
        this.position = position;
        setSide(side);
        this.width = width;
        setImagePath(imagePath);
        this.gameBoard = gameBoard;
    }

    public String getSide()
    {
        return side;
    }

    public int getWidth()
    {
        return width;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    /*
        get an array of position in a certain radius of caller
        > ex with radius = 3
        >
        >   * * * * * * * * * * *
        >   * * * * * * * * * * *
        >   * * / / / / / / / * *
        >   * * / / / / / / / * *
        >   * * / / / / / / / * *
        >   * * / / / 0 / / / * *
        >   * * / / / / / / / * *
        >   * * / / / / / / / * *
        >   * * / / / / / / / * *
        >   * * * * * * * * * * *
        >   * * * * * * * * * * *
    */
    protected ArrayList<Position> getPositionsInRadius(int radius)
    {
        ArrayList<Position> resultList = new ArrayList<>();

        for(int i = this.position.x-radius ;  i <= this.position.x+radius ; i++)
        {
            for(int j = this.position.y-radius ;  j <= this.position.y+radius ; j++)
            {
                if(!(i == this.position.x && j == this.position.y))
                {
                    resultList.add(new Position(i, j));
                }
            }     
        }

        return resultList;
    }

    // get an array of adjacent position of caller
    protected ArrayList<Position> getAdjacentPositions()
    {
        return getPositionsInRadius(DEFAULT_ADJACENT_RADIUS);
    }

    // method to know if caller is adjacent to a specified entity
    protected boolean isAdjacentToEntity(Entity entity)
    {
        return entity.getAdjacentPositions().contains(this.position);
    }

    // method to adjacent position where caller can move
    protected ArrayList<Position> getAccessibleAdjacentPositions()
    {
        ArrayList<Position> resultList = new ArrayList<Position>();
        ArrayList<Position> adjacentPositionsList = getAdjacentPositions();

        adjacentPositionLoop:
        for (Position adjacent : adjacentPositionsList)
        {
            if(Board.isPositionInBoard(adjacent))
            {

                for (Map.Entry<String, City> entry : gameBoard.citiesMap.entrySet())
                {
                    City city = entry.getValue();

                    if (city.getPositions().contains(adjacent))
                    {
                        continue adjacentPositionLoop;
                    }
                }

                for (Entity entity : gameBoard.entityList)
                {
                    if (entity.position.equals(adjacent) && !gameBoard.collectablesList.contains(entity))
                    {
                        continue adjacentPositionLoop;
                    }
                }

                resultList.add(adjacent);
            }
        }

        return resultList;
    }

    // method to get the nearest entity from caller from a list given
    protected Entity getNearestEntity(ArrayList<Entity> entityList)
    {
        if (entityList.size() == 0) return null;

        Entity nearestEntity = entityList.get(0);
        double minDistance = Double.MAX_VALUE;

        for (Entity entity : entityList)
        {
            if (entity != null)
            {
                double currentDistance = Board.getDistance(entity.position, this.position);

                if (currentDistance <= minDistance)
                {
                    minDistance = currentDistance;
                    nearestEntity = entity;
                }
            }
        }
        return nearestEntity;
    }

    // method called when an entity must be destroyed from the game
    protected void destroy()
    {
        gameBoard.removeNext(this);
    }

    // called by entity constructor to separate attributs logics
    private void setSide(String side)
    {
        if (side.equals("north") || side.equals("south") || side.equals("neutral"))
            this.side = side;
        else
            throw new IllegalArgumentException("side must be : north - south - neutral");
    }

    // called by entity constructor to separate attributs logics
    private void setImagePath(String imagePath)
    {
        if (!imagePath.startsWith("assets/"))
            throw new IllegalArgumentException(imagePath + " | Base directory must be : assets/");
        this.imagePath = imagePath;
    }

    @Override
    public String toString()
    {
        return position.toString();
    }
}

public abstract class Collectable extends Entity {

    public Collectable(Position position, String imagePath, HELBArmy gameBoard)
    {
        super(position, imagePath, gameBoard);
    }

    protected abstract void onCollectAction(MovableEntity collectorUnity);
}
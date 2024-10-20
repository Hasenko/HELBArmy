public abstract class MovableEntity extends Entity {
    public MovableEntity(int x, int y, String side)
    {
        super(x, y, side);
    }

    protected void moveRight()
    {
        this.x++;
    }

    protected void moveLeft()
    {
        this.x--;
    }

    protected void moveUp()
    {
        this.y--;
    }

    protected void moveDown()
    {
        this.y++;
    }

}
public abstract class MovableEntity extends Entity {
    /*
        Collecteur PV 50 Attaque 5
        Déserteur PV 125 Attaque 10 x1,5 contre les piquiers
        Cavalier PV 200 Attaque 10 x2 contre les déserteurs
        Piquier PV 175 Attaque 15 x3 contre les cavaliers
    */

    private int hp;
    private int damage;
    private int attackMultiplicator;

    public MovableEntity(int x, int y, String side, String imagePath, HELBArmy gameBoard, int hp, int damage)
    {
        this(x, y, side, imagePath, gameBoard, hp, damage, 1);
    }

    public MovableEntity(int x, int y, String side, String imagePath, HELBArmy gameBoard, int hp, int damage, int attackMultiplicator)
    {
        super(x, y, side, imagePath, gameBoard);
        this.hp = hp;
        this.damage = damage;
        this.attackMultiplicator = attackMultiplicator;
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

    protected void hit(Entity entity)
    {
        System.out.println("Hitting : " + entity);
    }
}
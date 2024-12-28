public class PhilosophersStone extends Collectable {
    
    public PhilosophersStone(Position position, HELBArmy gameBoard)
    {
        super(position, "assets/special/philosophers_stone.png", gameBoard);
    }

    @Override
    protected void onCollectAction(MovableEntity collectorUnity) {
        int nb = (int) (Math.random() * 2);

        if (nb == 0) // make unity invinsible
        {
            collectorUnity.makeInvincible();
            System.out.println(collectorUnity + " is now invincible");
        }
        else // kill unity
        {
            collectorUnity.destroy();
            System.out.println(collectorUnity + " is now dead because of philo stone");
        }

        destroy();
    }
}

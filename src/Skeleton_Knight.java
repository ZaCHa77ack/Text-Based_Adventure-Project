public class Skeleton_Knight extends Enemy
{
    public Skeleton_Knight(Adventure_Game adventure)
    {
        super("Skeleton", 60, 10, 15, 20, 70, 50, adventure, "A bag of bones, " +
                "reanimated due to a necromancer's influence. It's more durable than it looks.");
        this.armor = 20;
        addAction(new EnemyAttack("swings its sword", 1.5, 70, adventure));
        addAction(new EnemyAttack("bashes you with its shield", 0.75, 30, adventure));
        addAction(new EnemyWasteTurn("pulls out a set of xylophone mallets and begins playing a tune", 20, adventure));
        addAction(new EnemyWasteTurn("creaks its joints", 15, adventure));
    }
}

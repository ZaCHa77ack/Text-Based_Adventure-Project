public class Goblin extends Enemy
{
    public Goblin(Adventure_Game adventure)
    {
        super("Goblin", 20, 2, 5, 2, 85,25, adventure, "Small, green and really annoying");
        this.armor = 2;
        addAction(new EnemyAttack("swings its club", 1, 80, adventure));
        addAction(new EnemyWasteTurn("laughs excitedly", 15, adventure));
        addAction(new EnemyAttack("kicks you in the crotch", 6, 1, adventure));
        addAction(new EnemyAttack("throws a rock", 0.5, 29, adventure));
    }
}

public class Cultist extends Enemy
{
    // Cultists enemy and the parameters that define it.
    public Cultist(Adventure_Game adventure)
    {
        super("Cultist", 40, 5, 7, 10, 90, 40, adventure, "Your generic robe-clad bozo.");
        this.armor = 10;
        addAction(new EnemyAttack("swings its mace",1,66, adventure));
        addAction(new EnemyWasteTurn("chants mindlessly", 14, adventure));
        addAction(new EnemyAttack("swings with all their might", 2, 20, adventure));
        addAction(new EnemyAttack("casts a dark spell", 3, 30, adventure));
    }
}

public class Minotaur extends Enemy
{
    public Minotaur
            (Adventure_Game adventure)
    {
        super("Karovard", 250, 17, 27, 30, 95, 0, adventure,
                "A powerful minotaur seeking to take over the world.");
        this.armor = 30;
        addAction(new EnemyAttack("gores you with his horns", 1, 55, adventure));
        addAction(new EnemyAttack("delivers a crushing axe swing", 2, 30, adventure));
        addAction(new EnemyWasteTurn("snorts loudly", 7, adventure));
        addAction(new EnemyAttack("delivers a swift punch", 0.75, 38, adventure));
    }
}

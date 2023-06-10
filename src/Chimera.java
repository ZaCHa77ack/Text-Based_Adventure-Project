public class Chimera extends Enemy
{
    public Chimera(Adventure_Game adventure)
    {
        super("Chimera", 60, 10, 15, 20, 70, 50, adventure, "An amalgamation, " +
                "of several dangerous animals, the very sight of their unholy creation" +
                "is made more visible with the staples and magic glyphs that seem to have resurrected it");
        this.armor = 20;
        addAction(new EnemyAttack("gnashes your arm with its many fangs", 1, 60, adventure));
        addAction(new EnemyAttack("strikes you with its razor-sharp tail", .80, 40, adventure));
        addAction(new EnemyAttack("unleashes a sundering arcane howl", 5, 1, adventure));
        addAction(new EnemyWasteTurn("lets out a hiss and a roar simultaneously", 20, adventure));
        addAction(new EnemyWasteTurn("stares at you menacingly", 15, adventure));
        addItem(new Health_Potion("Health Potion", 150, "Restores 150 health"));
    }
}



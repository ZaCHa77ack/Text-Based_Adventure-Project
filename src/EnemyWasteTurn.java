public class EnemyWasteTurn implements EnemyAction
{

    private final Adventure_Game adventure;
    private final int weight;
    private final String description;
    public EnemyWasteTurn (String description, int weight, Adventure_Game adventure)
    {
        this.description = description;
        this.weight = weight;
        this.adventure = adventure;
    }
    @Override
    public void execute(Enemy enemy, Player player)
    {
        adventure.displayText("The " + enemy.getName() + " " + getDescription() + ".");
    }

    public String getDescription()
    {
        return description;
    }
}

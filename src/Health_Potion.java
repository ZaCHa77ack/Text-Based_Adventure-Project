public class Health_Potion extends Item implements Item_Effect
{
    private int healingAmount;

    public Health_Potion(String name, int healingAmount, String description)
    {
        super(name, description);
    }
    @Override
    public void use(Player player, Adventure_Game adventure)
    {
        player.heal(healingAmount);
        adventure.displayText("You used the " + this.getName() + ".");
    }
}

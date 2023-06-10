
// Weapon class that is a child of the Item class.
public class Weapon extends Item
{
    private int damageModifier;

    public Weapon(String name, String description, int damageModifier)
    {
        super(name, description);
        this.damageModifier = damageModifier;
    }

    // Used to retrieve the damage modifier parameter of the weapon to apply to the player's damage range.
    public int getDamageModifier()
    {
        return damageModifier;
    }
}

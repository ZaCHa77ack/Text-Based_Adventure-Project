
// Armor class that is a child of the Item class.
public class Armor extends Item
{
    private int armorValue;

    public Armor(String name, String description, int armorValue)
    {
        super(name, description);
        this.armorValue = armorValue;
    }

    public int getArmorValue()
    {
        return armorValue;
    }

    public void setArmorValue()
    {
        this.armorValue = armorValue;
    }
}

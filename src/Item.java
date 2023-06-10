public class Item implements Item_Effect
{
    private String name;
    private String description;
    private boolean equipped;

    public Item(String name, String description)
    {
        this.name = name;
        this.description = description;
        this.equipped = false;
    }

    // Return the name of the item.
    public String getName()
    {
        return name;
    }

    // Return the description of the item.
    public String getDescription()
    {
        return description;
    }

    @Override
    public void use(Player player, Adventure_Game adventure)
    {

    }
}

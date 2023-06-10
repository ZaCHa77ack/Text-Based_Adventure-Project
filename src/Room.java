import java.util.*;

public class Room
{
    private String description;
    private List<Item> items;
    private List<Enemy> enemies;

    private Map<Adventure_Game.Command, Room> exits;

    private String name;

    public Room(String name, String description)
    {
        this.description = description;
        this.items = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.exits = new HashMap<>();
        this.name = name;

    }

    public String getDescription()
    {
        return description;
    }

    public List<Item> getItems()
    {
        return items;
    }

    public void addItem(Item item)
    {
        items.add(item);
    }

    public void addEnemy(Enemy enemy)
    {
        this.enemies.add(enemy);
    }

    public List<Enemy> getEnemies()
    {
        return enemies;
    }

    public boolean hasEnemies()
    {
        return !enemies.isEmpty();
    }

    public Item findItemByName(String itemName)
    {
        for (Item item : items)
        {
            if (item.getName().equalsIgnoreCase(itemName))
            {
                return item;
            }
        }
        return null;
    }

    public void removeItem(Item item)
    {
        items.remove(item);
    }

    public String getName()
    {
        return name;
    }

    public void addExit(Adventure_Game.Command direction, Room room)
    {
        exits.put(direction, room);
    }

    public List<String> getExits()
    {
        List<String> exitsList = new ArrayList<>();
        for (Map.Entry<Adventure_Game.Command, Room> exit : exits.entrySet())
        {
            Adventure_Game.Command direction = exit.getKey();
            if (exit.getValue() != null)
            {
                exitsList.add(direction.toString());
            }
        }
        return exitsList;
    }

    public Room getExit(Adventure_Game.Command direction)
    {
        return exits.get(direction);
    }

    public void removeEnemy(Enemy enemy)
    {
        enemies.remove(enemy);
    }

    public List<Enemy> findEnemyByName(String enemyName)
    {
        List<Enemy> foundEnemies = new ArrayList<>();
        for (Enemy enemy : enemies)
        {
            if (enemy.getName().equalsIgnoreCase(enemyName))
            {
                foundEnemies.add(enemy);
            }
        }
        return foundEnemies;
    }
}


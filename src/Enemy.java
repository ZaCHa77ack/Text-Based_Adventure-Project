import java.util.*;

// Enemy class to be used by subclasses via inheritance.
public abstract class Enemy
{
    protected final String name;
    protected int health;
    protected final int minDamage;
    protected final int maxDamage;
    protected int armor;
    protected final List<EnemyAction> actions;

    protected final String description;
    protected final List<Item> itemDrops;
    protected final int accuracy;
    protected int experienceValue;

    // Enemy constructor method
    public Enemy(String name, int health, int minDamage, int maxDamage, int armor, int accuracy, int experienceValue, Adventure_Game adventure, String description)
    {
        this.name = name;
        this.health = health;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.armor = armor;
        this.actions = new ArrayList<>();
        this.description = description;
        this.itemDrops = new ArrayList<>();
        this.accuracy = accuracy;
        this.experienceValue = experienceValue;
    }
    public String getName()
    {
        return name;
    }

    public int getHealth()
    {
        return health;
    }

    /*
    public int getMaxHealth()
    {
        return maxHealth;
    }

    public void setHealth(int health)
    {
        this.health = health;
    }

    public int getDamage()
    {
        Random random = new Random();
        return random.nextInt(maxDamage - minDamage + 1) + minDamage;
    }
     */

    // Get the minimum damage for the enemy.
    public int getMinDamage()
    {
        return minDamage;
    }

    // Get the maximum damage for the enemy.
    public int getMaxDamage()
    {
        return maxDamage;
    }

    // Get the armor value for the enemy.
    public int getArmor()
    {
        return armor;
    }

    // Reduce the enemy's health when an attack lands.
    public int takeDamage(int damage)
    {
        double damageReduction = (double) this.armor / 100;
        int effectiveDamage = (int) Math.floor(damage * (1 - damageReduction));
        this.health = Math.max(this.health - effectiveDamage, 0);
        if (health < 0)
        {
            health = 0;
        }
        return effectiveDamage;
    }

    // Add action(s) for the enemy to be able to do.
    // For now, they are limited to various attacks and wasting their turn.
    public void addAction(EnemyAction action)
    {
        this.actions.add(action);
    }

    // Choose a random action from the actions assigned to the enemy.
    // Weight determines the probability of an action being selected.
    public EnemyAction chooseAction()
    {
        Random random = new Random();
        int totalWeight = actions.size();
        int randomValue = random.nextInt(totalWeight);

        return actions.get(randomValue);
    }

    // Add items to the enemy for drop purposes.
    public void addItem(Item item)
    {
        itemDrops.add(item);
    }

    public List<Item> dropItems()
    {
        List<Item> droppedItems = new ArrayList<>(itemDrops);
        itemDrops.clear();
        return droppedItems;
    }

    // Check to see if the enemy's health is above 0.
    public boolean isAlive()
    {
        return health > 0;
    }

    // Get the description for the enemy archetype.
    public String getDescription()
    {
        return description;
    }

    // Roll the enemy's attack. If the number rolled is <= their base accuracy, the attack hits.
    public boolean attackHits(int accuracy)
    {
        Random rand = new Random();
        int rollToHit = rand.nextInt(100) + 1;
        return rollToHit <= accuracy;
    }

    public int getAccuracy()
    {
        return accuracy;
    }

    public int getExperienceValue()
    {
        return experienceValue;
    }

    public void setExperienceValue(int experienceValue)
    {
        this.experienceValue = experienceValue;
    }
}

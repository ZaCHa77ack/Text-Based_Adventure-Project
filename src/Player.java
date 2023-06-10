import java.util.*;

// Player class that is used to declare the parameters of the player, such as Name, HP, Damage(Attack), Armor,
// Inventory, and Accuracy
public class Player
{
    // All the required variables for this class.
    private Weapon equippedWeapon;
    private int health;
    private int maxHealth;
    private String name;
    private int minDamage;
    private int maxDamage;
    private Armor equippedArmor;
    private int armor;
    private int accuracy;
    private Room currentRoom;
    private List<Item> inventory;
    private int remainingDefenseTurns;
    private boolean defending;
    private int experience;
    private int level;
    private int experienceToNextLevel;

    // Constructor method for the Player object.
    // Players require the following arguments: name, level, health, minDamage, maxDamage, armor, accuracy, inventory
    public Player(String name, int level, int health, int minDamage, int maxDamage, int armor, int accuracy, List<Item> inventory)
    {
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.armor = armor;
        this.accuracy = accuracy;
        this.inventory = new ArrayList<>();
        this.experienceToNextLevel = 100;
        this.level = 1;

        // Give the player some default gear.
        Health_Potion health_potion = new Health_Potion("Health Potion", 150, "Restores 150 health");
        Weapon ironSword = new Weapon("Iron Sword", "Your trusty iron sword. It's seen many a battle.",
                            5);
        Armor chainMail = new Armor("Chainmail", "A standard set of chainmail", 10);

        addItemToInventory(health_potion);
        addItemToInventory(ironSword);
        addItemToInventory(chainMail);
        equipArmor(chainMail);
        equipWeapon(ironSword);
    }

    // The methods listed below are straightforward in what they do. They each return the appropriate parameters of the player.
    public int getHealth()
    {
        return health;
    }
    public int getMaxHealth()
    {
        return maxHealth;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getArmor()
    {
        return armor;
    }

    // Allows the player to equip armor and prevents them from equipping the armor again if it is already equipped.
    public String equipArmor(Armor armorItem)
    {
        if (armorItem == null)
        {
            return armorItem.getName() + " is not in your inventory.";
        }
        if (equippedArmor == armorItem)
        {
            return armorItem.getName() + " is already equipped.";
        }
        else
        {
            // If the player already has armor equipped, unequip it and subtract its armor value from the player's
            // current armor value, then equip the new armor and apply the armor value. This is to prevent armor values
            // from stacking infinitely.
            if (equippedArmor != null)
            {
                this.armor -= equippedArmor.getArmorValue();
            }

            this.equippedArmor = armorItem;
            this.armor += armorItem.getArmorValue();
            return armorItem.getName() + " equipped";
        }
    }

    // allows the player to unequip armor and prevents the player from unequipping armor that is not equipped.
    public String unequipArmor(Armor armorItem)
    {
        if (armorItem == null)
        {
            return armorItem.getName() + " is not in your inventory.";
        }
        if (equippedArmor != armorItem)
        {
            return armorItem.getName() + " is not equipped.";
        }
        this.armor -= armorItem.getArmorValue();
        this.equippedArmor = null;
        return armorItem.getName() + " unequipped.";
    }

    // Allows the player to equip a weapon, thereby modifying their damage. They are prevented from equipping
    // the same weapon if it is already equipped.
    public String equipWeapon(Weapon weaponItem)
    {
        if (weaponItem == null)
        {
            return weaponItem.getName() + " is not in your inventory.";
        }

        if (equippedWeapon == weaponItem)
        {
            return weaponItem.getName() + " is already equipped.";
        }

        // If the player already has an equipped weapon, subtract the damage modifier before the applying the
        // new weapon's damage modifier. This is to prevent multiple damage modifiers from stacking infinitely.
        if (this.equippedWeapon != null)
        {
            this.minDamage -= this.equippedWeapon.getDamageModifier();
            this.maxDamage -= this.equippedWeapon.getDamageModifier();
        }

        this.equippedWeapon = weaponItem;
        this.minDamage = this.minDamage + weaponItem.getDamageModifier();
        this.maxDamage = this.maxDamage + weaponItem.getDamageModifier();
        return weaponItem.getName() + " equipped.";
    }

    // Allows the player to unequip their weapon. They cannot unequip a weapon that is not equipped.
    public String unequipWeapon(Weapon weaponItem)
    {
        if (weaponItem == null)
        {
            return weaponItem.getName() + " is not in your inventory.";
        }
        if (equippedWeapon != weaponItem)
        {
            return weaponItem.getName() + " is not equipped.";
        }

        this.minDamage -= weaponItem.getDamageModifier();
        this.maxDamage -= weaponItem.getDamageModifier();
        this.equippedWeapon = null;
        return weaponItem.getName() + " unequipped.";
    }

    // Planning to implement this later on to allow for some randomness in encounters.
    public int getAccuracy()
    {
        return accuracy;
    }

    // Adds item(s) to inventory
    public void addItemToInventory(Item item)
    {
        inventory.add(item);
    }

    // Removes item(s) from inventory
    public boolean removeItemFromInventory(Item item)
    {
        return inventory.remove(item);
    }

    // Returns the inventory when prompted.
    public List<Item> getInventory()
    {
        return inventory;
    }

    // Retrieves the player's equipped weapon.
    public Weapon getEquippedWeapon()
    {
        return equippedWeapon;
    }

    // Modifies the player's attack damage based on weapon.
    public int getModifiedAttackPower()
    {
        int baseDamage = new Random().nextInt(maxDamage - minDamage + 1) + minDamage;
        return baseDamage;
    }

    public Armor getEquippedArmor()
    {
        return equippedArmor;
    }

    // Used by the Health_Potion class to restore the player's health.
    public void heal(int amount)
    {
        health += amount;
        if (health > maxHealth)
        {
            health = maxHealth;
        }
    }

    public int takeDamage(int damage) {
        if (!defending) {
            double damageReduction = (double) getArmor() / 100;
            int effectiveDamage = (int) Math.floor(damage * (1 - damageReduction));
            this.health = Math.max(this.health - effectiveDamage, 0);
            return effectiveDamage;
        }
        else
        {
            defending = true;
            return 0;
        }
    }

    public boolean isAlive()
    {
        return health > 0;
    }

    // Attack the enemy and calculate the damage the attack does against the enemy based on their armor.
    // For example: 20 armor will provide a 20% damage resistance, rounded down.
    public void attack(Enemy currentEnemy, Adventure_Game adventure)
    {
        if (attackHits(getAccuracy()))
        {
            int damage = getModifiedAttackPower();
            int effectiveDamage = currentEnemy.takeDamage(damage);
            adventure.displayText("You attack the " + currentEnemy.getName() + " for " + effectiveDamage + " damage.\n");
        }
        else
        {
            adventure.displayText("The enemy dodged out of the way!\n");
        }
    }

    // If the player chooses to defend, all damage against them is nullified for the current turn.
    public void defend(Adventure_Game adventure, Enemy enemy)
    {
        defending = true;
        adventure.displayText("You take a defensive stance behind your shield.\n");
        setRemainingDefenseTurns(1);
    }
/*
    public void increaseArmor(int temporaryArmor)
    {
        this.temporaryArmor = temporaryArmor;
        setArmor(getArmor() + temporaryArmor);
    }

    public void setTemporaryArmor(int temporaryArmor)
    {
        this.temporaryArmor = temporaryArmor;
    }

    public void resetTemporaryArmor()
    {
        setArmor(getArmor() - temporaryArmor);
        setTemporaryArmor(0);
    }
*/

    // Set, get, and decrease the remaining number of defense turns, which is set to 1.
    public void setRemainingDefenseTurns(int turns)
    {
        remainingDefenseTurns = turns;
    }

    public int getRemainingDefenseTurns()
    {
        return remainingDefenseTurns;
    }

    public void decreaseDefenseTurns()
    {
        if (remainingDefenseTurns > 0)
        {
            remainingDefenseTurns--;

            if (remainingDefenseTurns == 0)
            {
                defending = false;
            }
        }
    }

    /*
    public void increaseTemporaryArmor(int amount)
    {
        this.temporaryArmor += amount;
    }
    */

    // Assign the player to the room in which they enter or start in.
    public void setCurrentRoom(Room currentRoom)
    {
        this.currentRoom = currentRoom;
    }

    // Get the current room of the player.
    public Room getCurrentRoom()
    {
        return currentRoom;
    }

    public Item findItemByName(String itemName)
    {
        for (Item item : inventory)
        {
            if (item.getName().equalsIgnoreCase(itemName))
            {
                return item;
            }
            else
            {
            }
        }
        return null;
    }

    // Get the minimum and maximum amount of damage the player can do.
    // This is modified based on the weapon they have equipped.
    public int getMinDamage()
    {
        return minDamage;
    }

    public int getMaxDamage()
    {
        return maxDamage;
    }

    // Roll to hit when the player attempts to attack the target. If the number rolled is <= their base accuracy,
    // the attack will hit.
    public boolean attackHits(int accuracy)
    {
        Random random = new Random();
        int rollToHit = random.nextInt(100) + 1;
        return rollToHit <= accuracy;
    }

    // When the player acquires enough experience points, they will level up.
    public String levelUp()
    {
        level++;
        this.maxHealth += 10;
        this.health = this.maxHealth;
        this.minDamage += 2;
        this.maxDamage += 2;

        // The experience required to reach the next level is doubled each level.
        experienceToNextLevel = (int) (100 * Math.pow(2, level));

        return "\nCongratulations! You have reached level " + this.level + "!\n";
    }

    // Allow the player to gain experience from slain enemies. Enemies all have an exp value that determines how much
    // experience they are worth.
    public boolean gainExperience(int expGained)
    {
        this.experience += expGained;

        boolean hasLeveledUp = false;
        if (this.experience >= experienceToNextLevel)
        {
            levelUp();
            hasLeveledUp = true;
        }
        return hasLeveledUp;
    }

    // Retrieve the player's level
    public int getLevel()
    {
        return level;
    }
    // Retrieve the player's current ex[erience
    public int getExperience()
    {
        return experience;
    }
    // Retrieve the total amount of experience required to level up.
    public int getExperienceToNextLevel()
    {
        experienceToNextLevel = (int) (100 * Math.pow(2, this.level - 1));
        return experienceToNextLevel;
    }
}

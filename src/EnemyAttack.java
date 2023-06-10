import java.util.concurrent.ThreadLocalRandom;
import java.text.DecimalFormat;

// Method used to define enemy attacks. This method can be used for any attack, where damage multipliers will be used
// to calculate different damage ranges rounded down to the nearest integer.
public class EnemyAttack implements EnemyAction
{
    private final DecimalFormat decfor = new DecimalFormat("##");
    private final Adventure_Game adventure;

    private final int weight;
    private final String description;
    private final double damageMultiplier;

    public EnemyAttack(String description, double damageMultiplier, int weight, Adventure_Game adventure)
    {
        this.description = description;
        this.adventure = adventure;
        this.weight = weight;
        this.damageMultiplier = damageMultiplier;
    }

    // Execute the action of the enemy, applying the appropriate damage multiplier depending on what action is chosen.
    @Override
    public void execute(Enemy enemy, Player player)
    {
        if (enemy.attackHits(enemy.getAccuracy()))
        {
            int actualDamage = calculateDamage(enemy.getMinDamage(), enemy.getMaxDamage());
            int modifiedDamage = (int) (actualDamage * damageMultiplier);
            int effectiveDamage = player.takeDamage(modifiedDamage);
            adventure.displayText(enemy.getName() + " " + getDescription() + ", dealing " + decfor.format(effectiveDamage) + " damage.");
        }
        else
        {
            adventure.displayText(enemy.getName() + "'s attack just barely missed you.");
        }
    }

    // Return the description of the attack to make them less generic depending on the enemy.
    public String getDescription()
    {
        return description;
    }

    // Calculate the damage of the enemy's attack based on the enemy's damage range.
    private int calculateDamage(int minDamage, int maxDamage)
    {
        return ThreadLocalRandom.current().nextInt(minDamage, maxDamage + 1);
    }
}


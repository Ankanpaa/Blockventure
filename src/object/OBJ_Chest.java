package object;

import java.util.List;
import java.util.Random;

import entity.Entity;
import main.GamePanel;

public class OBJ_Chest extends Entity {

    public boolean isRandomLoot = true; // Determines if the chest uses random loot
    public String specificItem = null; // The specific item to give if not random

    public OBJ_Chest(GamePanel gp) {
        super(gp);
        name = "Chest";
        down1 = setup("/res/objects/chest");
        collision = true;
    }

    public String openChest(List<String> lootTable) {
        if (isRandomLoot) {
            Random random = new Random();
            int randomIndex = random.nextInt(lootTable.size());
            return lootTable.get(randomIndex);
        } else {
            return specificItem;
        }
    }
}

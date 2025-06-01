package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;

public class MON_GreenSlime extends Entity {

    public MON_GreenSlime(GamePanel gp) {
        super(gp);
        
        type = 2;
        name = "Green Slime";
        speed = 1;
        attack = 1;
        maxHealth = 4;
        defaultAggroRange = 6;
        aggroRange = defaultAggroRange;
        health = maxHealth;
        direction = "right";
        exp = 2;

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        getImage();
        

    }
    public void getImage() {
        up1 = setup("/res/monster/green_slime_1_up");
        up2 = setup("/res/monster/green_slime_3_up");
        down1 = setup("/res/monster/green_slime_1_down");
        down2 = setup("/res/monster/green_slime_3_down");
        left1 = setup("/res/monster/green_slime_1_left");
        left2 = setup("/res/monster/green_slime_3_left");
        right1 = setup("/res/monster/green_slime_1_right");
        right2 = setup("/res/monster/green_slime_3_right");
    }
    @Override
    public void setAction() {
        // Calculate distance to player in tiles
        int playerCol = (gp.player.worldX + gp.player.solidArea.x) / gp.tileSize;
        int playerRow = (gp.player.worldY + gp.player.solidArea.y) / gp.tileSize;
        int monsterCol = (worldX + solidArea.x) / gp.tileSize;
        int monsterRow = (worldY + solidArea.y) / gp.tileSize;

        int dx = Math.abs(playerCol - monsterCol);
        int dy = Math.abs(playerRow - monsterRow);

        if (dx + dy <= aggroRange) {
            onPath = true;
            searchPath(playerCol, playerRow);
        } else {
            onPath = false;
            // Optional: random movement or idle
            actionLockCounter++;
            if(actionLockCounter == 120) {
                Random random = new Random();
                int i = random.nextInt(100) + 1;
                if (i <= 25) direction = "up";
                else if (i <= 50) direction = "down";
                else if (i <= 75) direction = "left";
                else direction = "right";
                actionLockCounter = 0;
            }
        }
        
        // if (aggroRange == 0) {
        //    aggroCooldownCounter++;
        //    if (aggroCooldownCounter >= 20) {
        //        aggroRange = defaultAggroRange;
        //        aggroCooldownCounter = 0;
        //    }
        
    }
    @Override
    public void damageReaction() {
        actionLockCounter = 0;
        direction = gp.player.direction;
        // aggroRange = 0; // Disable aggro
        // aggroCooldownCounter = 0; // Start cooldown
    }
}

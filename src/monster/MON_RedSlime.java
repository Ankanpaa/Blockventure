package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Fireball;

public class MON_RedSlime extends Entity {

    public MON_RedSlime(GamePanel gp) {
        super(gp);
        
        type = 2;
        name = "Red Slime";
        speed = 2;
        maxHealth = 8;
        defaultAggroRange = 8;
        aggroRange = defaultAggroRange;
        health = maxHealth;
        direction = "right";
        attack = 1;
        exp = 6
        ;

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        projectile = new OBJ_Fireball(gp);
        
        getImage();
        

    }
    public void getImage() {
        up1 = setup("/res/monster/red_slime_1_up");
        up2 = setup("/res/monster/red_slime_3_up");
        down1 = setup("/res/monster/red_slime_1_down");
        down2 = setup("/res/monster/red_slime_3_down");
        left1 = setup("/res/monster/red_slime_1_left");
        left2 = setup("/res/monster/red_slime_3_left");
        right1 = setup("/res/monster/red_slime_1_right");
        right2 = setup("/res/monster/red_slime_3_right");
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
            int i = new Random().nextInt(100)+1;
            if(i > 99 && projectile.alive == false && shotAvailableCounter == 30) {
                projectile.set(worldX,worldY,direction,true,this);
                gp.projectileList.add(projectile);
                shotAvailableCounter = 0;
            }
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
        
    }
    @Override
    public void damageReaction() {
        actionLockCounter = 0;
        direction = gp.player.direction;
    }
}

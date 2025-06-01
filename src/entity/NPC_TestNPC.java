package entity;

import java.util.ArrayList;
import java.util.Random;

import main.GamePanel;

public class NPC_TestNPC extends Entity {

    public NPC_TestNPC(GamePanel gp) {
        super(gp);
        type = 1; 
        name = "TestNPC";
        direction = "down";
        speed = 1;

        getImage();
        setDialogue();
    }
    public void getImage() {
        up1 = setup("/res/npc/oldman_up_1");
        up2 = setup("/res/npc/oldman_up_2");
        down1 = setup("/res/npc/oldman_down_1");
        down2 = setup("/res/npc/oldman_down_2");
        left1 = setup("/res/npc/oldman_left_1");
        left2 = setup("/res/npc/oldman_left_2");
        right1 = setup("/res/npc/oldman_right_1");
        right2 = setup("/res/npc/oldman_right_2");
    }
    public void setDialogue() {
        dialogues[0] = "So I am only a test NPC so\nI don't need to say anything useful..";
        dialogues[1] = "I don't still have anything useful..";
        dialogues[2] = "...";
    }
    public void setAction() {
        if(onPath == true) {

            int goalCol = 30;
            int goalRow = 2;
            searchPath(goalCol, goalRow);

        } else {
            actionLockCounter++;
        if(actionLockCounter == 120) {
            Random random = new Random();
            int i = random.nextInt(100) + 1;
            if (i <= 25) {
                direction = "up";
            } else if (i <= 50) {
                direction = "down";
            } else if (i <= 75) {
                direction = "left";
            } else {
                direction = "right";
            }
            actionLockCounter = 0;
        }
        }
            
        
    }
    public void speak() {
        super.speak();
        onPath = true;
        
        
    }
    public void searchPath(int goalCol, int goalRow) {
        if (onPath && (path == null || path.isEmpty())) {
            int startCol = (worldX + solidArea.x) / gp.tileSize;
            int startRow = (worldY + solidArea.y) / gp.tileSize;
            gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow, this);
            if (gp.pFinder.search()) {
                path = new ArrayList<>(gp.pFinder.pathList);
            }
        }
    }


}

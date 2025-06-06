package main;

import java.awt.Rectangle;

public class EventHandler {

    GamePanel gp;
    Rectangle eventRect;
    int eventRectDefaultX, eventRectDefaultY;

    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new Rectangle();
        eventRect.x = 23;
        eventRect.y = 23;
        eventRect.width = 2;
        eventRect.height = 2;
        eventRectDefaultX = eventRect.x;
        eventRectDefaultY = eventRect.y;
    }

    public void checkEvent() {


    }
    public boolean hit(int eventCol, int eventRow, String reqDirection) {

        boolean hit = false;
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRect.x = eventCol * gp.tileSize + eventRect.x;
        eventRect.y = eventRow * gp.tileSize + eventRect.y;

        if(gp.player.solidArea.intersects(eventRect)) {
            if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                hit = true;
            }
        }
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect.x = eventRectDefaultX;
        eventRect.y = eventRectDefaultY;

        return hit;
    }
    // EVENTS
    public void damage() {
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "Ouch! You stepped on a damage pit!";
        gp.player.health -= 1;
    }
    public void teleport() {
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You have been teleported!";
        gp.player.worldX = 23 * gp.tileSize;
        gp.player.worldY = 23 * gp.tileSize;
    }
    public void heal() {
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You have been healed!";
        gp.player.health += 1;
        if (gp.player.health > gp.player.maxHealth) {
            gp.player.health = gp.player.maxHealth;
        }
    }

}

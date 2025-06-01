package main;

import entity.NPC_TestNPC;
import monster.MON_GreenSlime;
import monster.MON_RedSlime;
import object.OBJ_BerryBush;
import object.OBJ_Chest;
import object.OBJ_Door;

public class AssetSetter {

    GamePanel gp;
    
    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }
    
    public void setObject() {

        // Chest 1: Random loot
        gp.obj[0] = new OBJ_Chest(gp);
        gp.obj[0].worldX = 11 * gp.tileSize;
        gp.obj[0].worldY = 32 * gp.tileSize;
        ((OBJ_Chest) gp.obj[0]).isRandomLoot = true; // Use random loot
        ((OBJ_Chest) gp.obj[0]).specificItem = "Sword"; // Specific item to give

        // Chest 2: Always gives a specific item
        gp.obj[1] = new OBJ_Chest(gp);
        gp.obj[1].worldX = 22 * gp.tileSize;
        gp.obj[1].worldY = 29 * gp.tileSize;
        ((OBJ_Chest) gp.obj[1]).isRandomLoot = false; // Always give a specific item
        ((OBJ_Chest) gp.obj[1]).specificItem = "Sword"; // Specific item to give



        gp.obj[2] = new OBJ_BerryBush(gp);
        gp.obj[2].worldX = 2*gp.tileSize;
        gp.obj[2].worldY = 37*gp.tileSize;
        gp.obj[3] = new OBJ_BerryBush(gp);
        gp.obj[3].worldX = 2*gp.tileSize;
        gp.obj[3].worldY = 38*gp.tileSize;
        gp.obj[4] = new OBJ_BerryBush(gp);
        gp.obj[4].worldX = 2*gp.tileSize;
        gp.obj[4].worldY = 39*gp.tileSize;

        gp.obj[5] = new OBJ_BerryBush(gp);
        gp.obj[5].worldX = 4*gp.tileSize;
        gp.obj[5].worldY = 37*gp.tileSize;
        gp.obj[6] = new OBJ_BerryBush(gp);
        gp.obj[6].worldX = 4*gp.tileSize;
        gp.obj[6].worldY = 38*gp.tileSize;
        gp.obj[7] = new OBJ_BerryBush(gp);
        gp.obj[7].worldX = 4*gp.tileSize;
        gp.obj[7].worldY = 39*gp.tileSize;
        gp.obj[8] = new OBJ_BerryBush(gp);
        gp.obj[8].worldX = 6*gp.tileSize;
        gp.obj[8].worldY = 37*gp.tileSize;
        gp.obj[9] = new OBJ_BerryBush(gp);
        gp.obj[9].worldX = 6*gp.tileSize;
        gp.obj[9].worldY = 38*gp.tileSize;
        gp.obj[10] = new OBJ_BerryBush(gp);
        gp.obj[10].worldX = 6*gp.tileSize;
        gp.obj[10].worldY = 39*gp.tileSize;

        
    }
    public void setNPC() {

        gp.npc[0] = new NPC_TestNPC(gp);
        gp.npc[0].worldX = 21 * gp.tileSize;
        gp.npc[0].worldY = 30 * gp.tileSize;

    }
    public void setMonster() {

        gp.monster[0] = new MON_GreenSlime(gp);
        gp.monster[0].worldX = 13 * gp.tileSize;
        gp.monster[0].worldY = 33 * gp.tileSize;

        gp.monster[1] = new MON_RedSlime(gp);
        gp.monster[1].worldX = 14 * gp.tileSize;
        gp.monster[1].worldY = 33 * gp.tileSize;

    }


}

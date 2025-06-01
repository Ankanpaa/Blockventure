package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import item.Item;
import item.ItemManager;
import main.GamePanel;
import main.KeyHandler;
import main.UI;
import main.UtilityTool;
import object.OBJ_Door;
import object.OBJ_Fireball;
import object.OBJ_Key;
import object.OBJ_LootedChest;
import object.OBJ_Bush;
import object.OBJ_BerryBush;
import object.OBJ_Chest;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;
    public UI ui;
    ItemManager itemM = new ItemManager();

    public final int screenX;
    public final int screenY;
    public ArrayList<Item> inventory = new ArrayList<>();
    public ArrayList<Item> lootTable = new ArrayList<>();

    Random rand = new Random();
    public final int inventorySize = 24;
    public String holding;
    public boolean enterPressed = false;
    public int health = 5;
    public int maxHealth = 10;
    public int defaultHealth = 3;
    public boolean ePressed = false;
    public boolean spacePressed = false;
    public boolean attacking;
    public int attackCooldown = 25;
    public int defence = 0;
    public boolean defending;

    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp); // Explicitly call the constructor of the Entity class
        this.gp = gp;
        this.keyH = keyH;
        this.ui = gp.ui;
        Graphics2D g2;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        // HITBOX FOR PLAYER

        solidArea = new Rectangle();
        solidArea.x = 16;
        solidArea.y = 26;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 20;
        solidArea.height = 17;

        attackArea.width = 36;
        attackArea.height = 36;
        speed = 4;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        getPlayerDefendImage();
        setItems();

    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 30;
        speed = 4;
        direction = "down";
        projectile = new OBJ_Fireball(gp);
        exp = 0;
    }

    public void setItems() {
        inventory.add(itemM.getItem("Key"));
        inventory.add(itemM.getItem("Boots"));
        inventory.add(itemM.getItem("Berry"));
        inventory.add(itemM.getItem("Knife"));
        inventory.add(itemM.getItem("Sword"));
        inventory.add(itemM.getItem("Pickaxe"));
        inventory.add(itemM.getItem("Shield"));
        inventory.add(itemM.getItem("Axe"));
        inventory.add(itemM.getItem("Potion"));
        inventory.add(itemM.getItem("Paper Roll"));
        inventory.add(itemM.getItem("Carrots"));
        inventory.add(itemM.getItem("Berry"));
        inventory.add(itemM.getItem("Raw Berry"));
        inventory.add(itemM.getItem("Missing Texture"));
    }

    public void getPlayerImage() {

        up1 = setup("boy_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("boy_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("boy_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("boy_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("boy_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("boy_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("boy_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("boy_right_2", gp.tileSize, gp.tileSize);

    }

    public void getPlayerAttackImage() {
        attackUp1 = setup("boy_attack_up_1", gp.tileSize, gp.tileSize * 2);
        attackUp2 = setup("boy_attack_up_2", gp.tileSize, gp.tileSize * 2);
        attackDown1 = setup("boy_attack_down_1", gp.tileSize, gp.tileSize * 2);
        attackDown2 = setup("boy_attack_down_2", gp.tileSize, gp.tileSize * 2);
        attackLeft1 = setup("boy_attack_left_1", gp.tileSize * 2, gp.tileSize);
        attackLeft2 = setup("boy_attack_left_2", gp.tileSize * 2, gp.tileSize);
        attackRight1 = setup("boy_attack_right_1", gp.tileSize * 2, gp.tileSize);
        attackRight2 = setup("boy_attack_right_2", gp.tileSize * 2, gp.tileSize);
        axeUp1 = setup("boy_axe_up_1", gp.tileSize, gp.tileSize * 2);
        axeUp2 = setup("boy_axe_up_2", gp.tileSize, gp.tileSize * 2);
        axeDown1 = setup("boy_axe_down_1", gp.tileSize, gp.tileSize * 2);
        axeDown2 = setup("boy_axe_down_2", gp.tileSize, gp.tileSize * 2);
        axeLeft1 = setup("boy_axe_left_1", gp.tileSize * 2, gp.tileSize);
        axeLeft2 = setup("boy_axe_left_2", gp.tileSize * 2, gp.tileSize);
        axeRight1 = setup("boy_axe_right_1", gp.tileSize * 2, gp.tileSize);
        axeRight2 = setup("boy_axe_right_2", gp.tileSize * 2, gp.tileSize);
        pickaxeUp1 = setup("boy_pick_up_1", gp.tileSize, gp.tileSize * 2);
        pickaxeUp2 = setup("boy_pick_up_2", gp.tileSize, gp.tileSize * 2);
        pickaxeDown1 = setup("boy_pick_down_1", gp.tileSize, gp.tileSize * 2);
        pickaxeDown2 = setup("boy_pick_down_2", gp.tileSize, gp.tileSize * 2);
        pickaxeLeft1 = setup("boy_pick_left_1", gp.tileSize * 2, gp.tileSize);
        pickaxeLeft2 = setup("boy_pick_left_2", gp.tileSize * 2, gp.tileSize);
        pickaxeRight1 = setup("boy_pick_right_1", gp.tileSize * 2, gp.tileSize);
        pickaxeRight2 = setup("boy_pick_right_2", gp.tileSize * 2, gp.tileSize);

    }
    public void getPlayerDefendImage() {
        blockUp = setup("boy_guard_up", gp.tileSize, gp.tileSize);
        blockDown = setup("boy_guard_down", gp.tileSize, gp.tileSize);
        blockLeft = setup("boy_guard_left", gp.tileSize, gp.tileSize);
        blockRight = setup("boy_guard_right", gp.tileSize, gp.tileSize);
    }

    public BufferedImage setup(String imageName, int width, int height) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/res/player/" + imageName + ".png"));
            image = uTool.scaleImage(image, width, height);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return image;
    }

    public void attack() {
        spriteCounter++;

        if (spriteCounter <= 5) {
            spriteNum = 1;
        }
        if (spriteCounter > 5 && spriteCounter <= 25) {
            spriteNum = 2;

            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            switch (direction) {
                case "up":
                    worldY -= attackArea.height;
                    break;
                case "down":
                    worldY += attackArea.height;
                    break;
                case "left":
                    worldX -= attackArea.width;
                    break;
                case "right":
                    worldX += attackArea.width;
                    break;
            }

            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex);

            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;

            // CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }
        if (spriteCounter > attackCooldown) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }
    public void defend() {
        
        invincible = true;
        switch (gp.ui.shieldSlot) {
            case "Shield":
                defence = 1;
            case "none":
                defence = 0;
        }

    }

    public void update() {
        if(shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
        if (attacking) {
        defending = false; // Cannot defend while attacking
        collisionOn = false;
        attack();
        return;
    }
    if (defending) {
        attacking = false; // Cannot attack while defending
        collisionOn = false;
        defend();
        return;
    }
        if (ePressed && !attacking && !defending && !spacePressed && ui.weaponSlot != "none") {
            switch (ui.weaponSlot) {
                case "Axe":
                    attackCooldown = 40; // slower
                    attack = 3; // more damage
                    break;
                case "Pickaxe":
                    attackCooldown = 35; // slower
                    attack = 2; // more damage
                    break;
                default: // Sword or others
                    attackCooldown = 25; // default speed
                    attack = 1; // default damage
                    break;
            }
            attacking = true;
    }
        if (spacePressed && !attacking && ui.shieldSlot != "none") {
            defending = true;
        }
            if (ui.holding.equals("Boots")) {
                speed = 6;
            } else {
                speed = 4;
            }
        if (health <= 0) {
            gp.gameState = gp.gameOverState;
        }
        if(gp.keyH.rPressed == true && projectile.alive == false && shotAvailableCounter == 30) {
            projectile.set(worldX, worldY, direction, true, this);

            gp.projectileList.add(projectile);
            shotAvailableCounter = 0;
        }

        // --- MOVEMENT LOGIC ---
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
            if (keyH.upPressed)
                direction = "up";
            else if (keyH.downPressed)
                direction = "down";
            else if (keyH.leftPressed)
                direction = "left";
            else if (keyH.rightPressed)
                direction = "right";

            // CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);

            // CHECK NPC COLLISION
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactWithNPC(npcIndex);

            // CHECK MONSTER COLLISION
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            // IF COLLISION IS FALSE, PLAYER CAN MOVE
            if (!collisionOn) {
                switch (direction) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }

            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }

            // CHECK IF PLAYER IS OUT OF BOUNDS
            if (worldX < 0 || worldY < 0 || worldX > gp.worldWidth - gp.tileSize || worldY > gp.worldHeight - 50) {
                health = 0;
            }
        }

        // --- INTERACTION LOGIC ---
        if (keyH.enterPressed) {
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactWithNPC(npcIndex);

            keyH.enterPressed = false;
        }

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    public void pickUpObject(int i) {

        if (i != 999) {
            String objectName = gp.obj[i].name;
            switch (objectName) {
                case "Key":
                    gp.obj[i] = null;
                    inventory.add(itemM.getItem("Key"));
                case "Chest":
                    lootChest((OBJ_Chest) gp.obj[i]);
                    openChest(i);
                    break;
                case "BerryBush":
                    takeBerry(i);
                    break;
            }
        }
    }

    public void draw(Graphics2D g2) {
    BufferedImage image = null;
    int tempScreenX = screenX;
    int tempScreenY = screenY;

    if (defending) {
        switch (direction) {
            case "up":
                image = blockUp;
                break;
            case "down":
                image = blockDown;
                break;
            case "left":
                image = blockLeft;
                break;
            case "right":
                image = blockRight;
                break;
        }
    } else {
        switch (direction) {
            case "up":
                if (!attacking) {
                    image = (spriteNum == 1) ? up1 : up2;
                } else {
                    tempScreenY = screenY - gp.tileSize;
                    if ("Axe".equals(ui.weaponSlot)) {
                        image = (spriteNum == 1) ? axeUp1 : axeUp2;
                    } else if ("Pickaxe".equals(ui.weaponSlot)) {
                        image = (spriteNum == 1) ? pickaxeUp1 : pickaxeUp2;
                    } else if ("Sword".equals(ui.weaponSlot)) {
                        image = (spriteNum == 1) ? attackUp1 : attackUp2;
                    }
                }
                break;
            case "down":
                if (!attacking) {
                    image = (spriteNum == 1) ? down1 : down2;
                } else {
                    if ("Axe".equals(ui.weaponSlot)) {
                        image = (spriteNum == 1) ? axeDown1 : axeDown2;
                    } else if ("Pickaxe".equals(ui.weaponSlot)) {
                        image = (spriteNum == 1) ? pickaxeDown1 : pickaxeDown2;
                    } else if ("Sword".equals(ui.weaponSlot)) {
                        image = (spriteNum == 1) ? attackDown1 : attackDown2;
                    }
                }
                break;
            case "left":
                if (!attacking) {
                    image = (spriteNum == 1) ? left1 : left2;
                } else {
                    tempScreenX = screenX - gp.tileSize;
                    if ("Axe".equals(ui.weaponSlot)) {
                        image = (spriteNum == 1) ? axeLeft1 : axeLeft2;
                    } else if ("Pickaxe".equals(ui.weaponSlot)) {
                        image = (spriteNum == 1) ? pickaxeLeft1 : pickaxeLeft2;
                    } else if ("Sword".equals(ui.weaponSlot)) {
                        image = (spriteNum == 1) ? attackLeft1 : attackLeft2;
                    }
                }
                break;
            case "right":
                if (!attacking) {
                    image = (spriteNum == 1) ? right1 : right2;
                } else {
                    if ("Axe".equals(ui.weaponSlot)) {
                        image = (spriteNum == 1) ? axeRight1 : axeRight2;
                    } else if ("Pickaxe".equals(ui.weaponSlot)) {
                        image = (spriteNum == 1) ? pickaxeRight1 : pickaxeRight2;
                    } else if ("Sword".equals(ui.weaponSlot)) {
                        image = (spriteNum == 1) ? attackRight1 : attackRight2;
                    }
                }
                break;
        }
    }

    if (invincible && !defending) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
    }

    g2.drawImage(image, tempScreenX, tempScreenY, null);
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
}

    public void changeAlpha(Graphics2D g2, float alpha) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }

    public void interactWithNPC(int i) {
        if (i != 999) {
            if (gp.keyH.enterPressed == true) {

                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
        }
        gp.keyH.enterPressed = false;
    }

    public void contactMonster(int i) {
        if (i != 999) {
            if (defending == false) {
                if(invincible == false && gp.monster[i].dying == false) {
                health -= 1;
                invincible = true;
            }
        }
        }
    }
    public void inventoryAdd(String item) {
        inventory.add(itemM.getItem(item));
    }
    public void inventoryRemoveIndex(int itemIndex) {
        inventory.remove(itemIndex);

    }
    public void inventoryRemove (String item){
        inventory.remove(itemM.getItem(item));
    }

    public void damageMonster(int i) {
        if (i != 999) {
            if (gp.monster[i].invincible == false) {

                int damage = attack;
                gp.monster[i].health -= attack;
                gp.ui.addMessage(damage + " damage!");
                gp.monster[i].invincible = true;
                gp.monster[i].damageReaction();

                if (gp.monster[i].health <= 0) {
                    gp.monster[i].dying = true;
                    gp.ui.addMessage("Killed the " + gp.monster[i].name + "!");
                    gp.ui.addMessage("+" + gp.monster[i].exp + " XP!");
                    exp += gp.monster[i].exp;
                    checkLevelUp();
                }
                for (Item item : inventory) {
                    if (item != null && item.name != null && item.name.equals(ui.weaponSlot)
                            && item.currentDurability > 0) {
                        item.currentDurability -= 1;
                        if (item.currentDurability <= 0) {
                            ui.weaponSlot = "none";
                        }
                        break;
                    }
                }
            }

        }
    }
    public void checkLevelUp() {
        
    }
    public void levelUp() {
    }

    public void interactWithHoldingItem() {
        switch (ui.holding) {
            case "Berry":
                if (health < maxHealth) {
                    health += 1;
                    ui.holding = "none";
                    break;
                } else {
                    break;
                }
            case "Carrots":
                if (health < maxHealth) {
                    health += 2;
                    ui.holding = "none";
                    break;
                } else {
                    break;
                }
            case "Potion":
                if (health < maxHealth) {
                    health = maxHealth;
                    ui.holding = "none";
                    break;
                } else {
                    break;
                }
            case "Sword":
                if (health > 0) {
                    health -= 1;
                    break;
                } else {
                    break;
                }
            case "Raw Berry":
                if (health > 0) {
                    ui.holding = "none";
                    health -= 1;
                    break;
                } else {
                    break;
                }
        }

    }

    public void openChest(int objNum) {
        if (ui.holding == "Key") {
            ui.holding = "none";
            int x = gp.obj[objNum].worldX;
            int y = gp.obj[objNum].worldY;
            gp.obj[objNum] = null;
            gp.obj[objNum] = new OBJ_LootedChest(gp);
            gp.obj[objNum].worldX = x;
            gp.obj[objNum].worldY = y;
        }

    }

    public void takeBerry(int objNum) {
        int rawRandom = rand.nextInt(100);
        if (rawRandom >= 10) {
            inventory.add(itemM.getItem("Berry"));
            int x = gp.obj[objNum].worldX;
            int y = gp.obj[objNum].worldY;
            gp.obj[objNum] = null;
            gp.obj[objNum] = new OBJ_Bush(gp);
            gp.obj[objNum].worldX = x;
            gp.obj[objNum].worldY = y;
        } else {
            inventory.add(itemM.getItem("Raw Berry"));
            int x = gp.obj[objNum].worldX;
            int y = gp.obj[objNum].worldY;
            gp.obj[objNum] = null;
            gp.obj[objNum] = new OBJ_Bush(gp);
            gp.obj[objNum].worldX = x;
            gp.obj[objNum].worldY = y;
        }

    }

    public void lootChest(OBJ_Chest chest) {
        if (ui.holding == "Key") {

            List<String> lootTable = List.of("Boots",
                    "Berry", "Berry", "Berry", "Berry", "Berry",
                    "Potion",
                    "Axe", "Axe",
                    "Sword", "Sword", "Sword", "Sword", "Sword");
            ;
            String item = chest.openChest(lootTable);
            gp.ui.addMessage("Looted Chest! Got: " + item);
            gp.player.inventory.add(itemM.getItem(item));
        }
    }
}

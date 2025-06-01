package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import ai.Node;
import main.GamePanel;

public class Entity {

    
        
    
    public GamePanel gp;
    public int worldX, worldY;
    public int speed;
    public String name;
    public int health, maxHealth;
    public BufferedImage image, image2, image3, image4;
    public boolean collision = false;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    public BufferedImage axeUp1, axeUp2, axeDown1, axeDown2, axeLeft1, axeLeft2, axeRight1, axeRight2;
    public BufferedImage pickaxeUp1, pickaxeUp2, pickaxeDown1, pickaxeDown2, pickaxeLeft1, pickaxeLeft2, pickaxeRight1, pickaxeRight2;
    public BufferedImage blockUp, blockDown, blockLeft, blockRight;
    public String direction = "down";
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea = new Rectangle(0,0,0,0);
    public Rectangle blockArea = new Rectangle(0,0,0,0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public int actionLockCounter = 0;
    public boolean invincible = false;
    public int invincibleCounter = 0;
    public int exp = 0;
    public String dialogues[] = new String[20];
    public int dialogueIndex = 0;
    public int type; // 0 = player, 1 = npc, 2 = monster
    public boolean alive = true;
    public boolean dying = false;
    public int deathCounter = 0;
    public boolean hpBarOn = false;
    public int hpBarCounter = 0;
    public boolean onPath = false;
    public java.util.List<Node> path = null;
    public int aggroRange = 3;
    public int defaultAggroRange = 3;
    public int aggroCooldownCounter = 0;
    public int attack = 1;
    public Projectile projectile;
    public int shotAvailableCounter = 0;

    public BufferedImage setup(String imagePath) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    public Entity(GamePanel gp) {
        this.gp = gp;
    }
    public void checkCollision() {
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);
            
    }
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            switch(direction) {
                case "up":
                    image = (spriteNum == 1) ? up1 : up2;
                    break;
                case "down":
                    image = (spriteNum == 1) ? down1 : down2;
                    break;
                case "left":
                    image = (spriteNum == 1) ? left1 : left2;
                    break;
                case "right":
                    image = (spriteNum == 1) ? right1 : right2;
                    break;
            }

            if(type == 2 && hpBarOn == true) {
                double oneScale = (double)gp.tileSize /maxHealth;
                double hpBarValue = oneScale * health;


                g2.setColor(new Color(35,35, 35));
                g2.fillRect(screenX - 1, screenY - 16, gp.tileSize + 2, 12);
                
                if(health > 0) {
                g2.setColor(new Color(255,0,3));
                g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);
                }
                

                hpBarCounter++;
                if(hpBarCounter > 600) {
                    hpBarOn = false;
                    hpBarCounter = 0;
                }
            }

            if(invincible == true) {
                hpBarOn = true;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
            }
            if(dying == true) {
                dyingAnimation(g2);
            }
            // Draw the selected image
            if (image != null) {
                g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
            // Reset the composite to default
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }

    public void dyingAnimation(Graphics2D g2) {
        deathCounter++;
        int i = 5;
        if(deathCounter <= i) {changeAlpha(g2, 0f);}
        if(deathCounter > i && deathCounter <= i * 2) {changeAlpha(g2, 1f);}
        if(deathCounter > i * 2 && deathCounter <= i * 3) {changeAlpha(g2, 0f);}
        if(deathCounter > i * 3 && deathCounter <= i * 4) {changeAlpha(g2, 1f);}
        if(deathCounter > i * 4 && deathCounter <= i * 5) {changeAlpha(g2, 0f);}
        if(deathCounter > i * 5 && deathCounter <= i * 6) {changeAlpha(g2, 1f);}
        if(deathCounter > i * 6 && deathCounter <= i * 7) {changeAlpha(g2, 0f);}
        if(deathCounter > i * 7 && deathCounter <= i * 8) {changeAlpha(g2, 1f);}
        if(deathCounter > i * 8) {
            dying = false;
            alive = false;
        }

    }

    public void changeAlpha(Graphics2D g2, float alpha) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }
    public void 
    searchPath(int goalCol, int goalRow) {
        int startCol = (worldX + solidArea.x) / gp.tileSize;
        int startRow = (worldY + solidArea.y) / gp.tileSize;

        gp.pFinder.setNodes(startCol, startRow, goalCol, goalRow, this);
        if (gp.pFinder.search()) {
            // Get the next node in the path
            int nextCol = gp.pFinder.pathList.get(0).col;
            int nextRow = gp.pFinder.pathList.get(0).row;

            int entityCol = (worldX + solidArea.x) / gp.tileSize;
            int entityRow = (worldY + solidArea.y) / gp.tileSize;

            if (entityRow > nextRow) {
                direction = "up";
            } else if (entityRow < nextRow) {
                direction = "down";
            } else if (entityCol > nextCol) {
                direction = "left";
            } else if (entityCol < nextCol) {
                direction = "right";
            }

            // Stop pathing if reached goal
            if (nextCol == goalCol && nextRow == goalRow) {
                onPath = false;
            }
        }
        else {
            onPath = false; // No path found
        }
    }

    public void setAction() {}
    public void damageReaction() {}
    public void speak() {
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;
        if (dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }

        switch(gp.player.direction) {
            case "up":
                direction = "down";
                break;
            case "down":

                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }
    }
    public void update() {

        setAction();
        checkCollision();
        
        if(type == 2) {
            boolean contactPlayer = gp.cChecker.checkPlayer(this);
            if(this.type == 2 && contactPlayer) {
                damagePlayer(attack);
            }
        }

        // --- PATHFINDING MOVEMENT ---
        if (onPath && path != null && !path.isEmpty()) {
            Node nextNode = path.get(0);
            int nextX = nextNode.col * gp.tileSize;
            int nextY = nextNode.row * gp.tileSize;

            if (!collisionOn) {
                if (worldX < nextX) {
                    direction = "right";
                    worldX += speed;
                } else if (worldX > nextX) {
                    direction = "left";
                    worldX -= speed;
                } else if (worldY < nextY) {
                    direction = "down";
                    worldY += speed;
                } else if (worldY > nextY) {
                    direction = "up";
                    worldY -= speed;
                }
            }
            if (Math.abs(worldX - nextX) < speed && Math.abs(worldY - nextY) < speed) {
                worldX = nextX;
                worldY = nextY;
                path.remove(0);
                if (path.isEmpty()) {
                    onPath = false;
                    System.out.println("Reached the goal!");
                }
            }
        }
        // --- REGULAR MOVEMENT---
        else if (!collisionOn) {
            switch (direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }

        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }
    public void damagePlayer(int attack) {
        if(gp.player.invincible == false) {

                int damage = attack - gp.player.defence;
                if(damage < 0) {
                    damage = 0;
                }

                gp.player.health -= damage;
                gp.player.invincible = true;
            }
        }
    
    

}

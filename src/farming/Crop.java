package farming;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class Crop {
    public String type;
    public int stage;        // 0..maxStage
    public final int maxStage;
    public final int framesPerStage;
    public int frameCounter;
    public boolean watered;
    public BufferedImage[] sprites;

    public Crop(String type, int maxStage, int framesPerStage, BufferedImage[] sprites) {
        this.type = type;
        this.maxStage = maxStage;
        this.framesPerStage = framesPerStage;
        this.stage = 0;
        this.frameCounter = 0;
        this.watered = false;
        this.sprites = sprites;
    }

    public void update(int deltaFrames) {
        if (stage >= maxStage) return;
        if (!watered) return;
        frameCounter += deltaFrames;
        while (frameCounter >= framesPerStage && stage < maxStage) {
            frameCounter -= framesPerStage;
            stage++;
            watered = false;
        }
    }

    public boolean isHarvestable() {
        return stage >= maxStage;
    }

    public void draw(Graphics2D g2, GamePanel gp, int col, int row) {
        int worldX = col * gp.tileSize;
        int worldY = row * gp.tileSize;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // quick on-screen check
        if (worldX + gp.tileSize < gp.player.worldX - gp.player.screenX ||
            worldX - gp.tileSize > gp.player.worldX + gp.player.screenX ||
            worldY + gp.tileSize < gp.player.worldY - gp.player.screenY ||
            worldY - gp.tileSize > gp.player.worldY + gp.player.screenY) {
            return;
        }

        BufferedImage img = null;
        if (sprites != null && sprites.length > 0) {
            int drawStage = Math.min(stage, sprites.length - 1);
            img = sprites[drawStage];
        }

        if (img != null) {
            g2.drawImage(img, screenX, screenY, gp.tileSize, gp.tileSize, null);
        } else {
            // fallback visible placeholder if sprite missing
            g2.setColor(new Color(34, 139, 34));
            g2.fillRect(screenX + gp.tileSize/4, screenY + gp.tileSize/4, gp.tileSize/2, gp.tileSize/2);
            g2.setColor(Color.BLACK);
            g2.drawString(type.substring(0, Math.min(3, type.length())), screenX + 2, screenY + 12);
        }

        // optional water indicator
        if (!watered && stage < maxStage) {
            g2.setColor(new Color(0, 120, 255, 180));
            g2.fillOval(screenX + gp.tileSize - 10, screenY + 4, 6, 6);
        }
    }
}
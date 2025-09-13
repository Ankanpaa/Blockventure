package farming;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class CropManager {

    GamePanel gp;
    public Map<Point, Crop> crops = new HashMap<>();
    // cache sprites per crop type
    private Map<String, BufferedImage[]> spriteCache = new HashMap<>();

    public CropManager(GamePanel gp) {
        this.gp = gp;
        // Preload known crop types (adjust names to your assets)
        preloadType("carrot", 4);
        preloadType("berry", 4);
    }

    private void preloadType(String type, int stages) {
        BufferedImage[] arr = new BufferedImage[stages];
        UtilityTool uTool = new UtilityTool(); // scale helper in your project
        for (int i = 0; i < stages; i++) {
            // use the type name so files can be carrot_stage0.png, berry_stage0.png, etc.
            String path = "/res/crops/carrot_stage_" + i + ".png";
            try {
                java.io.InputStream is = getClass().getResourceAsStream(path);
                if (is == null) {
                    System.err.println("Crop image not found: " + path + " (check filename & resource location)");
                    arr[i] = null;
                    continue;
                }
                BufferedImage raw = ImageIO.read(is);
                // scale to tileSize so it draws correctly
                arr[i] = uTool.scaleImage(raw, gp.tileSize, gp.tileSize);
            } catch (IOException | IllegalArgumentException e) {
                System.err.println("Failed to load crop image: " + path + " -> " + e.getMessage());
                arr[i] = null;
            }
        }
        spriteCache.put(type, arr);
    }

    // secondsPerStage is in seconds; we convert to frames using gp.FPS
    public boolean plantCrop(int col, int row, String seedType, int secondsPerStage, int maxStage) {
        Point p = new Point(col, row);
        if (crops.containsKey(p)) return false;
        int frames = Math.max(1, gp.FPS * secondsPerStage);

        BufferedImage[] sprites = spriteCache.get(seedType);
        if (sprites == null) {
            // try to lazy-load assuming 4 stages
            preloadType(seedType, 4);
            sprites = spriteCache.get(seedType);
        }

        // If sprites exist, set maxStage to sprites.length - 1 if you want exact mapping
        int effectiveMax = Math.min(maxStage, (sprites != null ? sprites.length - 1 : maxStage));
        crops.put(p, new Crop(seedType, effectiveMax, frames, sprites));
        return true;
    }

    public boolean waterCrop(int col, int row) {
        Crop c = crops.get(new Point(col, row));
        if (c == null) return false;
        c.watered = true;
        return true;
    }

    public java.util.List<String> harvestCrop(int col, int row) {
        Point p = new Point(col, row);
        Crop c = crops.get(p);
        if (c == null || !c.isHarvestable()) return null;

        java.util.List<String> drops = new java.util.ArrayList<>();
        java.util.Random rand = new java.util.Random();

        switch (c.type.toLowerCase()) {
            case "carrot": {
                // carrots: 1-3 carrots, plus 0-2 seeds
                int carrots = 1;
                int seeds = 1 + rand.nextInt(2); // 1..2
                for (int i = 0; i < carrots; i++) drops.add("Carrots");
                for (int i = 0; i < seeds; i++) drops.add("Carrot Seeds");
                break;
            }
            case "berry": {
                // berries: 1-4 berries, small chance for extra raw berries
                int berries = 1 + rand.nextInt(4); // 1..4
                for (int i = 0; i < berries; i++) drops.add("Berry");
                if (rand.nextInt(100) < 15) { // 15% chance extra raw berry
                    drops.add("Raw Berry");
                }
                break;
            }
            default: {
                // fallback: 1 item of the generic product
                drops.add("Berry");
                break;
            }
        }

        crops.remove(p);
        return drops;
    }

    public void updateAll(int deltaFrames) {
        Iterator<Crop> it = crops.values().iterator();
        while (it.hasNext()) {
            Crop c = it.next();
            c.update(deltaFrames);
        }
    }

    public void draw(Graphics2D g2) {
        for (Map.Entry<Point, Crop> e : crops.entrySet()) {
            Point p = e.getKey();
            Crop c = e.getValue();
            c.draw(g2, gp, p.x, p.y);
        }
    }
}
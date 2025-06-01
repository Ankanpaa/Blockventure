package item;

import java.awt.image.BufferedImage;

public class Item {
    public String name;
    public String description;
    public int maxDurability;
    public int currentDurability;
    public BufferedImage image;

    public Item(String name, String description, int maxDurability, BufferedImage image) {
        this.name = name;
        this.description = description;
        this.maxDurability = maxDurability;
        this.currentDurability = maxDurability;
        this.image = image;
    }
}

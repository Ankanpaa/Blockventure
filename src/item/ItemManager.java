package item;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ItemManager {

    public Map<String, Item> items = new HashMap<>();

    public ItemManager() {
        loadItems();
    }

    private void loadItems() {
        items.put("Sword", new Item(
            "Sword",
            "A powerful sword for combat.\n\nStats:\nAttack: 5\nDurability: 100",
            100,
            loadImage("/res/items/sword.png")
        ));
        items.put("Key", new Item(
            "Key",
            "A key that unlocks chests.",
            1,
            loadImage("/res/items/key.png")
        ));
        items.put("Shield", new Item(
            "Shield",
            "A resistant shield for defense.\n\nStats:\nDefense: 3\nDurability: 75",
            75,
            loadImage("/res/items/shield.png")
        ));
        items.put("Boots", new Item(
            "Boots",
            "Sturdy boots for better movement.\n\nStats:\nSpeed: +2\nDurability: 50",
            50,
            loadImage("/res/items/boots_of_swiftness.png")
        ));
        items.put("Berry", new Item(
            "Berry",
            "A sweet berry that restores health.\n\nStats:\nHealth: +1",
            1,
            loadImage("/res/items/berry.png")
        ));
        items.put("Knife", new Item(
            "Knife",
            "A sharp knife for quick attacks.\n\nStats:\nDamage: 1\nDurability: 20",
            20,
            loadImage("/res/items/knife.png")
        ));
        items.put("Pickaxe", new Item(
            "Pickaxe",
            "A sturdy pickaxe for mining.\n\nStats:\nDurability: 100\nDamage: 2",
            100,
            loadImage("/res/items/pickaxe.png")
        ));
        items.put("Paper Roll", new Item(
            "Paper Roll",
            "A roll of paper with mysterious writings.",
            999,
            loadImage("/res/items/paper_roll.png")
        ));
        items.put("Axe", new Item(
            "Axe",
            "A sharp axe for cutting trees.\n\nStats:\nAttack: 4\nDurability: 80",
            80,
            loadImage("/res/items/axe.png")
        ));
        items.put("Carrots", new Item(
            "Carrots",
            "Fresh carrots that restore health.\n\nStats:\nHealth: +2",
            1,
            loadImage("/res/items/carrots.png")
        ));
        items.put("Potion", new Item(
            "Potion",
            "A magical potion that restores health.\n\nStats:\nHealth: Restores all health\nDurability: 1",
            1,
            loadImage("/res/items/potion.png")
        ));
        items.put("Water Bottle", new Item(
            "Water Bottle",
            "This bottle contains clean water.",
            1,
            loadImage("/res/items/water_bottle.png")
        ));
        items.put("Raw Berry", new Item(
            "Raw Berry",
            "A raw berry that can be eaten.\n\nStats:\nHealth: +2\nDurability: 1",
            1,
            loadImage("/res/items/rawberry.png")
        ));
        items.put("Missing Texture", new Item(
            "Missing Texture",
            "No description available\n\nStats:\nUnknown",
            1,
            loadImage("/res/items/missingtexture.png")
        ));
        items.put("Sword shadow", new Item(
            "Sword shadow",
            "A shadowy version of the sword.\n\nStats:\nAttack: 5\nDurability: 100",
            100,
            loadImage("/res/guis/sword_shadow.png")
        ));
        items.put("Shield shadow", new Item(
            "Shield shadow",
            "A shadowy version of the shield.\n\nStats:\nDefense: 3\nDurability: 75",
            75,
            loadImage("/res/guis/shield_shadow.png")
        ));
        items.put("Carrot Seeds", new Item(
            "Carrot Seeds",
            "Used to plant carrots.",
            0,
            loadImage("/res/items/missingtexture.png")
        ));
    }

    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Item getItem(String name) {
        return items.get(name);
    }

    public String getItemDescription(String name) {
        Item item = items.get(name);
        if (item != null) {
            return item.description;
        } else {
            return "Item not found.";
        }
    }
}

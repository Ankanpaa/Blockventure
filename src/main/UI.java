package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.Desktop;

import javax.imageio.ImageIO;

import item.Item;
import item.ItemManager;

import java.awt.Font;
import java.awt.FontFormatException;

public class UI {

    Map<String, BufferedImage> itemImages = new HashMap<>();
    Map<String, BufferedImage> weaponMap = new HashMap<>();
    Map<String, BufferedImage> shieldMap = new HashMap<>();
    public boolean messageOn = false;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public String currentDialogue = "";
    public BufferedImage fullHealth, halfHealth, emptyHealth;
    public int commandNum = 0;
    public int pauseCommandNum = 0;
    public int gameOverCommandNum = 1;
    public int inventoryCommandNum = 0;
    public int slotCol = 0;
    public int slotRow = 0;
    public int itemIndex = 0;
    public boolean enterPressed = false;
    public String holding = "none";
    public String weaponSlot = "none";
    public String shieldSlot = "none";
    public String description;
    public int slotX;
    public int slotY;
    String dialogues[] = new String[20];

    Font Jersey;

    Graphics2D g2;
    GamePanel gp;
    ItemManager itemM = new ItemManager();
    KeyHandler keyH = new KeyHandler(gp);

    public UI(GamePanel gp) {
        this.gp = gp;

        try {
            InputStream is = getClass().getResourceAsStream("/res/fonts/Jersey.ttf");
            Jersey = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, 40F);
            is = getClass().getResourceAsStream("/res/fonts/arial.ttf");
            fullHealth = ImageIO.read(getClass().getResourceAsStream("/res/guis/fullhealth.png"));
            halfHealth = ImageIO.read(getClass().getResourceAsStream("/res/guis/halfhealth.png"));
            emptyHealth = ImageIO.read(getClass().getResourceAsStream("/res/guis/emptyhealth.png"));

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.exit(-99);
        }

        // Initialize the item-image map
        itemImages.put("Key", itemM.items.get("Key").image);
        itemImages.put("Boots", itemM.items.get("Boots").image);
        itemImages.put("Berry", itemM.items.get("Berry").image);
        itemImages.put("Knife", itemM.items.get("Knife").image);
        itemImages.put("Pickaxe", itemM.items.get("Pickaxe").image);
        itemImages.put("Paper Roll", itemM.items.get("Paper Roll").image);
        itemImages.put("Sword", itemM.items.get("Sword").image);
        itemImages.put("Shield", itemM.items.get("Shield").image);
        itemImages.put("Carrots", itemM.items.get("Carrots").image);
        itemImages.put("Axe", itemM.items.get("Axe").image);
        itemImages.put("Potion", itemM.items.get("Potion").image);
        itemImages.put("Water Bottle", itemM.items.get("Water Bottle").image);
        itemImages.put("Raw Berry", itemM.items.get("Raw Berry").image);
        itemImages.put("none", null); // For empty slots
        itemImages.put("MissingTexture", itemM.items.get("Missing Texture").image);

        // Initialize the weapon-image map
        weaponMap.put("Sword", itemM.items.get("Sword").image);
        weaponMap.put("Axe", itemM.items.get("Axe").image);
        weaponMap.put("Knife", itemM.items.get("Knife").image);
        weaponMap.put("Pickaxe", itemM.items.get("Pickaxe").image);

        // Initialize the shield-image map
        shieldMap.put("Shield", itemM.items.get("Shield").image);
    }

    public void addMessage(String text) {
        message.add(text);
        messageCounter.add(0);
    }

    public void drawMessage(String text, int x, int y) {
        g2.setFont(Jersey);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);
    }

    public void drawChestItem(BufferedImage image, int chestX, int chestY) {
        g2.drawImage(image, chestX, chestY, gp.tileSize, gp.tileSize, null);
    }

    public void draw(Graphics2D g2) {
        // Assign the passed Graphics2D object to the class-level g2 variable
        this.g2 = g2;

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
        g2.setColor(Color.white);
        drawHealth();
        // TITLE STATE
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }

        // PLAY STATE
        if (gp.gameState == gp.playState) {
            drawMessage();
        }
        if (gp.gameState != gp.gameOverState &&
                gp.gameState != gp.dialogueState &&
                gp.gameState != gp.titleState &&
                gp.gameState != gp.pauseState) {

            Color c = new Color(0, 0, 0, 200);
            int x = 458;
            int y = 500;
            g2.setColor(c);
            g2.fillRoundRect(x, y, gp.tileSize, gp.tileSize, 10, 10);

            // Draw the border
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(4));
            g2.drawRoundRect(x, y, gp.tileSize, gp.tileSize, 10, 10);
            if (holding != "none") {
                BufferedImage image = itemImages.getOrDefault(holding, itemM.items.get("Missing Texture").image);
                g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
            }
            x = 400;
            y = 500;
            g2.setColor(c);
            g2.fillRoundRect(x, y, gp.tileSize, gp.tileSize, 10, 10);
            if (weaponSlot == "none") {
                g2.drawImage(itemM.items.get("Sword shadow").image, x, y, gp.tileSize, gp.tileSize, null);
            } else {
                BufferedImage image = itemImages.getOrDefault(weaponSlot, itemM.items.get("Missing Texture").image);
                g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
            }
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(4));
            g2.drawRoundRect(x, y, gp.tileSize, gp.tileSize, 10, 10);

            x = 516;
            y = 500;
            g2.setColor(c);
            g2.fillRoundRect(x, y, gp.tileSize, gp.tileSize, 10, 10);
            if (shieldSlot == "none") {
                g2.drawImage(itemM.items.get("Shield shadow").image, x, y, gp.tileSize, gp.tileSize, null);
            } else {
                BufferedImage image = shieldMap.getOrDefault(shieldSlot, itemM.items.get("Missing Texture").image);
                g2.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
            }

            // Draw the border
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(4));
            g2.drawRoundRect(x, y, gp.tileSize, gp.tileSize, 10, 10);
        }

        // PAUSE STATE
        if (gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }

        // INVENTORY STATE
        if (gp.gameState == gp.inventoryState) {
            drawInventory();

        }
        // DIALOGUE STATE
        if (gp.gameState == gp.dialogueState) {
            drawDialogueScreen();
        }
        if (gp.gameState == gp.gameOverState) {
            drawGameOverScreen();
        }
    }
    public void drawMessage() {
        int messageX = gp.tileSize;
        int messageY = gp.tileSize * 4;
        g2.setFont(Jersey.deriveFont(Font.PLAIN, 32F));

        for(int i = 0; i < message.size(); i++) {
            if(message.get(i) != null) {
                g2.setColor(Color.black);
                g2.drawString(message.get(i), messageX+2, messageY+2);
                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1;
                messageCounter.set(i, counter);
                messageY += 30;
                if(messageCounter.get(i) > 180) {
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }
    public void drawTitleScreen() {

        g2.setColor(new Color(70, 120, 80));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(Jersey.deriveFont(Font.BOLD, 96F));
        String text = "Blockventure";
        int x = getXforCenteredText(text);
        int y = gp.tileSize * 3;

        g2.setColor(Color.black);
        g2.drawString(text, x + 5, y + 5);

        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        x = gp.screenWidth / 2 - (gp.tileSize * 2) / 2;
        y += gp.tileSize * 2;
        g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));

        text = "NEW GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize * 3.5;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "LOAD GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "QUIT";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 2) {
            g2.drawString(">", x - gp.tileSize, y);
        }
        if (UpdateChecker.isLatestVersion == false) {
            if (UpdateChecker.responseCode == 403 || UpdateChecker.responseCode == 429) {
                g2.setFont(Jersey.deriveFont(Font.PLAIN, 25F));
                text = "Rate limit exceeded, please try again later. We are currently trying to find a solution to fix this.";
                x = getXforCenteredText(text);
                y = gp.screenHeight - 20;
                g2.drawString(text, x, y);
                if (commandNum == 3) {
                    g2.drawString(">", x - 24, y);

                }
            } else if (UpdateChecker.responseCode != 200) {
                g2.setFont(Jersey.deriveFont(Font.PLAIN, 25F));
                text = "Failed to check for updates.";
                x = getXforCenteredText(text);
                y = gp.screenHeight - 20;
                g2.drawString(text, x, y);
                if (commandNum == 3) {
                    g2.drawString(">", x - 24, y);

                }
            } else if (UpdateChecker.isLatestVersion == false) {
                g2.setFont(Jersey.deriveFont(Font.PLAIN, 25F));
                text = "A new update is available: " + UpdateChecker.latestVersion;
                x = getXforCenteredText(text);
                y = gp.screenHeight - 20;
                g2.drawString(text, x, y);
                if (commandNum == 3) {
                    g2.drawString(">", x - 24, y);

                }
            }
        }
    }

    public void drawPauseScreen() {

        g2.setFont(Jersey.deriveFont(Font.BOLD, 96F));
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2 - 50;

        g2.setColor(Color.black);
        g2.drawString(text, x + 5, y + 5);

        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        text = "RESUME";
        g2.setFont(Jersey.deriveFont(Font.BOLD, 60F));
        x = getXforCenteredText(text);
        y = gp.screenHeight / 2 + 50;

        g2.setColor(Color.black);
        g2.drawString(text, x + 4, y + 4);

        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        if (pauseCommandNum == 0) {
            g2.setColor(Color.black);
            g2.drawString(">", x - gp.tileSize + 4, y + 4);

            g2.setColor(Color.white);
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "TITLE SCREEN";
        g2.setFont(Jersey.deriveFont(Font.BOLD, 60F));
        x = getXforCenteredText(text);
        y = gp.screenHeight / 2 + 125;

        g2.setColor(Color.black);
        g2.drawString(text, x + 4, y + 4);

        g2.setColor(Color.white);
        g2.drawString(text, x, y);
        if (pauseCommandNum == 1) {
            g2.setColor(Color.black);
            g2.drawString(">", x - gp.tileSize + 4, y + 4);

            g2.setColor(Color.white);
            g2.drawString(">", x - gp.tileSize, y);
        }

    }

    public void drawDialogueScreen() {
        // Draw the dialogue box
        int x = gp.tileSize;
        int y = gp.tileSize * 7;
        int width = gp.screenWidth - (gp.tileSize * 2);
        int height = gp.tileSize * 4;

        drawSubWindow(x, y, width, height);

        x += gp.tileSize;
        y += gp.tileSize;
        width -= gp.tileSize * 2;
        height -= gp.tileSize * 2;
        g2.setColor(Color.white);
        g2.setFont(Jersey.deriveFont(Font.PLAIN, 30F));
        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += g2.getFontMetrics().getHeight();
        }
    }

    public void drawGameOverScreen() {

        g2.setColor(new Color(102, 0, 19, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setFont(Jersey.deriveFont(Font.BOLD, 96F));
        String text = "GAME OVER";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2 - 50;

        g2.setColor(Color.black);
        g2.drawString(text, x + 5, y + 5);

        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        text = "TITLE SCREEN";
        g2.setFont(Jersey.deriveFont(Font.BOLD, 60F));
        x = getXforCenteredText(text);
        y = gp.screenHeight / 2 + 125;

        g2.setColor(Color.black);
        g2.drawString(text, x + 4, y + 4);

        g2.setColor(Color.white);
        g2.drawString(text, x, y);
        if (gameOverCommandNum == 1) {
            g2.setColor(Color.black);
            g2.drawString(">", x - gp.tileSize + 4, y + 4);

            g2.setColor(Color.white);
            g2.drawString(">", x - gp.tileSize, y);
        }
    }

    public void drawHealth() {
        switch (gp.player.health) {
            case 0:
                g2.drawImage(emptyHealth, 35, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(emptyHealth, 85, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(emptyHealth, 135, 30, gp.tileSize, gp.tileSize, null);
                break;
            case 1:
                g2.drawImage(halfHealth, 35, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(emptyHealth, 85, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(emptyHealth, 135, 30, gp.tileSize, gp.tileSize, null);
                break;
            case 2:
                g2.drawImage(fullHealth, 35, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(emptyHealth, 85, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(emptyHealth, 135, 30, gp.tileSize, gp.tileSize, null);
                break;
            case 3:
                g2.drawImage(fullHealth, 35, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(halfHealth, 85, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(emptyHealth, 135, 30, gp.tileSize, gp.tileSize, null);
                break;
            case 4:
                g2.drawImage(fullHealth, 35, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(fullHealth, 85, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(emptyHealth, 135, 30, gp.tileSize, gp.tileSize, null);
                break;
            case 5:
                g2.drawImage(fullHealth, 35, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(fullHealth, 85, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(halfHealth, 135, 30, gp.tileSize, gp.tileSize, null);
                break;
            case 6:
                g2.drawImage(fullHealth, 35, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(fullHealth, 85, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(fullHealth, 135, 30, gp.tileSize, gp.tileSize, null);
                break;
            case 7:
                g2.drawImage(fullHealth, 35, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(fullHealth, 85, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(fullHealth, 135, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(halfHealth, 185, 30, gp.tileSize, gp.tileSize, null);
                break;
            case 8:
                g2.drawImage(fullHealth, 35, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(fullHealth, 85, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(fullHealth, 135, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(fullHealth, 185, 30, gp.tileSize, gp.tileSize, null);
                break;
            case 9:
                g2.drawImage(fullHealth, 35, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(fullHealth, 85, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(fullHealth, 135, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(fullHealth, 185, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(halfHealth, 235, 30, gp.tileSize, gp.tileSize, null);
                break;
            case 10:
                g2.drawImage(fullHealth, 35, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(fullHealth, 85, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(fullHealth, 135, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(fullHealth, 185, 30, gp.tileSize, gp.tileSize, null);
                g2.drawImage(fullHealth, 235, 30, gp.tileSize, gp.tileSize, null);
                break;

        }
    }

    public void drawInventory() {
        // Draw the inventory window

        int x = 312;
        int y = gp.tileSize;
        int width = 325;
        int height = gp.tileSize * 9;

        drawSubWindow(x, y, width, height);
        drawSubWindow(20, gp.tileSize, 270, height);

        final int slotXStart = x + 20;
        final int slotYStart = y + 20;
        slotX = slotXStart;
        slotY = slotYStart + 30;

        int cursorX = slotX + (slotCol * gp.tileSize);
        int cursorY = slotY + (slotRow * gp.tileSize);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        while (gp.gameState == gp.inventoryState && itemIndex < gp.player.inventory.size()) {
            Item item = gp.player.inventory.get(itemIndex);
            String itemName = item.name;
            BufferedImage image = itemImages.getOrDefault(itemName, itemM.items.get("Missing Texture").image);

            g2.drawImage(image, slotX, slotY, gp.tileSize, gp.tileSize, null);
            itemIndex++;
            slotX += gp.tileSize;

            if (slotX >= slotXStart + (gp.tileSize * 6)) {
                slotX = slotXStart;
                slotY += gp.tileSize;
            }
        }

        if (enterPressed) {
            int index = slotCol + slotRow * 6;
            if (index < gp.player.inventory.size()) {
                if (weaponMap.containsKey(gp.player.inventory.get(index).name)) {
                    if (weaponSlot.equals("none")) {
                        weaponSlot = gp.player.inventory.get(index).name;
                        gp.player.inventory.remove(index);
                    } else {
                        gp.player.inventory.add(itemM.items.get(weaponSlot));
                        weaponSlot = gp.player.inventory.get(index).name;
                        gp.player.inventory.remove(index);
                    }
                } else if (shieldMap.containsKey(gp.player.inventory.get(index).name)) {
                    if (shieldSlot.equals("none")) {
                        shieldSlot = gp.player.inventory.get(index).name;
                        gp.player.inventory.remove(index);
                    } else {
                        gp.player.inventory.add(itemM.items.get(shieldSlot));
                        shieldSlot = gp.player.inventory.get(index).name;
                        gp.player.inventory.remove(index);
                    }
                } else {
                    if (!holding.equals("none")) {
                        gp.player.inventory.add(itemM.items.get(holding));
                        holding = gp.player.inventory.get(index).name;
                        gp.player.inventory.remove(index);
                    } else {
                        holding = gp.player.inventory.get(index).name;
                        gp.player.inventory.remove(index);
                    }
                }
            }
            enterPressed = false;
        }

        itemIndex = 0;
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 8, 8);
        int itemNameY = 88;
        int itemNameX = 37;
        int descriptionY = 128;
        int descriptionX = 37;

        g2.setFont(Jersey.deriveFont(Font.PLAIN, 40F));
        g2.setColor(Color.white);
        g2.drawString("Inventory", x + 20, y + 40);

        if (slotCol + slotRow * 6 < gp.player.inventory.size()) {
            g2.drawString(gp.player.inventory.get(slotCol + slotRow * 6).name, itemNameX, itemNameY);
            
            g2.setFont(Jersey.deriveFont(Font.PLAIN, 30F));
            description = itemM.getItemDescription(gp.player.inventory.get(slotCol + slotRow * 6).name);

            // Append durability info if applicable
            if (gp.player.inventory.get(slotCol + slotRow * 6).maxDurability > 1) {
                Item item = gp.player.inventory.get(slotCol + slotRow * 6);
            }

            // Wrap the description text
            String[] wrappedText = wrapText(description, 250); // 250 is the max width for the description
            int lineHeight = g2.getFontMetrics().getHeight();
            for (int i = 0; i < wrappedText.length; i++) {
                g2.drawString(wrappedText[i], descriptionX, descriptionY + (i * lineHeight));
            }
        } else {
            g2.drawString("No item selected", itemNameX, itemNameY);
        }

    }

    public void drawSubWindow(int x, int y, int width, int height) {
        // Draw the window
        Color c = new Color(0, 0, 0, 200);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 20, 20);

        // Draw the border
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 2, y + 2, width, height, 20, 20);
    }

    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth / 2 - length / 2;
        return x;
    }

    private String[] wrapText(String text, int maxWidth) {
        FontMetrics metrics = g2.getFontMetrics();
        String[] manualLines = text.split("\n"); // Split text by manual line breaks
        java.util.List<String> wrappedLines = new java.util.ArrayList<>();

        for (String manualLine : manualLines) {
            String[] words = manualLine.split(" ");
            StringBuilder line = new StringBuilder();

            for (String word : words) {
                if (metrics.stringWidth(line + word) > maxWidth) {
                    wrappedLines.add(line.toString());
                    line = new StringBuilder(word + " ");
                } else {
                    line.append(word).append(" ");
                }
            }
            if (!line.isEmpty()) {
                wrappedLines.add(line.toString());
            }
        }

        return wrappedLines.toArray(new String[0]);
    }
}
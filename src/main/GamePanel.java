package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ai.PathFinder;
import entity.Entity;
import entity.Player;
import item.ItemManager;
import tile.TileManager; 
public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    Font Jersey = new Font("Jersey", Font.PLAIN, 40);

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    // FPS
    int FPS = 60;
    int drawCount = 0; // Declare drawCount as a class-level variable

    // SYSTEM
    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public ItemManager itemM = new ItemManager();
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    public PathFinder pFinder = new PathFinder(this);
    public Entity entity;
    Main main = new Main();
    Thread gameThread;

    // ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public Entity obj[] = new Entity[30];
    public Entity npc[] = new Entity[10];
    public Entity monster[] = new Entity[20];
    
    ArrayList<Entity> entityList = new ArrayList<>();
    public ArrayList<Entity> projectileList = new ArrayList<>();

    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int inventoryState = 4;
    public final int gameOverState = 5;


    public int tick = 0;

    // FULLSCREEN
    private GraphicsDevice graphicsDevice;
    private boolean isFullscreen = false;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addMouseListener(keyH);
        this.setFocusable(true);

        // Get the default screen device for fullscreen mode
        graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        try {
            InputStream is = getClass().getResourceAsStream("/res/fonts/Jersey.ttf");
            Jersey = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, 40F);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.exit(-99);
        }
    }

    public void setupGame() {
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        player.setDefaultValues();
        player.inventory.clear();
        player.setItems();
        gameState = titleState;
        player.health = player.defaultHealth;
        ui.holding = "none";
        ui.weaponSlot = "none";
    }

    public void restartGame() {
        // Reset player state
        player.setDefaultValues();
        player.health = player.defaultHealth;
    
        // Reset game state
        gameState = titleState;
    
        // Clear inventory or other game data if needed
        player.inventory.clear();
        ui.holding = "none";
    
        // Reinitialize objects or tiles if necessary
        aSetter.setObject();
    }

    public void startnewGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

                tick++;
            }
        }
    
    public void update() {
        if (gameState == playState) {
            player.update();

            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].update();
                }
            }
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    if (monster[i].alive == true && monster[i].dying == false) {
                        monster[i].update();
                    }
                    if (monster[i].alive == false) {
                        monster[i] = null;
                    }
                }
            }
            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    if (projectileList.get(i).alive == true) {
                        projectileList.get(i).update();
                    }
                    if (projectileList.get(i).alive == false) {
                        projectileList.remove(i);
                    }
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // TITLE SCREEN
        if (gameState == titleState) {
            ui.draw(g2);
        } else {
            // TILES
            tileM.draw(g2);

            // PLAYER

            entityList.add(player);

            // NPC

            for(int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    entityList.add(npc[i]);
                }
            }
            // MONSTERS
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    entityList.add(monster[i]);
                }
            }

            // OBJECTS

            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) {
                    entityList.add(obj[i]);
                }
            }

            // PROJECTILES

            for (int i = 0; i < projectileList.size(); i++) {
                if(projectileList.get(i) != null) {
                    entityList.add(projectileList.get(i));
                }
            }

            // SORT
            Collections.sort(entityList, new Comparator<Entity>() {

                @Override
                public int compare(Entity e1, Entity e2) {

                    int result = Integer.compare(e1.worldY, e2.worldY);
                    return result;
                    
                }
                
            });
            
            // DRAW ENTITIES
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }
            entityList.clear();

            // UI
            ui.draw(g2);
        }

        // DEBUG
        if (keyH.debugMode == true) {
            g2.setColor(Color.white);
            g2.setFont(Jersey.deriveFont(Font.PLAIN, 40F));
            g2.drawString("Game Version: " + main.gameVersion, 10, 50);
            g2.drawString("Player X: " + player.worldX / tileSize, 10, 75);
            g2.drawString("Player Y: " + player.worldY / tileSize, 10, 100);
            g2.drawString("Player Speed: " + player.speed, 10, 125);
            g2.drawString("Player Direction: " + player.direction, 10, 150);
            g2.drawString("Health: " + player.health, 10, 175);
        }

        g2.dispose();
    }


    // Method to toggle fullscreen mode
    public void toggleFullscreen(JFrame frame) {
        if (!isFullscreen) {
            frame.dispose(); // Dispose the frame to apply fullscreen settings
            frame.setUndecorated(true); // Remove window borders
            graphicsDevice.setFullScreenWindow(frame); // Set fullscreen mode
            isFullscreen = true;
        } else {
            graphicsDevice.setFullScreenWindow(null); // Exit fullscreen mode
            frame.dispose(); // Dispose the frame to reset settings
            frame.setUndecorated(false); // Restore window borders
            frame.setVisible(true); // Make the frame visible again
            isFullscreen = false;
        }
    }

    // Method to update monster position based on player position
    public void updateMonsterPosition(int worldX, int worldY, int playerX, int playerY, int speed) {
        int distanceX = Math.abs(worldX - playerX);
        int distanceY = Math.abs(worldY - playerY);

        if (distanceX > tileSize / 2 || distanceY > tileSize / 2) {
            if (worldX < playerX) worldX += speed;
            else if (worldX > playerX) worldX -= speed;

            if (worldY < playerY) worldY += speed;
            else if (worldY > playerY) worldY -= speed;
        }
    }
}


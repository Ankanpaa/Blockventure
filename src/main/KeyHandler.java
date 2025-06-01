package main;

import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import item.ItemManager;

public class KeyHandler implements KeyListener, MouseListener {
    GamePanel gp;
    ItemManager itemM = new ItemManager();
    public boolean upPressed, downPressed, leftPressed, rightPressed, ePressed, enterPressed, rPressed;
    public boolean debugMode = false;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        

        if (gp.gameState == gp.titleState) {
            handleTitleStateKeys(code);
        } else if (gp.gameState == gp.pauseState) {
            handlePauseStateKeys(code);
        } else if (gp.gameState == gp.gameOverState) {
            handleGameOverStateKeys(code);
        } else if (gp.gameState == gp.inventoryState) {
            handleInventoryStateKeys(code);
        } else if (gp.gameState == gp.playState) {
            handlePlayStateKeys(code);
        } else if (gp.gameState == gp.dialogueState) {
            handleDialogueStateKeys(code);
        }

        // Global keys (work in all states)
        if (code == KeyEvent.VK_F3) debugMode = !debugMode;
        if (code == KeyEvent.VK_P) togglePause();
        if (code == KeyEvent.VK_ESCAPE) handleEscape();
    }

    private void handleTitleStateKeys(int code) {
        if (code == KeyEvent.VK_UP) {
            gp.ui.commandNum--;
            if (gp.ui.commandNum < 0) gp.ui.commandNum = UpdateChecker.isLatestVersion ? 2 : 3;
        }
        if (code == KeyEvent.VK_DOWN) {
            gp.ui.commandNum++;
            if (gp.ui.commandNum > (UpdateChecker.isLatestVersion ? 2 : 3)) gp.ui.commandNum = 0;
        }
        if (code == KeyEvent.VK_ENTER) {
            switch (gp.ui.commandNum) {
                case 0 -> gp.gameState = gp.playState;
                case 1 -> {} // Reserved for future
                case 2 -> System.exit(0);
                case 3 -> openUpdatePage();
            }
        }
    }

    private void handlePauseStateKeys(int code) {
        if (code == KeyEvent.VK_UP) {
            gp.ui.pauseCommandNum--;
            if (gp.ui.pauseCommandNum < 0) gp.ui.pauseCommandNum = 1;
        }
        if (code == KeyEvent.VK_DOWN) {
            gp.ui.pauseCommandNum++;
            if (gp.ui.pauseCommandNum > 1) gp.ui.pauseCommandNum = 0;
        }
        if (code == KeyEvent.VK_ENTER) {
            if (gp.ui.pauseCommandNum == 0) gp.gameState = gp.playState;
            if (gp.ui.pauseCommandNum == 1) gp.gameState = gp.titleState;
        }
    }

    private void handleGameOverStateKeys(int code) {
        if (code == KeyEvent.VK_ENTER) gp.setupGame();
    }

    private void handleInventoryStateKeys(int code) {
        if (code == KeyEvent.VK_UP) {
            gp.ui.slotRow = Math.max(0, gp.ui.slotRow - 1);
        }
        if (code == KeyEvent.VK_DOWN) {
            gp.ui.slotRow = Math.min(3, gp.ui.slotRow + 1);
        }
        if (code == KeyEvent.VK_LEFT) {
            gp.ui.slotCol = Math.max(0, gp.ui.slotCol - 1);
        }
        if (code == KeyEvent.VK_RIGHT) {
            gp.ui.slotCol = Math.min(5, gp.ui.slotCol + 1);
        }
        if (code == KeyEvent.VK_ENTER) {
            gp.ui.enterPressed = true;
        }
        if (code == KeyEvent.VK_I) {
            gp.gameState = gp.playState;
        }
        if (code == KeyEvent.VK_G ) {
            if (!gp.ui.holding.equals("none")) {
                gp.player.inventory.add(itemM.getItem(gp.ui.holding));
            }
            gp.ui.holding = "none";
        }
        if (code == KeyEvent.VK_F) {
            if (!gp.ui.weaponSlot.equals("none")) {
                gp.player.inventory.add(itemM.getItem(gp.ui.weaponSlot));   
            }
            gp.ui.weaponSlot = "none";
        }
        if (code == KeyEvent.VK_H) {
            if (!gp.ui.shieldSlot.equals("none")) {
                gp.player.inventory.add(itemM.getItem(gp.ui.shieldSlot));
            }
            gp.ui.shieldSlot = "none";
        }
    }

    private void handlePlayStateKeys(int code) {
        if (code == KeyEvent.VK_W) upPressed = true;
        if (code == KeyEvent.VK_S) downPressed = true;
        if (code == KeyEvent.VK_A) leftPressed = true;
        if (code == KeyEvent.VK_D) rightPressed = true;
        if (code == KeyEvent.VK_I) gp.gameState = gp.inventoryState;
        if (code == KeyEvent.VK_Q) gp.player.interactWithHoldingItem();
        if (code == KeyEvent.VK_E) gp.player.ePressed = true;
        if (code == KeyEvent.VK_SPACE) gp.player.spacePressed = true;
        if (code == KeyEvent.VK_ENTER) enterPressed = true;
        if (code == KeyEvent.VK_G ) {
            if (!gp.ui.holding.equals("none")) {
                gp.player.inventory.add(itemM.getItem(gp.ui.holding));
            }
            gp.ui.holding = "none";
        }
        if (code == KeyEvent.VK_F) {
            if (!gp.ui.weaponSlot.equals("none")) {
                gp.player.inventory.add(itemM.getItem(gp.ui.weaponSlot));   
            }
            gp.ui.weaponSlot = "none";
        }
        if (code == KeyEvent.VK_H) {
            if (!gp.ui.shieldSlot.equals("none")) {
                gp.player.inventory.add(itemM.getItem(gp.ui.shieldSlot));
            }
            gp.ui.shieldSlot = "none";
        }
        if(code == KeyEvent.VK_R) {
            rPressed = true;
        }
    }

    private void handleDialogueStateKeys(int code) {
        if (code == KeyEvent.VK_ENTER) gp.gameState = gp.playState;
    }

    private void togglePause() {
        if (gp.gameState == gp.playState) gp.gameState = gp.pauseState;
        else if (gp.gameState == gp.pauseState) gp.gameState = gp.playState;
    }

    private void handleEscape() {
        if (gp.gameState == gp.playState) gp.gameState = gp.pauseState;
        else if (gp.gameState == gp.titleState) System.exit(0);
        else gp.gameState = gp.playState;
    }

    private void openUpdatePage() {
        if (UpdateChecker.responseCode == 200 && !UpdateChecker.isLatestVersion) {
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(new URI("https://www.github.com/Ankanpaa/Blockventure/releases/" + UpdateChecker.latestVersion));
                }
            } catch (IOException | URISyntaxException excp) {
                excp.printStackTrace();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;
        if (code == KeyEvent.VK_E) gp.player.ePressed = false;
        if (code == KeyEvent.VK_SPACE) gp.player.spacePressed = false; gp.player.defending = false; gp.player.invincible = false;
        if (code == KeyEvent.VK_R) rPressed = false;

    }

    // --- MouseListener methods ---

    @Override
    public void mousePressed(MouseEvent e) {
        int mouseCode = e.getButton();
        int mouseX = e.getX();
        int mouseY = e.getY();

        if (mouseCode == MouseEvent.BUTTON1 && gp.gameState == gp.inventoryState) {
            // Inventory slot selection
            if (mouseX > 7 * gp.tileSize && mouseX < 13 * gp.tileSize && mouseY > 2 * gp.tileSize && mouseY < 6 * gp.tileSize) {
                gp.ui.slotCol = mouseX / gp.tileSize - 7;
                gp.ui.slotRow = mouseY / gp.tileSize - 2;
            }
        }
        }
    


    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}

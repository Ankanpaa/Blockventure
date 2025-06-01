package main;

import java.util.Scanner;

import entity.Player;
import item.ItemManager;

public class Commands {

    private Scanner scanner;
    GamePanel gp;
    ItemManager itemM = new ItemManager();

    public Commands(GamePanel gp) {
        gp = new GamePanel();
        scanner = new Scanner(System.in);
    }

    public String getCommand() {
        System.out.print("> ");
        return scanner.nextLine().trim();
    }

    public void processCommand(String command) {
        String[] parts = command.split(" "); // Split command into parts
        String action = parts[0].toLowerCase(); // First word is the action
        String arg1 = parts.length > 1 ? parts[1] : ""; // Second word is the argument (if any)
        String arg2 = parts.length > 2 ? parts[2] : ""; // Second argument

        switch (action) {

            case "inventory":
                inventory(arg1, arg2);
                break;
            case "help":
                showHelp();
                break;
            default:
                System.out.println("Unknown command. Type 'help' for a list of commands.");
        }
    }
    

    private void inventory(String arg1, String arg2) {
        switch(arg1) {
            case "add":
                gp.player.inventory.add(itemM.getItem(arg2));
                System.out.println("Added " + arg2 + " to inventory.");
                break;
            case "remove":
                gp.player.inventory.remove(itemM.getItem(arg2));
                System.out.println("Removed " + arg2 + " from inventory.");
                break;
            default:
                System.out.println("Your inventory contains:" + gp.player.inventory);
                
        
        }
    }

    private void showHelp() {
        System.out.println("Available commands:");
        System.out.println("  move <direction> - Move in a direction (north, south, east, west)");
        System.out.println("  attack <target>  - Attack a target");
        System.out.println("  inventory        - Show your inventory");
        System.out.println("  help             - Show this help message");
    }
}

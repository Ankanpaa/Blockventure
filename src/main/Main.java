package main;

import javax.swing.JFrame;

public class Main {
    public static String gameVersion = "v0.1.0-alpha" + " ";
    public static JFrame window;

    public static void main(String[] args) {
        // Start the command processing in a separate thread
        GamePanel gamePanel = new GamePanel();
        new Thread(() -> {
            Commands commands = new Commands(gamePanel);
            boolean running = true;

            while (running) {
                String command = commands.getCommand();
                if (command.equalsIgnoreCase("quit")) {
                    running = false;
                    System.exit(0); // Exit the game when "quit" is typed
                } else {
                    commands.processCommand(command);
                }
            }
        }).start();

        // Check for updates
        UpdateChecker.checkForUpdates();

        // Initialize the game window
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle("Blockventure " + gameVersion);

        
        window.add(gamePanel);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        // Start the game logic
        gamePanel.setupGame();
        gamePanel.startnewGameThread();
    }
}

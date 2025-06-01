package tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum [][];

    public TileManager(GamePanel gp) {

        this.gp = gp;

        tile = new Tile[10];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
        //
        // Load the map file. The map file is a text file that contains the tile numbers for each tile in the map.
        // Type the file path /res/maps/YOURMAP.txt
        //
        loadMap("/res/maps/map02.map");
    }

    public void getTileImage() {

            // REGISTER TILES
            // If you are a modder, import your tile files into the /res/tiles folder
            // and register them here. "tileID" corresponds to the number in the map file.
            // Set the "imageName" to the name of the image file without the extension.
            // If you want to add a new tile, for example "sand", copy the line below and set the "tileID" to the ID you want to use.
            // The tileID must be unique and not used by any other tile.
            //
            // setup(tileID, imageName, collision);

            setup(0, "grass", false);
            setup(1, "bricks", true);
            setup(2, "water", true);
            setup(3, "dirt", false);
            setup(4, "sand", false);
            setup(5, "tree", true);
            setup(6, "redbricks", true);
            setup(7, "planks", false);
            setup(8, "cobblestone", true);

    }
    public void setup(int tileID, String imageName, boolean collision) {
        UtilityTool uTool = new UtilityTool();

        try {

            tile[tileID] = new Tile();
            tile[tileID].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/" + imageName + ".png"));
            tile[tileID].image = uTool.scaleImage(tile[tileID].image, gp.tileSize, gp.tileSize);
            tile[tileID].collision = collision;
            System.out.println("Loaded image: " + imageName);

        }catch(IOException e) {
            System.out.println("ERROR while trying to load tile image:");
            System.out.println("");
            e.printStackTrace();
        }
    }
    public void loadMap(String filePath) {
        try {

            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine();

                while(col < gp.maxWorldCol) {
                    
                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);
                    
                    mapTileNum[col][row] = num;
                    col++;
                }
                if(col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            System.out.println("Loaded map: " + filePath);
            br.close();

        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2) {
        
        int worldCol = 0;
        int worldRow = 0;

        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = mapTileNum[worldCol][worldRow];
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
               worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
               worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
               worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, null);
            }
            worldCol++;
            if(worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
          }
        }
            
    }
        
}

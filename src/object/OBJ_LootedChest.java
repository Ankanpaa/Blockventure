package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Entity;
import main.GamePanel;

public class OBJ_LootedChest extends Entity {

    public OBJ_LootedChest(GamePanel gp) {
        super(gp);
        name = "LootedChest";
        down1 = setup("/res/objects/openedchest");
        collision = true;
    }

}

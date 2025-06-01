package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_BerryBush extends Entity {

    public OBJ_BerryBush(GamePanel gp) {
        super(gp);
        name = "BerryBush";
        down1 = setup("/res/objects/berrybush");
        collision = true;
    }

}

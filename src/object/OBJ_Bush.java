package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Bush extends Entity {

    public OBJ_Bush(GamePanel gp) {
        super(gp);
        name = "Bush";
        down1 = setup("/res/objects/bush");
        collision = true;
    }

}

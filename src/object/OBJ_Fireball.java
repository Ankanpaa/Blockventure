package object;

import entity.Projectile;
import main.GamePanel;

public class OBJ_Fireball extends Projectile {

    GamePanel gp;

    public OBJ_Fireball(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Fireball";
        speed = 5;
        maxHealth = 80;
        health = maxHealth;
        attack = 3;
        alive = false;
        getImage();
        
    }
    public void getImage() {
        up1 = setup("/res/projectile/fireball_up_1");
        up2 = setup("/res/projectile/fireball_up_2");
        down1 = setup("/res/projectile/fireball_down_1");
        down2 = setup("/res/projectile/fireball_down_2");
        left1 = setup("/res/projectile/fireball_left_1");
        left2 = setup("/res/projectile/fireball_left_2");
        right1 = setup("/res/projectile/fireball_right_1");
        right2 = setup("/res/projectile/fireball_right_2");
    }

}

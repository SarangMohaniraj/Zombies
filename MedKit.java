import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import java.net.URL;

public class MedKit extends PowerUp{


    public MedKit(Point2D pos){
        powerUp = new Image("media/img/medKit.png");
        this.pos = pos;
        hitbox = new Rectangle2D(pos.getX(), pos.getY(), 40, 40);
    }

    public void act(){
        Player.hp = 100;
        URL resource = getClass().getResource("media/audio/mushroom.mp3");
		AudioClip mushroom = new AudioClip(resource.toString());
        mushroom.play();
    }
}
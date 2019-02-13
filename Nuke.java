import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import java.net.URL;

public class Nuke extends PowerUp{


    public Nuke(Point2D pos){
        powerUp = new Image("media/img/nuke.png");
        this.pos = pos;
        hitbox = new Rectangle2D(pos.getX(), pos.getY(), 40, 40);

    }

    public void act(){
        Zombies.round.clear();
        Zombies.score += 400;

        URL resource = getClass().getResource("media/audio/kaboom.mp3");
        AudioClip kaboom = new AudioClip(resource.toString());
        kaboom.play();
    }

}
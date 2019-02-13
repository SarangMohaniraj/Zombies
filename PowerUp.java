import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public abstract class PowerUp {
    int lifetime = 15*60;
    Image powerUp;
    Point2D pos;
    Rectangle2D hitbox;
    boolean checked;

    public abstract void act();
}
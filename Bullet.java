import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.SnapshotParameters;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

public class Bullet {
   static Image bullet;
   int x = 0;
   int y = 0;
   int speed = 80;
   double sx;
   double sy;
   Point2D pos;
   Double angle;
   Rectangle2D hitbox;
   int hit = 0;

   public Bullet(Double angle){
       //bullet = new Image("media/img/bullet.png");
       this.angle = angle;

       bullet = new Image("media/img/bullet.png");  //sets a new image so the image quality does not deteriote
	   ImageView rotate = new ImageView(bullet);
	   rotate.setRotate(angle);
	   SnapshotParameters params = new SnapshotParameters();
	   params.setFill(Color.TRANSPARENT);
	   bullet = rotate.snapshot(params, null); //takes picture of the old image

	   sx = speed * Math.sin(Math.toRadians(angle)); //x is sin because it is rotated 90 degrees
       sy = speed * Math.cos(Math.toRadians(angle));


   }

   public void move(){ //called when gun is shot (mouse clicked)

        x += sx;
        y -= sy;



   }


}
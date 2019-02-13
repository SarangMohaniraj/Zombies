import java.net.URL;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.SnapshotParameters;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;

public class Zombie {
    static double damage = 5;
     double hp = 100;
    Image zombie;
    Point2D pos;
    double x = 0;
    double y = 0;
    int spawnX;
    int spawnY;
    static double speed = 3;
    Double angle;
    double sx;
    double sy;
    double width = 80.0;
    double height = 80.0;
    Rectangle2D hitbox;
    int cooldown = 0;
    int attackDelay = 2; //2 seconds between each attack



    public Zombie(int spawnX, int spawnY){
        zombie = new Image("media/img/zombie.png");
        pos = new Point2D(spawnX+x, spawnY+y);
        this.spawnX = (int)pos.getX();
        this.spawnY = (int)pos.getY();
        hitbox = new Rectangle2D(pos.getX(), pos.getY(),80,80);
        }
    public Image getImage(){
        return zombie;
    }
    public void move(Point2D playerPos, Point2D zombiePos, Image player){
        hitbox = new Rectangle2D(pos.getX(), pos.getY(),80,80);
        cooldown--;
        if((this.hitbox).intersects(Player.hitbox)){
            if(cooldown < 0){
                if(Player.hp > 0)
                    attack(); //start attacking
            }
            return;



        }


        double xZomb = pos.getX() + (width/2)-1;
        double yZomb = pos.getY() + (height/2)-1;

        double xPlayer = playerPos.getX()+ (40)-1;//player width/2 = 80/2 = 40
        double yPlayer = playerPos.getY()+ (40)-1;

        double hypotnuseX = xPlayer - xZomb;
        double hypotnuseY = yPlayer - yZomb;
/*
        double distance = Math.sqrt(hypotnuseX * hypotnuseX + hypotnuseY * hypotnuseY);
        //distance = playerPos.distance(zombiePos);
        int steps = (int)(distance/speed);

       	double stepX = hypotnuseX/steps;
       	double stepY = hypotnuseY/steps;

       	x += stepX;
       	y += stepY;
       	steps--;
        */

        angle = Math.toDegrees(Math.atan2(hypotnuseY, hypotnuseX)); //arctan (y/x) but corrects for quadrant

/*
        if((angle.isNaN())){
            if(Math.toDegrees(Math.atan(hypotnuseY/(hypotnuseX-1))) > 180) //catches vertical angles
                angle = 90.0;
            else
                angle = 180.0;
        }
*/
        angle += 90; //sets 0 degrees to north

		if(angle < 0)
			angle+=360;

        zombie = new Image("media/img/zombie.png");  //sets a new image so the image quality does not deteriote
        ImageView rotate = new ImageView(zombie);
        rotate.setRotate(angle);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        zombie = rotate.snapshot(params, null); //takes picture of the old image

	//speed vectors
        sx = speed * Math.sin(Math.toRadians(angle)); //x is sin because it is rotated 90 degrees
        sy = speed * Math.cos(Math.toRadians(angle)); //same with y

        x += sx;
        y -= sy;


    }
    public void attack(){
        URL resource = getClass().getResource("media/audio/punch.wav");
        AudioClip punch = new AudioClip(resource.toString());
        punch.play();

        Player.hp -= damage;
        cooldown = attackDelay * 60; //cancels out 60 times/second
     }
    public void die(){

            return; //how do I make it die?
    }
    public boolean containsPowerUp(){
      return (int)(Math.random()*11) == 1;
    }
    public PowerUp dropPowerUp(){
        int type =0;

        type = (int)(Math.random()*2);
        if(type == 0)
            return new MedKit(pos);
        else
            return new Nuke(pos);
    }
}
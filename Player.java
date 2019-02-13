import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;
import java.util.ArrayList;
import javafx.scene.media.AudioClip;
import java.net.URL;
import javafx.scene.paint.Color;

public class Player implements EventHandler<InputEvent> {
    static Image player = new Image("media/img/player.png");
    static int x = 0;
    static int y = 0;
    int startX = (int)(Zombies.canvas.getWidth())/2 - (int)player.getWidth()/2;
    int startY = (int)(Zombies.canvas.getHeight())/2- (int)player.getHeight()/2;
    int speed = 30;
    static Point2D playerPos;
    static Double angle = 0.0;
    double width = 80.0;
    double height = 80.0;
    double xMousePos=0;
    double yMousePos=0;
    static int hp = 100;
    static Rectangle2D hitbox;
    static ArrayList<Bullet> shotsFired = new ArrayList<>();
    int damage = 10;


    public Player(){

        playerPos = new Point2D(startX + x,startY + y);
        hitbox = new Rectangle2D(playerPos.getX(), playerPos.getY(),80,80);
    }
    public Image getImage(){
        return player;
    }



    public void handle(final InputEvent event){



        if(event instanceof KeyEvent){
			if(null != ((KeyEvent)event).getCode())
                //moves player
            switch (((KeyEvent)event).getCode()) {
            //KEY PRESS LEFT
                case A:
                case LEFT:
                    x -= speed;
                    break;
            //KEY PRESS RIGHT
                case D:
                case RIGHT:
                    x += speed;
                    break;
            //KEY PRESS UP
                case W:
                case UP:
                    y -= speed;
                    break;
            //KEY PRESS DOWN
                case S:
                case DOWN:
                    y += speed;
                    break;
                default:
                    break;
            }
        }


        if(event instanceof MouseEvent){

		  if(((MouseEvent)event).getClickCount() > 0){
				shoot();



           }



        }

        //rotates player
        rotate(event);

    }

    public void rotate(final InputEvent event){
        hitbox = new Rectangle2D(playerPos.getX(), playerPos.getY(),80,80);

        //get angle to rotate image
        playerPos = new Point2D(startX + x,startY + y);
        double xCenter = playerPos.getX()+ (player.getWidth()/2)-1; //middle of screen
        double yCenter = playerPos.getY()+ (player.getHeight()/2)-1;

        if(event instanceof MouseEvent){
            xMousePos = ((MouseEvent)event).getX();
            yMousePos = ((MouseEvent)event).getY();
        }

        double hypotnuseY = yMousePos - yCenter;
        double hypotnuseX = xMousePos - xCenter;

        angle = Math.toDegrees(Math.atan2(hypotnuseY, hypotnuseX)); //arctan (y/x) but corrects for quadrant

        if((angle.isNaN())){
            if(Math.toDegrees(Math.atan(hypotnuseY/(hypotnuseX-1))) > 180) //catches vetical angles
                angle = 90.0;
            else
                angle = 180.0;
        }

        angle += 90.0; //sets 0 degrees to north


        //rotate image
        player = new Image("media/img/player.png");  //sets a new image so the image quality does not deteriote
        ImageView rotate = new ImageView(getImage());
        rotate.setRotate(angle);
        SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
        player = rotate.snapshot(params, null); //takes picture of the old image


    }
    public Bullet shoot(){
	Bullet b = new Bullet(angle);
	shotsFired.add(b);
        b.move();

        URL resource = getClass().getResource("media/audio/pew.mp3");
        AudioClip pew = new AudioClip(resource.toString());
        pew.play();
        return b;
    }



}
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.*;
import javafx.scene.media.AudioClip;
import java.net.URL;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.event.*;
import javafx.geometry.Point2D;
import javafx.scene.input.*;
import javafx.scene.text.*;

public class Zombies extends Application implements EventHandler<InputEvent>{

    GraphicsContext gc;
    static Canvas canvas;
    AnimateObjects animate;
    Scene scene;
    Player player;
    Zombie zombie; //make into a list of zombies later
    Image map = new Image("media/img/map.gif");

    URL resource = getClass().getResource("media/audio/hitmarker.mp3");
    AudioClip hitmarker = new AudioClip(resource.toString());

    URL resource1 = getClass().getResource("media/audio/laugh.mp3");
    AudioClip laugh = new AudioClip(resource1.toString());

    URL resource2 = getClass().getResource("media/audio/round_start.mp3");
    AudioClip round_start = new AudioClip(resource2.toString());

    URL resource3 = getClass().getResource("media/audio/round_end.mp3");
    AudioClip round_end = new AudioClip(resource3.toString());

    int deathSoundCounter = 0;
    int roundSoundCounter = 0;


    static int score = 0;

    int roundGapTimer = 10*60;


    int roundNumber = 1;
    int spawnRandom = 200;


    ArrayList<Zombie> setRound = new ArrayList<>();
    static ArrayList<Zombie> round = new ArrayList<>();
    int roundSizeInit = 20;
    int roundSize = roundSizeInit;

    ArrayList<PowerUp> powerUps = new ArrayList<>();



	public static void main(String[]args){
		launch();
	}

	public void start(Stage stage){
		stage.setTitle("Zombies");
		Group root = new Group();
		canvas = new Canvas(1000, 700);
		root.getChildren().add(canvas);
		scene = new Scene(root);

		if(player.hp > 0){
			scene.addEventHandler(KeyEvent.KEY_PRESSED, this);
			scene.addEventHandler(MouseEvent.MOUSE_MOVED, this);
			scene.addEventHandler(MouseEvent.MOUSE_CLICKED, this);
		}

		stage.setScene(scene);

		animate = new AnimateObjects();
		animate.start();
		stage.show();

		gc = canvas.getGraphicsContext2D();


		player = new Player();
		gc.drawImage(player.getImage(), player.startX, player.startY);

		setRound(roundNumber);


		round_start.play();
		laugh.play();

		gc.setFill(Color.WHITE); //Fills the text in white
		gc.setStroke(Color.BLACK); //Changes the outline the black
		gc.setLineWidth(1); //How big the black lines will be
		Font font = Font.font("Arial Narrow",FontWeight.NORMAL, 72 );
		gc.setFont(font);

		gc.fillText(score+" " , 850, 650 ); //draws the white part of the text
		gc.strokeText(score+" " , 850, 650 ); //draws the outline part of the text





	}

	public void handle(final InputEvent event){
		if(player.hp > 0)
			player.handle(event); //moves player

		if (event instanceof KeyEvent){
			if (((KeyEvent)event).getCode() == KeyCode.ESCAPE)
				reset();
		}

	}
        public void setRound(int roundNumber){
            roundSize = roundSizeInit*(int)(Math.pow(1.3, roundNumber-1));
            for(int i = 0; i<roundSize; i++){
				int xRandom = (int)(Math.random()*canvas.getWidth());
				int yRandom = (int)(Math.random()*canvas.getHeight());

				int randomSpot = (int)(Math.random()*4);
				if(randomSpot == 0)
					setRound.add(new Zombie(xRandom, 0));//top
				else if(randomSpot == 1)
					setRound.add(new Zombie(xRandom, (int)canvas.getHeight()));//bottom
				else if(randomSpot == 2)
					setRound.add(new Zombie(0, yRandom));//left
				else
					setRound.add(new Zombie((int)canvas.getWidth(), yRandom));//right
				setRound.get(i).hp = 100;


				if(roundNumber != 1){
					setRound.get(i).damage = 10 * (Math.pow(1.1, roundNumber-1));
					setRound.get(i).hp = 100 * (Math.pow(1.1, roundNumber-1));
					setRound.get(i).speed = 3 * (Math.pow(1.1, roundNumber-1));


				}

			}
            roundGapTimer = 10*60 *(int)(Math.pow(.94, roundNumber-1));
            spawnRandom = 200 *(int)(Math.pow(.98, roundNumber-1));
            roundSoundCounter = 0;
        }

        public void reset(){


            setRound = new ArrayList<>();
            round = new ArrayList<>();
            roundSize = 1;
            deathSoundCounter = 0;
            roundSoundCounter = 0;

            score = 0;
            roundGapTimer = 10*60;

            roundNumber = 1;
			spawnRandom = 200;
            gc.clearRect(0,0, canvas.getWidth(), canvas.getHeight());
            gc = canvas.getGraphicsContext2D();


            player = new Player();
            player.hp = 100;
            player.startX = (int)(canvas.getWidth())/2 - (int)player.player.getWidth()/2;
            player.startY = (int)(canvas.getHeight())/2 - (int)player.player.getHeight()/2;

            gc.drawImage(player.getImage(), player.startX, player.startY);

            setRound(roundNumber);



            round_start.play();

            laugh.play();

        }

	public class AnimateObjects extends AnimationTimer{
		public void handle(long now){
			gc.clearRect(0,0, canvas.getWidth(), canvas.getHeight()); //removes trail

			gc.drawImage(map, 0, 0, canvas.getWidth(), canvas.getHeight());





			if(player.startX + player.x >= canvas.getWidth() - player.getImage().getWidth()) //horizontal (right) bounds
				player.x-=player.speed;
			if(player.startX + player.x <= 0) //horizontal (left) bounds
				player.x+=player.speed;

			if(player.startY + player.y >= canvas.getHeight() - player.getImage().getHeight()) //vertical (bottom) bounds
				player.y-=player.speed;
			if(player.startY + player.y <= 0) //vertical (top) bounds
				player.y+=player.speed;


			player.playerPos = new Point2D(player.startX + player.x,player.startY + player.y);

			gc.drawImage(player.getImage(), player.playerPos.getX(), player.playerPos.getY()); //animates player



			if((int)(Math.random()*spawnRandom) <= 1){
				if(setRound.size() > 0)
					round.add(setRound.remove(0));
			}

			for(Zombie z : round){
				z.pos = new Point2D(z.spawnX+z.x, z.spawnY+z.y);
				z.move(player.playerPos, z.pos, player.getImage());
				gc.drawImage(z.getImage(), z.pos.getX(), z.pos.getY()); //animates zombie
			}


			ArrayList<Bullet> removeBullet = new ArrayList<>();
			ArrayList<Zombie> removeZombie = new ArrayList<>();
			for(Bullet b: player.shotsFired){ //animates bullet
				b.pos = new Point2D(player.playerPos.getX()+player.width/2-10 , player.playerPos.getY()+player.height/2);
				b.move();
				b.hitbox = new Rectangle2D(b.pos.getX()+b.x, b.pos.getY()+b.y, 40, 40);
				if(b.pos.getX() + b.x < 0 || b.pos.getX() + b.x > canvas.getWidth() || b.pos.getY() + b.y < 0 || b.pos.getY() + b.y > canvas.getHeight())
					removeBullet.add(b);
				gc.drawImage(b.bullet, b.pos.getX() + b.x, b.pos.getY() + b.y, 40, 40);
				for(Zombie z : round){

					if(b.hitbox.intersects(z.hitbox)){
						score += player.damage;
						removeBullet.add(b); //removes the bullet later
						z.hp -= player.damage;
						if(z.hp <= 0){
							if(z.containsPowerUp())
								powerUps.add(z.dropPowerUp());
							removeZombie.add(z);
						}

						b.hit++;
						if(b.hit == 1) //the sound only plays once (not 60 times/s)
							hitmarker.play();
					}
				}
				round.removeAll(removeZombie);

			}
			 player.shotsFired.removeAll(removeBullet);

				if(round.size() == 0 && setRound.size() == 0){
					if(roundSoundCounter == 0)
						round_end.play();
					roundSoundCounter++;

					if(roundGapTimer <= 0){

						if(roundGapTimer == 0){
							round_start.play();
							roundNumber++;
							setRound(roundNumber);
						}
					}
					roundGapTimer--;


			   }



			ArrayList<PowerUp> removePowerUps = new ArrayList<>();
				for(PowerUp p: powerUps){
					p.lifetime--;
					if(p.lifetime <= 0){
						removePowerUps.add(p);
					}
					else{
						gc.drawImage(p.powerUp, p.pos.getX(), p.pos.getY(), 40, 40);

						if(player.hitbox.intersects(p.hitbox)){
							p.act();
							removePowerUps.add(p);
						}
					}
				}
				powerUps.removeAll(removePowerUps);



			gc.fillText(score+"" , 850, 650 ); //draws the white part of the text
			gc.strokeText(score+"" , 850, 650 ); //draws the outline part of the text

			gc.setFill(Color.DARKRED);
			gc.fillText(roundNumber+"" , 100, 650 );
			gc.strokeText(roundNumber+"" , 100, 650 );

			gc.setFill(Color.WHITE);//reset color

			if(player.hp <= 0){
				if(deathSoundCounter == 0){
					deathSoundCounter++;
					laugh.play();
				}
				setRound.clear();



				gc.fillText("GAME OVER", 300, 200 ); //draws the white part of the text
				gc.strokeText("GAME OVER", 300, 200 ); //draws the outline part of the text


				Font font = Font.font("Arial Narrow",FontWeight.NORMAL, 48 );
				gc.setFont(font);
				gc.fillText("YOU SURVIVED " + roundNumber + " ROUNDS", 220, 280 ); //draws the white part of the text
				gc.strokeText("YOU SURVIVED " + roundNumber + " ROUNDS", 220, 280 ); //draws the outline part of the text
				font = Font.font("Arial Narrow",FontWeight.NORMAL, 72 );
				gc.setFont(font);


				gc.fillText("Press Esc to Restart", 220, canvas.getHeight()/2+200 );
				gc.strokeText("Press Esc to Restart", 220, canvas.getHeight()/2+200 );

                }

			}
		}
	}
//}
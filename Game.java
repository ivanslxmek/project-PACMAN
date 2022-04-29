/*
ISTE 121 Project Pacman
Group 3
Bruno Leka & Ivan Trstenjak */

import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.effect.Light.Point;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.animation.*;
import java.io.*;
import java.util.*;
import java.util.prefs.BackingStoreException;

/**
 * PackmanGEOStarter with JavaFX and Thread
 */

public class Game extends Application {
   // Window attributes
   private Stage stage;
   private Scene scene;

   private StackPane root;

   private static String[] args;
   private final static String PACMAN_IMAGE = "packman.png"; // file with icon for a racer

   private PacmanRacer racer = null; // array of racers
   private ArrayList<Ghost> ghosts = new ArrayList<>();

   private AnimationTimer timer; // timer to control animation

   private int counter = 0;


   private final static  String POINT = "Dots.png";
   private ArrayList<Points> pointss = new ArrayList<>();

   // boolean for pacman colision

   // main program
   public static void main(String[] _args) {
      args = _args;
      launch(args);
   }

   // *****************- START *****************- */
   // start() method, called via launch
   public void start(Stage _stage) throws IOException {

      // stage seteup
      System.out.println(stage);
      stage = _stage;
      stage.setTitle("PacMan - Leka");
      stage.setOnCloseRequest(
            new EventHandler<WindowEvent>() {
               public void handle(WindowEvent evt) {
                  System.exit(0);
               }
            });

      // root pane
      root = new StackPane();
      // strart menu method

      initializeScene();

   }

   // *****************- initializeScene -*****************- */
   // start the race
   public void initializeScene() {

      // Make an icon image to find its size

      // Get image size
      ImageView backgroundImage = null;
      try {
         FileInputStream fis = new FileInputStream(new File("map.png"));
         Image image = new Image(fis);
         backgroundImage = new ImageView(image);

      } catch (FileNotFoundException e) {

         e.printStackTrace();
      }

      // root.setBackground(background);
      racer = new PacmanRacer();
      root.getChildren().addAll(backgroundImage);
      root.setId("pane");

      for (int i = 0; i < 5; i++) {
         Ghost ghost1 = new Ghost();
         ghosts.add(ghost1);
         root.getChildren().addAll(ghost1);
      }

      racer.setGhosts(ghosts);

      for ( int i=0; i < 8; i++){
         counter++;
         System.out.println(counter);
         Points points = new Points();
         pointss.add(points);
         
         root.getChildren().addAll(points);
      }
      root.getChildren().addAll(racer);
      // display the window

      scene = new Scene(root, 1000, 600);

      // scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
      stage.setResizable(false);

      // set an icon for the game
      try {
         stage.getIcons().add(new Image(new FileInputStream(PACMAN_IMAGE)));
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
      // stage.getIcons().add(new Image("pacman-icon.jpg"));
      stage.setScene(scene);
      stage.show();

      System.out.println("Starting race...");

      // Use an animation to update the screen
      timer = new AnimationTimer() {
         public void handle(long now) {
            racer.update();
            for (Ghost ghost : ghosts) {
               ghost.update();
            }
         }
      };

      // TimerTask to delay start of race for 2 seconds
      TimerTask task = new TimerTask() {
         public void run() {
            timer.start();
         }
      };
      Timer startTimer = new Timer();
      long delay = 1000L;
      startTimer.schedule(task, delay);

      setupEventHandling();
   } // end of initializeScene()

   public void setupEventHandling() {
      // Using the onKeyPress method to get the key pressed..
      scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

         @Override
         public void handle(KeyEvent pressEvent) {

            String key = pressEvent.getText();

            switch (key.toLowerCase()) {
               case "w":
                  racer.goUp();
                  break;
               case "s":
                  racer.goDown();
                  break;
               case "a":
                  racer.goLeft();
                  break;
               case "d":
                  racer.goRight();
                  break;
            }
         }

      });

   }
} // end class Races

// switch case that takes the key pressed and makes the pacman image -
// // move in the direction of the corresponding key pressed
// switch (key.toLowerCase()) {
// case "w":
// if ((getColor(racePosX + halfSize, racePosY) > 0.45)) {

// racePosY += 2;
// raceROT = 270;
// } else {
// racePosY -= (int) (iconWidth / 6);
// raceROT = 270;
// System.out.println("Moving up " + racePosY);
// }
// break;

// case "a":

// if ((getColor(racePosX , racePosY + halfSize) > 0.45)) {
// racePosX += 2;
// raceROT = 180;
// } else {
// racePosX -= (int) (iconWidth / 6);
// raceROT = 180;
// // when the image is turned to the left the pacmans eye is below the mouth
// so-
// // we changed the scale to mirror the image and make the pacman upright
// aPicView.setScaleY(-1);
// System.out.println("Moving left " + racePosX);

// }
// break;

// case "s":

// if ((getColor(racePosX + halfSize , racePosY + halfSize*2) > 0.45)) {
// racePosY -= 2;
// raceROT = 90;
// } else {
// racePosY += (int) (iconWidth / 6);
// raceROT = 90;
// System.out.println("Moving down " + racePosY);

// }

// break;

// case "d":
// if ((getColor(racePosX + halfSize*2 , racePosY + halfSize) > 0.45)) {
// racePosX -= 2;
// raceROT = 0;
// } else {
// racePosX += (int) (iconWidth / 6);
// System.out.println("moving right " + racePosX);

// // as the immage was mirrored the pacman faces the same problem when he turns
// // right so -
// // the image is mirrored again to fix that problemraceROT = 0;
// raceROT = 0;
// aPicView.setScaleY(1);

// }

// break;

// // to load the menu
// case "p":

// // do something to start the menu pane from startMenu()
// System.exit(0);

// break;
// }

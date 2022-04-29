/*
ISTE 121 Project Pacman
Group 3
Bruno Leka & Ivan Trstenjak 
*/

import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
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

// *****************- PacmanRacer -************************ */
/**
 * Racer creates the race lane (Pane) and the ability to
 * keep itself going (Runnable)
 */
public class PacmanRacer extends Pane {
   private int racePosX = 0; // x position of the racer
   private int racePosY = 0; // y position of the racer
   private int raceROT = 0; // x position of the racer
   private ImageView aPicView; // a view of the icon ... used to display and move the image
   private Image PacManOpen = null;
   private final static String PACMAN_IMAGE = "packman.png"; // file with icon for a racer
   private int iconWidth; // width (in pixels) of the icon
   private int iconHeight; // height (in pixels) or the icon
   private int speed = 10;
   private ArrayList<Ghost> ghosts = new ArrayList<>();

   public PacmanRacer() {
      // Draw the icon for the racer
      try {
         PacManOpen = new Image(new FileInputStream(PACMAN_IMAGE));
         // background = new Background((, new FileInputStream("pacman-map1.png")));
      } catch (Exception e) {
         System.out.println("Exception: " + e);
         System.exit(1);
      }
      iconWidth = (int) PacManOpen.getWidth();
      iconHeight = (int) PacManOpen.getHeight();
      aPicView = new ImageView(PacManOpen);
      this.getChildren().add(aPicView);
   }

   public void setGhosts(ArrayList<Ghost> ghosts) {
      this.ghosts = ghosts;
   }

   /**
    * update() method keeps the thread (racer) alive and moving.
    */
   public void update() {

      int halfSize = (int) PacManOpen.getWidth() / 2;

      // moving to oposite sides of the screen

      switch (raceROT) {
         case 0:
            // right
            if ((getColor(racePosX + halfSize * 2, racePosY + halfSize) < 0.45)) {
               racePosX += speed;
            }
            break;
         case 90:
            // down
            if ((getColor(racePosX + halfSize, racePosY + halfSize * 2) < 0.45)) {
               racePosY += speed;
            }
            break;
         case 180:
            // left
            if ((getColor(racePosX, racePosY + halfSize) < 0.45)) {
               racePosX -= speed;
            }
            break;
         case 270:
            // up
            if ((getColor(racePosX + halfSize, racePosY) < 0.45)) {
               racePosY -= speed;
            }
            break;
      }
      // right d
      if (racePosX > 950)
         racePosX = 0;
      // down s
      if (racePosY + halfSize > 575)
         racePosY = 0;
      // left a
      if (racePosX < 0)
         racePosX = 949;
      // up w
      if (racePosY < 0)
         racePosY = 549;

      // double something = getColor(racePosX + halfSize, racePosY + halfSize);
      // double location = getLocation();

      // //System.out.println("get color: "+ something + "\n\nget ghost color" +
      // location);

      // if (something == location) {
      // System.exit(0);
      // }

      // for(Ghost ghost: ghosts){
      // int ghostX = ghost.getCoordinateX();
      // int ghostY = ghost.getCoordinateY();

      // int xCenterPosPacman =racePosX + halfSize;
      // int yCenterPosPacman =racePosY + halfSize;

      // switch (ghost.getROT()) {
      // case 0:
      // // right d
      // if (ghostX + (halfSize * 2) > racePosX-25 && (ghostY + halfSize) < racePosY
      // && ghostY+halfSize > racePosY-50) {
      // System.out.println("right right");

      // }
      // break;
      // case 90:
      // // down
      // if (ghostY + (halfSize * 2) < racePosY && ghostX + halfSize > racePosX &&
      // ghostX+halfSize < racePosX+50) {
      // System.out.println("down down");
      // }
      // break;
      // case 180:
      // // left
      // if (ghostY - halfSize < racePosX + 50 && ghostX > racePosY && ghostX <
      // racePosY-50) {
      // System.out.println("left left");
      // }
      // break;
      // case 270:
      // // up
      // if (ghostX + halfSize > racePosY -50 && ghostY < racePosX && ghostY <
      // racePosX +50 ) {
      // System.out.println("up up");
      // }
      // break;
      // }

      // }
      for (Ghost ghost : ghosts) {

         int ghostX = ghost.getCoordinateX();
         int ghostY = ghost.getCoordinateY();

         ghostX += halfSize;
         ghostY += halfSize;
         ghost.getColor(ghostX, ghostY);

         int xCenterPosPacman = racePosX + halfSize;
         int yCenterPosPacman = racePosY + halfSize;
         //System.out.println(getLocation());
         
         if (xCenterPosPacman == ghostX
               || yCenterPosPacman == ghostY
               || yCenterPosPacman == ghostY + 10
               || xCenterPosPacman == ghostX + 10
               || yCenterPosPacman == ghostY - 10
               || xCenterPosPacman == ghostX - 10
               || xCenterPosPacman == ghostX - 20
               || xCenterPosPacman == ghostX + 20
               || yCenterPosPacman == ghostY - 20
               || yCenterPosPacman == ghostY + 20) {
            System.out.println("that's it");
         } else {
            break;
         }
      }

      aPicView.setTranslateX(racePosX);
      aPicView.setTranslateY(racePosY);
      aPicView.setRotate(raceROT);

   } // end update()

   public void goUp() {
      raceROT = 270;
   }

   public void goDown() {
      raceROT = 90;
   }

   public void goLeft() {
      aPicView.setScaleY(-1);
      raceROT = 180;
   }

   public void goRight() {
      aPicView.setScaleY(1);
      raceROT = 0;
   }

   public double getColor(int x, int y) {
      FileInputStream stream = null;
      PixelReader reader = null;
      double colorCode = 1.0;

      try {

         stream = new FileInputStream(new File("map.png"));
         Image mapImage = new Image(stream);
         reader = mapImage.getPixelReader();
         colorCode = reader.getColor(x, y).getBlue();

      } catch (Exception e) {
         e.printStackTrace();
      }
      return colorCode;

   }

   public double getLocation() {
   FileInputStream stream = null;
   PixelReader reader = null;
   double colorCode = 0;
   try {
   stream = new FileInputStream(new File("Ghost 1.png"));
   Image ghostImage = new Image(stream);
   reader = ghostImage.getPixelReader();
   colorCode = reader.getColor(25, 25).getBlue();
   } catch (FileNotFoundException e) {

   e.printStackTrace();
   }
   System.out.println("colorcode: " + colorCode);
   return colorCode ;
   }

} // end inner class Racer

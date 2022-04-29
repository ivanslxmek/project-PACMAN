
/*
ISTE 121 Project Pacman
Bruno Leka & Ivan Trstenjak */

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
public class Ghost extends Pane {
    private int racePosX = 475; // x position of the racer
    private int racePosY = 275; // y position of the racer
    private int raceROT = 270; // x position of the racer
    private ImageView aPicView; // a view of the icon ... used to display and move the image
    private Image PacManOpen = null;
    private int iconWidth; // width (in pixels) of the icon
    private int iconHeight; // height (in pixels) or the icon
    private int speed = 5;
    private final static String GHOST_BLUE = "Ghost 1.png";

    public Ghost() {
        // Draw the icon for the racer
        try {
            PacManOpen = new Image(new FileInputStream(GHOST_BLUE));
            // background = new Background((, new FileInputStream("pacman-map1.png")));
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            System.exit(1);
        }
        iconWidth = (int) PacManOpen.getWidth();
        iconHeight = (int) PacManOpen.getHeight();

        aPicView = new ImageView(PacManOpen);
        aPicView.setTranslateX(475);
        aPicView.setTranslateY(275);
        this.getChildren().add(aPicView);
    }

    /**
     * update() method keeps the thread (racer) alive and moving.
     */
    public void update() {

        int halfSize = (int) PacManOpen.getWidth() / 2;

        switch (raceROT) {
            case 0:
                // right
                if ((getColor(racePosX + halfSize * 2, racePosY + halfSize) < 0.45)) {
                    racePosX += speed;
                } else {
                    raceROT = (int) (Math.random() * 4) * 90;

                }
                break;
            case 90:
                // down
                if ((getColor(racePosX + halfSize, racePosY + halfSize * 2) < 0.45)) {
                    racePosY += speed;
                } else {
                    raceROT = (int) (Math.random() * 4) * 90;
                }
                break;
            case 180:
                // left
                if ((getColor(racePosX, racePosY + halfSize) < 0.45)) {
                    racePosX -= speed;
                } else {
                    raceROT = (int) (Math.random() * 4) * 90;
                }
                break;
            case 270:
                // up
                if ((getColor(racePosX + halfSize, racePosY) < 0.45)) {
                    racePosY -= speed;
                } else {
                    raceROT = (int) (Math.random() * 4) * 90;
                }
                break;
        }
        // moving to oposite sides of the screen
        if (racePosX + halfSize > 975)
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

    public int getCoordinateX(){
        return racePosX;
    }

    public int getCoordinateY(){
        return racePosY;
    }

    public int getROT(){
        return raceROT;
    }

} // end Ghost class

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

import org.w3c.dom.css.Counter;

public class Points extends Pane {
    private int iconWidth; // width (in pixels) of the icon
    private int iconHeight; // height (in pixels) or the icon
    private ImageView aPicView; // a view of the icon ... used to display and move the image
    private final static String POINT = "Dots.png";
    private Image pointImage = null;
    private int halfSize = 25;

    public Points() {
        createPoints();

    }

    public void createPoints() {
        int x = (int) (Math.random() *1000)  ;
        int y = (int) (Math.random() *600)  ;
        
        

        if ( (getColor(x, y) < 0.5 || getColor(x+50, y) < 0.5 || getColor(x+50, y+50) < 0.5 || getColor(x, y+50) < 0.5) && (getColor(x, y) > 0.1 || getColor(x+50, y) > 0.1 || getColor(x+50, y+50) > 0.1  || getColor(x, y+50) > 0.1)) {
            try {

                pointImage = new Image(new FileInputStream(POINT));

            } catch (FileNotFoundException e) {
                e.printStackTrace();

            }
            iconWidth = (int) pointImage.getWidth();
            halfSize = (int) iconWidth / 2;
            aPicView = new ImageView(pointImage);
            aPicView.setTranslateX(x);
            aPicView.setTranslateY(y);
            this.getChildren().add(aPicView);
            
            System.out.println( "x: "+ x+ "  y: " + y );
            System.out.println("color " + getColor(x, y)+ "\n" );
        }else{
            createPoints();
        }
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

}
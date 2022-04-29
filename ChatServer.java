/*
ISTE 121 Project Pacman
Group 3
Bruno Leka & Ivan Trstenjak */

import java.io.*;
import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;
import java.net.*;
import java.util.*;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import javafx.animation.Animation.Status;

public class ChatServer extends Application implements EventHandler<ActionEvent>{
    // window attributes

    private Stage stage;
    private Scene scene;
    private VBox rootsrvr = null;

    //Gui components
    private TextArea taList = new TextArea();
    private Button btnClear = new Button("Clear");

    // socket
    private static final int SERVER_PORT = 32001;
    List<ObjectOutputStream> nameOfWriters = new ArrayList<>();

    int clientIDCounter = 0;

    public static void main(String[] args){
        launch(args);
    }

    // start the server 
    public void start(Stage _stage){
        stage = _stage;
        stage.setTitle("Server");
        final int WIDTH = 450;
        final int HEIGHT = 400;
        final int X = 550;
        final int Y = 100;

        stage.setX(X);
        stage.setY(Y);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent evt){
                System.exit(0);
            }
            

        });
        //setup for root
        rootsrvr = new VBox();
        taList.setEditable(false);
        btnClear.setOnAction(this);

        //put clear button in north 
        HBox hbNorth = new HBox();
        hbNorth.setAlignment(Pos.CENTER);
        hbNorth.getChildren().addAll(btnClear);

        //setup rootis 
        rootsrvr.getChildren().addAll(hbNorth,taList);
        for(Node n : rootsrvr.getChildren()){
            VBox.setMargin(n, new Insets(10));
        }

        // set the scene and show the stage
        scene = new Scene( rootsrvr,WIDTH,HEIGHT);
        stage.setScene(scene);
        stage.show();

        //adjust size of textArea
        taList.setPrefHeight(HEIGHT - hbNorth.getPrefHeight());

        //call server stuff
        doServerStuff();
    }

    private void doServerStuff(){
        ServerThread st = new ServerThread();
        st.start();
    }


    // SErverThread

    class ServerThread extends Thread{
        @Override
        public void run(){
            try{
                System.out.println("Openning SOCKET PORT");
                ServerSocket sSocket = new ServerSocket(SERVER_PORT);
                while(true){

                    System.out.println("Waitig for client to connect...");
                    Socket cSocket = sSocket.accept();
                    ClientThread cT = new ClientThread(cSocket);
                    cT.start();
                }
            }catch(IOException e){
                showAlert(AlertType.ERROR, e.getMessage());
            }
        }
    }

    //Client thread
    class ClientThread extends Thread{
        private Socket cSocket;
        private ObjectOutputStream oos = null;
        private ObjectInputStream ois = null;

        public ClientThread(Socket cSocket){
            this.cSocket = cSocket;
        }

        @Override
        public void run(){
            try {
                this.ois = new ObjectInputStream(this.cSocket.getInputStream());
                this.oos = new ObjectOutputStream(this.cSocket.getOutputStream());

                nameOfWriters.add(this.oos);

                while(true){
                    Object obj = ois.readObject();
                    if (obj instanceof String){
                        String message = (String) obj;
                        taList.appendText("Recieved " + message + "\n");
                        String[] arrayOfMessage = message.split("-");
                        System.out.println(arrayOfMessage);
                        if(arrayOfMessage.length == 2){
                            switch (arrayOfMessage[0]){
                                case "REGISTER": 
                                taList.appendText(message + "\n");
                                oos.writeObject(clientIDCounter);
                                oos.flush();
                                clientIDCounter++;
                                break;

                            case "CHAT":
                            taList.appendText(message + "\n");

                            for (int i = 0; i < nameOfWriters.size(); i++){
                                nameOfWriters.get(i).writeObject(arrayOfMessage[1]);
                                nameOfWriters.get(i).flush();
                            }
                            break;
                        }
                    }
                }else if (obj instanceof Status){
                    Status newStatus = (Status) obj;
                    System.out.println(newStatus.toString());
                

                            for (int i = 0; i < nameOfWriters.size(); i++){
                                if(nameOfWriters.get(i) != this.oos){
                                nameOfWriters.get(i).writeObject(newStatus);
                                nameOfWriters.get(i).flush();
                            }
                        }
                    }
                 }
                

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }catch (EOFException eof){
                showAlert(AlertType.WARNING, "Connection lost");
            }catch(IOException ioe){
                showAlert(AlertType.ERROR, ioe.getMessage());
            }
            taList.appendText( "Client disconnected");
        }
    }

    //button handler
    public void handle(ActionEvent ae) {
        Button btn = (Button) ae.getSource();
        switch(btn.getText()){
            case "Clear":
            taList.clear();
            break;
        }

    }

    public void showAlert(AlertType type, String message){
        Platform.runLater(new Runnable(){

            @Override
            public void run() {
                Alert alert = new Alert(type,message);
                alert.showAndWait();
                
            }

        });
    }
}

import java.io.*;
import java.net.Socket;
import javafx.geometry.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.layout.FlowPane;
import javafx.animation.Animation.Status;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.net.*;

public class Menu extends Application implements EventHandler<ActionEvent> {

	Scene menuScene, settingsScene, playScene, singleScene, multiScene, connectScene, chatScene;
	private Stage stage;
	// client chat
	private Label lblServerIP = new Label(" IP Address: ");
	private TextField tfServerIP = new TextField("127.0.0.1");
	private Label lblName = new Label(" Name: ");
	private TextField srvName = new TextField();
	private Button btnConnect = new Button("Connect");
	private Label lblSentence = new Label(" Message: ");
	private TextField tfMessageToSend = new TextField();
	private Label lblLog = new Label("Chat Log:");
	private TextArea taForMessages = new TextArea();
	Pane root = new Pane();
	// socket
	private Socket socket = null;
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
	private static final int SERVER_PORT = 32001;

	private int currentID = -1;

	private Game game = null;

	@Override
	public void start(Stage primaryStage) throws Exception {

		// base settings
		primaryStage.setTitle("MENU");
		primaryStage.setResizable(false);
		stage = primaryStage;

		System.out.println(root);
		root.setPrefSize(1000, 600);

		// creating main menu buttons
		Button btnPlay = new Button("PLAY");
		Button btnSettings = new Button("SETTINGS");
		Button btnHtp = new Button("HOW TO PLAY");
		Button btnExit = new Button("EXIT");

		// resizing menu buttons
		btnPlay.setPrefWidth(170);
		btnPlay.setPrefHeight(45);

		// resizing menu buttons
		btnSettings.setPrefWidth(170);
		btnSettings.setPrefHeight(45);

		// resizing menu buttons
		btnHtp.setPrefWidth(170);
		btnHtp.setPrefHeight(45);

		// resizing menu buttons
		btnExit.setPrefWidth(170);
		btnExit.setPrefHeight(45);

		// styling the menu buttons
		btnPlay.setStyle(
				"-fx-font: bold 13pt Arial;-fx-background-color: #F08000;-fx-background-radius: 15px;-fx-text-fill: #FFFFFF;");
		btnSettings.setStyle(
				"-fx-font: bold 13pt Arial;-fx-background-color: #F08000;-fx-background-radius: 15px;-fx-text-fill: #FFFFFF;");
		btnHtp.setStyle(
				"-fx-font: bold 13pt Arial;-fx-background-color: #F08000;-fx-background-radius: 15px;-fx-text-fill: #FFFFFF;");
		btnExit.setStyle(
				"-fx-font: bold 13pt Arial;-fx-background-color: #F08000;-fx-background-radius: 15px;-fx-text-fill: #FFFFFF;");

		// section for background image on menu
		InputStream menuStream = new FileInputStream("menu.png");
		Image image = new Image(menuStream);
		ImageView imageView = new ImageView();
		imageView.setImage(image);
		imageView.setFitWidth(1000);
		imageView.setFitHeight(600);
		root.getChildren().add(imageView);

		// section for background image on settings
		InputStream streamSettings = new FileInputStream("settings.png");
		Image image1 = new Image(streamSettings);
		ImageView imageView1 = new ImageView();
		imageView1.setImage(image1);
		imageView1.setFitWidth(1000);
		imageView1.setFitHeight(600);
		Group grpSettings = new Group(imageView1);

		// section for background image on play
		InputStream streamPlay = new FileInputStream("play.png");
		Image image2 = new Image(streamPlay);
		ImageView imageView2 = new ImageView();
		imageView2.setImage(image2);
		imageView2.setFitWidth(1000);
		imageView2.setFitHeight(600);
		Group grpPlay = new Group(imageView2);

		// section for background image on singleplayer - not needed!
		InputStream streamSingle = new FileInputStream("single.png");
		Image image3 = new Image(streamSingle);
		ImageView imageView3 = new ImageView();
		imageView3.setImage(image3);
		imageView3.setFitWidth(1000);
		imageView3.setFitHeight(600);
		Group grpSingle = new Group(imageView3);

		// section for background image on multiplayer
		InputStream streamMulti = new FileInputStream("multi.png");
		Image image4 = new Image(streamMulti);
		ImageView imageView4 = new ImageView();
		imageView4.setImage(image4);
		imageView4.setFitWidth(1000);
		imageView4.setFitHeight(600);
		Group grpMulti = new Group(imageView4);

		// section for background image on info
		InputStream streamInfo = new FileInputStream("connect.png");
		Image image5 = new Image(streamInfo);
		ImageView imageView5 = new ImageView();
		imageView5.setImage(image5);
		imageView5.setFitWidth(1000);
		imageView5.setFitHeight(600);
		Group grpInfo = new Group(imageView5);

		// section for background image on info
		InputStream streamChat = new FileInputStream("chat.png");
		Image image6 = new Image(streamChat);
		ImageView imageView6 = new ImageView();
		imageView6.setImage(image6);
		imageView6.setFitWidth(1000);
		imageView6.setFitHeight(600);
		Group grpChat = new Group(imageView6);

		// items for settings page
	
		FlowPane fpTop = new FlowPane();
		FlowPane fpMid = new FlowPane();
		Label lblMusic = new Label(" Toggle Music ");
		Label lblVolume = new Label(" Adjust Volume");
		Slider sliderMusic = new Slider();

		// styling labels for the settings page
		lblMusic.setStyle(
				"-fx-font: bold 13pt Arial;-fx-background-color: #F08000;-fx-background-radius: 15px;-fx-text-fill: #FFFFFF;");
		lblMusic.setPrefHeight(30);
		lblMusic.setPrefWidth(130);

		// styling labels for the settings page
		lblVolume.setStyle(
				"-fx-font: bold 13pt Arial;-fx-background-color: #F08000;-fx-background-radius: 15px;-fx-text-fill: #FFFFFF;");
		lblVolume.setPrefHeight(30);
		lblVolume.setPrefWidth(140);

		// setting the text color of labels
		lblMusic.setTextFill(Color.WHITE);
		lblVolume.setTextFill(Color.WHITE);

		// connecting labels and buttons to the flow panes
		fpTop.getChildren().addAll(lblMusic);
		fpMid.getChildren().addAll(lblVolume, sliderMusic);

		// positioning the panes
		fpTop.setTranslateX(300);
		fpTop.setTranslateY(280);
		fpTop.setAlignment(Pos.CENTER);

		// positioning the panes
		fpMid.setTranslateX(300);
		fpMid.setTranslateY(340);
		fpMid.setAlignment(Pos.CENTER);

		// when clicking on settings button, sends to the settings page
		btnSettings.setOnAction(e -> primaryStage.setScene(settingsScene));
		FlowPane layout1 = new FlowPane();
		layout1.getChildren().addAll(btnSettings);

		// when clicking on play button, sends to the play page
		btnPlay.setOnAction(e -> primaryStage.setScene(playScene));
		FlowPane layout3 = new FlowPane(8, 8);
		layout3.getChildren().addAll(btnPlay);

		// code of the actual settings page
		Button settingsReturn = new Button("← Back");
		settingsReturn.setStyle(
				"-fx-font: bold 13pt Arial;-fx-background-color: #F08000;-fx-background-radius: 15px;-fx-text-fill: #FFFFFF;");
		settingsReturn.setOnAction(e -> primaryStage.setScene(menuScene));
		FlowPane layout2 = new FlowPane(8, 8);
		layout2.getChildren().addAll(settingsReturn);
		grpSettings.getChildren().addAll(layout2, fpTop, fpMid);
		settingsScene = new Scene(grpSettings, 1000, 600);

		// singleplayer button is pressed and sends you to the actual game scene - edit!
		Button btnSingle = new Button("Singleplayer");
		btnSingle.setStyle(
				"-fx-font: bold 13pt Arial;-fx-background-color: #F08000;-fx-background-radius: 15px;-fx-text-fill: #FFFFFF;");
		btnSingle.setPrefWidth(140);
		btnSingle.setPrefHeight(40);
		btnSingle.setOnAction(e -> doStartGame());
		FlowPane layout5 = new FlowPane(8, 8);
		layout5.setTranslateX(300);
		layout5.setTranslateY(290);
		layout5.setAlignment(Pos.CENTER);
		layout5.getChildren().addAll(btnSingle);

		// multiplayer button is pressed and sends you to the connect section
		Button btnMulti = new Button("Multiplayer");
		btnMulti.setStyle(
				"-fx-font: bold 13pt Arial;-fx-background-color: #F08000;-fx-background-radius: 15px;-fx-text-fill: #FFFFFF;");
		btnMulti.setPrefWidth(140);
		btnMulti.setPrefHeight(40);
		btnMulti.setOnAction(e -> primaryStage.setScene(multiScene));
		FlowPane layout7 = new FlowPane(8, 8);
		layout7.setTranslateX(300);
		layout7.setTranslateY(290);
		layout7.setAlignment(Pos.CENTER);
		layout7.getChildren().addAll(btnMulti);

		// this is the part of the actual play page
		Button playReturn = new Button("← Back");
		playReturn.setStyle(
				"-fx-font: bold 13pt Arial;-fx-background-color: #F08000;-fx-background-radius: 15px;-fx-text-fill: #FFFFFF;");
		playReturn.setOnAction(e -> primaryStage.setScene(menuScene));
		FlowPane layout4 = new FlowPane(8, 8);
		layout4.getChildren().addAll(playReturn);
		VBox items = new VBox(8);
		items.setTranslateX(430);
		items.setTranslateY(300);
		items.setAlignment(Pos.CENTER);
		items.getChildren().addAll(btnSingle, btnMulti);
		grpPlay.getChildren().addAll(layout4, items);
		playScene = new Scene(grpPlay, 1000, 600);


		// the page where connecting online
		Button back = new Button("← Back");
		back.setStyle(
				"-fx-font: bold 13pt Arial;-fx-background-color: #F08000;-fx-background-radius: 15px;-fx-text-fill: #FFFFFF;");
		back.setOnAction(e -> primaryStage.setScene(playScene));
		Button connect = new Button("Online");
		Button chat = new Button("Chat");
		Button score = new Button("Score");
		connect.setStyle(
				"-fx-font: bold 13pt Arial;-fx-background-color: #F08000;-fx-background-radius: 15px;-fx-text-fill: #FFFFFF;");
		chat.setStyle(
				"-fx-font: bold 13pt Arial;-fx-background-color: #F08000;-fx-background-radius: 15px;-fx-text-fill: #FFFFFF;");
		score.setStyle(
				"-fx-font: bold 13pt Arial;-fx-background-color: #F08000;-fx-background-radius: 15px;-fx-text-fill: #FFFFFF;");
		connect.setPrefWidth(140);
		connect.setPrefHeight(40);
		chat.setPrefWidth(140);
		chat.setPrefHeight(40);
		score.setPrefWidth(140);
		score.setPrefHeight(40);
		MenuBox multbtn = new MenuBox(connect, chat, score);
		multbtn.setTranslateX(410);
		multbtn.setTranslateY(300);
		multbtn.setAlignment(Pos.CENTER);
		connect.setOnAction(e -> primaryStage.setScene(connectScene));
		grpMulti.getChildren().addAll(multbtn, back);
		multiScene = new Scene(grpMulti, 1000, 600);
		// the page where connecting online ending here

		VBox chatBox = new VBox(8);
		chatBox.setTranslateX(60);
		chatBox.setTranslateY(0);
		chatBox.setAlignment(Pos.CENTER);

		FlowPane fpRow1 = new FlowPane(8, 8);
		fpRow1.setAlignment(Pos.TOP_LEFT);
		fpRow1.setTranslateX(1000);
		fpRow1.setTranslateY(0);
		tfServerIP.setPrefColumnCount(18);
		srvName.setPrefColumnCount(14);
		fpRow1.getChildren().addAll(lblServerIP, tfServerIP);
		chatBox.getChildren().add(fpRow1);

		FlowPane fpRow2 = new FlowPane(8, 8);
		fpRow2.setTranslateX(1000);
		fpRow2.setTranslateY(0);
		fpRow2.setAlignment(Pos.TOP_LEFT);
		tfMessageToSend.setPrefWidth(300);
		fpRow2.getChildren().addAll(lblName, srvName, btnConnect, lblSentence, tfMessageToSend);

		// tfMessageToSend and btnSend disabled until connected
		tfMessageToSend.setDisable(true);
		chatBox.getChildren().add(fpRow2);

		FlowPane fpLog = new FlowPane();
		fpLog.setTranslateX(1000);
		fpLog.setTranslateY(0);
		fpLog.setAlignment(Pos.CENTER);
		taForMessages.setPrefColumnCount(25);
		taForMessages.setPrefRowCount(24);
		taForMessages.setEditable(false);
		fpLog.getChildren().addAll(lblLog, taForMessages);
		chatBox.getChildren().add(fpLog);
		for (Node n : chatBox.getChildren()) {
			VBox.setMargin(n, new Insets(5));
		}

		// Listen for the buttons
		btnConnect.setOnAction(this);

		// ENTER PRESSED // import javafx.scene.input.KeyEvent;
		tfMessageToSend.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent t) {
				if (t.getCode() == KeyCode.ENTER) {
					sendMessage();
				}
			}
		});

		// the page where connecting to the chat
		Button backChat = new Button("← Back");
		backChat.setStyle(
				"-fx-font: bold 13pt Arial;-fx-background-color: #F08000;-fx-background-radius: 15px;-fx-text-fill: #FFFFFF;");
		backChat.setOnAction(e -> primaryStage.setScene(multiScene));
		chat.setOnAction(e -> primaryStage.setScene(chatScene));
		grpChat.getChildren().addAll(backChat, chatBox);
		chatScene = new Scene(grpChat, 1500, 600);
		// the page where connecting to the chat ending here

		// page where user connects to the server
		Button backinfo = new Button("← Back");
		backinfo.setStyle(
				"-fx-font: bold 13pt Arial;-fx-background-color: #F08000;-fx-background-radius: 15px;-fx-text-fill: #FFFFFF;");
		backinfo.setOnAction(e -> primaryStage.setScene(multiScene));
		Label lblName = new Label("  Username:");
		TextField tfName = new TextField();
		Label lblIP = new Label("  IP Address:");
		TextField tfIP = new TextField();
		lblName.setStyle(
				"-fx-font: bold 13pt Arial;-fx-background-color: #F08000;-fx-background-radius: 15px;-fx-text-fill: #FFFFFF;");
		lblName.setPrefHeight(30);
		lblName.setPrefWidth(120);
		lblIP.setStyle(
				"-fx-font: bold 13pt Arial;-fx-background-color: #F08000;-fx-background-radius: 15px;-fx-text-fill: #FFFFFF;");
		lblIP.setPrefHeight(30);
		lblIP.setPrefWidth(120);
		Button btnGo = new Button("Play");
		btnGo.setStyle(
				"-fx-font: bold 13pt Arial;-fx-background-color: #F08000;-fx-background-radius: 15px;-fx-text-fill: #FFFFFF;");
		FlowPane fpOne = new FlowPane(8, 8);
		FlowPane fpTwo = new FlowPane(8, 8);
		FlowPane fpThree = new FlowPane(8, 8);

		// positioning flow panes
		fpOne.setTranslateX(300);
		fpOne.setTranslateY(290);
		fpOne.setAlignment(Pos.CENTER);

		// positioning flow panes
		fpTwo.setTranslateX(260);
		fpTwo.setTranslateY(300);
		fpTwo.setAlignment(Pos.CENTER);

		// positioning flow panes
		fpThree.setTranslateX(260);
		fpThree.setTranslateY(300);
		fpThree.setAlignment(Pos.CENTER);

		// connecting items to panes
		fpOne.getChildren().addAll(btnGo);
		fpTwo.getChildren().addAll(lblName, tfName);
		fpThree.getChildren().addAll(lblIP, tfIP);

		// setting text color of labels
		lblName.setTextFill(Color.WHITE);
		lblIP.setTextFill(Color.WHITE);

		// connecting panes to the scene
		VBox layout9 = new VBox(8);
		layout9.setAlignment(Pos.CENTER);
		layout9.getChildren().addAll(fpOne, fpTwo, fpThree);
		grpInfo.getChildren().addAll(layout9, backinfo);
		connectScene = new Scene(grpInfo, 1000, 600);

		// creating the base scene
		menuScene = new Scene(root, 1000, 600);

		// main menu buttons
		MenuBox vbox = new MenuBox(btnPlay, btnSettings, btnHtp, btnExit);
		root.getChildren().addAll(vbox);

		// positioning the buttons placements
		vbox.setTranslateX(412);
		vbox.setTranslateY(300);
		vbox.setAlignment(Pos.CENTER);

		// connecting buttons
		btnExit.setOnAction(this);
		btnHtp.setOnAction(this);

		// setting the scene
		primaryStage.setScene(menuScene);
		primaryStage.show();

	} // start

	// MenuBox class
	private static class MenuBox extends VBox {

		// constructor for the main menu scene
		public MenuBox(Button... items) {
			getChildren().add(createSeperator());

			for (Button item : items) {
				getChildren().addAll(item, createSeperator());
			}
		}

		// adding lines between the buttons in main menu
		private Line createSeperator() {
			Line sep = new Line();
			sep.setEndX(175);
			sep.setStroke(Color.ORANGE);
			return sep;
		}

	} // MenuBox class end

	// main
	public static void main(String[] args) {

		launch(args);
		
	}

	// handle
	@Override
	public void handle(ActionEvent ae) {
		Button btn = (Button) ae.getSource();
		switch (btn.getText()) {
			case "PLAY":
			
			
				break;
			case "SETTINGS":
				break;
			case "HOW TO PLAY":
				doHowTo();
				break;
			case "Connect":
				doConnect();
				break;
			case "Disconnect":
				doDisconnect();
				break;
			case "EXIT":
				doExit();
				break;
				case "Singleplayer":
				doStartGame();
				break;
		}

	}

	private void doStartGame() {
		System.out.println("play");
		game = new Game();
				try {
					game.start(stage);
				} catch (IOException e) {
					e.printStackTrace();
				}
	}

	private void doDisconnect() {
		System.out.println("Disconnect the client");
		try {
			oos.close();
			ois.close();
			socket.close();
			btnConnect.setText("Connect");
			srvName.setDisable(false);
			tfServerIP.setDisable(false);
			taForMessages.clear();
			srvName.clear();
		} catch (IOException e) {
			alert(AlertType.ERROR, "Exception", "Error while Disconnecting");
		}
	}

	private void sendMessage() {
		try {
			oos.writeObject("CHAT- " + srvName.getText() + ": " + tfMessageToSend.getText());
			System.out.println("Sent: " + srvName.getText() + ": " + tfMessageToSend.getText());
			oos.flush();
			tfMessageToSend.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void doConnect() {

		try {
			this.socket = new Socket(tfServerIP.getText(), SERVER_PORT);

			this.oos = new ObjectOutputStream(this.socket.getOutputStream());
			this.ois = new ObjectInputStream(this.socket.getInputStream());

			this.oos.writeObject("REGISTER-" + srvName.getText());
			this.currentID = (Integer) this.ois.readObject();

			System.out.println("Client registered successfully at ID " + this.currentID);
			btnConnect.setText("Disconnect");
			srvName.setDisable(true);
			tfServerIP.setDisable(true);
			tfMessageToSend.setDisable(false);

			// run the message receiving thread
			ClientThread th = new ClientThread();
			th.start();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// method to show how to play the game
	private void doHowTo() {
		alert(AlertType.INFORMATION, " 1. Use WASD \n 2. Move around \n 3. Eat dots \n 4. Win", "How to Play");
	}

	// method to exit the program
	private void doExit() {
		alert(AlertType.INFORMATION, " Thank you for Playing! \n By: Ivan Trstenjak & Bruno Leka", "Exit");
		System.exit(0);
	}

	// method for creating alerts
	private void alert(AlertType type, String message, String title) {
		Alert alert = new Alert(type, message);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.showAndWait();
	}

	class ClientThread extends Thread {
		@Override
		public void run() {

			while (true) {
				try {
					Object obj = ois.readObject();
					if (obj instanceof String) {// this is chat feedback
						String message = (String) obj;
						System.out.println("Feedback: " + message);
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								taForMessages.appendText(message + "\n");
							}
						});
					} else if (obj instanceof Status) {
						Status newStatus = (Status) obj;
						System.out.println("NewStatusReceived " + newStatus.toString());
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (EOFException eof) {
					alert(AlertType.WARNING, "Connection lost", "No Connection");
				} catch (IOException ioe) {
					alert(AlertType.ERROR, ioe.getMessage(), "Error");
				}
			}
		}
	}

} // Menu class end

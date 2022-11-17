import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Laboratories extends Application{

	Button btAdd = new Button("Add New Entry");
	Button btExistList = new Button("Existing Laboratories List");
	Button btDelete = new Button("Delete old entry");
	Button btMenu = new Button("Back to Main Menu");

	Laboratories[] labo = new Laboratories[20];
	private String lab;
	private int cost;


	public Laboratories(String lab, int cost)
	{
		this.lab=lab;
		this.cost=cost;
	}

	public Laboratories() {
		// TODO Auto-generated constructor stub
	}

	public String getLab() {
		return lab;
	}

	public int getCost() {
		return cost;
	}

	public void setLab(String lab, int cost) {
		try (FileWriter f = new FileWriter("lab.txt", true);
				BufferedWriter b = new BufferedWriter(f);
				PrintWriter p = new PrintWriter(b);) {

			p.println("," + lab + "," + "RM" + cost);

		} catch (IOException i) {
			i.printStackTrace();
		}

	}

	public void replace(String old) {
		String oldFileName = "lab.txt";
		String tmpFileName = "tmp_lab.txt";

		BufferedReader br = null;
		BufferedWriter bw = null;
		try
		{
			int i = 0;
			br = new BufferedReader(new FileReader(oldFileName));
			bw = new BufferedWriter(new FileWriter(tmpFileName));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains(old))
				{
					line = line.replace(line, "");
					i = 1;
				}
				if (i == 0)
				{
					bw.write(line+"\n");
				}
				else
				{
					bw.write(line);
					i = 0;
				}

			}
		} catch (Exception e) {
			return;
		} finally {
			try {
				if(br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(bw != null)
					bw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Once everything is complete, delete old file..
		File oldFile = new File(oldFileName);
		oldFile.delete();

		// And rename tmp file's name to old file name
		File newFile = new File(tmpFileName);
		newFile.renameTo(oldFile);

	}

	public void readfile(){
		File myFile = new File("lab.txt");
		Scanner input;
		int count = 0;
		try {
			input = new Scanner(myFile);
			while(input.hasNext())
			{
				String labname = input.next();
				int labcost = input.nextInt();
				labo[count] = new Laboratories(labname,labcost);
				count++;

			}
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void start(Stage laboratoriesStage) {
		BorderPane pane = new BorderPane();

		// top: text field
		BorderPane paneForTextField = new BorderPane();
		paneForTextField.setPadding(new Insets(5));

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String stringDate= dtf.format(now);
		String menuword = "Welcome to HMS! The current date and time: " + stringDate + "\n\n\t\t\t\t   LABORATORY SECTION";
		Text menutext = new Text(menuword);
		paneForTextField.setCenter(menutext);
		pane.setTop(paneForTextField);

		// Hold buttons in an HBox
		HBox hBox = new HBox();
		hBox.setSpacing(10);
		hBox.setAlignment(Pos.CENTER);
		HBox hBox2 = new HBox();
		hBox2.setSpacing(10);
		hBox2.setAlignment(Pos.CENTER);


		hBox.getChildren().add(btAdd);
		hBox.getChildren().add(btExistList);
		hBox.getChildren().add(btDelete);
		hBox2.getChildren().add(btMenu);

		btAdd.setOnAction(e ->
		{
			laboratoriesStage.close();
			newLab();
		});
		btExistList.setOnAction(e ->
		{
			laboratoriesStage.close();
			labList();
		});
		btDelete.setOnAction(e ->
		{
			laboratoriesStage.close();
			deleteList();
		});
		btMenu.setOnAction(e ->
		{
			laboratoriesStage.close();
			Stage primaryStage = new Stage();
			new HospitalManagement().start(primaryStage);
		});

		pane.setCenter(hBox);
		pane.setBottom(hBox2);
		BorderPane.setAlignment(hBox, Pos.TOP_CENTER); 

		// Create a scene and place it in the stage
		Scene scene = new Scene(pane, 600, 400);
		laboratoriesStage.setTitle("HMS"); // Set the stage title
		laboratoriesStage.setScene(scene); // Place the scene in the stage
		laboratoriesStage.show(); // Display the stage
	}

	public void newLab()
	{
		Stage popupwindow=new Stage();
		GridPane addpane = new GridPane();
		addpane.setAlignment(Pos.CENTER);
		addpane.setPadding(new Insets(15));
		addpane.setHgap(5);
		addpane.setVgap(5);

		// Place nodes in the pane
		TextField name = new TextField();
		TextField cost = new TextField();
		addpane.add(new Label("Please enter the new information!"), 1, 0);
		addpane.add(new Label("Lab Name:"), 0, 1);
		addpane.add(name, 1, 1);
		addpane.add(new Label("Cost:"), 0, 2);
		addpane.add(cost, 1, 2);

		HBox layout= new HBox(10);
		Button btNew = new Button("Apply");
		Button btCancel = new Button("Cancel");
		layout.getChildren().addAll(btNew, btCancel);
		layout.setAlignment(Pos.CENTER);
		addpane.add(layout,  1, 3);

		btNew.setOnAction(e ->{
			try {
				String S = name.getText();
				String T = cost.getText();
				int C = Integer.parseInt(T);
				setLab(S, C);
				popupwindow.close();
				remindUser();
			}
			catch(NumberFormatException E)
			{
				popupwindow.close();
				warningUser();
			}
		});

		btCancel.setOnAction(e -> 
		{
			popupwindow.close();
			Stage laboratoriesStage = new Stage();
			new Laboratories().start(laboratoriesStage);
		});
		popupwindow.initModality(Modality.APPLICATION_MODAL);



		Scene scene1= new Scene(addpane, 600, 400);
		popupwindow.setTitle("HMS");
		popupwindow.setScene(scene1);
		popupwindow.showAndWait();

	}

	public void labList()
	{
		Stage popupwindow=new Stage();
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(15));

		HBox hbox = new HBox(90);
		hbox.setAlignment(Pos.TOP_CENTER);
		hbox.setPadding(new Insets(15));

		hbox.getChildren().addAll(new Label("Facilities"), new Label("Cost"));
		pane.setTop(hbox);

		HBox layout= new HBox(10);
		Button btMain = new Button("Main Menu");
		Button btBack = new Button("Back");
		layout.getChildren().addAll(btBack, btMain);
		layout.setAlignment(Pos.CENTER);
		pane.setBottom(layout);

		FlowPane spane = new FlowPane();
		spane.setAlignment(Pos.CENTER);
		spane.setHgap(5);
		spane.setVgap(5);
		spane.setPadding(new Insets(10,15,10,15));
		spane.setPrefWrapLength(300);
		TextArea txtArea1 = new TextArea();
		TextArea txtArea2 = new TextArea();
		TextArea tmp = new TextArea();
		txtArea1.setPrefHeight(270);
		txtArea1.setPrefWidth(100);
		txtArea2.setPrefHeight(270);
		txtArea2.setPrefWidth(100);
		VBox v1 = new VBox();
		VBox v2 = new VBox();
		txtArea1.setEditable(false);
		txtArea2.setEditable(false);
		v1.getChildren().add(txtArea1);
		v2.getChildren().add(txtArea2);

		File selectedFile = new File("Lab.txt");
		Scanner input;
		int i = 0;
		if (selectedFile != null)
		{
			try {
				input = new Scanner(selectedFile);
				input.useDelimiter(",");
				while (input.hasNext())
				{
					if (i == 0)
					{
						txtArea1.appendText(input.next()+"\n");
						i++;
					}
					else if (i == 1)
					{
						txtArea2.appendText(input.next());
						i--;
					}		

				}	
				// Close the file
				input.close();
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		spane.getChildren().addAll(v1,v2);
		//spane.setRight(txtArea2);
		pane.setCenter(spane);

		btMain.setOnAction(e ->
		{
			popupwindow.close();
			Stage primaryStage = new Stage();
			new HospitalManagement().start(primaryStage);
		});
		btBack.setOnAction(e -> 
		{	
			popupwindow.close();
			Stage laboratoriesStage = new Stage();
			new Laboratories().start(laboratoriesStage);

		});

		popupwindow.initModality(Modality.APPLICATION_MODAL);

		Scene scene= new Scene(pane, 600, 400);
		popupwindow.setTitle("HMS");
		popupwindow.setScene(scene);
		popupwindow.showAndWait();
	}

	public void deleteList()
	{

		Stage popupwindow=new Stage();
		GridPane addpane = new GridPane();
		addpane.setAlignment(Pos.CENTER);
		addpane.setPadding(new Insets(15));
		addpane.setHgap(5);
		addpane.setVgap(5);

		// Place nodes in the pane
		TextField name = new TextField();
		TextField cost = new TextField();
		addpane.add(new Label("Please enter the information to be deleted!"), 1, 0);
		addpane.add(new Label("Lab Name:"), 0, 1);
		addpane.add(name, 1, 1);

		HBox layout= new HBox(10);
		Button btNew = new Button("Apply");
		Button btCancel = new Button("Cancel");
		layout.getChildren().addAll(btNew, btCancel);
		layout.setAlignment(Pos.CENTER);
		addpane.add(layout,  1, 2);

		btNew.setOnAction(e ->{
			String S = name.getText();
			replace(S);
			popupwindow.close();
			remindUser();
		});

		btCancel.setOnAction(e -> 
		{
			popupwindow.close();
			Stage laboratoriesStage = new Stage();
			new Laboratories().start(laboratoriesStage);
		});
		popupwindow.initModality(Modality.APPLICATION_MODAL);



		Scene scene1= new Scene(addpane, 600, 400);
		popupwindow.setTitle("HMS");
		popupwindow.setScene(scene1);
		popupwindow.showAndWait();

	}

	public void warningUser()
	{
		Stage popupwindow=new Stage();
		StackPane pane = new StackPane();
		pane.setPadding(new Insets(15));

		HBox hbox = new HBox(250);
		hbox.setAlignment(Pos.TOP_CENTER);
		hbox.setPadding(new Insets(15));
		hbox.setStyle("-fx-font-size: 25px");

		hbox.getChildren().addAll(new Label("Error! Please enter valid input!"));
		Button btOkie = new Button("OK");
		pane.getChildren().add(hbox);
		pane.getChildren().add(btOkie);

		btOkie.setOnAction(e -> 
		{	
			popupwindow.close();
			Stage laboratoriesStage = new Stage();
			new Laboratories().start(laboratoriesStage);

		});

		popupwindow.initModality(Modality.APPLICATION_MODAL);

		Scene scene= new Scene(pane, 600, 400);
		popupwindow.setTitle("Warning!");
		popupwindow.setScene(scene);
		popupwindow.showAndWait();
	}

	public void remindUser()
	{
		Stage popupwindow=new Stage();
		StackPane pane = new StackPane();
		pane.setPadding(new Insets(15));

		HBox hbox = new HBox(250);
		hbox.setAlignment(Pos.TOP_CENTER);
		hbox.setPadding(new Insets(15));
		hbox.setStyle("-fx-font-size: 25px");

		hbox.getChildren().addAll(new Label("The information had been updated successfully!"));
		Button btOkie = new Button("OK");
		pane.getChildren().add(hbox);
		pane.getChildren().add(btOkie);

		btOkie.setOnAction(e -> 
		{	
			popupwindow.close();
			Stage laboratoriesStage = new Stage();
			new Laboratories().start(laboratoriesStage);

		});

		popupwindow.initModality(Modality.APPLICATION_MODAL);

		Scene scene= new Scene(pane, 600, 400);
		popupwindow.setTitle("Reminder!");
		popupwindow.setScene(scene);
		popupwindow.showAndWait();
	}

	public static void main(String[] args) {
		launch(args);

	}

}

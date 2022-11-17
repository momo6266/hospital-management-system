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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Facilities extends Application{

	Button btAdd = new Button("Add New Entry");
	Button btExistList = new Button("Existing Facility");
	Button btDelete = new Button("Delete old entry");
	Button btMenu = new Button("Back to Main Menu");

	private String facility;
	Facilities[] fc = new Facilities[20];

	public Facilities(String facility) {
		super();
		this.facility = facility;
	}

	public Facilities() {
		// TODO Auto-generated constructor stub
	}

	public String getFacility() {
		return facility;
	}

	public void setFacility(String facility) {
		this.facility = facility;
	}

	public void setf(String facility) {
		try (FileWriter f = new FileWriter("facility.txt", true);
				BufferedWriter b = new BufferedWriter(f);
				PrintWriter p = new PrintWriter(b);) {

			p.println(facility);

		} catch (IOException i) {
			i.printStackTrace();
		}

	}

	public void replace(String old) {
		String oldFileName = "facility.txt";
		String tmpFileName = "tmp_facility.txt";

		BufferedReader br = null;
		BufferedWriter bw = null;
		try {

			int i = 0;
			br = new BufferedReader(new FileReader(oldFileName));
			bw = new BufferedWriter(new FileWriter(tmpFileName));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains(old))
				{
					line = line.replace(old, "");
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

		File myFile = new File("facility.txt");
		Scanner input;
		int count = 0;
		try {
			input = new Scanner(myFile);
			while(input.hasNext())
			{
				String desc = input.next();
				fc[count] = new Facilities(desc);
				count++;

			}
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void start(Stage facilitiesStage) {


		BorderPane pane = new BorderPane();

		// top: text field
		BorderPane paneForTextField = new BorderPane();
		paneForTextField.setPadding(new Insets(5));

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String stringDate= dtf.format(now);
		String menuword = "Welcome to HMS! The current date and time: " + stringDate + "\n\n\t\t\t\tHOSPITAL FACILITY SECTION";
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
			facilitiesStage.close();
			newFacility();
		});
		btExistList.setOnAction(e ->
		{
			facilitiesStage.close();
			showFacility();
		});
		btDelete.setOnAction(e ->
		{
			facilitiesStage.close();
			deleteFacility();
		});
		btMenu.setOnAction(e ->
		{
			facilitiesStage.close();
			Stage primaryStage = new Stage();
			new HospitalManagement().start(primaryStage);
		});

		pane.setCenter(hBox);
		pane.setBottom(hBox2);
		BorderPane.setAlignment(hBox, Pos.TOP_CENTER); 

		// Create a scene and place it in the stage
		Scene scene = new Scene(pane, 600, 400);
		facilitiesStage.setTitle("HMS"); // Set the stage title
		facilitiesStage.setScene(scene); // Place the scene in the stage
		facilitiesStage.show(); // Display the stage
	}

	public void newFacility()
	{
		Stage popupwindow=new Stage();
		GridPane addpane = new GridPane();
		addpane.setAlignment(Pos.CENTER);
		addpane.setPadding(new Insets(15));
		addpane.setHgap(5);
		addpane.setVgap(5);

		// Place nodes in the pane
		TextField name = new TextField();
		addpane.add(new Label("Please enter the new information!"), 1, 0);
		addpane.add(new Label("Facility Name:"), 0, 1);
		addpane.add(name, 1, 1);

		HBox layout= new HBox(10);
		Button btNew = new Button("Apply");
		Button btCancel = new Button("Cancel");
		layout.getChildren().addAll(btNew, btCancel);
		layout.setAlignment(Pos.CENTER);
		addpane.add(layout,  1, 2);

		btNew.setOnAction(e ->{

			String S = name.getText();
			setf(S);
			popupwindow.close();
			remindUser();

		});

		btCancel.setOnAction(e -> 
		{
			popupwindow.close();
			Stage facilitiesStage = new Stage();
			new Facilities().start(facilitiesStage);
		});
		popupwindow.initModality(Modality.APPLICATION_MODAL);



		Scene scene1= new Scene(addpane, 600, 400);
		popupwindow.setTitle("HMS");
		popupwindow.setScene(scene1);
		popupwindow.showAndWait();

	}

	public void showFacility()
	{
		Stage popupwindow=new Stage();
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(15));

		HBox hbox = new HBox(250);
		hbox.setAlignment(Pos.TOP_CENTER);
		hbox.setPadding(new Insets(15));

		hbox.getChildren().add(new Label("Facilities"));
		pane.setTop(hbox);

		HBox layout= new HBox(10);
		Button btMain = new Button("Main Menu");
		Button btBack = new Button("Back");
		layout.getChildren().addAll(btBack, btMain);
		layout.setAlignment(Pos.CENTER);
		pane.setBottom(layout);

		GridPane spane = new GridPane();
		TextArea txtArea = new TextArea();
		VBox v1 = new VBox();
		txtArea.setEditable(false);
		v1.getChildren().add(txtArea);

		File selectedFile = new File("Facility.txt");
		Scanner input;
		if (selectedFile != null)
		{
			try {
				input = new Scanner(selectedFile);
				input.useDelimiter("\\r\\n|[\\n\\x0B\\x0C\\r\\u0085\\u2028\\u2029]"); 
				while (input.hasNext())
				{
					txtArea.appendText(input.next()+"\n");

				}	
				// Close the file
				input.close();
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		spane.add(v1, 1, 2);

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
			Stage facilitiesStage = new Stage();
			new Facilities().start(facilitiesStage);

		});

		popupwindow.initModality(Modality.APPLICATION_MODAL);

		Scene scene= new Scene(pane, 600, 400);
		popupwindow.setTitle("HMS");
		popupwindow.setScene(scene);
		popupwindow.showAndWait();
	}

	public void deleteFacility()
	{

		Stage popupwindow=new Stage();
		GridPane addpane = new GridPane();
		addpane.setAlignment(Pos.CENTER);
		addpane.setPadding(new Insets(15));
		addpane.setHgap(5);
		addpane.setVgap(5);

		// Place nodes in the pane
		TextField name = new TextField();
		addpane.add(new Label("Please enter the information to be deleted!"), 1, 0);
		addpane.add(new Label("Facility Name:"), 0, 1);
		addpane.add(name, 1, 1);

		HBox layout= new HBox(10);
		Button btNew = new Button("Apply");
		Button btCancel = new Button("Cancel");
		layout.getChildren().addAll(btNew, btCancel);
		layout.setAlignment(Pos.CENTER);
		addpane.add(layout,  1, 2);

		btNew.setOnAction(e ->{

			String S = name.getText();
			System.out.print(S);
			replace(S);
			popupwindow.close();
			remindUser();

		});

		btCancel.setOnAction(e -> 
		{
			popupwindow.close();
			Stage facilitiesStage = new Stage();
			new Facilities().start(facilitiesStage);
		});

		popupwindow.initModality(Modality.APPLICATION_MODAL);

		Scene scene1= new Scene(addpane, 600, 400);
		popupwindow.setTitle("HMS");
		popupwindow.setScene(scene1);
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
			Stage facilitiesStage = new Stage();
			new Facilities().start(facilitiesStage);

		});

		popupwindow.initModality(Modality.APPLICATION_MODAL);

		Scene scene= new Scene(pane, 600, 400);
		popupwindow.setTitle("Warning!");
		popupwindow.setScene(scene);
		popupwindow.showAndWait();
	}

	public static void main(String[] args) 
	{
		launch(args);

	}

}


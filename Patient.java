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

public class Patient extends Application{

	Button btAdd = new Button("Add New Entry");
	Button btExistList = new Button("Existing Patient");
	Button btDelete = new Button("Delete old entry");
	Button btMenu = new Button("Back to Main Menu");

	private String id,name,disease,sex,admitStatus;
	private int age;
	final static String DATE_FORMAT = "dd-MM-yy";
	Patient[] pat = new Patient[100];


	public Patient(String id, String name, String disease, String sex, String admitStatus, int age)
	{
		this.id=id;
		this.name=name;
		this.disease = disease;
		this.sex=sex;
		this.admitStatus=admitStatus;
		this.age=age; 	 
	}
	public Patient() {
		// TODO Auto-generated constructor stub
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisease() {
		return disease;
	}

	public void setDisease(String disease) {
		this.disease = disease;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAdmitStatus() {
		return admitStatus;
	}

	public void setAdmitStatus(String admitStatus) {
		this.admitStatus = admitStatus;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setf(String patient) {
		try (FileWriter f = new FileWriter("patient.txt", true);
				BufferedWriter b = new BufferedWriter(f);
				PrintWriter p = new PrintWriter(b);) {

			p.println(patient);

		} catch (IOException i) {
			i.printStackTrace();
		}

	}

	public void replace(String old) {
		String oldFileName = "patient.txt";
		String tmpFileName = "tmp_patient.txt";

		BufferedReader br = null;
		BufferedWriter bw = null;
		try {

			int i = 0;
			br = new BufferedReader(new FileReader(oldFileName));
			bw = new BufferedWriter(new FileWriter(tmpFileName));
			String line;
			System.out.print(old);
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

		File myFile = new File("patient.txt");
		Scanner input;
		int count = 0;
		try {
			input = new Scanner(myFile);
			while(input.hasNext())
			{
				String id = input.next();
				String name = input.next();
				String disease = input.next();
				String sex = input.next();
				String admitStatus = input.next();
				int age = input.nextInt();
				pat[count] = new Patient(id,name,disease,sex,admitStatus,age);
				count++;

			}
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void start(Stage patientStage) {


		BorderPane pane = new BorderPane();

		// top: text field
		BorderPane paneForTextField = new BorderPane();
		paneForTextField.setPadding(new Insets(5));

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String stringDate= dtf.format(now);
		String menuword = "Welcome to HMS! The current date and time: " + stringDate + "\n\n\t\t\t\t\tMEDICINE SECTION";
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
			patientStage.close();
			newPatient();
		});
		btExistList.setOnAction(e ->
		{
			patientStage.close();
			showPatientInfo();
		});
		btDelete.setOnAction(e ->
		{
			patientStage.close();
			deletePatient();
		});
		btMenu.setOnAction(e ->
		{
			patientStage.close();
			Stage primaryStage = new Stage();
			new HospitalManagement().start(primaryStage);
		});

		pane.setCenter(hBox);
		pane.setBottom(hBox2);
		BorderPane.setAlignment(hBox, Pos.TOP_CENTER); 

		// Create a scene and place it in the stage
		Scene scene = new Scene(pane, 600, 400);
		patientStage.setTitle("HMS"); // Set the stage title
		patientStage.setScene(scene); // Place the scene in the stage
		patientStage.show(); // Display the stage
	}

	public void newPatient()
	{
		Stage popupwindow=new Stage();
		GridPane addpane = new GridPane();
		addpane.setAlignment(Pos.CENTER);
		addpane.setPadding(new Insets(15));
		addpane.setHgap(5);
		addpane.setVgap(5);

		// Place nodes in the pane
		TextField id = new TextField();
		TextField name = new TextField();
		TextField disease = new TextField();
		TextField sex = new TextField();
		TextField admitStatus = new TextField();
		TextField age = new TextField();
		addpane.add(new Label("Please enter the new information!"), 1, 0);
		addpane.add(new Label("ID:"), 0, 1);
		addpane.add(id, 1, 1);
		addpane.add(new Label("Name:"), 0, 2);
		addpane.add(name, 1, 2);
		addpane.add(new Label("Disease:"), 0, 3);
		addpane.add(disease, 1, 3);
		addpane.add(new Label("Sex:"), 0, 4);
		addpane.add(sex, 1, 4);
		addpane.add(new Label("Admit Status:"), 0, 5);
		addpane.add(admitStatus, 1, 5);
		addpane.add(new Label("Age:"), 0, 6);
		addpane.add(age, 1, 6);

		HBox layout= new HBox(10);
		Button btNew = new Button("Apply");
		Button btCancel = new Button("Cancel");
		layout.getChildren().addAll(btNew, btCancel);
		layout.setAlignment(Pos.CENTER);
		addpane.add(layout,  1, 7);

		btNew.setOnAction(e ->{
			try {
				String S = id.getText();
				S = "," + S + "," + name.getText();
				S = S + "," + disease.getText();
				S = S + "," + sex.getText();
				S = S + "," + admitStatus.getText();
				String temp = age.getText();
				int C = Integer.parseInt(temp);
				S = S + "," + C;
				setf(S);
				popupwindow.close();
				remindUser();
				
				
			} catch (NumberFormatException E) {
				popupwindow.close();
				warningUser();
			}


		});

		btCancel.setOnAction(e -> 
		{
			popupwindow.close();
			Stage patientStage = new Stage();
			new Patient().start(patientStage);
		});
		popupwindow.initModality(Modality.APPLICATION_MODAL);



		Scene scene1= new Scene(addpane, 600, 400);
		popupwindow.setTitle("HMS");
		popupwindow.setScene(scene1);
		popupwindow.showAndWait();

	}

	public void showPatientInfo()
	{
		Stage popupwindow=new Stage();
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(15));

		HBox hbox = new HBox(70);
		hbox.setAlignment(Pos.TOP_CENTER);
		hbox.setPadding(new Insets(15));

		hbox.getChildren().addAll(new Label("ID"), new Label("Name")
				, new Label("Disease"), new Label("Sex"), new Label("Admit Status")
				, new Label("Age"));
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
		TextArea txtArea3 = new TextArea();
		TextArea txtArea4 = new TextArea();
		TextArea txtArea5 = new TextArea();
		TextArea txtArea6 = new TextArea();

		txtArea1.setPrefHeight(270);
		txtArea1.setPrefWidth(100);
		txtArea2.setPrefHeight(270);
		txtArea2.setPrefWidth(100);
		txtArea3.setPrefHeight(270);
		txtArea3.setPrefWidth(100);
		txtArea4.setPrefHeight(270);
		txtArea4.setPrefWidth(100);
		txtArea5.setPrefHeight(270);
		txtArea5.setPrefWidth(100);
		txtArea6.setPrefHeight(270);
		txtArea6.setPrefWidth(100);
		VBox v1 = new VBox();
		VBox v2 = new VBox();
		VBox v3 = new VBox();
		VBox v4 = new VBox();
		VBox v5 = new VBox();
		VBox v6 = new VBox();
		txtArea1.setEditable(false);
		txtArea2.setEditable(false);
		txtArea3.setEditable(false);
		txtArea4.setEditable(false);
		txtArea5.setEditable(false);
		v1.getChildren().add(txtArea1);
		v2.getChildren().add(txtArea2);
		v3.getChildren().add(txtArea3);
		v4.getChildren().add(txtArea4);
		v5.getChildren().add(txtArea5);
		v6.getChildren().add(txtArea6);

		File selectedFile = new File("patient.txt");
		Scanner input;
		if (selectedFile != null)
		{
			try {
				input = new Scanner(selectedFile);
				input.useDelimiter(","); 
				int rowNum=0;
				while (input.hasNext())
				{
					String temp;
					temp = input.next()+"\n";
					txtArea1.appendText(temp);
				
					temp = input.next()+"\n";
					txtArea2.appendText(temp);		
				
				    temp = input.next()+"\n";
					txtArea3.appendText(temp);
								
					temp = input.next()+"\n";
					txtArea4.appendText(temp);
					
				    temp = input.next()+"\n";
					txtArea5.appendText(temp);						
					
				    temp = input.next();
					txtArea6.appendText(temp);
					rowNum++;
					

				}	
				// Close the file
				input.close();
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		spane.getChildren().addAll(v1,v2,v3,v4,v5,v6);

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
			Stage patientStage = new Stage();
			new Patient().start(patientStage);

		});

		popupwindow.initModality(Modality.APPLICATION_MODAL);

		Scene scene= new Scene(pane, 750, 400);
		popupwindow.setTitle("HMS");
		popupwindow.setScene(scene);
		popupwindow.showAndWait();
	}

	public void deletePatient()
	{

		Stage popupwindow=new Stage();
		GridPane addpane = new GridPane();
		addpane.setAlignment(Pos.CENTER);
		addpane.setPadding(new Insets(15));
		addpane.setHgap(5);
		addpane.setVgap(5);

		// Place nodes in the pane
		TextField id = new TextField();
		TextField name = new TextField();
		TextField disease = new TextField();
		TextField sex = new TextField();
		TextField admitStatus = new TextField();
		TextField age = new TextField();
		addpane.add(new Label("Please enter the information to be deleted!"), 0, 0);
		addpane.add(new Label("ID:"), 0, 1);
		addpane.add(id, 1, 1);
		addpane.add(new Label("Patient Name:"), 0, 2);
		addpane.add(name, 1, 2);
		addpane.add(new Label("Disease:"), 0, 3);
		addpane.add(disease, 1, 3);
		addpane.add(new Label("Sex:"), 0, 4);
		addpane.add(sex, 1, 4);
		addpane.add(new Label("Admit Status:"), 0, 5);
		addpane.add(admitStatus, 1, 5);
		addpane.add(new Label("Age:"), 0, 6);
		addpane.add(age, 1, 6);

		HBox layout= new HBox(10);
		Button btNew = new Button("Apply");
		Button btCancel = new Button("Cancel");
		layout.getChildren().addAll(btNew, btCancel);
		layout.setAlignment(Pos.CENTER);
		addpane.add(layout,  1, 7);

		btNew.setOnAction(e ->{
			String S = name.getText();
			replace(S);
			popupwindow.close();
			remindUser();
		});

		btCancel.setOnAction(e -> 
		{
			popupwindow.close();
			Stage patientStage = new Stage();
			new Patient().start(patientStage);
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
			Stage MedicalStage = new Stage();
			new Patient().start(MedicalStage);

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
			Stage MedicalStage = new Stage();
			new Patient().start(MedicalStage);

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
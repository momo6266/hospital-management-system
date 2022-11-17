
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

public class Medical extends Application{

	Button btAdd = new Button("Add New Entry");
	Button btExistList = new Button("Existing medicines");
	Button btDelete = new Button("Delete old entry");
	Button btMenu = new Button("Back to Main Menu");

	private String name, manufacturer, expiryDate;
	private int cost, count;
	final static String DATE_FORMAT = "dd-MM-yy";
	Medical[] md = new Medical[100];


	public Medical(String name, String manufacturer, String expiryDate, int cost, int count)
	{
		this.name = name;
		this.manufacturer = manufacturer;
		this.expiryDate = expiryDate;
		this.cost = cost;
		this.count = count;
	}
	public Medical() {
		// TODO Auto-generated constructor stub
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public void setf(String medical) {
		try (FileWriter f = new FileWriter("medical.txt", true);
				BufferedWriter b = new BufferedWriter(f);
				PrintWriter p = new PrintWriter(b);) {

			p.println(medical);

		} catch (IOException i) {
			i.printStackTrace();
		}

	}

	public void replace(String old) {
		String oldFileName = "medical.txt";
		String tmpFileName = "tmp_medical.txt";

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

		File myFile = new File("medical.txt");
		Scanner input;
		int count = 0;
		try {
			input = new Scanner(myFile);
			while(input.hasNext())
			{
				String name = input.next();
				String mf = input.next();
				String date = input.next();
				int cost = input.nextInt();
				int unit = input.nextInt();
				md[count] = new Medical(name,mf,date,cost,unit);
				count++;

			}
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void start(Stage MedicalStage) {


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
			MedicalStage.close();
			newmedical();
		});
		btExistList.setOnAction(e ->
		{
			MedicalStage.close();
			findMedical();
		});
		btDelete.setOnAction(e ->
		{
			MedicalStage.close();
			deletemedical();
		});
		btMenu.setOnAction(e ->
		{
			MedicalStage.close();
			Stage primaryStage = new Stage();
			new HospitalManagement().start(primaryStage);
		});

		pane.setCenter(hBox);
		pane.setBottom(hBox2);
		BorderPane.setAlignment(hBox, Pos.TOP_CENTER); 

		// Create a scene and place it in the stage
		Scene scene = new Scene(pane, 600, 400);
		MedicalStage.setTitle("HMS"); // Set the stage title
		MedicalStage.setScene(scene); // Place the scene in the stage
		MedicalStage.show(); // Display the stage
	}

	public void newmedical()
	{
		Stage popupwindow=new Stage();
		GridPane addpane = new GridPane();
		addpane.setAlignment(Pos.CENTER);
		addpane.setPadding(new Insets(15));
		addpane.setHgap(5);
		addpane.setVgap(5);

		// Place nodes in the pane
		TextField name = new TextField();
		TextField mf = new TextField();
		TextField date = new TextField();
		date.setPromptText("(dd-MM-yy)");
		TextField cost = new TextField();
		TextField count = new TextField();
		addpane.add(new Label("Please enter the new information!"), 1, 0);
		addpane.add(new Label("Name:"), 0, 1);
		addpane.add(name, 1, 1);
		addpane.add(new Label("Manufacturer:"), 0, 2);
		addpane.add(mf, 1, 2);
		addpane.add(new Label("Expiry Date:"), 0, 3);
		addpane.add(date, 1, 3);
		addpane.add(new Label("Cost:"), 0, 4);
		addpane.add(cost, 1, 4);
		addpane.add(new Label("Unit:"), 0, 5);
		addpane.add(count, 1, 5);

		HBox layout= new HBox(10);
		Button btNew = new Button("Apply");
		Button btCancel = new Button("Cancel");
		layout.getChildren().addAll(btNew, btCancel);
		layout.setAlignment(Pos.CENTER);
		addpane.add(layout,  1, 6);

		btNew.setOnAction(e ->{
			try {
				String S = name.getText();
				S = "," + S + "," + mf.getText();
				String tmp = date.getText();
				DateFormat df = new SimpleDateFormat(DATE_FORMAT);
				df.setLenient(false);
				df.parse(tmp);
				S = S + "," + tmp;
				try {
					String temp = cost.getText();
					int C = Integer.parseInt(temp);
					S = S + ",RM" + C;
					try {
						String tp = count.getText();
						int D = Integer.parseInt(tp);
						S = S + "," + D;
						setf(S);
						popupwindow.close();
						remindUser();
					}
					catch(NumberFormatException G)
					{
						popupwindow.close();
						warningUser();
					}
				}
				catch(NumberFormatException G)
				{
					popupwindow.close();
					warningUser();
				}
				
			} catch (ParseException E) {
				popupwindow.close();
				warningUser();
			}


		});

		btCancel.setOnAction(e -> 
		{
			popupwindow.close();
			Stage MedicalStage = new Stage();
			new Medical().start(MedicalStage);
		});
		popupwindow.initModality(Modality.APPLICATION_MODAL);



		Scene scene1= new Scene(addpane, 600, 400);
		popupwindow.setTitle("HMS");
		popupwindow.setScene(scene1);
		popupwindow.showAndWait();

	}

	public void findMedical()
	{
		Stage popupwindow=new Stage();
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(15));

		HBox hbox = new HBox(70);
		hbox.setAlignment(Pos.TOP_CENTER);
		hbox.setPadding(new Insets(15));

		hbox.getChildren().addAll(new Label("Name"), new Label("Manufacturer"), new Label("Expiry Date"), new Label("Cost"));
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
		TextArea tmp = new TextArea();
		txtArea1.setPrefHeight(270);
		txtArea1.setPrefWidth(125);
		txtArea2.setPrefHeight(270);
		txtArea2.setPrefWidth(125);
		txtArea3.setPrefHeight(270);
		txtArea3.setPrefWidth(125);
		txtArea4.setPrefHeight(270);
		txtArea4.setPrefWidth(125);
		VBox v1 = new VBox();
		VBox v2 = new VBox();
		VBox v3 = new VBox();
		VBox v4 = new VBox();
		txtArea1.setEditable(false);
		txtArea2.setEditable(false);
		txtArea3.setEditable(false);
		txtArea4.setEditable(false);
		v1.getChildren().add(txtArea1);
		v2.getChildren().add(txtArea2);
		v3.getChildren().add(txtArea3);
		v4.getChildren().add(txtArea4);
		
		File selectedFile = new File("medical.txt");
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
						txtArea2.appendText(input.next()+"\n");
						i++;
					}
					else if (i == 2)
					{
						txtArea3.appendText(input.next()+"\n");
						i++;
					}
					else if(i == 3)
					{
						txtArea4.appendText(input.next()+"\n");
						i++;
					}
					else
					{
						tmp.appendText(input.next()+"\n");
						i = 0;
					}
					

				}	
				// Close the file
				input.close();
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		spane.getChildren().addAll(v1,v2,v3,v4);

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
			Stage MedicalStage = new Stage();
			new Medical().start(MedicalStage);

		});

		popupwindow.initModality(Modality.APPLICATION_MODAL);

		Scene scene= new Scene(pane, 600, 400);
		popupwindow.setTitle("HMS");
		popupwindow.setScene(scene);
		popupwindow.showAndWait();
	}

	public void deletemedical()
	{

		Stage popupwindow=new Stage();
		GridPane addpane = new GridPane();
		addpane.setAlignment(Pos.CENTER);
		addpane.setPadding(new Insets(15));
		addpane.setHgap(5);
		addpane.setVgap(5);

		// Place nodes in the pane
		TextField name = new TextField();
		TextField mf = new TextField();
		TextField date = new TextField();
		date.setPromptText("(dd-MM-yy)");
		addpane.add(new Label("Please enter the information to be deleted!"), 1, 0);
		addpane.add(new Label("Name:"), 0, 1);
		addpane.add(name, 1, 1);
		addpane.add(new Label("Manufacturer:"), 0, 2);
		addpane.add(mf, 1, 2);
		addpane.add(new Label("Date:"), 0, 3);
		addpane.add(date, 1, 3);
		

		HBox layout= new HBox(10);
		Button btNew = new Button("Apply");
		Button btCancel = new Button("Cancel");
		layout.getChildren().addAll(btNew, btCancel);
		layout.setAlignment(Pos.CENTER);
		addpane.add(layout,  1, 4);

		btNew.setOnAction(e ->{
			try {
			String S = name.getText();
			S = S + "," + mf.getText();
			String tmp = date.getText();
			DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			df.setLenient(false);
			df.parse(tmp);
			S = S + "," + tmp;
			replace(S);
			popupwindow.close();
			remindUser();
			}
			catch (ParseException E) 
			{
			popupwindow.close();
			warningUser();
			}

		});

		btCancel.setOnAction(e -> 
		{
			popupwindow.close();
			Stage MedicalStage = new Stage();
			new Medical().start(MedicalStage);
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
			new Medical().start(MedicalStage);

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
			new Medical().start(MedicalStage);

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


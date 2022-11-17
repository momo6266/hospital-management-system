import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HospitalManagement extends Application{

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {
		BorderPane pane = new BorderPane();

		// top: text field
		BorderPane paneForTextField = new BorderPane();
		paneForTextField.setPadding(new Insets(5));

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String stringDate= dtf.format(now);
		String menuword = "Welcome to HMS! The current date and time: " + stringDate + "\n\n\t\t\t\t\t   MAIN PAGE";
		Text menutext = new Text(menuword);
		paneForTextField.setCenter(menutext);
		pane.setTop(paneForTextField);

		// Hold buttons in an HBox
		HBox hBox = new HBox();
		hBox.setSpacing(10);
		hBox.setAlignment(Pos.CENTER);
		Button btDoctors = new Button("Doctors");
		Button btPatients = new Button("Patients");
		Button btMedicines= new Button("Medicines");
		Button btLaboratories = new Button("Laboratories");
		Button btFacilities = new Button("Facilities");
		Button btStaff = new Button("Staff");
		Button btClose = new Button("Quit");
		hBox.getChildren().add(btDoctors);
		hBox.getChildren().add(btPatients);
		hBox.getChildren().add(btMedicines);
		hBox.getChildren().add(btLaboratories);
		hBox.getChildren().add(btFacilities);
		hBox.getChildren().add(btStaff);
		pane.setBottom(btClose);
		BorderPane.setAlignment(btClose,Pos.TOP_CENTER);
		
		btDoctors.setOnAction(e->{
			primaryStage.close();
			Stage doctorStage = new Stage();
			new Doctor().start(doctorStage);
				 });
				 

		btPatients.setOnAction(e->{
			primaryStage.close();
			Stage patientStage = new Stage();
			new Patient().start(patientStage); 
				 });

		btMedicines.setOnAction(e->
		{
			primaryStage.close();
			Stage medicalStage = new Stage();
			new Medical().start(medicalStage);
		});

		btLaboratories.setOnAction(e->
		{
			primaryStage.close();
			Stage laboratoriesStage = new Stage();
			new Laboratories().start(laboratoriesStage);
		});

		btFacilities.setOnAction(e->
		{
			primaryStage.close();
			Stage facilitiesStage = new Stage();
			new Facilities().start(facilitiesStage);
		});

		btStaff.setOnAction(e->{
			primaryStage.close();
			Stage staffStage = new Stage();
			new Staff().start(staffStage);
				 });
		
		btClose.setOnAction(e->
		{
			primaryStage.close();
		});


		pane.setCenter(hBox);
		BorderPane.setAlignment(hBox, Pos.TOP_CENTER); 

		// Create a scene and place it in the stage
		Scene scene = new Scene(pane, 600, 400);
		primaryStage.setTitle("HMS"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
	}

	
	public static void main(String[] args)
	{		
		launch(args);
	}


}
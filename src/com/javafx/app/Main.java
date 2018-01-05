package com.javafx.app;

import java.io.File;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.report.maker.EmailSender;
import com.report.maker.EmailServerDetails;
import com.report.maker.Student;

public class Main extends Application {

	@Override
	public void start(final Stage primaryStage) {
		// Image image = new Image("https://www.cpcc.edu/ican/trc/images/uncc_logo.jpg");
		 
         // simple displays ImageView the image as is
        
        // iv1.setImage(image);
 
         
         
		primaryStage.setTitle("Report Generator");
		primaryStage.getIcons().add(new Image("https://www.cpcc.edu/ican/trc/images/uncc_logo.jpg"));
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		Text scenetitle = new Text("Enter your Credentials: ");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scenetitle, 0, 0, 2, 1);

		Label userName = new Label("Email Id:");
		grid.add(userName, 0, 1);

		final TextField emailIdField = new TextField();
		grid.add(emailIdField, 1, 1);

		Label pw = new Label("Password:");
		grid.add(pw, 0, 2);

		PasswordField pwdField = new PasswordField();
		grid.add(pwdField, 1, 2);

		final FileChooser fileChooser = new FileChooser();

		Button browseButton = new Button("Browse Grades XML File");
		HBox hbBtn1 = new HBox(10);
		hbBtn1.setAlignment(Pos.TOP_RIGHT);
		hbBtn1.getChildren().add(browseButton);
		grid.add(hbBtn1, 1, 5);

		final Button submitBtn = new Button("Generate Report");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		
		hbBtn.getChildren().add(submitBtn);
		grid.add(hbBtn, 1, 6);

		final Text filePathText = new Text();
		grid.add(filePathText, 1, 4);

		TextArea textArea = new TextArea();
		textArea.setEditable(false);

		grid.add(textArea, 1, 7);

		Scene welcomeScene = new Scene(grid, 700, 500);
		primaryStage.setScene(welcomeScene);
		//
		// GridPane statusGrid = new GridPane();
		// statusGrid.setAlignment(Pos.CENTER);
		// statusGrid.setHgap(10);
		// statusGrid.setVgap(10);
		// statusGrid.setPadding(new Insets(25, 25, 25, 25));
		//
		// Label label = new Label("Processing XML:");
		// statusGrid.add(label, 0, 1);

		submitBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				String emailId = emailIdField.getText();
				String password = pwdField.getText();
				String path = filePathText.getText();

				Thread thread = new Thread(() -> {
					List<Student> students = EmailSender.readGradesXml(path);
					EmailServerDetails emailServerDetails = EmailSender.getEmailServerDetails();
					emailServerDetails.setUsername(emailId);
					emailServerDetails.setPassword(password);

					
					// Prepare HTML Content
					for (int k = 0; k < students.size(); k++) {
						Student student = students.get(k);
						
						String htmlContent = EmailSender.getHtmlContent(student);
						try{
						EmailSender.send_email(emailServerDetails, htmlContent, student);
					//	textArea.appendText("\nProcessing XML... \n");
						textArea.appendText("\nSending Report to " + student.getName());
						textArea.appendText("\nMessage sent successfully");
						
						}
						catch(Exception e1){
							textArea.appendText("Invalid UserName/Password");
						throw e1;
							
						}
						
					}
				});
				thread.start();

			}
		});

		browseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				File file = fileChooser.showOpenDialog(primaryStage);
				if (file != null) {
					filePathText.setText(file.getPath());
				}
			}
		});

		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
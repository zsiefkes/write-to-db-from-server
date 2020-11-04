import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Date;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	TextField text = new TextField();

	@Override
	public void start(Stage primaryStage) {
		try {
			Button btn = new Button("Connect");
			VBox box = new VBox(btn, text);

			btn.setOnAction(e -> connect());

			Scene scene = new Scene(box, 400, 400);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void connect() {
		int port = Integer.valueOf(text.getText());
		ServerSocket listener;
		try {
			listener = new ServerSocket(port);
			System.out.println("Server started on: " + port);
//			while (true) {
				System.out.println("Server is up and running waiting on connections...");
				Socket socket = listener.accept();
				try {
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					System.out.println("A client request received at " + socket);
					String st = new Date().toString();
					out.println(st);
					insertTimeStampIntoDB(st);
				} finally {
					socket.close();
				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// listener.close();
		}
	}

	public void insertTimeStampIntoDB(String ts) {
		try {
			// System.out.println(Inet4Address.getLocalHost().getHostAddress());
			String databaseUser = "dizach";
			String databaseUserPass = "123";
			Class.forName("org.postgresql.Driver");
			Connection connection = null;
			String url = "jdbc:postgresql://localhost/testdb";
			connection = DriverManager.getConnection(url, databaseUser, databaseUserPass);
			Statement s = connection.createStatement();
			s.executeUpdate("insert into timestamps values ('" + ts + "')");
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("LogIn Error: " + e.toString());
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}

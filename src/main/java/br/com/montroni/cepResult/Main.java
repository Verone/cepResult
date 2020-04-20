package br.com.montroni.cepResult;

import java.net.URL;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(final Stage palco) throws Exception {
		URL arquivoFXML = getClass().getResource("frmForm.fxml");
		Parent fxmlParent = (Parent) FXMLLoader.load(arquivoFXML);
		palco.setScene(new Scene(fxmlParent, 670, 690));
		palco.setTitle("CEPFácil v1.19");
		palco.setResizable(false);
		palco.show();
		palco.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});
	}

}

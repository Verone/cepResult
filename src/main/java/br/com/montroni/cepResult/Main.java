package br.com.montroni.cepResult;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage palco) throws Exception {
		URL arquivoFXML = getClass().getResource("frmForm.fxml");
		Parent fxmlParent = (Parent) FXMLLoader.load(arquivoFXML);
		palco.setScene(new Scene(fxmlParent, 670, 690));
		palco.setTitle("CEPFácil v1.13");
		palco.setResizable(false);
		palco.show();
	}
}

package isd.aims.main.views.payment;

import java.io.IOException;

import isd.aims.main.views.BaseForm;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ResultForm extends BaseForm {
	public ResultForm(Stage stage, String screenPath, String result, String message) throws IOException {
		super(stage, screenPath);
		resultLabel.setText(result);
		messageLabel.setText(message);
		okButton.setOnMouseClicked(e -> {
			if (homeScreenHandler != null) {
				homeScreenHandler.show();
			} else {
				System.out.println("homeScreenHandler là null khi bấm nút.");
			}
		});
	}

	@FXML
	private Label pageTitle;

	@FXML
	private Label resultLabel;

	@FXML
	private Button okButton;

	@FXML
	private Label messageLabel;

	@FXML
	void confirmPayment(MouseEvent event) throws IOException {
		homeScreenHandler.show();
	}

}

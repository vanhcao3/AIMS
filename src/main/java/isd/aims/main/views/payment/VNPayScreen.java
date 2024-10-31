package isd.aims.main.views.payment;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;

import isd.aims.main.InterbankSubsystem.IPayment;
import isd.aims.main.InterbankSubsystem.VnPaySubsystem;
import isd.aims.main.InterbankSubsystem.vnPay.VnPaySubsystemController;
import isd.aims.main.controller.PaymentController;
import isd.aims.main.entity.invoice.Invoice;
import isd.aims.main.entity.payment.PaymentTransaction;
import isd.aims.main.entity.response.Response;
import isd.aims.main.InterbankSubsystem.vnPay.VnPayConfig;
import isd.aims.main.utils.Configs;
import isd.aims.main.views.BaseScreenHandler;
import isd.aims.main.views.home.HomeScreenHandler;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class VNPayScreen extends BaseScreenHandler {

	// @FXML
	// private Button btnConfirmPayment;

	// @FXML
	// private ImageView loadingImage;

	private Invoice invoice;
    private String paymentURL;
    @FXML
    private VBox vBox;
    private PaymentTransaction transactionResult;
    public PaymentTransaction getTransactionResult() {
        return transactionResult;
    }

    public VNPayScreen(Stage stage, String screenPath, String paymentURL) throws IOException {
        super(stage, screenPath);
        this.paymentURL = paymentURL;
        WebView paymentView = new WebView();
        WebEngine webEngine = paymentView.getEngine();
        webEngine.load(paymentURL);

        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
            // Xử lý khi URL thay đổi
            if (newValue.contains(VnPayConfig.vnp_ReturnUrl)) {
                handleUrlChanged(newValue);
            }
        });
        vBox.getChildren().clear();
        vBox.getChildren().add(paymentView);
    }




    private void handleUrlChanged(String newValue) {
        if (newValue.contains(VnPayConfig.vnp_ReturnUrl)) {
            try {
                // Xử lý giao dịch và lưu kết quả
                transactionResult = VnPaySubsystemController.processResponse(newValue);

                if (transactionResult != null) {
                    homeScreenHandler = new HomeScreenHandler(stage, Configs.HOME_PATH);
                    showResultScreen(transactionResult);
                    transactionResult.save(1);
                }
            } catch (URISyntaxException | ParseException | IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void showResultScreen(PaymentTransaction transactionResult) throws IOException {
        // Retrieve the result and message from the transaction result
        String result = transactionResult.isSuccess() ? "SUCCESS" : "FAILURE";
        String message = transactionResult.getMessage();

        // Create an instance of ResultScreenHandler with the result and message
        BaseScreenHandler resultScreen = new ResultScreenHandler(this.stage, Configs.RESULT_SCREEN_PATH, result, message);

        // Set the previous screen and home screen handler
        resultScreen.setPreviousScreen(this);
        resultScreen.setHomeScreenHandler(homeScreenHandler);
        resultScreen.setScreenTitle("Result Screen");

        // Show the result screen
        resultScreen.show();
    }

}

package com.proto.marginalapplication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MarginalAppController {

    @FXML
    private TextField buyPriceField;

    @FXML
    private TextField sellPriceField;

    @FXML
    private TextField lotSizeField;

    @FXML
    private ComboBox<String> takeProfitCombo;

    @FXML
    private Label marginDisplayLabel;

    @FXML
    private Label resultLabel;

    @FXML
    private ToggleGroup leverageGroup;

    private double selectedLeverage = 0.1; // Default 1/10

    @FXML
    public void initialize() {
        takeProfitCombo.getItems().addAll("Buy Order", "Sell Order");
        takeProfitCombo.setValue("Buy Order");

        // Set number input restrictions
        setNumberFieldValidator(buyPriceField);
        setNumberFieldValidator(sellPriceField);
        setNumberFieldValidator(lotSizeField);

        // Add listeners for real-time calculation
        buyPriceField.textProperty().addListener((obs, oldVal, newVal) -> calculateResult());
        sellPriceField.textProperty().addListener((obs, oldVal, newVal) -> calculateResult());
        lotSizeField.textProperty().addListener((obs, oldVal, newVal) -> calculateResult());
        takeProfitCombo.valueProperty().addListener((obs, oldVal, newVal) -> calculateResult());
    }

    /**
     * Validates that only numeric input is allowed in TextField
     */
    private void setNumberFieldValidator(TextField field) {
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && !newVal.matches("\\d+(\\.\\d*)?")) {
                field.setText(oldVal);
            }
        });
    }

    @FXML
    private void onLeverageButtonClicked(ActionEvent event) {
        RadioButton button = (RadioButton) event.getSource();
        String leverageText = button.getText();

        switch (leverageText) {
            case "1/10" -> selectedLeverage = 0.1;
            case "1/20" -> selectedLeverage = 0.05;
            case "1/30" -> selectedLeverage = 0.033333;
            case "1/50" -> selectedLeverage = 0.02;
            case "1/100" -> selectedLeverage = 0.01;
            case "1/200" -> selectedLeverage = 0.005;
            case "1/500" -> selectedLeverage = 0.002;
        }

        calculateResult();
    }

    private void calculateResult() {
        try {
            // Check if all fields have valid input
            if (buyPriceField.getText().isEmpty() ||
                    sellPriceField.getText().isEmpty() ||
                    lotSizeField.getText().isEmpty()) {
                resultLabel.setText("Result: --");
                marginDisplayLabel.setText("Margin: --");
                return;
            }

            double buyPrice = Double.parseDouble(buyPriceField.getText());
            double sellPrice = Double.parseDouble(sellPriceField.getText());
            double lotSize = Double.parseDouble(lotSizeField.getText());
            String orderType = takeProfitCombo.getValue();

            // Validate input ranges
            if (buyPrice <= 0 || sellPrice <= 0 || lotSize <= 0) {
                resultLabel.setText("Result: Invalid input (must be > 0)");
                marginDisplayLabel.setText("Margin: --");
                resultLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            if (lotSize < 0.01 || lotSize > 500.00) {
                resultLabel.setText("Result: Lot size must be 0.01-500.00");
                marginDisplayLabel.setText("Margin: --");
                resultLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            // Calculate margin difference
            double marginDiff = Math.abs(sellPrice - buyPrice);
            marginDisplayLabel.setText(String.format("Margin: Buy: %.4f | Sell: %.4f | Diff: %.4f",
                    buyPrice, sellPrice, marginDiff));

            // Calculate profit/loss based on order type
            double result;
            if ("Buy Order".equals(orderType)) {
                // For buy: profit if sell price > buy price
                result = (sellPrice - buyPrice) * lotSize * (1.0 / selectedLeverage);
            } else {
                // For sale: profit if buy price > sell price
                result = (buyPrice - sellPrice) * lotSize * (1.0 / selectedLeverage);
            }

            // Display result with proper formatting
            resultLabel.setText(String.format("Result: %.4f", result));
            resultLabel.setStyle(result >= 0 ? "-fx-text-fill: #00AA00;" : "-fx-text-fill: #FF0000;");

        } catch (NumberFormatException e) {
            resultLabel.setText("Result: Invalid number format");
            marginDisplayLabel.setText("Margin: --");
            resultLabel.setStyle("-fx-text-fill: red;");
        } catch (Exception e) {
            resultLabel.setText("Result: Error - " + e.getMessage());
            marginDisplayLabel.setText("Margin: --");
            resultLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void onClearAll() {
        buyPriceField.clear();
        sellPriceField.clear();
        lotSizeField.clear();
        resultLabel.setText("Result: --");
        marginDisplayLabel.setText("Margin: --");
        resultLabel.setStyle("-fx-text-fill: black;");
        // Reset to default leverage
        leverageGroup.selectToggle(leverageGroup.getToggles().get(0));
        takeProfitCombo.setValue("Buy Order");
    }
}
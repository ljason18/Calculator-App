package project;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Setup extends VBox {
    Label displayInput;
    Label result;
    TilePane numPad;
    Button[] numbers;
    String[] labels = {"%", "CE", "C", "Undo", "1/x", "x^2", "sqrt(x)",
        "/", "7", "8", "9", "*", "4", "5", "6", "-", "1", "2", "3", "+", "+/-", "0", "." , "="};
    double answer;
    String operation = "";
    
    public Setup() {
        super();
        this.setSpacing(4);
        displayInput = new Label();
        displayInput.setPrefSize(300, 55);
        displayInput.setAlignment(Pos.BOTTOM_RIGHT);
        displayInput.setText("");
        result = new Label();
        result.setPrefSize(300, 80);
        result.setAlignment(Pos.BOTTOM_RIGHT);
        numPad = new TilePane();
        numPad.setPrefSize(300, 300);
        numPad.setHgap(4);
        numPad.setVgap(4);
        numPad.setPrefColumns(4);
        
        numbers = new Button[24];
        for (int num = 0; num < 24; num++) {
            numbers[num] = new Button(labels[num]);
            numbers[num].setPrefSize(72, 72);
            if (num == 0)
                numbers[num].setOnAction(percent);
            else if (num == 1)
                numbers[num].setOnAction(clear);
            else if (num == 2)
                numbers[num].setOnAction(reset);
            else if (num == 3)
                numbers[num].setOnAction(undo);
            else if (num == 4)
                numbers[num].setOnAction(flip);    
            else if (num == 5)
                numbers[num].setOnAction(square);
            else if (num == 6)
                numbers[num].setOnAction(root);
            else if (num == 7 || num == 11 || num == 15 || num == 19)
                numbers[num].setOnAction(operator);
            else if (num == 20)
                numbers[num].setOnAction(changeSigns);
            else if (num == 23)
                numbers[num].setOnAction(enter);
            else
                numbers[num].setOnAction(input);
        } // for

        for (Button press : numbers) {
            numPad.getChildren().add(press);
        } //  for

        this.getChildren().addAll(displayInput, result, numPad);
    } // Setup

    EventHandler<ActionEvent> input = event -> {
        String inputText = ((Labeled) event.getSource()).getText();
        if (!inputText.isEmpty() && inputText.matches("[0-9.]+")) {
            if (!result.getText().equals("") && result.getText() != null && !displayInput.getText().equals(""))  {
                displayInput.setText("");
                result.setText("");
                answer = 0;
            }
            displayInput.setText(displayInput.getText() + inputText);
        }
    };
    
    EventHandler<ActionEvent> undo = event -> {
        if (!displayInput.getText().equals("")) {
            displayInput.setText(displayInput.getText().substring(0,displayInput.getText().length()-1));
        }
    }; 
    
    EventHandler<ActionEvent> clear = event -> {
        if (!operation.equals("")) {
            int cutoff = displayInput.getText().indexOf(operation);
            displayInput.setText(displayInput.getText().substring(0, cutoff + 1));
        } else {
            displayInput.setText("");
            result.setText("");
            answer = 0;
            operation = "";
        }
    }; 

    EventHandler<ActionEvent> reset = event -> {
        displayInput.setText("");
        result.setText("");
        answer = 0;
        operation = "";
    }; 

    EventHandler<ActionEvent> percent = event -> {
        double percentage = 0;
        if (operation.equals("") && result.getText().equals("")) {
            percentage = Double.valueOf(displayInput.getText()) / 100;
            displayInput.setText(Double.toString(percentage));
        } else if (operation.equals("") && !result.getText().equals("")) {
            percentage = Double.valueOf(result.getText()) / 100;
            displayInput.setText(Double.toString(percentage));
            result.setText("");
            operation = "";
        }
        answer = percentage;
    }; 

    EventHandler<ActionEvent> operator = event -> {
        if (answer == 0 && result.getText().equals("")) {
            answer = Double.valueOf(displayInput.getText());
            displayInput.setText(displayInput.getText() + ((Labeled) event.getSource()).getText());
            operation = displayInput.getText().substring(displayInput.getText().length()-1);
        } else {
            int position = displayInput.getText().indexOf(operation);
            double value = 0;
            if (result.getText() != null) {
                value = Double.valueOf(result.getText().substring(position+1));
                result.setText(null);
            } else
                value = Double.valueOf(displayInput.getText().substring(position+1));
            calculate(operation, value);
            displayInput.setText(Double.toString(answer) + ((Labeled) event.getSource()).getText());
            operation = displayInput.getText().substring(displayInput.getText().length()-1);
        } // if
    }; // operator

    EventHandler<ActionEvent> changeSigns = event -> {
        if (!operation.equals("")) {
            int cutoff = displayInput.getText().indexOf(operation);
            if (displayInput.getText().substring(cutoff + 1).startsWith("-")) {
                displayInput.setText(displayInput.getText().substring(0, cutoff + 1) + displayInput.getText().substring(cutoff + 1).substring(1));
            } else {
                displayInput.setText(displayInput.getText().substring(0, cutoff + 1) + "-" + displayInput.getText().substring(cutoff + 1));
            }
        } else {
            if (displayInput.getText().startsWith("-")) {
                displayInput.setText(displayInput.getText().substring(1));
            } else {
                displayInput.setText("-" + displayInput.getText());
            }
        }
    };

    EventHandler<ActionEvent> enter = event -> {
        int position = displayInput.getText().indexOf(operation);
        double secondFactor = Double.valueOf(displayInput.getText().substring(position+1));
        calculate(operation, secondFactor);
        displayInput.setText(displayInput.getText() + ((Labeled) event.getSource()).getText());
        result.setText(Double.toString(answer));
        result.setFont(new Font(30));
        operation = "";
    };

    EventHandler<ActionEvent> flip = event -> {
        double temp;
        if (!operation.equals("")) {
            int cutoff = displayInput.getText().indexOf(operation);
            temp = Double.valueOf(displayInput.getText().substring(cutoff + 1));
            temp = 1 / temp;
            displayInput.setText(displayInput.getText().substring(0, cutoff + 1) + temp);
        } else {
            temp = Double.valueOf(displayInput.getText());
            temp = 1 / temp;
            displayInput.setText(Double.toString(temp));
        }
    };

    EventHandler<ActionEvent> square = event -> {
        double temp;
        System.out.println(result.getText());
        if (!result.getText().equals("")) {
            temp = Double.valueOf(result.getText());
            temp = Math.pow(temp,2);
            displayInput.setText(Double.toString(temp));
            result.setText(null);
        } else {
            if (!operation.equals("")) {
                int cutoff = displayInput.getText().indexOf(operation);
                temp = Double.valueOf(displayInput.getText().substring(cutoff + 1));
                temp = Math.pow(temp,2);
                displayInput.setText(displayInput.getText().substring(0, cutoff + 1) + temp);
            } else {
                temp = Double.valueOf(displayInput.getText());
                temp = Math.pow(temp,2);
                displayInput.setText(Double.toString(temp));
            }
        }
    };

    EventHandler<ActionEvent> root = event -> {
       double temp;
        if (!operation.equals("")) {
            int cutoff = displayInput.getText().indexOf(operation);
            temp = Double.valueOf(displayInput.getText().substring(cutoff + 1));
            temp = Math.sqrt(temp);
            displayInput.setText(displayInput.getText().substring(0, cutoff + 1) + temp);
        } else {
            temp = Double.valueOf(displayInput.getText());
            temp = Math.sqrt(temp);
            displayInput.setText(Double.toString(temp));
        }
    };

    private void calculate(String operation, double second) {
        switch (operation) {
            case "+":
                answer += second;
                break;
    
            case "-":
                answer -= second;
                break;
    
            case "/":
                if (second != 0) {
                    answer /= second;
                } else {
                    // Handle division by zero
                    answer = Double.NaN;
                }
                break;
    
            case "*":
                answer *= second;
                break;
        }
    }
    
} // Setup

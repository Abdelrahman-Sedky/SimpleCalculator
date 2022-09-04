package com.bignerdranch.android.simplecalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ConstraintLayout constraintLayout;
    private TextView resultTextView, historyTextView;
    private StringBuilder expression = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();
        onClickListenerAll();


    }

    /**
     * @desc Initialize Used UI Components (Result TextView & History TextView)
     */
    private void initComponent() {
        constraintLayout = findViewById(R.id.root_layout);
        resultTextView = findViewById(R.id.result_tv);
        historyTextView = findViewById(R.id.history_tv);

    }

    /**
     * @desc Set onClickListener For All Calculator Buttons
     */
    private void onClickListenerAll() {
        for (int i = 0; i < constraintLayout.getChildCount(); i++) {
            if (constraintLayout.getChildAt(i) instanceof Button) {

                constraintLayout.getChildAt(i).setOnClickListener(this);

            }
        }
    }

    @Override
    public void onClick(View view) {
        Button clickedButton = (Button) view;
        int clickedButtonId = clickedButton.getId();
        String resultTextViewText = resultTextView.getText().toString();
        String clickedButtonText = clickedButton.getText().toString();

        if (clickedButtonId == R.id.clear_btn) {

            clearButtonClicked();

        } else if (clickedButtonId == R.id.sign_btn) {

            signButtonClicked(resultTextViewText);

        } else if (clickedButtonId == R.id.percent_btn) {

            percentButtonClicked(resultTextViewText);

        } else if (clickedButtonId == R.id.divide_btn) {

            operationButtonClicked(resultTextViewText, clickedButtonText);

        } else if (clickedButtonId == R.id.multiply_btn) {

            operationButtonClicked(resultTextViewText, clickedButtonText);

        } else if (clickedButtonId == R.id.minus_btn) {

            operationButtonClicked(resultTextViewText, clickedButtonText);

        } else if (clickedButtonId == R.id.plus_btn) {

            operationButtonClicked(resultTextViewText, clickedButtonText);

        } else if (clickedButtonId == R.id.dot_btn) {

            dotButtonClicked(resultTextViewText);

        } else if (clickedButtonId == R.id.delete_btn) {

            deleteButtonClicked(resultTextViewText);

        } else if (clickedButtonId == R.id.equal_btn) {

            equalButtonClicked(resultTextViewText);

        } else {

            resultTextView.append(clickedButtonText);

        }


    }


    private void operationButtonClicked(@NonNull String resultTextViewText, String clickedButtonText) {
        if (!resultTextViewText.isEmpty()) {
            expression.append(resultTextViewText).append(clickedButtonText);
            resultTextView.setText(null);
        }
        historyTextView.setText(expression.toString());
    }

    private void deleteButtonClicked(String resultTextViewText) {
        StringBuilder restored = new StringBuilder();

        if (expression.toString().isEmpty() && resultTextViewText.isEmpty()) {
            return;
        }

        if (resultTextViewText.isEmpty()) {
            if (Character.isDigit(expression.charAt(expression.length() - 1))) {
                while (!expression.toString().isEmpty() && Character.isDigit(expression.charAt(expression.length() - 1))) {
                    restored.append(expression.charAt(expression.length() - 1));
                    expression.deleteCharAt(expression.length() - 1);
                }
                restored.reverse();
                resultTextView.setText(restored.toString());
            } else {
                expression.deleteCharAt(expression.length() - 1);
            }
        } else {
            restored.append(resultTextViewText);
            resultTextView.setText(restored.deleteCharAt(restored.length() - 1));
        }
        historyTextView.setText(expression.toString());
    }

    private void clearButtonClicked() {
        resultTextView.setText(null);
        historyTextView.setText(null);
        expression = new StringBuilder();
    }

    private void signButtonClicked(String resultTextViewText) {
        double number = 0;
        try {
            number = -Double.parseDouble(resultTextViewText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (number - (int) number == 0) {
            resultTextView.setText(String.valueOf((int) number));
        } else {
            resultTextView.setText(String.valueOf(number));
        }
    }

    private void percentButtonClicked(@NonNull String resultTextViewText) {
        if (!resultTextViewText.isEmpty()) {
            BigDecimal number = new BigDecimal(resultTextViewText);
            number = number.divide(BigDecimal.valueOf(100), MathContext.DECIMAL64).stripTrailingZeros();
            resultTextView.setText(number.toPlainString());
        }
    }

    private void dotButtonClicked(@NonNull String resultTextViewText) {
        if (!resultTextViewText.contains(".") && !resultTextViewText.isEmpty()) {
            resultTextView.append(".");
        }
    }

    private void equalButtonClicked(String resultTextViewText) {
        Stack<String> numberStack = new Stack<>();

        if (!resultTextView.getText().toString().isEmpty()) {
            expression.append(resultTextViewText).append(" ");
        }

        getFinalResult(numberStack);
        BigDecimal number = new BigDecimal(numberStack.peek());
        number = number.stripTrailingZeros();
        resultTextView.setText(number.toPlainString());

        historyTextView.setText(expression.toString());
    }

    /**
     * @desc get the final result of the string mathematical expression
     */
    private void getFinalResult(Stack<String> numberStack) {
        StringBuilder number = new StringBuilder();
        String operator = "";

        for (int i = 0; i < expression.length(); i++) {
            if (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.' || expression.charAt(i) == '-') {
                number.append(expression.charAt(i));
            } else {
                numberStack.push(number.toString());
                number = new StringBuilder();
                if (!operator.isEmpty()) {
                    calculate(operator, numberStack);
                }
                operator = expression.charAt(i) + "";
            }
        }
        expression = new StringBuilder();
    }

    /**
     * @desc Calculate the result of two numbers given the operation
     */
    private void calculate(@NonNull String operator, @NonNull Stack<String> numberStack) {
        BigDecimal operand1, operand2;

        operand2 = new BigDecimal(numberStack.pop());
        operand1 = new BigDecimal(numberStack.pop());

        switch (operator) {
            case "+":
                numberStack.push((operand1.add(operand2)).toPlainString());
                break;
            case "－":
                numberStack.push((operand1.subtract(operand2)).toPlainString());
                break;
            case "÷":
                numberStack.push((operand1.divide(operand2, MathContext.DECIMAL64)).toPlainString());
                break;
            case "×":
                numberStack.push((operand1.multiply(operand2)).toPlainString());
                break;
        }
    }

}
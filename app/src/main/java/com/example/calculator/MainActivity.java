package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.*;
import android.view.View;
import android.widget.*;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private enum ButtonTypes {
        NUM, OP_BINARY, OP_UNARY, DEL, CLEAR, CALC
    }

    private double rightNum, leftNum, result;
    private int curBinOpId;
    private char curBinOpChar;
    private String rightNumString, leftNumString, resultString, curExpressionString;
    private ArrayList<Integer> btnHistory;

    private TextView displayExpression, displayResult;
    private Button btnNum1, btnNum2, btnNum3, btnNum4, btnNum5, btnNum6, btnNum7, btnNum8, btnNum9,
            btnNum0, btnOpAdd, btnOpSub, btnOpDiv, btnOpMult, btnOpSign, btnOpDec, btnOpCalc,
            btnOpClr, btnOpDel;



    private final DecimalFormat displayDecimalFormat = new DecimalFormat("0.####");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init history
        btnHistory = new ArrayList<>();

        // init views
        displayExpression = findViewById(R.id.displayExpression);
        displayResult = findViewById(R.id.displayResult);
        btnNum1 = findViewById(R.id.btnNum1);
        btnNum2 = findViewById(R.id.btnNum2);
        btnNum3 = findViewById(R.id.btnNum3);
        btnNum4 = findViewById(R.id.btnNum4);
        btnNum5 = findViewById(R.id.btnNum5);
        btnNum6 = findViewById(R.id.btnNum6);
        btnNum7 = findViewById(R.id.btnNum7);
        btnNum8 = findViewById(R.id.btnNum8);
        btnNum9 = findViewById(R.id.btnNum9);
        btnNum0 = findViewById(R.id.btnNum0);
        btnOpAdd = findViewById(R.id.btnOpAdd);
        btnOpSub = findViewById(R.id.btnOpSub);
        btnOpDiv = findViewById(R.id.btnOpDiv);
        btnOpMult = findViewById(R.id.btnOpMult);
        btnOpSign = findViewById(R.id.btnOpSign);
        btnOpDec = findViewById(R.id.btnOpDec);
        btnOpCalc = findViewById(R.id.btnOpCalc);
        btnOpClr = findViewById(R.id.btnOpClr);
        btnOpDel = findViewById(R.id.btnOpDel);


        // set listeners
        btnNum1.setOnClickListener(btnListener);
        btnNum2.setOnClickListener(btnListener);
        btnNum3.setOnClickListener(btnListener);
        btnNum4.setOnClickListener(btnListener);
        btnNum5.setOnClickListener(btnListener);
        btnNum6.setOnClickListener(btnListener);
        btnNum7.setOnClickListener(btnListener);
        btnNum8.setOnClickListener(btnListener);
        btnNum9.setOnClickListener(btnListener);
        btnNum0.setOnClickListener(btnListener);
        btnOpAdd.setOnClickListener(btnListener);
        btnOpSub.setOnClickListener(btnListener);
        btnOpDiv.setOnClickListener(btnListener);
        btnOpMult.setOnClickListener(btnListener);
        btnOpSign.setOnClickListener(btnListener);
        btnOpDec.setOnClickListener(btnListener);
        btnOpCalc.setOnClickListener(btnListener);
        btnOpClr.setOnClickListener(btnListener);
        btnOpDel.setOnClickListener(btnListener);

        // init values and displays
        setDefaultAndInitUI();

    }

    /* ==================================== SPEC. METHODS ==================================== */

    //
    private void setDefaultAndInitUI() {
        // set all values to default
        rightNum = 0;
        leftNum = 0;
        result = 0;
        rightNumString = null;
        leftNumString = null;
        curBinOpId = R.id.btnOpClr;

        // clear press history to avoid leaks
        btnHistory.clear();
        // other functions will check to see if this was the last pressed, so add it in manually
        btnHistory.add(R.id.btnOpClr);

        //initialize UI elements
        updateExpressionString();
        updateExpressionDisplay();
        updateResultNumString();
        updateResultDisplay();
    }
    /* ==================================== GETTERS ==================================== */

    private int getLastButtonPressed() {
        int btnHistoryLength = btnHistory.size();
        if (btnHistoryLength > 1) {
            return btnHistory.get(btnHistory.size() - 2);
        } else {
            return R.id.btnOpClr;
        }

    }

    public ButtonTypes getButtonType(int id) {
        if (id == R.id.btnNum1 || id == R.id.btnNum5 || id == R.id.btnNum2
                || id == R.id.btnNum3 || id == R.id.btnNum4 || id == R.id.btnNum6
                || id == R.id.btnNum7 || id == R.id.btnNum8 || id == R.id.btnNum9
                || id == R.id.btnNum0) {
            return ButtonTypes.NUM;
        } else if (id == R.id.btnOpAdd || id == R.id.btnOpSub || id == R.id.btnOpMult || id == R.id.btnOpDiv) {
            return ButtonTypes.OP_BINARY;
        } else if (id == R.id.btnOpSign || id == R.id.btnOpDec) {
            return ButtonTypes.OP_UNARY;
        } else if (id == R.id.btnOpClr)  {
            return ButtonTypes.CLEAR;
        } else if (id == R.id.btnOpDel) {
            return ButtonTypes.DEL;
        } else /* if (id == R.id.btnOpCalc) */ {
            return ButtonTypes.CALC;
        }
    }

    /* ==================================== UPDATE STRINGS ==================================== */

    private void updateResultNumString() { // tested
        resultString = displayDecimalFormat.format(Double.valueOf(result));
    }

    private void updateLeftNumString() {
        leftNumString = displayDecimalFormat.format(Double.valueOf(leftNum));
    }

    private void updateRightNumString() {
        rightNumString = displayDecimalFormat.format(Double.valueOf(rightNum));
    }

    private void updateCurBinOpChar() {
        if (curBinOpId == R.id.btnOpAdd) {
            curBinOpChar = '+';
        } else if (curBinOpId == R.id.btnOpSub) {
            curBinOpChar = '-';
        } else if (curBinOpId == R.id.btnOpMult) {
            curBinOpChar = '*';
        } else if (curBinOpId == R.id.btnOpDiv) {
            curBinOpChar = '/';
        }
    }

    private void updateExpressionString() {
        String newExpressionString = "";

        if (leftNumString != null) {
            newExpressionString += leftNumString;
        }

        if (curBinOpId != R.id.btnOpClr) {
            newExpressionString += " " + curBinOpChar;
            if (rightNumString != null) {
                newExpressionString += " " + rightNumString;
            }
        }


        curExpressionString = newExpressionString;

    }

    /* ==================================== DISPLAY CONTROL ==================================== */

    private void updateResultDisplay() {
        displayResult.setText(resultString);
    }

    private void updateExpressionDisplay() {
        displayExpression.setText(curExpressionString);
    }


    /* ==================================== EVENT LISTENERS ==================================== */

    public View.OnClickListener btnListener = new View.OnClickListener() { //todo:

        @Override
        public void onClick(View v) {

            int id = v.getId();
            btnHistory.add(id);

            ButtonTypes bType = getButtonType(id);

            switch (bType) {

                case NUM:
                    numButtonPressed(id);
                    break;
                case OP_BINARY:
                    binOpButtonPressed(id);
                    break;
                case OP_UNARY:
                    unOpButtonPressed(id);
                    break;
                case DEL:
                    delButtonPressed();
                    break;
                case CLEAR:
                    setDefaultAndInitUI();
                    setDefaultAndInitUI();
                    break;
                case CALC:
                    calcBtnPressed();
                    break;
            }

        }

        private void numButtonPressed(int id) { // TESTED

            int num;
            if (id == R.id.btnNum1) {
                num = 1;
            } else if (id == R.id.btnNum2) {
                num = 2;
            } else if (id == R.id.btnNum3) {
                num = 3;
            } else if (id == R.id.btnNum4) {
                num = 4;
            } else if (id == R.id.btnNum5) {
                num = 5;
            } else if (id == R.id.btnNum6) {
                num = 6;
            } else if (id == R.id.btnNum7) {
                num = 7;
            } else if (id == R.id.btnNum8) {
                num = 8;
            } else if (id == R.id.btnNum9) {
                num = 9;
            } else /* if (id == R.id.btnNum0) */ {
                num = 0;
            }

            int lastPressed = getLastButtonPressed();
            ButtonTypes lastPressedType = getButtonType(lastPressed);


            if (result == 0 || lastPressedType == ButtonTypes.CALC
                    || lastPressedType == ButtonTypes.OP_BINARY) {
                result = num;
                updateResultNumString();
            } else {
                resultString += Integer.toString(num);
                result = Double.parseDouble(resultString);
            }

            updateResultDisplay();
        }

        private void binOpButtonPressed(int id) { //todo: test
            curBinOpId = id;
            updateCurBinOpChar();

            int lastBtnPressed = getLastButtonPressed();
            ButtonTypes lastBtnPressedType = getButtonType(lastBtnPressed);

            if (lastBtnPressedType == ButtonTypes.CALC) {
                rightNum = 0;
                rightNumString = null;
                leftNum = result;
                updateLeftNumString();
            } else if (leftNumString == null) {
                leftNum = result;
                updateLeftNumString();
            }

            updateExpressionString();
            updateExpressionDisplay();
            updateResultNumString();
            updateResultDisplay();

            
        }


        private void unOpButtonPressed(int id) { // TESTED
            if (id == R.id.btnOpSign) {
                result = Calculator.sign(result);
                updateResultNumString();
            } else if (id == R.id.btnOpDec) {
                decButtonPressed();
            }
            updateResultDisplay();
            
        }

        private void decButtonPressed() { // test case: press decimal button more than once for same number
            if(!resultString.matches("-?\\d*[.]\\d*")) {
                resultString += ".";
            }

        }

        private void delButtonPressed() { // TESTED

            ButtonTypes lastPressedType = getButtonType( getLastButtonPressed() );
            switch (lastPressedType) {
                case NUM:
                case OP_UNARY:
                    // remove last character
                    int resultLength = resultString.length();

                    if (resultLength > 1) {
                        resultString = resultString.substring(0, (resultString.length() - 1));
                        result = Double.parseDouble(resultString);
                    } else {
                        result = 0;
                        updateResultNumString();
                    }
                    break;
                default:
                    setDefaultAndInitUI();
            }


            updateResultDisplay();
            
        }

        private void calcBtnPressed() {
            int lastPress = getLastButtonPressed();
            ButtonTypes lastPressType = getButtonType(lastPress);

            if (leftNumString == null) {
                leftNum = result;
                updateLeftNumString();
            } else if (rightNumString == null) {
                if (lastPressType != ButtonTypes.CALC) {
                    rightNum = result;
                    updateRightNumString();
                    result = performCalculation();
                }
            } else if (lastPressType == ButtonTypes.CALC) {
                leftNum = result;
                updateLeftNumString();
                result = performCalculation();
            }// end if else

            updateResultNumString();
            updateResultDisplay();
            updateExpressionString();
            updateExpressionDisplay();

        } // end method

        private double performCalculation() {
            return Calculator.calculate(leftNum, rightNum, curBinOpChar);
        }
    };


}
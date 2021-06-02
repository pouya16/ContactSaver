package com.example.contactsaver.layout;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.contactsaver.R;

public class ValueSelector extends LinearLayout implements View.OnClickListener, View.OnLongClickListener, View.OnTouchListener {

    Context context;
    View rootView;
    TextView valueText;
    View PlusBtn,MinusBtn;

    private int MIN_VALUE = 0;
    private int MAX_VALUE = 100;

    private boolean isPlusBtnPressed = false;
    private boolean isMinusBtnPressed = false;
    private static final int TIME_INTERVAL = 100;
    private Handler handler;

    public ValueSelector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        setSaveEnabled(true);
        rootView = inflate(this.context, R.layout.value_selector,this);
        valueText = rootView.findViewById(R.id.txt_value);
        PlusBtn = rootView.findViewById(R.id.btn_plus);
        MinusBtn = rootView.findViewById(R.id.btn_minus);
        
        handler = new android.os.Handler();

        PlusBtn.setOnClickListener(this);
        PlusBtn.setOnLongClickListener(this);
        PlusBtn.setOnTouchListener(this);

        MinusBtn.setOnClickListener(this);
        MinusBtn.setOnLongClickListener(this);
        MinusBtn.setOnTouchListener(this);
    }

    public int getMIN_VALUE() {
        return MIN_VALUE;
    }

    public void setMIN_VALUE(int MIN_VALUE) {
        if(MIN_VALUE < this.MAX_VALUE){
            this.MIN_VALUE = MIN_VALUE;
        }else{
            this.MIN_VALUE = 0;
        }
    }

    public int getMAX_VALUE() {
        return MAX_VALUE;
    }

    public void setMAX_VALUE(int MAX_VALUE) {
        if(MAX_VALUE > this.MIN_VALUE){
            this.MAX_VALUE = MAX_VALUE;
        }else{
            this.MAX_VALUE = 100;
        }
    }

    public void setValue(int newValue){
        if(newValue>MAX_VALUE){
            valueText.setText(String.valueOf(MAX_VALUE));
        }else if(newValue<MIN_VALUE){
            valueText.setText(String.valueOf(MIN_VALUE));
        }else{
            valueText.setText(String.valueOf(newValue));
        }
    }

    public int getValue(){
        String text = valueText.getText().toString();
        if(text.isEmpty()){
            text = "0";
            valueText.setText(text);
        }
        return Integer.valueOf(text);
    }

    @Override
    public void onClick(View v) {
        int currentValue = getValue();
        if(v.getId() == PlusBtn.getId()){
            increment(currentValue);
        }else if(v.getId() == MinusBtn.getId()){
            decrement(currentValue);
        }
    }

    private void increment(int value){
        value++;
        setValue(value);
    }

    private void decrement(int value){
        value--;
        setValue(value);
    }

    @Override
    public boolean onLongClick(View v) {
        if(v.getId() == MinusBtn.getId()){
            isMinusBtnPressed = true;
            handler.postDelayed(new AutoDecrement(),TIME_INTERVAL);
        }else if(v.getId() == PlusBtn.getId()){
            isPlusBtnPressed = true;
            handler.postDelayed(new AutoIncrement(),TIME_INTERVAL);
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP||event.getAction() == MotionEvent.ACTION_CANCEL){
            isPlusBtnPressed = false;
            isMinusBtnPressed = false;
        }
        return false;
    }

    private class AutoIncrement implements Runnable{
        @Override
        public void run() {
            if(isPlusBtnPressed){
                int value = getValue();
                increment(value);
                handler.postDelayed(this,TIME_INTERVAL);
            }
        }
    }

    private class AutoDecrement implements Runnable{
        @Override
        public void run() {
            if(isMinusBtnPressed){
                int value = getValue();
                decrement(value);
                handler.postDelayed(this,TIME_INTERVAL);
            }
        }
    }
}

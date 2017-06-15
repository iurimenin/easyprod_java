package io.github.iurimenin.easyprod.app.util;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.NumberFormat;

/**
 * Created by Iuri Menin on 15/06/17.
 */

public class MoneyMaskMaterialEditText extends MaterialEditText {

    private TextWatcher textWatcher;

    public MoneyMaskMaterialEditText(Context context) {
        super(context);
        addTextWatcher();
    }

    public MoneyMaskMaterialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        addTextWatcher();
    }

    public MoneyMaskMaterialEditText(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        addTextWatcher();
    }

    public void setTextFromDouble(Double value) {

        MoneyMaskMaterialEditText.this.setText(NumberFormat.getCurrencyInstance().format(value).replace(NumberFormat.getCurrencyInstance().getCurrency().getSymbol(), ""));
    }

    public double getDouble(){

        if (this.getText().toString() == null || this.getText().toString().length() == 0)
            return 0.0;

        String cleanString = MoneyMaskMaterialEditText.this.getText().toString().replaceAll("[,.*]", "").replace(NumberFormat.getCurrencyInstance().getCurrency().getSymbol(), "");
        return (Double.parseDouble(cleanString)/100);
    }

    private void addTextWatcher(){

        textWatcher = new TextWatcher(){

            private String current = "";

            @Override
            public void afterTextChanged(Editable arg0) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(!s.toString().equals(current)){
                    MoneyMaskMaterialEditText.this.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[,.*]", "").replace(NumberFormat.getCurrencyInstance().getCurrency().getSymbol(),"");

                    if (cleanString != null && cleanString.length() != 0){
                        double parsed = Double.parseDouble(cleanString);
                        String formatted = NumberFormat.getCurrencyInstance().format((parsed/100)).replace(NumberFormat.getCurrencyInstance().getCurrency().getSymbol(),"");
                        current = formatted;
                        MoneyMaskMaterialEditText.this.setText(formatted);
                        MoneyMaskMaterialEditText.this.setSelection(formatted.length());
                    } else {
                        MoneyMaskMaterialEditText.this.setText(null);
                    }

                    MoneyMaskMaterialEditText.this.addTextChangedListener(this);
                }
            }
        };

        this.addTextChangedListener(textWatcher);
    }
}

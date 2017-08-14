package com.tobosu.mydecorate.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tobosu.mydecorate.R;

/**
 * Created by dec on 2016/9/23.
 */
public class TipDialog1 extends Dialog {

    public TipDialog1(Context context) {
        super(context);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
    }

    public TipDialog1(Context context, int theme) {
        super(context, theme);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
    }

    public static class Builder {
        private Context context;
        private String positiveButtonText;
        private View contentView;
        private OnClickListener positiveButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
//            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
//            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setTitle(int title) {
//            this.title = (String) context.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
//            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = (String) context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }


        public TipDialog1 create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final TipDialog1 dialog = new TipDialog1(context, R.style.warn_dialog_style);
            View layout = inflater.inflate(R.layout.tip_dialog_layout1, null);
            dialog.addContentView(layout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.ok_button)).setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.ok_button)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.ok_button).setVisibility(View.GONE);
            }

            dialog.setContentView(layout);
            Window window = dialog.getWindow();
            return dialog;
        }
    }
}

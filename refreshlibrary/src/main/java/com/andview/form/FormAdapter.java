package com.andview.form;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.andview.refreshview.R;

import java.util.ArrayList;
import java.util.List;


/**
 * The adpater the holds and displays the form objects
 * Created by Adib on 16-Apr-17.
 */

public class FormAdapter extends RecyclerView.Adapter<FormAdapter.FormViewHolder> {

    // defining marker for header view
    private int IS_HEADER_VIEW = 0;

    private List<FormObject> mDataset;
    private Context mContext;
    private OnFormElementValueChangedListener mListener;

    private int clickedPosition;

    private final ForegroundColorSpan RED_SPAN = new ForegroundColorSpan(Color.RED);

    /**
     * public constructor with context
     *
     * @param context
     */
    public FormAdapter(Context context, OnFormElementValueChangedListener listener) {
        mContext = context;
        mListener = listener;
        mDataset = new ArrayList<>();
        clickedPosition = -1;
    }

    /**
     * adds list of elements to be shown
     *
     * @param formObjects
     */
    public void addElements(List<FormObject> formObjects) {
        this.mDataset = formObjects;
    }

    /**
     * adds single element to be shown
     *
     * @param formObject
     */
    public void addElement(FormObject formObject) {
        this.mDataset.add(formObject);
    }

    /**
     * set value for any unique index
     *
     * @param position
     * @param value
     */
    public void setValueAtIndex(int position, String value) {
        FormElement formElement = (FormElement) mDataset.get(position);
        formElement.setValue(value);
    }

    /**
     * set value for any unique tag
     *
     * @param tag
     * @param value
     */
    public void setValueAtTag(int tag, String value) {

        for (FormObject f : this.mDataset) {
            if (f instanceof FormElement) {
                FormElement formElement = (FormElement) f;
                if (formElement.getTag() == tag) {
                    formElement.setValue(value);
                    return;
                }
            }
        }
    }

    /**
     * get value of any element by tag
     *
     * @param index
     * @return
     */
    public FormObject getValueAtIndex(int index) {
        return (mDataset.get(index));
    }

    /**
     * get value of any element by tag
     *
     * @param tag
     * @return
     */
    public FormElement getValueAtTag(int tag) {
        for (FormObject f : this.mDataset) {
            if (f instanceof FormElement) {
                FormElement formElement = (FormElement) f;
                if (formElement.getTag() == tag) {
                    return formElement;
                }
            }
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataset.get(position).isHeader()) {
            return IS_HEADER_VIEW;
        } else {
            return ((FormElement) mDataset.get(position)).getType();
        }
    }

    @Override
    public FormAdapter.FormViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // FIXME: 17/10/18 在这里添加ViewHolder
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        FormAdapter.FormViewHolder vh;
        switch (viewType) {
            case 0:
                v = inflater.inflate(R.layout.form_element_header, parent, false);
                vh = new HeaderViewHolder(v);
                break;
            default:
                v = inflater.inflate(R.layout.form_element, parent, false);
                vh = new EditTextViewHolder(v, new FormCustomEditTextListener());
                break;
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(FormAdapter.FormViewHolder vh, final int position) {

        // updates edit text listener index
        vh.callback.updatePosition(vh.getAdapterPosition());

        // gets current object
        FormObject currentObject = mDataset.get(position);
        // FIXME: 17/10/18 根据不同类型进行绑定
        int itemType = getItemViewType(position);
        if (itemType == IS_HEADER_VIEW) {
            FormHeader formHeader = (FormHeader) currentObject;
            ((HeaderViewHolder) vh).mTextViewTitle.setText(formHeader.getTitle());
        } else if (itemType == FormElement.TYPE_EMPTY) {
            FormElement formElement = (FormElement) currentObject;
            EmptyViewHolder holder = (EmptyViewHolder) vh;
            String title = formElement.getTitle();
            if (TextUtils.isEmpty(title)) {
                holder.mTextViewTitle.setText("无数据");
            } else {
                holder.mTextViewTitle.setText(title);
            }
        } else {
            EditTextViewHolder holder = (EditTextViewHolder) vh;
            // other wise, it just displays form element with respective type
            FormElement formElement = (FormElement) currentObject;
            setupTitle(holder.mTextViewTitle, formElement);
            setupDivider(holder.mDivider, position);
            holder.mEditTextValue.setText(formElement.getValue());
            holder.mEditTextValue.setHint(formElement.getHint());
            switch (getItemViewType(position)) {
                case FormElement.TYPE_EDITTEXT_TEXT_SINGLELINE:
                    holder.mEditTextValue.setMaxLines(1);
                    setEditTextFocusEnabled(holder);
                    break;
                default:
                    break;
            }
        }

    }

    private void setupTitle(TextView tv, FormElement e) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(e.getTitle());
        if (e.isRequired()) {
           // ssb.append(" *");
           // ssb.setSpan(RED_SPAN, ssb.length() - 1, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tv.setText(ssb, TextView.BufferType.SPANNABLE);
    }

    private void setupDivider(View mDivider, int position) {
        if (position == 0) {
            mDivider.setVisibility(View.INVISIBLE);
        } else if (getItemViewType(position - 1) == IS_HEADER_VIEW) {
            mDivider.setVisibility(View.INVISIBLE);
        } else {
            mDivider.setVisibility(View.VISIBLE);
        }
    }

    /**
     * brings focus when clicked on the whole container
     *
     * @param holder
     */
    private void setEditTextFocusEnabled(final EditTextViewHolder holder) {
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mEditTextValue.requestFocus();
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(holder.mEditTextValue, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }


    public static class FormViewHolder extends RecyclerView.ViewHolder {
        public FormValueCallback callback;

        public FormViewHolder(View itemView, FormValueCallback callback) {
            super(itemView);
            this.callback = callback;
        }
    }

    /**
     * EditText的ViewHolder
     */
    public static class EditTextViewHolder extends FormViewHolder {

        public TextView mTextViewTitle;
        public TextView mTextViewOptions;
        public EditText mEditTextValue;
        public View mDivider;

        public EditTextViewHolder(View v, FormCustomEditTextListener listener) {
            super(v, listener);
            mDivider = v.findViewById(R.id.mDivider);
            mTextViewTitle = (TextView) v.findViewById(R.id.formElementTitle);
            mTextViewOptions = (TextView) v.findViewById(R.id.formElementTitle);
            mEditTextValue = (EditText) v.findViewById(R.id.formElementValue);
            if (mEditTextValue != null) {
                mEditTextValue.addTextChangedListener(listener);
            }
        }
    }


    /**
     * 头ViewHolder
     */
    public static class HeaderViewHolder extends FormViewHolder {

        public TextView mTextViewTitle;

        public HeaderViewHolder(View v) {
            super(v, new FormValueCallback() {
                @Override
                public void updatePosition(int position) {

                }
            });
            mTextViewTitle = (TextView) v.findViewById(R.id.formElementTitle);
        }
    }

    /**
     * 带辅助按键的ViewHolder
     */
    private class AssistantButtonViewHolder extends FormViewHolder {

        public View mDivider;
        public TextView mTextViewTitle;
        public ImageButton btnAssistant;
        public EditText mEditTextValue;

        public AssistantButtonViewHolder(View v, FormCustomEditTextListener listener) {
            super(v, listener);
            mDivider = (View) v.findViewById(R.id.mDivider);
            mTextViewTitle = (TextView) v.findViewById(R.id.formElementTitle);
           // btnAssistant = (ImageButton) v.findViewById(R.id.btnAssistant);
            mEditTextValue = (EditText) v.findViewById(R.id.formElementValue);
            if (mEditTextValue != null) {
                mEditTextValue.addTextChangedListener(listener);
            }
        }
    }

    /**
     * 空ViewHolder
     */
    public static class EmptyViewHolder extends FormViewHolder {

        public TextView mTextViewTitle;

        public EmptyViewHolder(View v) {
            super(v, new FormValueCallback() {
                @Override
                public void updatePosition(int position) {

                }
            });
            mTextViewTitle = (TextView) v.findViewById(R.id.formElementTitle);
        }
    }

    // FIXME: 17/10/18 添加Viewholder

    private interface FormValueCallback {
        void updatePosition(int position);
    }

    /**
     * Text watcher for Edit texts
     */
    private class FormCustomEditTextListener implements TextWatcher, FormValueCallback {
        private int position;

        @Override
        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            // get current form element, existing value and new value
            FormElement formElement = (FormElement) mDataset.get(position);
            String currentValue = formElement.getValue();
            String newValue = charSequence.toString();

            // trigger event only if the value is changed
            if (!currentValue.equals(newValue)) {
                formElement.setValue(newValue);
                if (mListener != null) {
                    mListener.onValueChanged(formElement);
                }
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private class FormCompoundButtonListener implements CompoundButton.OnCheckedChangeListener, FormValueCallback {

        private int position;

        @Override
        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.isPressed()) {
                FormElement formElement = (FormElement) mDataset.get(position);
                String newValue = isChecked ? formElement.getCheckedAndNotCheckValue().first : formElement.getCheckedAndNotCheckValue().second;
                formElement.setValue(newValue);
                if (mListener != null) {
                    mListener.onValueChanged(formElement);
                }
            }
        }

    }


}
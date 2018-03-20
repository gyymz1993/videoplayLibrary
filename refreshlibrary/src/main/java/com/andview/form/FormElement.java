package com.andview.form;

import android.util.Pair;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;


/**
 * 表格元素
 */
public class FormElement implements FormObject {

    // different types for the form elements
    public static final int TYPE_EDITTEXT_TEXT_SINGLELINE = 1;

    public static final int TYPE_EMPTY = 14;
    // private variables
    private int mTag; // unique tag to identify the object
    private int mType; // type for the form element
    private String mTitle; // title to be shown on left
    private String mValue; // value to be shown on right
    private List<String> mOptions; // list of options for single and multi picker
    private List<String> mOptionsSelected; // list of selected options for single and multi picker
    private String mHint; // value to be shown if mValue is null
    private boolean mRequired; // value to set is the field is required
    private Pair<String, String> checkedAndNotCheckValue = new Pair<>("true", "false");
    private FormAssistantCallback formAssistantCallback;
   // private int assistantButtonResId = R.drawable.fm_ic_assistant_button;

    public FormElement() {
    }

    /**
     * static method to create instance
     *
     * @return
     */
    public static FormElement createInstance() {
        return new FormElement();
    }

    public interface FormAssistantCallback {
        void onAssistantButtonClick(EditText valueEditText);
    }

    // getters and setters
    public FormElement setTag(int mTag) {
        this.mTag = mTag;
        return this;
    }

    public FormElement setType(int mType) {
        this.mType = mType;
        return this;
    }

    public FormElement setTitle(String mTitle) {
        this.mTitle = mTitle;
        return this;
    }

    public FormElement setValue(String mValue) {
        this.mValue = mValue;
        return this;
    }

    public FormElement setHint(String mHint) {
        this.mHint = mHint;
        return this;
    }

    public FormElement setRequired(boolean required) {
        this.mRequired = required;
        return this;
    }

    public FormElement setOptions(List<String> mOptions) {
        this.mOptions = mOptions;
        return this;
    }

    public FormElement setOptionsSelected(List<String> mOptionsSelected) {
        this.mOptionsSelected = mOptionsSelected;
        return this;
    }

    public FormElement setCheckedAndNotCheckValue(Pair<String, String> checkedAndNotCheckValue) {
        this.checkedAndNotCheckValue = checkedAndNotCheckValue;
        return this;
    }

//    public FormElement setFormAssistantCallback(FormAssistantCallback formAssistantCallback) {
//        this.formAssistantCallback = formAssistantCallback;
//        return this;
//    }
//
//    public FormElement setAssistantButtonResId(int assistantButtonResId) {
//        this.assistantButtonResId = assistantButtonResId;
//        return this;
//    }

    public int getTag() {
        return this.mTag;
    }

    public int getType() {
        return this.mType;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getValue() {
        return (this.mValue == null) ? "" : this.mValue;
    }

    public String getHint() {
        return (this.mHint == null) ? "" : this.mHint;
    }

    public boolean isRequired() {
        return this.mRequired;
    }

    public List<String> getOptions() {
        return (this.mOptions == null) ? new ArrayList<String>() : this.mOptions;
    }

    public List<String> getOptionsSelected() {
        return (this.mOptionsSelected == null) ? new ArrayList<String>() : this.mOptionsSelected;
    }

    public Pair<String, String> getCheckedAndNotCheckValue() {
        return checkedAndNotCheckValue;
    }

    public FormAssistantCallback getFormAssistantCallback() {
        return formAssistantCallback;
    }

//    public int getAssistantButtonResId() {
//        return assistantButtonResId;
//    }

    @Override
    public boolean isHeader() {
        return false;
    }

    @Override
    public String toString() {
        return "TAG: " + String.valueOf(this.mTag) + ", TITLE: " + this.mTitle + ", VALUE: " + this.mValue + ", REQUIRED: " + String.valueOf(
                this.mRequired);
    }

}

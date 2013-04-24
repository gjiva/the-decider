package com.argint.android.thedecider.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.argint.android.thedecider.DecisionData;
import com.argint.android.thedecider.R;

public class OptionsEntryActivity 
	extends DecisionActivity implements OnClickListener, OnTouchListener{
	
	private LinearLayout layout;
	private List<EditText> editTexts = new ArrayList<EditText>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.layout = new LinearLayout(this);
		this.layout.setOrientation(LinearLayout.VERTICAL);
		
		TextView instructions = new TextView(this);
		instructions.setText(getString(R.string.default_options_entry_message));
		instructions.setPadding(5, 5, 5, 5);
		
		TextView elaboration = new TextView(this);
		elaboration.setText(getString(R.string.options_entry_elaboration));
		elaboration.setPadding(5, 5, 5, 5);

		this.layout.addView(instructions);
		this.layout.addView(elaboration);
		
		Button doneButton = new Button(this);
		doneButton.setText(getString(R.string.options_entry_button_text));
		doneButton.setOnClickListener(this);
		this.layout.addView(doneButton);
		
		createEditText();
		createEditText();

		// Set the scroll view as the activity layout
		ScrollView scroller = new ScrollView(this);
		scroller.addView(this.layout);
		setContentView(scroller);
	}
	
	/**
	 * 
	 */
	private void createEditText(){
		
		EditText editText = new EditText(this);
		editText.setHint(getString(R.string.option_entry_hint));
		editText.setOnTouchListener(this);
		editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
		this.editTexts.add(editText);
		this.layout.addView(editText, this.layout.getChildCount()-1);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.options_entry, menu);
		return true; 
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * 
	 * @param view
	 */
	public void enterOptions(View view){
		
		HashMap<String, Float> optionMap = new HashMap<String, Float>();
		for (EditText edit:this.editTexts){
			String criterion = edit.getText().toString();
			if (!criterion.isEmpty()){
				optionMap.put(criterion,0f);
			}
		}
    	
    	// add the option list to the decision data from the previous activity's Intent, 
    	// and pass it to the next activity
    	DecisionData decisionData = super.getDecisionData();
    	decisionData.optionToGradeMap = optionMap;
    	startActivity(super.createIntent(decisionData));
	}
	
	@Override
	public void onClick(View v) {
		
		if (v instanceof Button){
			enterOptions(v);
		}		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int numEmpty = 0;
		for (EditText edit:this.editTexts){
			String option = edit.getText().toString();
			if (option.isEmpty()){
				numEmpty++;
			}
		}
		if (numEmpty < 2){
			createEditText();
		}
		return false;
	}

}

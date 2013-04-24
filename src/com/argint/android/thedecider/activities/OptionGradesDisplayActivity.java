package com.argint.android.thedecider.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.argint.android.thedecider.ActivityController;
import com.argint.android.thedecider.DecisionData;
import com.argint.android.thedecider.R;

public class OptionGradesDisplayActivity 
	extends DecisionActivity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the decision data from the intent
		DecisionData decisionData = super.getDecisionData();

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		TextView instructions = new TextView(this);
		instructions.setText(getString(R.string.option_grades_display_message));
		instructions.setPadding(5, 5, 5, 10);
		instructions.setGravity(Gravity.CENTER);
		instructions.setTypeface(Typeface.DEFAULT_BOLD);

		layout.addView(instructions);
		
		TableLayout table = new TableLayout(this);
		table.setPadding(2, 2, 2, 10);

		Iterator<String> it = decisionData.optionToGradeMap.keySet().iterator();
		List<TableRow> descendingOptions = new ArrayList<TableRow>();
		
		// go through the options
		while (it.hasNext()){
			
			TableRow row = new TableRow(this);
			row.setOrientation(LinearLayout.HORIZONTAL);
			
			// get the display data ready
			String option = it.next();
			Float grade = decisionData.optionToGradeMap.get(option);
			String percent = String.valueOf(Math.round(Float.valueOf(grade)));
			logInfo("Option: " + option + " | grade: " + percent);
			
			TextView optionLabel = new TextView(this);
			optionLabel.setText(option + ": ");
			optionLabel.setGravity(Gravity.RIGHT);
			optionLabel.setPadding(5, 5, 5, 5);
			
			TextView optionGrade = new TextView(this);
			optionGrade.setText(percent + "%");
			optionGrade.setGravity(Gravity.LEFT);
			optionGrade.setPadding(5, 5, 5, 5);
			
			// add the label, with the bar next to it
			row.addView(optionLabel);
			row.addView(optionGrade);
			row.setTag(grade);
			
			// sort the display elements in descending order
			if (descendingOptions.isEmpty()){
				descendingOptions.add(row);
			}
			else {
				boolean inserted = false;
				for (int i=0; i<descendingOptions.size(); i++){
					if (((Float) descendingOptions.get(i).getTag()) < grade){
						descendingOptions.add(i, row);
						inserted = true;
						break;
					}
				}
				if (!inserted){
					descendingOptions.add(row);
				}
			}
			
		}
		
		// add the sorted display elements
		for (TableRow option:descendingOptions){
			table.addView(option);
		}
		layout.addView(table);
		
		TextView furtherInstructions = new TextView(this);
		furtherInstructions.setText(getString(R.string.option_grades_display_further_instructions));
		furtherInstructions.setPadding(5, 5, 5, 10);
		layout.addView(furtherInstructions);
		
		Button doneButton = new Button(this);
		doneButton.setText(getString(R.string.option_grades_display_button_text));
		doneButton.setOnClickListener(this);
		layout.addView(doneButton);

		// Set the scroll view as the activity layout
		ScrollView scroller = new ScrollView(this);
		scroller.addView(layout);
		setContentView(scroller);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.option_grades_display, menu);
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

	@Override
	public void onClick(View v) {
		super.startActivity(new Intent(this, ActivityController.getNextActivity(this.getClass())));
		
	}

}

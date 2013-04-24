package com.argint.android.thedecider.activities;

import java.util.Iterator;

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

import com.argint.android.thedecider.DecisionData;
import com.argint.android.thedecider.R;

public class CriteriaWeightDisplayActivity 
	extends DecisionActivity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the decision data from the intent
		DecisionData decisionData = super.getDecisionData();

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		TextView instructions = new TextView(this);
		instructions.setText(getString(R.string.criteria_display_message));
		instructions.setPadding(5, 5, 5, 10);
		instructions.setGravity(Gravity.CENTER);
		instructions.setTypeface(Typeface.DEFAULT_BOLD);

		layout.addView(instructions);
		
		TableLayout table = new TableLayout(this);
		table.setPadding(2, 2, 2, 10);
		
		Iterator<String> it = decisionData.criterionToWeightMap.keySet().iterator();
		while (it.hasNext()){
			
			TableRow row = new TableRow(this);
			row.setOrientation(LinearLayout.HORIZONTAL);
			
			// generate the label
			String criterion = it.next();
			Float weight = decisionData.criterionToWeightMap.get(criterion);
			String percent = String.valueOf(Math.round(Float.valueOf(weight * 100)));
			
			TextView criteriaLabel = new TextView(this);
			criteriaLabel.setText(criterion + ": ");
			criteriaLabel.setGravity(Gravity.RIGHT);
			criteriaLabel.setPadding(5, 5, 5, 5);
			
			TextView criteriaWeight = new TextView(this);
			criteriaWeight.setText(percent + "%");
			criteriaWeight.setGravity(Gravity.LEFT);
			criteriaWeight.setPadding(5, 5, 5, 5);
			
			// add the label, with the bar next to it
			row.addView(criteriaLabel);
			row.addView(criteriaWeight);
			
			table.addView(row);
		}
		layout.addView(table);
		
		Button okButton = new Button(this);
		okButton.setText("OK");
		okButton.setOnClickListener(this);
		layout.addView(okButton);

		// Set the scroll view as the activity layout
		ScrollView scroller = new ScrollView(this);
		scroller.addView(layout);
		setContentView(scroller);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.criteria_weight_display, menu);
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
		startActivity(super.createIntent());
		
	}

}

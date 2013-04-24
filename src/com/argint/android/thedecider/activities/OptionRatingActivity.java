package com.argint.android.thedecider.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.argint.android.thedecider.DecisionData;
import com.argint.android.thedecider.Matrix;
import com.argint.android.thedecider.R;

public class OptionRatingActivity 
	extends DecisionActivity
	implements OnClickListener, OnRatingBarChangeListener{
	
	private static final float DEFAULT_RATING = 0f;
	
	private Matrix matrix;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the decision data from the intent
		DecisionData decisionData = super.getDecisionData();
		this.matrix = new Matrix(decisionData.optionToGradeMap.keySet(),
				decisionData.criterionToWeightMap.keySet());

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		TextView instructions = new TextView(this);
		instructions.setText(getString(R.string.option_rating_message));
		instructions.setPadding(5, 5, 5, 5);

		layout.addView(instructions);

		// generate the rating bars
		Iterator<String> it = decisionData.optionToGradeMap.keySet().iterator();
		while (it.hasNext()){
			String option = it.next();
			
			TextView label = new TextView(this);
			label.setText(option);
			label.setGravity(Gravity.CENTER);
			label.setTextSize(30);
			label.setTypeface(Typeface.DEFAULT_BOLD);
			label.setPadding(5, 5, 5, 5);
			layout.addView(label);
			
			List<LinearLayout> bars = generateRatingBars(option,decisionData.criterionToWeightMap.keySet());
			for (LinearLayout bar:bars){
				layout.addView(bar);
			}
		}

		Button doneButton = new Button(this);
		doneButton.setText("Done");
		doneButton.setOnClickListener(this);
		layout.addView(doneButton);

		// Set the scroll view as the activity layout
		ScrollView scroller = new ScrollView(this);
		scroller.addView(layout);
		setContentView(scroller);
	}
	
	/**
	 * 
	 * @param option
	 * @param criteria
	 * @return
	 */
	private List<LinearLayout> generateRatingBars(String option, Set<String> criteria){
		
		List<LinearLayout> bars = new ArrayList<LinearLayout>();
		
		Iterator<String> it = criteria.iterator();
		while (it.hasNext()){
			
			LinearLayout row = new LinearLayout(this);
			row.setOrientation(LinearLayout.VERTICAL);
			
			// generate the label
			String criterion = it.next();
			TextView criteriaLabel = new TextView(this);
			criteriaLabel.setText(criterion + "");
			criteriaLabel.setGravity(Gravity.LEFT);
			criteriaLabel.setTypeface(Typeface.DEFAULT_BOLD);
			criteriaLabel.setPadding(5, 1, 5, 1);
			
			// add the label, with the bar next to it
			row.addView(criteriaLabel);
			row.addView(createRatingBar(option,criterion));
			
			bars.add(row);
		}
		
		return bars;
	}
	
	/**
	 * 
	 * @param option
	 * @param criterion
	 * @return
	 */
	private RatingBar createRatingBar(String option, String criterion){
		
		RatingBar bar = new RatingBar(this);
		bar.setTag(super.encodeStringPair(option, criterion));
		bar.setNumStars(5);
		bar.setStepSize(0.5f);
		bar.setRating(DEFAULT_RATING);
		bar.setPadding(20, 2, 5, 5);
		bar.setOnRatingBarChangeListener(this);
		// also populate the matrix
		this.matrix.setValue(option, criterion, DEFAULT_RATING);
		
		return bar;
	}
	
	/**
	 * 
	 */
	private void calculateGrades(){
		
		DecisionData dd = super.getDecisionData();
		Iterator<String> optIt = dd.optionToGradeMap.keySet().iterator();
		while (optIt.hasNext()){
			String option = optIt.next();
			float grade = 0;
			
			Iterator<String> crIt = dd.criterionToWeightMap.keySet().iterator();
			while (crIt.hasNext()){
				
				String criterion = crIt.next();
				float weight = dd.criterionToWeightMap.get(criterion);
				float rating = this.matrix.getValue(option, criterion) * 20; // 5 stars = 100%
				
				grade += rating * weight;
			}
			dd.optionToGradeMap.put(option, grade);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.option_rating, menu);
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
	public void onRatingChanged(RatingBar bar, float rating, boolean fromUser) {
		
		String[] labels = super.decodeStringPair((String) bar.getTag());
		String rowLabel = labels[0];
		String colLabel = labels[1];
		this.matrix.setValue(rowLabel, colLabel, rating);
		Log.i(this.getClass().getName(), this.matrix.toString());
		
	}

	@Override
	public void onClick(View v) {
		calculateGrades();
		startActivity(super.createIntent());
		
	}

}

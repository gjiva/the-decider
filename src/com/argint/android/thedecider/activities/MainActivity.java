package com.argint.android.thedecider.activities;

import com.argint.android.thedecider.R;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends DecisionActivity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /**
     * 
     * @param view
     */
    public void enterDecisionName(View view){
    	
    	EditText decisionNameTextField = (EditText) findViewById(R.id.decision_name);
    	String decisionName = decisionNameTextField.getText().toString();
    	startActivity(super.createIntent(decisionName));
    }
    
}

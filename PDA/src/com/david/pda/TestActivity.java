package com.david.pda;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.david.pda.adapter.MainAffairPlanAdapter;
import com.david.pda.sqlite.model.Plan;

public class TestActivity extends Activity {
	private Button btnShowCity = null;  
	   private Spinner spCityStatic = null;  
	    private Spinner spCity = null;  
    private ArrayAdapter<CharSequence> adapterCity = null;  
	    private static String[] cityInfo={"北京","江苏","浙江","上海"};  
	    @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.main_affair_plan);  
	        ListView plan_listview = (ListView) findViewById(R.id.main_affair_plan_listview);
	        MainAffairPlanAdapter planadapter = new MainAffairPlanAdapter(this,getItems()); 
	        plan_listview.setAdapter(planadapter);

	    }  
	  
	public List<Plan> getItems(){
		List<Plan> list = new ArrayList<Plan>();
		Plan plan = null;
		for(int i=0;i<5;i++){
			plan = new Plan();
			plan.setTitle("title"+(i+1));
			plan.setCreateTime(2222l);
			list.add(plan);
		}
		return list;
	}   

}

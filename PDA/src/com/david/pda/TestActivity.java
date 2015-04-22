package com.david.pda;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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
//	        //按钮相关  
//	       btnShowCity = (Button) super.findViewById(R.id.btn);  
//       btnShowCity.setOnClickListener(new OnClickListenerImpl());  
//	       //静态实现的下拉框，数据写在cityInfo.xml文件中  
//	       this.spCityStatic = (Spinner) super  
//	                .findViewById(R.id.spinner1);  
//	        this.spCityStatic  
//               .setOnItemSelectedListener(new OnItemSelectedListenerImpl());  
//	       //动态实现的下拉框，数据在程序中获得，实际项目可能来自数据库等  
//	       this.spCity = (Spinner) super.findViewById(R.id.spinner2);  
//	       this.adapterCity = new ArrayAdapter<CharSequence>(this,  
//	                android.R.layout.simple_spinner_dropdown_item, cityInfo);  
//	       this.spCity.setAdapter(adapterCity);  
//	      this.spCity.setOnItemSelectedListener(new OnItemSelectedListenerImpl());  
	    }  
	    //按钮点击事件  
	    private class OnClickListenerImpl implements OnClickListener {  
	        @Override  
	       public void onClick(View v) {  
	           String cityStatic = spCityStatic.getSelectedItem().toString();  
	           String city = spCity.getSelectedItem().toString();  
	           String selectInfo = "第一个选择的城市是：" + city + "，第二个选择的城市是："  
	                   + cityStatic;  
	           Toast.makeText(TestActivity.this, selectInfo, Toast.LENGTH_LONG)  
	                 .show();  
	       }  
	 
   }  
	   //下拉框选择事件  
	   private class OnItemSelectedListenerImpl implements OnItemSelectedListener {  
	       @Override  
	      public void onItemSelected(AdapterView<?> parent, View view,  
	               int position, long id) {  
	          String city = parent.getItemAtPosition(position).toString();  
	          Toast.makeText(TestActivity.this, "选择的城市是：" + city,  
	                    Toast.LENGTH_LONG).show();  
	       }  
	 
	        @Override  
       public void onNothingSelected(AdapterView<?> parent) {  
	          // TODO Auto-generated method stub  
	       }  
	  
	    }  


}

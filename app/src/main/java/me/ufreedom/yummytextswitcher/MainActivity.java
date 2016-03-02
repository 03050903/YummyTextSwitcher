package me.ufreedom.yummytextswitcher;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ufreedom.YummyTextSwitcher;

public class MainActivity extends AppCompatActivity {

    YummyTextSwitcher yummyTextSwitcher;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     /*   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        
       yummyTextSwitcher= (YummyTextSwitcher) findViewById(R.id.yummyTextSwitcher);
        yummyTextSwitcher.setTypeface(Typeface.createFromAsset(getAssets(), "HelveticaNeueLTPro.otf"));

    }

    public void startAnim(View view ){
        yummyTextSwitcher.startAnim();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package admin.purple.metrobuspanamasaldo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Thread timer;
    TextView textView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView3 = findViewById(R.id.textView3);
        textView3.setText("Version "+ BuildConfig.VERSION_NAME);

        timer = new Thread(){
            @Override
            public void run() {
                try {
                    synchronized (this){
                        wait(3000);
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                } finally {

                    getSharedPref();


                }
            }
        };
        timer.start();

    }

    private void getSharedPref() {
        try {
            SharedPreferences prefs = getSharedPreferences("FIRST_OPEN_SHARED_PREF", MODE_PRIVATE);
            String name = prefs.getString("name", "ElPana ;)");
            boolean firstOpen = prefs.getBoolean("firstOpen", false);

            if(!firstOpen){
                Intent intent = new Intent(MainActivity.this, YourName.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);
                finish();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
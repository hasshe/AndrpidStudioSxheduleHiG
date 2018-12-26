package Navigation;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.barankazan.kronoxapp.R;
import com.example.barankazan.kronoxapp.ScheduleActivity;

public class LoadingScreen extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run() {
                goToSchedule();
            }
        }, 2500);


    }
    public void goToSchedule() {
        Intent intent = new Intent(LoadingScreen.this, ScheduleActivity.class);
        startActivity(intent);
        this.finish();
    }
}

package Navigation;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.barankazan.kronoxapp.R;
import com.example.barankazan.kronoxapp.ScheduleActivity;

public class LoadingScreen extends AppCompatActivity {

    /**
     * Metoden körs när activity blir framkallad, det sätter upp layout för loading, skapar en ny instans
     * av Handler och ställer in timer innan nästa metoden körs.
     * @param savedInstanceState
     */
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

    /**
     * Metoden skapar ny instans av Intent så att nästa activity kan köras.
     */
    public void goToSchedule() {
        Intent intent = new Intent(LoadingScreen.this, ScheduleActivity.class);
        startActivity(intent);
        this.finish();
    }
}

package NavigationAndView;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.barankazan.kronoxapp.R;

/**
 * Laddningsskärm som anropas vid öppning av ett schema
 */
public class LoadingScreen extends AppCompatActivity {
    private Intent intent;
    private Handler handler;
    /**
     * Anropas när ett schema väljs. Laddningsskärmen påbörjar och skapar en delay på 1.5 sekunder
     * för att tillåta schemat att laddas färdigt
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);
        handler = new Handler();

        handler.postDelayed(new Runnable()
        {
            @Override
            public void run() {
                intent = new Intent(LoadingScreen.this, ScheduleActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}

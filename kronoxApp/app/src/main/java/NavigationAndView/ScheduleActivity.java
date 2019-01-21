package NavigationAndView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.barankazan.kronoxapp.R;

/**
 * Aktivitet som endast består av schemats utseende
 */
public class ScheduleActivity extends AppCompatActivity {

    /**
     * Körs när activity blir andropad
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
    }
    public void toaster() {
        Toast.makeText(ScheduleActivity.this, "No Schedule Found", Toast.LENGTH_LONG).show();

    }
}

package NavigationAndView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.barankazan.kronoxapp.R;

/**
 * Aktivitet som endast består av schemats utseende
 */
public class ScheduleActivity extends Activity {

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

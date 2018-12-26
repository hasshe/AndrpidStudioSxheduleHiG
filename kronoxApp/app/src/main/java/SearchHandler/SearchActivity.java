package SearchHandler;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.barankazan.kronoxapp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import Navigation.LoadingScreen;

public class SearchActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 1;
    private String[] mPermission = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ListView suggestionsList;
    private EditText searchText;
    private Handler guiThread;
    private ExecutorService suggestionThread;
    private Runnable updateTask;
    private Future<?> suggestionPending;
    private List<String> items;
    private ArrayAdapter<String> adapter;
    private DownloadManager downloadManager;
    private SearchSuggestions suggestions;

    private String startDate = "idag";
    private String programCode = "";

    private RadioButton progToggle, teachToggle;
    private RadioGroup grpRadio;
    public int toggle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
        setContentView(R.layout.activity_search);

        initThreading();
        findViews();
        setListeners();
        setAdapters();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length == 4 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[3] == PackageManager.PERMISSION_GRANTED) {
            } else {

            }
        }
    }
    private void requestPermissions() {
            if (ActivityCompat.checkSelfPermission(this, mPermission[0])
                    != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[1])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[2])
                            != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[3])
                            != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        mPermission, REQUEST_CODE_PERMISSION);
            } else {

        }
    }

    public String generateURL() {
        if(toggle == 1) {
            String scheduleURL = "http://schema.hig.se/setup/jsp/SchemaICAL.ics?startDatum=";
            scheduleURL += startDate + "&intervallTyp=m&intervallAntal=6&sprak=SV&sokMedAND=true&forklaringar=true&resurser=p." + programCode;
            return scheduleURL;
        }
        else if(toggle == 2) {
            String scheduleURL = "http://schema.hig.se/setup/jsp/SchemaICAL.ics?startDatum=";
            scheduleURL += startDate + "&intervallTyp=m&intervallAntal=6&sprak=SV&sokMedAND=true&forklaringar=true&resurser=s." + programCode;
            return scheduleURL;
        }
        else if(toggle == 3) {
            String scheduleURL = "http://schema.hig.se/setup/jsp/SchemaICAL.ics?startDatum=";
            scheduleURL += startDate + "&intervallTyp=m&intervallAntal=6&sprak=SV&sokMedAND=true&forklaringar=true&resurser=k." + programCode;
            return scheduleURL;
        }
         return null;
    }

    public void onRadioClick(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.programToggle:
                if (checked) {
                    toggle = 1;
                    break;
                }
            case R.id.teacherToggle:
                if (checked) {
                    toggle = 2;
                    break;
                }
            case R.id.courseToggle:
                if (checked) {
                    toggle = 3;
                    break;
                }
        }
    }

    public void downloadSchedule() {
        Uri calURI = Uri.parse(generateURL());
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        File file = new File(path + "/temp/SC1444.ics");
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        file.delete();
        DownloadManager.Request request = new DownloadManager.Request(calURI);
        request.setTitle("SC1444");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "temp/SC1444.ics");

        downloadManager.enqueue(request);
    }

    @Override
    protected void onDestroy() {
        suggestionThread.shutdownNow();
        super.onDestroy();
    }

    private void findViews() {
        searchText = findViewById(R.id.search);
        suggestionsList = findViewById(R.id.suggestion_list);
    }

    private void setListeners() {
        TextWatcher textWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                queueUpdate(1000);
            }
            public void afterTextChanged(Editable s) {

            }
        };

        searchText.addTextChangedListener(textWatcher);

        AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String query = (String) parent.getItemAtPosition(position);
                doSchedule(query);
            }
        };

        suggestionsList.setOnItemClickListener(clickListener);
    }

    private void doSchedule(String query) {
        Intent intent = new Intent(SearchActivity.this, LoadingScreen.class);
        int semicolonCounter = 0;
        for(int z = 0; z < query.length(); z++) {
            if(query.charAt(z) == ':' && semicolonCounter != 1) {
                programCode = query.substring(0, z);
                semicolonCounter++;
            }
        }

        downloadSchedule();
        startActivity(intent);
    }

    private void setAdapters() {
        items = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        suggestionsList.setAdapter(adapter);
    }

    private void initThreading() {
        guiThread = new Handler();
        suggestionThread = Executors.newSingleThreadExecutor();

        updateTask = new Runnable() {
            public void run() {
                String original = searchText.getText().toString().trim();

                if (suggestionPending != null)
                    suggestionPending.cancel(true);

                if (original.length() != 0) {
                    setText(R.string.loading_text);
                    SearchSuggestions suggestTask = new SearchSuggestions(SearchActivity.this, original);
                    suggestionPending = suggestionThread.submit(suggestTask);


                }
            }
        };
    }

    private void queueUpdate(long delayMillis) {
        guiThread.removeCallbacks(updateTask);
        guiThread.postDelayed(updateTask, delayMillis);
    }

    public void setSuggestions(List<String> suggestions) {
        guiSetList(suggestionsList, suggestions);
    }

    private void guiSetList(final ListView view,
                            final List<String> list) {
        guiThread.post(new Runnable() {
            public void run() {
                setList(list);
            }

        });
    }

    private void setText(int id) {
        adapter.clear();
        adapter.add(getResources().getString(id));
    }

    private void setList(List<String> list) {
        adapter.clear();
        adapter.addAll(list);
    }
}

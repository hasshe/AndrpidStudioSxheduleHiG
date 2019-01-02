package SearchAndSuggest;

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

import com.example.barankazan.kronoxapp.Database.DatabaseFragment;
import com.example.barankazan.kronoxapp.LoadingScreen;
import com.example.barankazan.kronoxapp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SearchActivity extends AppCompatActivity {
    private static int permission_request_code = 1;
    private String[] mPermission = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ListView listOfSuggestions;
    private EditText searchText;
    private Handler guiThread;
    private ExecutorService suggestionThread;
    private Runnable updater;
    private Future<?> suggestionPending;
    private List<String> listData;
    private ArrayAdapter<String> adapter;
    private DownloadManager downloadManager;
    private String startDate = "idag";
    private String searchCode = "";

    public int toggle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
        setContentView(R.layout.activity_search);

        initiateThread();
        findViews();
        setListeners();
        setAdapters();
    }

    /**
     *
     * @param requestCode om tillåtelse finns ( 1 eller 0)
     * @param permissions vilka tillåtelser finns
     * @param grantResults resultat av tillåtelserna
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == permission_request_code) {
            if (grantResults.length == 4 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[3] == PackageManager.PERMISSION_GRANTED) {
            } else {

            }
        }
    }

    /**
     * Frågar efter tillåtelse
     */
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
                        mPermission, permission_request_code);
            } else {

        }
    }

    /**
     * genererar den URL som behövs för sökning av ett schema
     * @return null
     */
    public String generateURL() {
        if(toggle == 1) {
            String scheduleURL = "http://schema.hig.se/setup/jsp/SchemaICAL.ics?startDatum=";
            scheduleURL += startDate + "&intervallTyp=m&intervallAntal=6&sprak=SV&sokMedAND=true&forklaringar=true&resurser=p." + searchCode;
            DatabaseFragment.addItem(searchCode, scheduleURL);
            return scheduleURL;
        }
        else if(toggle == 2) {
            String scheduleURL = "http://schema.hig.se/setup/jsp/SchemaICAL.ics?startDatum=";
            scheduleURL += startDate + "&intervallTyp=m&intervallAntal=6&sprak=SV&sokMedAND=true&forklaringar=true&resurser=s." + searchCode;
            DatabaseFragment.addItem(searchCode, scheduleURL);
            return scheduleURL;
        }
        else if(toggle == 3) {
            String scheduleURL = "http://schema.hig.se/setup/jsp/SchemaICAL.ics?startDatum=";
            scheduleURL += startDate + "&intervallTyp=m&intervallAntal=6&sprak=SV&sokMedAND=true&forklaringar=true&resurser=k." + searchCode;
            DatabaseFragment.addItem(searchCode, scheduleURL);
            return scheduleURL;
        }
         return null;
    }

    /**
     *
     * @param view vilken radioknapp som är intryckt
     */
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

    /**
     * Temporärt laddar ned det valda schemat för tolkning
     */
    public void downloadSchedule() {
        Uri calURI = Uri.parse(generateURL());
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        File file = new File(path + "/temp/ICFile.ics");
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        file.delete();
        DownloadManager.Request request = new DownloadManager.Request(calURI);
        request.setTitle("ICFile");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "temp/ICFile.ics");

        downloadManager.enqueue(request);
    }

    /**
     * lista som består av alternativen
     */
    private void findViews() {
        searchText = findViewById(R.id.search);
        listOfSuggestions = findViewById(R.id.suggestion_list);
    }

    /**
     * Reagerar på ändringar i sökfältet. Uppdateringen sker varje sekund vid ändring av textfältet
     */
    private void setListeners() {
        TextWatcher textWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
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
                goToSchedule(query);
            }
        };
        listOfSuggestions.setOnItemClickListener(clickListener);
    }
    /**
     *
     * @param query öppnar laddningsskärmen
     */
    private void goToSchedule(String query) {
        Intent intent = new Intent(SearchActivity.this, LoadingScreen.class);
        int semicolonCounter = 0;
        for(int z = 0; z < query.length(); z++) {
            if(query.charAt(z) == ':' && semicolonCounter != 1) {
                searchCode = query.substring(0, z);
                semicolonCounter++;
            }
        }
        downloadSchedule();
        startActivity(intent);
    }

    /**
     * Lägger till adapters till listorna
     */
    private void setAdapters() {
        listData = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listOfSuggestions.setAdapter(adapter);
    }

    /**
     * initierar sökningen och redovisning av resultatet
     */
    private void initiateThread() {
        guiThread = new Handler();
        suggestionThread = Executors.newSingleThreadExecutor();

        updater = new Runnable() {
            public void run() {
                String search = searchText.getText().toString().trim();

                if (search.length() != 0) {

                    SearchSuggestions suggestTask = new SearchSuggestions(SearchActivity.this, search);
                    suggestionPending = suggestionThread.submit(suggestTask);
                }
            }
        };
    }

    /**
     *
     * @param delay tid i millisekunder för att skapa en delay som tillåter applikationen att utföra färdigt exekveringar
     */
    private void queueUpdate(long delay) {
        guiThread.removeCallbacks(updater);
        guiThread.postDelayed(updater, delay);
    }

    /**
     *
     * @param suggestions lägger till resultat i listan som skrivs ut i applikationen
     */
    public void setSuggestions(final List<String> suggestions) {
        guiThread.post(new Runnable() {
            public void run() {
                setList(suggestions);
            }
        });
    }
    /**
     *
     * @param list lägger till data till listan
     */
    private void setList(List<String> list) {
        adapter.clear();
        adapter.addAll(list);
    }
}

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
import com.example.barankazan.kronoxapp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import NavigationAndView.LoadingScreen;

/**
 * Denna klass hanterar generering av URL för sökning av ett schema samt det temporära sparandet av
 * schema-filen för tolkning
 */
public class SearchActivity extends AppCompatActivity {

    private String path;
    private File file;
    private Handler guiThreadingHandler;
    private ExecutorService suggestionThreading;
    private Runnable updater;
    private DownloadManager downloadManager;
    private SearchSuggestions suggestTask;
    private Intent loadingIntent;

    private Uri uri;
    private ArrayList<String> listData;
    private ArrayAdapter<String> adapter;
    private String[] mPermission = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private String startDate = "idag";
    private String searchCode = "";

    private String scheduleURL;
    private ListView listOfSuggestions;
    private EditText searchText;
    public int toggle = 0;

    /**
     * Metod som anropas först vid instansiering/anrop av klassen
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
        setContentView(R.layout.activity_search);

        initiateThread();
        setListeners();
        setAdapters();
    }

    /**
     *Ger tillåtelse till olika kännsliga funktioner
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
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
     * Hanterar tillåtelser
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
                        mPermission, 1);
            } else {

        }
    }

    /**
     * genererar den URL som behövs för sökning av ett schema
     * @return null
     */
    public String generateURL() {
        if(toggle == 1) {
            scheduleURL = "http://schema.hig.se/setup/jsp/SchemaICAL.ics?startDatum=";
            scheduleURL += startDate + "&intervallTyp=m&intervallAntal=6&sprak=SV&sokMedAND=true&forklaringar=true&resurser=p." + searchCode;
            DatabaseFragment.addItem(searchCode, scheduleURL);
            return scheduleURL;
        }
        else if(toggle == 2) {
            scheduleURL = "http://schema.hig.se/setup/jsp/SchemaICAL.ics?startDatum=";
            scheduleURL += startDate + "&intervallTyp=m&intervallAntal=6&sprak=SV&sokMedAND=true&forklaringar=true&resurser=s." + searchCode;
            DatabaseFragment.addItem(searchCode, scheduleURL);
            return scheduleURL;
        }
        else if(toggle == 3) {
            scheduleURL = "http://schema.hig.se/setup/jsp/SchemaICAL.ics?startDatum=";
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
        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        uri = Uri.parse(generateURL());
        file = new File(path + "/temp/ICFile.ics");
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        file.delete();
        DownloadManager.Request downloadRequest = new DownloadManager.Request(uri);
        downloadRequest.setTitle("ICFile");
        downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        downloadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "temp/ICFile.ics");
        downloadManager.enqueue(downloadRequest);
    }


    /**
     * Reagerar på ändringar i sökfältet. Uppdateringen sker varje 400ms vid ändring av textfältet
     */
    private void setListeners() {
        TextWatcher textWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                guiThreadingHandler.postDelayed(updater,400);
            }
            public void afterTextChanged(Editable s) {
            }
        };
        searchText.addTextChangedListener(textWatcher);

        AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String suggestFieldItem = (String) parent.getItemAtPosition(position);
                loadingIntent = new Intent(SearchActivity.this, LoadingScreen.class);
                for(int z = 0; z < suggestFieldItem.length(); z++) {
                    if(suggestFieldItem.charAt(z) == ':') {
                        searchCode = suggestFieldItem.substring(0, z);
                        break;
                    }
                }
                downloadSchedule();
                startActivity(loadingIntent);
            }
        };
        listOfSuggestions.setOnItemClickListener(clickListener);
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
     * initierar trådningen för sökningen och redovisning av resultatet
     */
    private void initiateThread() {
        guiThreadingHandler = new Handler();
        suggestionThreading = Executors.newSingleThreadExecutor();

        searchText = findViewById(R.id.searchHint);
        listOfSuggestions = findViewById(R.id.suggestions);

        updater = new Runnable() {
            @Override
            public void run() {
                String searchFieldText = searchText.getText().toString().trim();

                if (searchFieldText.length() > 0) {
                    suggestTask = new SearchSuggestions(SearchActivity.this, searchFieldText);
                    suggestionThreading.submit(suggestTask);
                }
            }
        };
    }
    /**
     *
     * @param suggestions lägger till resultat i listan
     */
    public void setSuggestions(final ArrayList<String> suggestions) {
        guiThreadingHandler.post(new Runnable() {
            @Override
            public void run() {
                adapter.clear();
                adapter.addAll(suggestions);
            }
        });
    }
}

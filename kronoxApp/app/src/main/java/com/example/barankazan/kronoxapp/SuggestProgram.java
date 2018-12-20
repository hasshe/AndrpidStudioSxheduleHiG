package com.example.barankazan.kronoxapp;

import android.text.Html;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SuggestProgram implements Runnable {
    private static final String TAG = "SuggestProgram";
    private final SearchActivity suggest;
    private final String searchBarData;
    private static final String SIGN_TYP = "signatur";
    private static final String PROGRAM_TYP = "program";


    SuggestProgram(SearchActivity context, String searchBarData) {
        this.suggest = context;
        this.searchBarData = searchBarData;
    }

    @Override
    public void run() {
        List<String> suggestions = doSuggest(searchBarData, null);
        suggest.setSuggestions(suggestions);
    }

    /**
     * Här läser appen in kronox JSON-kod och hämtar därifrån namn på alla program.
     * @param original
     * @return
     */
    private List<String> doSuggest(String original, String typ) {
        int is = 2;
        if(is == 1) {
            typ = SIGN_TYP;
        }
        else {
            typ = PROGRAM_TYP;
        }
        List<String> messages = new LinkedList<String>();
        String error = null;

        Log.d(TAG, "doSuggest(" + original + ")");

        try {
            if (Thread.interrupted())
                throw new InterruptedException();

            String q = URLEncoder.encode(original, "UTF-8");
            URL url = new URL(
                    "https://kronox.hig.se/ajax/ajax_autocompleteResurser.jsp?typ=" + typ + "&term="
                            + q);
            InputStream in = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String s, r = "";

            while((s = br.readLine()) != null) {
                r += s;
            }

            ArrayList<JSONObject> ls = new ArrayList<>();

            JSONArray a;
                    a = new JSONArray(r);
            for(int i = 0; i < a.length(); i++) {
                ls.add(a.getJSONObject(i));
            }

            for(JSONObject o : ls) {
                String courseName = o.optString("value") + ": " + cleanCourseName( o.optString("label"));
                messages.add(courseName);
            }

            if (Thread.interrupted())
                throw new InterruptedException();


            if (Thread.interrupted())
                throw new InterruptedException();

        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
            error = suggest.getResources().getString(R.string.error_text)
                    + " " + e.toString();
        } catch (InterruptedException e) {
            Log.d(TAG, "InterruptedException", e);
            error = suggest.getResources().getString(
                    R.string.interrupted_text);
        } catch(JSONException e) {
            Log.e(TAG, "JSONException", e);
            error = "Error" + " " + e.toString();
        }
        finally {

        }

        if (error != null) {
            messages.clear();
            messages.add(error);
        }

        if (messages.size() == 0) {
            messages.add(suggest.getResources().getString(
                    R.string.no_results));
        }

        Log.d(TAG, "   -> returned " + messages);
        return messages;
    }

    public String cleanCourseName(String s) {
        s = Html.fromHtml(s).toString();
        String[] parts = s.split(",");
        if(parts.length > 1) {
            return parts[1].trim();
        } else {
            return s;
        }
    }
}

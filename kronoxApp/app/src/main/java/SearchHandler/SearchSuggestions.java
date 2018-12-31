package SearchHandler;

import android.text.Html;

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

public class SearchSuggestions implements Runnable {
    private SearchActivity suggest;
    private String searchFieldInput;

    /**
     *
     * @param suggest alternativen som framställs ska vara de nuvarande alterantiv som hittats
     * @param searchFieldInput data som matas in skall vara det nuvarande inmatade data från sökfältet
     */
    SearchSuggestions(SearchActivity suggest, String searchFieldInput) {
        this.suggest = suggest;
        this.searchFieldInput = searchFieldInput;
    }

    /**
     * Kör igång koden för tolkning av data för schemat
     */
    @Override
    public void run() {
        List<String> suggestions = null;
        try {
            suggestions = suggestionsList(searchFieldInput);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        suggest.setSuggestions(suggestions);
    }

    /**
     * Här läses kronox JSON-kod och hämtar namn på resultat
     * @param searchFieldInput data som matades in i sökfältet
     * @return data som hittades
     */
    private List<String> suggestionsList(String searchFieldInput) throws JSONException, IOException {

        List<String> messages = new LinkedList<>();


            String inputDataSearch = URLEncoder.encode(searchFieldInput, "UTF-8");
            if(suggest.toggle == 1) {
                URL url = new URL(
                        "https://kronox.hig.se/ajax/ajax_autocompleteResurser.jsp?typ=program&term="
                                + inputDataSearch);
                InputStream input = url.openStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(input));
                String bufferLine, bufferData = "";

                while((bufferLine = buffer.readLine()) != null) {
                    bufferData += bufferLine;
                }

                ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(bufferData);;

                for(int i = 0; i < jsonArray.length(); i++) {
                    jsonObjectArrayList.add(jsonArray.getJSONObject(i));
                }

                for(JSONObject object : jsonObjectArrayList) {
                    String courseName = object.getString("value") + ": \n" + courseName( object.getString("label"));
                    messages.add(courseName);
                }
                return messages;
            }else if(suggest.toggle == 2) {
                URL url = new URL(
                        "https://kronox.hig.se/ajax/ajax_autocompleteResurser.jsp?typ=signatur&term="
                                + inputDataSearch);
                InputStream input = url.openStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(input));
                String bufferLine, bufferData = "";

                while((bufferLine = buffer.readLine()) != null) {
                    bufferData += bufferLine;
                }

                ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(bufferData);;

                for(int i = 0; i < jsonArray.length(); i++) {
                    jsonObjectArrayList.add(jsonArray.getJSONObject(i));
                }

                for(JSONObject object : jsonObjectArrayList) {
                    String courseName = object.getString("value") + ": \n" + courseName( object.getString("label"));
                    messages.add(courseName);
                }
                return messages;
            }else if(suggest.toggle == 3) {
                URL url = new URL(
                        "https://kronox.hig.se/ajax/ajax_autocompleteResurser.jsp?typ=kurs&term="
                                + inputDataSearch);
                InputStream input = url.openStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(input));
                String bufferLine, bufferData = "";

                while((bufferLine = buffer.readLine()) != null) {
                    bufferData += bufferLine;
                }

                ArrayList<JSONObject> jsonObjectArrayList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(bufferData);;

                for(int i = 0; i < jsonArray.length(); i++) {
                    jsonObjectArrayList.add(jsonArray.getJSONObject(i));
                }

                for(JSONObject object : jsonObjectArrayList) {
                    String courseName = object.getString("value") + ": \n" + courseName( object.getString("label"));
                    messages.add(courseName);
                }
                return messages;
            }

        return messages;
    }

    /**
     *
     * @param courseName kursens namn från HTML format
     * @return kursens namn
     */
    public String courseName(String courseName) {
        courseName = Html.fromHtml(courseName).toString();
        String[] courseNameHTMLData = courseName.split(",");
        if(courseNameHTMLData.length > 1) {
            return courseNameHTMLData[1].trim();
        } else {
            return courseName;
        }
    }
}

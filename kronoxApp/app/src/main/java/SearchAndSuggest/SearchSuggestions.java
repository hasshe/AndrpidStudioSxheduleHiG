package SearchAndSuggest;

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

/**
 * Klass som hanterar sökfältets input via kronox Ajax tjänst
 */
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
     * Startar igång autofyll funktionerna
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
     * @return data som hittades som matchar sökfältet
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
                    String programName = object.getString("value") + ": \n" + programName(object.getString("label"));
                    messages.add(programName);
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
                    String teacherName = object.getString("value") + ": \n" + teacherName( object.getString("label"));
                    messages.add(teacherName);
                }
                return messages;
            }

        return messages;
    }

    /**
     *
     * @param courseName kursens namn i HTML format
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
    /**
     *
     * @param programName programmets namn i HTML format
     * @return programmets namn
     */
    public String programName(String programName) {
        programName = Html.fromHtml(programName).toString();
        String[] programNameHTMLData = programName.split(",");
        if(programNameHTMLData.length > 1) {
            return programNameHTMLData[1].trim();
        } else {
            return programName;
        }
    }
    /**
     *
     * @param teacherName lektorns namn i HTML format
     * @return lektorns namn
     */
    public String teacherName(String teacherName) {
        teacherName = Html.fromHtml(teacherName).toString();
        String[] teacherNameHTMLData = teacherName.split(",");
        if(teacherNameHTMLData.length > 1) {
            return teacherNameHTMLData[1].trim();
        } else {
            return teacherName;
        }
    }
}

package Controllers;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * ICAL-filen parsas och den informationen som behövs spars ner i info listan.
 */

public class ICalParser {
    private static final String TAG = "ICALParser";
    public static ArrayList<InfoHandler> info;
    private String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    private InfoHandler infoHandler;
    private BufferedReader br;
    private FileInputStream fis;

    public void parseICS(){
        info = new ArrayList<InfoHandler>();
        try {
            fis = new FileInputStream(new File(path, "/temp/SC1444.ics"));
            InputStreamReader isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);

            String s;
            infoHandler = new InfoHandler();
            while((s = br.readLine()) != null) {
                if(s.contains("DTSTART:")) infoHandler.setStart(s.substring(s.lastIndexOf(":") + 1));
                if(s.contains("DTEND:")) infoHandler.setStop(s.substring(s.lastIndexOf(":") + 1));

                if(s.contains("SUMMARY:Program:")){
                    String[] holder = s.split(" ");
                    infoHandler.setProgramCode(holder[1]);
                    for(int i = 2; i < holder.length; i++) {
                        switch(holder[i]) {
                            case "Kurs.grp:":
                                infoHandler.setCourseCode(holder[i+1].substring(0, holder[i+1].lastIndexOf("-")));
                            case "Sign:":
                                infoHandler.setTeacherSignature(holder[i+1]);
                                if(!holder[i+2].equals("Moment:")) infoHandler.setSecondTeacherSignature(holder[i+2]);
                                else infoHandler.setSecondTeacherSignature("");
                            case "Moment:":
                                String info = "";
                                for(int c = i+1; c < holder.length; c++) {
                                    if(!holder[c].equals("Aktivitetstyp:")) info += holder[c] + " ";
                                    if(holder[c].equals("Aktivitetstyp:")) c = holder.length;
                                }
                                infoHandler.setLectureInfo(info);
                        }
                    }
                }
                if(s.contains("LOCATION:")) {
                    infoHandler.setRoomNr(s.substring(s.lastIndexOf(":")+1));
                }

                if(s.equals("END:VEVENT") && infoHandler != null) {
                    info.add(infoHandler);
                    infoHandler = new InfoHandler();
                }


            }

        } catch(IOException e) {
            Log.e(TAG, "IOException", e);
        }
    }

    public ArrayList<InfoHandler> getInfoList(){
        return info;
    }
}

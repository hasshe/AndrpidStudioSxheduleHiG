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
    private BufferedReader bufferRead;
    private FileInputStream fileReadi;

    public void parseICS(){
        info = new ArrayList<InfoHandler>();
        try {
            fileReadi = new FileInputStream(new File(path, "/temp/SC1444.ics"));
            InputStreamReader inputStreamRead = new InputStreamReader(fileReadi);
            bufferRead = new BufferedReader(inputStreamRead);

            String scheduleCalData;
            infoHandler = new InfoHandler();
            while((scheduleCalData = bufferRead.readLine()) != null) {
                if(scheduleCalData.contains("DTSTART:"))
                    infoHandler.setStart(scheduleCalData.substring(scheduleCalData.lastIndexOf(":") + 1));
                if(scheduleCalData.contains("DTEND:"))
                    infoHandler.setStop(scheduleCalData.substring(scheduleCalData.lastIndexOf(":") + 1));

                if(scheduleCalData.contains("SUMMARY:Program:")) {
                    String[] holder = scheduleCalData.split(" ");
                    infoHandler.setProgramCode(holder[1]);
                    for (int i = 2; i < holder.length; i++) {
                        switch (holder[i]) {
                            case "Kurs.grp:":
                                infoHandler.setCourseCode(holder[i + 1].substring(0, holder[i + 1].lastIndexOf("-")));
                            case "Sign:":
                                infoHandler.setTeacherSignature(holder[i + 1]);
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

                if(scheduleCalData.contains("LOCATION:")) {
                    infoHandler.setRoomNr(scheduleCalData.substring(scheduleCalData.lastIndexOf(":")+1));
                }

                if(scheduleCalData.equals("END:VEVENT") && infoHandler != null) {
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


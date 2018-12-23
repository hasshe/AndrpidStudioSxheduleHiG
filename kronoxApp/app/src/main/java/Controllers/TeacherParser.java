package Controllers;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TeacherParser {
    private static final String TAG = "TeacherParser";
    public static ArrayList<TeacherInfoHandler> info;
    private String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    private TeacherInfoHandler teacherInfoHandler;
    private BufferedReader br;
    private FileInputStream fis;

    public void parseICS(){
        info = new ArrayList<TeacherInfoHandler>();
        try {
            fis = new FileInputStream(new File(path, "/temp/SchemaICAL(3).ics"));
            InputStreamReader isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);

            String s;
            teacherInfoHandler = new TeacherInfoHandler();
            while((s = br.readLine()) != null) {
                if(s.contains("DTSTART:")) teacherInfoHandler.setStart(s.substring(s.lastIndexOf(":") + 1));
                if(s.contains("DTEND:")) teacherInfoHandler.setStop(s.substring(s.lastIndexOf(":") + 1));

                if(s.contains("SUMMARY:Program:")){
                    String[] holder = s.split(" ");
                    teacherInfoHandler.setTeacherSignature(holder[1]);
                    for(int i = 2; i < holder.length; i++) {
                        switch(holder[i]) {
                            case "Kurs.grp:":
                                teacherInfoHandler.setRoomNr(holder[i+1].substring(0, holder[i+1].lastIndexOf("-")));
                            case "Sign:":
                                teacherInfoHandler.setTeacherSignature(holder[i+1]);
                            case "Moment:":
                                String info = "";
                                for(int c = i+1; c < holder.length; c++) {
                                    if(!holder[c].equals("Aktivitetstyp:")) info += holder[c] + " ";
                                    if(holder[c].equals("Aktivitetstyp:")) c = holder.length;
                                }
                                teacherInfoHandler.setLectureInfo(info);
                        }
                    }
                }
                if(s.contains("LOCATION:")) {
                    teacherInfoHandler.setRoomNr(s.substring(s.lastIndexOf(":")+1));
                }

                if(s.equals("END:VEVENT") && teacherInfoHandler != null) {
                    info.add(teacherInfoHandler);
                    teacherInfoHandler = new TeacherInfoHandler();
                }


            }

        } catch(IOException e) {
            Log.e(TAG, "IOException", e);
        }
    }

    public ArrayList<TeacherInfoHandler> getInfoList(){
        return info;
    }
}

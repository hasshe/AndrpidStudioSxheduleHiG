package Parser;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * ICAL-filen parsas och den informationen som behövs spars ner i info listan.
 */

public class ICalDataParser {
    public static ArrayList<ScheduleInfo> info;
    private String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    private ScheduleInfo ScheduleInfo;
    private BufferedReader bufferRead;
    private FileInputStream fileReadi;

    public void parseICS(){
        info = new ArrayList<>();
        try {
            fileReadi = new FileInputStream(new File(path, "/temp/SC1444.ics"));
            InputStreamReader inputStreamRead = new InputStreamReader(fileReadi);
            bufferRead = new BufferedReader(inputStreamRead);

            String scheduleCalData;
            ScheduleInfo = new ScheduleInfo();
            while((scheduleCalData = bufferRead.readLine()) != null) {
                if(scheduleCalData.contains("DTSTART:")) {
                    ScheduleInfo.setStart(scheduleCalData.substring(scheduleCalData.lastIndexOf(":") + 1));
                    ScheduleInfo.setDate(scheduleCalData.substring(scheduleCalData.lastIndexOf(":")+1).substring(0, 4) +
                   " - ");
                    String lessonDate = ScheduleInfo.getDate();
                    ScheduleInfo.setDate(scheduleCalData.substring(scheduleCalData.lastIndexOf(":")+1).substring(4, 6) +
                            " - ");
                    lessonDate += ScheduleInfo.getDate();
                    ScheduleInfo.setDate(scheduleCalData.substring(scheduleCalData.lastIndexOf(":")+1).substring(6, 8));
                    lessonDate += ScheduleInfo.getDate();

                    ScheduleInfo.setDate(lessonDate);
                }
                if(scheduleCalData.contains("DTEND:"))
                    ScheduleInfo.setStop(scheduleCalData.substring(scheduleCalData.lastIndexOf(":") + 1));

                if(scheduleCalData.contains("SUMMARY:Program:")) {
                    String[] scheduleCalDataHolder = scheduleCalData.split(" ");
                    ScheduleInfo.setProgramCode(scheduleCalDataHolder[1]);
                    for (int i = 2; i < scheduleCalDataHolder.length; i++) {
                        switch (scheduleCalDataHolder[i]) {
                            case "Kurs.grp:":
                                ScheduleInfo.setCourseCode(scheduleCalDataHolder[i + 1].substring(0, scheduleCalDataHolder[i + 1].lastIndexOf("-")));
                            case "Sign:":
                                ScheduleInfo.setTeacherSignature(scheduleCalDataHolder[i + 1]);
                                if(!scheduleCalDataHolder[i+2].equals("Moment:")) ScheduleInfo.setSecondTeacherSignature(scheduleCalDataHolder[i+2]);
                                else ScheduleInfo.setSecondTeacherSignature("");
                            case "Moment:":
                                String info = "";
                                for(int index = i+1; index < scheduleCalDataHolder.length; index++) {
                                    if(!scheduleCalDataHolder[index].equals("Aktivitetstyp:")) info += scheduleCalDataHolder[index] + " ";
                                    if(scheduleCalDataHolder[index].equals("Aktivitetstyp:")) index = scheduleCalDataHolder.length;
                                }
                                ScheduleInfo.setLectureInfo(info);
                        }
                    }
                }


                if(scheduleCalData.contains("LOCATION:")) {
                    ScheduleInfo.setRoomNr(scheduleCalData.substring(scheduleCalData.lastIndexOf(":")+1));
                }

                if(scheduleCalData.equals("END:VEVENT") && ScheduleInfo != null) {
                    info.add(ScheduleInfo);
                    ScheduleInfo = new ScheduleInfo();
                }


            }

        } catch(IOException e) {
        }
    }

    public ArrayList<ScheduleInfo> getInfoList(){

        return info;
    }
}


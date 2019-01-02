package ParserAndModel;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * I denna klass tolkas data i filen och returnerar det data
 */
public class ICalDataParser {
    public static ArrayList<ScheduleInfo> scheduleInfo;
    private String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    private ScheduleInfo ScheduleInfo;
    private BufferedReader bufferRead;
    private FileInputStream fileRead;

    /**
     * ICAL filen sparas temporärt och tolkar det data som anses vara relevant
     */
    public void parseICS(){
        scheduleInfo = new ArrayList<>();
        ScheduleInfo = new ScheduleInfo();
        try {
            fileRead = new FileInputStream(new File(path, "/temp/ICFile.ics"));
            InputStreamReader inputStreamRead = new InputStreamReader(fileRead);
            bufferRead = new BufferedReader(inputStreamRead);

            String scheduleCalData;

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
                                String detailedDesc = "";
                                for(int index = i+1; index < scheduleCalDataHolder.length; index++) {
                                    if(scheduleCalDataHolder[index].equals("Aktivitetstyp:") == false)
                                        detailedDesc += scheduleCalDataHolder[index] + " ";
                                    if(scheduleCalDataHolder[index].equals("Aktivitetstyp:"))
                                        index = scheduleCalDataHolder.length;
                                }
                                ScheduleInfo.setLectureInfo(detailedDesc);
                        }
                    }
                }
                if(scheduleCalData.contains("LOCATION:")) {
                    ScheduleInfo.setRoomNr(scheduleCalData.substring(scheduleCalData.lastIndexOf(":")+1));
                }

                if(scheduleCalData.equals("END:VEVENT") && ScheduleInfo != null) {
                    scheduleInfo.add(ScheduleInfo);
                    ScheduleInfo = new ScheduleInfo();
                }
            }

        } catch(IOException e) {
        }
    }

    /**
     *
     * @return array med scheduleInfo om schemat
     */
    public ArrayList<ScheduleInfo> getScheduleInfoList(){

        return scheduleInfo;
    }
}


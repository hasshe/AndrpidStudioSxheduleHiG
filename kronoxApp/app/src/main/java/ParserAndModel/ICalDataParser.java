package ParserAndModel;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * I denna klass tolkas data i filen
 */
public class ICalDataParser {

    protected ArrayList<ScheduleInfo> scheduleInfo;
    private String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    private ScheduleInfo ScheduleInfo;
    private BufferedReader bufferRead;
    private FileInputStream fileRead;
    private int counter = 0;
    private SimpleDateFormat sdf;
    private Date date;
    private DateFormat df;
    private String scheduleCalData;
    private InputStreamReader inputStreamRead;

    /**
     * ICAL filen sparas temporärt och tolkar det data som anses vara relevant
     */
    public void parseICS() throws ParseException, IOException {
        scheduleInfo = new ArrayList<>();
        ScheduleInfo = new ScheduleInfo();
        fileRead = new FileInputStream(new File(path, "/temp/ICFile.ics"));
        inputStreamRead = new InputStreamReader(fileRead);
        bufferRead = new BufferedReader(inputStreamRead);

        sdf = new SimpleDateFormat("yyyy - mm - dd");
        df = new SimpleDateFormat("EEEE");

        while((scheduleCalData = bufferRead.readLine()) != null) {
            if(scheduleCalData.contains("DTSTART:")) {
                ScheduleInfo.setStart(scheduleCalData.substring(scheduleCalData.lastIndexOf(":") + 1));
                ScheduleInfo.setDate(scheduleCalData.substring(scheduleCalData.lastIndexOf(":")+1).substring(0, 4) + " - ");
                String lessonDate = ScheduleInfo.getDate();
                ScheduleInfo.setDate(scheduleCalData.substring(scheduleCalData.lastIndexOf(":")+1).substring(4, 6) + " - ");
                lessonDate += ScheduleInfo.getDate();
                ScheduleInfo.setDate(scheduleCalData.substring(scheduleCalData.lastIndexOf(":")+1).substring(6, 8));
                lessonDate += ScheduleInfo.getDate();
                date = sdf.parse(lessonDate);
                String dayDate = df.format(date);
                ScheduleInfo.setDate(dayDate + ":   " + lessonDate);
            }
            if(scheduleCalData.contains("DTEND:")) {
                ScheduleInfo.setStop(scheduleCalData.substring(scheduleCalData.lastIndexOf(":") + 1));
            }
            if(scheduleCalData.contains("SUMMARY:Program:")) {
                String[] scheduleCalDataHolder = scheduleCalData.split(" ");
                ScheduleInfo.setProgramCode(scheduleCalDataHolder[1]);
                for (int i = 2; i < scheduleCalDataHolder.length; i++) {
                    switch (scheduleCalDataHolder[i]) {
                        case "Kurs.grp:":
                            ScheduleInfo.setCourseCode(scheduleCalDataHolder[i + 1].substring(0, scheduleCalDataHolder[i + 1].lastIndexOf("-")));
                            case "Sign:":
                                ScheduleInfo.setTeacherSignature(scheduleCalDataHolder[i + 1]);
                                if(scheduleCalDataHolder[i+2].equals("Moment:")==false &&
                                        scheduleCalDataHolder[i+2].equalsIgnoreCase("tentamen")==false) {
                                    ScheduleInfo.setSecondTeacherSignature(", " + scheduleCalDataHolder[i + 2]);
                                }
                                else {
                                    ScheduleInfo.setSecondTeacherSignature("");
                                }
                            case "Moment:":
                                String detailedDesc = "";
                                for(int index = i+1; index < scheduleCalDataHolder.length; index++) {
                                    if(scheduleCalDataHolder[index].equals("Aktivitetstyp:") == false)
                                        detailedDesc += scheduleCalDataHolder[index] + " ";
                                    if(detailedDesc.trim().length()<2) {
                                        ScheduleInfo.setDetailedInfo("No Info Given...");
                                        counter++;
                                    }
                                    if(scheduleCalDataHolder[index].equals("Aktivitetstyp:")) {
                                        break;
                                    }
                                }
                                if(counter < 1) {
                                    ScheduleInfo.setDetailedInfo(detailedDesc);
                                }
                        }
                    }
                }
                else if(scheduleCalData.contains("SUMMARY:kurs.grp:")) {
                    ScheduleInfo.setRoomNr("Missing Data: Location");
                    ScheduleInfo.setCourseCode("Missing Data: Course");
                    ScheduleInfo.setDetailedInfo("Missing Data: Description");
                }
                if(scheduleCalData.contains("LOCATION:")) {
                    ScheduleInfo.setRoomNr(scheduleCalData.substring(scheduleCalData.lastIndexOf(":")+1));
                }

                if(scheduleCalData.equals("END:VEVENT")) {
                    scheduleInfo.add(ScheduleInfo);
                    ScheduleInfo = new ScheduleInfo();
                }
            }
    }

    /**
     *
     * @return array med data om schemat
     */
    public ArrayList<ScheduleInfo> getScheduleInfoList(){

        return scheduleInfo;
    }
}


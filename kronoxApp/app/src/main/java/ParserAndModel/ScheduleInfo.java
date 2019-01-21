package ParserAndModel;

/**
 * Modellen för vad som behövs för en lektion/bokat evenemang
 */

public class ScheduleInfo {
    private String courseCode, programCode, detailedInfo, start, stop, date,
            startTime, stopTime, roomNr, teacherSignature, secondTeacherSignature;

    /**
     *
     * @param courseCode sätter kursens kod
     */
    public void setCourseCode(String courseCode) {

        this.courseCode = courseCode;
    }
    /**
     *
     * @param detailedInfo sätter lektionens detaljerade information
     */
    public void setDetailedInfo(String detailedInfo) {

        this.detailedInfo = detailedInfo;
    }
    /**
     *
     * @param programCode sätter programmets kod
     */
    public void setProgramCode(String programCode) {

        this.programCode = programCode;
    }

    /**
     *
     * @return koden för programmet
     */
    public String getProgramCode() {
        return this.programCode;
    }

    /**
     *
     * @param roomNr sätter vilken sal lektionen tar plats i
     */
    public void setRoomNr(String roomNr) {

        this.roomNr = roomNr;
    }


    /**
     *
     * @param teacherSignature sätter lektorns signatur
     */
    public void setTeacherSignature(String teacherSignature) {
        this.teacherSignature = teacherSignature;
    }
    /**
     *
     * @param secondTeacherSignature sätter andra lektorns signatur ifall det finns
     */
    public void setSecondTeacherSignature(String secondTeacherSignature) {
        this.secondTeacherSignature = secondTeacherSignature;
    }
    /**
     *
     * @param date sätter datumet för lektionen
     */
    public void setDate(String date) {

        this.date = date;
    }

    /**
     *
     * @param start sätter tiden då lektionen startar
     */
    public void setStart(String start) {
        this.start = start;
        int hour = Integer.parseInt(start.substring(start.lastIndexOf("T") + 1, start.lastIndexOf("Z")-4)) + 1;
        startTime = hour + ":" + start.substring(start.lastIndexOf("T") + 3, start.lastIndexOf("Z")-2);
    }
    /**
     *
     * @param stop sätter tiden då lektionen slutar
     */
    public void setStop(String stop) {
        this.stop = stop;
        int hour = Integer.parseInt(stop.substring(stop.lastIndexOf("T") + 1, stop.lastIndexOf("Z")-4)) + 1;
        stopTime = hour + ":" + stop.substring(stop.lastIndexOf("T") + 3, stop.lastIndexOf("Z")-2);
    }
    /**
     *
     * @return datumet lektionen tar plats
     */
    public String getDate() {

        return this.date;
    }
    /**
     *
     * @return detaljerad beskrivning om lektionen
     */
    public String getLectureDetailedInfo() {

        return this.detailedInfo;
    }

    /**
     *
     * @return tiden lektionen startar
     */
    public String getStartTime() {

        return this.startTime;
    }


    /**
     *
     * @return koden till kursen
     */
    public String getCourseCode() {

        return this.courseCode;
    }
    /**
     *
     * @return lektorns signatur
     */
    public String getTeacherSignature() {

        return this.teacherSignature + "" + this.secondTeacherSignature;
    }
    /**
     *
     * @return salen där lektionen tar plats
     */
    public String getRoomNr() {

        return this.roomNr;
    }
    /**
     *
     * @return tiden då lektionen slutar
     */
    public String getStopTime() {

        return this.stopTime;
    }

}

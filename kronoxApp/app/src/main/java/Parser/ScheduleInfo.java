package Parser;

/**
 * Modellen för vad som behövs för en lektion
 */

public class ScheduleInfo {
    private String courseCode, programCode, lectureInfo, start, stop, date,
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
     * @param programCode sätter programmets kod
     */
    public void setProgramCode(String programCode) {

        this.programCode = programCode;
    }

    /**
     *
     * @param lectureInfo sätter lektionens detaljerade information
     */
    public void setLectureInfo(String lectureInfo) {

        this.lectureInfo = lectureInfo;
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
     * @param secondTeacherSignature sätter andra lektorns signatur ifall det finns
     */
    public void setSecondTeacherSignature(String secondTeacherSignature) {
        this.secondTeacherSignature = secondTeacherSignature;
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
        setStartTime();
    }

    /**
     *
     * @param stop sätter tiden då lektionen slutar
     */
    public void setStop(String stop) {
        this.stop = stop;
        setStopTime();
    }

    /**
     * Här hanteras biten "DTSTART" och tar tiden i hh:mm
     */
    public void setStartTime() {
        int firstPart = Integer.parseInt(start.substring(start.lastIndexOf("T") + 1, start.lastIndexOf("Z")-4)) + 1;
        startTime = firstPart + ":" + start.substring(start.lastIndexOf("T") + 3, start.lastIndexOf("Z")-2);
    }

    /**
     * Här hanteras biten "DTSTOP" och tar tiden i hh:mm
     */
    public void setStopTime() {
        int firstPart = Integer.parseInt(stop.substring(stop.lastIndexOf("T") + 1, stop.lastIndexOf("Z")-4)) + 1;
        stopTime = firstPart + ":" + stop.substring(stop.lastIndexOf("T") + 3, stop.lastIndexOf("Z")-2);
    }

    /**
     *
     * @return detaljerad beskrivning om lektionen
     */
    public String getLectureMoment () {

        return lectureInfo;
    }

    /**
     *
     * @return datumet lektionen tar plats
     */
    public String getDate() {

        return date;
    }

    /**
     *
     * @return tiden lektionen startar
     */
    public String getStartTime() {

        return startTime;
    }

    /**
     *
     * @return tiden då lektionen slutar
     */
    public String getStopTime() {

        return stopTime;
    }

    /**
     *
     * @return koden till kursen
     */
    public String getCourseCode() {

        return courseCode;
    }
    /**
     *
     * @return salen där lektionen tar plats
     */
    public String getRoomNr() {

        return roomNr;
    }

    /**
     *
     * @return lektorns signatur
     */
    public String getTeacherSignature() {

        return teacherSignature + " " + secondTeacherSignature;
    }

}


package hospitalsimulation;


public class Hospital {

    private SimulationConfig config;
    private SimulationLogger logger;

    private ResourcePool nurses;
    private ResourcePool doctors;
    private ResourcePool rooms;
    private ResourcePool labMachines;

    private long millisPerMinute;

    public Hospital(
            SimulationConfig config,
            SimulationLogger logger,
            long millisPerMinute) {

        this.config = config;
        this.logger = logger;
        this.millisPerMinute = millisPerMinute;

        nurses = new ResourcePool(
                "Nurse",
                config.getNumberOfNurses()
        );

        doctors = new ResourcePool(
                "Doctor",
                config.getNumberOfDoctors()
        );

        rooms = new ResourcePool(
                "Treatment Room",
                config.getNumberOfRooms()
        );

        labMachines = new ResourcePool(
                "Lab Machine",
                config.getNumberOfLabMachines()
        );
    }

    // ---------------- REGISTRATION ----------------

    public void registerPatient(PatientRecord patient)
            throws InterruptedException {

        logger.log(
                "Patient " + patient.getPatientID()
                + " is waiting for registration."
        );

        int nurseID = nurses.acquire();

        try {
            patient.setRegistrationStart(
                    logger.nowMinute(),
                    nurseID
            );

            logger.log(
                    "Nurse " + nurseID
                    + " starts registering Patient "
                    + patient.getPatientID()
            );

            Thread.sleep(
                    config.getRegistrationTime()
                    * millisPerMinute
            );

            patient.setRegistrationEnd(
                    logger.nowMinute()
            );

            logger.log(
                    "Nurse " + nurseID
                    + " completes registration for Patient "
                    + patient.getPatientID()
            );

        } finally {
            nurses.release(nurseID);

            logger.log(
                    "Nurse " + nurseID + " is available."
            );
        }
    }

    // ---------------- ROOM ASSIGNMENT ----------------

    public int assignRoom(PatientRecord patient)
            throws InterruptedException {

        logger.log(
                "Patient " + patient.getPatientID()
                + " is waiting for a treatment room."
        );

        int roomID = rooms.acquire();

        patient.setRoomAssignment(
                logger.nowMinute(),
                roomID
        );

        logger.log(
                "Patient " + patient.getPatientID()
                + " enters Treatment Room " + roomID
        );

        return roomID;
    }

    // ---------------- EXAMINATION ----------------

    public void examinePatient(PatientRecord patient)
            throws InterruptedException {

        int doctorID = doctors.acquire();

        try {
            patient.setExaminationStart(
                    logger.nowMinute(),
                    doctorID
            );

            logger.log(
                    "Doctor " + doctorID
                    + " starts examining Patient "
                    + patient.getPatientID()
            );

            Thread.sleep(
                    config.getExaminationTime()
                    * millisPerMinute
            );

            patient.setExaminationEnd(
                    logger.nowMinute()
            );

            logger.log(
                    "Doctor " + doctorID
                    + " completes examination of Patient "
                    + patient.getPatientID()
            );

        } finally {
            doctors.release(doctorID);

            logger.log(
                    "Doctor " + doctorID + " is available."
            );
        }
    }

    // ---------------- LABORATORY ----------------

    public void performLabTest(PatientRecord patient)
            throws InterruptedException {

        int machineID = labMachines.acquire();

        try {
            patient.setLabStart(
                    logger.nowMinute(),
                    machineID
            );

            logger.log(
                    "Lab Machine " + machineID
                    + " starts testing Patient "
                    + patient.getPatientID()
            );

            Thread.sleep(
                    config.getLabTime()
                    * millisPerMinute
            );

            patient.setLabEnd(
                    logger.nowMinute()
            );

            logger.log(
                    "Lab result is ready for Patient "
                    + patient.getPatientID()
            );

        } finally {
            labMachines.release(machineID);

            logger.log(
                    "Lab Machine " + machineID
                    + " is available."
            );
        }
    }

    // ---------------- TREATMENT ----------------

    public void treatPatient(PatientRecord patient)
            throws InterruptedException {

        int doctorID = doctors.acquire();

        try {
            patient.setTreatmentStart(
                    logger.nowMinute(),
                    doctorID
            );

            logger.log(
                    "Doctor " + doctorID
                    + " starts treatment for Patient "
                    + patient.getPatientID()
            );

            Thread.sleep(
                    config.getTreatmentTime()
                    * millisPerMinute
            );

            patient.setTreatmentEnd(
                    logger.nowMinute()
            );

            logger.log(
                    "Doctor " + doctorID
                    + " completes treatment for Patient "
                    + patient.getPatientID()
            );

        } finally {
            doctors.release(doctorID);

            logger.log(
                    "Doctor " + doctorID + " is available."
            );
        }
    }

    // ---------------- ROOM RELEASE ----------------

    public void releaseRoom(int roomID) {

        rooms.release(roomID);

        logger.log(
                "Treatment Room " + roomID + " is available."
        );
    }
}


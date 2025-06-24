package id.ac.stis.pbo.demo1;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TUGAS KELOMPOK - DATA MODELS & STORE
 * Anggota 5: DataModels.java (Data Classes & Store) - FILE INI
 *
 * File ini berisi:
 * - Class Pegawai (Employee data model)
 * - Class Presensi (Attendance data model)
 * - Class Rapat (Meeting data model)
 * - Class Cuti (Leave data model)
 * - DataStore (Centralized data management)
 * - Utility methods
 */

/**
 * Employee Data Model
 * Represents employee information and salary calculations
 */
class Pegawai {
    String id, nama, password, role, divisi, jabatan;
    Date tglMasuk;
    int sisaCuti;
    double gajiPokok;

    public Pegawai(String id, String nama, String password, String role, String divisi, String jabatan, Date tglMasuk) {
        this.id = id;
        this.nama = nama;
        this.password = password;
        this.role = role;
        this.divisi = divisi;
        this.jabatan = jabatan;
        this.tglMasuk = tglMasuk;
        this.sisaCuti = 12; // 12 days annual leave
        this.gajiPokok = calculateGajiPokok(role, divisi);
    }

    /**
     * Calculates base salary based on role and division
     */
    public double calculateGajiPokok(String role, String divisi) {
        double baseGaji = 4000000; // Base salary 4 million

        // Role multiplier
        switch (role.toLowerCase()) {
            case "manajer":
                baseGaji *= 2.5;
                break;
            case "hr":
                baseGaji *= 1.8;
                break;
            default: // pegawai
                baseGaji *= 1.0;
                break;
        }

        // Division multiplier
        switch (divisi) {
            case "Keuangan":
                baseGaji *= 1.2;
                break;
            case "Marketing":
                baseGaji *= 1.1;
                break;
            case "HR":
                baseGaji *= 1.0;
                break;
        }

        return baseGaji;
    }

    /**
     * Calculates monthly salary with experience and attendance bonuses
     */
    public double calculateGajiBulanan() {
        // Calculate work experience in years
        long workYears = ChronoUnit.YEARS.between(
                tglMasuk.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                LocalDate.now()
        );

        // Experience bonus: 5% per year, max 50%
        double experienceMultiplier = 1 + Math.min(workYears * 0.05, 0.5);

        // Attendance bonus: 50k per day
        long hadirCount = DataStore.getKehadiranBulan(id, LocalDate.now().getMonthValue(), LocalDate.now().getYear());
        double attendanceBonus = hadirCount * 50000;

        return (gajiPokok * experienceMultiplier) + attendanceBonus;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s [%s]", nama, id, jabatan, divisi);
    }
}

/**
 * Attendance Data Model
 * Represents daily attendance records
 */
class Presensi {
    String pegawaiId;
    Date tanggal;
    String keterangan; // Hadir, Izin, Sakit, Lembur
    String approvedBy; // For validation by division head

    public Presensi(String pegawaiId, Date tanggal, String keterangan) {
        this.pegawaiId = pegawaiId;
        this.tanggal = tanggal;
        this.keterangan = keterangan;
        this.approvedBy = null;
    }

    /**
     * Gets formatted attendance string
     */
    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(tanggal);
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %s", pegawaiId, keterangan, getFormattedDate());
    }
}

/**
 * Meeting Data Model
 * Represents meeting information and scheduling
 */
class Rapat {
    String namaRapat, topik, tema, divisi, pihakTerlibat, ruangan;
    Date tanggalMulai, tanggalSelesai;
    String ringkasan;
    List<String> divisiDibutuhkan;

    public Rapat(String namaRapat, String topik, String tema, String divisi,
                 Date tanggalMulai, Date tanggalSelesai, String pihakTerlibat, String ruangan) {
        this.namaRapat = namaRapat;
        this.topik = topik;
        this.tema = tema;
        this.divisi = divisi;
        this.tanggalMulai = tanggalMulai;
        this.tanggalSelesai = tanggalSelesai;
        this.pihakTerlibat = pihakTerlibat;
        this.ruangan = ruangan;
        this.ringkasan = "";
        this.divisiDibutuhkan = new ArrayList<>();
    }

    /**
     * Gets formatted meeting datetime
     */
    public String getFormattedDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(tanggalMulai);
    }

    @Override
    public String toString() {
        return String.format("Rapat: %s - %s [%s] pada %s", namaRapat, topik, ruangan, getFormattedDateTime());
    }
}

/**
 * Leave Data Model
 * Represents leave applications and approvals
 */
class Cuti {
    String pegawaiId;
    Date mulai, selesai;
    String alasan, keperluan;
    String status; // Pending, Disetujui, Ditolak
    String approvedBy;

    public Cuti(String pegawaiId, Date mulai, Date selesai, String alasan, String keperluan) {
        this.pegawaiId = pegawaiId;
        this.mulai = mulai;
        this.selesai = selesai;
        this.alasan = alasan;
        this.keperluan = keperluan;
        this.status = "Pending";
        this.approvedBy = null;
    }

    /**
     * Calculates number of leave days
     */
    public long getJumlahHari() {
        LocalDate start = mulai.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = selesai.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(start, end) + 1;
    }

    /**
     * Gets formatted date range
     */
    public String getFormattedDateRange() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(mulai) + " - " + sdf.format(selesai);
    }

    @Override
    public String toString() {
        return String.format("%s: %s (%s) [%s] - %d hari",
                pegawaiId, keperluan, getFormattedDateRange(), status, getJumlahHari());
    }
}

/**
 * Centralized Data Store
 * Manages all application data and provides utility methods
 */
class DataStore {
    // Data collections
    static Map<String, Pegawai> pegawaiMap = new HashMap<>();
    static List<Presensi> presensiList = new ArrayList<>();
    static List<Rapat> rapatList = new ArrayList<>();
    static List<Cuti> cutiList = new ArrayList<>();

    /**
     * Initializes sample data for the application
     */
    public static void init() {
        initializeEmployees();
        initializeAttendanceData();
        initializeMeetingData();
        initializeLeaveData();
    }

    /**
     * Initialize sample employees
     */
    private static void initializeEmployees() {
        Calendar cal = Calendar.getInstance();

        // Main test accounts
        pegawaiMap.put("pegawai@gawe.com",
                new Pegawai("pegawai@gawe.com", "Andi Prasetyo", "123", "pegawai", "Marketing", "Staff Marketing", cal.getTime()));

        pegawaiMap.put("hr@gawe.com",
                new Pegawai("hr@gawe.com", "Budi Santoso", "admin", "hr", "HR", "HRD", cal.getTime()));

        pegawaiMap.put("manajer@gawe.com",
                new Pegawai("manajer@gawe.com", "Cici Amelia", "456", "manajer", "Keuangan", "Manajer Keuangan", cal.getTime()));

        pegawaiMap.put("kepala.marketing@gawe.com",
                new Pegawai("kepala.marketing@gawe.com", "Dedi Kurniawan", "789", "manajer", "Marketing", "Kepala Divisi Marketing", cal.getTime()));

        // Additional staff
        pegawaiMap.put("staff.hr@gawe.com",
                new Pegawai("staff.hr@gawe.com", "Eka Putri", "password123", "pegawai", "HR", "Staff HR", cal.getTime()));

        pegawaiMap.put("staff.keuangan@gawe.com",
                new Pegawai("staff.keuangan@gawe.com", "Fajar Rahman", "password123", "pegawai", "Keuangan", "Staff Keuangan", cal.getTime()));
    }

    /**
     * Initialize sample attendance data for the last 10 working days
     */
    private static void initializeAttendanceData() {
        Date today = new Date();
        Calendar attendanceCal = Calendar.getInstance();

        // Add attendance for last 10 days (excluding weekends)
        for (int i = 0; i < 10; i++) {
            attendanceCal.setTime(today);
            attendanceCal.add(Calendar.DAY_OF_MONTH, -i);

            // Skip weekends
            if (attendanceCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY &&
                    attendanceCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {

                // Regular attendance for main employees
                presensiList.add(new Presensi("pegawai@gawe.com", attendanceCal.getTime(), "Hadir"));
                presensiList.add(new Presensi("hr@gawe.com", attendanceCal.getTime(), "Hadir"));
                presensiList.add(new Presensi("manajer@gawe.com", attendanceCal.getTime(), "Hadir"));

                // Varied attendance for staff
                if (i % 3 == 0) {
                    presensiList.add(new Presensi("staff.hr@gawe.com", attendanceCal.getTime(), "Lembur"));
                } else {
                    presensiList.add(new Presensi("staff.hr@gawe.com", attendanceCal.getTime(), "Hadir"));
                }
            }
        }
    }

    /**
     * Initialize sample meeting data
     */
    private static void initializeMeetingData() {
        Calendar meetingCal = Calendar.getInstance();

        // Today's meeting
        meetingCal.add(Calendar.HOUR, 2);
        Rapat rapat1 = new Rapat("Evaluasi Kinerja Q2", "Evaluasi Tim", "Review kinerja tim marketing",
                "Marketing", new Date(), meetingCal.getTime(), "Tim Marketing", "Ruang A");
        rapat1.divisiDibutuhkan.add("Marketing");
        rapat1.divisiDibutuhkan.add("HR");
        rapatList.add(rapat1);

        // Tomorrow's meeting
        meetingCal.add(Calendar.DAY_OF_MONTH, 1);
        meetingCal.set(Calendar.HOUR_OF_DAY, 10);
        Calendar meetingEnd = (Calendar) meetingCal.clone();
        meetingEnd.add(Calendar.HOUR, 2);

        Rapat rapat2 = new Rapat("Budget Planning 2025", "Perencanaan Budget", "Diskusi budget tahunan",
                "Keuangan", meetingCal.getTime(), meetingEnd.getTime(), "Tim Keuangan", "Ruang B");
        rapat2.divisiDibutuhkan.add("Keuangan");
        rapat2.divisiDibutuhkan.add("HR");
        rapatList.add(rapat2);
    }

    /**
     * Initialize sample leave data
     */
    private static void initializeLeaveData() {
        Calendar leaveCal = Calendar.getInstance();

        // Future leave request
        leaveCal.add(Calendar.DAY_OF_MONTH, 7);
        Date leaveStart = leaveCal.getTime();
        leaveCal.add(Calendar.DAY_OF_MONTH, 2);
        Date leaveEnd = leaveCal.getTime();

        cutiList.add(new Cuti("pegawai@gawe.com", leaveStart, leaveEnd, "Keperluan keluarga", "Pernikahan adik"));
        cutiList.add(new Cuti("staff.hr@gawe.com", new Date(), new Date(), "Sakit", "Demam tinggi"));

        // Sample approved leave
        Cuti approvedLeave = new Cuti("staff.keuangan@gawe.com", leaveStart, leaveEnd, "Liburan", "Liburan keluarga");
        approvedLeave.status = "Disetujui";
        approvedLeave.approvedBy = "hr@gawe.com";
        cutiList.add(approvedLeave);
    }

    // Utility Methods

    /**
     * Gets employee by ID
     */
    public static Pegawai getPegawai(String id) {
        return pegawaiMap.get(id);
    }

    /**
     * Gets attendance count for a specific month
     */
    public static long getKehadiranBulan(String pegawaiId, int bulan, int tahun) {
        return presensiList.stream()
                .filter(p -> p.pegawaiId.equals(pegawaiId))
                .filter(p -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(p.tanggal);
                    return cal.get(Calendar.MONTH) + 1 == bulan && cal.get(Calendar.YEAR) == tahun;
                })
                .filter(p -> "Hadir".equals(p.keterangan))
                .count();
    }

    /**
     * Checks if a date is weekend
     */
    public static boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek().getValue() >= 6; // Saturday = 6, Sunday = 7
    }

    /**
     * Checks if a date is today
     */
    public static boolean isToday(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date).equals(sdf.format(new Date()));
    }

    /**
     * Gets all employees by division
     */
    public static List<Pegawai> getPegawaiByDivision(String divisi) {
        return pegawaiMap.values().stream()
                .filter(p -> p.divisi.equals(divisi))
                .collect(Collectors.toList());
    }

    /**
     * Gets pending leave requests for a division
     */
    public static List<Cuti> getPendingCutiByDivision(String divisi) {
        return cutiList.stream()
                .filter(c -> "Pending".equals(c.status))
                .filter(c -> {
                    Pegawai p = getPegawai(c.pegawaiId);
                    return p != null && p.divisi.equals(divisi);
                })
                .collect(Collectors.toList());
    }

    /**
     * Gets today's meetings for a division
     */
    public static List<Rapat> getTodayMeetingsForDivision(String divisi) {
        return rapatList.stream()
                .filter(r -> isToday(r.tanggalMulai))
                .filter(r -> r.divisiDibutuhkan.contains(divisi) || r.divisi.equals(divisi))
                .collect(Collectors.toList());
    }

    /**
     * Adds new employee
     */
    public static void addPegawai(Pegawai pegawai) {
        pegawaiMap.put(pegawai.id, pegawai);
    }

    /**
     * Adds new attendance record
     */
    public static void addPresensi(Presensi presensi) {
        presensiList.add(presensi);
    }

    /**
     * Adds new meeting
     */
    public static void addRapat(Rapat rapat) {
        rapatList.add(rapat);
    }

    /**
     * Adds new leave request
     */
    public static void addCuti(Cuti cuti) {
        cutiList.add(cuti);
    }
}
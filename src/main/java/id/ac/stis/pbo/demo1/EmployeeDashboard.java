package id.ac.stis.pbo.demo1;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TUGAS KELOMPOK - EMPLOYEE DASHBOARD
 * Anggota 2: EmployeeDashboard.java (Dashboard Pegawai) - FILE INI
 *
 * Fitur yang diimplementasikan:
 * - Dashboard Overview dengan statistik personal
 * - Presensi kehadiran harian
 * - Jadwal rapat yang relevan
 * - Pengajuan cuti dengan validasi
 * - Informasi gaji dan riwayat
 * - Edit profil personal
 */

public class EmployeeDashboard extends Application {
    private final Pegawai pegawai;
    private StackPane contentArea;

    public EmployeeDashboard(Pegawai pegawai) {
        this.pegawai = pegawai;
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Create header, navigation, and content areas
        HBox header = createHeader(stage);
        VBox navigation = createNavigation();
        contentArea = createContentArea();

        root.setTop(header);
        root.setLeft(navigation);
        root.setCenter(contentArea);

        // Show default dashboard content
        showDashboardContent();

        Scene scene = new Scene(root, 1400, 900);
        stage.setScene(scene);
        stage.setTitle("GAWE - Employee Dashboard");
        stage.show();
    }

    /**
     * Creates the header with user info and logout button
     */
    private HBox createHeader(Stage stage) {
        HBox header = new HBox();
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #667eea;");
        header.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("GAWE - Employee Dashboard");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userLabel = new Label("Welcome, " + pegawai.nama + " (" + pegawai.divisi + " - " + pegawai.jabatan + ")");
        userLabel.setFont(Font.font("Arial", 14));
        userLabel.setTextFill(Color.WHITE);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 8 15;");
        logoutBtn.setOnAction(e -> {
            new LoginPage().start(new Stage());
            stage.close();
        });

        header.getChildren().addAll(titleLabel, spacer, userLabel, logoutBtn);
        return header;
    }

    /**
     * Creates the navigation sidebar
     */
    private VBox createNavigation() {
        VBox navigation = new VBox(10);
        navigation.setPadding(new Insets(20));
        navigation.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 0 1 0 0;");
        navigation.setPrefWidth(280);

        // Navigation buttons
        Button dashboardBtn = createNavButton("📊 Dashboard");
        Button presensiBtn = createNavButton("✅ Presensi");
        Button jadwalBtn = createNavButton("📅 Jadwal Rapat");
        Button cutiBtn = createNavButton("🏖️ Pengajuan Cuti");
        Button gajiBtn = createNavButton("💰 Gaji & Riwayat");
        Button profileBtn = createNavButton("👤 Edit Profil");

        // Add event handlers
        dashboardBtn.setOnAction(e -> showDashboardContent());
        presensiBtn.setOnAction(e -> showPresensiContent());
        jadwalBtn.setOnAction(e -> showJadwalRapatContent());
        cutiBtn.setOnAction(e -> showCutiContent());
        gajiBtn.setOnAction(e -> showGajiContent());
        profileBtn.setOnAction(e -> showProfileContent());

        navigation.getChildren().addAll(dashboardBtn, presensiBtn, jadwalBtn, cutiBtn, gajiBtn, profileBtn);
        return navigation;
    }

    /**
     * Creates a styled navigation button
     */
    private Button createNavButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #495057; -fx-font-size: 14; -fx-alignment: center-left; -fx-padding: 15 20; -fx-pref-width: 240;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #e9ecef; -fx-text-fill: #495057; -fx-font-size: 14; -fx-alignment: center-left; -fx-padding: 15 20; -fx-pref-width: 240;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent; -fx-text-fill: #495057; -fx-font-size: 14; -fx-alignment: center-left; -fx-padding: 15 20; -fx-pref-width: 240;"));
        return button;
    }

    /**
     * Creates the main content area
     */
    private StackPane createContentArea() {
        StackPane contentArea = new StackPane();
        contentArea.setPadding(new Insets(20));
        return contentArea;
    }

    /**
     * Shows the main dashboard content with statistics
     */
    private void showDashboardContent() {
        contentArea.getChildren().clear();

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label titleLabel = new Label("Dashboard Overview");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Statistics cards
        HBox statsRow = new HBox(20);
        statsRow.getChildren().addAll(
                createStatCard("Kehadiran Bulan Ini",
                        String.valueOf(DataStore.getKehadiranBulan(pegawai.id, LocalDate.now().getMonthValue(), LocalDate.now().getYear())),
                        "#28a745"),
                createStatCard("Sisa Cuti", pegawai.sisaCuti + " hari", "#17a2b8"),
                createStatCard("Gaji Bulan Ini", "Rp " + String.format("%.0f", pegawai.calculateGajiBulanan()), "#ffc107")
        );

        // Recent activities
        VBox recentActivities = createRecentActivitiesSection();

        content.getChildren().addAll(titleLabel, statsRow, recentActivities);
        contentArea.getChildren().add(content);
    }

    /**
     * Creates a statistics card
     */
    private VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");
        card.setPrefWidth(250);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", 12));
        titleLabel.setTextFill(Color.GRAY);

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        valueLabel.setTextFill(Color.web(color));

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    /**
     * Creates recent activities section
     */
    private VBox createRecentActivitiesSection() {
        VBox recentActivities = new VBox(10);
        recentActivities.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label activitiesTitle = new Label("Aktivitas Terbaru");
        activitiesTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        ListView<String> activitiesList = new ListView<>();
        activitiesList.setPrefHeight(200);

        // Add recent attendance records
        for (Presensi p : DataStore.presensiList) {
            if (p.pegawaiId.equals(pegawai.id)) {
                activitiesList.getItems().add("Presensi: " + p.keterangan + " - " + p.getFormattedDate());
            }
        }

        if (activitiesList.getItems().isEmpty()) {
            activitiesList.getItems().add("Belum ada aktivitas");
        }

        recentActivities.getChildren().addAll(activitiesTitle, activitiesList);
        return recentActivities;
    }

    /**
     * Shows attendance/presence content
     */
    private void showPresensiContent() {
        contentArea.getChildren().clear();

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label titleLabel = new Label("Presensi Kehadiran");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Attendance form
        VBox presensiForm = createPresensiForm();

        // Attendance history
        VBox historyBox = createAttendanceHistorySection();

        content.getChildren().addAll(titleLabel, presensiForm, historyBox);
        contentArea.getChildren().add(content);
    }

    /**
     * Creates the attendance form
     */
    private VBox createPresensiForm() {
        VBox presensiForm = new VBox(15);
        presensiForm.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");
        presensiForm.setMaxWidth(400);

        Label todayLabel = new Label("Presensi Hari Ini: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        todayLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("Hadir", "Izin", "Sakit", "Lembur");
        statusCombo.setPromptText("Pilih Status Kehadiran");
        statusCombo.setPrefWidth(300);

        TextArea keteranganArea = new TextArea();
        keteranganArea.setPromptText("Keterangan tambahan (opsional)");
        keteranganArea.setPrefRowCount(3);
        keteranganArea.setPrefWidth(300);

        Button submitBtn = new Button("Submit Presensi");
        submitBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");
        submitBtn.setOnAction(e -> handlePresensiSubmit(statusCombo, keteranganArea));

        presensiForm.getChildren().addAll(todayLabel, statusCombo, keteranganArea, submitBtn);
        return presensiForm;
    }

    /**
     * Handles attendance submission
     */
    private void handlePresensiSubmit(ComboBox<String> statusCombo, TextArea keteranganArea) {
        if (statusCombo.getValue() != null) {
            // Check if already submitted today
            boolean alreadySubmitted = DataStore.presensiList.stream()
                    .anyMatch(p -> p.pegawaiId.equals(pegawai.id) && DataStore.isToday(p.tanggal));

            if (alreadySubmitted) {
                showAlert(Alert.AlertType.WARNING, "Presensi Sudah Ada", "Anda sudah melakukan presensi hari ini!");
            } else {
                Presensi presensi = new Presensi(pegawai.id, new Date(), statusCombo.getValue());
                DataStore.addPresensi(presensi);
                showAlert(Alert.AlertType.INFORMATION, "Presensi Berhasil", "Presensi berhasil dicatat!");
                showPresensiContent(); // Refresh
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Lengkap", "Mohon pilih status kehadiran!");
        }
    }

    /**
     * Creates attendance history section
     */
    private VBox createAttendanceHistorySection() {
        VBox historyBox = new VBox(15);
        historyBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label historyTitle = new Label("Riwayat Presensi");
        historyTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TableView<Presensi> historyTable = new TableView<>();

        TableColumn<Presensi, String> tanggalCol = new TableColumn<>("Tanggal");
        tanggalCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getFormattedDate()));

        TableColumn<Presensi, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("keterangan"));

        historyTable.getColumns().addAll(tanggalCol, statusCol);

        ObservableList<Presensi> userPresensi = DataStore.presensiList.stream()
                .filter(p -> p.pegawaiId.equals(pegawai.id))
                .sorted((a, b) -> b.tanggal.compareTo(a.tanggal))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        historyTable.setItems(userPresensi);
        historyTable.setPrefHeight(300);

        historyBox.getChildren().addAll(historyTitle, historyTable);
        return historyBox;
    }

    /**
     * Shows meeting schedule content
     */
    private void showJadwalRapatContent() {
        contentArea.getChildren().clear();

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label titleLabel = new Label("Jadwal Rapat");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Today's meetings
        VBox todayMeetings = createTodayMeetingsSection();

        // Upcoming meetings
        VBox upcomingMeetings = createUpcomingMeetingsSection();

        content.getChildren().addAll(titleLabel, todayMeetings, upcomingMeetings);
        contentArea.getChildren().add(content);
    }

    /**
     * Creates today's meetings section
     */
    private VBox createTodayMeetingsSection() {
        VBox todayMeetings = new VBox(15);
        todayMeetings.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label todayTitle = new Label("Rapat Hari Ini");
        todayTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        ListView<String> todayList = new ListView<>();
        todayList.setPrefHeight(150);

        List<Rapat> todayMeetingsList = DataStore.getTodayMeetingsForDivision(pegawai.divisi);

        if (todayMeetingsList.isEmpty()) {
            todayList.getItems().add("Tidak ada rapat hari ini");
        } else {
            for (Rapat rapat : todayMeetingsList) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                String meetingInfo = String.format("%s - %s (%s)\n%s - %s",
                        rapat.namaRapat, rapat.topik, rapat.ruangan,
                        sdf.format(rapat.tanggalMulai), sdf.format(rapat.tanggalSelesai));
                todayList.getItems().add(meetingInfo);
            }
        }

        todayMeetings.getChildren().addAll(todayTitle, todayList);
        return todayMeetings;
    }

    /**
     * Creates upcoming meetings section
     */
    private VBox createUpcomingMeetingsSection() {
        VBox upcomingMeetings = new VBox(15);
        upcomingMeetings.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label upcomingTitle = new Label("Rapat Mendatang");
        upcomingTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TableView<Rapat> upcomingTable = new TableView<>();

        TableColumn<Rapat, String> namaCol = new TableColumn<>("Nama Rapat");
        namaCol.setCellValueFactory(new PropertyValueFactory<>("namaRapat"));

        TableColumn<Rapat, String> tanggalCol = new TableColumn<>("Tanggal & Waktu");
        tanggalCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getFormattedDateTime()));

        TableColumn<Rapat, String> ruanganCol = new TableColumn<>("Ruangan");
        ruanganCol.setCellValueFactory(new PropertyValueFactory<>("ruangan"));

        upcomingTable.getColumns().addAll(namaCol, tanggalCol, ruanganCol);

        ObservableList<Rapat> userMeetings = DataStore.rapatList.stream()
                .filter(r -> !DataStore.isToday(r.tanggalMulai) && r.tanggalMulai.after(new Date()))
                .filter(r -> r.divisiDibutuhkan.contains(pegawai.divisi) || r.divisi.equals(pegawai.divisi))
                .sorted((a, b) -> a.tanggalMulai.compareTo(b.tanggalMulai))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        upcomingTable.setItems(userMeetings);
        upcomingTable.setPrefHeight(300);

        upcomingMeetings.getChildren().addAll(upcomingTitle, upcomingTable);
        return upcomingMeetings;
    }

    /**
     * Shows leave application content
     */
    private void showCutiContent() {
        contentArea.getChildren().clear();

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label titleLabel = new Label("Pengajuan Cuti");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Leave application form
        VBox cutiForm = createLeaveApplicationForm();

        // Leave history
        VBox historyBox = createLeaveHistorySection();

        content.getChildren().addAll(titleLabel, cutiForm, historyBox);
        contentArea.getChildren().add(content);
    }

    /**
     * Creates leave application form
     */
    private VBox createLeaveApplicationForm() {
        VBox cutiForm = new VBox(15);
        cutiForm.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");
        cutiForm.setMaxWidth(500);

        Label formTitle = new Label("Ajukan Cuti Baru");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label sisaCutiLabel = new Label("Sisa Cuti: " + pegawai.sisaCuti + " hari");
        sisaCutiLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        sisaCutiLabel.setTextFill(Color.BLUE);

        DatePicker mulaiPicker = new DatePicker();
        mulaiPicker.setPromptText("Tanggal Mulai");
        mulaiPicker.setPrefWidth(300);

        DatePicker selesaiPicker = new DatePicker();
        selesaiPicker.setPromptText("Tanggal Selesai");
        selesaiPicker.setPrefWidth(300);

        TextField keperluanField = new TextField();
        keperluanField.setPromptText("Keperluan Cuti");
        keperluanField.setPrefWidth(300);

        TextArea alasanArea = new TextArea();
        alasanArea.setPromptText("Alasan detail");
        alasanArea.setPrefRowCount(3);
        alasanArea.setPrefWidth(300);

        Button submitCutiBtn = new Button("Ajukan Cuti");
        submitCutiBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");
        submitCutiBtn.setOnAction(e -> handleLeaveSubmission(mulaiPicker, selesaiPicker, keperluanField, alasanArea));

        cutiForm.getChildren().addAll(formTitle, sisaCutiLabel,
                new Label("Tanggal Mulai:"), mulaiPicker,
                new Label("Tanggal Selesai:"), selesaiPicker,
                new Label("Keperluan:"), keperluanField,
                new Label("Alasan:"), alasanArea,
                submitCutiBtn);

        return cutiForm;
    }

    /**
     * Handles leave application submission
     */
    private void handleLeaveSubmission(DatePicker mulaiPicker, DatePicker selesaiPicker,
                                       TextField keperluanField, TextArea alasanArea) {
        if (mulaiPicker.getValue() == null || selesaiPicker.getValue() == null || keperluanField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Lengkap", "Mohon lengkapi semua field!");
            return;
        }

        // Check for weekend
        if (DataStore.isWeekend(mulaiPicker.getValue()) || DataStore.isWeekend(selesaiPicker.getValue())) {
            showAlert(Alert.AlertType.ERROR, "Tanggal Tidak Valid", "Tidak bisa mengajukan cuti di hari weekend!");
            return;
        }

        // Calculate days
        long jumlahHari = ChronoUnit.DAYS.between(mulaiPicker.getValue(), selesaiPicker.getValue()) + 1;

        if (jumlahHari > pegawai.sisaCuti) {
            showAlert(Alert.AlertType.ERROR, "Sisa Cuti Tidak Mencukupi",
                    "Anda butuh " + jumlahHari + " hari, sisa " + pegawai.sisaCuti + " hari");
            return;
        }

        Date mulai = java.sql.Date.valueOf(mulaiPicker.getValue());
        Date selesai = java.sql.Date.valueOf(selesaiPicker.getValue());

        Cuti cuti = new Cuti(pegawai.id, mulai, selesai, alasanArea.getText(), keperluanField.getText());
        DataStore.addCuti(cuti);

        showAlert(Alert.AlertType.INFORMATION, "Pengajuan Berhasil", "Pengajuan cuti berhasil dikirim!\nMenunggu persetujuan manajer.");

        // Clear form
        mulaiPicker.setValue(null);
        selesaiPicker.setValue(null);
        keperluanField.clear();
        alasanArea.clear();

        showCutiContent(); // Refresh
    }

    /**
     * Creates leave history section
     */
    private VBox createLeaveHistorySection() {
        VBox historyBox = new VBox(15);
        historyBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label historyTitle = new Label("Riwayat Pengajuan Cuti");
        historyTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TableView<Cuti> historyTable = new TableView<>();

        TableColumn<Cuti, String> keperluanCol = new TableColumn<>("Keperluan");
        keperluanCol.setCellValueFactory(new PropertyValueFactory<>("keperluan"));

        TableColumn<Cuti, String> periodeCol = new TableColumn<>("Periode");
        periodeCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getFormattedDateRange()));

        TableColumn<Cuti, String> hariCol = new TableColumn<>("Jumlah Hari");
        hariCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getJumlahHari())));

        TableColumn<Cuti, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        historyTable.getColumns().addAll(keperluanCol, periodeCol, hariCol, statusCol);

        ObservableList<Cuti> userCuti = DataStore.cutiList.stream()
                .filter(c -> c.pegawaiId.equals(pegawai.id))
                .sorted((a, b) -> b.mulai.compareTo(a.mulai))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        historyTable.setItems(userCuti);
        historyTable.setPrefHeight(250);

        historyBox.getChildren().addAll(historyTitle, historyTable);
        return historyBox;
    }

    /**
     * Shows salary and history content
     */
    private void showGajiContent() {
        contentArea.getChildren().clear();

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label titleLabel = new Label("Gaji & Riwayat");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Current salary info
        VBox salaryInfo = createSalaryInfoSection();

        // Attendance summary
        VBox attendanceSummary = createAttendanceSummarySection();

        content.getChildren().addAll(titleLabel, salaryInfo, attendanceSummary);
        contentArea.getChildren().add(content);
    }

    /**
     * Creates salary information section
     */
    private VBox createSalaryInfoSection() {
        VBox salaryInfo = new VBox(15);
        salaryInfo.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label salaryTitle = new Label("Informasi Gaji Bulan Ini");
        salaryTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Calculate salary components
        long hadirCount = DataStore.getKehadiranBulan(pegawai.id, LocalDate.now().getMonthValue(), LocalDate.now().getYear());
        long workYears = ChronoUnit.YEARS.between(
                pegawai.tglMasuk.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                LocalDate.now()
        );
        double experienceMultiplier = 1 + Math.min(workYears * 0.05, 0.5);
        double attendanceBonus = hadirCount * 50000;
        double totalGaji = pegawai.calculateGajiBulanan();

        GridPane salaryGrid = new GridPane();
        salaryGrid.setHgap(20);
        salaryGrid.setVgap(10);

        salaryGrid.add(new Label("Gaji Pokok:"), 0, 0);
        salaryGrid.add(new Label("Rp " + String.format("%.0f", pegawai.gajiPokok)), 1, 0);

        salaryGrid.add(new Label("Masa Kerja:"), 0, 1);
        salaryGrid.add(new Label(workYears + " tahun (bonus " + String.format("%.0f", (experienceMultiplier - 1) * 100) + "%)"), 1, 1);

        salaryGrid.add(new Label("Kehadiran Bulan Ini:"), 0, 2);
        salaryGrid.add(new Label(hadirCount + " hari"), 1, 2);

        salaryGrid.add(new Label("Bonus Kehadiran:"), 0, 3);
        salaryGrid.add(new Label("Rp " + String.format("%.0f", attendanceBonus)), 1, 3);

        salaryGrid.add(new Label("Total Gaji:"), 0, 4);
        Label totalLabel = new Label("Rp " + String.format("%.0f", totalGaji));
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        totalLabel.setTextFill(Color.GREEN);
        salaryGrid.add(totalLabel, 1, 4);

        salaryInfo.getChildren().addAll(salaryTitle, salaryGrid);
        return salaryInfo;
    }

    /**
     * Creates attendance summary section
     */
    private VBox createAttendanceSummarySection() {
        VBox attendanceSummary = new VBox(15);
        attendanceSummary.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label attendanceTitle = new Label("Ringkasan Kehadiran Bulan Ini");
        attendanceTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Count different attendance types for current month
        long hadirCount = DataStore.presensiList.stream()
                .filter(p -> p.pegawaiId.equals(pegawai.id))
                .filter(p -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(p.tanggal);
                    return cal.get(Calendar.MONTH) + 1 == LocalDate.now().getMonthValue()
                            && cal.get(Calendar.YEAR) == LocalDate.now().getYear();
                })
                .filter(p -> "Hadir".equals(p.keterangan))
                .count();

        long izinCount = DataStore.presensiList.stream()
                .filter(p -> p.pegawaiId.equals(pegawai.id))
                .filter(p -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(p.tanggal);
                    return cal.get(Calendar.MONTH) + 1 == LocalDate.now().getMonthValue()
                            && cal.get(Calendar.YEAR) == LocalDate.now().getYear();
                })
                .filter(p -> "Izin".equals(p.keterangan))
                .count();

        long lemburCount = DataStore.presensiList.stream()
                .filter(p -> p.pegawaiId.equals(pegawai.id))
                .filter(p -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(p.tanggal);
                    return cal.get(Calendar.MONTH) + 1 == LocalDate.now().getMonthValue()
                            && cal.get(Calendar.YEAR) == LocalDate.now().getYear();
                })
                .filter(p -> "Lembur".equals(p.keterangan))
                .count();

        double persentaseKehadiran = hadirCount > 0 ? (hadirCount / 22.0) * 100 : 0;

        HBox attendanceStats = new HBox(30);
        attendanceStats.getChildren().addAll(
                createStatCard("Hadir", hadirCount + " hari", "#28a745"),
                createStatCard("Izin", izinCount + " hari", "#ffc107"),
                createStatCard("Lembur", lemburCount + " hari", "#17a2b8"),
                createStatCard("Persentase", String.format("%.1f%%", persentaseKehadiran), "#6f42c1")
        );

        attendanceSummary.getChildren().addAll(attendanceTitle, attendanceStats);
        return attendanceSummary;
    }

    /**
     * Shows profile edit content
     */
    private void showProfileContent() {
        contentArea.getChildren().clear();

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label titleLabel = new Label("Edit Profil");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Profile form
        VBox profileForm = createProfileForm();

        content.getChildren().addAll(titleLabel, profileForm);
        contentArea.getChildren().add(content);
    }

    /**
     * Creates profile editing form
     */
    private VBox createProfileForm() {
        VBox profileForm = new VBox(15);
        profileForm.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");
        profileForm.setMaxWidth(500);

        Label formTitle = new Label("Informasi Pribadi");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);

        TextField namaField = new TextField(pegawai.nama);
        TextField emailField = new TextField(pegawai.id);
        emailField.setDisable(true); // Email cannot be changed

        PasswordField oldPasswordField = new PasswordField();
        oldPasswordField.setPromptText("Password Lama");

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Password Baru");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Konfirmasi Password Baru");

        Label divisiLabel = new Label(pegawai.divisi);
        Label jabatanLabel = new Label(pegawai.jabatan);

        form.add(new Label("Nama:"), 0, 0);
        form.add(namaField, 1, 0);
        form.add(new Label("Email:"), 0, 1);
        form.add(emailField, 1, 1);
        form.add(new Label("Divisi:"), 0, 2);
        form.add(divisiLabel, 1, 2);
        form.add(new Label("Jabatan:"), 0, 3);
        form.add(jabatanLabel, 1, 3);
        form.add(new Label("Password Lama:"), 0, 4);
        form.add(oldPasswordField, 1, 4);
        form.add(new Label("Password Baru:"), 0, 5);
        form.add(newPasswordField, 1, 5);
        form.add(new Label("Konfirmasi Password:"), 0, 6);
        form.add(confirmPasswordField, 1, 6);

        Button updateBtn = new Button("Update Profil");
        updateBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20;");
        updateBtn.setOnAction(e -> handleProfileUpdate(namaField, oldPasswordField, newPasswordField, confirmPasswordField));

        profileForm.getChildren().addAll(formTitle, form, updateBtn);
        return profileForm;
    }

    /**
     * Handles profile update
     */
    private void handleProfileUpdate(TextField namaField, PasswordField oldPasswordField,
                                     PasswordField newPasswordField, PasswordField confirmPasswordField) {
        if (namaField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", "Nama tidak boleh kosong!");
            return;
        }

        // Update name
        pegawai.nama = namaField.getText();

        // Update password if provided
        if (!oldPasswordField.getText().isEmpty()) {
            if (!pegawai.password.equals(oldPasswordField.getText())) {
                showAlert(Alert.AlertType.ERROR, "Password Salah", "Password lama tidak sesuai!");
                return;
            }

            if (newPasswordField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Tidak Lengkap", "Password baru tidak boleh kosong!");
                return;
            }

            if (!newPasswordField.getText().equals(confirmPasswordField.getText())) {
                showAlert(Alert.AlertType.ERROR, "Password Tidak Cocok", "Konfirmasi password tidak sesuai!");
                return;
            }

            pegawai.password = newPasswordField.getText();
        }

        showAlert(Alert.AlertType.INFORMATION, "Update Berhasil", "Profil berhasil diperbarui!");

        // Clear password fields
        oldPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
    }

    /**
     * Shows alert dialog
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
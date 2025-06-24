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
 * TUGAS KELOMPOK - MANAGER DASHBOARD
 * Anggota 3: ManagerDashboard.java (Dashboard Manajer) - FILE INI
 *
 * Fitur yang diimplementasikan:
 * - Dashboard Overview dengan statistik manajerial
 * - Semua fitur Employee (inherit dari employee functionality)
 * - Manajemen rapat dengan multi-divisi selection
 * - Persetujuan cuti untuk anggota tim divisi
 * - Monitoring performa tim
 * - Analytics dan laporan divisi
 */

public class ManagerDashboard extends Application {
    private final Pegawai manajer;
    private StackPane contentArea;

    public ManagerDashboard(Pegawai manajer) {
        this.manajer = manajer;
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Create main layout components
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
        stage.setTitle("GAWE - Manager Dashboard");
        stage.show();
    }

    /**
     * Creates the header with manager info and logout
     */
    private HBox createHeader(Stage stage) {
        HBox header = new HBox();
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #667eea;");
        header.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("GAWE - Manager Dashboard");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userLabel = new Label("Welcome, " + manajer.nama + " (" + manajer.divisi + " Manager)");
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
     * Creates navigation with both employee and manager features
     */
    private VBox createNavigation() {
        VBox navigation = new VBox(10);
        navigation.setPadding(new Insets(20));
        navigation.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-width: 0 1 0 0;");
        navigation.setPrefWidth(280);

        // Dashboard
        Button dashboardBtn = createNavButton("📊 Dashboard");
        dashboardBtn.setOnAction(e -> showDashboardContent());
        navigation.getChildren().add(dashboardBtn);

        // Employee features section
        navigation.getChildren().add(createSeparator("EMPLOYEE FEATURES"));

        Button presensiBtn = createNavButton("✅ Presensi");
        Button jadwalBtn = createNavButton("📅 Jadwal Rapat");
        Button cutiBtn = createNavButton("🏖️ Pengajuan Cuti");
        Button gajiBtn = createNavButton("💰 Gaji & Riwayat");
        Button profileBtn = createNavButton("👤 Edit Profil");

        // Add employee feature handlers
        presensiBtn.setOnAction(e -> showPresensiContent());
        jadwalBtn.setOnAction(e -> showJadwalRapatContent());
        cutiBtn.setOnAction(e -> showCutiContent());
        gajiBtn.setOnAction(e -> showGajiContent());
        profileBtn.setOnAction(e -> showProfileContent());

        navigation.getChildren().addAll(presensiBtn, jadwalBtn, cutiBtn, gajiBtn, profileBtn);

        // Manager features section
        navigation.getChildren().add(createSeparator("MANAGER FEATURES"));

        Button rapatManagerBtn = createNavButton("📋 Kelola Rapat");
        Button cutiManagerBtn = createNavButton("✅ Setujui Cuti Tim");
        Button performaBtn = createNavButton("📈 Performa Tim");
        Button analyticsBtn = createNavButton("📊 Analytics Divisi");

        // Add manager feature handlers
        rapatManagerBtn.setOnAction(e -> showMeetingManagementContent());
        cutiManagerBtn.setOnAction(e -> showLeaveApprovalContent());
        performaBtn.setOnAction(e -> showTeamPerformanceContent());
        analyticsBtn.setOnAction(e -> showAnalyticsContent());

        navigation.getChildren().addAll(rapatManagerBtn, cutiManagerBtn, performaBtn, analyticsBtn);

        return navigation;
    }

    /**
     * Creates a section separator
     */
    private Label createSeparator(String text) {
        Label separator = new Label(text);
        separator.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        separator.setTextFill(Color.GRAY);
        separator.setPadding(new Insets(15, 0, 5, 0));
        return separator;
    }

    /**
     * Creates styled navigation button
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
     * Shows manager dashboard with team statistics
     */
    private void showDashboardContent() {
        contentArea.getChildren().clear();

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label titleLabel = new Label("Manager Dashboard Overview");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Manager statistics
        HBox statsRow = createManagerStatsRow();

        // Quick actions for managers
        VBox quickActions = createQuickActionsSection();

        // Team overview
        VBox teamOverview = createTeamOverviewSection();

        content.getChildren().addAll(titleLabel, statsRow, quickActions, teamOverview);
        contentArea.getChildren().add(content);
    }

    /**
     * Creates manager statistics row
     */
    private HBox createManagerStatsRow() {
        HBox statsRow = new HBox(20);

        // Count team members
        long teamCount = DataStore.pegawaiMap.values().stream()
                .filter(p -> manajer.divisi.equals(p.divisi))
                .filter(p -> !p.id.equals(manajer.id))
                .count();

        // Count pending leave requests for division
        long pendingLeaves = DataStore.getPendingCutiByDivision(manajer.divisi).size();

        // Count meetings scheduled
        long meetingsCount = DataStore.rapatList.stream()
                .filter(r -> r.divisi.equals(manajer.divisi))
                .count();

        // Calculate team average attendance
        double avgAttendance = DataStore.pegawaiMap.values().stream()
                .filter(p -> manajer.divisi.equals(p.divisi))
                .filter(p -> !p.id.equals(manajer.id))
                .mapToLong(p -> DataStore.getKehadiranBulan(p.id, LocalDate.now().getMonthValue(), LocalDate.now().getYear()))
                .average()
                .orElse(0.0);

        statsRow.getChildren().addAll(
                createStatCard("Anggota Tim", String.valueOf(teamCount), "#28a745"),
                createStatCard("Cuti Pending", String.valueOf(pendingLeaves), "#ffc107"),
                createStatCard("Total Rapat", String.valueOf(meetingsCount), "#17a2b8"),
                createStatCard("Rata-rata Kehadiran", String.format("%.1f hari", avgAttendance), "#6f42c1")
        );

        return statsRow;
    }

    /**
     * Creates quick actions section for managers
     */
    private VBox createQuickActionsSection() {
        VBox quickActions = new VBox(15);
        quickActions.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label actionsTitle = new Label("Quick Actions");
        actionsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        HBox actionButtons = new HBox(15);

        Button createMeetingBtn = new Button("Buat Rapat Baru");
        createMeetingBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-padding: 10 15; -fx-background-radius: 5;");
        createMeetingBtn.setOnAction(e -> showMeetingManagementContent());

        Button reviewLeavesBtn = new Button("Review Cuti Tim");
        reviewLeavesBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-padding: 10 15; -fx-background-radius: 5;");
        reviewLeavesBtn.setOnAction(e -> showLeaveApprovalContent());

        Button teamReportBtn = new Button("Laporan Tim");
        teamReportBtn.setStyle("-fx-background-color: #6f42c1; -fx-text-fill: white; -fx-padding: 10 15; -fx-background-radius: 5;");
        teamReportBtn.setOnAction(e -> showTeamPerformanceContent());

        actionButtons.getChildren().addAll(createMeetingBtn, reviewLeavesBtn, teamReportBtn);
        quickActions.getChildren().addAll(actionsTitle, actionButtons);

        return quickActions;
    }

    /**
     * Creates team overview section
     */
    private VBox createTeamOverviewSection() {
        VBox teamOverview = new VBox(10);
        teamOverview.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label overviewTitle = new Label("Tim " + manajer.divisi + " - Ringkasan");
        overviewTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        ListView<String> teamList = new ListView<>();
        teamList.setPrefHeight(150);

        // Add team members with their current status
        for (Pegawai p : DataStore.pegawaiMap.values()) {
            if (manajer.divisi.equals(p.divisi) && !p.id.equals(manajer.id)) {
                long hadirBulanIni = DataStore.getKehadiranBulan(p.id, LocalDate.now().getMonthValue(), LocalDate.now().getYear());
                teamList.getItems().add(p.nama + " (" + p.jabatan + ") - Hadir: " + hadirBulanIni + " hari");
            }
        }

        if (teamList.getItems().isEmpty()) {
            teamList.getItems().add("Tidak ada anggota tim");
        }

        teamOverview.getChildren().addAll(overviewTitle, teamList);
        return teamOverview;
    }

    /**
     * Shows meeting management content
     */
    private void showMeetingManagementContent() {
        contentArea.getChildren().clear();

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label title = new Label("Manajemen Rapat");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Meeting creation form
        VBox meetingForm = createMeetingForm();

        // Existing meetings list
        VBox existingMeetings = createExistingMeetingsSection();

        content.getChildren().addAll(title, meetingForm, existingMeetings);
        contentArea.getChildren().add(content);
    }

    /**
     * Creates meeting creation form
     */
    private VBox createMeetingForm() {
        VBox formContainer = new VBox(15);
        formContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label formTitle = new Label("Buat Rapat Baru");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        TextField namaRapatField = new TextField();
        namaRapatField.setPromptText("Nama Rapat");

        TextField topikField = new TextField();
        topikField.setPromptText("Topik Rapat");

        TextArea temaField = new TextArea();
        temaField.setPromptText("Tema/Agenda Rapat");
        temaField.setPrefRowCount(2);

        DatePicker tanggalPicker = new DatePicker();
        tanggalPicker.setPromptText("Tanggal Rapat");

        Spinner<Integer> jamMulaiSpinner = new Spinner<>(8, 17, 9);
        Spinner<Integer> jamSelesaiSpinner = new Spinner<>(9, 18, 10);

        ComboBox<String> ruanganCombo = new ComboBox<>();
        ruanganCombo.getItems().addAll("Ruang A", "Ruang B", "Ruang C", "Ruang Meeting Utama", "Ruang Konferensi");
        ruanganCombo.setPromptText("Pilih Ruangan");

        // Division checkboxes
        CheckBox marketingCheck = new CheckBox("Marketing");
        CheckBox hrCheck = new CheckBox("HR");
        CheckBox keuanganCheck = new CheckBox("Keuangan");

        form.add(new Label("Nama Rapat:"), 0, 0);
        form.add(namaRapatField, 1, 0);
        form.add(new Label("Topik:"), 0, 1);
        form.add(topikField, 1, 1);
        form.add(new Label("Tema/Agenda:"), 0, 2);
        form.add(temaField, 1, 2);
        form.add(new Label("Tanggal:"), 0, 3);
        form.add(tanggalPicker, 1, 3);
        form.add(new Label("Jam Mulai:"), 0, 4);
        form.add(jamMulaiSpinner, 1, 4);
        form.add(new Label("Jam Selesai:"), 0, 5);
        form.add(jamSelesaiSpinner, 1, 5);
        form.add(new Label("Ruangan:"), 0, 6);
        form.add(ruanganCombo, 1, 6);
        form.add(new Label("Divisi yang Dibutuhkan:"), 0, 7);

        HBox divisiBox = new HBox(10);
        divisiBox.getChildren().addAll(marketingCheck, hrCheck, keuanganCheck);
        form.add(divisiBox, 1, 7);

        Button createBtn = new Button("Buat Rapat");
        createBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 5;");
        createBtn.setOnAction(e -> handleMeetingCreation(namaRapatField, topikField, temaField, tanggalPicker,
                jamMulaiSpinner, jamSelesaiSpinner, ruanganCombo,
                marketingCheck, hrCheck, keuanganCheck));

        formContainer.getChildren().addAll(formTitle, form, createBtn);
        return formContainer;
    }

    /**
     * Handles meeting creation
     */
    private void handleMeetingCreation(TextField namaRapatField, TextField topikField, TextArea temaField,
                                       DatePicker tanggalPicker, Spinner<Integer> jamMulaiSpinner,
                                       Spinner<Integer> jamSelesaiSpinner, ComboBox<String> ruanganCombo,
                                       CheckBox marketingCheck, CheckBox hrCheck, CheckBox keuanganCheck) {

        if (namaRapatField.getText().isEmpty() || tanggalPicker.getValue() == null || ruanganCombo.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Lengkap", "Mohon lengkapi field yang diperlukan!");
            return;
        }

        // Create meeting schedule
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(java.sql.Date.valueOf(tanggalPicker.getValue()));
        startCal.set(Calendar.HOUR_OF_DAY, jamMulaiSpinner.getValue());

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(java.sql.Date.valueOf(tanggalPicker.getValue()));
        endCal.set(Calendar.HOUR_OF_DAY, jamSelesaiSpinner.getValue());

        Rapat rapat = new Rapat(
                namaRapatField.getText(),
                topikField.getText(),
                temaField.getText(),
                manajer.divisi,
                startCal.getTime(),
                endCal.getTime(),
                "Tim " + manajer.divisi,
                ruanganCombo.getValue()
        );

        // Add required divisions
        if (marketingCheck.isSelected()) rapat.divisiDibutuhkan.add("Marketing");
        if (hrCheck.isSelected()) rapat.divisiDibutuhkan.add("HR");
        if (keuanganCheck.isSelected()) rapat.divisiDibutuhkan.add("Keuangan");

        // Always include manager's division
        if (!rapat.divisiDibutuhkan.contains(manajer.divisi)) {
            rapat.divisiDibutuhkan.add(manajer.divisi);
        }

        DataStore.addRapat(rapat);
        showAlert(Alert.AlertType.INFORMATION, "Rapat Berhasil Dibuat", "Rapat berhasil dijadwalkan!");

        // Clear form
        namaRapatField.clear();
        topikField.clear();
        temaField.clear();
        tanggalPicker.setValue(null);
        ruanganCombo.setValue(null);
        marketingCheck.setSelected(false);
        hrCheck.setSelected(false);
        keuanganCheck.setSelected(false);

        showMeetingManagementContent(); // Refresh
    }

    /**
     * Creates existing meetings section
     */
    private VBox createExistingMeetingsSection() {
        VBox existingMeetings = new VBox(15);
        existingMeetings.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label sectionTitle = new Label("Rapat yang Telah Dijadwalkan");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TableView<Rapat> meetingsTable = new TableView<>();

        TableColumn<Rapat, String> namaCol = new TableColumn<>("Nama Rapat");
        namaCol.setCellValueFactory(new PropertyValueFactory<>("namaRapat"));
        namaCol.setPrefWidth(200);

        TableColumn<Rapat, String> tanggalCol = new TableColumn<>("Tanggal & Waktu");
        tanggalCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getFormattedDateTime()));
        tanggalCol.setPrefWidth(150);

        TableColumn<Rapat, String> ruanganCol = new TableColumn<>("Ruangan");
        ruanganCol.setCellValueFactory(new PropertyValueFactory<>("ruangan"));
        ruanganCol.setPrefWidth(120);

        TableColumn<Rapat, String> divisiCol = new TableColumn<>("Divisi Terlibat");
        divisiCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(String.join(", ", data.getValue().divisiDibutuhkan)));
        divisiCol.setPrefWidth(200);

        meetingsTable.getColumns().addAll(namaCol, tanggalCol, ruanganCol, divisiCol);

        // Show meetings created by this manager
        ObservableList<Rapat> managerMeetings = DataStore.rapatList.stream()
                .filter(r -> r.divisi.equals(manajer.divisi))
                .sorted((a, b) -> a.tanggalMulai.compareTo(b.tanggalMulai))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        meetingsTable.setItems(managerMeetings);
        meetingsTable.setPrefHeight(250);

        existingMeetings.getChildren().addAll(sectionTitle, meetingsTable);
        return existingMeetings;
    }

    /**
     * Shows leave approval content for team members
     */
    private void showLeaveApprovalContent() {
        contentArea.getChildren().clear();

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label title = new Label("Persetujuan Cuti Tim " + manajer.divisi);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Pending leave requests table
        VBox pendingSection = createPendingLeaveSection();

        // Leave history section
        VBox historySection = createLeaveHistorySection();

        content.getChildren().addAll(title, pendingSection, historySection);
        contentArea.getChildren().add(content);
    }

    /**
     * Creates pending leave requests section
     */
    private VBox createPendingLeaveSection() {
        VBox pendingSection = new VBox(15);
        pendingSection.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label sectionTitle = new Label("Pengajuan Cuti Pending");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TableView<Cuti> table = new TableView<>();

        TableColumn<Cuti, String> pegawaiCol = new TableColumn<>("Pegawai");
        pegawaiCol.setCellValueFactory(data -> {
            Pegawai p = DataStore.getPegawai(data.getValue().pegawaiId);
            return new javafx.beans.property.SimpleStringProperty(p != null ? p.nama : data.getValue().pegawaiId);
        });
        pegawaiCol.setPrefWidth(150);

        TableColumn<Cuti, String> keperluanCol = new TableColumn<>("Keperluan");
        keperluanCol.setCellValueFactory(new PropertyValueFactory<>("keperluan"));
        keperluanCol.setPrefWidth(120);

        TableColumn<Cuti, String> periodeCol = new TableColumn<>("Periode");
        periodeCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getFormattedDateRange()));
        periodeCol.setPrefWidth(180);

        TableColumn<Cuti, String> jumlahHariCol = new TableColumn<>("Jumlah Hari");
        jumlahHariCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getJumlahHari())));
        jumlahHariCol.setPrefWidth(100);

        TableColumn<Cuti, String> alasanCol = new TableColumn<>("Alasan");
        alasanCol.setCellValueFactory(new PropertyValueFactory<>("alasan"));
        alasanCol.setPrefWidth(200);

        table.getColumns().addAll(pegawaiCol, keperluanCol, periodeCol, jumlahHariCol, alasanCol);
        table.setPrefHeight(200);

        // Load pending leave requests for this division
        ObservableList<Cuti> pendingRequests = DataStore.getPendingCutiByDivision(manajer.divisi)
                .stream()
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        table.setItems(pendingRequests);

        HBox actionButtons = new HBox(10);
        Button approveBtn = new Button("Setujui");
        approveBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-padding: 8 15; -fx-background-radius: 5;");

        Button rejectBtn = new Button("Tolak");
        rejectBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-padding: 8 15; -fx-background-radius: 5;");

        approveBtn.setOnAction(e -> handleLeaveApproval(table, true));
        rejectBtn.setOnAction(e -> handleLeaveApproval(table, false));

        actionButtons.getChildren().addAll(approveBtn, rejectBtn);
        pendingSection.getChildren().addAll(sectionTitle, table, actionButtons);

        return pendingSection;
    }

    /**
     * Handles leave approval/rejection
     */
    private void handleLeaveApproval(TableView<Cuti> table, boolean approve) {
        Cuti selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (approve) {
                selected.status = "Disetujui";
                selected.approvedBy = manajer.id;

                // Update employee's remaining leave
                Pegawai employee = DataStore.getPegawai(selected.pegawaiId);
                if (employee != null) {
                    employee.sisaCuti -= (int) selected.getJumlahHari();
                }

                showAlert(Alert.AlertType.INFORMATION, "Cuti Disetujui",
                        "Cuti telah disetujui!\nPegawai: " + (employee != null ? employee.nama : "N/A") +
                                "\nSisa cuti: " + (employee != null ? employee.sisaCuti : "N/A") + " hari");
            } else {
                selected.status = "Ditolak";
                selected.approvedBy = manajer.id;
                showAlert(Alert.AlertType.INFORMATION, "Cuti Ditolak", "Cuti telah ditolak!");
            }

            showLeaveApprovalContent(); // Refresh
        } else {
            showAlert(Alert.AlertType.WARNING, "Pilihan Tidak Valid", "Pilih pengajuan cuti yang akan diproses!");
        }
    }

    /**
     * Creates leave history section
     */
    private VBox createLeaveHistorySection() {
        VBox historySection = new VBox(15);
        historySection.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label sectionTitle = new Label("Riwayat Persetujuan Cuti");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TableView<Cuti> historyTable = new TableView<>();

        TableColumn<Cuti, String> pegawaiCol = new TableColumn<>("Pegawai");
        pegawaiCol.setCellValueFactory(data -> {
            Pegawai p = DataStore.getPegawai(data.getValue().pegawaiId);
            return new javafx.beans.property.SimpleStringProperty(p != null ? p.nama : data.getValue().pegawaiId);
        });

        TableColumn<Cuti, String> periodeCol = new TableColumn<>("Periode");
        periodeCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getFormattedDateRange()));

        TableColumn<Cuti, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Cuti, String> approvedByCol = new TableColumn<>("Disetujui Oleh");
        approvedByCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().approvedBy != null ? data.getValue().approvedBy : ""));

        historyTable.getColumns().addAll(pegawaiCol, periodeCol, statusCol, approvedByCol);

        ObservableList<Cuti> processedLeaves = DataStore.cutiList.stream()
                .filter(c -> !"Pending".equals(c.status))
                .filter(c -> {
                    Pegawai p = DataStore.getPegawai(c.pegawaiId);
                    return p != null && manajer.divisi.equals(p.divisi);
                })
                .sorted((a, b) -> b.mulai.compareTo(a.mulai))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        historyTable.setItems(processedLeaves);
        historyTable.setPrefHeight(200);

        historySection.getChildren().addAll(sectionTitle, historyTable);
        return historySection;
    }

    /**
     * Shows team performance content
     */
    private void showTeamPerformanceContent() {
        contentArea.getChildren().clear();

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label title = new Label("Performa Tim " + manajer.divisi);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Team performance table
        VBox performanceTable = createTeamPerformanceTable();

        // Performance analytics
        VBox analytics = createPerformanceAnalytics();

        content.getChildren().addAll(title, performanceTable, analytics);
        contentArea.getChildren().add(content);
    }

    /**
     * Creates team performance table
     */
    private VBox createTeamPerformanceTable() {
        VBox tableSection = new VBox(15);
        tableSection.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label sectionTitle = new Label("Performa Anggota Tim");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TableView<Map<String, Object>> table = new TableView<>();

        TableColumn<Map<String, Object>, String> namaCol = new TableColumn<>("Nama");
        namaCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty((String) data.getValue().get("nama")));

        TableColumn<Map<String, Object>, String> jabatanCol = new TableColumn<>("Jabatan");
        jabatanCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty((String) data.getValue().get("jabatan")));

        TableColumn<Map<String, Object>, String> kehadiranCol = new TableColumn<>("Kehadiran");
        kehadiranCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("kehadiran").toString()));

        TableColumn<Map<String, Object>, String> persentaseCol = new TableColumn<>("Persentase");
        persentaseCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("persentase").toString() + "%"));

        TableColumn<Map<String, Object>, String> gajiCol = new TableColumn<>("Estimasi Gaji");
        gajiCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty("Rp " + String.format("%.0f", (Double) data.getValue().get("gaji"))));

        table.getColumns().addAll(namaCol, jabatanCol, kehadiranCol, persentaseCol, gajiCol);

        ObservableList<Map<String, Object>> teamData = FXCollections.observableArrayList();
        for (Pegawai p : DataStore.pegawaiMap.values()) {
            if (manajer.divisi.equals(p.divisi) && !p.id.equals(manajer.id)) {
                Map<String, Object> row = new HashMap<>();
                long hadirCount = DataStore.getKehadiranBulan(p.id, LocalDate.now().getMonthValue(), LocalDate.now().getYear());
                row.put("nama", p.nama);
                row.put("jabatan", p.jabatan);
                row.put("kehadiran", hadirCount);
                row.put("persentase", hadirCount > 0 ? Math.round((hadirCount / 22.0) * 100) : 0);
                row.put("gaji", p.calculateGajiBulanan());
                teamData.add(row);
            }
        }
        table.setItems(teamData);
        table.setPrefHeight(300);

        tableSection.getChildren().addAll(sectionTitle, table);
        return tableSection;
    }

    /**
     * Creates performance analytics section
     */
    private VBox createPerformanceAnalytics() {
        VBox analytics = new VBox(15);
        analytics.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label analyticsTitle = new Label("Analytics Tim");
        analyticsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Calculate team statistics
        List<Pegawai> teamMembers = DataStore.pegawaiMap.values().stream()
                .filter(p -> manajer.divisi.equals(p.divisi))
                .filter(p -> !p.id.equals(manajer.id))
                .collect(Collectors.toList());

        double avgTeamAttendance = teamMembers.stream()
                .mapToLong(p -> DataStore.getKehadiranBulan(p.id, LocalDate.now().getMonthValue(), LocalDate.now().getYear()))
                .average()
                .orElse(0.0);

        double totalTeamSalary = teamMembers.stream()
                .mapToDouble(Pegawai::calculateGajiBulanan)
                .sum();

        long totalPendingLeaves = DataStore.getPendingCutiByDivision(manajer.divisi).size();

        HBox analyticsStats = new HBox(20);
        analyticsStats.getChildren().addAll(
                createStatCard("Rata-rata Kehadiran", String.format("%.1f hari", avgTeamAttendance), "#17a2b8"),
                createStatCard("Total Gaji Tim", "Rp " + String.format("%.0f", totalTeamSalary), "#28a745"),
                createStatCard("Cuti Pending", String.valueOf(totalPendingLeaves), "#ffc107")
        );

        analytics.getChildren().addAll(analyticsTitle, analyticsStats);
        return analytics;
    }

    /**
     * Shows analytics content
     */
    private void showAnalyticsContent() {
        contentArea.getChildren().clear();

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label title = new Label("Analytics Divisi " + manajer.divisi);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Detailed analytics would go here
        Label comingSoon = new Label("Feature analytics detail akan segera hadir!");
        comingSoon.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        comingSoon.setTextFill(Color.GRAY);

        content.getChildren().addAll(title, comingSoon);
        contentArea.getChildren().add(content);
    }

    // Employee feature methods (inherited functionality)
    private void showPresensiContent() {
        // Reuse EmployeeDashboard presensi functionality
        // Implementation would be similar to EmployeeDashboard.showPresensiContent()
        contentArea.getChildren().clear();
        Label placeholder = new Label("Presensi feature - sama seperti Employee Dashboard");
        placeholder.setFont(Font.font("Arial", 16));
        contentArea.getChildren().add(placeholder);
    }

    private void showJadwalRapatContent() {
        // Reuse EmployeeDashboard jadwal functionality
        contentArea.getChildren().clear();
        Label placeholder = new Label("Jadwal Rapat feature - sama seperti Employee Dashboard");
        placeholder.setFont(Font.font("Arial", 16));
        contentArea.getChildren().add(placeholder);
    }

    private void showCutiContent() {
        // Reuse EmployeeDashboard cuti functionality
        contentArea.getChildren().clear();
        Label placeholder = new Label("Pengajuan Cuti feature - sama seperti Employee Dashboard");
        placeholder.setFont(Font.font("Arial", 16));
        contentArea.getChildren().add(placeholder);
    }

    private void showGajiContent() {
        // Reuse EmployeeDashboard gaji functionality
        contentArea.getChildren().clear();
        Label placeholder = new Label("Gaji & Riwayat feature - sama seperti Employee Dashboard");
        placeholder.setFont(Font.font("Arial", 16));
        contentArea.getChildren().add(placeholder);
    }

    private void showProfileContent() {
        // Reuse EmployeeDashboard profile functionality
        contentArea.getChildren().clear();
        Label placeholder = new Label("Edit Profil feature - sama seperti Employee Dashboard");
        placeholder.setFont(Font.font("Arial", 16));
        contentArea.getChildren().add(placeholder);
    }

    // Utility methods
    private VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");
        card.setPrefWidth(200);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", 12));
        titleLabel.setTextFill(Color.GRAY);

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        valueLabel.setTextFill(Color.web(color));

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
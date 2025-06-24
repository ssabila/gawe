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
 * TUGAS KELOMPOK - HR DASHBOARD
 * Anggota 4: HRDashboard.java (Dashboard HR) - FILE INI
 *
 * Fitur yang diimplementasikan:
 * - Dashboard Overview dengan statistik company-wide
 * - Manajemen data pegawai (CRUD operations)
 * - Laporan kehadiran semua pegawai
 * - Manajemen cuti company-wide (approve/reject)
 * - Laporan gaji dan payroll management
 * - Analytics dan reporting
 * - Employee lifecycle management
 */

public class HRDashboard extends Application {
    private final Pegawai hrStaff;
    private StackPane contentArea;

    public HRDashboard(Pegawai hrStaff) {
        this.hrStaff = hrStaff;
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

        Scene scene = new Scene(root, 1500, 900);
        stage.setScene(scene);
        stage.setTitle("GAWE - HR Dashboard");
        stage.show();
    }

    /**
     * Creates the header with HR info and logout
     */
    private HBox createHeader(Stage stage) {
        HBox header = new HBox();
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #667eea;");
        header.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("GAWE - HR Dashboard");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.WHITE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userLabel = new Label("Welcome, " + hrStaff.nama + " (Human Resources)");
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
     * Creates navigation with HR-specific features
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

        // HR Core Features
        navigation.getChildren().add(createSeparator("HR MANAGEMENT"));

        Button employeeMgmtBtn = createNavButton("👥 Kelola Pegawai");
        Button attendanceReportBtn = createNavButton("📊 Laporan Kehadiran");
        Button leaveManagementBtn = createNavButton("🏖️ Kelola Semua Cuti");
        Button salaryReportBtn = createNavButton("💳 Laporan Gaji");
        Button analyticsBtn = createNavButton("📈 Analytics & Reports");

        // Add HR feature handlers
        employeeMgmtBtn.setOnAction(e -> showEmployeeManagementContent());
        attendanceReportBtn.setOnAction(e -> showAttendanceReportContent());
        leaveManagementBtn.setOnAction(e -> showLeaveManagementContent());
        salaryReportBtn.setOnAction(e -> showSalaryReportContent());
        analyticsBtn.setOnAction(e -> showAnalyticsContent());

        navigation.getChildren().addAll(employeeMgmtBtn, attendanceReportBtn, leaveManagementBtn, salaryReportBtn, analyticsBtn);

        // Personal Features (HR as employee)
        navigation.getChildren().add(createSeparator("PERSONAL"));

        Button personalPresensiBtn = createNavButton("✅ My Presensi");
        Button personalProfileBtn = createNavButton("👤 My Profile");

        personalPresensiBtn.setOnAction(e -> showPersonalPresensiContent());
        personalProfileBtn.setOnAction(e -> showPersonalProfileContent());

        navigation.getChildren().addAll(personalPresensiBtn, personalProfileBtn);

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
     * Shows HR dashboard with company-wide statistics
     */
    private void showDashboardContent() {
        contentArea.getChildren().clear();

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label titleLabel = new Label("HR Dashboard Overview");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Company statistics
        HBox statsRow = createCompanyStatsRow();

        // Quick actions for HR
        VBox quickActions = createHRQuickActionsSection();

        // Recent activities and alerts
        HBox alertsRow = createAlertsRow();

        content.getChildren().addAll(titleLabel, statsRow, quickActions, alertsRow);
        contentArea.getChildren().add(content);
    }

    /**
     * Creates company statistics row
     */
    private HBox createCompanyStatsRow() {
        HBox statsRow = new HBox(20);

        long totalEmployees = DataStore.pegawaiMap.size();
        long pendingLeaves = DataStore.cutiList.stream()
                .filter(c -> "Pending".equals(c.status))
                .count();

        double avgAttendance = DataStore.pegawaiMap.values().stream()
                .mapToLong(p -> DataStore.getKehadiranBulan(p.id, LocalDate.now().getMonthValue(), LocalDate.now().getYear()))
                .average()
                .orElse(0.0);

        double totalPayroll = DataStore.pegawaiMap.values().stream()
                .mapToDouble(Pegawai::calculateGajiBulanan)
                .sum();

        long activeEmployees = DataStore.pegawaiMap.values().stream()
                .filter(p -> DataStore.getKehadiranBulan(p.id, LocalDate.now().getMonthValue(), LocalDate.now().getYear()) > 0)
                .count();

        statsRow.getChildren().addAll(
                createStatCard("Total Pegawai", String.valueOf(totalEmployees), "#28a745"),
                createStatCard("Pegawai Aktif", String.valueOf(activeEmployees), "#17a2b8"),
                createStatCard("Cuti Pending", String.valueOf(pendingLeaves), "#ffc107"),
                createStatCard("Total Payroll", "Rp " + String.format("%.0f", totalPayroll), "#6f42c1"),
                createStatCard("Rata-rata Kehadiran", String.format("%.1f hari", avgAttendance), "#dc3545")
        );

        return statsRow;
    }

    /**
     * Creates HR quick actions section
     */
    private VBox createHRQuickActionsSection() {
        VBox quickActions = new VBox(15);
        quickActions.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label actionsTitle = new Label("Quick Actions");
        actionsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        HBox actionButtons = new HBox(15);

        Button addEmployeeBtn = new Button("Tambah Pegawai Baru");
        addEmployeeBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-padding: 10 15; -fx-background-radius: 5;");
        addEmployeeBtn.setOnAction(e -> showEmployeeManagementContent());

        Button processLeavesBtn = new Button("Proses Semua Cuti");
        processLeavesBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-padding: 10 15; -fx-background-radius: 5;");
        processLeavesBtn.setOnAction(e -> showLeaveManagementContent());

        Button generateReportBtn = new Button("Generate Laporan");
        generateReportBtn.setStyle("-fx-background-color: #6f42c1; -fx-text-fill: white; -fx-padding: 10 15; -fx-background-radius: 5;");
        generateReportBtn.setOnAction(e -> showAnalyticsContent());

        Button payrollBtn = new Button("Kelola Payroll");
        payrollBtn.setStyle("-fx-background-color: #fd7e14; -fx-text-fill: white; -fx-padding: 10 15; -fx-background-radius: 5;");
        payrollBtn.setOnAction(e -> showSalaryReportContent());

        actionButtons.getChildren().addAll(addEmployeeBtn, processLeavesBtn, generateReportBtn, payrollBtn);
        quickActions.getChildren().addAll(actionsTitle, actionButtons);

        return quickActions;
    }

    /**
     * Creates alerts and notifications row
     */
    private HBox createAlertsRow() {
        HBox alertsRow = new HBox(20);

        // Pending leaves alert
        VBox leavesAlert = createAlertCard("Cuti Pending",
                DataStore.cutiList.stream().filter(c -> "Pending".equals(c.status)).count() + " pengajuan cuti menunggu approval",
                "#ffc107");

        // Low attendance alert
        long lowAttendanceCount = DataStore.pegawaiMap.values().stream()
                .filter(p -> DataStore.getKehadiranBulan(p.id, LocalDate.now().getMonthValue(), LocalDate.now().getYear()) < 15)
                .count();

        VBox attendanceAlert = createAlertCard("Kehadiran Rendah",
                lowAttendanceCount + " pegawai dengan kehadiran < 15 hari",
                "#dc3545");

        // Payroll alert
        VBox payrollAlert = createAlertCard("Payroll Info",
                "Total payroll bulan ini: Rp " + String.format("%.0f",
                        DataStore.pegawaiMap.values().stream().mapToDouble(Pegawai::calculateGajiBulanan).sum()),
                "#28a745");

        alertsRow.getChildren().addAll(leavesAlert, attendanceAlert, payrollAlert);
        return alertsRow;
    }

    /**
     * Creates an alert card
     */
    private VBox createAlertCard(String title, String message, String color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: " + color + "; -fx-border-width: 0 0 0 4;");
        card.setPrefWidth(350);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.web(color));

        Label messageLabel = new Label(message);
        messageLabel.setFont(Font.font("Arial", 12));
        messageLabel.setWrapText(true);

        card.getChildren().addAll(titleLabel, messageLabel);
        return card;
    }

    /**
     * Shows employee management content
     */
    private void showEmployeeManagementContent() {
        contentArea.getChildren().clear();

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label title = new Label("Manajemen Data Pegawai");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Employee creation form
        VBox employeeForm = createEmployeeForm();

        // Employee list/table
        VBox employeeList = createEmployeeListSection();

        content.getChildren().addAll(title, employeeForm, employeeList);
        contentArea.getChildren().add(content);
    }

    /**
     * Creates employee creation form
     */
    private VBox createEmployeeForm() {
        VBox formContainer = new VBox(15);
        formContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label formTitle = new Label("Tambah Pegawai Baru");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(10);

        TextField emailField = new TextField();
        emailField.setPromptText("Email (Username)");

        TextField namaField = new TextField();
        namaField.setPromptText("Nama Lengkap");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password Default");
        passwordField.setText("password123"); // Default password

        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("pegawai", "hr", "manajer");
        roleCombo.setPromptText("Pilih Role");

        ComboBox<String> divisiCombo = new ComboBox<>();
        divisiCombo.getItems().addAll("Marketing", "HR", "Keuangan");
        divisiCombo.setPromptText("Pilih Divisi");

        TextField jabatanField = new TextField();
        jabatanField.setPromptText("Jabatan");

        form.add(new Label("Email (Username):"), 0, 0);
        form.add(emailField, 1, 0);
        form.add(new Label("Nama Lengkap:"), 0, 1);
        form.add(namaField, 1, 1);
        form.add(new Label("Password Default:"), 0, 2);
        form.add(passwordField, 1, 2);
        form.add(new Label("Role:"), 0, 3);
        form.add(roleCombo, 1, 3);
        form.add(new Label("Divisi:"), 0, 4);
        form.add(divisiCombo, 1, 4);
        form.add(new Label("Jabatan:"), 0, 5);
        form.add(jabatanField, 1, 5);

        Button addBtn = new Button("Tambah Pegawai");
        addBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 5;");
        addBtn.setOnAction(e -> handleEmployeeCreation(emailField, namaField, passwordField, roleCombo, divisiCombo, jabatanField));

        Button clearBtn = new Button("Clear Form");
        clearBtn.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 5;");
        clearBtn.setOnAction(e -> {
            emailField.clear();
            namaField.clear();
            passwordField.setText("password123");
            roleCombo.setValue(null);
            divisiCombo.setValue(null);
            jabatanField.clear();
        });

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addBtn, clearBtn);

        formContainer.getChildren().addAll(formTitle, form, buttonBox);
        return formContainer;
    }

    /**
     * Handles employee creation
     */
    private void handleEmployeeCreation(TextField emailField, TextField namaField, PasswordField passwordField,
                                        ComboBox<String> roleCombo, ComboBox<String> divisiCombo, TextField jabatanField) {

        if (emailField.getText().isEmpty() || namaField.getText().isEmpty() ||
                roleCombo.getValue() == null || divisiCombo.getValue() == null || jabatanField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Lengkap", "Mohon lengkapi semua field!");
            return;
        }

        // Check if email already exists
        if (DataStore.getPegawai(emailField.getText()) != null) {
            showAlert(Alert.AlertType.ERROR, "Email Sudah Terdaftar", "Email tersebut sudah digunakan!");
            return;
        }

        Pegawai newPegawai = new Pegawai(
                emailField.getText(),
                namaField.getText(),
                passwordField.getText().isEmpty() ? "password123" : passwordField.getText(),
                roleCombo.getValue(),
                divisiCombo.getValue(),
                jabatanField.getText(),
                new Date()
        );

        DataStore.addPegawai(newPegawai);

        showAlert(Alert.AlertType.INFORMATION, "Pegawai Berhasil Ditambahkan",
                "Pegawai berhasil ditambahkan!\n\nDetail Login:\nUsername: " + newPegawai.id +
                        "\nPassword: " + newPegawai.password + "\n\nSilakan informasikan kepada pegawai untuk mengganti password.");

        // Clear form
        emailField.clear();
        namaField.clear();
        passwordField.setText("password123");
        roleCombo.setValue(null);
        divisiCombo.setValue(null);
        jabatanField.clear();

        showEmployeeManagementContent(); // Refresh
    }

    /**
     * Creates employee list section
     */
    private VBox createEmployeeListSection() {
        VBox listSection = new VBox(15);
        listSection.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label sectionTitle = new Label("Daftar Pegawai");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TableView<Pegawai> employeeTable = new TableView<>();

        TableColumn<Pegawai, String> namaCol = new TableColumn<>("Nama");
        namaCol.setCellValueFactory(new PropertyValueFactory<>("nama"));
        namaCol.setPrefWidth(150);

        TableColumn<Pegawai, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        emailCol.setPrefWidth(200);

        TableColumn<Pegawai, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setPrefWidth(100);

        TableColumn<Pegawai, String> divisiCol = new TableColumn<>("Divisi");
        divisiCol.setCellValueFactory(new PropertyValueFactory<>("divisi"));
        divisiCol.setPrefWidth(120);

        TableColumn<Pegawai, String> jabatanCol = new TableColumn<>("Jabatan");
        jabatanCol.setCellValueFactory(new PropertyValueFactory<>("jabatan"));
        jabatanCol.setPrefWidth(150);

        TableColumn<Pegawai, String> tglMasukCol = new TableColumn<>("Tanggal Masuk");
        tglMasukCol.setCellValueFactory(data -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return new javafx.beans.property.SimpleStringProperty(sdf.format(data.getValue().tglMasuk));
        });
        tglMasukCol.setPrefWidth(120);

        TableColumn<Pegawai, String> gajiCol = new TableColumn<>("Gaji Bulanan");
        gajiCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty("Rp " + String.format("%.0f", data.getValue().calculateGajiBulanan())));
        gajiCol.setPrefWidth(150);

        employeeTable.getColumns().addAll(namaCol, emailCol, roleCol, divisiCol, jabatanCol, tglMasukCol, gajiCol);

        ObservableList<Pegawai> employees = DataStore.pegawaiMap.values().stream()
                .sorted((a, b) -> a.nama.compareTo(b.nama))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        employeeTable.setItems(employees);
        employeeTable.setPrefHeight(350);

        // Action buttons for selected employee
        HBox actionButtons = new HBox(10);
        Button editBtn = new Button("Edit Pegawai");
        editBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-padding: 8 15; -fx-background-radius: 5;");

        Button resetPasswordBtn = new Button("Reset Password");
        resetPasswordBtn.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black; -fx-padding: 8 15; -fx-background-radius: 5;");

        editBtn.setOnAction(e -> {
            Pegawai selected = employeeTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showEmployeeEditDialog(selected);
            } else {
                showAlert(Alert.AlertType.WARNING, "Pilihan Tidak Valid", "Pilih pegawai yang akan diedit!");
            }
        });

        resetPasswordBtn.setOnAction(e -> {
            Pegawai selected = employeeTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.password = "password123";
                showAlert(Alert.AlertType.INFORMATION, "Password Reset",
                        "Password untuk " + selected.nama + " telah direset ke 'password123'");
            } else {
                showAlert(Alert.AlertType.WARNING, "Pilihan Tidak Valid", "Pilih pegawai yang akan direset passwordnya!");
            }
        });

        actionButtons.getChildren().addAll(editBtn, resetPasswordBtn);
        listSection.getChildren().addAll(sectionTitle, employeeTable, actionButtons);

        return listSection;
    }

    /**
     * Shows employee edit dialog
     */
    private void showEmployeeEditDialog(Pegawai employee) {
        Dialog<Pegawai> dialog = new Dialog<>();
        dialog.setTitle("Edit Pegawai");
        dialog.setHeaderText("Edit data pegawai: " + employee.nama);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField namaField = new TextField(employee.nama);
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("pegawai", "hr", "manajer");
        roleCombo.setValue(employee.role);

        ComboBox<String> divisiCombo = new ComboBox<>();
        divisiCombo.getItems().addAll("Marketing", "HR", "Keuangan");
        divisiCombo.setValue(employee.divisi);

        TextField jabatanField = new TextField(employee.jabatan);

        grid.add(new Label("Nama:"), 0, 0);
        grid.add(namaField, 1, 0);
        grid.add(new Label("Role:"), 0, 1);
        grid.add(roleCombo, 1, 1);
        grid.add(new Label("Divisi:"), 0, 2);
        grid.add(divisiCombo, 1, 2);
        grid.add(new Label("Jabatan:"), 0, 3);
        grid.add(jabatanField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                employee.nama = namaField.getText();
                employee.role = roleCombo.getValue();
                employee.divisi = divisiCombo.getValue();
                employee.jabatan = jabatanField.getText();
                employee.gajiPokok = employee.calculateGajiPokok(employee.role, employee.divisi);
                return employee;
            }
            return null;
        });

        Optional<Pegawai> result = dialog.showAndWait();
        result.ifPresent(pegawai -> {
            showAlert(Alert.AlertType.INFORMATION, "Update Berhasil", "Data pegawai berhasil diperbarui!");
            showEmployeeManagementContent(); // Refresh
        });
    }

    /**
     * Shows attendance report content
     */
    private void showAttendanceReportContent() {
        contentArea.getChildren().clear();

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label title = new Label("Laporan Kehadiran Pegawai");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Filter controls
        HBox filterBox = createAttendanceFilterControls();

        // Attendance table
        VBox attendanceTable = createAttendanceReportTable();

        content.getChildren().addAll(title, filterBox, attendanceTable);
        contentArea.getChildren().add(content);
    }

    /**
     * Creates attendance filter controls
     */
    private HBox createAttendanceFilterControls() {
        HBox filterBox = new HBox(15);
        filterBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 15;");
        filterBox.setAlignment(Pos.CENTER_LEFT);

        Label filterLabel = new Label("Filter:");
        filterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        ComboBox<String> monthCombo = new ComboBox<>();
        monthCombo.getItems().addAll("Januari", "Februari", "Maret", "April", "Mei", "Juni",
                "Juli", "Agustus", "September", "Oktober", "November", "Desember");
        monthCombo.setValue("Juni"); // Current month

        ComboBox<String> divisionCombo = new ComboBox<>();
        divisionCombo.getItems().addAll("Semua Divisi", "Marketing", "HR", "Keuangan");
        divisionCombo.setValue("Semua Divisi");

        Button generateBtn = new Button("Generate Report");
        generateBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-padding: 8 15; -fx-background-radius: 5;");

        Button exportBtn = new Button("Export Excel");
        exportBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-padding: 8 15; -fx-background-radius: 5;");

        filterBox.getChildren().addAll(filterLabel, new Label("Bulan:"), monthCombo,
                new Label("Divisi:"), divisionCombo, generateBtn, exportBtn);
        return filterBox;
    }

    /**
     * Creates attendance report table
     */
    private VBox createAttendanceReportTable() {
        VBox tableSection = new VBox(15);
        tableSection.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label sectionTitle = new Label("Laporan Kehadiran - " + LocalDate.now().getMonth() + " " + LocalDate.now().getYear());
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TableView<Map<String, Object>> table = new TableView<>();

        TableColumn<Map<String, Object>, String> namaCol = new TableColumn<>("Nama");
        namaCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty((String) data.getValue().get("nama")));
        namaCol.setPrefWidth(150);

        TableColumn<Map<String, Object>, String> divisiCol = new TableColumn<>("Divisi");
        divisiCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty((String) data.getValue().get("divisi")));
        divisiCol.setPrefWidth(100);

        TableColumn<Map<String, Object>, String> hadirCol = new TableColumn<>("Hadir");
        hadirCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("hadir").toString()));
        hadirCol.setPrefWidth(80);

        TableColumn<Map<String, Object>, String> izinCol = new TableColumn<>("Izin");
        izinCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("izin").toString()));
        izinCol.setPrefWidth(80);

        TableColumn<Map<String, Object>, String> sakitCol = new TableColumn<>("Sakit");
        sakitCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("sakit").toString()));
        sakitCol.setPrefWidth(80);

        TableColumn<Map<String, Object>, String> lemburCol = new TableColumn<>("Lembur");
        lemburCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("lembur").toString()));
        lemburCol.setPrefWidth(80);

        TableColumn<Map<String, Object>, String> persentaseCol = new TableColumn<>("Persentase");
        persentaseCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("persentase").toString() + "%"));
        persentaseCol.setPrefWidth(100);

        TableColumn<Map<String, Object>, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> {
            double persentase = (Double) data.getValue().get("persentase");
            String status = persentase >= 80 ? "Baik" : persentase >= 60 ? "Cukup" : "Kurang";
            return new javafx.beans.property.SimpleStringProperty(status);
        });
        statusCol.setPrefWidth(80);

        table.getColumns().addAll(namaCol, divisiCol, hadirCol, izinCol, sakitCol, lemburCol, persentaseCol, statusCol);

        // Populate data
        ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();
        for (Pegawai p : DataStore.pegawaiMap.values()) {
            Map<String, Object> row = new HashMap<>();

            long hadirCount = DataStore.presensiList.stream()
                    .filter(pr -> pr.pegawaiId.equals(p.id))
                    .filter(pr -> {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(pr.tanggal);
                        return cal.get(Calendar.MONTH) + 1 == LocalDate.now().getMonthValue()
                                && cal.get(Calendar.YEAR) == LocalDate.now().getYear();
                    })
                    .filter(pr -> "Hadir".equals(pr.keterangan))
                    .count();

            long izinCount = DataStore.presensiList.stream()
                    .filter(pr -> pr.pegawaiId.equals(p.id))
                    .filter(pr -> {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(pr.tanggal);
                        return cal.get(Calendar.MONTH) + 1 == LocalDate.now().getMonthValue()
                                && cal.get(Calendar.YEAR) == LocalDate.now().getYear();
                    })
                    .filter(pr -> "Izin".equals(pr.keterangan))
                    .count();

            long sakitCount = DataStore.presensiList.stream()
                    .filter(pr -> pr.pegawaiId.equals(p.id))
                    .filter(pr -> {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(pr.tanggal);
                        return cal.get(Calendar.MONTH) + 1 == LocalDate.now().getMonthValue()
                                && cal.get(Calendar.YEAR) == LocalDate.now().getYear();
                    })
                    .filter(pr -> "Sakit".equals(pr.keterangan))
                    .count();

            long lemburCount = DataStore.presensiList.stream()
                    .filter(pr -> pr.pegawaiId.equals(p.id))
                    .filter(pr -> {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(pr.tanggal);
                        return cal.get(Calendar.MONTH) + 1 == LocalDate.now().getMonthValue()
                                && cal.get(Calendar.YEAR) == LocalDate.now().getYear();
                    })
                    .filter(pr -> "Lembur".equals(pr.keterangan))
                    .count();

            double persentase = hadirCount > 0 ? Math.round((hadirCount / 22.0) * 100 * 100.0) / 100.0 : 0;

            row.put("nama", p.nama);
            row.put("divisi", p.divisi);
            row.put("hadir", hadirCount);
            row.put("izin", izinCount);
            row.put("sakit", sakitCount);
            row.put("lembur", lemburCount);
            row.put("persentase", persentase);
            data.add(row);
        }
        table.setItems(data);
        table.setPrefHeight(400);

        tableSection.getChildren().addAll(sectionTitle, table);
        return tableSection;
    }

    /**
     * Shows leave management content for all employees
     */
    private void showLeaveManagementContent() {
        contentArea.getChildren().clear();

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label title = new Label("Manajemen Cuti Company-Wide");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Pending leaves section
        VBox pendingSection = createAllPendingLeavesSection();

        // Leave statistics
        VBox statsSection = createLeaveStatsSection();

        content.getChildren().addAll(title, pendingSection, statsSection);
        contentArea.getChildren().add(content);
    }

    /**
     * Creates pending leaves section for all employees
     */
    private VBox createAllPendingLeavesSection() {
        VBox pendingSection = new VBox(15);
        pendingSection.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label sectionTitle = new Label("Semua Pengajuan Cuti Pending");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TableView<Cuti> table = new TableView<>();

        TableColumn<Cuti, String> pegawaiCol = new TableColumn<>("Pegawai");
        pegawaiCol.setCellValueFactory(data -> {
            Pegawai p = DataStore.getPegawai(data.getValue().pegawaiId);
            return new javafx.beans.property.SimpleStringProperty(p != null ? p.nama + " (" + p.divisi + ")" : data.getValue().pegawaiId);
        });
        pegawaiCol.setPrefWidth(200);

        TableColumn<Cuti, String> keperluanCol = new TableColumn<>("Keperluan");
        keperluanCol.setCellValueFactory(new PropertyValueFactory<>("keperluan"));
        keperluanCol.setPrefWidth(150);

        TableColumn<Cuti, String> periodeCol = new TableColumn<>("Periode");
        periodeCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getFormattedDateRange()));
        periodeCol.setPrefWidth(180);

        TableColumn<Cuti, String> jumlahCol = new TableColumn<>("Jumlah Hari");
        jumlahCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getJumlahHari())));
        jumlahCol.setPrefWidth(100);

        TableColumn<Cuti, String> alasanCol = new TableColumn<>("Alasan");
        alasanCol.setCellValueFactory(new PropertyValueFactory<>("alasan"));
        alasanCol.setPrefWidth(200);

        TableColumn<Cuti, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        table.getColumns().addAll(pegawaiCol, keperluanCol, periodeCol, jumlahCol, alasanCol, statusCol);
        table.setPrefHeight(300);

        // Load all pending requests
        ObservableList<Cuti> pendingRequests = DataStore.cutiList.stream()
                .filter(c -> "Pending".equals(c.status))
                .sorted((a, b) -> a.mulai.compareTo(b.mulai))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        table.setItems(pendingRequests);

        HBox actionButtons = new HBox(10);
        Button approveBtn = new Button("Setujui");
        approveBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-padding: 8 15; -fx-background-radius: 5;");

        Button rejectBtn = new Button("Tolak");
        rejectBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-padding: 8 15; -fx-background-radius: 5;");

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-padding: 8 15; -fx-background-radius: 5;");

        approveBtn.setOnAction(e -> handleHRLeaveAction(table, true));
        rejectBtn.setOnAction(e -> handleHRLeaveAction(table, false));
        refreshBtn.setOnAction(e -> showLeaveManagementContent());

        actionButtons.getChildren().addAll(approveBtn, rejectBtn, refreshBtn);
        pendingSection.getChildren().addAll(sectionTitle, table, actionButtons);

        return pendingSection;
    }

    /**
     * Handles HR-level leave approval/rejection
     */
    private void handleHRLeaveAction(TableView<Cuti> table, boolean approve) {
        Cuti selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (approve) {
                selected.status = "Disetujui";
                selected.approvedBy = hrStaff.id;

                // Update employee's remaining leave
                Pegawai employee = DataStore.getPegawai(selected.pegawaiId);
                if (employee != null) {
                    employee.sisaCuti -= (int) selected.getJumlahHari();
                }

                showAlert(Alert.AlertType.INFORMATION, "Cuti Disetujui HR",
                        "Cuti telah disetujui oleh HR!\nPegawai: " + (employee != null ? employee.nama : "N/A") +
                                "\nSisa cuti: " + (employee != null ? employee.sisaCuti : "N/A") + " hari");
            } else {
                selected.status = "Ditolak";
                selected.approvedBy = hrStaff.id;
                showAlert(Alert.AlertType.INFORMATION, "Cuti Ditolak HR", "Cuti telah ditolak oleh HR!");
            }

            showLeaveManagementContent(); // Refresh
        } else {
            showAlert(Alert.AlertType.WARNING, "Pilihan Tidak Valid", "Pilih pengajuan cuti yang akan diproses!");
        }
    }

    /**
     * Creates leave statistics section
     */
    private VBox createLeaveStatsSection() {
        VBox statsSection = new VBox(15);
        statsSection.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label sectionTitle = new Label("Statistik Cuti");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Calculate leave statistics
        long totalPending = DataStore.cutiList.stream().filter(c -> "Pending".equals(c.status)).count();
        long totalApproved = DataStore.cutiList.stream().filter(c -> "Disetujui".equals(c.status)).count();
        long totalRejected = DataStore.cutiList.stream().filter(c -> "Ditolak".equals(c.status)).count();

        double avgLeaveDays = DataStore.cutiList.stream()
                .filter(c -> "Disetujui".equals(c.status))
                .mapToLong(Cuti::getJumlahHari)
                .average()
                .orElse(0.0);

        HBox leaveStats = new HBox(20);
        leaveStats.getChildren().addAll(
                createStatCard("Pending", String.valueOf(totalPending), "#ffc107"),
                createStatCard("Disetujui", String.valueOf(totalApproved), "#28a745"),
                createStatCard("Ditolak", String.valueOf(totalRejected), "#dc3545"),
                createStatCard("Rata-rata Hari", String.format("%.1f", avgLeaveDays), "#17a2b8")
        );

        statsSection.getChildren().addAll(sectionTitle, leaveStats);
        return statsSection;
    }

    /**
     * Shows salary report content
     */
    private void showSalaryReportContent() {
        contentArea.getChildren().clear();

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label title = new Label("Laporan Gaji & Payroll");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Payroll summary
        VBox payrollSummary = createPayrollSummarySection();

        // Detailed salary table
        VBox salaryTable = createDetailedSalaryTable();

        content.getChildren().addAll(title, payrollSummary, salaryTable);
        contentArea.getChildren().add(content);
    }

    /**
     * Creates payroll summary section
     */
    private VBox createPayrollSummarySection() {
        VBox summarySection = new VBox(15);
        summarySection.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label sectionTitle = new Label("Ringkasan Payroll Bulan " + LocalDate.now().getMonth() + " " + LocalDate.now().getYear());
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Calculate payroll totals by division
        Map<String, Double> payrollByDivision = DataStore.pegawaiMap.values().stream()
                .collect(Collectors.groupingBy(p -> p.divisi,
                        Collectors.summingDouble(Pegawai::calculateGajiBulanan)));

        double totalPayroll = payrollByDivision.values().stream().mapToDouble(Double::doubleValue).sum();

        HBox payrollStats = new HBox(20);
        payrollStats.getChildren().addAll(
                createStatCard("Total Payroll", "Rp " + String.format("%.0f", totalPayroll), "#28a745"),
                createStatCard("Marketing", "Rp " + String.format("%.0f", payrollByDivision.getOrDefault("Marketing", 0.0)), "#007bff"),
                createStatCard("HR", "Rp " + String.format("%.0f", payrollByDivision.getOrDefault("HR", 0.0)), "#6f42c1"),
                createStatCard("Keuangan", "Rp " + String.format("%.0f", payrollByDivision.getOrDefault("Keuangan", 0.0)), "#fd7e14"),
                createStatCard("Rata-rata", "Rp " + String.format("%.0f", totalPayroll / DataStore.pegawaiMap.size()), "#17a2b8")
        );

        summarySection.getChildren().addAll(sectionTitle, payrollStats);
        return summarySection;
    }

    /**
     * Creates detailed salary table
     */
    private VBox createDetailedSalaryTable() {
        VBox tableSection = new VBox(15);
        tableSection.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label sectionTitle = new Label("Detail Gaji Pegawai");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TableView<Map<String, Object>> table = new TableView<>();

        TableColumn<Map<String, Object>, String> namaCol = new TableColumn<>("Nama");
        namaCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty((String) data.getValue().get("nama")));
        namaCol.setPrefWidth(150);

        TableColumn<Map<String, Object>, String> divisiCol = new TableColumn<>("Divisi");
        divisiCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty((String) data.getValue().get("divisi")));
        divisiCol.setPrefWidth(100);

        TableColumn<Map<String, Object>, String> jabatanCol = new TableColumn<>("Jabatan");
        jabatanCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty((String) data.getValue().get("jabatan")));
        jabatanCol.setPrefWidth(150);

        TableColumn<Map<String, Object>, String> gajiPokokCol = new TableColumn<>("Gaji Pokok");
        gajiPokokCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty("Rp " + String.format("%.0f", (Double) data.getValue().get("gajiPokok"))));
        gajiPokokCol.setPrefWidth(120);

        TableColumn<Map<String, Object>, String> bonusCol = new TableColumn<>("Bonus");
        bonusCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty("Rp " + String.format("%.0f", (Double) data.getValue().get("bonus"))));
        bonusCol.setPrefWidth(120);

        TableColumn<Map<String, Object>, String> totalCol = new TableColumn<>("Total Gaji");
        totalCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty("Rp " + String.format("%.0f", (Double) data.getValue().get("total"))));
        totalCol.setPrefWidth(120);

        TableColumn<Map<String, Object>, String> kehadiranCol = new TableColumn<>("Kehadiran");
        kehadiranCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("kehadiran").toString() + " hari"));
        kehadiranCol.setPrefWidth(100);

        table.getColumns().addAll(namaCol, divisiCol, jabatanCol, gajiPokokCol, bonusCol, totalCol, kehadiranCol);

        ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();
        for (Pegawai p : DataStore.pegawaiMap.values()) {
            Map<String, Object> row = new HashMap<>();

            long hadirCount = DataStore.getKehadiranBulan(p.id, LocalDate.now().getMonthValue(), LocalDate.now().getYear());
            double attendanceBonus = hadirCount * 50000;
            double totalGaji = p.calculateGajiBulanan();

            row.put("nama", p.nama);
            row.put("divisi", p.divisi);
            row.put("jabatan", p.jabatan);
            row.put("gajiPokok", p.gajiPokok);
            row.put("bonus", attendanceBonus);
            row.put("total", totalGaji);
            row.put("kehadiran", hadirCount);
            data.add(row);
        }

        // Sort by total salary descending
        data.sort((a, b) -> Double.compare((Double) b.get("total"), (Double) a.get("total")));

        table.setItems(data);
        table.setPrefHeight(400);

        // Export button
        Button exportBtn = new Button("Export Payroll");
        exportBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-padding: 10 15; -fx-background-radius: 5;");
        exportBtn.setOnAction(e -> showAlert(Alert.AlertType.INFORMATION, "Export", "Fitur export akan segera tersedia!"));

        tableSection.getChildren().addAll(sectionTitle, table, exportBtn);
        return tableSection;
    }

    /**
     * Shows analytics content
     */
    private void showAnalyticsContent() {
        contentArea.getChildren().clear();

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));

        Label title = new Label("Analytics & Reports");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        // Company overview
        VBox companyOverview = createCompanyOverviewSection();

        // Trends and insights
        VBox trendsSection = createTrendsSection();

        content.getChildren().addAll(title, companyOverview, trendsSection);
        contentArea.getChildren().add(content);
    }

    /**
     * Creates company overview section
     */
    private VBox createCompanyOverviewSection() {
        VBox overviewSection = new VBox(15);
        overviewSection.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label sectionTitle = new Label("Company Overview");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Calculate key metrics
        long totalEmployees = DataStore.pegawaiMap.size();
        Map<String, Long> employeesByDivision = DataStore.pegawaiMap.values().stream()
                .collect(Collectors.groupingBy(p -> p.divisi, Collectors.counting()));

        Map<String, Long> employeesByRole = DataStore.pegawaiMap.values().stream()
                .collect(Collectors.groupingBy(p -> p.role, Collectors.counting()));

        // Create overview text
        StringBuilder overview = new StringBuilder();
        overview.append("Total Pegawai: ").append(totalEmployees).append("\n\n");
        overview.append("Distribusi per Divisi:\n");
        employeesByDivision.forEach((divisi, count) ->
                overview.append("• ").append(divisi).append(": ").append(count).append(" orang\n"));

        overview.append("\nDistribusi per Role:\n");
        employeesByRole.forEach((role, count) ->
                overview.append("• ").append(role.toUpperCase()).append(": ").append(count).append(" orang\n"));

        TextArea overviewText = new TextArea(overview.toString());
        overviewText.setEditable(false);
        overviewText.setPrefHeight(200);

        overviewSection.getChildren().addAll(sectionTitle, overviewText);
        return overviewSection;
    }

    /**
     * Creates trends section
     */
    private VBox createTrendsSection() {
        VBox trendsSection = new VBox(15);
        trendsSection.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 20;");

        Label sectionTitle = new Label("Trends & Insights");
        sectionTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Calculate trends
        double avgAttendanceRate = DataStore.pegawaiMap.values().stream()
                .mapToLong(p -> DataStore.getKehadiranBulan(p.id, LocalDate.now().getMonthValue(), LocalDate.now().getYear()))
                .average()
                .orElse(0.0) / 22.0 * 100;

        long highPerformers = DataStore.pegawaiMap.values().stream()
                .filter(p -> DataStore.getKehadiranBulan(p.id, LocalDate.now().getMonthValue(), LocalDate.now().getYear()) >= 20)
                .count();

        // Create insights text
        StringBuilder insights = new StringBuilder();
        insights.append("📊 Key Insights untuk ").append(LocalDate.now().getMonth()).append(" ").append(LocalDate.now().getYear()).append(":\n\n");
        insights.append("• Tingkat Kehadiran Rata-rata: ").append(String.format("%.1f%%", avgAttendanceRate)).append("\n");
        insights.append("• High Performers (≥20 hari hadir): ").append(highPerformers).append(" pegawai\n");
        insights.append("• Pendek Cuti: ").append(DataStore.cutiList.stream().filter(c -> "Pending".equals(c.status)).count()).append(" menunggu approval\n\n");

        if (avgAttendanceRate >= 80) {
            insights.append("✅ Kehadiran secara umum baik\n");
        } else if (avgAttendanceRate >= 60) {
            insights.append("⚠️ Kehadiran perlu ditingkatkan\n");
        } else {
            insights.append("❌ Kehadiran rendah - perlu tindakan khusus\n");
        }

        TextArea insightsText = new TextArea(insights.toString());
        insightsText.setEditable(false);
        insightsText.setPrefHeight(150);

        trendsSection.getChildren().addAll(sectionTitle, insightsText);
        return trendsSection;
    }

    // Personal features for HR staff
    private void showPersonalPresensiContent() {
        contentArea.getChildren().clear();
        Label placeholder = new Label("Personal Presensi - sama seperti Employee Dashboard");
        placeholder.setFont(Font.font("Arial", 16));
        contentArea.getChildren().add(placeholder);
    }

    private void showPersonalProfileContent() {
        contentArea.getChildren().clear();
        Label placeholder = new Label("Personal Profile - sama seperti Employee Dashboard");
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
package id.ac.stis.pbo.demo1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * tugas kelompok 9 anjay keren banget nich xixiix MARIO GANTENG BANGET parahhh cuyyy kece abis
 * TUGAS KELOMPOK - SISTEM MANAJEMEN PEGAWAI GAWE
 *
 * Pembagian Tugas:
 * 1. Anggota 1: HelloApplication.java (Main & Login) - FILE INI
 * 2. Anggota 2: EmployeeDashboard.java (Dashboard Pegawai)
 * 3. Anggota 3: ManagerDashboard.java (Dashboard Manajer)
 * 4. Anggota 4: HRDashboard.java (Dashboard HR)
 * 5. Anggota 5: DataModels.java (Data Classes & Store)
 */

public class HelloApplication extends Application {
    public static void main(String[] args) {
        // Initialize data store
        DataStore.init();
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Start with login page
        new LoginPage().start(stage);
    }
}

/**
 * Enhanced Login Page with Modern Design
 *
 * Features:
 * - Gradient background
 * - Modern card design
 * - Hover effects
 * - Role-based redirection
 */
class LoginPage extends Application {

    @Override
    public void start(Stage stage) {
        // Main container with gradient background
        VBox mainContainer = new VBox(20);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(40));
        mainContainer.setStyle("-fx-background: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");

        // App title
        Label titleLabel = new Label("GAWE");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setTextFill(Color.WHITE);

        Label subtitleLabel = new Label("Employee Management System");
        subtitleLabel.setFont(Font.font("Arial", 16));
        subtitleLabel.setTextFill(Color.LIGHTGRAY);

        // Login form container with card design
        VBox formContainer = createLoginForm(stage);

        // Add all components to main container
        mainContainer.getChildren().addAll(titleLabel, subtitleLabel, formContainer);

        // Create and show scene
        Scene scene = new Scene(mainContainer, 1200, 700);
        stage.setScene(scene);
        stage.setTitle("GAWE - Employee Management System");
        stage.show();
    }

    /**
     * Creates the login form with modern card design
     */
    private VBox createLoginForm(Stage stage) {
        VBox formContainer = new VBox(15);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(30));
        formContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);");
        formContainer.setMaxWidth(350);

        Label formTitle = new Label("Login");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        formTitle.setTextFill(Color.DARKSLATEGRAY);

        // Username field
        TextField userField = new TextField();
        userField.setPromptText("Username (Email)");
        userField.setStyle("-fx-pref-height: 40; -fx-font-size: 14;");

        // Password field
        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setStyle("-fx-pref-height: 40; -fx-font-size: 14;");

        // Login button with hover effects
        Button loginBtn = createLoginButton(userField, passField, stage);

        // Add help text
        Label helpLabel = new Label("Demo Accounts:\nEmployee: pegawai@gawe.com / 123\nHR: hr@gawe.com / admin\nManager: manajer@gawe.com / 456");
        helpLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 10;");

        formContainer.getChildren().addAll(formTitle, userField, passField, loginBtn, helpLabel);
        return formContainer;
    }

    /**
     * Creates login button with styling and event handling
     */
    private Button createLoginButton(TextField userField, PasswordField passField, Stage stage) {
        Button loginBtn = new Button("LOGIN");
        loginBtn.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-height: 45; -fx-pref-width: 200; -fx-background-radius: 5;");

        // Hover effects
        loginBtn.setOnMouseEntered(e ->
                loginBtn.setStyle("-fx-background-color: #5a6fd8; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-height: 45; -fx-pref-width: 200; -fx-background-radius: 5;"));
        loginBtn.setOnMouseExited(e ->
                loginBtn.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; -fx-font-weight: bold; -fx-pref-height: 45; -fx-pref-width: 200; -fx-background-radius: 5;"));

        // Login action
        loginBtn.setOnAction(e -> handleLogin(userField, passField, stage));

        return loginBtn;
    }

    /**
     * Handles login authentication and role-based redirection
     */
    private void handleLogin(TextField userField, PasswordField passField, Stage stage) {
        String username = userField.getText().trim();
        String password = passField.getText().trim();

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            showErrorAlert("Please fill in both username and password.");
            return;
        }

        // Get employee from data store
        Pegawai pegawai = DataStore.getPegawai(username);

        if (pegawai != null && pegawai.password.equals(password)) {
            // Login successful - redirect based on role
            stage.close();
            redirectToRoleDashboard(pegawai);
        } else {
            // Login failed
            showErrorAlert("Invalid username or password.\nPlease check your credentials and try again.");
        }
    }

    /**
     * Redirects user to appropriate dashboard based on their role
     */
    private void redirectToRoleDashboard(Pegawai pegawai) {
        try {
            switch (pegawai.role.toLowerCase()) {
                case "hr":
                    new HRDashboard(pegawai).start(new Stage());
                    break;
                case "manajer":
                    new ManagerDashboard(pegawai).start(new Stage());
                    break;
                default: // pegawai
                    new EmployeeDashboard(pegawai).start(new Stage());
                    break;
            }
        } catch (Exception e) {
            showErrorAlert("Error opening dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Shows error alert dialog
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText("Authentication Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
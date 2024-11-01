import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealPlanner {
    private JFrame frame;
    private JPanel loginPanel;
    private JPanel registerPanel;
    private JPanel mainPanel;
    private JPanel addRecipePanel;
    private JPanel listRecipesPanel;
    private JPanel createMealPlanPanel;

    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;

    private JTextField registerUsernameField;
    private JPasswordField registerPasswordField;
    private JTextField registerEmailField;
    private JTextField registerHeightField;
    private JTextField registerWeightField;

    private JTextField recipeTitleField;
    private JTextArea recipeDescriptionArea;
    private JTextField recipeCaloriesField;

    private List<User> users;
    private User currentUser;

    private List<Recipe> recipes;

    private Connection connection;

    public MealPlanner() {
        connectToDatabase();

        users = loadUsers();
        recipes = loadRecipes();

        initialize();
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/meal_planner";
            String user = "root"; // Replace with your MySQL username
            String password = "375674"; // Replace with your MySQL password

            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database connection failed.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private List<User> loadUsers() {
        List<User> userList = new ArrayList<>();
        try {
            String query = "SELECT username, password, email, height, weight FROM users";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                double height = rs.getDouble("height");
                double weight = rs.getDouble("weight");
                userList.add(new User(username, password, email, height, weight));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    private List<Recipe> loadRecipes() {
        List<Recipe> recipeList = new ArrayList<>();
        try {
            String query = "SELECT title, description, calories, user_id FROM recipes";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");
                int calories = rs.getInt("calories");
                recipeList.add(new Recipe(title, description, calories));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipeList;
    }

    private void initialize() {
        frame = new JFrame("Meal Planner App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.getContentPane().setBackground(Color.LIGHT_GRAY);
        showLoginPanel();
        frame.setVisible(true);
    }

    private void showLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(4, 2));
        loginPanel.setBackground(Color.WHITE);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.BLUE);
        loginUsernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.BLUE);
        loginPasswordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(Color.GREEN);
        loginButton.setForeground(Color.WHITE);
        JButton switchToRegisterButton = new JButton("Register");
        switchToRegisterButton.setBackground(Color.ORANGE);
        switchToRegisterButton.setForeground(Color.WHITE);

        loginButton.addActionListener(e -> handleLogin());
        switchToRegisterButton.addActionListener(e -> showRegisterPanel());

        loginPanel.add(usernameLabel);
        loginPanel.add(loginUsernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(loginPasswordField);
        loginPanel.add(loginButton);
        loginPanel.add(switchToRegisterButton);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(loginPanel);
        frame.validate();
        frame.repaint();
    }

    private void showRegisterPanel() {
        registerPanel = new JPanel();
        registerPanel.setLayout(new GridLayout(6, 2));
        registerPanel.setBackground(Color.WHITE);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.BLUE);
        registerUsernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.BLUE);
        registerPasswordField = new JPasswordField();

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setForeground(Color.BLUE);
        registerEmailField = new JTextField();

        JLabel heightLabel = new JLabel("Height (m):");
        heightLabel.setForeground(Color.BLUE);
        registerHeightField = new JTextField();

        JLabel weightLabel = new JLabel("Weight (kg):");
        weightLabel.setForeground(Color.BLUE);
        registerWeightField = new JTextField();

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(Color.GREEN);
        registerButton.setForeground(Color.WHITE);
        JButton switchToLoginButton = new JButton("Back to Login");
        switchToLoginButton.setBackground(Color.ORANGE);
        switchToLoginButton.setForeground(Color.WHITE);

        registerButton.addActionListener(e -> handleRegister());
        switchToLoginButton.addActionListener(e -> showLoginPanel());

        registerPanel.add(usernameLabel);
        registerPanel.add(registerUsernameField);
        registerPanel.add(passwordLabel);
        registerPanel.add(registerPasswordField);
        registerPanel.add(emailLabel);
        registerPanel.add(registerEmailField);
        registerPanel.add(heightLabel);
        registerPanel.add(registerHeightField);
        registerPanel.add(weightLabel);
        registerPanel.add(registerWeightField);
        registerPanel.add(registerButton);
        registerPanel.add(switchToLoginButton);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(registerPanel);
        frame.validate();
        frame.repaint();
    }

    private void showMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(7, 1));
        mainPanel.setBackground(Color.WHITE);

        JLabel bmiLabel = new JLabel("Your BMI: " + String.format("%.2f", currentUser.calculateBMI()));
        bmiLabel.setForeground(Color.BLUE);
        mainPanel.add(bmiLabel);

        JButton addRecipeButton = new JButton("Add Recipe");
        addRecipeButton.setBackground(Color.GREEN);
        addRecipeButton.setForeground(Color.WHITE);

        JButton listRecipesButton = new JButton("List Recipes");
        listRecipesButton.setBackground(Color.GREEN);
        listRecipesButton.setForeground(Color.WHITE);

        JButton createMealPlanButton = new JButton("Create Meal Plan");
        createMealPlanButton.setBackground(Color.GREEN);
        createMealPlanButton.setForeground(Color.WHITE);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);

        addRecipeButton.addActionListener(e -> showAddRecipePanel());
        listRecipesButton.addActionListener(e -> showListRecipesPanel());
        createMealPlanButton.addActionListener(e -> showCreateMealPlanPanel());
        logoutButton.addActionListener(e -> {
            currentUser = null;
            showLoginPanel();
        });

        mainPanel.add(addRecipeButton);
        mainPanel.add(listRecipesButton);
        mainPanel.add(createMealPlanButton);
        mainPanel.add(logoutButton);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(mainPanel);
        frame.validate();
        frame.repaint();
    }

    private void showAddRecipePanel() {
        addRecipePanel = new JPanel();
        addRecipePanel.setLayout(new GridLayout(4, 2));
        addRecipePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Recipe Title:");
        titleLabel.setForeground(Color.BLUE);
        recipeTitleField = new JTextField();

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setForeground(Color.BLUE);
        recipeDescriptionArea = new JTextArea(3, 20);

        JLabel caloriesLabel = new JLabel("Calories:");
        caloriesLabel.setForeground(Color.BLUE);
        recipeCaloriesField = new JTextField();

        JButton addRecipeButton = new JButton("Add Recipe");
        addRecipeButton.setBackground(Color.GREEN);
        addRecipeButton.setForeground(Color.WHITE);
        JButton backButton = new JButton("Back to Main");
        backButton.setBackground(Color.ORANGE);
        backButton.setForeground(Color.WHITE);

        addRecipeButton.addActionListener(e -> handleAddRecipe());
        backButton.addActionListener(e -> showMainPanel());

        addRecipePanel.add(titleLabel);
        addRecipePanel.add(recipeTitleField);
        addRecipePanel.add(descriptionLabel);
        addRecipePanel.add(new JScrollPane(recipeDescriptionArea));
        addRecipePanel.add(caloriesLabel);
        addRecipePanel.add(recipeCaloriesField);
        addRecipePanel.add(addRecipeButton);
        addRecipePanel.add(backButton);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(addRecipePanel);
        frame.validate();
        frame.repaint();
    }

    private void showListRecipesPanel() {
        listRecipesPanel = new JPanel();
        listRecipesPanel.setLayout(new BorderLayout());
        listRecipesPanel.setBackground(Color.WHITE);

        JTextArea recipeListArea = new JTextArea();
        recipeListArea.setEditable(false);
        StringBuilder recipesText = new StringBuilder("Recipes:\n");

        for (Recipe recipe : recipes) {
            recipesText.append(recipe.getTitle()).append(" - ").append(recipe.getDescription()).append(" (").append(recipe.getCalories()).append(" calories)\n");
        }
        recipeListArea.setText(recipesText.toString());
        listRecipesPanel.add(new JScrollPane(recipeListArea), BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Main");
        backButton.setBackground(Color.ORANGE);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> showMainPanel());

        listRecipesPanel.add(backButton, BorderLayout.SOUTH);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(listRecipesPanel);
        frame.validate();
        frame.repaint();
    }

    private void showCreateMealPlanPanel() {
        createMealPlanPanel = new JPanel();
        createMealPlanPanel.setLayout(new GridLayout(2, 1));
        createMealPlanPanel.setBackground(Color.WHITE);

        JLabel mealPlanLabel = new JLabel("Meal Plan:");
        mealPlanLabel.setForeground(Color.BLUE);
        JTextArea mealPlanArea = new JTextArea(5, 20);
        mealPlanArea.setEditable(false);
        
        StringBuilder mealPlanText = new StringBuilder("Suggested Meal Plan:\n");
        for (Recipe recipe : recipes) {
            mealPlanText.append(recipe.getTitle()).append(" - ").append(recipe.getCalories()).append(" calories\n");
        }
        mealPlanArea.setText(mealPlanText.toString());

        JButton backButton = new JButton("Back to Main");
        backButton.setBackground(Color.ORANGE);
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> showMainPanel());

        createMealPlanPanel.add(mealPlanLabel);
        createMealPlanPanel.add(new JScrollPane(mealPlanArea));
        createMealPlanPanel.add(backButton);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(createMealPlanPanel);
        frame.validate();
        frame.repaint();
    }

    private void handleLogin() {
        String username = loginUsernameField.getText();
        String password = new String(loginPasswordField.getPassword());

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                showMainPanel();
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
    }

    private void handleRegister() {
        String username = registerUsernameField.getText();
        String password = new String(registerPasswordField.getPassword());
        String email = registerEmailField.getText();
        double height = Double.parseDouble(registerHeightField.getText());
        double weight = Double.parseDouble(registerWeightField.getText());

        try {
            String query = "INSERT INTO users (username, password, email, height, weight) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setDouble(4, height);
            pstmt.setDouble(5, weight);
            pstmt.executeUpdate();

            users.add(new User(username, password, email, height, weight));
            JOptionPane.showMessageDialog(frame, "Registration successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
            showLoginPanel();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Registration failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAddRecipe() {
        String title = recipeTitleField.getText();
        String description = recipeDescriptionArea.getText();
        int calories = Integer.parseInt(recipeCaloriesField.getText());

        try {
            String query = "INSERT INTO recipes (title, description, calories, user_id) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setInt(3, calories);
            pstmt.setInt(4, users.indexOf(currentUser) + 1); // Assuming user IDs are sequential starting from 1
            pstmt.executeUpdate();

            recipes.add(new Recipe(title, description, calories));
            JOptionPane.showMessageDialog(frame, "Recipe added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            showMainPanel();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to add recipe: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MealPlanner());
    }
}

class User {
    private String username;
    private String password;
    private double height;
    private double weight;

    public User(String username, String password, String email, double height, double weight) {
        this.username = username;
        this.password = password;
        this.height = height;
        this.weight = weight;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public double calculateBMI() {
        return weight / (height * height);
    }
}

class Recipe {
    private String title;
    private String description;
    private int calories;

    public Recipe(String title, String description, int calories) {
        this.title = title;
        this.description = description;
        this.calories = calories;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }
}

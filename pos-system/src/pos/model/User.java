package pos.model;

public class User {
    private String id;
    private String username;
    private String password;
    private String role; // "ADMIN" or "CASHIER"
    private String fullName;

    public User(String id, String username, String password, String role, String fullName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getFullName() { return fullName; }
    public void setPassword(String password) { this.password = password; }

    public boolean isAdmin() { return "ADMIN".equals(role); }

    @Override
    public String toString() {
        return String.format("%-6s %-15s %-10s %s", id, username, role, fullName);
    }
}

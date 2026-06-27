package pos.service;

import pos.model.User;

import java.util.*;

public class AuthService {
    private Map<String, User> users = new LinkedHashMap<>();
    private User currentUser = null;
    private int idCounter = 10;

    public AuthService() {
        users.put("admin",   new User("U001", "admin",   "admin123",  "ADMIN",   "Administrator"));
        users.put("cashier1",new User("U002", "cashier1","cash1234",  "CASHIER", "Alice Johnson"));
        users.put("cashier2",new User("U003", "cashier2","cash5678",  "CASHIER", "Bob Smith"));
    }

    public boolean login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public void logout() { currentUser = null; }

    public User getCurrentUser() { return currentUser; }

    public boolean isLoggedIn() { return currentUser != null; }

    public boolean isAdmin() { return currentUser != null && currentUser.isAdmin(); }

    public List<User> getAllUsers() { return new ArrayList<>(users.values()); }

    public boolean addUser(String username, String password, String role, String fullName) {
        if (users.containsKey(username)) return false;
        String id = "U" + String.format("%03d", ++idCounter);
        users.put(username, new User(id, username, password, role, fullName));
        return true;
    }

    public boolean removeUser(String username) {
        if ("admin".equals(username)) return false;
        return users.remove(username) != null;
    }

    public boolean changePassword(String username, String newPassword) {
        User u = users.get(username);
        if (u == null) return false;
        u.setPassword(newPassword);
        return true;
    }
}

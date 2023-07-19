package controller;

import model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserController {
    private static ArrayList<User> users = new ArrayList<>();
    private static User currentUser;


    public static boolean validateLoginForm(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        if (username.equals("") || password.equals("")) {
            return false;
        }
        User user = getUserByUsername(username);
        if (user == null) {
            return false;
        }
        if (!user.isPasswordCorrect(password)) {
            return false;
        }
        return true;
    }

    public static boolean validateRegisterForm(String username, String password, String rePass) {
        if (username == null || password == null || rePass == null) {
            return false;
        }
        if (username.equals("") || password.equals("") || rePass.equals("")) {
            return false;
        }
        if (getUserByUsername(username) != null) {
            return false;
        }
        if (checkPasswordPower(password)) {
            return false;
        }
        if (!password.equals(rePass)) {
            return false;
        }
        return true;
    }

    public static boolean validateChangePassForm(String oldPass, String password, String rePass) {
        if (password == null || rePass == null || oldPass == null) {
            return false;
        }
        if (password.equals("") || rePass.equals("") || oldPass.equals("")) {
            return false;
        }
        if (!currentUser.isPasswordCorrect(oldPass)) {
            return false;
        }
        if (currentUser.isPasswordCorrect(password)) {
            return false;
        }
        if (checkPasswordPower(password)) {
            return false;
        }
        if (!password.equals(rePass)) {
            return false;
        }
        return true;
    }

    public static boolean validateChangeUsernameForm(String username) {
        if (username == null || username.equals("")) {
            return false;
        }
        if (!username.equals(currentUser.getUsername()) && getUserByUsername(username) != null) {
            return false;
        }
        return true;
    }

    public static String validateChangeUsername(String username) {
        if (username == null || username.equals("")) {
            return "error: username field is required!";
        }
        if (!username.equals(currentUser.getUsername()) && getUserByUsername(username) != null) {
            return "error: user with this username already exists!";
        }
        return "";
    }

    public static void changeUsername(String username) {
        if (validateChangeUsernameForm(username)) {
            currentUser.setUsername(username);
            DBController.saveCurrentUser();
            DBController.saveAllUsers();
        }
    }

    public static void changePassword(String password, String oldPass) {
        if (validateChangePassForm(oldPass, password, password)) {
            currentUser.setPassword(password);
            DBController.saveCurrentUser();
            DBController.saveAllUsers();
        }
    }

    public static String validateRegisterUsername(String username) {
        if (username == null || username.length() == 0) {
            return "error: username field is required!";
        }
        if (getUserByUsername(username) != null) {
            return "error: user with this username already exists!";
        }
        return "";
    }

    public static String validateRegisterPassword(String password) {
        if (password == null || password.length() == 0) {
            return "error: password field is required!";
        }
        if (checkPasswordPower(password)) {
            return "error: password is weak!";
        }
        return "";
    }

    public static String validateChangePassPassword(String password) {
        if (password == null || password.length() == 0) {
            return "error: password field is required!";
        }
        if (checkPasswordPower(password)) {
            return "error: password is weak!";
        }
        if (currentUser.isPasswordCorrect(password)) {
            return "error: This is your current password!";
        }
        return "";
    }

    public static String validateChangePassOldPasswordAfterSubmit(String password) {
        if (password == null || password.length() == 0) {
            return "error: current password field is required!";
        }
        if (!currentUser.isPasswordCorrect(password)) {
            return "error: current password is not correct!";
        }
        return "";
    }

    public static String validateChangePassOldPassword(String password) {
        if (password == null || password.length() == 0) {
            return "error: current password field is required!";
        }
        return "";
    }

    public static String validateRepeatPassword(String password, String rePass) {
        if (rePass == null || rePass.equals("")) {
            return "error: repeat password field is required!";
        }
        if (!rePass.equals(password)) {
            return "error: passwords are not same!";
        }
        return "";
    }

    public static void register(String username, String password) {
        if (validateRegisterForm(username, password, password)) {
            User user = new User(username, password);
            users.add(user);
            DBController.saveAllUsers();
            currentUser = getUserByUsername(username);
            DBController.saveCurrentUser();
        }
    }

    public static void login(String username, String password) {
        if (validateLoginForm(username, password)) {
            currentUser = getUserByUsername(username);
            DBController.saveCurrentUser();
        }
    }

    public static String validateLoginUsername(String username) {
        if (username == null || username.length() == 0) {
            return "error: username field is required!";
        }
        return "";
    }

    public static String validateLoginPassword(String password) {
        if (password == null || password.length() == 0) {
            return "error: password field is required!";
        }
        return "";
    }

    public static String validateLoginPasswordAfterSubmit(String username, String password) {
        if (password == null || password.length() == 0) {
            return "error: password field is required!";
        }
        User user = getUserByUsername(username);
        if (user != null && !user.isPasswordCorrect(password)) {
            return "error: password isn't correct!";
        }
        return "";
    }

    public static String validateLoginUsernameAfterSubmit(String username) {
        if (username == null || username.length() == 0) {
            return "error: username field is required!";
        }
        if (getUserByUsername(username) == null) {
            return "error: no user with this username exists!";
        }
        return "";
    }

    public static User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static boolean checkPasswordPower(String password) {
        if (password.length() < 6) {
            return true;
        }

        Pattern pattern1 = Pattern.compile("[a-z]");
        Pattern pattern2 = Pattern.compile("[A-Z]");
        Pattern pattern3 = Pattern.compile("\\d");
        Pattern pattern4 = Pattern.compile("[^\\da-zA-Z]");
        Matcher matcher1 = pattern1.matcher(password);
        Matcher matcher2 = pattern2.matcher(password);
        Matcher matcher3 = pattern3.matcher(password);
        Matcher matcher4 = pattern4.matcher(password);
        return !(matcher1.find() && matcher2.find() && matcher3.find() && matcher4.find());
    }

    public static String convertPasswordToHash(String password) {
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void setUsers(ArrayList<User> users) {
        UserController.users = users;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        UserController.currentUser = currentUser;
    }

    public static String generateUsername() {
        char[] allCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        char[] username = new char[5];
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int selectedChar = random.nextInt(allCharacters.length);
            username[i] = allCharacters[selectedChar];
        }
        if (getUserByUsername(Arrays.toString(username)) != null) {
            return generateUsername();
        }
        return "guest-" + new String(username);
    }


    public static ArrayList<User> getSortedListOfUsers() {
        ArrayList<User> sortedList = new ArrayList<>(users);
        sortedList.sort((user1, user2) -> {
            if (user1.getScore() > user2.getScore()) {
                return -1;
            }
            if (user1.getScore() < user2.getScore()) {
                return 1;
            }
            if (user1.getTime() > user2.getTime()) {
                return -1;
            }
            if (user1.getTime() < user2.getTime()) {
                return 1;
            }
            if (user1.getLevel() > user2.getLevel()) {
                return -1;
            }
            if (user1.getLevel() < user2.getLevel()) {
                return 1;
            }
            return user1.getUsername().compareTo(user2.getUsername());
        });
        return sortedList;
    }
    public static ArrayList<User> getSortedListOfUsers(int level) {
        if (level == 0){
            return getSortedListOfUsers();
        }
        ArrayList<User> sortedList = new ArrayList<>();
        for (User user:users){
            if (user.getLevel() == level){
                sortedList.add(user);
            }
        }
        sortedList.sort((user1, user2) -> {
            if (user1.getScore() > user2.getScore()) {
                return -1;
            }
            if (user1.getScore() < user2.getScore()) {
                return 1;
            }
            if (user1.getTime() > user2.getTime()) {
                return -1;
            }
            if (user1.getTime() < user2.getTime()) {
                return 1;
            }
            if (user1.getLevel() > user2.getLevel()) {
                return -1;
            }
            if (user1.getLevel() < user2.getLevel()) {
                return 1;
            }
            return user1.getUsername().compareTo(user2.getUsername());
        });
        return sortedList;
    }
}

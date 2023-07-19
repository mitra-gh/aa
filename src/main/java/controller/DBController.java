package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import enumoration.Paths;
import model.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DBController {


    // load & save users from files
    public static void loadAllUsers(){
        try {
            Gson gson = new Gson();
            checkFileExist(Paths.USERS_PATH.getPath());
            String text = new String(Files.readAllBytes(Path.of(Paths.USERS_PATH.getPath())));
            ArrayList<User> allUsers = gson.fromJson(text, new TypeToken<List<User>>(){}.getType());
            UserController.setUsers(allUsers);
            if (allUsers == null) {
                UserController.setUsers(new ArrayList<>());
            }
        } catch (IOException e) {
            System.out.println("An error occurred.[load users]");
            e.printStackTrace();
        }
    }

    public static void saveAllUsers(){

        try {
            checkFileExist(Paths.USERS_PATH.getPath());
            File file = new File(Paths.USERS_PATH.getPath());
            FileWriter fileWriter = new FileWriter(file);
            if(UserController.getUsers() != null){
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(UserController.getUsers());
                fileWriter.write(json);
            }else {
                fileWriter.write("");
            }
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.[save users]");
            e.printStackTrace();
        }
    }

    public static void loadCurrentUser(){
        try {
            Gson gson = new Gson();
            checkFileExist(Paths.CURRENT_USER_PATH.getPath());
            String text = new String(Files.readAllBytes(Path.of(Paths.CURRENT_USER_PATH.getPath())));
            User user = gson.fromJson(text, User.class);
            if (user != null) {
                User currentUser = UserController.getUserByUsername(user.getUsername());
                UserController.setCurrentUser(currentUser);
                assert currentUser != null;
                MainController.game = currentUser.getLastGame();
            } else {
                UserController.setCurrentUser(null);
            }
        } catch (IOException e) {
            System.out.println("An error occurred.[load current user]");
            e.printStackTrace();
        }
    }

    public static void saveCurrentUser(){
        try {
            checkFileExist(Paths.CURRENT_USER_PATH.getPath());
            File file = new File(Paths.CURRENT_USER_PATH.getPath());
            FileWriter fileWriter = new FileWriter(file);
            if(UserController.getCurrentUser() != null){
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(UserController.getCurrentUser());
                fileWriter.write(json);
            }else {
                fileWriter.write("");
            }
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.[save current user]");
            e.printStackTrace();
        }
    }

    public static void checkFileExist(String fileAddress){
        try {
            File myObj = new File(fileAddress);
            boolean check = myObj.createNewFile();
        } catch (IOException e) {
            System.out.println("An error occurred.[check file exist]");
            e.printStackTrace();
        }
    }
}

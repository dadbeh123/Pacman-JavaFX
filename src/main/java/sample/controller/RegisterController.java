package sample.controller;

import sample.model.User;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.*;

public class RegisterController {
    private static RegisterController instance = null;
    public static User currentUser = null;

    public static RegisterController getInstance() {
        if (instance == null)
            instance = new RegisterController();
        return instance;
    }

    public boolean registerNewUser(String username, String password) throws IOException {
        File file = new File("resources/users/" + username + ".json");
        if (file.exists())
            return false;
        else {
            FileWriter fileWriter = new FileWriter(file);
            User user = new User(username, password);
            Gson gson = new Gson();
            gson.toJson(user, fileWriter);
            fileWriter.close();
            return true;
        }
    }

    public String loginUser(String username, String password) throws IOException {
        File file = new File("resources/users/" + username + ".json");
        if (!file.exists())
            return "No such user was found";
        FileReader fileReader = new FileReader(file);
        GsonBuilder gsonBuilder = new GsonBuilder();
        User user = gsonBuilder.create().fromJson(fileReader, User.class);
        fileReader.close();
        if (!user.getPassword().equals(password))
            return "Wrong password";
        currentUser = user;
        return "Logged in successfully";
    }

    public String changePassword(String newPass) throws IOException {
        if (!checkFormat(newPass))
            return "Wrong password format";
        currentUser.setPassword(newPass);
        File file = new File("resources/users/" + currentUser.getUsername() + ".json");
        FileWriter fileWriter = new FileWriter(file);
        Gson gson = new Gson();
        gson.toJson(currentUser, fileWriter);
        fileWriter.close();
        return "Password got changed";
    }

    public void deleteUser() {
        File file = new File("resources/users/" + currentUser.getUsername() + ".json");
        file.delete();
        currentUser = null;
    }

    public boolean checkFormat(String password) {
        Pattern pattern = Pattern.compile("^\\S+$");
        Matcher passMatcher = pattern.matcher(password);
        return passMatcher.find();
    }

    public void updateScore(int score) throws IOException {
        currentUser.increaseScore(score);
        currentUser.setLastScoreChange();
        File file = new File("resources/users/" + currentUser.getUsername() + ".json");
        FileWriter fileWriter = new FileWriter(file);
        Gson gson = new Gson();
        gson.toJson(currentUser, fileWriter);
        fileWriter.close();
    }
}

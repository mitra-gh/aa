package controller;

import model.Game;

public class MainController {
    public static Game game = new Game();
    public static void loadGame() {
        DBController.loadAllUsers();
        DBController.loadCurrentUser();
    }
}

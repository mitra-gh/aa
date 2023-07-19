package enumoration;

import controller.MainController;

public enum Translate {
    START("start", "شروع"),
    UPLOAD("upload", "آپلود"),
    LOGOUT("logout", "خروج"),
    DELETE_ACCOUNT("delete account", "حذف اکانت"),
    CHANGE_USERNAME("change username", "تغییر نام کاربری"),
    CHANGE_PASS("change password", "تفییر رمز عبور"),
    AVATAR("avatar", "آواتار"),
    BACK("back", "بازگشت"),
    SAVE("save", "ذخیره"),
    PASSWORD("password", "رمزعبور"),
    REPEAT_PASSWORD("repeat password", "تکرار رمز عبور"),
    SETTING("setting", "تنظیمات"),
    HARD("hard", "سخت"),
    AVERAGE("average", "متوسط"),
    EASY("easy", "آسان"),
    HARDSHIP_LEVEL("hardship level", "سطح دشواری"),

    SOLITUDE("solitude", "خلوت"),
    DENSE("dense", "شلوغ"),
    MESSY("messy", "آشفته"),
    PLAN("plan", "نقشه"),


    AUDIO_SOUND("audio setting", "تنظیم صدا"),
    PERSIAN("persian", "فارسی"),
    COUNT_OF_BALL("count of ball", "تعداد توپ"),
    BLACK_WHITE("black white screen", "صفحه سیاه سفید "),
    CURRENT_PASSWORD("current password", "رمز عبور کنونی"),
    SELECT_MUSIC("select music", "انتخاب موزیک"),
    REMIND_BALL("remind ball", "توپ های باقی مانده"),
    SCORE("score", "امتیاز"),
    PHASE("phase", "فاز"),
    DEGREE("degree", "درجه"),
    YES("yes", "بله"),
    NO("no", "نه"),
    RANKING("ranking", "رده بندی"),
    HOME("home", "خانه"),
    TIME("time", "زمان"),
    CONTINUE("continue", "ادامه"),
    LEVEL("level", "درجه سختی"),
    NO_USER("there is no user", "کاربری وجود ندارد"),
    SAVE_DIALOG("do you want to save the game?", "آیا قصد ذخیره بازی را دارید؟"),
    RESTART("restart", "شروع بازی"),
    RESUME("resume", "ادامه"),
    WIN("win!", "برنده شده شدی!"),
    GUIDE("guide", "راهنما"),
    SHOOTING("shooting", "پرتاب"),
    ICE_STATE("ice state(when progressbar is full)", "حالت یخ (بعد از پر شدن)"),
    MOVING_RIGHT("move right(phase4)", "حرکت به راست (فاز 4)"),
    MOVING_LEFT("move left(phase4)", "حرکت به چپ (فاز 4)"),
    LOSE("lose!", "باختی!"),
    EXIT("exit", "خروج!"),
    DO_YOU_WANT_TO_EXIT("do you want to exit?", "می خوای از برنامه خارج شوید؟"),
    ARE_YOU_SURE_LOGOUT("are you sure want to logout account?", "مطمئن هستید که می خواهید از حساب کاربری خود خارج شوید؟"),
    ARE_YOU_SURE_ِDELETE("are you sure want to delete account?", "مطمئن هستید که می خواهید حساب کاربری خود را پاک کنید؟"),
    CONTINUE_GAME_QUESTION("do you want to continue the last game?", "آیا می خواهید آخرین بازی ذخیره شده خود را ادامه دهید؟"),
    CONTINUE_GAME("continue game", "ادامه بازی"),
    USERNAME("username", "نام کاربری");
    private final String english;
    private final String persian;

    Translate(String english, String persian) {
        this.english = english;
        this.persian = persian;
    }

    public String getString() {
        if (MainController.game.isPersian()) {
            return persian;
        }
        return english;
    }
}

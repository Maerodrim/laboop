package ru.ssau.tk.itenion.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class Init extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public static void disableWarning() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            Unsafe u = (Unsafe) theUnsafe.get(null);

            Class cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field logger = cls.getDeclaredField("logger");
            u.putObjectVolatile(cls, u.staticFieldOffset(logger), null);
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(IO.FXML_PATH + "tab.fxml"));
        Parent root = loader.load();
        TabController controller = loader.getController();
        controller.setStage(primaryStage);

        disableWarning();

        primaryStage.setTitle("Itenion");
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(450);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}
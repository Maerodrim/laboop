package ru.ssau.tk.sergunin.lab;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.ui.TableWindows;
import ru.ssau.tk.sergunin.lab.functions.factory.ArrayTabulatedFunctionFactory;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage){
        new TableWindows(stage, new ArrayTabulatedFunctionFactory());
    }

    /*@Override
    public void start(final Stage primaryStage) {

        MenuBar menuBar = new MenuBar();
        Label label = new Label("");
        Label label2 = new Label("Здесь могла быть ваша реклама!!!");
        // Use the font method of the Font class
        label2.setFont(Font.font("Cambria", 32));
        label2.setTextFill(Color.web("#0076a3"));
        label2.setCenterShape(true);

        String url = "https://static.tildacdn.com/tild3837-3835-4737-a165-376136313332/_111.png";
        boolean backgroundLoading = true;

        // The image is being loaded in the background
        Image image = new Image(url, backgroundLoading);

        ImageView imageView = new ImageView(image);

        label.setGraphic(imageView);
        // Create menus
        Menu fileMenu = new Menu("File");
        Menu tableMenu = new Menu("Table");

        // Create MenuItems
        MenuItem newItem = new MenuItem("New Function");
        MenuItem openFileItem = new MenuItem("Load Function");
        MenuItem table = new MenuItem("Table");


        // Add menuItems to the Menus
        fileMenu.getItems().addAll(newItem, openFileItem);
        tableMenu.getItems().addAll(table);
        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, tableMenu);

        newItem.setOnAction(event -> functions.newFunction(primaryStage));

        openFileItem.setOnAction(event -> {
            try {
                TableWindows tableFunction = new TableWindows();
                tableFunction.table(primaryStage, functions.loadFunctionAs(Dialog.showInputTextDialog()));
            } catch (Exception e) {
                ErrorWindows errorWindows = new ErrorWindows();
                errorWindows.showError(e);
            }
        });

        table.setOnAction(event -> {
            try {
                TableWindows tableWindows = new TableWindows();
                tableWindows.table(primaryStage, functions.loadFunction());
            } catch (Exception e) {
                ErrorWindows errorWindows = new ErrorWindows();
                errorWindows.showError(e);
            }
        });

        BorderPane root2 = new BorderPane();
        root2.setTop(menuBar);
        root2.setCenter(label);
        root2.setBottom(label2);
        StackPane root = new StackPane();
        root.getChildren().addAll(root2);

        Scene scene = new Scene(root, 700, 500);

        primaryStage.setTitle("Main");
        primaryStage.setScene(scene);
        primaryStage.show();
    }*/

}

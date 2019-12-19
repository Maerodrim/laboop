package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.ArrayTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.StrictTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.UnmodifiableTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.io.FunctionsIO;
import ru.ssau.tk.sergunin.lab.ui.AlertWindows;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;

public class Functions {
    TabulatedFunctionFactory factory;

    Functions(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public static File load(Stage stage, String defaultPath) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load function");
        fileChooser.setInitialDirectory(new File(defaultPath));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"),
                new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml")
        );
        return fileChooser.showOpenDialog(stage);
    }

    public static File save(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save function");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"),
                new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml")
        );
        return fileChooser.showSaveDialog(stage);
    }

    public static <T> T initializeModalityWindow(String pathFXML, Class<T> controllerClass) {
        FXMLLoader loader;
        Parent createNewFunction = null;
        T modalityWindow = null;
        try {
            modalityWindow = controllerClass.getDeclaredConstructor().newInstance();
            loader = new FXMLLoader(Paths.get(pathFXML).toUri().toURL());
            createNewFunction = loader.load();
            modalityWindow = loader.getController();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | IOException e) {
            e.printStackTrace();
        }
        Stage createNewFunctionStage = new Stage();
        createNewFunctionStage.setScene(new Scene(createNewFunction));
        createNewFunctionStage.initModality(Modality.WINDOW_MODAL);
        try {
            controllerClass.getMethod("setStage", Stage.class).invoke(modalityWindow, createNewFunctionStage);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return modalityWindow;
    }

    private TabulatedFunction unwrap(TabulatedFunction function) {
        boolean isStrict = function.isStrict();
        boolean isUnmodifiable = function.isUnmodifiable();
        if (isUnmodifiable && isStrict) {
            function = function.unwrap().unwrap();
        } else if (isUnmodifiable || isStrict) {
            function = function.unwrap();
        }
        function.offerStrict(isStrict);
        function.offerUnmodifiable(isUnmodifiable);
        return function;
    }

    private TabulatedFunction wrap(TabulatedFunction function) {
        boolean isStrict = function.isStrict();
        boolean isUnmodifiable = function.isUnmodifiable();
        if (isUnmodifiable && isStrict) {
            return new UnmodifiableTabulatedFunction(new StrictTabulatedFunction(function));
        } else if (isUnmodifiable) {
            return new UnmodifiableTabulatedFunction(function);
        } else if (isStrict) {
            return new StrictTabulatedFunction(function);
        } else {
            return function;
        }
    }

    public void saveFunctionAs(File file, TabulatedFunction function) {
        switch (file.getPath().split("(?=[.])")[1]) {
            case ".json" -> {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    FunctionsIO.serializeJson(writer, unwrap(function));
                } catch (IOException e) {
                    AlertWindows.showError(e);
                }
            }
            case ".xml" -> {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    FunctionsIO.serializeXml(writer, unwrap(function));
                } catch (IOException e) {
                    AlertWindows.showError(e);
                }
            }
            case ".txt" -> {
                try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                    FunctionsIO.writeTabulatedFunction(outputStream, function);
                } catch (IOException e) {
                    AlertWindows.showError(e);
                }
            }
        }
    }

    public TabulatedFunction loadFunction() {
        return loadFunctionAs(new File("temp.txt"));
    }

    public TabulatedFunction loadFunctionAs(File file) {
        TabulatedFunction function = null;
        switch (file.getPath().split("(?=[.])")[1]) {
            case ".json" -> {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    function = wrap(FunctionsIO.deserializeJson(reader, factory.getTabulatedFunctionClass()));
                } catch (IOException e) {
                    AlertWindows.showError(e);
                }
            }
            case ".xml" -> {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    function = wrap(FunctionsIO.deserializeXml(reader, factory.getTabulatedFunctionClass()));
                } catch (IOException e) {
                    AlertWindows.showError(e);
                }
            }
            case ".txt" -> {
                try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
                    function = wrap(FunctionsIO.readTabulatedFunction(inputStream, factory));
                } catch (IOException e) {
                    AlertWindows.showError(e);
                }
            }
        }
        return function;
    }

   /* public TabulatedFunction insert(double x, double y) {
        //TabulatedFunction function = loadFunction();
        //String functionClassName = function.getClass().getSimpleName();
        *//*if (functionClassName.equals("ArrayTabulatedFunction") || functionClassName.equals("LinkedListTabulatedFunction")) {
            ((ArrayTabulatedFunction) function).insert(x, y);
        } else {
            AlertWindows.showError(new UnsupportedOperationException());
        }*//*
        //saveFunction(function);
        //return function;
    }*/

    public TabulatedFunction remove(double x) {
        TabulatedFunction function = loadFunction();
        String functionClassName = function.getClass().getSimpleName();
        if (functionClassName.equals("ArrayTabulatedFunction") || functionClassName.equals("LinkedListTabulatedFunction")) {
            ((ArrayTabulatedFunction) function).remove(function.indexOfX(x));
        } else {
            AlertWindows.showError(new UnsupportedOperationException());
        }
        //saveFunction(function);
        return function;
    }
}

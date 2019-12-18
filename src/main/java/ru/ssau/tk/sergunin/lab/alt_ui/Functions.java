package ru.ssau.tk.sergunin.lab.alt_ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.ArrayTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.io.FunctionsIO;
import ru.ssau.tk.sergunin.lab.ui.AlertWindows;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;

public class Functions {
    TabulatedFunctionFactory factory;
    TabulatedFunction function;

    Functions(TabulatedFunctionFactory factory, TabulatedFunction function) {
        this.factory = factory;
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

    public void saveFunction(TabulatedFunction function) {
        saveFunctionAs(new File("temp.txt"), function);
    }

    public void saveFunctionAs(File file, TabulatedFunction function) {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            FunctionsIO.writeTabulatedFunction(outputStream, function);
        } catch (IOException e) {
            AlertWindows.showError(e);
        }
    }

    public TabulatedFunction loadFunction() {
        return loadFunctionAs(new File("temp.txt"));
    }

    public TabulatedFunction loadFunctionAs(File file) {
        TabulatedFunction function = null;
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            function = FunctionsIO.readTabulatedFunction(inputStream, factory);
        } catch (IOException e) {
            AlertWindows.showError(e);
        }
        return function;
    }

    public TabulatedFunction insert(double x, double y) {
        //TabulatedFunction function = loadFunction();
        //String functionClassName = function.getClass().getSimpleName();
        /*if (functionClassName.equals("ArrayTabulatedFunction") || functionClassName.equals("LinkedListTabulatedFunction")) {
            ((ArrayTabulatedFunction) function).insert(x, y);
        } else {
            AlertWindows.showError(new UnsupportedOperationException());
        }*/
        saveFunction(function);
        return function;
    }

    public TabulatedFunction remove(double x) {
        TabulatedFunction function = loadFunction();
        String functionClassName = function.getClass().getSimpleName();
        if (functionClassName.equals("ArrayTabulatedFunction") || functionClassName.equals("LinkedListTabulatedFunction")) {
            ((ArrayTabulatedFunction) function).remove(function.indexOfX(x));
        } else {
            AlertWindows.showError(new UnsupportedOperationException());
        }
        saveFunction(function);
        return function;
    }
}

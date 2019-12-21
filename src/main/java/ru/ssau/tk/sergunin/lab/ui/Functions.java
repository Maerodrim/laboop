package ru.ssau.tk.sergunin.lab.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.ssau.tk.sergunin.lab.functions.StrictTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.TabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.UnmodifiableTabulatedFunction;
import ru.ssau.tk.sergunin.lab.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.sergunin.lab.io.FunctionsIO;

import java.io.*;
import java.nio.file.Paths;

class Functions {
    static final String FXML_PATH = "src/main/java/ru/ssau/tk/sergunin/lab/ui/fxml/";
    private TabulatedFunctionFactory factory;

    Functions(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    static File load(Stage stage, String defaultPath) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load function");
        fileChooser.setInitialDirectory(new File(defaultPath));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Function files (*.fnc)", "*.fnc"),
                new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"),
                new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml")
        );
        return fileChooser.showOpenDialog(stage);
    }

    static File save(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save function");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Function files (*.fnc)", "*.fnc"),
                new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"),
                new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml")
        );
        return fileChooser.showSaveDialog(stage);
    }

    static <T extends Openable> T initializeModalityWindow(String pathFXML, T modalityWindow) {
        FXMLLoader loader;
        Parent createNewFunction = null;
        try {
            loader = new FXMLLoader(Paths.get(pathFXML).toUri().toURL());
            createNewFunction = loader.load();
            modalityWindow = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage createNewFunctionStage = new Stage();
        createNewFunctionStage.setScene(new Scene(createNewFunction));
        createNewFunctionStage.initModality(Modality.APPLICATION_MODAL);
        modalityWindow.setStage(createNewFunctionStage);
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

    TabulatedFunction wrap(TabulatedFunction function) {
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

    void saveFunctionAs(File file, TabulatedFunction function) {
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
            case ".fnc" -> {
                try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                    FunctionsIO.writeTabulatedFunction(outputStream, function);
                } catch (IOException e) {
                    AlertWindows.showError(e);
                }
            }
        }
    }

    TabulatedFunction loadFunctionAs(File file) {
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
            case ".fnc" -> {
                try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
                    function = wrap(FunctionsIO.readTabulatedFunction(inputStream, factory));
                } catch (IOException e) {
                    AlertWindows.showError(e);
                }
            }
        }
        return function;
    }

}

package ru.ssau.tk.itenion.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.atteo.classindex.ClassIndex;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.StrictTabulatedFunction;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.UnmodifiableTabulatedFunction;
import ru.ssau.tk.itenion.io.FunctionsIO;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class IO {
    static final String FXML_PATH = "fxml/";
    static final String DEFAULT_DIRECTORY = System.getenv("APPDATA") + "\\tempFunctions"; // будеть работать только
    private static Collection<FileChooser.ExtensionFilter> EXTENSION_FILTERS = List.of(
            new FileChooser.ExtensionFilter("Function files (*.fnc)", "*.fnc"),
            new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"),
            new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
    private static Pattern FILE_EXTENSION_PATTERN = Pattern.compile(".*\\.(...)");
    // на компьютерах под управлением OS Windows 7/8/8.1/10
    private final TabulatedFunctionFactory factory;

    IO(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    static File load(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load function");
        fileChooser.setInitialDirectory(new File(IO.DEFAULT_DIRECTORY));
        fileChooser.getExtensionFilters().addAll(EXTENSION_FILTERS);
        return fileChooser.showOpenDialog(stage);
    }

    static File save(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save function");
        fileChooser.getExtensionFilters().addAll(EXTENSION_FILTERS);
        return fileChooser.showSaveDialog(stage);
    }

    static <T extends Openable> T initializeModalityWindow(String pathFXML, T modalityWindow) {
        FXMLLoader loader;
        Parent createNewFunction;
        Stage createNewFunctionStage = new Stage();
        try {
            loader = new FXMLLoader(modalityWindow.getClass().getClassLoader().getResource(pathFXML));
            createNewFunction = loader.load();
            modalityWindow = loader.getController();
            createNewFunctionStage.setScene(new Scene(createNewFunction));
            createNewFunctionStage.initModality(Modality.APPLICATION_MODAL);
            modalityWindow.setStage(createNewFunctionStage);
        } catch (IOException e) {
            AlertWindows.showError(e);
        }
        return modalityWindow;
    }

    public static Map[] initializeMap(Map<Method, Class<?>> classes, Map<String, Method> map, Item item) {
        StreamSupport.stream(ClassIndex.getAnnotated(ConnectableItem.class).spliterator(), false)
                .filter(f -> f.getDeclaredAnnotation(ConnectableItem.class).type() == item)
                .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(ConnectableItem.class).priority()))
                .forEach(clazz -> Stream.of(clazz.getMethods())
                        .filter(method -> method.isAnnotationPresent(ConnectableItem.class))
                        .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(ConnectableItem.class).priority()))
                        .forEach(method -> {
                            map.put(method.getDeclaredAnnotation(ConnectableItem.class).name(), method);
                            classes.put(method, clazz);
                        }));
        return new Map[]{classes, map};
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
        Matcher m = FILE_EXTENSION_PATTERN.matcher(file.getPath());
        if ((!m.hitEnd() && (m.find()))) {
            switch (m.group(1)) {
                case ("json"): {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        FunctionsIO.serializeJson(writer, unwrap(function));
                    } catch (IOException e) {
                        AlertWindows.showError(e);
                    }
                    break;
                }
                case ("xml"): {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        FunctionsIO.serializeXml(writer, unwrap(function));
                    } catch (IOException e) {
                        AlertWindows.showError(e);
                    }
                    break;
                }
                case ("fnc"): {
                    try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                        FunctionsIO.writeTabulatedFunction(outputStream, function);
                    } catch (IOException e) {
                        AlertWindows.showError(e);
                    }
                    break;
                }
            }
        }
    }

    TabulatedFunction loadFunctionAs(File file) {
        TabulatedFunction function = null;
        Matcher m = FILE_EXTENSION_PATTERN.matcher(file.getPath());
        if ((!m.hitEnd() && (m.find()))) {
            switch (m.group(1)) {
                case ("json"): {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        function = wrap(FunctionsIO.deserializeJson(reader, factory.getTabulatedFunctionClass()));
                    } catch (IOException e) {
                        AlertWindows.showError(e);
                    }
                    break;
                }
                case ("xml"): {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        function = wrap(FunctionsIO.deserializeXml(reader, factory.getTabulatedFunctionClass()));
                    } catch (IOException e) {
                        AlertWindows.showError(e);
                    }
                    break;
                }
                case ("fnc"): {
                    try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
                        function = wrap(FunctionsIO.readTabulatedFunction(inputStream, factory));
                    } catch (IOException | ClassNotFoundException e) {
                        AlertWindows.showError(e);
                    }
                    break;
                }
            }
        }
        return function;
    }

}

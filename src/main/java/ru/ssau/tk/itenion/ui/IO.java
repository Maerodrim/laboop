package ru.ssau.tk.itenion.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.atteo.classindex.ClassIndex;
import org.jetbrains.annotations.NotNull;
import ru.ssau.tk.itenion.enums.BelongTo;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.Point;
import ru.ssau.tk.itenion.functions.TabHolderMathFunction;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions.VMF;
import ru.ssau.tk.itenion.functions.powerFunctions.polynomial.PolynomialParser;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.StrictTabulatedFunction;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.UnmodifiableTabulatedFunction;
import ru.ssau.tk.itenion.io.FunctionsIO;
import ru.ssau.tk.itenion.numericalMethods.NumericalMethodsFactory;
import ru.ssau.tk.itenion.numericalMethods.SNumericalMethodsFactory;
import ru.ssau.tk.itenion.numericalMethods.VNumericalMethodsFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class IO {
    public static final BelongTo belongTo = BelongTo.STANISLAV;
    static final String FXML_PATH = "fxml/";
    static final String DEFAULT_DIRECTORY = System.getenv("APPDATA") + "\\tempFunctions";
    private static final TextInputDialog dialog = new TextInputDialog();
    private static Map<BelongTo, NumericalMethodsFactory> numericalMethodsFactories = new HashMap<>();

    static {
        numericalMethodsFactories.put(BelongTo.VALENTIN, new VNumericalMethodsFactory());
        numericalMethodsFactories.put(BelongTo.STANISLAV, new SNumericalMethodsFactory());
        dialog.setOnShowing(dialogEvent -> dialog.getEditor().setText(""));
    }

    public static NumericalMethodsFactory getNumericalMethodFactory() {
        return numericalMethodsFactories.get(belongTo);
    }

    public static Predicate<String> isDouble = s -> {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    };

    public static Predicate<String> isInteger = s -> {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    };
    private static Collection<FileChooser.ExtensionFilter> VMF_EXTENSION_FILTERS = List.of(
            new FileChooser.ExtensionFilter("Vector math function files (*.vmf)", "*.vmf"));
    private static Collection<FileChooser.ExtensionFilter> TF_EXTENSION_FILTERS = List.of(
            new FileChooser.ExtensionFilter("Function files (*.fnc)", "*.fnc"),
            new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"),
            new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
    private static Collection<FileChooser.ExtensionFilter> EXTENSION_FILTERS = List.of(
            new FileChooser.ExtensionFilter("Function files (*.fnc)", "*.fnc"),
            new FileChooser.ExtensionFilter("Vector math function files (*.vmf)", "*.vmf"),
            new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"),
            new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
    private static Pattern FILE_EXTENSION_PATTERN = Pattern.compile(".*\\.(...)");
    private static BooleanBinding booleanBinding;
    private static Button textInputDialogOkButton;
    private static TextField textInputDialogInputField;
    private static Function<ConnectableItem, ?> itemOptionalFunction = connectableItem -> {
        if (connectableItem.parameterInstance().equals(Double.class)) {
            AtomicReference<Double> doubleAtomicReference = new AtomicReference<>();
            IO.getValue("Parameter: ", IO.isDouble).ifPresent(s -> doubleAtomicReference.set(Double.parseDouble(s)));
            return doubleAtomicReference.get();
        } else if (connectableItem.parameterInstance().equals(Integer.class)) {
            AtomicReference<Integer> integerAtomicReference = new AtomicReference<>();
            IO.getValue("Parameter: ", IO.isInteger).ifPresent(s -> integerAtomicReference.set(Integer.parseInt(s)));
            return integerAtomicReference.get();
        } else {
            switch (connectableItem.name()) {
                case "Полином": {
                    AtomicReference<String> stringAtomicReference = new AtomicReference<>();
                    IO.getValue("Polynomial: ", PolynomialParser.isPolynomial).ifPresent(stringAtomicReference::set);
                    return stringAtomicReference.get();
                }
                // add rules for further functions here
            }
        }
        return null;
    };
    // на компьютерах под управлением OS Windows 7/8/8.1/10
    private final TabulatedFunctionFactory factory;
    private boolean isVMF;

    IO(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    public static File load(Stage stage) {
        return load(stage, true, true);
    }

    public static File loadTF(Stage stage) {
        return load(stage, false, true);
    }

    public static File loadVMF(Stage stage) {
        return load(stage, true, false);
    }

    private static File load(Stage stage, boolean allowVMF, boolean allowTF) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load function");
        fileChooser.setInitialDirectory(new File(IO.DEFAULT_DIRECTORY));
        if (allowTF && allowVMF) {
            fileChooser.getExtensionFilters().addAll(EXTENSION_FILTERS);
        } else if (allowTF) {
            fileChooser.getExtensionFilters().addAll(TF_EXTENSION_FILTERS);
        } else if (allowVMF) {
            fileChooser.getExtensionFilters().addAll(VMF_EXTENSION_FILTERS);
        } else {
            throw new UnsupportedOperationException();
        }
        return fileChooser.showOpenDialog(stage);
    }

    static File save(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save function");
        fileChooser.getExtensionFilters().addAll(EXTENSION_FILTERS);
        return fileChooser.showSaveDialog(stage);
    }

    public static void sort(@NotNull ObservableList<Point> list) {
        list.sort(Comparator.comparingDouble(point -> point.x));
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

    public static Map[] initializeMap(Map<Method, Class<?>> classes, Map<String, Method> map, Item item, Predicate<ConnectableItem> classPredicate, Predicate<ConnectableItem> methodPredicate) {
        StreamSupport.stream(ClassIndex.getAnnotated(ConnectableItem.class).spliterator(), false)
                .filter(f -> f.getDeclaredAnnotation(ConnectableItem.class).type() == item)
                .filter(method -> classPredicate.test(method.getDeclaredAnnotation(ConnectableItem.class)))
                .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(ConnectableItem.class).priority()))
                .forEach(clazz -> Stream.of(clazz.getMethods())
                        .filter(method -> method.isAnnotationPresent(ConnectableItem.class))
                        .filter(method -> methodPredicate.test(method.getDeclaredAnnotation(ConnectableItem.class)))
                        .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(ConnectableItem.class).priority()))
                        .forEach(method -> {
                            map.put(method.getDeclaredAnnotation(ConnectableItem.class).name(), method);
                            classes.put(method, clazz);
                        }));
        return new Map[]{classes, map};
    }

    public static Map[] initializeMap(Map<Method, Class<?>> classes, Map<String, Method> map, Item item, Predicate<ConnectableItem> methodPredicate) {
        return initializeMap(classes, map, item, connectableItem -> true, methodPredicate);
    }

    public static Map[] initializeMap(Map<Method, Class<?>> classes, Map<String, Method> map, Item item) {
        return initializeMap(classes, map, item, connectableItem -> true, connectableItem -> true);
    }

//    public static ConnectableItem getConnectableItem(Map<String, Method> map, ActionEvent event){
//        return map.get(((ComboBox<String>) event.getSource()).getValue())
//                .getDeclaredAnnotation(ConnectableItem.class);
//    }

    public static Optional<String> getValue(String title, String headerText, String contentText, Predicate<String> isValid) {
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contentText);
        textInputDialogOkButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        textInputDialogInputField = dialog.getEditor();
        booleanBinding = Bindings.createBooleanBinding(() -> isValid.test(textInputDialogInputField.getText()), textInputDialogInputField.textProperty());
        textInputDialogOkButton.disableProperty().bind(booleanBinding.not());
        return dialog.showAndWait();
    }

    public static Optional<String> getValue(String contentText, Predicate<String> isValid) {
        return getValue("Input parameter window", null, contentText, isValid);
    }

    public static Optional<?> getValue(ConnectableItem item) {
        if (Objects.isNull(item)) {
            return Optional.empty();
        } else return Optional.of(item).filter(ConnectableItem::hasParameter).map(itemOptionalFunction);
    }

    public static void setActualParameter(Map<String, MathFunction> map, String selectedValue, Optional<?> value) {
        ConnectableItem item = map.get(selectedValue).getClass().getDeclaredAnnotation(ConnectableItem.class);
        if (Objects.isNull(item)) {
            return;
        }
        value.ifPresent(value1 -> {
            if (item.hasParameter()) {
                try {
                    if (item.hasParameter()) {
                        map.replace(selectedValue, map.get(selectedValue).getClass()
                                .getDeclaredConstructor(item.parameterInstance()).newInstance(value1));
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    AlertWindows.showError(e);
                }
            }
        });
    }

    public static MathFunction setActualParameter(MathFunction function, Optional<?> value) {
        AtomicReference<MathFunction> functionAtomicReference = new AtomicReference<>();
        ConnectableItem item = function.getClass().getDeclaredAnnotation(ConnectableItem.class);
        if (Objects.isNull(item)) {
            return function;
        }
        value.ifPresent(value1 -> {
            if (item.hasParameter()) {
                try {
                    if (item.hasParameter()) {
                        functionAtomicReference.set(function.getClass().getDeclaredConstructor(item.parameterInstance()).newInstance(value1));
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    AlertWindows.showError(e);
                }
            }
        });
        return functionAtomicReference.get();
    }

    private static TabulatedFunction unwrap(TabulatedFunction function) {
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

    static TabulatedFunction wrap(TabulatedFunction function) {
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

    public boolean isVMF() {
        return isVMF;
    }

    private TabHolderMathFunction loadFunctionAs(File file, boolean permitVMF, boolean permitTF) {
        TabHolderMathFunction function = null;
        Matcher m = FILE_EXTENSION_PATTERN.matcher(file.getPath());
        if ((!m.hitEnd() && (m.find()))) {
            if (permitTF) {
                switch (m.group(1)) {
                    case ("json"): {
                        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                            function = wrap(FunctionsIO.deserializeJson(reader, factory.getTabulatedFunctionClass()));
                            isVMF = false;
                        } catch (IOException e) {
                            AlertWindows.showError(e);
                        }
                        break;
                    }
                    case ("xml"): {
                        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                            function = wrap(FunctionsIO.deserializeXml(reader, factory.getTabulatedFunctionClass()));
                            isVMF = false;
                        } catch (IOException e) {
                            AlertWindows.showError(e);
                        }
                        break;
                    }
                    case ("fnc"): {
                        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
                            function = wrap(FunctionsIO.readTabulatedFunction(inputStream, factory));
                            isVMF = false;
                        } catch (IOException | ClassNotFoundException e) {
                            AlertWindows.showError(e);
                        }
                        break;
                    }

                }
            }
            if (permitVMF) {
                switch (m.group(1)) {
                    case ("vmf"): {
                        isVMF = true;
                        //todo
                        break;
                    }
                }
            }
        }
        return function;
    }

    public TabHolderMathFunction loadFunctionAs(File file) {
        return loadFunctionAs(file, true, true);
    }

    public VMF loadVMFAs(File file) {
        return (VMF) loadFunctionAs(file, true, false);
    }

    public TabulatedFunction loadTabulatedFunctionAs(File file) {
        return (TabulatedFunction) loadFunctionAs(file, false, true);
    }

}

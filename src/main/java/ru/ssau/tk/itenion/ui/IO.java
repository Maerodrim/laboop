package ru.ssau.tk.itenion.ui;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.ssau.tk.itenion.enums.BelongTo;
import ru.ssau.tk.itenion.functions.AnyTabHolderMathFunction;
import ru.ssau.tk.itenion.functions.TabHolderMathFunction;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions.VMF;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.StrictTabulatedFunction;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.UnmodifiableTabulatedFunction;
import ru.ssau.tk.itenion.io.FunctionsIO;
import ru.ssau.tk.itenion.numericalMethods.factory.LNumericalMethodsFactory;
import ru.ssau.tk.itenion.numericalMethods.factory.MNumericalMethodsFactory;
import ru.ssau.tk.itenion.numericalMethods.factory.NumericalMethodsFactory;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class IO {
    public static final BelongTo belongTo = BelongTo.LAUFINSCONSCA;
    static final String FXML_PATH = "fxml/";
    static final String DEFAULT_DIRECTORY = System.getenv("APPDATA") + "\\tempFunctions";     // на компьютерах под управлением OS Windows 7/8/8.1/10

    private static Map<BelongTo, NumericalMethodsFactory> numericalMethodsFactories = new HashMap<>();
    private static final FileChooser.ExtensionFilter vmfExtensionFilter =
            new FileChooser.ExtensionFilter("Vector math function files (*.vmf)", "*.vmf");
    private static final FileChooser.ExtensionFilter tfExtensionFilter =
            new FileChooser.ExtensionFilter("Function files (*.fnc)", "*.fnc");
    private static final FileChooser.ExtensionFilter jsonTFExtensionFilter =
            new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
    private static final FileChooser.ExtensionFilter xmlTFExtensionFilter =
            new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");

    static {
        numericalMethodsFactories.put(BelongTo.LAUFINSCONSCA, new LNumericalMethodsFactory());
        numericalMethodsFactories.put(BelongTo.MAERODRIM, new MNumericalMethodsFactory());
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
    private static Collection<FileChooser.ExtensionFilter> VMF_EXTENSION_FILTERS = List.of(vmfExtensionFilter);
    private static Collection<FileChooser.ExtensionFilter> TF_EXTENSION_FILTERS = List.of(
            tfExtensionFilter,
            jsonTFExtensionFilter,
            xmlTFExtensionFilter);
    private static Collection<FileChooser.ExtensionFilter> EXTENSION_FILTERS = List.of(
            tfExtensionFilter,
            vmfExtensionFilter,
            jsonTFExtensionFilter,
            tfExtensionFilter);
    private static Pattern FILE_EXTENSION_PATTERN = Pattern.compile(".*\\.(.+)");
    private final TabulatedFunctionFactory factory;

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
        bindExtensionFilters(fileChooser, allowVMF, allowTF);
        return fileChooser.showOpenDialog(stage);
    }

    public static File save(Stage stage) {
        return save(stage, true, true);
    }

    public static File saveTF(Stage stage) {
        return save(stage, false, true);
    }

    public static File saveVMF(Stage stage) {
        return save(stage, true, false);
    }

    static File save(Stage stage, boolean allowVMF, boolean allowTF) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save function");
        bindExtensionFilters(fileChooser, allowVMF, allowTF);
        return fileChooser.showSaveDialog(stage);
    }

    private static void bindExtensionFilters(FileChooser fileChooser, boolean allowVMF, boolean allowTF) {
        if (allowTF && allowVMF) {
            fileChooser.getExtensionFilters().addAll(EXTENSION_FILTERS);
        } else if (allowTF) {
            fileChooser.getExtensionFilters().addAll(TF_EXTENSION_FILTERS);
        } else if (allowVMF) {
            fileChooser.getExtensionFilters().addAll(VMF_EXTENSION_FILTERS);
        } else {
            throw new UnsupportedOperationException();
        }
    }

//    public static ConnectableItem getConnectableItem(Map<String, Method> map, ActionEvent event){
//        return map.get(((ComboBox<String>) event.getSource()).getValue())
//                .getDeclaredAnnotation(ConnectableItem.class);
//    }

    private AnyTabHolderMathFunction loadFunctionAs(File file, boolean permitVMF, boolean permitTF) {
        AnyTabHolderMathFunction function = null;
        Matcher m = FILE_EXTENSION_PATTERN.matcher(file.getPath());
        if ((!m.hitEnd() && (m.find()))) {
            if (permitTF) {
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
            if (permitVMF) {
                switch (m.group(1)) {
                    case ("vmf"): {
                        //todo here's should be loading vector math function
                        break;
                    }
                }
            }
        }
        return function;
    }

    public AnyTabHolderMathFunction loadFunctionAs(File file) {
        return loadFunctionAs(file, true, true);
    }

    public VMF loadVMFAs(File file) {
        return (VMF) loadFunctionAs(file, true, false);
    }

    public TabulatedFunction loadTabulatedFunctionAs(File file) {
        return (TabulatedFunction) loadFunctionAs(file, false, true);
    }

    private void saveFunctionAs(File file, AnyTabHolderMathFunction function, boolean permitVMF, boolean permitTF) {
        Matcher m = FILE_EXTENSION_PATTERN.matcher(file.getPath());
        if ((!m.hitEnd() && (m.find()))) {
            if (permitTF) {
                TabulatedFunction tabulatedFunction = (TabulatedFunction)function;
                switch (m.group(1)) {
                    case ("json"): {
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                            FunctionsIO.serializeJson(writer, unwrap(tabulatedFunction));
                        } catch (IOException e) {
                            //AlertWindows.showError(e);
                            e.printStackTrace();
                        }
                        break;
                    }
                    case ("xml"): {
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                            FunctionsIO.serializeXml(writer, unwrap(tabulatedFunction));
                        } catch (IOException e) {
                            AlertWindows.showError(e);
                        }
                        break;
                    }
                    case ("fnc"): {
                        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
                            FunctionsIO.writeTabulatedFunction(outputStream, tabulatedFunction);
                        } catch (IOException e) {
                            AlertWindows.showError(e);
                        }
                        break;
                    }
                }
            }
            if (permitVMF) {
                switch (m.group(1)) {
                    case ("vmf"): {
                        //todo here's should be saving vector math function
                        break;
                    }
                }
            }
        }
    }

    public void saveFunctionAs(File file, AnyTabHolderMathFunction function) {
        saveFunctionAs(file, function, true, true);
    }

    public void saveTabulatedFunctionAs(File file, TabulatedFunction function) {
        saveFunctionAs(file, function, false, true);
    }

    public void saveVMFAs(File file, VMF function) {
        saveFunctionAs(file, function, true, false);
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

}

package ru.ssau.tk.itenion.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.ssau.tk.itenion.enums.State;
import ru.ssau.tk.itenion.functions.MathFunction;
import ru.ssau.tk.itenion.functions.Point;
import ru.ssau.tk.itenion.functions.factory.ArrayTabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.factory.TabulatedFunctionFactory;
import ru.ssau.tk.itenion.functions.multipleVariablesFunctions.vectorFunctions.VMF;
import ru.ssau.tk.itenion.functions.tabulatedFunctions.TabulatedFunction;
import ru.ssau.tk.itenion.operations.TabulatedFunctionOperationService;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import static ru.ssau.tk.itenion.ui.Initializer.initializeMathFunctionMap;

public class TabController implements Initializable, OpenableWindow {

    private TabController tabController = this;
    private final TableColumn<Point, Double> x = new TableColumn<>("X");
    private final TableColumn<Point, Double> y = new TableColumn<>("Y");
    private Stage stage;
    private int numberId = 1;
    private TabulatedFunctionFactory factory;
    private Map<Tab, TabulatedFunction> tabulatedFunctionMap;
    private Map<Tab, VMF> VMFMap;
    private Map<String, MathFunction> mathFunctionMap;
    private Map<String, OpenableWindow> controllerMap;
    private Map<String, MathFunction> compositeFunctionMap;
    private Tab currentTab;
    private IO io;
    private boolean isStrict = false;
    private boolean isUnmodifiable = false;
    public TFState tfState;
    public VMFState vmfState;
    public static AnyTabHolderState anyTabHolderState;
    public static TabHolderState state;

    @FXML
    private TabPane tabPane;
    @FXML
    private BorderPane mainPane, bottomPane;
    @FXML
    private Pane labelPane;
    @FXML
    private Button addPointButton, deletePointButton, calculateValueButton;
    @FXML
    private Label label;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfState = new TFState();
        vmfState = new VMFState();
        anyTabHolderState = new AnyTabHolderState();
        state = tfState;

        bottomPane.setTop(null);
        bottomPane.setLeft(null);
        bottomPane.setCenter(null);
        bottomPane.setRight(null);
        bottomPane.setBottom(null);

        controllerMap = new HashMap<>();
        mathFunctionMap = new LinkedHashMap<>();
        tabulatedFunctionMap = new LinkedHashMap<>();
        VMFMap = new LinkedHashMap<>();
        compositeFunctionMap = new LinkedHashMap<>();
        x.setCellValueFactory(new PropertyValueFactory<>("X"));
        y.setCellValueFactory(new PropertyValueFactory<>("Y"));
        x.setPrefWidth(300);
        y.setPrefWidth(300);
        tabPane.getSelectionModel().selectLast();
        factory = new ArrayTabulatedFunctionFactory();
        io = new IO(factory);
        boolean isCreated = new File(IO.DEFAULT_DIRECTORY).mkdir();
        new Initializer(stage).initializeWindowControllers(controllerMap);
        initializeMathFunctionMap(mathFunctionMap);
        bindMathFunctionMap();
    }

    private void bindMathFunctionMap() {
        controllerMap.values().stream()
                .filter(f -> f instanceof MathFunctionAccessible)
                .forEach(f -> ((MathFunctionAccessible) f).bindMathFunctionMap(mathFunctionMap));
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void loadFunction() {
        state.accept(new TabVisitor() {
            @Override
            public void visit(TFState tfState) {
                File file = IO.loadTF(stage);
                if (!Objects.isNull(file)) {
                    tfState.createTab(io.loadTabulatedFunctionAs(file));
                }
            }

            @Override
            public void visit(VMFState vmfState) {

            }
        });
    }

    @FXML
    private void saveFunction() {
        save(true);
    }

    @FXML
    private void saveAsFunction() {
        save(false);
    }

    private void save(boolean toTempPath) {
        if (isTabExist()) {
            state.accept(new TabVisitor() {
                @Override
                public void visit(TFState tfState) {
                    File file = toTempPath
                            ? new File(IO.DEFAULT_DIRECTORY + "\\" + tfState.getFunction().getMathFunction() + ".fnc")
                            : IO.saveTF(stage);
                    if (!Objects.isNull(file)) {
                        io.saveTabulatedFunctionAs(file, tfState.getFunction());
                    }
                }

                @Override
                public void visit(VMFState vmfState) {
                }
            });
        }
    }


    @FXML
    private void exit() {
        stage.close();
    }

    private boolean isTabExist() {
        return !Objects.isNull(currentTab);
    }

    private void show(boolean isResizable, StackTraceElement stackTraceElement) {
        Stage stage = lookupController(stackTraceElement.getMethodName()).getStage();
        stage.setResizable(isResizable);
        stage.showAndWait();
    }

    private void showFromNestedClass(boolean isResizable) {
        show(isResizable, Thread.currentThread().getStackTrace()[4]);
    }

    private void show(boolean isResizable) {
        show(isResizable, Thread.currentThread().getStackTrace()[2]);
    }

    private OpenableWindow lookupController() {
        return lookupController(Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    private OpenableWindow lookupController(String path) {
        OpenableWindow controller = controllerMap.get(path + ".fxml");
        if (controller instanceof TabulatedFunctionAccessible) {
            ((TabulatedFunctionAccessible) controller).bindTabulatedFunctionMap(tabulatedFunctionMap);
        }
        if (controller instanceof CompositeFunctionAccessible) {
            ((CompositeFunctionAccessible) controller).bindCompositeFunctionMap(compositeFunctionMap);
        }
        return controllerMap.get(path + ".fxml");
    }

    @FXML
    public void mathFunction() {
        show(true);
    }

    @FXML
    public void vectorFunction() {
        show(true);
    }

    Function<Boolean, TabVisitor> tabVisitorFunction = aBoolean -> new TabVisitor() {
        @Override
        public void visit(TFState tfState) {
            if (aBoolean ? !tfState.getFunction().isStrict() : !tfState.getFunction().isUnmodifiable()) {
                showFromNestedClass(true);
            } else {
                String temp = aBoolean ? "strict" : "unmodifiable";
                AlertWindows.showWarning("Function is " + temp);
            }
        }

        @Override
        public void visit(VMFState vmfState) {
        }
    };

    @FXML
    private void deletePoint() {
        if (isTabExist()) {
            state.accept(tabVisitorFunction.apply(false));
        }
    }

    @FXML
    private void addPoint() {
        if (isTabExist()) {
            state.accept(tabVisitorFunction.apply(false));
        }
    }

    @FXML
    private void calculate() {
        if (isTabExist()) {
            state.accept(tabVisitorFunction.apply(true));
        }
    }

    @FXML
    private void compose() {
        if (isTabExist()) {
            state.accept(tabVisitorFunction.apply(true));
        }
    }

    @FXML
    private void about() {
        if (isTabExist()) {
            AtomicBoolean atomicBoolean = new AtomicBoolean(true);
            state.accept((TFTabVisitor) tabulatedFunction -> atomicBoolean.set(tabulatedFunction.getFunction().isMathFunctionExist()));
            if (atomicBoolean.get()) {
                OpenableWindow controller = lookupController();
                ((AboutController) controller).setInfo();
                controller.getStage().show();
            } else {
                AlertWindows.showWarning("Function doesn't have base math function");
            }
        }
    }

    @FXML
    public void plot() {
        if (isTabExist()) {
            PlotController controller = (PlotController) lookupController();
            controller.setSeries();
            controller.getStage().show();
        }
    }

    @FXML
    private void solve() {
        if (isTabExist()) {
            AtomicBoolean atomicBoolean = new AtomicBoolean(true);
            state.accept((TFTabVisitor) tabulatedFunction -> atomicBoolean.set(!tabulatedFunction.getFunction().isStrict()));
            if (atomicBoolean.get()) {
                SolveController controller = (SolveController) lookupController();
                controller.getStage().setResizable(false);
                controller.getStage().show();
            } else {
                AlertWindows.showWarning("Function is strict");
            }
        }
    }

    @FXML
    private void settings() {
        ((SettingsController) lookupController()).start();
    }

    @FXML
    private void tabulatedFunction() {
        show(false);
    }

    @FXML
    private void apply() {
        if (isTabExist()) {
            state.accept((TFTabVisitor) tfState -> showFromNestedClass(false));
        }
    }

    @FXML
    private void operator() {
        if (isTabExist()) {
            state.accept(tabVisitorFunction.apply(true));
        }
    }

    public boolean isStrict() {
        return isStrict;
    }

    void setStrict(boolean strict) {
        isStrict = strict;
    }

    public boolean isUnmodifiable() {
        return isUnmodifiable;
    }

    void setUnmodifiable(boolean unmodifiable) {
        isUnmodifiable = unmodifiable;
    }

    public TabulatedFunctionFactory getFactory() {
        return factory;
    }

    public void setFactory(TabulatedFunctionFactory factory) {
        this.factory = factory;
    }

    Map<Tab, TabulatedFunction> getTabulatedFunctionMap() {
        return tabulatedFunctionMap;
    }

    public Tab getCurrentTab() {
        return currentTab;
    }

    public Map<String, MathFunction> getCompositeFunctionMap() {
        return compositeFunctionMap;
    }

    public final class AnyTabHolderState {

        private AnyTabHolderState() {
        }

        public boolean isStrict() {
            return isStrict;
        }

        public boolean isUnmodifiable() {
            return isUnmodifiable;
        }

        public void setFactory(TabulatedFunctionFactory tfFactory) {
            factory = tfFactory;
        }

        public void setStrict(boolean strict) {
            isStrict = strict;
        }

        public void setUnmodifiable(boolean unmodifiable) {
            isUnmodifiable = unmodifiable;
        }

        public void plot() {
            tabController.plot();
        }

        public TabulatedFunctionFactory getFactory() {
            return factory;
        }

        public void addCompositeFunction(MathFunction function) {
            compositeFunctionMap.put(function.getName(), function);
        }
    }

    public final class TFState implements TabHolderState {

        public final State state = State.TF;

        private TFState() {
        }

        @Override
        public TabulatedFunction getFunction() {
            return tabulatedFunctionMap.get(currentTab);
        }

        public void createTab(TabulatedFunction function) {
            createTab(TabulatedFunctionOperationService.asObservableList(function), function);
        }

        public void createTab(ObservableList<Point> list) {
            TabulatedFunction function = factory.create(list);
            function.offerUnmodifiable(isUnmodifiable);
            function.offerStrict(isStrict);
            IO.wrap(function);
            createTab(list, function);
        }

        private void createTab(ObservableList<Point> list, TabulatedFunction function) {
            function.offerStrict(isStrict);
            function.offerUnmodifiable(isUnmodifiable);
            TableView<Point> table = new TableView<>();
            table.setItems(list);
            table.getColumns().addAll(x, y);
            Tab tab = new Tab("Function" + numberId, table);
            tab.setId("function" + numberId++);
            tab.setClosable(true);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            tabulatedFunctionMap.put(tab, function);
            notifyAboutAccessibility(function);
            currentTab = tab;
            tab.setOnSelectionChanged(event -> {
                if (tab.isSelected()) {
                    currentTab = tab;
                    mainPane.setBottom(null);
                    mainPane.setBottom(bottomPane);
                    TabController.state = this;
                    notifyAboutAccessibility(getFunction());
                }
            });
            tab.setOnCloseRequest(event -> {
                tabulatedFunctionMap.remove(tab);
                if (tabulatedFunctionMap.isEmpty()) {
                    mainPane.getChildren().remove(bottomPane);
                    currentTab = null;
                }
            });
        }

        private void notifyAboutAccessibility(TabulatedFunction function) {
            boolean isStrict = function.isStrict();
            boolean isUnmodifiable = function.isUnmodifiable();
            List<Node> list = new ArrayList<>();
            list.add(calculateValueButton);
            list.add(label);
            if (tabulatedFunctionMap.size() == 1) {
                mainPane.setBottom(bottomPane);
            }
            clear(bottomPane);
            if (isUnmodifiable && isStrict) {
                bottomPane.setLeft(null);
                bottomPane.setCenter(labelPane);
                bottomPane.setRight(null);
                labelPane.getChildren().removeAll(list);
                labelPane.getChildren().add(label);
                label.setText("Function is strict and unmodifiable");
            } else if (isUnmodifiable) {
                bottomPane.setLeft(null);
                bottomPane.setCenter(labelPane);
                bottomPane.setRight(null);
                labelPane.getChildren().removeAll(list);
                labelPane.getChildren().add(calculateValueButton);
                calculateValueButton.layoutXProperty().setValue(225);
            } else if (isStrict) {
                bottomPane.setLeft(addPointButton);
                BorderPane.setMargin(addPointButton, new Insets(0, 0, 0, 80));
                BorderPane.setMargin(deletePointButton, new Insets(0, 80, 0, 0));
                bottomPane.setRight(deletePointButton);
            } else {
                bottomPane.setLeft(addPointButton);
                BorderPane.setMargin(addPointButton, new Insets(0, 0, 0, 30));
                BorderPane.setMargin(deletePointButton, new Insets(0, 30, 0, 0));
                bottomPane.setRight(deletePointButton);
                bottomPane.setCenter(labelPane);
                labelPane.getChildren().removeAll(list);
                labelPane.getChildren().add(calculateValueButton);
                calculateValueButton.layoutXProperty().setValue(45);
            }
        }

        private void clear(BorderPane pane) {
            pane.setTop(null);
            pane.setLeft(null);
            pane.setCenter(null);
            pane.setRight(null);
            pane.setBottom(null);
        }

        @SuppressWarnings("unchecked")
        public ObservableList<Point> getObservableList() {
            return ((TableView<Point>) currentTab.getContent()).getItems();
        }

        @Override
        public void changeState(State state) {
            switch (state) {
                case TF:
                    TabController.state = tfState;
                    break;
                case VMF:
                    TabController.state = vmfState;
                    break;
            }
        }

        @Override
        public void accept(TabVisitor tabVisitor) {
            tabVisitor.visit(this);
        }
    }

    public final class VMFState implements TabHolderState {

        public final State state = State.VMF;

        private VMFState() {
        }

        @Override
        public VMF getFunction() {
            return VMFMap.get(currentTab);
        }

        public void createTab(AnchorPane vmfPane, VMF vmf) {
            mainPane.setBottom(null);
            // todo
            Tab tab = new Tab("Function" + numberId, vmfPane);
            tab.setId("function" + numberId++);
            tab.setClosable(true);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            currentTab = tab;
            VMFMap.put(tab, vmf);
            tab.setOnSelectionChanged(event -> {
                if (tab.isSelected()) {
                    TabController.state = this;
                    mainPane.setBottom(null);
                    currentTab = tab;
                }
            });
            tab.setOnCloseRequest(event -> {
                VMFMap.remove(tab);
                if (VMFMap.isEmpty()) {
                    currentTab = null;
                }
            });
        }

        @Override
        public void changeState(State state) {
            switch (state) {
                case TF:
                    TabController.state = tfState;
                    break;
                case VMF:
                    TabController.state = vmfState;
                    break;
            }
        }

        @Override
        public void accept(TabVisitor tabVisitor) {
            tabVisitor.visit(this);
        }
    }

}
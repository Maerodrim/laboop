package ru.ssau.tk.itenion.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.atteo.classindex.ClassIndex;
import ru.ssau.tk.itenion.functions.MathFunction;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Initializer {
    private Stage ownerStage;
    private static final Class<ConnectableItem> annotationClass = ConnectableItem.class;
    private Function<Class<?>, OpenableWindow> initializeWindowController = new Function<>() {
        @Override
        public OpenableWindow apply(Class<?> clazz) {
            OpenableWindow controller = null;
            try {
                controller = (OpenableWindow) clazz.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            String path = IO.FXML_PATH + controller.getClass().getDeclaredAnnotation(annotationClass).pathFXML();
            controller = initializeModalityWindow(path, controller);
            controller.getStage().initOwner(ownerStage);
            controller.getStage().setTitle(controller.getClass().getDeclaredAnnotation(annotationClass).name());
            return controller;
        }
    };

    public Initializer(Stage ownerStage) {
        this.ownerStage = ownerStage;
    }

    private static <T extends OpenableWindow> T initializeModalityWindow(String pathFXML, T modalityWindow) {
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

    public void initializeWindowControllers(Map<String, OpenableWindow> controllerMap) {
        StreamSupport.stream(ClassIndex.getAnnotated(annotationClass).spliterator(), false)
                .filter(f -> f.getDeclaredAnnotation(annotationClass).type() == Item.CONTROLLER)
                .forEach(clazz -> controllerMap.put(clazz.getDeclaredAnnotation(annotationClass).pathFXML(),
                        initializeWindowController.apply(clazz)));
    }

    public static void initializeMathFunctionMap(Map<String, MathFunction> mathFunctionMap) {
        StreamSupport.stream(ClassIndex.getAnnotated(annotationClass).spliterator(), false)
                .filter(f -> f.getDeclaredAnnotation(annotationClass).type() == Item.FUNCTION)
                .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(annotationClass).priority()))
                .forEach(clazz -> {
                    try {
                        mathFunctionMap.put(clazz.getDeclaredAnnotation(annotationClass).name(),
                                (MathFunction) clazz.getDeclaredConstructor().newInstance());
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        AlertWindows.showError(e);
                    }
                });
    }

    public static Map[] initializeMap(Map<Method, Class<?>> classes, Map<String, Method> map, Item item, Predicate<ConnectableItem> classPredicate, Predicate<ConnectableItem> methodPredicate) {
        StreamSupport.stream(ClassIndex.getAnnotated(annotationClass).spliterator(), false)
                .filter(f -> f.getDeclaredAnnotation(annotationClass).type() == item)
                .filter(method -> classPredicate.test(method.getDeclaredAnnotation(annotationClass)))
                .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(annotationClass).priority()))
                .forEach(clazz -> Stream.of(clazz.getMethods())
                        .filter(method -> method.isAnnotationPresent(annotationClass))
                        .filter(method -> methodPredicate.test(method.getDeclaredAnnotation(annotationClass)))
                        .sorted(Comparator.comparingInt(f -> f.getDeclaredAnnotation(annotationClass).priority()))
                        .forEach(method -> {
                            map.put(method.getDeclaredAnnotation(annotationClass).name(), method);
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
}

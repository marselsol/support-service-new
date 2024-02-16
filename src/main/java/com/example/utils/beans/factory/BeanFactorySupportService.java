package com.example.utils.beans.factory;

import com.example.controllers.ControllerSupportService;
import com.example.logging.ConsoleLoggerService;
import com.example.logging.LoggerService;
import com.example.utils.beans.factory.annotation.AutowiredSupportService;
import com.example.utils.beans.factory.stereotype.ComponentSupportService;
import com.example.utils.beans.factory.stereotype.ControllerSupportServiceAnnotation;
import com.example.utils.beans.factory.stereotype.RepositorySupportService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

@ComponentSupportService
public class BeanFactorySupportService {
    Map<String, Object> singletonsMap = new HashMap<>();

    public Object getBeans(String beanName) {
        return singletonsMap.get(beanName);
    }

    public void fillAutowired() {
        System.out.println("==fillAutowired==");
        singletonsMap.values().forEach(this::searchAndInjectAutowiredDependenciesInSingletonsMap);
    }

    private void searchAndInjectAutowiredDependenciesInSingletonsMap(Object object) {
        Arrays.stream(object.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(AutowiredSupportService.class))
                .forEach(field -> injectDependency(object, field));
    }

    private void injectDependency(Object object, Field field) {
        singletonsMap.values().stream()
                .filter(dependency -> dependency.getClass().equals(field.getType()))
                .findFirst()
                .ifPresent(dependency -> invokeSetterMethod(object, field, dependency));
    }

    private void invokeSetterMethod(Object object, Field field, Object dependency) {
        String setterName = "set" + capitalizeFirstLetter(field.getName());
        System.out.println("Setter name = " + setterName);
        try {
            Method setter = object.getClass().getMethod(setterName, dependency.getClass());
            setter.invoke(object, dependency);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private String capitalizeFirstLetter(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public void fillSingletonsMap(String basePackage) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = basePackage.replace('.', '/');
        try {
            Enumeration<URL> resources = classLoader.getResources(path);
            Iterator<URL> iterator = Collections.list(resources).iterator();
            while (iterator.hasNext()) {
                URL resource = iterator.next();
                File file = new File(resource.toURI());
                processDirectory(file, basePackage);
            }
            singletonsMap.put("objectMapper", returnObjectMapper());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        System.err.println("Size singletons map is: " + singletonsMap.size());
    }

    private void processDirectory(File directory, String packageName) {
        for (File file : directory.listFiles()) {
            String fileName = file.getName();
            if (file.isDirectory()) {
                processDirectory(file, packageName + "." + fileName); //Для поддиректорий
            } else if (fileName.endsWith(".class")) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                try {
                    /**
                     * используем чтобы достать класс с помощью classLoader по имени и полному пути
                     */
                    Class<?> classObject = Class.forName(packageName + "." + className);
                    if (classObject.isAnnotationPresent(ComponentSupportService.class)
                            || classObject.isAnnotationPresent(RepositorySupportService.class)
                            || classObject.isAnnotationPresent(ControllerSupportServiceAnnotation.class)) {
                        System.out.println("Component: " + classObject);
                        Object instance = classObject.newInstance();
                        String beanName = className.substring(0, 1).toLowerCase() + className.substring(1);
                        singletonsMap.put(beanName, instance);
                    }

                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void wrapControllersWithLoggingProxy() {
        LoggerService logger = new ConsoleLoggerService();
        singletonsMap.forEach((beanName, beanInstance) -> {
            if (beanInstance instanceof ControllerSupportService) {
                Object proxyInstance = ProxyFactory.createProxy(beanInstance, logger);
                singletonsMap.put(beanName, proxyInstance);
            }
        });
    }

    public ObjectMapper returnObjectMapper() {
        return new ObjectMapper();
    }
}

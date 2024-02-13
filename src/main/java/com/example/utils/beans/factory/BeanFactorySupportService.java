package com.example.utils.beans.factory;

import com.example.controllers.ControllerSupportService;
import com.example.logging.ConsoleLoggerService;
import com.example.logging.LoggerService;
import com.example.utils.beans.factory.annotation.AutowiredSupportService;
import com.example.utils.beans.factory.stereotype.ComponentSupportService;
import com.example.utils.beans.factory.stereotype.ControllerSupportServiceAnnotation;
import com.example.utils.beans.factory.stereotype.RepositorySupportService;

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
        for (Object object : singletonsMap.values()) {
            for (Field field : object.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(AutowiredSupportService.class)) {
                    for (Object dependency : singletonsMap.values()) {
                        if (dependency.getClass().equals(field.getType())) {
                            String setterName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);//setPromotionsService
                            System.out.println("Setter name = " + setterName);
                            Method setter = null;
                            try {
                                setter = object.getClass().getMethod(setterName, dependency.getClass());
                            } catch (NoSuchMethodException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                setter.invoke(object, dependency);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
        }
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
}

package com.example.utils.beans.factory;

import com.example.utils.beans.factory.annotation.AutowiredMarsel;
import com.example.utils.beans.factory.stereotype.ComponentMarsel;
import com.example.utils.beans.factory.stereotype.RepositoryMarsel;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class BeanFactoryMarsel {
    Map<String, Object> singletonsMap = new HashMap<>();

    public Object getBeans(String beanName) {
        return singletonsMap.get(beanName);
    }

    public void getInfoAboutSingletonsMap(String text) {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!in singletonsMap. size is: " + singletonsMap.size());
        System.out.println("В методе " + text);
        for (Map.Entry<String, Object> entry : singletonsMap.entrySet()) {
            System.out.println("Что лежит в singletonsMap: " + entry.getKey() + ": " + entry.getValue());
        }
    }

    public void fillAutowired() {
        System.out.println("==fillAutowired==");
        for (Object object : singletonsMap.values()) {
            for (Field field : object.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(AutowiredMarsel.class)) {
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
        getInfoAboutSingletonsMap("fillAutowired");
    }

    public void fillSingletonsMap(String basePackage) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = basePackage.replace('.', '/'); // "com.example" -> "com/example"
        try {
            Enumeration<URL> resources = classLoader.getResources(path);
            Iterator<URL> iterator = Collections.list(resources).iterator();
            while (iterator.hasNext()) {
                URL resource = iterator.next();
                File file = new File(resource.toURI());
                processDirectory(file, basePackage);
            }
            getInfoAboutSingletonsMap("fillSingletonsMap");
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        System.err.println("Size singletons map is: " + singletonsMap.size());
        System.out.println(singletonsMap.get("phraseStorage"));
        System.out.println(singletonsMap.get("helpServlet"));
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
                    if (classObject.isAnnotationPresent(ComponentMarsel.class) || classObject.isAnnotationPresent(RepositoryMarsel.class)) {
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
}

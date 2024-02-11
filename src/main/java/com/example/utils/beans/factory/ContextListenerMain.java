//package com.example.utils.beans.factory;
//
//import jakarta.servlet.ServletContextEvent;
//import jakarta.servlet.ServletContextListener;
//import jakarta.servlet.annotation.WebListener;
//
//@WebListener
//public class ContextListenerMain implements ServletContextListener {
//    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//        System.out.println("contextInitialized is running.");
//        BeanFactoryMarsel beanFactory = new BeanFactoryMarsel();
//        beanFactory.fillSingletonsMap("com.example");
//        beanFactory.fillAutowired();
//    }
//}

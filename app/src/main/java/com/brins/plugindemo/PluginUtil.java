package com.brins.plugindemo;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class PluginUtil {

    public static final String apkPath = "plugin-debug.apk";

    public static void loadPlugin(Context context) {

        Class<?> dexPathListClass = null;
        Class<?> baseDexClassLoaderClass = null;
        try {
            dexPathListClass = Class.forName("dalvik.system.DexPathList");
            Field dexElements = dexPathListClass.getDeclaredField("dexElements");
            dexElements.setAccessible(true);

            baseDexClassLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathList = baseDexClassLoaderClass.getDeclaredField("pathList");
            pathList.setAccessible(true);

            PathClassLoader classLoader = (PathClassLoader) context.getClassLoader();
            //获取宿主DexPathList
            Object hostPathList = pathList.get(classLoader);
            //获取宿主Elements
            Object[] hostElements = (Object[]) dexElements.get(hostPathList);

            File extractFile = context.getFileStreamPath(apkPath);
            String dexpath = extractFile.getPath();
            File fileRelease = context.getDir("dex", 0); //0 表示Context.MODE_PRIVATE


            //用于加载插件的类加载器
            DexClassLoader dexClassLoader = new DexClassLoader(dexpath, fileRelease.getAbsolutePath(),
                    null, classLoader);

            //获取插件的DexPathList
            Object pluginPathList = pathList.get(dexClassLoader);
            //获取插件的Elements
            Object[] pluginElements = (Object[]) dexElements.get(pluginPathList);

            //合并 因为无法获取Element类，所以只能利用反射创建数组
            Object[] mergeElements = (Object[]) Array.newInstance(
                    hostElements.getClass().getComponentType(), hostElements.length + pluginElements.length);

            System.arraycopy(hostElements, 0, mergeElements, 0, hostElements.length);
            System.arraycopy(pluginElements, 0, mergeElements, hostElements.length, pluginElements.length);
            dexElements.set(hostPathList, mergeElements);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

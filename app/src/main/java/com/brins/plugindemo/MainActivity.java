package com.brins.plugindemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mText = findViewById(R.id.text);
        printClassLoader();
    }

    public void printClassLoader() {
        ClassLoader classLoader = this.getClassLoader();
        while (classLoader != null) {
            Log.d("classLoader", classLoader.toString());
            classLoader = classLoader.getParent();
        }
        Log.d("classLoader", Activity.class.getClassLoader() + "");

    }

    public void OnStart(View view) {
        try {
            Class<?> clazz = Class.forName("com.brins.plugin.Test");
            Method method = clazz.getMethod("startPlugin");
            String str = (String) method.invoke(null);
            mText.setText(str);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }


}
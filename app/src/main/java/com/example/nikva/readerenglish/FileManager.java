package com.example.nikva.readerenglish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;


/**
 *Created by nikitavasko11@gmail.com
 * The class that responsible for the navigation of files(File Manager).
 * @author Nikita Vasko
 */
public class FileManager extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listOfDirs;
    ArrayList<String> paths = new ArrayList<>();
    String currentPath = "";
    final private String TAG = "myTags";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);

        listOfDirs = (ListView) findViewById(R.id.list);
        listOfDirs.setOnItemClickListener(this);

        paths.add("/storage/emulated/0/");
        paths.add("/storage/extSdCard/");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                paths);
        listOfDirs.setAdapter(adapter);//Creating a primary form of application
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String localPath = paths.get(position);

        if (localPath.equals("...")) {
            Log.d(TAG, "Переход на уровень ниже");
            upOnLevel();
            /*Toast.makeText(this, String.valueOf(view.getId()), Toast.LENGTH_SHORT).show();*/
        } else {
            transition(localPath);
        }
    }

    /**
     * Method performs transition to new directory.
     * @param currentDirectory - current directory or file that user selected.
     */
    void transition(String currentDirectory){
        if (new File(currentPath + currentDirectory).isFile()) {
            Intent intent;
            String pathToFile = currentPath + currentDirectory;

            //Дальше небольшой блок говнокода, не обращай внимания, я закоментил
            /*if ((currentPath + currentDirectory).contains(".txt")) {
                //intent = new Intent(this, ReaderActivity.class);
                startActivity(intent);
                //intent.putExtra();
            }*/
        }
        else {
            if (currentPath.equals("")) {
                Log.d(TAG, "Создание пути в новое хранилище.");
                currentPath = currentDirectory;
            } else {
                Log.d(TAG, "Создание пути в новую директорию");
                currentPath = currentPath + currentDirectory + "/";
            }

            File file = new File(currentPath);
            Log.d(TAG, "Создание объекта File " + file.getPath());
            if (file.isDirectory()) {
                Log.d(TAG, "Выбрана директория");
                fill(file.list());
            } else {
                Log.d(TAG, "Выбран файл");
                return;
            }
        }
    }

    /**
     * Method performs transition to upper level.
     */
    void upOnLevel(){
        //If user want to go to the first level then paths setting manually
        Log.d(TAG, "Проверка: на каком уровне находится приложение" + new File(currentPath).getParent());
        if (new File(currentPath).getParent().equals("/storage/emulated") ||
                new File(currentPath).getParent().equals("/storage")){
            Log.d(TAG, "Возвращаемся на первый уровень");
            String [] s  = {"/storage/emulated/0/", "/storage/extSdCard/"};
            currentPath = "";
            fill(s);
        } else{
            String path = new File(currentPath).getParent() + '/';
            currentPath = "";
            transition(path);
        }
    }

    /**
     * Method which fills ListView with new directories or files from directory that was selected
     * by user.
     * @param dirs - list of elements from new directory.
     */
    void fill(String [] dirs){
        Log.d(TAG, "Заполнение ListView");
        paths.clear();

        if (!currentPath.equals("")){
            paths.add("...");
        }

        for (int i = 0; i < dirs.length; i++){
            paths.add(dirs[i]);
        }
        Log.d(TAG, "Создание адаптера");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                paths);
        listOfDirs.setAdapter(adapter);
    }
}
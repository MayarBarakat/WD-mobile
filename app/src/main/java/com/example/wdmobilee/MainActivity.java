package com.example.wdmobilee;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    RecyclerView rv;
    FloatingActionButton fab;
    Toolbar toolbar;
    FolderRVAdapter adapter;
    ArrayList<Folder>folders=new ArrayList<>();
    Folder fo=new Folder();
    public static final int DOWNLOAD_REQ_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.main_tool_bar);
        setSupportActionBar(toolbar);

        rv=findViewById(R.id.main_rv);
        fab=findViewById(R.id.main_fab);

        String TAG = "Permsission : ";
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            }
        }
        else {
            Log.v(TAG,"Permission is granted");

        }


        File f=new File(getFilesDir(),"WD");
        if (!(f.exists())){
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try{
            ArrayList<Folder>getFolders=new ArrayList<>();
            FileInputStream readData = new FileInputStream(f);
            ObjectInputStream readStream = new ObjectInputStream(readData);

            getFolders = (ArrayList<Folder>) readStream.readObject();
            readStream.close();
            for (Folder temp:getFolders){
                folders.add(temp);
            }

        }catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        adapter=new FolderRVAdapter(folders, new onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(String path) {
                Toast.makeText(getBaseContext(), path, Toast.LENGTH_LONG).show();
            }
        });
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new GridLayoutManager(this,2);
        rv.setLayoutManager(lm);
        rv.setHasFixedSize(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(),DownloadActivity.class);
startActivityForResult(intent,DOWNLOAD_REQ_CODE);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        SearchView searchView=
                (SearchView) menu.findItem(R.id.main_search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                ArrayList<Folder>f=fo.getFolders(folders,query);

                adapter.setFolders(f);
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                ArrayList<Folder>f=fo.getFolders(folders,newText);

                adapter.setFolders(f);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {



                adapter.setFolders(folders);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == DOWNLOAD_REQ_CODE) {
                ArrayList<Folder> folderDownloaded = new ArrayList<>();
                File f = new File(getFilesDir(), "WD");
                if (!(f.exists())) {
                    try {
                        f.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {

                    FileInputStream readData = new FileInputStream(f);
                    ObjectInputStream readStream = new ObjectInputStream(readData);

                    folderDownloaded = (ArrayList<Folder>) readStream.readObject();
                    folderDownloaded.add((Folder) Objects.requireNonNull(data).getSerializableExtra("send"));
                    readStream.close();

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                try {
                    FileOutputStream writeData = new FileOutputStream(f);
                    ObjectOutputStream writeStream = new ObjectOutputStream(writeData);

                    writeStream.writeObject(folderDownloaded);
                    writeStream.flush();
                    writeStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                adapter.setFolders(folderDownloaded);
                adapter.notifyDataSetChanged();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String TAG = "Permsission : ";
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            File mFolder = new File(Environment.getExternalStorageDirectory(), "Folder_Name");
            if (!mFolder.exists()) {
                boolean b =  mFolder.mkdirs();

            }
        }
    }
}

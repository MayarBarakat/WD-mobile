package com.example.wdmobilee;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class DownloadActivity extends AppCompatActivity implements Serializable {
    TextInputEditText et_link;
    Button btn_download,btn_done;
    Toolbar toolbar;
    LinearLayout linearLayout;
    TextView tv_info;
     ProgressBar progressBar;

     public static final String SEND_DOWNLOAD_FILE="send";
     public static final int DOWNLOAD_RES_CODE=1;
    PageFiles pageFiles=new PageFiles(Environment.getExternalStorageDirectory()+ File.separator+"WDMobile");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        et_link=findViewById(R.id.download_et_firstURL);
        btn_download=findViewById(R.id.download_btn_download);
        progressBar=findViewById(R.id.download_pb_download);
        tv_info=findViewById(R.id.download_tv_info);
        linearLayout=findViewById(R.id.download_linear);
        btn_done=findViewById(R.id.download_btn_test);
        toolbar=findViewById(R.id.download_toolbar);
        setSupportActionBar(toolbar);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();


                String[] parts=pageFiles.getMainPath().split(File.separator);
                String name=parts[parts.length-1];
                String path=pageFiles.getMainPath();
                String date=dtf.format(now);
                Folder folder=new Folder(name,path,date);

                Intent intent=new Intent();
                intent.putExtra(SEND_DOWNLOAD_FILE,folder);
                setResult(DOWNLOAD_RES_CODE,intent);
                Toast.makeText(getBaseContext(), "Download done successful", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et_link.setVisibility(View.INVISIBLE);
                btn_download.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MyTask task=new MyTask();
                        task.execute();

                    }
                });
                thread.start();


                    }
        });
    }
    class MyTask extends AsyncTask<String, Integer,Void> {

        ALLURL allurl=new ALLURL();
        ArrayList<String>types=new ArrayList<>();
        Connection connection=new Connection();
        String link=et_link.getText().toString();
        public void init(){
            types.add("HTML");
            types.add("CSS");
            types.add("JS");
            types.add("Media");
        }


        int size;
        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            init();
            ArrayList<String>allLink=new ArrayList<>();
            try {
                allLink=(ArrayList<String>) allurl.getAllLink(link,types,3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int size=allLink.size();
            progressBar.setMax(size);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            btn_done.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            try {
                int number=pageFiles.numberOfLinesIn_URLS();
                progressBar.incrementProgressBy(1);
                tv_info.setText(progressBar.getProgress()+"/" +progressBar.getMax());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                types.add("HTML");
                types.add("CSS");
                types.add("JS");
                types.add("Media");
                String url=link;
                int depth=3;
                ArrayList<String> allLink=  (ArrayList<String>) allurl.getAllLink(url,types,depth);

                pageFiles.setURLS(allLink);
                String oneURL="";
                String data="";
                while (!pageFiles.isURL_InURL_Text()){
                    oneURL=pageFiles.getOneURL();
                    data=connection.DownloadWebPage(oneURL);
                    pageFiles.saveIn(data,oneURL);
                    publishProgress();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
//            try {
////                pageFiles.repair();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            pageFiles.deleteURLs();
            pageFiles.deleteDownloading();
            return null;
        }
    }
}

package com.example.wdmobilee;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Connection {
    
    //download web page without jSoup
    public String DownloadWebPage(String url) throws IOException {
        URL u = new URL(url);
        BufferedReader readR = new BufferedReader(new InputStreamReader(u.openStream()));
        String line ;
        StringBuilder page = new StringBuilder();
        while ((line = readR.readLine()) != null){
            page.append(line).append("\n");
        }
        return page.toString();
    }
}


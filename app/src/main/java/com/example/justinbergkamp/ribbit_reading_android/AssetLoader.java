package com.example.justinbergkamp.ribbit_reading_android;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by justinbergkamp on 4/17/18.
 */

public class AssetLoader {

    Context context;

    public AssetLoader(Context c){
        context = c;
    }

    public Element getDocElement(String file_name){
        Element element = null;
        try {
            String pls = "stories/" + file_name;
            InputStream is = context.getAssets().open(pls);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            element = doc.getDocumentElement();
            element.normalize();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return element;
    }
}

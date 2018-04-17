package com.example.justinbergkamp.ribbit_reading_android;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;

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

    public Element getDocElement(String fileName){
        Element element = null;
        try {
            String pls = "stories/" + fileName;
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

    public Drawable getImage(String fileName){
        InputStream image_lookup = null;
        try {
            image_lookup = context.getAssets().open("pictures/"+fileName);
        }catch (Exception e) {
            e.printStackTrace();
        }
        Drawable d = Drawable.createFromStream(image_lookup, null);
        return d;
    }

    public AssetFileDescriptor getAudio(String fileName){
        AssetFileDescriptor afd = null;
        try {
         afd = context.getAssets().openFd("audio/"+fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return afd;
    }
}

package com.example.justinbergkamp.ribbit_reading_android;
import com.example.justinbergkamp.ribbit_reading_android.R;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class LibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        LinearLayout layout = findViewById(R.id.activity_library);

        try {
            String [] list = getAssets().list("stories");
            System.out.println(list.length);
            if (list.length > 0) {
                for (String file : list) {
                    String pls = "stories/"+file;
                    InputStream is = getAssets().open(pls);
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(is);
                    Element element = doc.getDocumentElement();
                    element.normalize();
                    Node cover_image = doc.getElementById("cover_image");
                    //add to the screen as an ImageButton
                    String ex = "pictures/"+cover_image.getTextContent();
                    InputStream image_lookup = getAssets().open(ex);
                    Drawable d = Drawable.createFromStream(image_lookup, null);
                    ImageButton ib = new ImageButton(this);
                    ib.setImageDrawable(d);
                    layout.addView(ib);
                    is.close();
                    image_lookup.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String tag, Element e) {

        NodeList node = e.getElementsByTagName(tag).item(0).getChildNodes();
        Node nodes = node.item(0);
        return nodes.getNodeValue();

    }


}

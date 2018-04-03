package com.example.justinbergkamp.ribbit_reading_android;
import com.example.justinbergkamp.ribbit_reading_android.R;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
                for (final String file : list) {
                    String pls = "stories/"+file;
                    InputStream is = getAssets().open(pls);
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(is);
                    Element element = doc.getDocumentElement();
                    element.normalize();
                    Node cover_image = doc.getElementById("cover_image");
                    final String story_title = doc.getElementsByTagName("title").item(0).getTextContent();
                    //add to the screen as an ImageButton
                    String ex = "pictures/"+cover_image.getTextContent();
                    InputStream image_lookup = getAssets().open(ex);
                    Drawable d = Drawable.createFromStream(image_lookup, null);
                    ImageButton ib = new ImageButton(this);
                    ib.setImageDrawable(d);
                    ib.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //need to either go to a landing page or start a new story activity
                            Toast.makeText(LibraryActivity.this,  story_title,
                                    Toast.LENGTH_SHORT).show();
                            goToStory(file);
                        }
                    });
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

    public void goToStory(String f ){
        Intent intent = new Intent(this, LandingActivity.class);
        intent.putExtra("EXTRA_FILE_NAME", f);
        startActivity(intent);
    };


}

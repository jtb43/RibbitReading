package com.example.justinbergkamp.ribbit_reading_android;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class LandingActivity extends AppCompatActivity {
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String file_name = getIntent().getStringExtra("EXTRA_FILE_NAME");
        getInformation(file_name);
    }
    public void getInformation(final String file){
        ImageButton cover = findViewById(R.id.cover_image_landing);
        TextView title = findViewById(R.id.title_landing);
        TextView description = findViewById(R.id.description_landing);

        try {
            String pls = "stories/" + file;
            InputStream is = getAssets().open(pls);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();
            Node cover_image = doc.getElementById("cover_image");
            final String story_title = doc.getElementsByTagName("title").item(0).getTextContent();
            final String story_description = doc.getElementsByTagName("description").item(0).getTextContent();
            title.setText(story_title);
            description.setText(story_description);
            //add to the screen as an ImageButton
            String ex = "pictures/" + cover_image.getTextContent();
            InputStream image_lookup = getAssets().open(ex);
            Drawable d = Drawable.createFromStream(image_lookup, null);
            cover.setImageDrawable(d);

            cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accessStory(file);
                }
            });


        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void accessStory(String f){
        System.out.println("Attempting to access story");
        Intent intent = new Intent(this, StoryActivity.class);
        intent.putExtra("EXTRA_FILE_NAME", f);
        intent.putExtra("USER_NAME", "Justin");
        startActivity(intent);
    };

    public void askName(){
        
    }

}

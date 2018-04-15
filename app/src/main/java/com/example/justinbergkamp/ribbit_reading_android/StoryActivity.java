package com.example.justinbergkamp.ribbit_reading_android;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jp.wasabeef.blurry.Blurry;

public class StoryActivity extends AppCompatActivity {
    List pages;
    int currentPage;
    List choices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        String file_name = getIntent().getStringExtra("EXTRA_FILE_NAME");
        pages = new ArrayList();
        currentPage = 0;
        Element element = null;
        try {
            String pls = "stories/" + file_name;
            InputStream is = getAssets().open(pls);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            element = doc.getDocumentElement();
            element.normalize();
        }catch (Exception e) {
            e.printStackTrace();
        }
        loadPages(element);
        getContent();
    }

    public void getContent(){
        System.out.println("Number pages = "+pages.size());
        System.out.println("Current pages = "+currentPage);

        if(pages.size()==currentPage){
           endOfStory();
           return;
       }
        Node n = (Node) pages.get(currentPage);
        TextView content = findViewById(R.id.story_content);
        ImageView background = findViewById(R.id.story_background);
        if(n.hasAttributes()){
            //this is a fork node
            choices = new ArrayList();
            NodeList children = n.getChildNodes();
            for (int x = 0; x<children.getLength();x++){
                if (children.item(x).getNodeName().equals("choice")){
                    choices.add(children.item(x));
                }
            }

            createChoices(n,content, background, choices);

            //initialize image buttons and question
            //set on click listeners
            //pass to choice method with an int repping the choice made
        }else{
            content.setText(getText(n));
            background.setImageDrawable(getImage(n));
            //normal page node
            //display things normally
        };
    };


    private void createChoices(Node n, TextView content, ImageView background, List choices){
        content.setText(getText(n));
        background.setImageDrawable(getImage(n));
        Blurry.with(StoryActivity.this).radius(25)
                .sampling(1)
                .color(Color.argb(66, 0, 255, 255))
                .async()
                .capture(background)
                .into((ImageView) background);

        ImageButton first = findViewById(R.id.choice_one);
        ImageButton second = findViewById(R.id.choice_two);
        first.setImageDrawable(getImage((Node) choices.get(0)));
        second.setImageDrawable(getImage((Node) choices.get(1)));
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeChoice(0);
            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeChoice(1);
            }
        });
        first.setVisibility(View.VISIBLE);
        second.setVisibility(View.VISIBLE);
        //instantiate buttons
        //blur screen
        //add question
        //create buttons

    }

    private void endOfStory(){
        //out of pages
        Intent intent = new Intent(this, LibraryActivity.class);
        startActivity(intent);
    }

    public void makeChoice(int i){
        Element p = (Element) choices.get(i);
        loadPages(p);
        getContent();
    }
    public void nextPage(View view){
      currentPage++;
      getContent();
    };

    public String getText(Node n){
        NodeList children = n.getChildNodes();
        for(int x = 0; x< children.getLength();x++){
            if(children.item(x).getNodeName().equals("text")){
                return children.item(x).getTextContent();
            }
        }
        return "Error loading content :(";
    }

    public Drawable getImage(Node n){
        NodeList children = n.getChildNodes();
        String picName = "";
        for(int x = 0; x< children.getLength();x++){
            if(children.item(x).getNodeName().equals("image")){
                picName = children.item(x).getTextContent();
                break;
            }
        }
        String ex = "pictures/"+picName;
        InputStream image_lookup = null;
        try {
             image_lookup = getAssets().open(ex);
        }catch (Exception e) {
                e.printStackTrace();
            }
        Drawable d = Drawable.createFromStream(image_lookup, null);
        return d;
    }


    public void loadPages(Element element){
        pages.clear();
        NodeList children = element.getChildNodes();
        for (int x = 0; x<children.getLength();x++){
            if (children.item(x).getNodeName().equals("page")){
                //this is a page node
                pages.add(children.item(x));
            }
        }
        currentPage=0;
    }
}

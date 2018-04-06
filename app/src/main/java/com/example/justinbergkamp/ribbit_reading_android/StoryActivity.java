package com.example.justinbergkamp.ribbit_reading_android;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
        Node n = (Node) pages.get(currentPage);
        TextView content = findViewById(R.id.story_content);

        if(n.hasAttributes()){
            //this is a fork node
            choices = new ArrayList();
            NodeList children = n.getChildNodes();
            for (int x = 0; x<children.getLength();x++){
                if (children.item(x).getNodeName().equals("choice")){
                    choices.add(children.item(x));
                }
            }

            Element p = (Element) choices.get(1);
            loadPages(p);
            getContent();
            //initialize image buttons and question
            //set on click listeners
            //pass to choice method with an int repping the choice made
        }else{
            content.setText(getText(n));
            //normal page node
            //display things normally
        };
    };

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

package com.example.justinbergkamp.ribbit_reading_android;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
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
    String user_name = "Lily";
    ArrayList sounds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        String file_name = getIntent().getStringExtra("EXTRA_FILE_NAME");
        user_name = getIntent().getStringExtra("USER_NAME");
        pages = new ArrayList();
        currentPage = 0;
        sounds = new ArrayList();
        AssetLoader al = new AssetLoader(StoryActivity.this);
        Element element = al.getDocElement(file_name);
        loadPages(element);
        getContent();
    }

    public void loadPages(Element element){
        //This method finds all pages in the document and adds them to an arraylist named pages
        pages.clear();
        nestedLoop(element, pages, "page");
        currentPage=0;
    }

    public void getContent(){
        Node n = (Node) pages.get(currentPage);
        TextView content = findViewById(R.id.story_content);
        ImageView background = findViewById(R.id.story_background);

        if(n.hasAttributes()){
            //this is a fork node
            //remove the next button
            ImageButton next = findViewById(R.id.imageButton3);
            next.setVisibility(View.GONE);
            choices = new ArrayList();
            nestedLoop(n,choices,"choice");
            createChoices(n,content, background, choices);
        }else{
            //it's just a normal page
            //Add text, image, and sounds
            content.setText(getText(n));
            background.setImageDrawable(getImage(n));
            getSounds(n);
        }
    }

    private void nestedLoop(Node n, List list, String type ){
        NodeList children = n.getChildNodes();
        for (int x = 0; x<children.getLength();x++){
            if (children.item(x).getNodeName().equals(type)){
                list.add(children.item(x));
            }
        }
    }

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
    }

    private void endOfStory(){
        pages.clear();
        currentPage=0;
        Intent intent = new Intent(this, LibraryActivity.class);
        startActivity(intent);
    }

    public void makeChoice(int i){
        Element p = (Element) choices.get(i);
        loadPages(p);
        getContent();
        ImageButton first = findViewById(R.id.choice_one);
        ImageButton second = findViewById(R.id.choice_two);
        first.setVisibility(View.GONE);
        second.setVisibility(View.GONE);
        ImageButton next = findViewById(R.id.imageButton3);
        next.setVisibility(View.VISIBLE);


    }
    public void nextPage(View view){
      currentPage++;
        if(pages.size()==currentPage){
            endOfStory();
            return;
        }
      getContent();
    }

    public String getText(Node n){
        NodeList children = n.getChildNodes();
        for(int x = 0; x< children.getLength();x++){
            if(children.item(x).getNodeName().equals("text")){
                String s = children.item(x).getTextContent();
                System.out.println("Justin- "+s);
                String r = s.replaceAll("NAME", user_name);
                System.out.println("Justin- "+r);
                return r;
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


    public void getSounds(Node n){
        NodeList children = n.getChildNodes();
        sounds.clear();
        for(int x = 0; x< children.getLength();x++){
            if(children.item(x).getNodeName().equals("audio")){
                sounds.add(children.item(x).getTextContent());
                MediaPlayer player = new MediaPlayer();
                playSounds(player,children.item(x).getTextContent() );
            }
        }
    }

    public void playSounds(MediaPlayer m, String fileName) {
        try {
            if (m.isPlaying()) {
                m.stop();
                m.release();
                m = new MediaPlayer();
            }
            String s = "audio/"+fileName;
            AssetFileDescriptor descriptor = getAssets().openFd(s);
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            m.prepare();
            m.setVolume(1f, 1f);
            //m.setLooping(true);
            m.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

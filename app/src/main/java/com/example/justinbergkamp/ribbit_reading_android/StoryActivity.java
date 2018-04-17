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
    ArrayList<Node> sounds;
    AssetLoader al;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        String file_name = getIntent().getStringExtra("EXTRA_FILE_NAME");
        user_name = getIntent().getStringExtra("USER_NAME");
        pages = new ArrayList();
        currentPage = 0;

        sounds = new ArrayList();

        al = new AssetLoader(StoryActivity.this);
        Element element = al.getDocElement(file_name);
        loadPages(element);
    }

    public void loadPages(Element element){
        //This method finds all pages in the document and adds them to an arraylist named pages
        pages.clear();
        nestedLoop(element, pages, "page");
        currentPage=0;
        getContent();
    }

    public void getContent(){
        Node n = (Node) pages.get(currentPage);
        TextView content = findViewById(R.id.story_content);
        ImageView background = findViewById(R.id.story_background);

        if(n.hasAttributes()){
            //this is a fork node
            //remove the 'next' button
            content.setText(getText(n));
            background.setImageDrawable(getImage(n));
            getSounds(n);
            
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
        //find all children of a given type
        NodeList children = n.getChildNodes();
        for (int x = 0; x<children.getLength();x++){
            if (children.item(x).getNodeName().equals(type)){
                list.add(children.item(x));
            }
        }
    }

    private void createChoices(Node n, TextView content, ImageView background, List choices){
        //blurs background image
        Blurry.with(StoryActivity.this).radius(25)
                .sampling(1)
                .color(Color.argb(66, 0, 255, 255))
                .async()
                .capture(background)
                .into((ImageView) background);
        ImageButton first = findViewById(R.id.choice_one);
        ImageButton second = findViewById(R.id.choice_two);
        makeChoiceButton(first, 0, choices);
        makeChoiceButton(second, 1, choices);
    }

    private void makeChoiceButton(ImageButton ib, final int choiceNum, List choices){
        ib.setImageDrawable(getImage((Node) choices.get(choiceNum)));
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeChoice(choiceNum);
            }
        });
        ib.setVisibility(View.VISIBLE);
    }

    public void makeChoice(int i){
        Element p = (Element) choices.get(i);
        //the new list of pages will be those under the chosen option
        loadPages(p);
        ImageButton first = findViewById(R.id.choice_one);
        ImageButton second = findViewById(R.id.choice_two);
        first.setVisibility(View.GONE);
        second.setVisibility(View.GONE);
        ImageButton next = findViewById(R.id.imageButton3);
        next.setVisibility(View.VISIBLE);
    }

    private void endOfStory(){
        pages.clear();
        currentPage=0;
        Intent intent = new Intent(this, LibraryActivity.class);
        startActivity(intent);
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
        List<Node> text = new ArrayList();
        nestedLoop(n, text, "text");
        String sentence = text.get(0).getTextContent().replaceAll("NAME", user_name);
        return sentence;
    }

    public Drawable getImage(Node n){
        ArrayList<Node> pictures = new ArrayList();
        nestedLoop(n,pictures,"image");
        String picName = pictures.get(0).getTextContent();
        return al.getImage(picName);
    }


    public void getSounds(Node n){
        sounds.clear();
        nestedLoop(n, sounds, "audio");
        for(int x = 0; x< sounds.size();x++) {
            playSounds(sounds.get(x).getTextContent());
        }
    }

    public void playSounds(String fileName) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor afd = StoryActivity.this.getAssets().openFd("audio/"+fileName);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mediaPlayer.prepare();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.start();

    }
}

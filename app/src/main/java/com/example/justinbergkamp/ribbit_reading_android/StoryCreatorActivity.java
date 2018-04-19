package com.example.justinbergkamp.ribbit_reading_android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Xml;
import android.view.View;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class StoryCreatorActivity extends AppCompatActivity {
    String story_title = "title ";
    String story_description= "desp ";
    String story_cover_image = " image";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_creator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    public void attempt(View view) {
        try {
            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // root element
            Element rootElement = doc.createElement("story");
            doc.appendChild(rootElement);

            // title element
            Element title = doc.createElement("title");
            title.appendChild(doc.createTextNode(story_title));
            rootElement.appendChild(title);


            // description element
            Element description = doc.createElement("description");
            description.appendChild(doc.createTextNode(story_description));
            rootElement.appendChild(description);


            // image element
            Element cover_image = doc.createElement("cover_image");
            cover_image.appendChild(doc.createTextNode(story_cover_image));
            rootElement.appendChild(cover_image);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File story = new File(getDir("newStory",MODE_PRIVATE),"story.xml");
            System.out.println("HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            StreamResult result = new StreamResult(story);
            transformer.transform(source, result);
            System.out.println("HERE AGAIN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

            // Output to console for testing
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
            System.out.println("HERE THE THIRD TIME!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void internal(String f){
        String d = f+"_folder";
        File directory = StoryCreatorActivity.this.getDir(d, MODE_PRIVATE);
        File file = new File(directory, f);
        File fl = new File(getDir(d,MODE_PRIVATE),f);

    }

    public void test(View view) {
        try {
            System.out.println("Justin - In the test function");
            FileInputStream fis = StoryCreatorActivity.this.openFileInput(addXMLTest());
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            System.out.println(sb);

            System.out.println("K");
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String addXMLTest(){
        //Alright so this actually works, but the serialization process might make it more of a pain to create and parse
        String filename = "fakefile.txt";

        FileOutputStream fos = null;
        try {
            fos = openFileOutput(filename, StoryCreatorActivity.this.MODE_APPEND);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        XmlSerializer serializer = Xml.newSerializer();
        try {
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, Boolean.valueOf(true));

            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

            serializer.startTag(null, "story");
            serializer.startTag(null, "title");
            serializer.text("Title - Justin");
            serializer.endTag(null, "title");
            serializer.startTag(null, "description");
            serializer.text("Descprip - Justin");
            serializer.endTag(null, "description");


            serializer.endDocument();

            serializer.flush();

            fos.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return filename;

    }

    private String addFileTest(){
        String filename = "myfile";
        String fileContents = "Hello world!";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, StoryCreatorActivity.this.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filename;

        //so this works, what does that tell me. it means that this method of adding the file works fine.
        //somehow I have to adapt this method of adding files to XML
        //
    }

}

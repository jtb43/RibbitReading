package com.example.justinbergkamp.ribbit_reading_android;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import java.io.File;
import java.io.FileOutputStream;

public class StoryCreatorActivity extends AppCompatActivity {
    String story_title;
    String story_description;
    String story_cover_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_creator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    public  void attempt() {

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
            StreamResult result = new StreamResult(new File("C:\\cars.xml"));
            transformer.transform(source, result);

            // Output to console for testing
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void internal(String f){
        String d = f+"_folder";
        File directory = StoryCreatorActivity.this.getDir(d, MODE_PRIVATE);
        File file = new File(directory, f);

    }

    public void test(){
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
    }
}

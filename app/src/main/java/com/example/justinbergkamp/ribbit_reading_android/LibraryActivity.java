package com.example.justinbergkamp.ribbit_reading_android;
import com.example.justinbergkamp.ribbit_reading_android.R;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

            InputStream is = getAssets().open("friends.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();

            element.normalize();
            NodeList nodeList = doc.getElementsByTagName("employee");
            for (int i = 0; i < nodeList.getLength(); i++) {

                TextView text1 = new TextView(this);
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;
                    text1.setTextSize(15);
                    text1.setTextColor(0xFFFF0000);
                    text1.setText(text1.getText() + "Name: " + getValue("name", element2) + "\n");
                    text1.setText(text1.getText() + "Profession: " + getValue("profession", element2) + "\n");
                    layout.addView(text1);
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

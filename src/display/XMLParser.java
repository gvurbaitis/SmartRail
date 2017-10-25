package display;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.io.File;

public class XMLParser
{
    public void parse()
    {
        try
        {
            File fXmlFile = new File("./resources/Config_01.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            System.out.println("Smart Rail Lane Distribution");
            NodeList laneList = doc.getElementsByTagName("lane");
            NodeList stationList = doc.getElementsByTagName("station");
            System.out.println("-----------------------------");

            int temp = 0;
            for (int i = 0; i < laneList.getLength(); i++)
            {
                Element e = (Element) laneList.item(i);
                System.out.println("Lane Number: " + e.getAttribute("number"));

                for (int j = temp; j < temp + 2; j++)
                {
                    Element s = (Element) stationList.item(j);
                    String stationType;

                    if (j == 0 || (j % 2 == 0)) stationType = "Departure: ";
                    else stationType = "Destination: ";

                    System.out.println(stationType+ s.getTextContent());
                }
                temp += 2;
                System.out.println("-----------------------------");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
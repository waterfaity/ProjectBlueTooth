package com.waterfairy.tool.xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by water_fairy on 2017/2/16.
 */

public class PullPersonParser extends IPullParser {
    public static final PullPersonParser PULL_PERSON_PARSER = new PullPersonParser();

    public static PullPersonParser getPullPersonParser() {
        return PULL_PERSON_PARSER;
    }

    @Override
    List<Person> readXml(InputStream inputStream) throws XmlPullParserException, IOException {
        super.readXml(inputStream);
        Person person = null;
        while (eventCode != XmlPullParser.END_DOCUMENT) {
            switch (eventCode) {
                case XmlPullParser.START_DOCUMENT:
                    objectList = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    if ("person".equals(xmlPullParser.getName())) {
                        person = new Person();
                    } else if (person != null) {
                        String name = xmlPullParser.getName();
                        String nextText = xmlPullParser.nextText();

                        switch (name) {
                            case "name":
                                person.setName(nextText);
                                break;
                            case "age":
                                person.setAge(Integer.parseInt(nextText));
                                break;
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("person".equals(xmlPullParser.getName()) && person != null) {
                        objectList.add(person);
                        person = null;
                    }
                    break;
            }
            eventCode = xmlPullParser.next();
        }
        return objectList;
    }

    @Override
    void writeXml(List objectList, OutputStream outputStream) throws IOException {
        if (objectList != null) {
            super.writeXml(objectList, outputStream);
            xmlSerializer.startTag(null, "persons");
            for (int i = 0; i < objectList.size(); i++) {
                Person person = (Person) objectList.get(i);
                xmlSerializer.startTag(null, "person");
                xmlSerializer.startTag(null, "name");
                xmlSerializer.text(person.getName());
                xmlSerializer.endTag(null, "name");
                xmlSerializer.startTag(null, "age");
                xmlSerializer.text(person.getAge() + "");
                xmlSerializer.endTag(null, "age");
                xmlSerializer.endTag(null, "person");
            }
            xmlSerializer.endTag(null, "persons");
            xmlSerializer.endDocument();
            outputStream.flush();
            outputStream.close();
        }

    }
}

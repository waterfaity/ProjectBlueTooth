package com.waterfairy.tool.xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by water_fairy on 2017/2/16.
 */

public abstract class IPullParser<T> {
    int eventCode;
    XmlPullParser xmlPullParser;
    XmlSerializer xmlSerializer;
    List<T> objectList;

    List<T> readXml(InputStream inputStream) throws IOException, XmlPullParserException {
        xmlPullParser = Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        eventCode = xmlPullParser.getEventType();
        return objectList;
    }

    void writeXml(List<T> objectList, OutputStream outputStream) throws IOException {
        xmlSerializer = Xml.newSerializer();
        xmlSerializer.setOutput(outputStream, "UTF-8");
        xmlSerializer.startDocument("UTF-8", true);
    }

}

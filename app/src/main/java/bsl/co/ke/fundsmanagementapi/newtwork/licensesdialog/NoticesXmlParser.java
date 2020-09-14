package bsl.co.ke.fundsmanagementapi.newtwork.licensesdialog;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import bsl.co.ke.fundsmanagementapi.newtwork.licensesdialog.licenses.License;
import bsl.co.ke.fundsmanagementapi.newtwork.licensesdialog.model.Notice;
import bsl.co.ke.fundsmanagementapi.newtwork.licensesdialog.model.Notices;


public final class NoticesXmlParser {

    private NoticesXmlParser() {
    }

    public static Notices parse(final InputStream inputStream) throws Exception {
        try {
            final XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return parse(parser);
        } finally {
            inputStream.close();
        }
    }

    private static Notices parse(final XmlPullParser parser) throws IOException, XmlPullParserException {
        final Notices notices = new Notices();
        parser.require(XmlPullParser.START_TAG, null, "notices");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            final String name = parser.getName();
            // Starts by looking for the entry tag
            if ("notice".equals(name)) {
                notices.addNotice(readNotice(parser));
            } else {
                skip(parser);
            }
        }
        return notices;
    }

    private static Notice readNotice(final XmlPullParser parser) throws IOException,
        XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "notice");
        String name = null;
        String url = null;
        String copyright = null;
        License license = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            final String element = parser.getName();
            if ("name".equals(element)) {
                name = readName(parser);
            } else if ("url".equals(element)) {
                url = readUrl(parser);
            } else if ("copyright".equals(element)) {
                copyright = readCopyright(parser);
            } else if ("license".equals(element)) {
                license = readLicense(parser);
            } else {
                skip(parser);
            }
        }
        return new Notice(name, url, copyright, license);
    }

    private static String readName(final XmlPullParser parser) throws IOException, XmlPullParserException {
        return readTag(parser, "name");
    }

    private static String readUrl(final XmlPullParser parser) throws IOException, XmlPullParserException {
        return readTag(parser, "url");
    }

    private static String readCopyright(final XmlPullParser parser) throws IOException, XmlPullParserException {
        return readTag(parser, "copyright");
    }

    private static License readLicense(final XmlPullParser parser) throws IOException, XmlPullParserException {
        final String license = readTag(parser, "license");
        return LicenseResolver.read(license);
    }

    private static String readTag(final XmlPullParser parser, final String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, tag);
        final String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, tag);
        return title;
    }

    private static String readText(final XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private static void skip(final XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}

package com.keithsmyth.reader.data;

import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;

import com.keithsmyth.reader.model.Entry;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GetRssTask extends AsyncTask<String, Void, List<Entry>> {

    private final Listener listener;
    private String error;

    public GetRssTask(Listener listener) {
        this.listener = listener;
    }

    @Override protected List<Entry> doInBackground(String... params) {
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
            .url(params[0])
            .build();
        final Response response;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            return null;
        }

        try {
            final XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            final XmlPullParser parser = factory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(response.body().charStream());
            parser.nextTag();

            final List<Entry> items = new ArrayList<>();
            parser.require(XmlPullParser.START_TAG, null, "feed");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {continue;}
                final String name = parser.getName();
                if ("entry".equals(name)) {
                    processEntry(parser, items);
                } else {
                    skip(parser);
                }
            }
            return items;
        } catch (XmlPullParserException | IOException e) {
            error = e.getMessage();
            return null;
        }

    }

    @Override protected void onPostExecute(List<Entry> items) {
        if (items == null) {
            listener.onError(error);
        } else {
            listener.onGet(items);
        }
    }

    private void processEntry(XmlPullParser parser, List<Entry> items) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "entry");
        String id = null;
        String title = null;
        Date published = null;
        String url = null;
        String thumbnail = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) { continue; }
            final String name = parser.getName();
            switch (name) {
                case "id":
                    id = readTextTag(parser, "id");
                    break;
                case "title":
                    title = readTextTag(parser, "title");
                    break;
                case "published":
                    published = readTextTagAsDate(parser, "published");
                    break;
                case "link":
                    final String tempUrl = readAttributeConditional(parser, "link", "href", new Pair<>("rel", "alternate"));
                    if (!TextUtils.isEmpty(tempUrl)) {
                        url = tempUrl;
                    }
                    break;
                case "media:thumbnail":
                    thumbnail = readAttribute(parser, "media:thumbnail", "url");
                    break;
                default:
                    skip(parser);
            }
        }
        items.add(new Entry(id, title, published, url, thumbnail));
    }

    private String readTextTag(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, tag);
        final String text = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, tag);
        return text;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        final String result;
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        } else {
            result = "";
        }
        return result;
    }

    private Date readTextTagAsDate(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        final String value = readTextTag(parser, tag);
        final DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS", Locale.getDefault());
        try {
            return format.parse(value);
        } catch (ParseException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
            return null;
        }
    }

    private String readAttributeConditional(XmlPullParser parser, String tag, String attribute, Pair<String, String> condition) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, tag);
        final String conditionalValue = parser.getAttributeValue(null, condition.first);
        final String text = condition.second.equalsIgnoreCase(conditionalValue) ? parser.getAttributeValue(null, attribute) : "";
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, tag);
        return text;
    }

    private String readAttribute(XmlPullParser parser, String tag, String attribute) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, tag);
        final String text = parser.getAttributeValue(null, attribute);
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, null, tag);
        return text;
    }

    private void skip(XmlPullParser parser) throws IOException, XmlPullParserException {
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

    public interface Listener {
        void onGet(List<Entry> items);

        void onError(String msg);
    }
}

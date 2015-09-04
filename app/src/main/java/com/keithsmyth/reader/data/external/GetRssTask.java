package com.keithsmyth.reader.data.external;

import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;

import com.keithsmyth.reader.data.DataUtils;
import com.keithsmyth.reader.model.Entry;
import com.keithsmyth.reader.model.Feed;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetRssTask extends AsyncTask<String, Void, Feed> {

    private static final String TAG = "GetRssTask";

    private final Listener listener;
    private String error;

    public GetRssTask(Listener listener) {
        this.listener = listener;
    }

    @Override protected Feed doInBackground(String... params) {
        final String url = params[0];
        Log.d(TAG, "doInBackground() called with: " + "params = [" + url + "]");
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
            .url(url)
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

            String feedId = null;
            String feedName = null;

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {continue;}

                final String name = parser.getName();
                switch (name) {
                    case "id":
                        feedId = readTextTag(parser, "id");
                        break;
                    case "title":
                        feedName = readTextTag(parser, "title");
                        break;
                    case "entry":
                        if (TextUtils.isEmpty(feedId)) {
                            throw new RuntimeException("No feed Id before entries");
                        }
                        processEntry(parser, items, feedId);
                        break;
                    default:
                        skip(parser);
                }
            }

            // create a new feed object
            final Feed feed = new Feed(feedId, feedName, url, new Date());
            feed.entries.addAll(items);

            return feed;
        } catch (XmlPullParserException | IOException e) {
            error = e.getMessage();
            return null;
        }

    }

    @Override
    protected void onPostExecute(Feed feed) {
        if (feed == null) {
            listener.onError(error);
        } else {
            listener.onGet(feed);
        }
    }

    private void processEntry(XmlPullParser parser, List<Entry> items, String feedId) throws IOException, XmlPullParserException {
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
        items.add(new Entry(id, feedId, title, published, url, thumbnail, false));
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
        return DataUtils.parseDate(value);
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
        void onGet(Feed feed);

        void onError(String msg);
    }
}

package com.guo.duoduo.dlnaserver;


import java.net.URLEncoder;

import org.cybergarage.net.HostInterface;
import org.cybergarage.upnp.std.av.server.MediaServer;
import org.cybergarage.upnp.std.av.server.UPnP;
import org.cybergarage.upnp.std.av.server.object.container.ContainerNode;
import org.cybergarage.upnp.std.av.server.object.item.ItemNode;
import org.cybergarage.xml.Attribute;
import org.cybergarage.xml.AttributeList;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;


public class Dlna_MediaServer
{

    public final String AUDIO_ID = "2";
    public final String AUDIO_TITLE = "音频";
    public final String MIME_AUDIO = "audio/*";

    public final static int port = 8192;
    private MediaServer mediaServer = null;
    private boolean isStop = false;

    private Context mContext;

    public Dlna_MediaServer(Context context, String dms_name)
    {
        mContext = context;
        mediaServer = new MediaServer();
        mediaServer.setFriendlyName(dms_name);
        mediaServer.setManufacture("zte");
        mediaServer.setModelName("zte");
        mediaServer.setManufactureURL("zte");
        setMediaContent();
    }

    private void setMediaContent()
    {
        addMusic();
    }

    private void addMusic()
    {
        ContainerNode music = new ContainerNode();
        music.setTitle(AUDIO_TITLE);
        music.setID(AUDIO_ID);
        music.setParentID(0);
        music.setRestricted(1);
        music.setContentDirectory(mediaServer.getContentDirectory());
        mediaServer.getContentDirectory().getRootNode().addNode(music);

        String[] audioColumns = {MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DURATION};
        Cursor cursor = mContext.getContentResolver().query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, audioColumns, null, null, null);

        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                String title = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String filePath = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                long size = cursor.getLong(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                long duration = cursor.getLong(cursor
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

                String id = title + filePath.substring(filePath.lastIndexOf("."));//gpf add

                ItemNode item = new ItemNode();
                item.setID(id);
                item.setTitle(title);
                item.setRestricted(0);
                item.setParentID(AUDIO_ID);
                item.setUPnPClass(UPnP.OBJECT_ITEM_AUDIOITEM_AUDIO);
                String url = "http://" + HostInterface.getIPv4Address() + ":" + port + "/"
                    + URLEncoder.encode(id);
                String protocolInfo = "http-get:" + "*:" + MIME_AUDIO + ":" + "*";
                AttributeList attrList = new AttributeList();
                Attribute attrSize = new Attribute("size", Long.toString(size));
                attrList.add(attrSize);
                String dur = convertToDlnaDuration(duration);
                Attribute attrDuration = new Attribute("duration", dur);
                attrList.add(attrDuration);
                item.setResource(url, protocolInfo, attrList);

                music.addNode(item);
                music.setChildCount(music.getChildCount() + 1);

            } while (cursor.moveToNext() && !isStop);
        }
    }

    public void stop()
    {
        isStop = true;

        if (mediaServer != null)
        {
            mediaServer.stop();
            mediaServer.removeAllContentDirectories();
        }
    }

    public void start()
    {
        mediaServer.start();
    }

    private String convertToDlnaDuration(long duration)
    {
        return duration / (1000 * 60 * 60) + ":" + (duration % (1000 * 60 * 60))
            / (1000 * 60) + ":" + (duration % (1000 * 60)) / 1000;
    }

}

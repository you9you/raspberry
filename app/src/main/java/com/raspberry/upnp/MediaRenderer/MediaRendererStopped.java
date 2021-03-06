package com.raspberry.upnp.MediaRenderer;

import com.raspberry.VideoService;

import android.util.Log;

import org.fourthline.cling.support.avtransport.impl.state.Stopped;
import org.fourthline.cling.support.avtransport.lastchange.AVTransportVariable;
import org.fourthline.cling.support.model.AVTransport;
import org.fourthline.cling.support.model.MediaInfo;
import org.fourthline.cling.support.model.PositionInfo;
import org.fourthline.cling.support.model.SeekMode;

import java.net.URI;

public class MediaRendererStopped extends Stopped {

    public MediaRendererStopped(AVTransport transport) {
        super(transport);
    }

    public void onEntry() {
        super.onEntry();
        // Optional: Stop playing, release resources, etc.
    }

    @Override
    public Class<?> setTransportURI(URI uri, String metaData) {
        // This operation can be triggered in any state, you should think
        // about how you'd want your player to react. If we are in Stopped
        // state nothing much will happen, except that you have to set
        // the media and position info, just like in MyRendererNoMediaPresent.
        // However, if this would be the MyRendererPlaying state, would you
        // prefer stopping first?
        getTransport().setMediaInfo(new MediaInfo(uri.toString(), metaData));

        // if you can, you should find and set the duration of the track here!
        getTransport().setPositionInfo(new PositionInfo(1, metaData, uri.toString()));

        // it's up to you what "last changes" you want to announce to event listeners
        getTransport().getLastChange().setEventedValue(
                getTransport().getInstanceId(),
                new AVTransportVariable.AVTransportURI(uri),
                new AVTransportVariable.CurrentTrackURI(uri)
        );

        VideoService.getEvent().onNewMedia(uri, metaData);
        //MainActivity.newMedia(uri);

        return MediaRendererStopped.class;
    }

    @Override
    public Class<?> stop() {
        /// Same here, if you are stopped already and someone calls STOP, well...
        VideoService.destroyAll();
        return MediaRendererStopped.class;
    }

    @Override
    public Class<?> play(String speed) {
        // It's easier to let this classes' onEntry() method do the work
        System.out.println("speed:" + speed);
        return MediaRendererPlaying.class;
    }

    @Override
    public Class<?> next() {
        return MediaRendererStopped.class;
    }

    @Override
    public Class<?> previous() {
        return MediaRendererStopped.class;
    }

    @Override
    public Class<?> seek(SeekMode unit, String target) {
        // Implement seeking with the stream in stopped state!
        Log.d("cling", "seek: " + target);
        return MediaRendererStopped.class;
    }
}
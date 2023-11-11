package app.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class TrackScheduler extends AudioEventAdapter {
    private AudioPlayer player;
    private final BlockingDeque<AudioTrack> queue = new LinkedBlockingDeque<>();

    public TrackScheduler(AudioPlayer player){
        this.player = player;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        player.startTrack(queue.poll(), false);
    }

    public void queue(AudioTrack track){
        if (!player.startTrack(track, true)){
            queue.offer(track);
        }
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public void setPlayer(AudioPlayer player) {
        this.player = player;
    }

    public BlockingDeque<AudioTrack> getQueue() {
        return queue;
    }
}
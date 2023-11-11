package app.events.guild.voice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static app.configs.Intialize.bot;
import static app.statics.Functions.paraContagemTempo;

public class VoiceLeft extends ListenerAdapter {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {

        try {
            ObjectNode config = objectMapper.readValue(new File(("config.json")), ObjectNode.class);
            String memberMention = event.getMember().getAsMention();
            String memberEffectiveName = event.getMember().getEffectiveName();
            if (event.getGuild().equals(bot.getGuildById(config.get("guild").asLong()))) {
                if (event.getChannelJoined() == null && event.getChannelLeft() != null) {
                    LocalDateTime dateTime = LocalDateTime.now();

                    if (!event.getMember().getVoiceState().isSelfMuted()) {
                        paraContagemTempo(event.getMember(), dateTime);
                    }

                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public String toString() {
        return "Evento - VoiceLeft";
    }


}

package app.lavaplayer;

import app.commands.mannagers.SCommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class Play implements SCommand {
    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "Coloca a musica selecionada na fila";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.STRING, "musica", "Nome da musica desejada"));
        return data;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();
        if (!memberVoiceState.inAudioChannel()){
            event.reply("Você deve estar em um canal de voz").setEphemeral(true).queue();
            return;
        }

        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inAudioChannel()) {
            event.getGuild().getAudioManager().openAudioConnection(memberVoiceState.getChannel());
        }
        else  {
            if (selfVoiceState.getChannel() != memberVoiceState.getChannel()) {
                event.reply("Você deve estar no mesmo canal de voz que eu.").setEphemeral(true).queue();
                return;
            }
        }

        PlayerManager playerManager = PlayerManager.get();
        event.reply("Tocando agora").queue();
        playerManager.play(event.getGuild(), event.getOption("name").getAsString());
    }
}

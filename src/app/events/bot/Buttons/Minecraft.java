package app.events.bot.Buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

import static app.statics.Basics.ygd;
import static app.statics.cargos.Funcionais.cargoMinecraft;

public class Minecraft extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (Objects.equals(event.getButton().getId(), "acessomine")){
            if (!event.getMember().getRoles().contains(cargoMinecraft)){
                ygd.addRoleToMember(event.getMember(), cargoMinecraft).queue();
                event.reply("Acesso Liberado!").setEphemeral(true).queue();
            }
            else {
                ygd.removeRoleFromMember(event.getMember(), cargoMinecraft).queue();
                event.reply("Acesso Removido!").setEphemeral(true).queue();

            }
        }
    }
}

package app.configs;

import app.commands.mannagers.MessageReceived;
import app.commands.mannagers.SlashManager;
import app.commands.prefix.bot.Prefix;
import app.commands.prefix.misc.*;
import app.commands.prefix.mod.cargos.AddCargo;
import app.commands.prefix.mod.cargos.Cargo;
import app.commands.prefix.mod.cargos.RemoveCargo;
import app.commands.prefix.mod.mensagens.Clear;
import app.events.bot.OnReady;
import app.events.bot.SelfMention;
import app.events.guild.messages.MessageDelete;
import app.events.guild.messages.MessageEdited;
import app.events.guild.voice.VoiceJoin;
import app.events.guild.voice.VoiceLeft;
import app.events.guild.voice.VoiceSelfMute;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.File;
import java.util.EnumSet;
import java.util.List;

public class Intialize {
    public static JDA bot;
    public static String prefixo;
    @JsonProperty("prefixos")
    public static List<String> prefixos;

    public static void inicializarBot(){
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            ObjectNode config = objectMapper.readValue(new File(("config.json")), ObjectNode.class);
            bot = JDABuilder.create(config.get("token").asText(), EnumSet.allOf(GatewayIntent.class)).build();
            inicializaEventos();
            incializaComandosSlash();
            inicializaComandosPrefixo();
            prefixo = config.get("prefixo").asText();



            System.out.println("Prefixo definido: " + prefixo);
            System.out.println("Listeners incializados:");
            for (Object listener: bot.getRegisteredListeners()
            ) {
                if (listener.toString().toLowerCase().startsWith("evento")){
                    System.out.println("\u001B[38;2;0;191;255m" + listener.toString() + "\u001B[0m");
                }
                else if (listener.toString().toLowerCase().startsWith("manager")){
                    System.out.println("\u001B[31m" + listener.toString() + "\u001B[0m");
                }
                else if (listener.toString().toLowerCase().startsWith("comando")){
                    System.out.println("\u001B[32m"+ listener.toString() + "\u001B[0m");
                }
                else {
                    System.out.println(listener.toString());
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void inicializaEventos(){
        bot.addEventListener(new MessageReceived());
        bot.addEventListener(new OnReady());
        bot.addEventListener(new SelfMention());
        bot.addEventListener(new VoiceJoin());
        bot.addEventListener(new VoiceLeft());
        bot.addEventListener(new VoiceSelfMute());
        bot.addEventListener(new MessageEdited());
        bot.addEventListener(new MessageDelete());
    }


    protected static void inicializaComandosPrefixo(){
        bot.addEventListener(new Afk());
        bot.addEventListener(new Roleta());
        bot.addEventListener(new Soco());
        bot.addEventListener(new UserInfo());
        bot.addEventListener(new Tempo());
        bot.addEventListener(new Cargo());
        bot.addEventListener(new AddCargo());
        bot.addEventListener(new RemoveCargo());
        bot.addEventListener(new Clear());
        bot.addEventListener(new Prefix());
    }

    protected static void incializaComandosSlash(){
        SlashManager manager = new SlashManager();
        bot.addEventListener(manager);
    }



}

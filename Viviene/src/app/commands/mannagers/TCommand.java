package app.commands.mannagers;


import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TCommand extends ListenerAdapter {

    private String[] mensagem;
    private Member author;
    private Member member;
    MessageReceivedEvent event;


    public String[] getMensagem() {
        return mensagem;
    }

    public void setMensagem(String[] mensagem) {
        this.mensagem = mensagem;
    }

    public Member getAuthor() {
        return author;
    }

    public void setAuthor(Member author) {
        this.author = author;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public MessageReceivedEvent getEvent() {
        return event;
    }

    public void setEvent(MessageReceivedEvent event) {
        this.event = event;
    }

    public TCommand(){
        setEvent(event);
        setMensagem(event.getMessage().getContentRaw().split(" "));
        setAuthor(event.getMember());
    }
}

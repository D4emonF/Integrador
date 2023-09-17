package app.commands.slash.misc;

import net.dv8tion.jda.api.entities.Message;



public class SorteioRunnable implements Runnable {
    private final Message msg;

    public SorteioRunnable(Message msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        // Chame o método sortear() usando a mensagem fornecida
        Sorteio.sortear(msg);
    }
}

package app.protecoes;

public class ActionCounter {
    private int actionCount;
    private long lastActionTimestamp;

    public ActionCounter() {
        this.actionCount = 0;
        this.lastActionTimestamp = System.currentTimeMillis();
    }

    public void incrementAction() {
        actionCount++;
    }

    public boolean isWithinTimeLimit() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastActionTimestamp) <= 120000; // 2 minutos em milissegundos
    }

    public void reset() {
        actionCount = 0;
        lastActionTimestamp = System.currentTimeMillis();
    }

    public int getActionCount() {
        return actionCount;
    }
}

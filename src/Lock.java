public class Lock{

    private boolean isLocked = false;

    public synchronized void lock() {
        isLocked = true;
        while(isLocked){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void unlock(){
        isLocked = false;
        notify();
    }
}
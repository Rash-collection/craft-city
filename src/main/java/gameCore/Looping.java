/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gameCore;

/**
 *
 * @author rash4
 */
public class Looping implements Runnable{
    final static double NANOS = 1_000_000_000.0d;
    Looping(){
        this.running = false;
    }
    public void toggle(){
        if(this.running)this.stops();
        else            this.start();
    }
    public boolean isRunning(){return this.running;}
    public synchronized void start() {
        if (this.running) return;
        this.running = true;
        this.runner = new Thread(this, "Loop");
        this.runner.start();
    }
    public synchronized void stops() {
        if (!this.running) return;
        this.running = false;
        if (Thread.currentThread() != this.runner) {
            try {
                this.runner.join();
            } catch (InterruptedException ignored) {}
        }
    }
    @Override public void run(){
        final double timePerFrame = NANOS / Generals.fps;
        final double timePerUpdate = NANOS / Generals.ups;
        long previousTime = System.nanoTime();
        long lastCheck = System.currentTimeMillis();
        double deltaU = 0;
        double deltaF = 0;
        int realFPS = 0;
        int realUPS = 0;
        long currentTime;
        try{while(this.running){
            deltaF += ((currentTime = System.nanoTime()) - previousTime) / timePerFrame;
            deltaU += (currentTime - previousTime) / timePerUpdate;
            previousTime = currentTime;
            // for the UPDATE
            while(deltaU >= 1){// to handle lags XD
                Generals.update();
                realUPS++;
                deltaU--;
            }
            // for the REPAINT
            if(deltaF >= 1){
                Generals.render();
                realFPS++;
                deltaF--;
            }
            if(System.currentTimeMillis() - lastCheck >= 1000){
                lastCheck = System.currentTimeMillis();
                final String updates = "FPS: " + realFPS + " || UPS: " + realUPS;
//                if(Launcher.appendLoop)Console.VIRTUAL_CONSOLE.appendl(updates);
                System.out.println(updates);
                realFPS = realUPS = 0;
            }
            Thread.yield();
        }}catch(Exception ex){
            this.stops();
            System.out.println(">> Due to Thread-loop exception, the loop has been forcefully STOPPED.");
            System.out.println("> SOME EXCEPTION :\n\t>>" + ex.getMessage());
            ex.printStackTrace();
        }catch(Error er){
            System.out.println("> SEVER ERROR :\n\t>>" + er.getMessage());
            er.printStackTrace();
            System.exit(0); // force an exit.
        }
    }
    private volatile boolean running;
    private Thread runner;
}
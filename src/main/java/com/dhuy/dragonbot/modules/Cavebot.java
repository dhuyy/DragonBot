package com.dhuy.dragonbot.modules;

public class Cavebot implements Runnable {
	private String name = null;
	 
    public Cavebot(String name) {
        this.name = name;
    }
 
    public String getName() {
        return this.name;
    }
    
	public void run() {
		try {
			Thread.sleep(500);
			
			System.out.println("Code that process Cavebot responsibility.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(name + " module execution has been finished.");
	}
}

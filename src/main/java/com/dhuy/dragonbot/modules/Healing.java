package com.dhuy.dragonbot.modules;

public class Healing implements Runnable {
	private String name = null;
	 
    public Healing(String name) {
        this.name = name;
    }
 
    public String getName() {
        return this.name;
    }
    
	public void run() {
		try {
			Thread.sleep(1000);
			
			System.out.println("Code that process Healing responsibility.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(name + " module execution has been finished.");
	}
}

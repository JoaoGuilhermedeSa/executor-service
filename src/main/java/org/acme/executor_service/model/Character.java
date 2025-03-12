package org.acme.executor_service.model;

public class Character {
    private final String name;
    private final String role;
    private boolean isStunned;
    private int health;
    private int damage;
    private double critChance;
    private double blockChance;

    public Character(String name, String role) {
        this.name = name;
        this.role = role;
        this.isStunned = false;
        this.health = 100; // Todos começam com 100 de vida
        this.damage = 10;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
    
    public boolean isStunned() {
        return isStunned;
    }
    
    public int getHealth() {
        return health;
    }
    
    public void stun() {
        this.isStunned = true;
    }
    
    public void heal() {
        this.isStunned = false;
        this.health = Math.min(100, this.health + 20);
    }
    
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            System.out.println(name + " foi derrotado!");
        }
    }
    
    public boolean attemptBlock(double val) {
        return val + blockChance < 0.2;
    }
    
    public boolean attemptCritical(double val) {
        return val + critChance < 0.15;
    }
    
    public boolean isAlive() {
        return health > 0;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public void applyDamageBuff() {
    	this.damage *= 1.1;
    }
    
    public void applyCriticalBuff() {
    	this.critChance += 0.1;
    }
    
    public void applyBlockBuff() {
    	this.blockChance += 0.1;
    }
    
}
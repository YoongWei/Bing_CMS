package com.ntu.cz3003.CMS.models;

public class User {
    private String name;
    private int rewards;
    private String email;

    public User() {
    }

    public String getName() {
        return name;
    }

    public int getRewards() {
        return rewards;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRewards(int rewards) {
        this.rewards = rewards;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

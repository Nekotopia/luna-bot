package com.nekotopia;

import com.nekotopia.config.Config;
import com.nekotopia.config.Listener;
import com.nekotopia.database.DatabaseManager;
import com.nekotopia.database.SQLiteDataSource;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Bot {

    private Bot() throws LoginException {
        DatabaseManager.INSTANCE.getPrefix(-1);
        JDABuilder.createDefault(
                Config.get("token"),
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.GUILD_EMOJIS
        )
                .addEventListeners(new Listener())
                .setActivity(Activity.watching("kittens"))
                .build();

    }
    public static void main(String[] args) throws LoginException {
        new Bot();
    }
}

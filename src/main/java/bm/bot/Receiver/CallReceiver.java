package bm.bot.Receiver;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface CallReceiver
{
    void receiveCall( GuildMessageReceivedEvent aEvent );
}

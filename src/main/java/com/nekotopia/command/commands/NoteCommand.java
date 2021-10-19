package com.nekotopia.command.commands;

import com.nekotopia.command.CommandContext;
import com.nekotopia.command.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NoteCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();
        StringBuilder note = new StringBuilder();
        if (args.isEmpty() || args.size() < 3) {
            channel.sendMessage("Missing args").queue();
            return;
        }
        for (int i = 0; i < args.size()-2; i++) {
            note.append(args.get(i)).append(" ");
        }
        long timeArgs = Long.parseLong(args.get(args.size() - 1));
        String when = args.get(args.size() - 2).toUpperCase();

        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        Runnable task = () -> channel.sendMessageFormat(
                member.getAsMention() + " here's your note nya~\n" + "`" + note + "`"
        ).queue();
        switch (when) {
            case "IN":
                ses.schedule(task, timeArgs, TimeUnit.SECONDS);
                break;
            case "AT":
                channel.sendMessage("Lunatic note : haven't done it yet ^^'").queue();
                return;
            default:
                channel.sendMessage("No IN or AT").queue();
                return;
        }
        channel.sendMessageFormat("Ok, I will remind you nya~").queue();
        ses.shutdown();
    }

    @Override
    public String getName() {
        return "note";
    }

    @Override
    public String getHelp() {
        return "Remind you to do something\n" +
                "Usage: `!!note <note> IN/AT <time>\n" +
                "(IN is in seconds)" +
                "(AT exemple : 22:33)`";
    }
}

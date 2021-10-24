package com.nekotopia.command.commands;

import com.nekotopia.command.CommandContext;
import com.nekotopia.command.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NoteCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();
        int timeArgPos;
        boolean isAnIn;
        StringBuilder note = new StringBuilder();
        if (args.isEmpty() || args.size() < 3) {
            channel.sendMessage("Missing args").queue();
            return;
        }
        if (args.get(args.size() - 3).equalsIgnoreCase("IN")) {
            timeArgPos = 2;
            isAnIn = true;
        }
        else if (args.get(args.size() - 2).equalsIgnoreCase("AT")) {
            timeArgPos = 1;
            isAnIn = false;
        }
        else{
            channel.sendMessage("No IN or AT").queue();
            return;
        }
        for (int i = 0; i < args.size()-(timeArgPos+1); i++) {
            note.append(args.get(i)).append(" ");
        }
        note.trimToSize();
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        Runnable task = () -> channel.sendMessageFormat(
                member.getAsMention() + " here's your note nya~\n" + "`" + note.toString().trim() + "`"
        ).queue();
        if (isAnIn) {
            try {
                long time = Long.parseLong(args.get(args.size() - 2));
                switch(args.get(args.size() - 1).toUpperCase()) {
                    case "S":
                        ses.schedule(task, time, TimeUnit.SECONDS);
                        break;
                    case "M":
                        ses.schedule(task, time, TimeUnit.MINUTES);
                        break;
                    case "H":
                        ses.schedule(task, time, TimeUnit.HOURS);
                        break;
                    case "D":
                        ses.schedule(task, time, TimeUnit.DAYS);
                        break;
                    default:
                        channel.sendMessage("Last character not S, M, H or D").queue();
                }
            } catch (Exception e) {
                channel.sendMessage("An error occured").queue();
                channel.sendMessage(args.toString() + args.size()).queue();
                return;
            }
        } else {
            channel.sendMessage("Lunatic note : haven't done it yet ^^'").queue();
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
                "Usage: `!!note <note> IN <time> (S/H/D)\n" +
                "(IN example : !!note task IN 5 H)" +
                "(AT example : !!note task AT 22:33)`";
    }
}

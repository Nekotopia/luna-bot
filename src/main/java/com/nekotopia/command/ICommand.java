package com.nekotopia.command;

import java.util.Arrays;
import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx);

    String getName();

    String getHelp();

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    default List<String> getAliases(){
        return Arrays.asList();
    }
}

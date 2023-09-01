package org.betonquest.betonquest.quest.event.sudo;

import java.util.List;

public class Command {

    public String command;
    public List<String> variables;

    public Command(String command, List<String> variables) {
        this.command = command;
        this.variables = variables;
    }

    public List<String> getVariables() {
        return variables;
    }

    public String getCommand() {
        return command;
    }
}

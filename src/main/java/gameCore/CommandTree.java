/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gameCore;

import java.util.HashMap;

/**
 *
 * @author rash4
 */
public final class CommandTree implements Com{ 
    public CommandTree(){this.CMDS = new HashMap<>();}
    public CommandTree setCom(String name, Com com){
        if(name == null || com == null){}
        else if(com instanceof Command single){
            if(single.com() == null)return this;
            this.CMDS.putIfAbsent(name, single);
        }else if(com instanceof CommandTree list){
            if(list.CMDS.isEmpty())return this;
            this.CMDS.putIfAbsent(name, list);
        }
        return this;
    }
    public void listHelp(String prefix) {
        final var cons = Generals.console();
        // List the direct command for this SubCommand if it exists
        if (this.direct != null) {
            cons.appendl(prefix + "Direct command: " + this.direct.getClass().getSimpleName());
        }

        // List subcommands within this SubCommand
        for (String key : this.CMDS.keySet()) {
            Com com = this.CMDS.get(key);
            switch (com) {
                case CommandTree commandTree -> {
                    // If it's a SubCommand, recursively list its help
                    cons.appendl(prefix + "--" + key + " (SubCommand):");
                    commandTree.listHelp(prefix + "  ");
                }
                case Command cmd -> // If it's a Command, just print it out
                    cons.appendl(prefix + "--" + key + " (Command)");
            }
        }
    }
    public boolean hasDirectCom(){return this.direct != null;}
    @Override public boolean hasParam(){return true;}
    public CommandTree setDirectCom(Command com){
        this.direct = com;
        return this;
    }
    public String[] getList(){return this.CMDS.keySet().toArray(String[]::new);}
    public Com getCom(String command){return this.CMDS.get(command);}
    public Command directCommand(){return this.direct;}
    private Command direct;
    private final HashMap<String, Com> CMDS;
}
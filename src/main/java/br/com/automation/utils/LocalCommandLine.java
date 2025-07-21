package br.com.automation.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class LocalCommandLine {

    final ArrayList<String> commands = new ArrayList<String>();;
    private static final Logger log = Logger.getLogger(LocalCommandLine.class.getName());
    public void executeCommand(final String command) throws IOException{

        if(isWindows()) {
            commands.add("cmd.exe");
            commands.add("/c");
        } else if(isUnix()) {
            commands.add("/bin/bash");
            commands.add("-c");
        } else {
            commands.add("/bin/bash");
            commands.add("-c");
        }

        commands.add(command);
        BufferedReader br = null;

        try {
            final ProcessBuilder p = new ProcessBuilder(commands);
            final Process process = p.start();
            final InputStream is = process.getInputStream();
            final InputStreamReader isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            System.out.println("Command Line Execution = [" + command + "]");

            String line;
            while((line = br.readLine()) != null) {
                System.out.println("Command return = [" + line + "]");
            }


        } catch (IOException ioe) {
            log.severe("Error Executing Shell Command" + ioe.getMessage());
            throw ioe;
        } finally {
            secureClose(br);
        }
    }

    private void secureClose(final Closeable resource) {
        try {
            if (resource != null) {
                resource.close();
            }
        } catch (IOException ex) {
            log.severe("Error = " + ex.getMessage());
        }
    }

    public static void main (String[] args) throws IOException {
        final LocalCommandLine shell = new LocalCommandLine();
        shell.executeCommand("ping 8.8.8.8");
    }

    // windows
    public static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("win") >= 0);
    }

    // Mac
    public static boolean isMac() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("mac") >= 0);
    }

    // linux or unix
    public static boolean isUnix() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
    }

    // Solaris
    public static boolean isSolaris() {
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf("sunos") >= 0);
    }
}

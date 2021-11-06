package org.fleamarket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProjectMethods extends Thread {
    @Override
    public void run() {
        try {
            waitCommand();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void waitCommand() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String command = bufferedReader.readLine();
        exec(command);

        waitCommand();
    }

    void exec(String command)
    {
        if(command.equals("stop"))
        {
            System.exit(1);
        }
    }
}

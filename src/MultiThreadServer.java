import com.sun.org.apache.xpath.internal.operations.Mult;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MultiThreadServer
{
    private static final int PORT = 1234;
    private static ServerSocket serverSocket;
    private static List<ClientHandler> handlers = new ArrayList<>();

    public static void main(String [] args) throws IOException
    {
        try
        {
            serverSocket = new ServerSocket(PORT);
        }
        catch(IOException ioEx)
        {
            System.out.println("\nJ_ER IOException : Unable to set up port");
            System.exit(1);
        }

        while(true)
        {
            //Listen on port and wait for client
            Socket client = serverSocket.accept();

            System.out.println("\nJ_OK\n");

            //Create a thread to handle communication with
            //this client and pass the constructor for this
            //thread a reference to the relevant socket
            ClientHandler handler = new ClientHandler(client);

            handlers.add(handler);

            Thread thread = new Thread(handler);

            thread.start();


        }

    }

    private static class ClientHandler implements Runnable
    {
        private Socket client;
        private Scanner input;
        private PrintWriter output;

        public ClientHandler(Socket socket)
        {
            //Set up reference to associated socket
            this.client = socket;

            try
            {
                this.input = new Scanner(client.getInputStream());

                this.output = new PrintWriter(
                        client.getOutputStream(), true);
            }
            catch (IOException ioEx)
            {
                ioEx.printStackTrace();
            }
        }

        @Override
        public void run()
        {
            String received;

            do
            {
                //Accept message from client
                //on the socket's input stream
                received = input.nextLine();

                //Echo message back to client
                //on the socket's output stream
                for (ClientHandler handler : MultiThreadServer.handlers)
                {
                    handler.output.println("DATA " + received);
                }

                //Repeat until client types exit message
            }
            while(!received.equals("QUIT"));

            try
            {
                if(client != null)
                {
                    System.out.println("QUIT");

                    client.close();
                }
            }
            catch (IOException ioEx)
            {
                System.out.println("Unable to disconnect!");
            }
        }
    }
}

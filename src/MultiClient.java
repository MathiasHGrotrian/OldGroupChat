import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MultiClient implements ObserverInterface
{
    private static InetAddress host;
    private static final int PORT = 1234;

    public static void main(String[] args)
    {
        try
        {
            host = InetAddress.getLocalHost();
        }
        catch (UnknownHostException uhEx)
        {
            System.out.println("\nHost ID not found\n");

            System.exit(1);
        }

        sendMessages();
    }

    private static void sendMessages()
    {
        Socket socket = null;

        try
        {
            socket = new Socket(host, PORT);

            Scanner networkInput =
                    new Scanner(socket.getInputStream());

            PrintWriter networkOutput =
            new PrintWriter(socket.getOutputStream(), true);

            //Set up stream for keyboard entry
            Scanner userEntry = new Scanner(System.in);

            String message, response;

            do
            {
                System.out.println("Type 'QUIT' to exit");

                message = userEntry.nextLine();

                //Send message to the server
                //on the socket's output stream
                networkOutput.println(message);

                //Accept response from the server
                //on the socket's input stream
                response = networkInput.nextLine();

                //Display server's response to clients
                System.out.println("\n" + response);
            }
            while(!message.equals("QUIT"));
        }
        catch (IOException ioEx)
        {
            ioEx.printStackTrace();
        }

        finally
        {
            try
            {
                System.out.println("\nQUIT");

                socket.close();
            }
            catch (IOException ioEx)
            {
                System.out.println("J_ER IOException : Unable to disconnect");

                System.exit(1);
            }
        }
    }

    public void update()
    {

    }
}
